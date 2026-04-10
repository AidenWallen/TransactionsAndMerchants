package com.skillstorm.project_one.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skillstorm.project_one.dtos.transaction.TransactionDTO;
import com.skillstorm.project_one.dtos.transaction.TransactionFilterDTO;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionGroup;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.repositories.TransactionGroupRepository;
import com.skillstorm.project_one.repositories.TransactionRepository;
import com.skillstorm.project_one.validators.TransactionValidator;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionGroupRepository groupRepository;

    private final TransactionValidator transactionValidator;

    TransactionService(TransactionRepository repository, TransactionGroupRepository groupRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.transactionValidator = new TransactionValidator();
    }

    public TransactionGroup findGroup(Transaction transaction) {
        Long id = transaction.getGroup().getId();
        Optional<TransactionGroup> group = groupRepository
                .findByIdAndDeletedFalse(id);
        if (group.isPresent()) {
            return group.get();
        }
        throw new NoSuchElementException("There is no Group with that id");
    }

    public Transaction findById(Long id) {
        Optional<Transaction> transaction = repository.findByIdAndGroupDeletedFalse(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        throw new NoSuchElementException("There is no Transaction with that id");
    }

    public Long countTransactionsByGroupId(Long id) {
        return repository.countTransactionsByGroupId(id);
    }

    @Transactional
    public Transaction add(Transaction transaction) {
        transactionValidator.isEmpty(transaction, "Transaction");

        if (transaction.getType() == TransactionTypes.CHARGEBACK || transaction.getType() == TransactionTypes.REFUND)
            throw new UnsupportedOperationException("Must be type PURCHASE or TRANSFER_IN/OUT for creation");

        TransactionGroup group;
        group = transaction.getGroup() == null ? new TransactionGroup() : findGroup(transaction);
        transaction.setGroup(group);

        if (transaction.getDate() == null)
            transaction.setDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return repository.save(transaction);
    }

    private boolean validTransition(TransactionStatuses before, TransactionStatuses after) {
        if (before == TransactionStatuses.PENDING
                && (after == TransactionStatuses.SETTLED || after == TransactionStatuses.FAILED)
                || before == TransactionStatuses.SETTLED && after == TransactionStatuses.DISPUTED
                || before == TransactionStatuses.DISPUTED && after == TransactionStatuses.SETTLED)
            return true;
        return false;

    }

    @Transactional
    public Transaction update(Long id, TransactionStatuses status, String notes) {
        Transaction transaction = findById(id);
        TransactionGroup group = transaction.getGroup();

        if (groupRepository.groupAlreadyPending(group.getId())
                && transaction.getStatus() != TransactionStatuses.PENDING) {
            throw new UnsupportedOperationException("Can not update while group is pending");
        }
        if (group.isReversed()) {
            throw new UnsupportedOperationException("Can not update already reversed transaction");
        }
        if (status != null) {
            if (!validTransition(transaction.getStatus(), status)) {
                throw new UnsupportedOperationException(
                        "Can not change the status " + transaction.getStatus() + " to " + status);
            }

            int groupSize = group.getTransactions().size();
            if (groupSize > 1 && transaction.getStatus() == TransactionStatuses.PENDING
                    && status == TransactionStatuses.SETTLED) {
                group.setReversed(true);
            }

            transaction.setStatus(status);
        }

        if (notes != null)
            transaction.setNotes(notes);
        return repository.save(transaction);
    }

    private void validReversal(TransactionTypes transactionType, TransactionTypes reverseType) {
        if (!(transactionType == TransactionTypes.PURCHASE
                && (reverseType == TransactionTypes.CHARGEBACK || reverseType == TransactionTypes.REFUND)
                || transactionType == TransactionTypes.TRANSFER_IN && reverseType == TransactionTypes.TRANSFER_OUT
                || transactionType == TransactionTypes.TRANSFER_OUT && reverseType == TransactionTypes.TRANSFER_IN))
            throw new UnsupportedOperationException("Can not reverse " + transactionType + " to " + reverseType);

    }

    @Transactional
    public Transaction reverse(Long id, TransactionTypes reverseType, String notes) {
        Transaction transaction = findById(id);
        TransactionGroup group = transaction.getGroup();
        TransactionStatuses status = transaction.getStatus();
        TransactionTypes type = transaction.getType();

        validReversal(transaction.getType(), reverseType);
        if (group.isReversed()) {
            throw new UnsupportedOperationException("Can not " + reverseType + " already reversed transaction");
        }
        if (status == TransactionStatuses.FAILED
                || (status != TransactionStatuses.DISPUTED
                        && (type != TransactionTypes.TRANSFER_IN && type != TransactionTypes.TRANSFER_OUT))) {
            throw new UnsupportedOperationException("Can not refund the status " + status);
        }
        if (groupRepository.groupAlreadyPending(group.getId())) {
            throw new UnsupportedOperationException("Can not reverse transaction with a pending reversal");
        }

        Transaction refundTransaction = new Transaction(reverseType, transaction.getAmount(),
                transaction.getCurrency(), TransactionStatuses.PENDING, notes, group);
        return repository.save(refundTransaction);
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = findById(id);
        transaction.setDeleted(true);
        repository.save(transaction);
    }

    private List<TransactionDTO> toDTOList(List<Transaction> transactions) {
        List<TransactionDTO> transactionDTOs = new ArrayList<>(transactions.size());

        for (Transaction t : transactions) {
            transactionDTOs.add(new TransactionDTO(t));
        }

        return transactionDTOs;
    }

    public List<TransactionDTO> view() {
        List<Transaction> transactions = repository.findByGroupDeletedFalse();
        return toDTOList(transactions);
    }

    public TransactionDTO view(Long id) {
        return new TransactionDTO(findById(id));
    }

    public List<TransactionDTO> view(TransactionFilterDTO transactionFilter) {
        TransactionTypes type = transactionFilter.type();
        TransactionStatuses status = transactionFilter.status();
        Instant start = transactionFilter.start();
        Instant end = transactionFilter.end();

        List<Transaction> transactions = repository.findByFilter(type, status, start, end);
        return toDTOList(transactions);
    }

}
