package com.estufinance.serverfinanceapp.model.mapper;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.entity.enums.AuthorityEnum;

@Service
public class UserDetailsMapper implements GenericMapper<Usuario, UserDetails> {

    @Override
    public Usuario dtoToPojo(UserDetails dto) {
        
        AuthorityEnum authority = dto.getAuthorities().stream().findFirst().get().toString().contains("ADMINISTRADOR")
                ? AuthorityEnum.ROLE_ADMINISTRADOR
                : AuthorityEnum.ROLE_USUARIO;

        return Usuario.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .enabled(dto.isEnabled())
                .accountNonLocked(dto.isAccountNonLocked())
                .credentialsNonExpired(dto.isCredentialsNonExpired())
                .accountNonExpired(dto.isAccountNonExpired())
                .authority(authority)
                .build();
    }

    @Override
    public UserDetails pojoToDto(Usuario pojo) {
        return User.builder()
                .username(pojo.getUsername())
                .password(pojo.getPassword())
                .disabled(!(pojo.getEnabled()))
                .accountExpired(!(pojo.getAccountNonExpired()))
                .accountLocked(!(pojo.getAccountNonLocked()))
                .credentialsExpired(!(pojo.getCredentialsNonExpired()))
                .authorities(pojo.getAuthority().toString())
                .build();
    }

}

