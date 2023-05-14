package com.estufinance.serverfinanceapp.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.service.BalanceService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("api/v1/balance")
@RequiredArgsConstructor
public class BalanceController {
    
    private final BalanceService balanceService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Double> getBalanceAmountByUsuario(@PathVariable UUID idUsuario){
        final Double balance = balanceService.getBalanceByUsuario(idUsuario).getBalance().doubleValue();
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }
}
