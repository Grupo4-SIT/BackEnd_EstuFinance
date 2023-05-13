package com.estufinance.serverfinanceapp.model.mapper;

import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.dto.PortafolioDTO;
import com.estufinance.serverfinanceapp.model.entity.Objetivo;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.Usuario;

@Service
public class PortafolioMapper implements GenericMapper<Portafolio, PortafolioDTO> {

    @Override
    public Portafolio dtoToPojo(PortafolioDTO dto) {
        Portafolio portafolio = Portafolio.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .usuario(Usuario.builder().id(dto.getIdUsuario()).build())
                .objetivo(Objetivo.builder().id(dto.getIdObjetivo()).build())
                .build();
        if (dto.getId() != null) {
            portafolio.setId(dto.getId());
        }
        return portafolio;
    }

    @Override
    public PortafolioDTO pojoToDto(Portafolio pojo) {
        PortafolioDTO portafolioDTO = PortafolioDTO.builder()
                .id(pojo.getId())
                .nombre(pojo.getNombre())
                .descripcion(pojo.getDescripcion())
                .idUsuario(pojo.getUsuario().getId())
                .idObjetivo(pojo.getObjetivo().getId())
                .build();
        return portafolioDTO;
    }

}
