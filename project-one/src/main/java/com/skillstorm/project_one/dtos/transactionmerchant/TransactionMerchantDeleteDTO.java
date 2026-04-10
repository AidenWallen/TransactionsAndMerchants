package com.skillstorm.project_one.dtos.transactionmerchant;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public record TransactionMerchantDeleteDTO(
    Long merchantId,
    Long transactionId,
    TransactionMerchantRole role
) {

}
