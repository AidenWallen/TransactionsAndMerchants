package com.skillstorm.project_one.validators;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class Validator<T> {

    // check if object exists
    public void isEmpty(Object obj, String objectName) {
        if(obj == null)
            throw new IllegalArgumentException(objectName+" can not be null");
    }

    // check valid string (only [a-zA-Z ]+)
    private void isEmptyString(String s, String propertyName) {
        isEmpty(s, propertyName);
        if(s.isBlank())
            throw new IllegalArgumentException(propertyName+" can not be empty");
    }

    private void isValidChars(String s, String propertyName) {
        if (!s.matches("[a-zA-Z ]+"))
            throw new IllegalArgumentException(s+" must be valid chars");
    }

    public void isValidString(String s, String propertyName) {
        isEmptyString(s, propertyName);
        isValidChars(s, propertyName);
    }

    // check valid amount for money
    public void isValidAmount(BigDecimal amount, String propertyName){
        isEmpty(amount, propertyName);
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(propertyName+" can not be less than or equal to 0");
    }

    // check valid date
    public void isValidDate(Instant date){
        isEmpty(date, "Transaction.date");
        if(date.getNano() != 0)
            throw new IllegalArgumentException("Transaction.date must be truncated to seconds");
    }

    // check valid Id
    public void isValidId(Long id, String objectName){ 
        isEmpty(id, objectName);
        if(id<=0)
            throw new IllegalArgumentException(objectName+" can not be less than or equal to zero");
    }

    public abstract void isValid(T obj);
}
