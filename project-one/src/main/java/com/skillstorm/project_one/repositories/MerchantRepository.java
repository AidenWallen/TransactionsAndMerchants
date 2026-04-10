package com.skillstorm.project_one.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.project_one.models.merchant.Merchant;
import java.util.List;
import java.util.Optional;


public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    List<Merchant> findByDeletedFalse();

    Optional<Merchant> findByIdAndDeletedFalse(Long merchantId);
}
