package com.estufinance.serverfinanceapp.service;

import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.Condicion;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;

public interface CondicionesService {
    void deleteCondicion(UUID idAhorro);
    void save(Condicion condicion);
    Movimiento applyCondicionIfExist(Movimiento movimiento);
    Movimiento applyCondicionToSpecificAhorro(Movimiento moviento, UUID idAhorro);
}
