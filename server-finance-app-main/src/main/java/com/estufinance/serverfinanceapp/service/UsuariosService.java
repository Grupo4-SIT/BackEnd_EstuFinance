package com.estufinance.serverfinanceapp.service;

import java.util.Optional;

import com.estufinance.serverfinanceapp.model.entity.Usuario;

public interface UsuariosService {

    Optional<Usuario> findByUsername(String username);
    Boolean usernameAlreadyExist(String username);
    Usuario createUsuario(Usuario usuario);
    
    
}
