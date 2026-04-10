package com.skillstorm.project_one.models.transaction;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "TRANSACTION_GROUPS")
public class TransactionGroup {
    @Id                                                     
    @Column                                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column       
    private boolean deleted = false;

    @Column       
    private boolean reversed = false;

    @Valid
    @OneToMany(targetEntity = Transaction.class, mappedBy = "group")
    private Set<Transaction> transactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

}
