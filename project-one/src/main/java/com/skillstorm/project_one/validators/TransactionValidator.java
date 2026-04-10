package com.skillstorm.project_one.validators;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.skillstorm.project_one.models.transaction.Transaction;

public class TransactionValidator extends Validator<Transaction>{

    public static Instant validateDate(Instant date){
        return date!=null? date.truncatedTo(ChronoUnit.SECONDS):null;
    }

    @Override
    public void isValid(Transaction transaction) {
        isEmpty(transaction, "Transaction");
        isEmpty(transaction.getType(), "Transaction.type");
        isValidAmount(transaction.getAmount(), "Transaction.amount");
        isEmpty(transaction.getCurrency(), "Transaction.currency");
        isValidDate(transaction.getDate());
        isEmpty(transaction.getStatus(), "Transaction.status");
    }
    
    public void isValidWithId(Transaction transaction){
        isValid(transaction);
        isValidId(transaction.getId(), "Transaction.id");
    }
}
