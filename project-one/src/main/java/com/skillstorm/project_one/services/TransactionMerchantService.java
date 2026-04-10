package com.skillstorm.project_one.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skillstorm.project_one.dtos.merchant.MerchantDTO;
import com.skillstorm.project_one.dtos.transaction.TransactionDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.MerchantDetailedDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.MerchantWithTransactionsDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionDetailedDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionWithMerchantsDTO;
import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;
import com.skillstorm.project_one.repositories.MerchantRepository;
import com.skillstorm.project_one.repositories.TransactionMerchantRepository;
import com.skillstorm.project_one.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionMerchantService {

    private final TransactionMerchantRepository repository;
    private final MerchantRepository mRepository;
    private final TransactionRepository tRepository;

    TransactionMerchantService(TransactionMerchantRepository repository, MerchantRepository mRepository,
            TransactionRepository tRepository) {
        this.repository = repository;
        this.mRepository = mRepository;
        this.tRepository = tRepository;
    }

    public TransactionMerchant findById(Long id) {
        Optional<TransactionMerchant> tm = repository.findById(id);
        if (tm.isPresent()) {
            return tm.get();
        }
        throw new NoSuchElementException("There is no TransactionMerchant with that id");
    }

    @Transactional
    public TransactionMerchant link(Long merchantId, Long transactionId, TransactionMerchantRole role,
            Instant processingDate, String notes) {
        if (repository.existsByMerchantIdAndTransactionIdAndRole(merchantId, transactionId, role)) {
            throw new UnsupportedOperationException(
                    "Cannot create a Merchant Transaction link where this role already exists");
        }

        Optional<Merchant> merchant = mRepository.findByIdAndDeletedFalse(merchantId);
        Optional<Transaction> transaction = tRepository.findByIdAndGroupDeletedFalse(transactionId);

        if (merchant.isPresent() && transaction.isPresent()) {
            TransactionMerchant transactionMerchant = new TransactionMerchant(merchant.get(), transaction.get(), role,
                    processingDate, notes);
            return repository.save(transactionMerchant);
        }
        throw new NoSuchElementException("There is no Merchant or Transaction with that id");
    }

    @Transactional
    public TransactionMerchant edit(Long merchantId, Long transactionId, TransactionMerchantRole fromRole,
            TransactionMerchantRole toRole, Instant date, String notes) {
        TransactionMerchant tm = repository.findByMerchantIdAndTransactionIdAndRole(merchantId, transactionId, fromRole);
        if (toRole != null)
            tm.setRole(toRole);
        if (date != null)
            tm.setDate(date);
        if (notes != null)
            tm.setNotes(notes);
        return repository.save(tm);
    }

    @Transactional
    public void remove(Long merchantId, Long transactionId, TransactionMerchantRole role) {
        repository.deleteByMerchantIdAndTransactionIdAndRole(merchantId, transactionId, role);
    }

    // needs to return list of transactionMerchants and merchant, or dtos
    public TransactionWithMerchantsDTO viewMerchants(Long transactionId) {
        Optional<Transaction> transaction = tRepository.findByIdAndGroupDeletedFalse(transactionId);
        if (transaction.isEmpty())
            throw new NoSuchElementException("There is no Transaction with that id");

        List<TransactionMerchant> transactionMerchants = repository.findByTransactionId(transactionId);
        List<MerchantDetailedDTO> merchantDTOs = new ArrayList<>(transactionMerchants.size());

        for (TransactionMerchant tm : transactionMerchants) {
            merchantDTOs.add(new MerchantDetailedDTO(tm));
        }

        TransactionDTO transactionDTO = new TransactionDTO(transaction.get());
        TransactionWithMerchantsDTO dto = new TransactionWithMerchantsDTO(transactionDTO, merchantDTOs);
        return dto;
    }

    public MerchantWithTransactionsDTO viewTransactions(Long merchantId) {
        Optional<Merchant> merchant = mRepository.findByIdAndDeletedFalse(merchantId);
        if (merchant.isEmpty())
            throw new NoSuchElementException("There is no Merchant with that id");

        List<TransactionMerchant> transactionMerchants = repository.findByMerchantId(merchantId);
        List<TransactionDetailedDTO> transactionDTOs = new ArrayList<>(transactionMerchants.size());

        for (TransactionMerchant tm : transactionMerchants) {
            transactionDTOs.add(new TransactionDetailedDTO(tm));
        }

        MerchantDTO merchantDTO = new MerchantDTO(merchant.get());
        MerchantWithTransactionsDTO dto = new MerchantWithTransactionsDTO(merchantDTO, transactionDTOs);
        return dto;
    }
}
