package com.example.wallet.controller;

import com.example.wallet.dto.WalletResponseDto;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor

public class WalletController {
private final WalletService walletService;
    @PostMapping("/create")
    public WalletResponseDto create(){
        return walletService.createWallet();
    }

}
