package com.example.infinityweb_be.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// PhraseTokenMap.java
@Entity
@Table(name = "phrase_token_map")
public class PhraseTokenMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="phrase_id") private Long phraseId;
    @Column(name="token_start") private Integer tokenStart;
    @Column(name="token_end") private Integer tokenEnd;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "lexicon_unit_id")
    private LexiconUnit lexiconUnit;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "sense_id")
    private LexiconSense sense;

    @Column(name="gloss_vi", length=512) private String glossVi;
    private String ipa;
    private String audioUrl;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
