package com.estufinance.serverfinanceapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.estufinance.serverfinanceapp.model.domain.MetricaBalance;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;

public interface MovimientosService {

    List<Movimiento> findMovimientosByPresupuestoId(UUID idPresupuesto);
    MetricaBalance getMetricaBalanceByUsuario(UUID idUsuario);
    Page<Movimiento> getMovimientosPaginateByUsuario(Pageable paging, Example<Movimiento> example);
    boolean movimientoAlreadyExist(UUID idMovimiento);
    Optional<Movimiento> getMovimientoById(UUID idMovimiento);
    void deleteMovimientoById(UUID id);
    void deleteAllMovimientosById(List<UUID> idsMovimientos);
    void deleteAllMovimientos(List<Movimiento> movimientos);
    Page<Movimiento> getMovimientosPaginateByPortafolio(Pageable paging, Example<Movimiento> example);
    void createMovimiento(Movimiento movimiento);
    void crearMovimientoHaciaDisponible(BigDecimal importe, UUID idUsuario, String concepto, String logoConcepto);
    void crearMovimientoDesdeDisponible(BigDecimal importe, UUID idUsuario, String concepto, String logoConcepto);
    void crearMovimientoHaciaDisponibleDesdeAhorro(BigDecimal importe, UUID idUsuario);
    void crearMovimientoDesdeDisponibleParaAhorrro(BigDecimal importe, UUID idUsuario);
    void createMovimientoDescuentoACuentaEspecifica(Movimiento movimiento, UUID idCuentaAhorroEspecifica);
    void createMovimientoOfPresupuesto(Movimiento movimiento);
    Boolean hasAnyMovimientoByUsuario(UUID idUsuario);
    Boolean hasAnyMovimientoByPresupuesto(UUID idPresupuesto);
    
}