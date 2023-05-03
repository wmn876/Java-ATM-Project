package com.uni.atm.domain;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 100)
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "customer")
    private List<Card> cards;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;
}
