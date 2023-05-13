package com.estufinance.serverfinanceapp.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.estufinance.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoRepository extends JpaRepository<Objetivo, UUID> {
    
    @Query(value = "select * from objetivos o where o.usuario_fk = :idUsuario", nativeQuery = true)
    List<Objetivo> findByUsuarioId(UUID idUsuario);

    @Query(value = "select * from objetivos o inner join ahorros a on o.id = a.objetivo_fk where o.usuario_fk = :idUsuario and o.id = :idObjetivo", nativeQuery = true)
    List<Objetivo> findObjetivoOfUserInAhorros(UUID idUsuario, UUID idObjetivo);

    @Query(value = "select * from objetivos o inner join portafolio p on o.id = p.objetivo_fk where o.usuario_fk = :idUsuario and o.id = :idObjetivo", nativeQuery = true)
    List<Objetivo> findObjetivosInPortafoslioOfUser(UUID idUsuario, UUID idObjetivo);
}
