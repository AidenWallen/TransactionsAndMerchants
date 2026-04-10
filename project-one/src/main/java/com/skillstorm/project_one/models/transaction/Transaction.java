package com.skillstorm.project_one.models.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {
    
    @Id                                                     
    @Column                                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING) 
    private TransactionTypes type;

    @NotNull
    @Positive
    @Column
    private BigDecimal amount;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING) 
    private Currencies currency;

    @Column
    private Instant date;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING) 
    private TransactionStatuses status;

    @Column
    private String notes;

    @Valid
    @OneToMany(targetEntity = TransactionMerchant.class, mappedBy = "transaction")
    private Set<TransactionMerchant> transactionMerchants;

    @Valid
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id")
    private TransactionGroup group;

    // @Column
    // private boolean deleted;

    public Transaction() {
    }   

    public Transaction(TransactionTypes type, BigDecimal amount, Currencies currency, 
        TransactionStatuses status, String notes, TransactionGroup group) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.date = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        this.status = status;
        this.notes = notes;
        this.group = group;
    }

    public Transaction(TransactionTypes type, BigDecimal amount, Currencies currency, Instant date, 
        TransactionStatuses status, String notes, TransactionGroup group) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.date = (date!=null ? date:Instant.now()).truncatedTo(ChronoUnit.SECONDS);
        this.status = status;
        this.notes = notes;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionTypes getType() {
        return type;
    }

    public void setTransactionType(TransactionTypes type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date!=null? date.truncatedTo(ChronoUnit.SECONDS):null;
    }

    public TransactionStatuses getStatus() {
        return status;
    }

    public void setStatus(TransactionStatuses status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TransactionGroup getGroup() {
        return group;
    }

    public void setGroup(TransactionGroup group) {
        this.group = group;
    }

    public boolean isDeleted() {
        return group.isDeleted();
    }

    public void setDeleted(boolean deleted) {
        this.group.setDeleted(deleted);
    }
}
