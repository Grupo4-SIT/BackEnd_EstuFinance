package com.estufinance.serverfinanceapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estufinance.serverfinanceapp.model.entity.Condicion;


public interface CondicionRepository extends JpaRepository<Condicion, UUID> {

}
