
package com.example.banking.service;

import java.math.BigDecimal;

public interface FraudDetectionService {
    boolean isTransactionFraudulent(Long fromAccountId, BigDecimal amount);
}
