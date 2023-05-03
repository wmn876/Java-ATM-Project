package com.uni.atm.domain;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)

@Entity
public class Card {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 16, nullable = false, unique = true)
    private String cardNo;

    @Column(nullable = false)
    private CardType cardType;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(length = 4, nullable = false)
    private String pin;

    @ManyToOne
    private Customer customer;

    @OneToOne
    private Account account;
}
