package com.skillstorm.project_one.validators;

import com.skillstorm.project_one.models.merchant.Merchant;

public class MerchantValidator extends Validator<Merchant>{

    // Main valid checking method
    @Override
    public void isValid(Merchant merchant){
        isEmpty(merchant, "Merchant");
        isValidString(merchant.getFirstName(), "Merchant.firstName");
        isValidString(merchant.getLastName(), "Merchant.lastName");
        isEmpty(merchant.getCategory(), "Merchant.category");
        isValidString(merchant.getCountry(), "Merchant.country");
        isEmpty(merchant.getStatus(), "Merchant.status");
    }

    public void isValidWithId(Merchant merchant){
        isValid(merchant);
        isValidId(merchant.getId(), "Merchant.id");
    }
}
