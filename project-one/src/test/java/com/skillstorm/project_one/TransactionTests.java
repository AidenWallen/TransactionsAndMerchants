package com.skillstorm.project_one;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.project_one.dtos.transaction.TransactionDTO;
import com.skillstorm.project_one.dtos.transaction.TransactionFilterDTO;
import com.skillstorm.project_one.models.transaction.Currencies;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.services.TransactionService;
import com.skillstorm.project_one.validators.TransactionValidator;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class TransactionTests {

    @Autowired
    private TransactionService tService;

    TransactionValidator transactionValidator;

    @BeforeEach
    public void setUp() {
        transactionValidator = new TransactionValidator();
    }

    // Transaction Validator
    // Check if Transaction exists
    @Test
    @DisplayName("Throws when Transaction is null")
    public void testIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(null);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction exists")
    public void testExists() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // type
    @Test
    @DisplayName("Throws when Transaction.type is null")
    public void testTypeIsNull() {
        Transaction transaction = new Transaction(null, new BigDecimal(1000000), Currencies.USD, Instant.now(),
                TransactionStatuses.SETTLED, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction.type is valid")
    public void testValidType() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // amount
    @ParameterizedTest
    @ValueSource(strings = { "0", "-10000000000.56" })
    @DisplayName("Throws when Transaction.amount less than or equal to 0")
    public void testInvalidAmount(String amount) {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(amount), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction.amount is valid")
    public void testValidAmount() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal("10000000000.99"),
                Currencies.USD, Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // currency
    @Test
    @DisplayName("Throws when Transaction.currency is null")
    public void testCurrencyIsNull() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), null,
                Instant.now(), TransactionStatuses.SETTLED, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction.currency is valid")
    public void testValidCurrency() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // date
    @Test
    @DisplayName("Throws when Transaction.date is null")
    public void testInstantIsNull() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                null, TransactionStatuses.SETTLED, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Transaction.date can not be set to any more specific time than seconds")
    public void testInstantIsSeconds() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                null, TransactionStatuses.SETTLED, "", null);
        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValidDate(Instant.now());
        });
        transaction.setDate(Instant.now());
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction.date is valid")
    public void testValidInstant() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // status
    @Test
    @DisplayName("Throws when Transaction.status is null")
    public void testStatusIsNull() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), null, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValid(transaction);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when Transaction.status is valid")
    public void testValidStatus() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        assertDoesNotThrow(() -> {
            transactionValidator.isValid(transaction);
        });
    }

    // group
    @Test
    @DisplayName("Test group creation")
    public void testGroup(){
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        Transaction savedTransaction = tService.add(transaction);

        Transaction transaction2 = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", savedTransaction.getGroup());
        Transaction savedTransaction2 = tService.add(transaction2);

        assertNotNull(savedTransaction.getGroup().getId());
        assertEquals(savedTransaction.getGroup().getId(), savedTransaction2.getGroup().getId());
    }

    // add()
    @Test
    @DisplayName("Throws when Transaction.add() tries to save a null Transaction")
    public void testAddNull() {
        Transaction transaction = null;
        ;

        assertThrows(IllegalArgumentException.class, () -> {
            tService.add(transaction);
        });
    }

    @Test
    @DisplayName("Throws when Transaction.add() tries to save an invalid Transaction")
    public void testAddInvalid() {
        Transaction transaction = new Transaction(null, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);

        assertThrows(ConstraintViolationException.class, () -> {
            tService.add(transaction);
        });
    }

    @Test
    @DisplayName("Throws when Transaction.id is null")
    public void testIdIsNull() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValidWithId(transaction);
        });
    }

    @Test
    @DisplayName("Throws when Transaction.id is less than or equal to 0")
    public void testIdIsInvalid() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        transaction.setId(0L);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionValidator.isValidWithId(transaction);
        });
    }

    @Test
    @DisplayName("Transaction.id is not Null")
    public void testIdNotNull() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        Transaction savedTransaction = tService.add(transaction);

        assertDoesNotThrow(() -> {
            transactionValidator.isValidWithId(savedTransaction);
        });
    }

    // update()
    @Test
    @DisplayName("Transaction.update() is invalid for null id")
    public void testUpdateNullId() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.PENDING, "", null);
        tService.add(transaction);

        assertThrows(NoSuchElementException.class, () -> {
            tService.update(null, TransactionStatuses.FAILED, null);
        });
    }

    @ParameterizedTest
    @ValueSource(longs = { 0L, -10000000000L })
    @DisplayName("Transaction.update() is invalid for invalid id")
    public void testUpdateInvalidId(Long id) {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.PENDING, "", null);
        tService.add(transaction);

        assertThrows(NoSuchElementException.class, () -> {
            tService.update(id, TransactionStatuses.FAILED, null);
        });
    }

    @ParameterizedTest
    @EnumSource(value = TransactionStatuses.class, names = { "SETTLED", "DISPUTED", "FAILED" })
    @DisplayName("Transaction.update() can not update status of SETTLED, DISPUTED, or FAILED Transactions to invalid states")
    public void testUpdateImmutable(TransactionStatuses status) {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), status, "", null);
        tService.add(transaction);

        assertThrows(UnsupportedOperationException.class, () -> {
            tService.update(1L, TransactionStatuses.PENDING, null);
        });
    }

    @Test
    @DisplayName("Transaction.service update is valid for status change")
    public void testUpdateStatus() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.PENDING, "", null);
        tService.add(transaction);

        Transaction updateTransaction = tService.update(1L, TransactionStatuses.FAILED, "");
        assertEquals(updateTransaction.getStatus(), TransactionStatuses.FAILED);
    }

    @Test
    @DisplayName("Transaction.notes update is valid for notes change")
    public void testUpdateNotes() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.PENDING, "", null);
        tService.add(transaction);

        Transaction updateTransaction = tService.update(1L, null, "hello");
        assertEquals(updateTransaction.getNotes(), "hello");
    }

    // delete() (implement soft and update view)
    @Test
    @DisplayName("Testing Transaction.add() and Transaction.delete()")
    public void testDelete() {
        Transaction transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);
        tService.add(transaction);

        assertEquals(tService.view(1L).id(), 1L);
        assertDoesNotThrow(() -> {
            tService.delete(1L);
        });
        assertThrows(NoSuchElementException.class, () -> {
            tService.view(1L);
        });
    }

    // view()
    @Test
    @DisplayName("Testing Transaction.view() for different filtering options")
    public void testView() {
        // database was auto truncating Instant to seconds causing inconsistencies
        // between the application Instant and database timestamp
        // now every Instant used in Transaction is automatically truncated to seconds
        // in the application to create consistency
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant start = now.minusSeconds(10);
        Instant end = now.plusSeconds(10);

        Transaction transaction1 = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, start,
                TransactionStatuses.SETTLED, "", null);
        Transaction transaction3 = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("400.00"), Currencies.USD, now,
                TransactionStatuses.PENDING, "", null);
        Transaction transaction4 = new Transaction(
                TransactionTypes.TRANSFER_OUT, new BigDecimal("173.88"), Currencies.USD, end,
                TransactionStatuses.SETTLED, "", null);

        tService.add(transaction1);

        tService.update(1L, TransactionStatuses.DISPUTED, null);
        tService.reverse(1L, TransactionTypes.REFUND, null);
        tService.add(transaction3);
        tService.add(transaction4);
        tService.reverse(4L, TransactionTypes.TRANSFER_IN, null);

        TransactionDTO test1 = tService.view(1L);

        TransactionFilterDTO filter1 = new TransactionFilterDTO(TransactionTypes.PURCHASE, null, null, null);
        TransactionFilterDTO filter2 = new TransactionFilterDTO(null, TransactionStatuses.SETTLED, null, null);
        List<TransactionDTO> test2 = tService.view(filter1);
        List<TransactionDTO> test3 = tService.view(filter2);

        TransactionFilterDTO filter3 = new TransactionFilterDTO(null, null, start, end);
        TransactionFilterDTO filter4 = new TransactionFilterDTO(TransactionTypes.TRANSFER_OUT, TransactionStatuses.SETTLED, start, end);
        tService.delete(1L);
        List<TransactionDTO> test4 = tService.view(filter3);
        List<TransactionDTO> test5 = tService.view(filter4);


        assertEquals(test1.amount(), transaction1.getAmount());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 1);
        assertEquals(test4.size(), 2);
        assertEquals(test5.size(), 1);
    }

    // reverse
    @Test
    @DisplayName("Throws when trying to do an invalid reversal")
    public void testInvalidReverse(){
        Transaction transaction = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
                TransactionStatuses.DISPUTED, "", null);
        Transaction invalidTransaction = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
                TransactionStatuses.SETTLED, "", null);
        tService.add(transaction);
        tService.add(invalidTransaction);

        assertThrows(UnsupportedOperationException.class, () -> {
            tService.reverse(1L, TransactionTypes.TRANSFER_OUT, "");
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            tService.reverse(2L, TransactionTypes.REFUND, "");
        });
    }

    @Test
    @DisplayName("Throws when trying to do an reversal while pending transaction")
    public void testReverseWhilePending(){
        Transaction transaction = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
                TransactionStatuses.DISPUTED, "", null);
        tService.add(transaction);
        tService.reverse(1L, TransactionTypes.REFUND, "");

        assertThrows(UnsupportedOperationException.class, () -> {
            tService.reverse(1L, TransactionTypes.REFUND, "");
        });
    }

    @Test
    @DisplayName("Throws when trying to do an reversal twice")
    public void testReverseTwice(){
        Transaction transaction = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
                TransactionStatuses.DISPUTED, "", null);
        tService.add(transaction);
        tService.reverse(1L, TransactionTypes.REFUND, "");
        tService.update(2L, TransactionStatuses.SETTLED, "");

        assertThrows(UnsupportedOperationException.class, () -> {
            tService.reverse(1L, TransactionTypes.REFUND, "");
        });
    }

    @Test
    @DisplayName("Does Not Throw when trying to do an valid reversal")
    public void testValidReverse(){
        Transaction transaction = new Transaction(
                TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
                TransactionStatuses.DISPUTED, "", null);
            
        tService.add(transaction);

        
        assertDoesNotThrow(()->{
            tService.reverse(1L, TransactionTypes.REFUND, "");
        });
        tService.update(2L, TransactionStatuses.FAILED, "");
        assertDoesNotThrow(()->{
            tService.reverse(1L, TransactionTypes.REFUND, "");
        });

        Long countTransactions = tService.countTransactionsByGroupId(1L);
        assertEquals(countTransactions, 3);
    }


}
