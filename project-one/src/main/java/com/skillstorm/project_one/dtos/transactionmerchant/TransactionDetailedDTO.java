package com.skillstorm.project_one.dtos.transactionmerchant;

import java.math.BigDecimal;
import java.time.Instant;

import com.skillstorm.project_one.models.transaction.Currencies;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record TransactionDetailedDTO(
        Long id,
        TransactionTypes type,
        BigDecimal amount,
        Currencies currency,
        TransactionStatuses status,
        TransactionMerchantRole role,
        Instant processingDate,
        String processingNotes
    ) {

    public TransactionDetailedDTO(TransactionMerchant tm) {
        this(   
            tm.getTransaction().getId(), 
            tm.getTransaction().getType(), 
            tm.getTransaction().getAmount(), 
            tm.getTransaction().getCurrency(), 
            tm.getTransaction().getStatus(),
            tm.getRole(),
            tm.getDate(),
            tm.getNotes()
        );
    }

}
