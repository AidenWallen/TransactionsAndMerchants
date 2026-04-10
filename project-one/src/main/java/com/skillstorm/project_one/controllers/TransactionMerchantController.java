package com.skillstorm.project_one.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skillstorm.project_one.dtos.transactionmerchant.MerchantWithTransactionsDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionMerchantDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionMerchantDeleteDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionMerchantLinkDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionMerchantUpdateDTO;
import com.skillstorm.project_one.dtos.transactionmerchant.TransactionWithMerchantsDTO;
import com.skillstorm.project_one.models.transactionmerchant.TransactionMerchant;
import com.skillstorm.project_one.services.TransactionMerchantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class TransactionMerchantController {
    private final TransactionMerchantService service;

    public TransactionMerchantController(TransactionMerchantService service) {
        this.service = service;
    }

    @PostMapping("/transaction-merchants")
    public ResponseEntity<TransactionMerchantDTO> link(@Valid @RequestBody TransactionMerchantLinkDTO linkDTO) {
        TransactionMerchant tm = service.link(linkDTO.merchantId(), linkDTO.transactionId(), linkDTO.role(), linkDTO.date(), linkDTO.notes());
        TransactionMerchantDTO dto = new TransactionMerchantDTO(tm);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/transaction-merchants")
    public ResponseEntity<TransactionMerchantDTO> edit(@Valid @RequestBody TransactionMerchantUpdateDTO updateDTO) {
        TransactionMerchant tm = service.edit(updateDTO.merchantId(), updateDTO.transactionId(), updateDTO.fromRole(), updateDTO.toRole(), updateDTO.date(), updateDTO.notes());
        TransactionMerchantDTO dto = new TransactionMerchantDTO(tm);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
 
    @DeleteMapping("/transaction-merchants")
    public ResponseEntity<Void> remove(@Valid @RequestBody TransactionMerchantDeleteDTO dto){
        service.remove(dto.merchantId(),dto.transactionId(),dto.role());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transactions/{transactionId}/merchants")
    public ResponseEntity<TransactionWithMerchantsDTO> viewMerchants(@PathVariable Long transactionId) {
        return new ResponseEntity<>(service.viewMerchants(transactionId), HttpStatus.OK);
    }

    @GetMapping("/merchants/{merchantId}/transactions")
    public ResponseEntity<MerchantWithTransactionsDTO> viewTransactions(@PathVariable Long merchantId) {
        return new ResponseEntity<>(service.viewTransactions(merchantId), HttpStatus.OK);
    }
}
