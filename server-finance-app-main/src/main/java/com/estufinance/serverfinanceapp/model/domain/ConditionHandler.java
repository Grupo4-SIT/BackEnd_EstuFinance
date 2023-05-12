package com.estufinance.serverfinanceapp.model.domain;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.estufinance.serverfinanceapp.model.entity.Ahorro;
import com.estufinance.serverfinanceapp.model.entity.Condicion;
import com.estufinance.serverfinanceapp.model.entity.enums.Expresion;
import com.estufinance.serverfinanceapp.model.entity.enums.TipoImporte;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConditionHandler {
    
    public BigDecimal buildConditionBasedOn(BigDecimal importe, Ahorro ahorro) {
                                        //cambiar nombre metodo
        Condicion condicion = ahorro.getCondicion();
        Double ingreso = importe.doubleValue();
        Double ingresoLuegoDescuento = 0.0;
        if(condicion.getTipoImporte().equals(TipoImporte.EFECTIVO)) {
            ahorro.setImporte(BigDecimal.valueOf(ahorro.getImporte().doubleValue() + condicion.getCantidadDescontar().doubleValue()));
            ingresoLuegoDescuento = ingreso - condicion.getCantidadDescontar().doubleValue();
        }else if(condicion.getTipoImporte().equals(TipoImporte.PORCENTAJE)) {
            BigDecimal percentajeDiscounter = BigDecimal.valueOf(ahorro.getImporte().doubleValue() 
                                            * (condicion.getCantidadDescontar().doubleValue() / 100));
            ahorro.setImporte(ahorro.getImporte().add(percentajeDiscounter));
            ingresoLuegoDescuento = ingreso - percentajeDiscounter.doubleValue();
        }
        return BigDecimal.valueOf(ingresoLuegoDescuento);
    }

    public boolean fullfitCondition(BigDecimal importe, Condicion condicion) {
        if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_IGUAL_A)) {
            return importe.doubleValue() >= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_A)) {
            return importe.doubleValue() > condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_IGUAL_A)) {
            return importe.doubleValue() == condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_IGUAL_A)) {
            return importe.doubleValue() <= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_A)) {
            return importe.doubleValue() < condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR)) {
            return true;
        }
        else {
            return false;
        }
    }
}
