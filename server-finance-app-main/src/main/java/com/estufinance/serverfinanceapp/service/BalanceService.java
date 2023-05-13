package com.estufinance.serverfinanceapp.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.Balance;
import com.estufinance.serverfinanceapp.model.entity.Usuario;

public interface BalanceService {
    
    Balance getBalanceByUsuario(UUID idUsuario);
    void aumentarBalanceByUsuario(BigDecimal importe, UUID idUsuario);
    void disminuirBalanceByUsuario(BigDecimal importe, UUID idUsuario);
    void crearBalanceByUsuario(Usuario usuario);

}
