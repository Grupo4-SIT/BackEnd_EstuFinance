package com.estufinance.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id" , "idPortafolio", "nombre", "descripcion", "precio", "cantidad", "plazo", "perfilRiesgo", "tipo", "sector", "rentabilidadEsperada", "simulada", "liquidada"})
public class InversionDTO {

    private UUID id;
    
    @NotEmpty
    @JsonProperty("idPortafolio")
    private UUID idPortafolio;

    @NotEmpty
    @JsonProperty("nombre")
    private String nombre;

    @NotEmpty
    @JsonProperty("descripcion")
    private String descripcion;

    @NotEmpty
    @JsonProperty("precio")
    private Double precio;

    @NotEmpty
    @JsonProperty("cantidad")
    private Double cantidad;

    @NotEmpty
    @JsonProperty("plazo")
    private String plazo;

    @NotEmpty
    @JsonProperty("perfilRiesgo")
    private String perfilRiesgo;

    @NotEmpty
    @JsonProperty("tipo")
    private String tipo;

    @NotEmpty
    @JsonProperty("sector")
    private String sector;
    
    @NotEmpty
    @JsonProperty("rentabilidadEsperada")
    private Double rentabilidadEsperada;

    @NotEmpty
    @JsonProperty("simulada")
    private Boolean simulada;

    @JsonProperty("liquidada")
    private Boolean liquidada;
    
}
