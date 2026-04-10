package com.skillstorm.project_one.dtos.transactionmerchant;

import java.time.Instant;

import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record MerchantDetailedDTO(
    Long id,
    String firstName,
    String lastName,
    MerchantCategories category,
    MerchantStatuses status,
    TransactionMerchantRole role,
    Instant date
) {

    public MerchantDetailedDTO(TransactionMerchant transactionMerchant){
        this(
            transactionMerchant.getMerchant().getId(),
            transactionMerchant.getMerchant().getFirstName(),
            transactionMerchant.getMerchant().getLastName(),
            transactionMerchant.getMerchant().getCategory(), 
            transactionMerchant.getMerchant().getStatus(), 
            transactionMerchant.getRole(), 
            transactionMerchant.getDate()
        );
    }
}
