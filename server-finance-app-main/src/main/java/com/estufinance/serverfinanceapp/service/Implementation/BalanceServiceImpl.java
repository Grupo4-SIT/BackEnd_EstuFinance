package com.estufinance.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.entity.Balance;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.BalanceRepository;
import com.estufinance.serverfinanceapp.service.BalanceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public Balance getBalanceByUsuario(UUID idUsuario) {
        Example<Balance> example = Example.of(Balance.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return balanceRepository.findOne(example).get();
    }

    @Override
    public void aumentarBalanceByUsuario(BigDecimal importe, UUID idUsuario) {
        Balance balance = getBalanceByUsuario(idUsuario);
        Double saldoActual = balance.getBalance().doubleValue();
        BigDecimal saldoActualizado = BigDecimal.valueOf(saldoActual + importe.doubleValue());
        balance.setBalance(saldoActualizado);
        balanceRepository.save(balance);
    }

    @Override
    public void disminuirBalanceByUsuario(BigDecimal importe, UUID idUsuario) {
        Balance balance = getBalanceByUsuario(idUsuario);
        Double saldoActual = balance.getBalance().doubleValue();
        BigDecimal saldoActualizado = BigDecimal.valueOf(saldoActual - importe.doubleValue());
        balance.setBalance(saldoActualizado);
        balanceRepository.save(balance);
    }

    @Override
    public void crearBalanceByUsuario(Usuario usuario) {
        Balance balance = Balance.builder().balance(BigDecimal.valueOf(0.0)).usuario(usuario).build();
        balanceRepository.save(balance);
    }
    
}
