
package com.example.banking.exception;

public class FraudulentTransactionException extends RuntimeException {
    public FraudulentTransactionException(String message) { super(message); }
}
