package com.skillstorm.project_one;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;
import com.skillstorm.project_one.models.transaction.Currencies;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;
import com.skillstorm.project_one.services.MerchantService;
import com.skillstorm.project_one.services.TransactionMerchantService;
import com.skillstorm.project_one.services.TransactionService;
import com.skillstorm.project_one.validators.TransactionMerchantValidator;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class TransactionMerchantTests {

    @Autowired
    private MerchantService mService;
    @Autowired
    private TransactionService tService;
    @Autowired
    private TransactionMerchantService tmService;

    TransactionMerchantValidator transactionMerchantValidator;

    Merchant merchant;
    Transaction transaction;

    @BeforeEach
    public void setUp() {
        transactionMerchantValidator = new TransactionMerchantValidator();
        merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
                MerchantStatuses.ACTIVE);
        transaction = new Transaction(TransactionTypes.PURCHASE, new BigDecimal(1000000), Currencies.USD,
                Instant.now(), TransactionStatuses.SETTLED, "", null);

        mService.add(merchant);
        tService.add(transaction);
    }

    // TransactionMerchant Validator
    // Check if TransactionMerchant, Merchant, and Transaction exists
    @Test
    @DisplayName("Throws when TransactionMerchant is null")
    public void testIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValid(null);
        });
    }

    @Test
    @DisplayName("Throws when Merchant is null or invalid")
    public void testMerchantIsNull() {
        Merchant merchant = null;
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    @Test
    @DisplayName("Throws when Transaction is null or invalid")
    public void testTransactionIsNull() {
        Transaction transaction = null;
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when TransactionMerchant, Merchant, and Transaction exists")
    public void testExists() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertDoesNotThrow(() -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    // date
    @Test
    @DisplayName("Throws when TransactionMerchant.date is null")
    public void testInstantIsNull() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, null, "");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    @Test
    @DisplayName("TransactionMerchant.date can not be set to any more specific time than seconds")
    public void testInstantIsSeconds() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValidDate(Instant.now());
        });
        transactionMerchant.setDate(Instant.now());
        assertDoesNotThrow(() -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when TransactionMerchant.date is valid")
    public void testValidInstant() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertDoesNotThrow(() -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    // role
    @Test
    @DisplayName("Throws when TransactionMerchant.categorie is null")
    public void testCategorieIsNull() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                null, Instant.now(), "");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    @Test
    @DisplayName("Assert DoesNotThrow when TransactionMerchant.categorie is valid")
    public void testValidCategorie() {
        TransactionMerchant transactionMerchant = new TransactionMerchant(merchant, transaction,
                TransactionMerchantRole.PAYEE, Instant.now(), "");

        assertDoesNotThrow(() -> {
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    // link()
    @Test
    @DisplayName("TransactionMerchant.link() can not take null params")
    public void testLinkNull() {
        assertThrows(ConstraintViolationException.class, () -> {
            tmService.link(1L, 1L, null, Instant.now(), "");
        });
    }

    @Test
    @DisplayName("TransactionMerchant.link() can not link softDeleted or non existent Transactions and Merchants")
    public void testLinkDeleted() {
        assertDoesNotThrow(() -> {
            tService.delete(1L);
        });
        assertThrows(NoSuchElementException.class, () -> {
            tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
        });
    }

    @Test
    @DisplayName("TransactionMerchant.link() successfully connects an active transaction to an active merchant")
    public void testLink() {
        assertDoesNotThrow(() -> {
            TransactionMerchant transactionMerchant = tmService.link(1L, 1L, TransactionMerchantRole.PAYEE,
                    Instant.now(), "");
            transactionMerchantValidator.isValid(transactionMerchant);
        });
    }

    // // edit()
    // @Test
    // @DisplayName("TransactionMerchant.edit() is invalid for null id")
    // public void testEditNullId() {
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");

    //     assertThrows(InvalidDataAccessApiUsageException.class, () -> {
    //         tmService.edit(null, TransactionMerchantRole.PAYEE, Instant.now(), "Buying cookies");
    //     });
    // }

    // @ParameterizedTest
    // @ValueSource(longs = { 0L, -10000000000L })
    // @DisplayName("TransactionMerchant.edit() is invalid for invalid id")
    // public void testEditInvalidId(Long id) {
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");

    //     assertThrows(NoSuchElementException.class, () -> {
    //         tmService.edit(id, TransactionMerchantRole.PAYEE, Instant.now(), "Buying cookies");
    //     });
    // }

    // @Test
    // @DisplayName("TransactionMerchant.role edit is valid for role change")
    // public void testEditRole() {
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");

    //     TransactionMerchant editTransactionMerchant = tmService.edit(1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
    //     assertEquals(editTransactionMerchant.getRole(), TransactionMerchantRole.PAYEE);
    // }

    // @Test
    // @DisplayName("TransactionMerchant.date edit is valid for date change")
    // public void testEditDate() {
    //     Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, time, "");

    //     TransactionMerchant editTransactionMerchant = tmService.edit(1L, TransactionMerchantRole.PAYEE, time.minusSeconds(10), "");
    //     assertEquals(editTransactionMerchant.getDate(), time.minusSeconds(10));
    //     assertDoesNotThrow(() -> {
    //         transactionMerchantValidator.isValid(editTransactionMerchant);
    //     });
    // }

    // @Test
    // @DisplayName("TransactionMerchant.notes edit is valid for notes change")
    // public void testEditNotes() {
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");

    //     TransactionMerchant editTransactionMerchant = tmService.edit(1L, TransactionMerchantRole.PAYEE, Instant.now(), "Buying cookies");
    //     assertEquals(editTransactionMerchant.getNotes(), "Buying cookies");
    }

    // remove()
    // @Test
    // @DisplayName("TransactionMerchant.delete() is successful")
    // public void testDelete(){
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
    //     assertNotNull(tmService.findById(1L));      
    //     tmService.remove(1L);
    //     assertThrows(NoSuchElementException.class, () -> {
    //         assertNull(tmService.findById(1L));
    //     });

    // }

    // // viewMerchant()
    // @Test
    // @DisplayName("TransactionMerchant.viewMerchant() is successful")
    // public void testViewMerchant(){
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
    //     List<MerchantDetailedDTO> merchants = tmService.viewMerchants(1L);

    //     assertEquals(MerchantCategories.RETAIL, merchants.get(0).category());
    // }

    // // viewTransaction()
    // @Test
    // @DisplayName("TransactionMerchant.viewTransaction() is successful")
    // public void testViewTransaction(){
    //     tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
    //     List<TransactionDetailedDTO> transactions = tmService.viewTransactions(1L);

    //     assertTrue(new BigDecimal(1000000).compareTo(transactions.get(0).amount()) == 0);
    // }
// }
