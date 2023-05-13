package com.estufinance.serverfinanceapp.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.estufinance.serverfinanceapp.model.domain.MetricaAhorro;
import com.estufinance.serverfinanceapp.model.domain.MetricaAhorros;
import com.estufinance.serverfinanceapp.model.entity.Ahorro;
import com.estufinance.serverfinanceapp.model.entity.Condicion;

import org.springframework.data.domain.Page;

public interface AhorrosService {
    boolean ahorroExistById(UUID idAhorro);
    boolean ahorroExistByNameAndUser(String name, UUID idUser);
    boolean hasCondition(UUID idAhorro);
    void saveCondicion(Condicion condicion);
    Ahorro createBolsilloAhorro(Ahorro ahorro);
    void unableCondicion(UUID idAhorro);
    List<Ahorro> getAllAhorros();
    Page<Ahorro> getAllAhorrosOfUserPaginated(Pageable pageable, UUID idUser);
    void updateBolsilloAhorro(Ahorro ahorro);
    void deleteBolsilloAhorro(UUID idAhorro);
    List<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID idUsuario);
    List<Ahorro> findAhorrosByUsuarioId(UUID idUsuario);
    Ahorro findAhorroAutomaticoDefaultByUsuarioId(UUID idAhorro);
    MetricaAhorros getMetricaAhorros(UUID idUsuario);
    MetricaAhorro getMetricaAhorro(UUID idAhorro);
    Optional<Ahorro> findAhorroById(UUID idAhorro);
    void transferAhorroToDisponible(Ahorro ahorro, BigDecimal ImporteToTransfer);
    void transferDisponibleToAhorro(Ahorro ahorro, BigDecimal ImporteToTransfer);
}
