package com.skillstorm.project_one.dtos.transactionmerchant;

import java.util.List;

import com.skillstorm.project_one.dtos.transaction.TransactionDTO;

public record TransactionWithMerchantsDTO(
    TransactionDTO transaction,
    List<MerchantDetailedDTO> merchants
) {

}
