package com.skillstorm.project_one;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.project_one.dtos.merchant.MerchantUpdateDTO;
import com.skillstorm.project_one.dtos.merchant.MerchantViewDTO;
import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;
import com.skillstorm.project_one.models.transaction.Currencies;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;
import com.skillstorm.project_one.services.MerchantService;
import com.skillstorm.project_one.services.TransactionMerchantService;
import com.skillstorm.project_one.services.TransactionService;
import com.skillstorm.project_one.validators.MerchantValidator;

@SpringBootTest
class MerchantTests {
	@Autowired
	private MerchantService mService;
	@Autowired
	private TransactionService tService;
	@Autowired
	private TransactionMerchantService tmService;

	MerchantValidator merchantValidator;

	@BeforeEach
	public void setUp() {
		merchantValidator = new MerchantValidator();
	}

	// Merchant Validator
	// Check if Merchant Exists
	@Test
	@DisplayName("Throws when Merchant is null")
	public void testIsNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(null);
		});
	}

	@Test
	@DisplayName("Assert DoesNotThrow when Merchant exists")
	public void testExists() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertDoesNotThrow(() -> {
			merchantValidator.isValid(merchant);
		});
	}

	// Check if Merchant names are valid

	@Test
	@DisplayName("Throws when Merchant.firstName is null")
	public void testFirstNameIsNull() {
		Merchant merchant = new Merchant(null, "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@Test
	@DisplayName("Throws when Merchant.lastName is null")
	public void testLastameIsNull() {
		Merchant merchant = new Merchant("Aiden", null, MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })
	@DisplayName("Throws when Merchant.firstName is empty")
	public void testFirstNameIsEmpty(String firstName) {
		Merchant merchant = new Merchant(firstName, "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })
	@DisplayName("Throws when Merchant.lastName is empty")
	public void testLastNameIsEmpty(String lastName) {
		Merchant merchant = new Merchant("Aiden", lastName, MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "@!den", "A1d3n" })
	@DisplayName("Throws when Merchant.firstName is non-valid chars")
	public void testFirstNameIsNonValidChars(String firstName) {
		Merchant merchant = new Merchant(firstName, "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "W#((en", "Wa445n" })
	@DisplayName("Throws when Merchant.firstName is non-valid chars")
	public void testLastNameIsNonValidChars(String lastName) {
		Merchant merchant = new Merchant("Aiden", lastName, MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@Test
	@DisplayName("Assert DoesNotThrow when names are valid")
	public void testValidNames() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertDoesNotThrow(() -> {
			merchantValidator.isValid(merchant);
		});
	}

	// Check if Merchant categories are valid
	@Test
	@DisplayName("Throws when Merchant.categorie is null")
	public void testCategorieIsNull() {
		Merchant merchant = new Merchant("Aiden", "Wallen", null, "United States", MerchantStatuses.ACTIVE);
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@Test
	@DisplayName("Assert DoesNotThrow when Merchant.categorie is valid")
	public void testValidCategorie() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertDoesNotThrow(() -> {
			merchantValidator.isValid(merchant);
		});
	}

	// Check if Merchant countries are valid
	@Test
	@DisplayName("Throws when Merchant.country is null")
	public void testCountryIsNull() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, null,
				MerchantStatuses.ACTIVE);
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })
	@DisplayName("Throws when Merchant.country is empty")
	public void testCountryIsEmpty(String country) {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, country,
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "Un1t3d States", "Ynited St@tes" })
	@DisplayName("Throws when Merchant.country is non-valid chars")
	public void testCountryIsNonValidChars(String country) {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, country,
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@Test
	@DisplayName("Assert DoesNotThrow when country is valid")
	public void testValidCountry() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertDoesNotThrow(() -> {
			merchantValidator.isValid(merchant);
		});
	}

	// Check if Merchant statuses are valid
	@Test
	@DisplayName("Throws when Merchant status is null")
	public void testStatusIsNull() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States", null);
		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValid(merchant);
		});
	}

	@Test
	@DisplayName("Assert DoesNotThrow when Merchant status is valid")
	public void testValidStatus() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		assertDoesNotThrow(() -> {
			merchantValidator.isValid(merchant);
		});
	}

	// add()
	@Test
	@DisplayName("Throws when Merchant.add() tries to save an invalid Merchant")
	public void testAddInvalid() {
		Merchant merchant = new Merchant(null, "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			mService.add(merchant);
		});
	}

	@Test
	@DisplayName("Throws when Merchant.id is null")
	public void testIdIsNull() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValidWithId(merchant);
		});
	}

	@Test
	@DisplayName("Throws when Merchant.id is less than or equal to 0")
	public void testIdIsInvalid() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		merchant.setId(0L);

		assertThrows(IllegalArgumentException.class, () -> {
			merchantValidator.isValidWithId(merchant);
		});
	}

	@Test
	@DisplayName("Merchant.add() saves and returns a valid Merchant with a valid Merchant.id")
	public void testAdd() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);

		Merchant savedMerchant = mService.add(merchant);

		assertDoesNotThrow(() -> {
			merchantValidator.isValidWithId(savedMerchant);
		});
	}

	// view()
	@Test
	@DisplayName("Merchant.view() returns all merchants and their transaction count, volume, and other key metrics")
	public void testView() {
		// Create two Merchants and add each Merchant to the database
		Merchant merchant1 = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		Merchant merchant2 = new Merchant("John", "Smith", MerchantCategories.TRAVEL, "United Kingdom",
				MerchantStatuses.SUSPENDED);
		mService.add(merchant1);
		mService.add(merchant2);

		// Create four Transactions and add each Transaction to the database
		Transaction transaction1 = new Transaction(
				TransactionTypes.PURCHASE, new BigDecimal("10.35"), Currencies.USD, Instant.now(),
				TransactionStatuses.SETTLED, "", null);
		Transaction transaction3 = new Transaction(
				TransactionTypes.PURCHASE, new BigDecimal("400.00"), Currencies.USD, Instant.now(),
				TransactionStatuses.SETTLED, "", null);
		Transaction transaction4 = new Transaction(
				TransactionTypes.TRANSFER_OUT, new BigDecimal("173.88"), Currencies.USD, Instant.now(),
				TransactionStatuses.FAILED, "", null);
		Transaction transaction5 = new Transaction(
				TransactionTypes.PURCHASE, new BigDecimal("200.00"), Currencies.USD, Instant.now(),
				TransactionStatuses.FAILED, "", null);
		tService.add(transaction1);

		tService.update(1L, TransactionStatuses.DISPUTED, null);
		tService.reverse(1L, TransactionTypes.CHARGEBACK, null);
		tService.add(transaction3);
		tService.add(transaction4);
		tService.add(transaction5);

		// Link each Merchant to Two Transactions in the database
		tmService.link(1L, 1L, TransactionMerchantRole.PAYEE, Instant.now(), "");
		tmService.link(1L, 2L, TransactionMerchantRole.REFUND_ISSUER, Instant.now(), "");
		tmService.link(2L, 3L, TransactionMerchantRole.PAYEE, Instant.now(), "");
		tmService.link(2L, 4L, TransactionMerchantRole.PAYMENT_PROCESSOR, Instant.now(), "");
		tmService.link(2L, 5L, TransactionMerchantRole.REFUND_ISSUER, Instant.now(), "");

		tService.delete(5L);

		// Use mService.view() to get a list of all Merchants and their Transactions in
		// the database
		List<MerchantViewDTO> dtos = mService.view();
		MerchantViewDTO dto1 = dtos.get(0);
		MerchantViewDTO dto2 = dtos.get(1);

		// Assert each MerchantViewDTO object contains expected values
		assertEquals(dto1.firstName(), "Aiden");
		assertEquals(dto1.lastName(), "Wallen");
		assertEquals(dto1.transactionCount(), 2);
		assertEquals(dto1.transactionVolume(), new BigDecimal("0"));

		assertEquals(dto2.firstName(), "John");
		assertEquals(dto2.lastName(), "Smith");
		assertEquals(dto2.transactionCount(), 2);
		assertEquals(dto2.transactionVolume(), new BigDecimal("400.00"));
	}

	// edit()
	@Test
	@DisplayName("Merchant.edit() is invalid for null id")
	public void testEditNullId() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(MerchantStatuses.ACTIVE, MerchantCategories.FOOD_AND_BEVERAGE,
				"");
		assertThrows(NoSuchElementException.class, () -> {
			mService.edit(null, updateDTO);
		});
	}

	@ParameterizedTest
	@ValueSource(longs = { 0L, -10000000000L })
	@DisplayName("Merchant.edit() is invalid for invalid id")
	public void testEditInvalidId(Long id) {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(MerchantStatuses.ACTIVE, MerchantCategories.FOOD_AND_BEVERAGE,
				"");
		assertThrows(NoSuchElementException.class, () -> {
			mService.edit(id, updateDTO);
		});
	}

	@Test
	@DisplayName("Merchant.service edit is valid for category change")
	public void testEditCategory() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(null, MerchantCategories.TRAVEL, null);
		Merchant editMerchant = mService.edit(1L, updateDTO);
		assertDoesNotThrow(() -> {
			merchantValidator.isValidWithId(merchant);
		});
		assertEquals(editMerchant.getCategory(), MerchantCategories.TRAVEL);
	}

	@ParameterizedTest
	@ValueSource(strings = { "A!d@n", "1232ade Kingdom" })
	@DisplayName("Merchant.service edit throws for invalid country change")
	public void testEditInvalidCountry(String country) {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(MerchantStatuses.ACTIVE, MerchantCategories.FOOD_AND_BEVERAGE,
				country);
		assertThrows(IllegalArgumentException.class, () -> {
			mService.edit(1L, updateDTO);
		});
	}

	@Test
	@DisplayName("Merchant.country edit is valid for country change")
	public void testEditCountry() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(null, null, "United Kingdom");
		Merchant editMerchant = mService.edit(1L, updateDTO);
		assertDoesNotThrow(() -> {
			merchantValidator.isValidWithId(merchant);
		});
		assertEquals(editMerchant.getCountry(), "United Kingdom");
	}

	@Test
	@DisplayName("Merchant.status edit is valid for status change")
	public void testEditStatus() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		MerchantUpdateDTO updateDTO = new MerchantUpdateDTO(MerchantStatuses.SUSPENDED, null, null);
		Merchant editMerchant = mService.edit(1L, updateDTO);
		assertDoesNotThrow(() -> {
			merchantValidator.isValidWithId(merchant);
		});
		assertEquals(editMerchant.getStatus(), MerchantStatuses.SUSPENDED);
	}

	// delete()
	@Test
	@DisplayName("Testing Merchant.add() and Merchant.delete()")
	public void testDelete() {
		Merchant merchant = new Merchant("Aiden", "Wallen", MerchantCategories.RETAIL, "United States",
				MerchantStatuses.ACTIVE);
		mService.add(merchant);

		assertEquals(mService.view().size(), 1);
		assertDoesNotThrow(() -> {
			mService.delete(1L);
		});
		assertTrue(mService.view().isEmpty());
	}
}
