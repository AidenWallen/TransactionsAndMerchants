package com.skillstorm.project_one.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndGroupDeletedFalse(Long id);

    @Query("""
            SELECT t FROM Transaction t
            WHERE (
                (:type IS NULL OR t.type = :type)
                AND (:status IS NULL OR t.status = :status)
                AND (t.date >= COALESCE(:start, t.date))
                AND (t.date <= COALESCE(:end, t.date))
                AND t.group.deleted = FALSE
            )
            """)
    List<Transaction> findByFilter(TransactionTypes type, TransactionStatuses status, Instant start,
            Instant end);

    
    Long countTransactionsByGroupId(Long id);

    List<Transaction> findByGroupDeletedFalse();
}
