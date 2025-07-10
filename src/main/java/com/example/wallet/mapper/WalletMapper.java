package com.example.wallet.mapper;


import com.example.wallet.dto.WalletRequestDto;
import com.example.wallet.dto.WalletResponseDto;
import com.example.wallet.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet requestToEntity(WalletRequestDto walletRequestDto);
    WalletResponseDto entityToResponse(Wallet wallet);
}
