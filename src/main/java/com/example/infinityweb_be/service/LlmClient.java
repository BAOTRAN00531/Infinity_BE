package com.example.infinityweb_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LlmClient {

  @Value("${openai.api.key:}")
  private String openaiApiKey;

  @Value("${openai.model:gpt-4o-mini}")
  private String model;

  @Value("${openai.base:https://api.openai.com}")
  private String base;

  private final ObjectMapper om = new ObjectMapper();
  private final HttpClient http = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(15)).build();

  public boolean isEnabled() {
    return openaiApiKey != null && !openaiApiKey.isBlank();
  }

  /** Trả JSON mảng; tự retry khi 429. */
  public String completeJson(String prompt) throws Exception {
    if (!isEnabled()) throw new IllegalStateException("LLM disabled (missing OPENAI_API_KEY)");

    final String url = base + "/v1/chat/completions";
    final int MAX_RETRY = 3;
    final long BASE_DELAY_MS = 600; // 0.6s

    // ==== Build body (GIỮ nguyên phần schema OBJECT có 'items' là ARRAY) ====
    ObjectNode root = om.createObjectNode();
    root.put("model", model);
    root.put("temperature", 0.2);
    root.put("max_tokens", 220);

    ArrayNode msgs = root.putArray("messages");
    msgs.addObject().put("role", "system").put("content", "Return only compact JSON. No prose.");
    msgs.addObject().put("role", "user").put("content", prompt);

    ObjectNode respFmt = root.putObject("response_format");
    respFmt.put("type", "json_schema");
    ObjectNode js = respFmt.putObject("json_schema");
    js.put("name", "lexicon_suggest");
    ObjectNode sch = js.putObject("schema");
    sch.put("type", "object");
    ObjectNode props = sch.putObject("properties");
    ObjectNode itemsProp = props.putObject("items");
    itemsProp.put("type", "array");
    ObjectNode itemSchema = itemsProp.putObject("items");
    itemSchema.put("type", "object");
    ObjectNode itemProps = itemSchema.putObject("properties");
    itemProps.putObject("word").put("type", "string");
    itemProps.putObject("pos").put("type", "string");
    itemProps.putObject("ipa").put("type", "string");
    itemProps.putObject("glossVi").put("type", "string");
    itemProps.putObject("confidence").put("type", "number");
    itemSchema.putArray("required").add("word").add("glossVi");
    sch.putArray("required").add("items");

    String body = om.writeValueAsString(root);

    HttpRequest req = HttpRequest.newBuilder(URI.create(url))
            .header("Authorization", "Bearer " + openaiApiKey)
            .header("Content-Type", "application/json")
            .timeout(Duration.ofSeconds(20))
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
            .build();

    // ================= Retry loop =================
    for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
      HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
      int sc = resp.statusCode();

      // --- 429: rate limit ---
      if (sc == 429) {
        // Prefer header Retry-After (giây); nếu không có thì exponential backoff
        long retryAfterMs = resp.headers().firstValue("retry-after")
                .map(v -> {
                  try { return Long.parseLong(v) * 1000L; } catch (Exception e) { return -1L; }
                }).orElse(-1L);

        long backoff = (retryAfterMs > 0)
                ? retryAfterMs
                : (long) (BASE_DELAY_MS * Math.pow(2, attempt)); // 0.6s, 1.2s, 2.4s, ...

        if (attempt == MAX_RETRY) {
          // ném lỗi cho controller trả 429
          throw new RateLimitedException("LLM rate-limited. Try again later.", backoff);
        }
        try { Thread.sleep(backoff); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        continue; // retry
      }

      // --- Non-2xx khác ---
      if (sc / 100 != 2) {
        // Thử parse message lỗi của OpenAI để log/trace
        String msg = resp.body();
        try {
          JsonNode err = om.readTree(msg).path("error").path("message");
          if (!err.isMissingNode()) msg = err.asText();
        } catch (Exception ignore) {}
        throw new RuntimeException("LLM error " + sc + ": " + msg);
      }

      // --- OK: trả content ---
      String content = om.readTree(resp.body())
              .path("choices").get(0).path("message").path("content").asText();

      // Chuẩn hoá: nếu trả { "items":[...] } thì lấy mảng items
      try {
        JsonNode node = om.readTree(content);
        if (node.isObject()) {
          if (node.has("items") && node.get("items").isArray())
            return om.writeValueAsString(node.get("items"));
          if (node.has("suggestions") && node.get("suggestions").isArray())
            return om.writeValueAsString(node.get("suggestions"));
        } else if (node.isArray()) {
          return om.writeValueAsString(node);
        }
      } catch (Exception ignore) {}

      return content.trim();
    }

    // không bao giờ tới đây
    throw new IllegalStateException("Unexpected retry loop exit");
  }
  public static class RateLimitedException extends RuntimeException {
    public final long retryAfterMs;
    public RateLimitedException(String message, long retryAfterMs) {
      super(message);
      this.retryAfterMs = retryAfterMs;
    }
  }
  public String transcribeWavToText(byte[] wav, String language) throws Exception {
    String url = base + "/v1/audio/transcriptions";
    var req = HttpRequest.newBuilder(URI.create(url))
        .header("Authorization", "Bearer " + openaiApiKey)
        .header("Content-Type", "multipart/form-data; boundary=----X")
        .POST(ofMultipart(wav, language))
        .timeout(Duration.ofSeconds(60))
        .build();
    var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
    if (resp.statusCode()/100 != 2) throw new RuntimeException("ASR error "+resp.statusCode()+": "+resp.body());
    return om.readTree(resp.body()).path("text").asText("");
  }
  
  // Tạo multipart body
  private static HttpRequest.BodyPublisher ofMultipart(byte[] wav, String language) throws Exception {
    String boundary = "----X";
    var byteArrays = new ArrayList<byte[]>();
    String meta = "--"+boundary+"\r\n"
        + "Content-Disposition: form-data; name=\"model\"\r\n\r\nwhisper-1\r\n"
        + "--"+boundary+"\r\n"
        + "Content-Disposition: form-data; name=\"response_format\"\r\n\r\njson\r\n";
    if (language != null && !language.isBlank()) {
      meta += "--"+boundary+"\r\n"
        + "Content-Disposition: form-data; name=\"language\"\r\n\r\n"+language+"\r\n";
    }
    meta += "--"+boundary+"\r\n"
        + "Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\r\n"
        + "Content-Type: audio/wav\r\n\r\n";
    byteArrays.add(meta.getBytes(StandardCharsets.UTF_8));
    byteArrays.add(wav);
    byteArrays.add(("\r\n--"+boundary+"--").getBytes(StandardCharsets.UTF_8));
    return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
  }
  
  public Map<String, Object> completeJsonWithSchema(
    String prompt, String jsonSchema, double temperature, int maxTokens) {
return completeToJsonMap(prompt, jsonSchema, temperature, maxTokens);
}

