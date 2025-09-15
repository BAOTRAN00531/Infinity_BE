package com.example.infinityweb_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

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
}