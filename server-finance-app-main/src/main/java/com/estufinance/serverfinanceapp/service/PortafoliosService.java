package com.estufinance.serverfinanceapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.estufinance.serverfinanceapp.model.domain.MetricaPortafolio;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.Usuario;

public interface PortafoliosService {

    MetricaPortafolio getMetricaPortafolioByInversiones(Set<Inversion> inversiones);
    List<MetricaPortafolio> getMetricasPortafolios(Set<Portafolio> portafolios);
    Optional<Portafolio> getPortafolioById(UUID idPortafolio);
    Portafolio createPortafolio(Portafolio portafolio);
    void deletePortafolioById(UUID idPortafolio);
    void updatePortafolio(Portafolio portafolio);
    Boolean similarAlreadyExist(String nombrePortafolio, UUID idUsuario);
    boolean portafolioAlreadyExist(UUID idPortafolio);
    List<Portafolio> getPortafoliosByUsuario(Usuario usuario);
    Page<Portafolio> getPortafoliosPaginate(Pageable paging);
    boolean hasAnyInversion(UUID id);
    Boolean hasAnyPortafolio(UUID idUsuario);

}