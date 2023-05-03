package com.uni.atm.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 17, nullable = false, unique = true)
    private String accountNo;
    @ManyToOne
    private Customer customer;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false)
    private AccountType accountType;

    @OneToOne
    private Card card;

}
