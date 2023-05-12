package com.estufinance.serverfinanceapp.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonPropertyOrder({"id", "nombre", "descripcion", "fechaEstimada", "monto", "idUsuario"})
public class ObjetivoDTO {

    private UUID id;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String descripcion;
    @NotEmpty
    private LocalDateTime fechaEstimada;
    @NotEmpty
    private Double monto;
    @NotEmpty
    private UUID idUsuario;
}
