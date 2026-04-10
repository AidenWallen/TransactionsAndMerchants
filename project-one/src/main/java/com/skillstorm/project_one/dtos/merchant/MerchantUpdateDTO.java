package com.skillstorm.project_one.dtos.merchant;

import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;

public record MerchantUpdateDTO(
    MerchantStatuses status,
    MerchantCategories category,
    String country
) {
}
