package com.estufinance.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id" , "importe", "tipo", "concepto", "idUsuario","idPresupuesto", "contabilizable","logoConcepto"})
public class MovimientoDTO {

    @JsonProperty("id")
    private UUID id;

    @NotEmpty
    @JsonProperty("importe")
    private Double importe;

    @NotEmpty
    @JsonProperty("tipo")
    private String tipo;

    @NotEmpty
    @JsonProperty("concepto")
    private String concepto;

    @NotEmpty
    @JsonProperty("idUsuario")
    private UUID idUsuario;

    @JsonProperty("idPresupuesto")
    private UUID idPresupuesto;

    @NotEmpty
    @JsonProperty("contabilizable")
    private Boolean contabilizable;

    @NotEmpty
    @JsonProperty("logoConcepto")
    private String logoConcepto;
    
}
