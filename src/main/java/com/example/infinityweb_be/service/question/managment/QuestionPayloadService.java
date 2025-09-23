package com.example.infinityweb_be.service.question.managment;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionPayload;
import com.example.infinityweb_be.repository.question.managment.QuestionPayloadRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionPayloadService {

    private final QuestionPayloadRepository payloadRepository;
    private final ObjectMapper objectMapper;

    private String write(Map<String, Object> payload) {
        try {
            return payload == null ? null : objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> read(String json) {
        try {
            return json == null ? null : objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void upsert(Question question, Map<String, Object> payload) {
        var opt = payloadRepository.findByQuestionId(question.getId());
        var json = write(payload);
        if (opt.isPresent()) {
            var ent = opt.get();
            ent.setPayloadJson(json);
            payloadRepository.save(ent); // UPDATE
        } else {
            var ent = QuestionPayload.builder()
                    .question(question)         // CHỈ set question
                    // .questionId(question.getId())  // <- BỎ DÒNG NÀY
                    .payloadJson(json)
                    .build();
            payloadRepository.save(ent);  // INSERT (vì id đang null)
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> load(Integer questionId) {
        return payloadRepository.findByQuestionId(questionId)
                .map(QuestionPayload::getPayloadJson)
                .map(this::read)
                .orElse(null);
    }
}
