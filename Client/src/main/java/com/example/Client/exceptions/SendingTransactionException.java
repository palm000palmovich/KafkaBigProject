package com.example.Client.exceptions;

public class SendingTransactionException extends RuntimeException {
    public SendingTransactionException(String message) {
        super(message);
    }
}
