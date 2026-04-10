package com.skillstorm.project_one.dtos.merchant;

import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;

public record MerchantDTO(
    Long id,
    String firstName,
    String lastName,
    MerchantCategories category,
    String country,
    MerchantStatuses status
) {
    public MerchantDTO(Merchant merchant){
        this(merchant.getId(), 
            merchant.getFirstName(), 
            merchant.getLastName(), 
            merchant.getCategory(), 
            merchant.getCountry(), 
            merchant.getStatus()
        );
    }
}
