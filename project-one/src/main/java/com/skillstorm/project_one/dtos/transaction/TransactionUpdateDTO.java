package com.skillstorm.project_one.dtos.transaction;

import com.skillstorm.project_one.models.transaction.TransactionStatuses;

public record TransactionUpdateDTO(
    Long id,
    TransactionStatuses status,
    String notes
) {

}
