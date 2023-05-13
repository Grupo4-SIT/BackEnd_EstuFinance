package com.estufinance.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.dto.InversionDTO;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.estufinance.serverfinanceapp.model.entity.enums.PlazoInversion;
import com.estufinance.serverfinanceapp.model.entity.enums.SectorActivo;
import com.estufinance.serverfinanceapp.model.entity.enums.TipoActivo;

@Service
public class InversionMapper implements GenericMapper<Inversion, InversionDTO> {

    @Override
    public Inversion dtoToPojo(InversionDTO dto) {
        Inversion inversion = Inversion.builder()
                .portafolio(Portafolio.builder().id(dto.getIdPortafolio()).build())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(BigDecimal.valueOf(dto.getPrecio()))
                .cantidad(dto.getCantidad())
                .plazo(PlazoInversion.valueOf(dto.getPlazo()))
                .perfilRiesgo(PerfilRiesgo.valueOf(dto.getPerfilRiesgo()))
                .tipo(TipoActivo.valueOf(dto.getTipo()))
                .sector(SectorActivo.valueOf(dto.getSector()))
                .rentabilidadEsperada(BigDecimal.valueOf(dto.getRentabilidadEsperada()))
                .simulada(dto.getSimulada())
                .liquidada(false)
                .build();
        if (dto.getId() != null) {
            inversion.setId(dto.getId());
        }
        return inversion;
    }

    @Override
    public InversionDTO pojoToDto(Inversion pojo) {
        InversionDTO inversionDTO = InversionDTO.builder()
                .id(pojo.getId())
                .idPortafolio(pojo.getPortafolio().getId())
                .nombre(pojo.getNombre())
                .descripcion(pojo.getDescripcion())
                .precio(pojo.getPrecio().doubleValue())
                .cantidad(pojo.getCantidad())
                .plazo(pojo.getPlazo().toString())
                .perfilRiesgo(pojo.getPerfilRiesgo().toString())
                .tipo(pojo.getTipo().toString())
                .sector(pojo.getSector().toString())
                .rentabilidadEsperada(pojo.getRentabilidadEsperada().doubleValue())
                .simulada(pojo.getSimulada())
                .liquidada(pojo.getLiquidada())
                .build();
        return inversionDTO;
    }

}
