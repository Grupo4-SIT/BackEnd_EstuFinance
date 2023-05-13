package com.estufinance.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.dto.ObjetivoDTO;
import com.estufinance.serverfinanceapp.model.entity.Objetivo;
import com.estufinance.serverfinanceapp.model.entity.Usuario;

@Service
public class ObjetivoMapper implements GenericMapper<Objetivo, ObjetivoDTO> {

    @Override
    public Objetivo dtoToPojo(ObjetivoDTO dto) {
        Objetivo objetivo = Objetivo.builder()
                                .nombre(dto.getNombre())
                                .descripcion(dto.getDescripcion())
                                .fechaEstimada(dto.getFechaEstimada())
                                .monto(BigDecimal.valueOf(dto.getMonto()))
                                .usuario(Usuario.builder().id(dto.getIdUsuario()).build())
                                .build();
        if(dto.getId() != null) {
            objetivo.setId(dto.getId());
        }
        return objetivo;
    }

    @Override
    public ObjetivoDTO pojoToDto(Objetivo pojo) {
        return ObjetivoDTO.builder()
                    .id(pojo.getId())
                    .nombre(pojo.getNombre())
                    .descripcion(pojo.getDescripcion())
                    .fechaEstimada(pojo.getFechaEstimada())
                    .monto(pojo.getMonto().doubleValue())
                    .idUsuario(pojo.getUsuario().getId())
                    .build();
        
    }

}