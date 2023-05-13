package com.estufinance.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.dto.MovimientoDTO;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;
import com.estufinance.serverfinanceapp.model.entity.Presupuesto;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.entity.enums.TipoMovimiento;

@Service
public class MovimientoMapper implements GenericMapper<Movimiento, MovimientoDTO> {

    @Override
    public Movimiento dtoToPojo(MovimientoDTO dto) {
        Movimiento movimiento = Movimiento.builder()
                .importe(BigDecimal.valueOf(dto.getImporte()))
                .tipo(TipoMovimiento.valueOf(dto.getTipo()))
                .usuario(Usuario.builder().id(dto.getIdUsuario()).build())
                .concepto(dto.getConcepto())
                .contabilizable(dto.getContabilizable())
                .logoConcepto(dto.getLogoConcepto())
                .build();
        if (dto.getId() != null) {
            movimiento.setId(dto.getId());
        }
        if (dto.getIdPresupuesto() != null) {
            movimiento.setPresupuesto(Presupuesto.builder().id(dto.getIdPresupuesto()).build());
        }
        return movimiento;
    }

    @Override
    public MovimientoDTO pojoToDto(Movimiento pojo) {
        MovimientoDTO movimientoDTO = MovimientoDTO.builder()
                .id(pojo.getId())
                .importe(pojo.getImporte().doubleValue())
                .tipo(pojo.getTipo().toString())
                .idUsuario(pojo.getUsuario().getId())
                .concepto(pojo.getConcepto())
                .contabilizable(pojo.getContabilizable())
                .logoConcepto(pojo.getLogoConcepto())
                .build();
        if (pojo.getPresupuesto() != null) {
            movimientoDTO.setIdPresupuesto(pojo.getPresupuesto().getId());
        }
        return movimientoDTO;
    }

}
