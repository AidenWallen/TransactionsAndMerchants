package com.skillstorm.project_one.dtos.transactionmerchant;

import java.time.Instant;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record TransactionMerchantLinkDTO(
    Long merchantId,
    Long transactionId,
    TransactionMerchantRole role,
    Instant date,
    String notes
) {
}
