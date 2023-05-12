package com.estufinance.serverfinanceapp.model.domain;

import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.Inversion;

import lombok.Data;

@Data
public class MetricaInversion {

    //Agregar datos de inversion: Nombre - Descripcion - sector
    private String nombre;
    private String sector;
    private UUID idInversion;
    private Double gananciaEsperada;
    private Double valorTotal;
    private String plazo;
    private String nivelRiesgo;

    public MetricaInversion(Inversion inversion) {

        this.nombre = inversion.getNombre();
        this.sector = inversion.getSector().toString();
        this.idInversion = idInversion(inversion);
        this.valorTotal = valorTotal(inversion);
        this.gananciaEsperada = gananciaEsperada(inversion);
        this.plazo = plazo(inversion);
        this.nivelRiesgo = nivelRiesgo(inversion);

    }

    private double gananciaEsperada(Inversion inversion) {
        double rentabilidadEsperada = inversion.getRentabilidadEsperada().doubleValue();
        double valorTotalInversion = inversion.getCantidad()*inversion.getPrecio().doubleValue();
        return MetricaInversion.calcularGananciaEsperada(rentabilidadEsperada, valorTotalInversion);
    }

    private String nivelRiesgo(Inversion inversion) {
        return inversion.getPerfilRiesgo().toString();
    }

    private String plazo(Inversion inversion) {
        return inversion.getPlazo().toString();
    }

    private double valorTotal(Inversion inversion) {
        return inversion.getCantidad() * inversion.getPrecio().doubleValue();
    }

    private UUID idInversion(Inversion inversion) {
        return inversion.getId();
    }

    public static double calcularGananciaEsperada(double rentabilidadEsperada, double valorTotalInversion){
        return valorTotalInversion + ((rentabilidadEsperada/100)*valorTotalInversion);
    }

}
