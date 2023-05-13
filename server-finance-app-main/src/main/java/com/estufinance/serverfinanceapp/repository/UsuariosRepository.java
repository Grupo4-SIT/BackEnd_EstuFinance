package com.estufinance.serverfinanceapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estufinance.serverfinanceapp.model.entity.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);
    
}
