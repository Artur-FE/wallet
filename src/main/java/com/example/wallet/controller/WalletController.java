package com.example.wallet.controller;

import com.example.wallet.dto.WalletRequestDto;
import com.example.wallet.dto.WalletResponseDto;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor

public class WalletController {
private final WalletService walletService;

    @PostMapping("/create")
    public WalletResponseDto create(){
        return walletService.createWallet();
    }

    @PostMapping
    public WalletResponseDto operation(@Valid @RequestBody WalletRequestDto walletRequestDto){
        WalletResponseDto operation = walletService.operation(walletRequestDto);
        log.info("метод WalletResponseDto operation в контроллере отработал, баланс - {}", operation.getBalance());
        return operation;
    }

    @GetMapping("/{WALLET_UUID}")
    public WalletResponseDto getBalance(@PathVariable(value = "WALLET_UUID") UUID id){
        log.info("метод getBalance контроллера отработал - ID: {}", id);
      return walletService.getBalanceByWalletId(id);
    }

}
