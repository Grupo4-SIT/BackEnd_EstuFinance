package com.estufinance.serverfinanceapp.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.model.dto.UsuarioDTO;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.mapper.UsuarioMapper;
import com.estufinance.serverfinanceapp.service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/new-user")
    public ResponseEntity<HttpStatus> registerNewUser(@RequestBody UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioMapper.dtoToPojo(usuarioDTO);
        usuariosService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<UUID> getUserDetailsByUsername(@PathVariable String username) throws EntityNotFoundException {
        final Optional<Usuario> optional = usuariosService.findByUsername(username);
        if(optional.isEmpty()){
            throw new EntityNotFoundException("Usuario do not exist: Username -> " + username);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optional.get().getId());
    }

    @GetMapping
    public ResponseEntity<Boolean> usernameAlreadyExist(@RequestParam String username) {
        final Boolean alreadyExist = usuariosService.usernameAlreadyExist(username);
        return ResponseEntity.status(HttpStatus.OK).body(alreadyExist);
    }
    
}
