package com.skillstorm.project_one.dtos.transactionmerchant;

import java.time.Instant;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record TransactionMerchantUpdateDTO(
    Long merchantId,
    Long transactionId,
    TransactionMerchantRole fromRole,
    TransactionMerchantRole toRole,
    Instant date,
    String notes
) {
}
