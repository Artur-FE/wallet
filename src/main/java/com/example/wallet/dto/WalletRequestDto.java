package com.example.wallet.dto;

import com.example.wallet.model.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class WalletRequestDto {
    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    @NotNull(message = "Operation type cannot be null")
    private OperationType operationType;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Amount value exceeds maximum allowed precision (17 digits before decimal, 2 after).")
    private BigDecimal amount;
}
