package com.example.wallet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "wallets")
public class Wallet {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "balance")
    private BigDecimal balance;

    public Wallet() {
        this.id = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
    }
}
