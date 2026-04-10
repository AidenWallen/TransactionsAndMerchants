package com.skillstorm.project_one.dtos.merchant;

import java.math.BigDecimal;

public record MerchantViewDTO(
    Long id,
    String firstName,
    String lastName,
    Long transactionCount,
    BigDecimal transactionVolume
) {
    
}
