package com.skillstorm.project_one.dtos.transaction;

import java.math.BigDecimal;
import java.time.Instant;

import com.skillstorm.project_one.models.transaction.Currencies;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;

public record TransactionDTO(
        Long id,
        TransactionTypes type,
        BigDecimal amount,
        Currencies currency,
        Instant date,
        TransactionStatuses status,
        String notes
    ) {

    public TransactionDTO(Transaction transaction) {
        this(   
            transaction.getId(), 
            transaction.getType(), 
            transaction.getAmount(), 
            transaction.getCurrency(),
            transaction.getDate(), 
            transaction.getStatus(),
            transaction.getNotes()
        );
    }

}
