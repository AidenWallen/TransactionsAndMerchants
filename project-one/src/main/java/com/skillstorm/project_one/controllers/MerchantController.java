package com.skillstorm.project_one.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project_one.dtos.merchant.MerchantDTO;
import com.skillstorm.project_one.dtos.merchant.MerchantUpdateDTO;
import com.skillstorm.project_one.dtos.merchant.MerchantViewDTO;
import com.skillstorm.project_one.models.merchant.Merchant;
import com.skillstorm.project_one.services.MerchantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/merchants")
@CrossOrigin(origins = "*")
public class MerchantController {

    private final MerchantService service;

    public MerchantController(MerchantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MerchantViewDTO>> getMerchants() {
        return new ResponseEntity<>(service.view(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MerchantDTO> createMerchant(@Valid @RequestBody Merchant newMerchant) {
        Merchant merchant = service.add(newMerchant);
        if (merchant == null) {
            return ResponseEntity.notFound().build();
        }
        MerchantDTO dto = new MerchantDTO(merchant);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(@PathVariable Long id, @Valid @RequestBody MerchantUpdateDTO updateMerchant) {

        Merchant merchant = service.edit(id, updateMerchant);
        MerchantDTO dto = new MerchantDTO(merchant);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
