package com.example.wallet.service;

import com.example.wallet.dto.WalletRequestDto;
import com.example.wallet.dto.WalletResponseDto;
import com.example.wallet.exception.InsufficientFundsException;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.mapper.WalletMapper;
import com.example.wallet.model.OperationType;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    
    @Override
    @Transactional
    public WalletResponseDto operation(WalletRequestDto walletRequestDto) {
        Wallet wallet = walletRepository
                .findByIdForUpdate(walletRequestDto.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(walletRequestDto.getWalletId()));
        log.info("WalletServiceImpl, метод operation, получили wallet {} по ID {} ", wallet, wallet.getId());
        log.info("WalletServiceImpl, метод operation, баланс - {}", wallet.getBalance());

        if (walletRequestDto.getOperationType().equals(OperationType.DEPOSIT)){
            wallet.setBalance(wallet.getBalance().add(walletRequestDto.getAmount()));
            log.info("WalletServiceImpl, метод operation, баланс увеличен и равен {}", wallet.getBalance());
        } else if (walletRequestDto.getOperationType().equals(OperationType.WITHDRAW)){
            if(wallet.getBalance().compareTo(walletRequestDto.getAmount()) < 0) {
                throw new InsufficientFundsException();
            }
            wallet.setBalance(wallet.getBalance().subtract(walletRequestDto.getAmount()));
            log.info("WalletServiceImpl, метод operation, баланс уменьшен и равен {}", wallet.getBalance());
        }
        return walletMapper.entityToResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponseDto getBalanceByWalletId(UUID walletId) {
        Wallet wallet = walletRepository
                .findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        log.info("WalletServiceImpl, метод getBalanceByWalletId получили wallet {} по ID {} ", wallet, walletId);
        log.info("WalletServiceImpl, метод getBalanceByWalletId баланс - {}", wallet.getBalance());
        return walletMapper.entityToResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponseDto createWallet() {
        Wallet wallet = new Wallet();
        log.info("WalletServiceImpl, метод createWallet новый кошелек создан, ID {}, баланс {}", wallet.getId(), wallet.getBalance());
        walletRepository.save(wallet);
        return walletMapper.entityToResponse(wallet);
    }


}
