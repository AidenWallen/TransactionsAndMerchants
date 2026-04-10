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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project_one.dtos.transaction.TransactionDTO;
import com.skillstorm.project_one.dtos.transaction.TransactionFilterDTO;
import com.skillstorm.project_one.models.transaction.Transaction;
import com.skillstorm.project_one.models.transaction.TransactionStatuses;
import com.skillstorm.project_one.models.transaction.TransactionTypes;
import com.skillstorm.project_one.services.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return new ResponseEntity<>(service.view(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return new ResponseEntity<>(service.view(id), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionDTO>> getTransactionByFilter(@Valid TransactionFilterDTO transactionFilter) {
        return new ResponseEntity<>(service.view(transactionFilter), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody Transaction newTransaction) {
        Transaction transaction = service.add(newTransaction);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        TransactionDTO dto = new TransactionDTO(transaction);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<TransactionDTO> reverseTransaction(@PathVariable Long id,
            @RequestParam TransactionTypes reverseType, @RequestParam(required = false) String notes) {
        Transaction transaction = service.reverse(id, reverseType, notes);
        TransactionDTO dto = new TransactionDTO(transaction);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
            @RequestParam TransactionStatuses status, @RequestParam(required = false) String notes) {
        Transaction transaction = service.update(id, status, notes);
        TransactionDTO dto = new TransactionDTO(transaction);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
