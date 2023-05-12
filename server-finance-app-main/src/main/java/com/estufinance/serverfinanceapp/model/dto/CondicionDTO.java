package com.estufinance.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "expresion", "importe", "tipoImporte", "enabled"})
public class CondicionDTO {
    
    private UUID id;
    @NotEmpty
    private String expresion;
    @NotEmpty
    private Double importe;
    @NotEmpty
    private String tipoImporte;
    @NotEmpty
    private Boolean enabled;
    private UUID idAhorro;
}
