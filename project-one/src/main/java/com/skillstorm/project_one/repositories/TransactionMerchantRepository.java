package com.skillstorm.project_one.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchantRole;

public interface TransactionMerchantRepository extends JpaRepository<TransactionMerchant, Long> {

    @Query("SELECT COUNT(tm.merchant.id) FROM TransactionMerchant tm WHERE tm.merchant.id = :id AND tm.transaction.group.deleted = FALSE")
    Long countByMerchantId(Long id);

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN tm.transaction.type IN ('REFUND', 'CHARGEBACK', 'TRANSFER_OUT') THEN -tm.transaction.amount
                    ELSE tm.transaction.amount
                END
            ),0)
            FROM TransactionMerchant tm WHERE tm.merchant.id= :id
                AND tm.transaction.status = 'SETTLED'
                AND tm.transaction.group.deleted = FALSE
            """)
    BigDecimal transactionVolumeByMerchantId(Long id);

    @Query("""
            SELECT tm
            FROM TransactionMerchant tm
            WHERE tm.transaction.id = :id
                AND tm.merchant.deleted = FALSE
                AND tm.transaction.group.deleted = FALSE
            """)
    List<TransactionMerchant> findByTransactionId(Long id);

    @Query("""
            SELECT tm
            FROM TransactionMerchant tm
            WHERE tm.merchant.id = :id
                AND tm.merchant.deleted = FALSE
                AND tm.transaction.group.deleted = FALSE
            """)
    List<TransactionMerchant> findByMerchantId(Long id);

    void deleteByMerchantIdAndTransactionIdAndRole(Long merchantId, Long transactionId, TransactionMerchantRole role);

    boolean existsByMerchantIdAndTransactionIdAndRole(Long merchantId, Long transactionId,
            TransactionMerchantRole role);

    TransactionMerchant findByMerchantIdAndTransactionIdAndRole(Long merchantId, Long transactionId,
            TransactionMerchantRole fromRole);
}
