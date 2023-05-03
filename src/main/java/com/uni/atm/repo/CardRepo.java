package com.uni.atm.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.uni.atm.domain.Card;


public interface CardRepo extends CrudRepository<Card, Long> {

    public Optional<Card> findByCardNo(String cardNo);
}
