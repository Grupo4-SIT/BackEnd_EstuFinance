package com.estufinance.serverfinanceapp.model.domain;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.estufinance.serverfinanceapp.model.entity.Movimiento;
import com.estufinance.serverfinanceapp.model.entity.Presupuesto;
import com.estufinance.serverfinanceapp.model.entity.enums.PeriodoPresupuesto;
import com.estufinance.serverfinanceapp.model.entity.enums.TipoMovimiento;

import lombok.Data;

@Data
public class MetricaPresupuesto {

    private UUID idPresupuesto;
    private Boolean mostraMetricas;
    private double ingresoNecesarioMinimo;
    private double ingresoDisponible;
    private String temporalidad;
    private Map<String, Long> conceptos;
    private Map<String, Long> relacionTipoMovimiento;

    public MetricaPresupuesto(Set<Movimiento> movimientos, UUID idPresupuesto) {
        this.mostraMetricas = false;
        this.idPresupuesto = idPresupuesto;
        if (!movimientos.isEmpty()) {
            mostraMetricas = true;
            this.ingresoDisponible = ingresoDisponible(movimientos);
            this.ingresoNecesarioMinimo = ingresoNecesarioMinimo(movimientos);
            this.temporalidad = temporalidad(movimientos);
            this.conceptos = conceptos(movimientos);
            this.relacionTipoMovimiento = relacionTipoMovimiento(movimientos);
        }
    }

    /**
    Retorna un objeto Map que relaciona cada tipo de movimiento en el conjunto de movimientos proporcionado con su respectiva cantidad.
    @param movimientos El conjunto de movimientos del cual se quieren obtener las relaciones de tipo de movimiento.
    @return Un objeto Map que relaciona cada tipo de movimiento con su cantidad correspondiente.
    */
    private Map<String, Long> relacionTipoMovimiento(Set<Movimiento> movimientos) {
        return movimientos.stream().collect(Collectors.groupingBy(m -> m.getTipo().toString(), Collectors.counting()));
    }

    /**
    Retorna un objeto Map que relaciona cada concepto de movimiento en el conjunto de movimientos proporcionado con su respectiva cantidad.
    @param movimientos El conjunto de movimientos del cual se quieren obtener las relaciones de conceptos de movimiento.
    @return Un objeto Map que relaciona cada concepto de movimiento con su cantidad correspondiente.
    Este método utiliza la interfaz Stream de Java para agrupar los movimientos según su concepto y contar la cantidad de movimientos asociados a cada concepto. Luego, devuelve un objeto Map que relaciona cada concepto de movimiento con su respectiva cantidad.
    */
    private Map<String, Long> conceptos(Set<Movimiento> movimientos) {
        return movimientos.stream().collect(Collectors.groupingBy(m -> m.getConcepto(), Collectors.counting()));
    }

    /**
    Retorna un objeto String que representa la temporalidad del conjunto de movimientos proporcionado.
    @param movimientos El conjunto de movimientos del cual se quiere obtener la temporalidad.
    @return Un objeto String que representa la temporalidad del conjunto de movimientos proporcionado.
    Este método utiliza la interfaz Stream de Java para obtener el primer movimiento del conjunto proporcionado y, en caso de que el conjunto esté vacío, crea un movimiento con un presupuesto que tenga un período de presupuesto 'NA'. A continuación, se obtiene el período del presupuesto del primer movimiento y se devuelve como un objeto String.
    */
    private String temporalidad(Set<Movimiento> movimientos) {
        return movimientos.stream().findFirst().orElse(Movimiento.builder().presupuesto(Presupuesto.builder().periodo(PeriodoPresupuesto.NA).build()).build()).getPresupuesto().getPeriodo().toString();
    }

    /**
    Retorna un valor double que representa el monto mínimo de ingresos necesarios para cubrir los egresos del conjunto de movimientos proporcionado.
    @param movimientos El conjunto de movimientos del cual se quiere obtener el monto mínimo de ingresos necesarios.
    @return Un valor double que representa el monto mínimo de ingresos necesarios para cubrir los egresos del conjunto de movimientos proporcionado.
    Este método utiliza la interfaz Stream de Java para filtrar los movimientos que corresponden a egresos, luego, a través de la función mapToDouble, se obtiene el importe de cada movimiento como un valor double. Posteriormente, se suman todos los valores y se devuelve el resultado como el monto mínimo de ingresos necesarios para cubrir los egresos del conjunto de movimientos proporcionado.
    */
    private double ingresoNecesarioMinimo(Set<Movimiento> movimientos) {
        return movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.EGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
    }

    /**
    Retorna un valor double que representa el monto disponible de ingresos en el conjunto de movimientos proporcionado.
    @param movimientos El conjunto de movimientos del cual se quiere obtener el monto disponible de ingresos.
    @return Un valor double que representa el monto disponible de ingresos en el conjunto de movimientos proporcionado.
    Este método utiliza la interfaz Stream de Java para filtrar los movimientos que corresponden a ingresos, luego, a través de la función mapToDouble, se obtiene el importe de cada movimiento como un valor double. Posteriormente, se suman todos los valores y se devuelve el resultado como el monto disponible de ingresos en el conjunto de movimientos proporcionado.
    */
    private double ingresoDisponible(Set<Movimiento> movimientos) {
        return movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.INGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
    }

}
