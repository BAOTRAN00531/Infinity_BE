package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lexicon_sense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LexiconSense {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;  // ✅ INT

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lexicon_unit_id")
  private LexiconUnit lexiconUnit;

  private String pos;
  private String ipa;

  @Column(name = "gloss_vi", length = 512)
  private String glossVi;

  @Column(name = "gloss_en", length = 512)
  private String glossEn;

  @Lob @Column(name = "examples_json")
  private String examplesJson;

  @Lob @Column(name = "collocations_json")
  private String collocationsJson;

  @Column(name = "audio_url")
  private String audioUrl;

  private Double confidence;
  private String status; // proposed/approved

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
}