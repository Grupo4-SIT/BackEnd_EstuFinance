package com.estufinance.serverfinanceapp.model.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id" , "nombre", "descripcion", "idUsuario", "idObjectivo"})
public class PortafolioDTO {

    @JsonProperty("id")
    private UUID id;

    @NotEmpty
    @JsonProperty("nombre")
    private String nombre;
    
    @NotEmpty
    @JsonProperty("descripcion")
    private String descripcion;

    @JsonIgnore
    private List<InversionDTO> inversiones;

    @NotEmpty
    @JsonAlias("usuario")
    @JsonProperty("idUsuario")
    private UUID idUsuario;

    @JsonAlias("objetivo")
    @JsonProperty("idObjetivo")
    private UUID idObjetivo;
    
}