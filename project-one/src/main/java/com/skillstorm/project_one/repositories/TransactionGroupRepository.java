package com.skillstorm.project_one.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillstorm.project_one.models.transaction.TransactionGroup;

public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Long>{

    Optional<TransactionGroup> findByIdAndDeletedFalse(Long id);

    @Query("""
            SELECT COUNT(t) > 0
            FROM Transaction t
            WHERE t.group.id = :id
            AND t.status = 'PENDING'
            """)
    boolean groupAlreadyPending(Long id);

}
