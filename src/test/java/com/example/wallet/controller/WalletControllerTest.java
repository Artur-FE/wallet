package com.example.wallet.controller;

import com.example.wallet.dto.WalletRequestDto;
import com.example.wallet.dto.WalletResponseDto;
import com.example.wallet.exception.GlobalExceptionHandler;
import com.example.wallet.exception.InsufficientFundsException;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.model.OperationType;
import com.example.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private WalletService walletService;

    private WalletController walletController;

    private UUID testWalletId;
    private BigDecimal initialBalance;

    @BeforeEach
    void setUp() {
        testWalletId = UUID.randomUUID();
        initialBalance = new BigDecimal("1000.00");
        objectMapper = new ObjectMapper();
        this.walletController = new WalletController(walletService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("1. Должен успешно выполнить операцию пополнения (DEPOSIT)")
    void performOperation_Deposit_Success() throws Exception {
        WalletRequestDto requestDto = new WalletRequestDto();
        requestDto.setWalletId(testWalletId);
        requestDto.setOperationType(OperationType.DEPOSIT);
        requestDto.setAmount(new BigDecimal("500.00"));

        BigDecimal newBalance = initialBalance.add(requestDto.getAmount());
        WalletResponseDto expectedResponse = new WalletResponseDto();
        expectedResponse.setBalance(newBalance);
        when(walletService.operation(any(WalletRequestDto.class))).thenReturn(expectedResponse);


        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(newBalance.doubleValue()));
    }

    @Test
    @DisplayName("2. Должен успешно выполнить операцию снятия (WITHDRAW)")
    void performOperation_Withdraw_Success() throws Exception {
        WalletRequestDto requestDto = new WalletRequestDto();
        requestDto.setWalletId(testWalletId);
        requestDto.setOperationType(OperationType.WITHDRAW);
        requestDto.setAmount(new BigDecimal("200.00"));

        BigDecimal newBalance = initialBalance.subtract(requestDto.getAmount());
        WalletResponseDto expectedResponse = new WalletResponseDto();
        expectedResponse.setBalance(newBalance);
        when(walletService.operation(any(WalletRequestDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(newBalance.doubleValue()));
    }

    @Test
    @DisplayName("3. Должен вернуть 400 Bad Request, если недостаточно средств для снятия")
    void performOperation_InsufficientFunds_BadRequest() throws Exception {
        WalletRequestDto requestDto = new WalletRequestDto();
        requestDto.setWalletId(testWalletId);
        requestDto.setOperationType(OperationType.WITHDRAW);
        requestDto.setAmount(new BigDecimal("99999.00"));

        when(walletService.operation(any(WalletRequestDto.class)))
                .thenThrow(new InsufficientFundsException());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Operation impossible"))
                .andExpect(jsonPath("$.message").value("There are not enough funds on your wallet balance."));
    }

    @Test
    @DisplayName("4. Должен вернуть 400 Bad Request при неверном формате UUID в запросе")
    void performOperation_InvalidUuidFormat_BadRequest() throws Exception {
        String invalidJson = "{\"walletId\":\"not-a-valid-uuid\",\"operationType\":\"DEPOSIT\",\"amount\":100.00}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Wallet ID is not a valid UUID format. It must be a 36-character string."));
    }

    @Test
    @DisplayName("5. Должен успешно получить баланс кошелька по ID")
    void getBalance_Success() throws Exception {
        WalletResponseDto expectedResponse = new WalletResponseDto();
        expectedResponse.setBalance(initialBalance);
        when(walletService.getBalanceByWalletId(testWalletId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/wallet/{WALLET_UUID}", testWalletId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(initialBalance.doubleValue()));
    }

    @Test
    @DisplayName("6. Должен вернуть 404 Not Found, если кошелек не найден при запросе баланса")
    void getBalance_WalletNotFound() throws Exception {
        String errorMessage = "Wallet with ID " + testWalletId + " not found";
        when(walletService.getBalanceByWalletId(testWalletId))
                .thenThrow(new WalletNotFoundException(testWalletId));

        mockMvc.perform(get("/api/v1/wallet/{WALLET_UUID}", testWalletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}