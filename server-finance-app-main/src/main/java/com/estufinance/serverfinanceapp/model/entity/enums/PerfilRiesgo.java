package com.estufinance.serverfinanceapp.model.entity.enums;

import java.util.List;

public enum PerfilRiesgo {

    ALTO(5),
    MODERADO(3),
    BAJO(1);

    private Integer valorRiesgo;

    private PerfilRiesgo(Integer valorRiesgo){
        this.valorRiesgo = valorRiesgo;
    }

    public Integer getValorRiesgo() {
        return valorRiesgo;
    }

    public static PerfilRiesgo getPromedioRiesgo(List<PerfilRiesgo> riesgos){
        Integer value = riesgos.stream().mapToInt(PerfilRiesgo::getValorRiesgo).sum() / riesgos.size();
        PerfilRiesgo perfil = null;
        if (value > 3) {
            perfil = PerfilRiesgo.ALTO;
        } else if (value > 2 ) {
            perfil = PerfilRiesgo.MODERADO;
        } else {
            perfil = PerfilRiesgo.BAJO;
        }
        return perfil;
    }

}
