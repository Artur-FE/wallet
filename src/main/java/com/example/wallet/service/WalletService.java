package com.example.wallet.service;


import com.example.wallet.dto.WalletRequestDto;
import com.example.wallet.dto.WalletResponseDto;

import java.util.UUID;

public interface WalletService {

    public WalletResponseDto operation (WalletRequestDto walletRequestDto);
    public WalletResponseDto getBalanceByWalletId(UUID walletId);
    public WalletResponseDto createWallet();

}
