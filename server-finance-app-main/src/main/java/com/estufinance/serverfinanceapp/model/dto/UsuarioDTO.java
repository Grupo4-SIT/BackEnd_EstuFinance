package com.estufinance.serverfinanceapp.model.dto;

import java.util.UUID;

import org.springframework.data.annotation.ReadOnlyProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "username", "password", "authority", "enabled", "accountNonLocked", "credentialsNonExpired", "accountNonExpired"})
public class UsuarioDTO {

    @JsonProperty("id")
    @ReadOnlyProperty
    private UUID id;

    @NotEmpty
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonProperty("authority")
	private String authority;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("accountNonLocked")
    private Boolean accountNonLocked;

    @JsonProperty("credentialsNonExpired")
    private Boolean credentialsNonExpired;

    @JsonProperty("accountNonExpired")
    private Boolean accountNonExpired;
    
}
