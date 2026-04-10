package com.skillstorm.project_one.dtos.transactionmerchant;

import java.time.Instant;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record TransactionMerchantDTO(
        Long id,
        Long merchantId,
        Long transactionId,
        TransactionMerchantRole role,
        Instant date,
        String notes) {

    public TransactionMerchantDTO(TransactionMerchant tm){
        this(tm.getId(), 
        tm.getMerchant().getId(), 
        tm.getTransaction().getId(), 
        tm.getRole(), 
        tm.getDate(), 
        tm.getNotes());
    }
}
