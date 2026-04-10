package com.skillstorm.project_one.models.merchant;

import java.util.Set;

import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "MERCHANTS")
public class Merchant {

    @Id                                                     
    @Column                                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank
    @Column(name = "last_name")
    private String lastName;
    
    @NotNull
    @Column
    @Enumerated(EnumType.STRING) 
    private MerchantCategories category;

    @NotBlank
    @Column
    private String country;
 
    @NotNull
    @Column
    @Enumerated(EnumType.STRING) 
    private MerchantStatuses status;
    
    @Valid
    @OneToMany(targetEntity = TransactionMerchant.class, mappedBy = "merchant")
    private Set<TransactionMerchant> transactionMerchants;

    @Column
    private boolean deleted;

    public Merchant() {
    }

    public Merchant(String firstName, String lastName, MerchantCategories category, String country,
            MerchantStatuses status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.country = country;
        this.status = status;
        deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MerchantCategories getCategory() {
        return category;
    }

    public void setCategory(MerchantCategories category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public MerchantStatuses getStatus() {
        return status;
    }

    public void setStatus(MerchantStatuses status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