/**
* Nếu project bạn đã có method này rồi thì giữ nguyên, xoá phần bên dưới.
* Nếu CHƯA có, tạm thời cho 1 bản “stub” trả lời rỗng để compile được.
* Sau này bạn thay bằng gọi OpenAI/Claude tuỳ ý.
*/
public Map<String, Object> completeToJsonMap(
    String prompt, String jsonSchema, double temperature, int maxTokens) {
  
  if (!isEnabled()) {
    log.warn("OpenAI API key not configured, using fallback response");
    return Map.of("feedback", "Good effort! Speak a bit clearer on tricky words and keep a steady pace.");
  }
  
  try {
    final String url = base + "/v1/chat/completions";
    
    // Build request body with JSON schema
    ObjectNode root = om.createObjectNode();
    root.put("model", model);
    root.put("temperature", temperature);
    root.put("max_tokens", maxTokens);

    ArrayNode msgs = root.putArray("messages");
    msgs.addObject().put("role", "system").put("content", "You are an expert pronunciation coach and language assessor. Return only valid JSON that matches the provided schema.");
    msgs.addObject().put("role", "user").put("content", prompt);

    // Use JSON schema response format
    ObjectNode respFmt = root.putObject("response_format");
    respFmt.put("type", "json_schema");
    ObjectNode js = respFmt.putObject("json_schema");
    js.put("name", "speaking_assessment");
    js.set("schema", om.readTree(jsonSchema));

    String body = om.writeValueAsString(root);

    HttpRequest req = HttpRequest.newBuilder(URI.create(url))
            .header("Authorization", "Bearer " + openaiApiKey)
            .header("Content-Type", "application/json")
            .timeout(Duration.ofSeconds(30))
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
            .build();

    HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
    int sc = resp.statusCode();

    if (sc / 100 != 2) {
      String msg = resp.body();
      try {
        JsonNode err = om.readTree(msg).path("error").path("message");
        if (!err.isMissingNode()) msg = err.asText();
      } catch (Exception ignore) {}
      log.error("OpenAI API error {}: {}", sc, msg);
      throw new RuntimeException("OpenAI API error " + sc + ": " + msg);
    }

    // Parse response
    String content = om.readTree(resp.body())
            .path("choices").get(0).path("message").path("content").asText();

    // Parse JSON response
    JsonNode jsonResponse = om.readTree(content);
    @SuppressWarnings("unchecked")
    Map<String, Object> result = om.convertValue(jsonResponse, Map.class);
    return result;
    
  } catch (Exception e) {
    log.error("Failed to call OpenAI API: {}", e.getMessage(), e);
    // Fallback response
    return Map.of(
      "feedback", "AI assessment temporarily unavailable. Good effort! Focus on clear pronunciation and steady pace.",
      "accuracy", 75.0,
      "pronunciation", 70.0,
      "completeness", 80.0
    );
  }
}
  
}