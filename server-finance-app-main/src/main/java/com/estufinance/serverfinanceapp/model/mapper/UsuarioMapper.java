package com.estufinance.serverfinanceapp.model.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.dto.UsuarioDTO;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.entity.enums.AuthorityEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioMapper implements GenericMapper<Usuario, UsuarioDTO>{

    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario dtoToPojo(UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                                .username(dto.getUsername())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .authority(AuthorityEnum.valueOf(dto.getAuthority()))
                                .enabled(true)
                                .accountNonExpired(true)
                                .credentialsNonExpired(true)
                                .accountNonLocked(true)
                                .build();
        if (dto.getId() != null) {
            usuario.setId(dto.getId());
        }
        return usuario;
    }

    @Override
    public UsuarioDTO pojoToDto(Usuario pojo) {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                                        .id(pojo.getId())
                                        .username(pojo.getUsername())
                                        .password("")
                                        .authority(pojo.getAuthority().toString())
                                        .enabled(pojo.getEnabled())
                                        .accountNonLocked(pojo.getAccountNonLocked())
                                        .credentialsNonExpired(pojo.getCredentialsNonExpired())
                                        .accountNonExpired(pojo.getAccountNonExpired())
                                        .build();
        return usuarioDTO;
    }
    
}
