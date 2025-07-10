package com.example.wallet.dto;

import com.example.wallet.model.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class WalletRequestDto {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
