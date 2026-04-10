package com.skillstorm.project_one.models.transactionmerchant;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.transaction.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TRANSACTION_MERCHANTS")
public class TransactionMerchant {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Valid
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private TransactionMerchantRole role;

    @NotNull
    @Column(name = "processing_date")
    private Instant date;

    @Column
    private String notes;

    public TransactionMerchant() {
    }

    public TransactionMerchant(Merchant merchant, Transaction transaction, TransactionMerchantRole role,
            String notes) {
        this.merchant = merchant;
        this.transaction = transaction;
        this.role = role;
        this.date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        this.notes = notes;
    }

    public TransactionMerchant(Merchant merchant, Transaction transaction, TransactionMerchantRole role, Instant date,
            String notes) {
        this.merchant = merchant;
        this.transaction = transaction;
        this.role = role;
        this.date = (date != null ? date : Instant.now()).truncatedTo(ChronoUnit.SECONDS);
        this.notes = notes;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date != null ? date.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    public TransactionMerchantRole getRole() {
        return role;
    }

    public void setRole(TransactionMerchantRole role) {
        this.role = role;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
