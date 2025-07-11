package com.example.wallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends RuntimeException {

  public WalletNotFoundException(UUID walletId) {
    super("Wallet with ID " + walletId + " not found");
      log.error("Кошелек с ID {} не найден", walletId);
  }

}
