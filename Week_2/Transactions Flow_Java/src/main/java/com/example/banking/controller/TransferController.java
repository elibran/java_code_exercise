
package com.example.banking.controller;

import com.example.banking.dto.TransferRequest;
import com.example.banking.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        transferService.transferMoney(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ResponseEntity.ok().body(java.util.Map.of("message", "Transfer successful"));
    }
}
