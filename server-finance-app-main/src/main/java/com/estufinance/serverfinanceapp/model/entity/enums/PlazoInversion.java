package com.estufinance.serverfinanceapp.model.entity.enums;

import java.util.List;

public enum PlazoInversion {
    CORTO(1), 
    MEDIANO(3), 
    LARGO(5);

    private Integer valorPlazo;

    private PlazoInversion(Integer valorPlazo){
        this.valorPlazo = valorPlazo;
    }

    public Integer getValorPlazo() {
        return valorPlazo;
    }

    public static PlazoInversion getPromedioPlazo(List<PlazoInversion> plazos){
        Integer value = plazos.stream().mapToInt(PlazoInversion::getValorPlazo).sum() / plazos.size();
        PlazoInversion plazo = null;
        if (value > 3) {
            plazo = PlazoInversion.LARGO;
        } else if (value > 2 ) {
            plazo = PlazoInversion.MEDIANO;
        } else {
            plazo = PlazoInversion.CORTO;
        }
        return plazo;
    }
    
}
