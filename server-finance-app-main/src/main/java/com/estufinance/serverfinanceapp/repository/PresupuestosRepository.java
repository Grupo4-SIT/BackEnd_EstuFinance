package com.estufinance.serverfinanceapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estufinance.serverfinanceapp.model.entity.Presupuesto;

public interface PresupuestosRepository extends JpaRepository<Presupuesto, UUID> {
    
}
