package com.uni.atm.service;

import com.uni.atm.domain.Account;
import com.uni.atm.exception.InvalidRequestException;
import com.uni.atm.repo.AccountRepo;
import com.uni.atm.requests.TransactionRequest;
import org.springframework.stereotype.Service;

import com.uni.atm.domain.Card;
import com.uni.atm.exception.AuthFailedException;
import com.uni.atm.exception.ResourceNotExistException;
import com.uni.atm.repo.CardRepo;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service

public class CardService {

    private final CardRepo cardRepo;
    private final AccountRepo accountRepo;

    public CardService(CardRepo cardRepo, AccountRepo accountRepo) {
        this.cardRepo = cardRepo;
        this.accountRepo = accountRepo;
    }

    public Card fetchCardByCardNo(String cardNo) {
        return cardRepo.findByCardNo(cardNo).orElseThrow(() -> new ResourceNotExistException("Card Not Found"));
    }

    public Card validateCard(String cardNo, String pin) {
        Card card = fetchCardByCardNo(cardNo);
        if (card.pin().equals(pin)) {
            return card;
        } else {
            throw new AuthFailedException("Invalid Pin");
        }
    }

    public BigDecimal performTransaction(TransactionRequest transactionRequest) {
        Card card = validateCard(transactionRequest.getCardNo(), transactionRequest.getPin());
        switch (transactionRequest.getTransactionType()) {
            case BALANCE_INQUIRY:
                return handleBalanceInquiry(card, transactionRequest);
            case DEPOSIT:
                return handleDeposit(card, transactionRequest);
            case WITHDRAW:
                return handleWithDraw(card, transactionRequest);
            default:
                throw new InvalidRequestException("Unsupported transaction type");

        }
    }

    private BigDecimal handleBalanceInquiry(Card card, TransactionRequest transactionRequest) {
        return card.account().balance();
    }

    @Transactional
    private BigDecimal handleDeposit(Card card, TransactionRequest transactionRequest) {
        if (null == transactionRequest.getAmount() || transactionRequest.getAmount().equals(BigDecimal.ZERO))
            throw new InvalidRequestException("For Deposit: Amount Can't be empty");
        Account account = card.account();
        synchronized (this) {
            BigDecimal currentBalance = account.balance();
            BigDecimal updatedBalance = currentBalance.add(transactionRequest.getAmount());
            account.balance(updatedBalance);
            accountRepo.save(account);
        }
        return card.account().balance();
    }

    @Transactional
    private BigDecimal handleWithDraw(Card card, TransactionRequest transactionRequest) {
        if (null == transactionRequest.getAmount() || transactionRequest.getAmount().equals(BigDecimal.ZERO))
            throw new InvalidRequestException("For Withdraw: Amount Can't be empty");
        Account account = card.account();
        synchronized (this) {
            BigDecimal currentBalance = account.balance();
            if (currentBalance.compareTo(transactionRequest.getAmount()) < 0) {
                throw new InvalidRequestException("Insufficient Balance");
            }
            BigDecimal updatedBalance = currentBalance.subtract(transactionRequest.getAmount());
            account.balance(updatedBalance);
            accountRepo.save(account);
        }
        return card.account().balance();
    }
}
