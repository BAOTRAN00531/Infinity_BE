package com.example.infinityweb_be.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Question_Payloads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionPayload {

    @Id
    @Column(name = "question_id")
    private Integer questionId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    @Column(name = "payload_json", columnDefinition = "NVARCHAR(MAX)")
    private String payloadJson;
}
