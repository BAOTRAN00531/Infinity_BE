package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Phrase_Lexicon_Map")
@Getter
@Setter
public class PhraseLexiconMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="phrase_id", nullable=false)
    private Long phraseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lexicon_id", nullable=false)
    private LexiconUnit lexiconUnit;

    @Column(name="[order]", nullable=false)
    private Integer order;   // vị trí trong cụm
}