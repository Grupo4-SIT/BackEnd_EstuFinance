package com.estufinance.serverfinanceapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estufinance.serverfinanceapp.model.entity.Balance;


public interface BalanceRepository extends JpaRepository<Balance, UUID> {
    
}
