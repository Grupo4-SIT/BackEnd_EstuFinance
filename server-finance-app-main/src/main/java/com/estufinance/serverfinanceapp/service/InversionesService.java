package com.estufinance.serverfinanceapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.estufinance.serverfinanceapp.model.domain.MetricaInversion;
import com.estufinance.serverfinanceapp.model.entity.Inversion;

public interface InversionesService {

    List<Inversion> findInversionesByPortafolioId(UUID idPortafolio);
    Boolean alreadyExistOnPortafolio(UUID idPortafolio, String nombre);
    boolean inversionAlreadyExist(UUID idInversion);
    Optional<Inversion> getInversionById(UUID idInversion);
    MetricaInversion getMetricaInversion(Inversion inversion);
    void deleteInversionById(UUID id);
    void deleteAllInversionesById(List<UUID> idInversiones);
    List<MetricaInversion> getMetricasInversiones(Set<Inversion> inversiones);
    Page<Inversion> getInversionesPaginateByPortafolio(Pageable paging, Example<Inversion> example);
    void updateInversion(Inversion inversion);
    void createInversion(Inversion inversion);
    void liquidarInversion(Inversion inversion, UUID idUsuario);
    Boolean portafolioHasAnyInversion(UUID idPortafolio);
    Boolean hasPortafolioAnyInversion(UUID idPortafolio);
    void setInversionAsLiquidada(Inversion inversion);
}
