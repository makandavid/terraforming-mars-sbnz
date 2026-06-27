package com.tm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "card_synergies")
@Getter
@Setter
public class CardSynergyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "required_tag", nullable = false)
    private String requiredTag;

    @Column(nullable = false)
    private int threshold;

    @Column(nullable = false)
    private String priority;

    @Column(name = "effect_text", nullable = false)
    private String effectText;

}
