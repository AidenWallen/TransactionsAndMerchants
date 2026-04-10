package com.skillstorm.project_one.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skillstorm.project_one.dtos.merchant.MerchantUpdateDTO;
import com.skillstorm.project_one.dtos.merchant.MerchantViewDTO;
import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.models.merchant.MerchantCategories;
import com.skillstorm.project_one.models.merchant.MerchantStatuses;
import com.skillstorm.project_one.repositories.MerchantRepository;
import com.skillstorm.project_one.repositories.TransactionMerchantRepository;
import com.skillstorm.project_one.validators.MerchantValidator;

import jakarta.transaction.Transactional;

@Service
public class MerchantService {

    private final MerchantRepository repository;
    private final TransactionMerchantRepository tmRepository;
    
    private final MerchantValidator merchantValidator;

    MerchantService(MerchantRepository repository, TransactionMerchantRepository tmRepository) {
        this.repository = repository;
        this.tmRepository = tmRepository;
        merchantValidator = new MerchantValidator();
    }

    @Transactional
    public Merchant add(Merchant merchant) {
        merchantValidator.isValidString(merchant.getFirstName(), "Merchant.firstName");
        merchantValidator.isValidString(merchant.getLastName(), "Merchant.lastName");
        merchantValidator.isValidString(merchant.getCountry(), "Merchant.country");
        return repository.save(merchant);
    }

    public Merchant findById(Long id) {
        Optional<Merchant> merchant = repository.findByIdAndDeletedFalse(id);
        if (merchant.isPresent()) {
            return merchant.get();
        }
        throw new NoSuchElementException("There is no Merchant with that id");
    }

    @Transactional
    public Merchant edit(Long id, MerchantUpdateDTO merchantUpdate) {
        Merchant merchant = findById(id);
        MerchantStatuses status = merchantUpdate.status();
        MerchantCategories category = merchantUpdate.category();
        String country = merchantUpdate.country();

        if(status != null)
            merchant.setStatus(status);
        if(category != null)
            merchant.setCategory(category);
        if(country != null){
            merchantValidator.isValidString(country, "Merchant.country");
            merchant.setCountry(country);
        }
        
        return repository.save(merchant);
    }

    @Transactional
    public void delete(Long id) {
        Merchant merchant = findById(id);
        if(merchant.isDeleted())
            throw new NoSuchElementException("There is no Merchant with that id");
        merchant.setDeleted(true);
        repository.save(merchant);
    }

    public List<MerchantViewDTO> view() {
        List<Merchant> merchants = repository.findByDeletedFalse();
        List<MerchantViewDTO> merchantsDTOs = new ArrayList<>(merchants.size());

        for (Merchant m : merchants) {
            merchantValidator.isValidWithId(m);
            Long id = m.getId();
            String firstName = m.getFirstName();
            String lastName = m.getLastName();
            long transactionCount = tmRepository.countByMerchantId(id);
            BigDecimal transactionVolume = tmRepository.transactionVolumeByMerchantId(id);

            MerchantViewDTO dto = new MerchantViewDTO(id, firstName, lastName, transactionCount, transactionVolume);
            merchantsDTOs.add(dto);
        }

        return merchantsDTOs;
    }
}
