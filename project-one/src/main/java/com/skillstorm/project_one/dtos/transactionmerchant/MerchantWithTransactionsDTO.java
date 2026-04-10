package com.skillstorm.project_one.dtos.transactionmerchant;

import java.util.List;

import com.skillstorm.project_one.dtos.merchant.MerchantDTO;

public record MerchantWithTransactionsDTO(
    MerchantDTO merchant,
    List<TransactionDetailedDTO> transactions
) {

}
