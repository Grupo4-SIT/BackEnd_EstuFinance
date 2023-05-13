package com.estufinance.serverfinanceapp.repository;

import java.util.UUID;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.estufinance.serverfinanceapp.model.entity.Ahorro;

public interface AhorroRepository extends JpaRepository<Ahorro, UUID> {
    
    @Query(value = "select * from ahorros a where a.usuario_fk = :idUsuario and a.automatico",
             nativeQuery = true)
    Set<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID idUsuario);
}
