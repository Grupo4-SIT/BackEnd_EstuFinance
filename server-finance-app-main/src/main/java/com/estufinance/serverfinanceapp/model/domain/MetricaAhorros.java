package com.estufinance.serverfinanceapp.model.domain;
import java.util.List;

import com.estufinance.serverfinanceapp.model.entity.Ahorro;

import lombok.Data;

@Data
public class MetricaAhorros {
    
    private Boolean mostraMetricas;
    private Double ahorroTotal;
    private String ahorroAutomatico;
    private Double promedioAhorro;
    private Double nivelCumplimientoPromedio;

    public MetricaAhorros(List<Ahorro> ahorros) {
        this.mostraMetricas = false;
        if (!ahorros.isEmpty()) {
            this.mostraMetricas = true;
            this.ahorroTotal = getAhorroTotalOperation(ahorros);
            this.ahorroAutomatico = getAhorroAutomaticoOperation(ahorros);
            this.promedioAhorro = getPromedioAhorroOperation(ahorros);
            this.nivelCumplimientoPromedio = getNivelCumplimientoPromedioOperation(ahorros);     
        }
    }

    private Double getAhorroTotalOperation(List<Ahorro> ahorros) {
        return ahorros
                    .stream()
                    .mapToDouble(ahorro -> ahorro.getImporte().doubleValue())
                    .sum();
    }

    private String getAhorroAutomaticoOperation(List<Ahorro> ahorros) {
        StringBuilder ahorroAutomatico = new StringBuilder();
        ahorroAutomatico.append(String.valueOf(ahorros.stream()
                                                    .filter(ahorro -> ahorro.isAutomatico() == true)
                                                    .count()))
                        .append("/")
                        .append(String.valueOf(ahorros.size()));
        return ahorroAutomatico.toString();
    }

    private Double getPromedioAhorroOperation(List<Ahorro> ahorros) {
        return ahorros.stream()
                    .mapToDouble(ahorro -> ahorro.getImporte().doubleValue())
                    .sum() / ahorros.size();
    }

    private Double getNivelCumplimientoPromedioOperation(List<Ahorro> ahorros) {
        return ahorros
                .stream()
                .mapToDouble(ahorro -> ahorro.getImporte().doubleValue() / ahorro.getObjetivo().getMonto().doubleValue())
                .sum() / ahorros.size();
    }
}
