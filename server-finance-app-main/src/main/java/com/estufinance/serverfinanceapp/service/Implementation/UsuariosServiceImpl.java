package com.estufinance.serverfinanceapp.service.Implementation;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.configuration.security.service.AuthenticationService;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.UsuariosRepository;
import com.estufinance.serverfinanceapp.service.BalanceService;
import com.estufinance.serverfinanceapp.service.UsuariosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class UsuariosServiceImpl implements UsuariosService {


    private final UsuariosRepository usuariosRepository;
    private final BalanceService balanceService;
    private final AuthenticationService authenticationService;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        Example<Usuario> example = Example.of(Usuario.builder().username(username).build());
        return usuariosRepository.findOne(example);
    }

    @Override
    public Boolean usernameAlreadyExist(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        Usuario usuarioSaved = usuariosRepository.save(usuario);
        authenticationService.registerNewUserToken(usuarioSaved);
        balanceService.crearBalanceByUsuario(usuarioSaved);
        return usuarioSaved;
    }
    
}
