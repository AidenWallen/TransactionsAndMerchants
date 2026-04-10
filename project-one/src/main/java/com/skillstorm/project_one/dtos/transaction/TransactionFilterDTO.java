package com.skillstorm.project_one.dtos.transaction;

import java.time.Instant;

import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;

public record TransactionFilterDTO(
        TransactionTypes type,
        TransactionStatuses status,
        Instant start,
        Instant end
) {

}
