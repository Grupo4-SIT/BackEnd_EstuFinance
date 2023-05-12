package com.estufinance.serverfinanceapp.model.domain;

import lombok.Data;

@Data
public class ImporteConcepto {

    private Double monto;
    private Double porcentaje;

    public ImporteConcepto(Double montoTotalTipo, Double monto){
        this.monto = monto;
        this.porcentaje = (monto/montoTotalTipo)*100; 
    }
    
}
