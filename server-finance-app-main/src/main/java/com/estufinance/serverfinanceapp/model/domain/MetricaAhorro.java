package com.estufinance.serverfinanceapp.model.domain;

import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.Ahorro;

import lombok.Data;

@Data
public class MetricaAhorro {
    
    private Boolean mostrarMetricas;
    private UUID idAhorro;
    private Double totalSaving; 
    private Double differenceWithObjetivo;
    private Double amountToReach;
    private String tipo;

    public MetricaAhorro(Ahorro ahorro) {
        this.idAhorro = ahorro.getId();
        this.differenceWithObjetivo = getDifferenceWithObjetivo(ahorro);
        this.totalSaving = getTotalSavings(ahorro);
        this.amountToReach = getAmountToReach(ahorro);
        this.tipo = ahorro.getTipo().toString();
    }

    private Double getDifferenceWithObjetivo(Ahorro ahorro) {
        return (ahorro.getObjetivo().getMonto().subtract(ahorro.getImporte())).doubleValue();
    }

    private Double getTotalSavings(Ahorro ahorro) {
        return ahorro.getImporte().doubleValue();
    }

    private Double getAmountToReach(Ahorro ahorro) {
        return ahorro.getObjetivo().getMonto().doubleValue();
    }

}
