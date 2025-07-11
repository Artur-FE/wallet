package com.example.wallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("There are not enough funds on your wallet balance.");
        log.error("Недостаточно денег на кошельке для снятия");
    }
}
