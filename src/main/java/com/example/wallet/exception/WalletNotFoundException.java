package com.example.wallet.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class WalletNotFoundException extends RuntimeException {

  public WalletNotFoundException(UUID walletId) {
    super("Wallet with ID " + walletId + "not found");
      log.error("Кошелек с ID {} не найден", walletId);
  }

}
