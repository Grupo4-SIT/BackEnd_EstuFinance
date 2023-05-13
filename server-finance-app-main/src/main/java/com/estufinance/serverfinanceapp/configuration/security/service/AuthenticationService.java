package com.estufinance.serverfinanceapp.configuration.security.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.configuration.security.model.AuthenticationRequest;
import com.estufinance.serverfinanceapp.configuration.security.model.AuthenticationResponse;
import com.estufinance.serverfinanceapp.configuration.security.model.Token;
import com.estufinance.serverfinanceapp.configuration.security.repository.TokenRepository;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.mapper.UserDetailsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserDetailsMapper userDetailsMapper;
    private final AppUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public void registerNewUserToken(Usuario usuario) {
        String jwtToken = jwtService.generateToken(userDetailsMapper.pojoToDto(usuario));
        saveUserToken(usuario, jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Usuario usuario = userDetailsService.loadUsuarioByUsername(request.getUsername());
        revokeAllUserTokens(usuario);
        String jwtToken = jwtService.generateToken(userDetailsMapper.pojoToDto(usuario));
        saveUserToken(usuario, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(Usuario usuario, String jwtToken) {
        Token token = Token.builder()
                .user(usuario)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(usuario.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
