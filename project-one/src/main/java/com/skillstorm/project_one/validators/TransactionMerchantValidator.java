package com.skillstorm.project_one.validators;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;

public class TransactionMerchantValidator extends Validator<TransactionMerchant>{

    private TransactionValidator transactionValidator;
    private MerchantValidator merchantValidator;

    public TransactionMerchantValidator() {
        transactionValidator = new TransactionValidator();
        merchantValidator = new MerchantValidator();
    }

    @Override
    public void isValid(TransactionMerchant transactionMerchant){
        isEmpty(transactionMerchant, "TransactionMerchant");
        transactionValidator.isValid(transactionMerchant.getTransaction());
        merchantValidator.isValid(transactionMerchant.getMerchant());
        isValidDate(transactionMerchant.getDate());
        isEmpty(transactionMerchant.getRole(), "Transaction.role");
    }
}
