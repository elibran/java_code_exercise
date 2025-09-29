package com.example.fraud.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fraud")
public class FraudController {

    @PostMapping("/check")
    public Map<String, Object> check(@RequestBody Map<String, Object> payload) {
        Long accountId = Long.valueOf(payload.get("accountId").toString());
        String amount = payload.get("amount").toString();

        // Simple mock rule: flag fraud if amount > 1000
        boolean isFraudulent = new java.math.BigDecimal(amount).compareTo(new java.math.BigDecimal("1000")) > 0;

        return Map.of("isFraudulent", isFraudulent);
    }
}
