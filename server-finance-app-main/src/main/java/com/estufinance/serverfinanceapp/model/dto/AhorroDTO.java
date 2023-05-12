package com.estufinance.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "nombre", "descripcion", "tipo", "importe", "automatico", "idObjetivo", "idCondicion"})
public class AhorroDTO {
    
    private UUID id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String descripcion;
    
    @NotEmpty
    private String tipo;

    @NotEmpty
    private Double importe;

    @NotEmpty
    private boolean automatico;

    @NotEmpty
    private UUID idObjetivo;

    @NotEmpty
    private UUID idUsuario;

    private CondicionDTO condicionDTO;

}
