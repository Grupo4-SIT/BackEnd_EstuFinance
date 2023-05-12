package com.estufinance.serverfinanceapp.model.domain;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.estufinance.serverfinanceapp.model.entity.enums.PlazoInversion;

import lombok.Data;

@Data
public class MetricaPortafolio {

        private Boolean mostraMetricas;
        private UUID idPortafolio;
        private Double valorTotal;
        private Double rentabilidadPromedio;
        private Double gananciaEsperada;
        private String plazo;
        private String nivelRiesgo;
        private Map<String, Long> sectores;

        public MetricaPortafolio(Set<Inversion> inversiones) {
                this.mostraMetricas = false;
                if (!inversiones.isEmpty()) {
                mostraMetricas = true;
                this.valorTotal = valorTotalOperation(inversiones);
                this.rentabilidadPromedio = rentabilidadPromedioOperation(inversiones);
                this.nivelRiesgo = nivelRiesgoOperation(inversiones);
                this.plazo = plazoOperation(inversiones);
                this.gananciaEsperada = gananciaEsperadaOperation(inversiones);
                this.sectores = sectoresOperation(inversiones);
                this.idPortafolio = idPortafolioOperation(inversiones);
                }
        }

        private UUID idPortafolioOperation(Set<Inversion> inversiones) {
                return inversiones.stream().findFirst().get().getPortafolio().getId();
        }

        private String plazoOperation(Set<Inversion> inversiones) {
                return PlazoInversion.getPromedioPlazo(inversiones.stream().map(Inversion::getPlazo).toList())
                                .toString();
        }

        private Map<String, Long> sectoresOperation(Set<Inversion> inversiones) {
                return inversiones.stream().collect(Collectors.groupingBy(
                                inversion -> inversion.getPerfilRiesgo().toString(), Collectors.counting()));
        }

        private double rentabilidadPromedioOperation(Set<Inversion> inversiones) {
                return inversiones.stream()
                                .mapToDouble(inversion -> inversion.getRentabilidadEsperada().doubleValue()).sum()
                                / inversiones.size();
        }

        private double gananciaEsperadaOperation(Set<Inversion> inversiones) {
                return inversiones.stream().mapToDouble(
                                i -> ((i.getCantidad() * i.getPrecio().doubleValue())
                                                * i.getRentabilidadEsperada().doubleValue())
                                                - i.getPrecio().doubleValue())
                                .sum();
        }

        private String nivelRiesgoOperation(Set<Inversion> inversiones) {
                return PerfilRiesgo
                                .getPromedioRiesgo(inversiones.stream().map(Inversion::getPerfilRiesgo).toList())
                                .toString();
        }

        private double valorTotalOperation(Set<Inversion> inversiones) {
                return inversiones.stream()
                                .mapToDouble(inversion -> inversion.getCantidad() * inversion.getPrecio().doubleValue())
                                .sum();
        }

}
