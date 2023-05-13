package com.estufinance.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.domain.MetricaInversion;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.repository.InversionesRepository;
import com.estufinance.serverfinanceapp.service.InversionesService;
import com.estufinance.serverfinanceapp.service.MovimientosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class InversionesServiceImpl implements InversionesService {

    private final InversionesRepository inversionesRepository;
    private final MovimientosService movimientosService;

    @Override
    public List<Inversion> findInversionesByPortafolioId(UUID idPortafolio) {
        Example<Inversion> example = Example
                .of(Inversion.builder().liquidada(false).portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findAll(example);
    }

    @Override
    public boolean inversionAlreadyExist(UUID idInversion) {
        return inversionesRepository.existsById(idInversion);
    }

    @Override
    public Optional<Inversion> getInversionById(UUID idInversion) {
        return inversionesRepository.findById(idInversion);
    }

    @Override
    public MetricaInversion getMetricaInversion(Inversion inversion) {
        return new MetricaInversion(inversion);
    }

    @Override
    public void deleteInversionById(UUID id) {
        inversionesRepository.deleteById(id);
    }

    @Override
    public void deleteAllInversionesById(List<UUID> idInversiones) {
        inversionesRepository.deleteAllById(idInversiones);
    }

    @Override
    public List<MetricaInversion> getMetricasInversiones(Set<Inversion> inversiones) {
        return inversiones.stream().map(i -> new MetricaInversion(i)).toList();
    }

    @Override
    public Page<Inversion> getInversionesPaginateByPortafolio(Pageable paging, Example<Inversion> example) {
        return inversionesRepository.findAll(example, paging);
    }

    @Override
    public void updateInversion(Inversion inversion) {
        inversionesRepository.save(inversion);
    }

    @Override
    public Boolean alreadyExistOnPortafolio(UUID idPortafolio, String nombre) {
        Example<Inversion> example = Example
                .of(Inversion.builder().liquidada(false).nombre(nombre).portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findOne(example).isPresent();
    }

    @Override
    public void createInversion(Inversion inversion) {
        inversionesRepository.save(inversion);
    }

    @Override
    public void liquidarInversion(Inversion inversion, UUID idUsuario) {
        if (inversion.getSimulada().equals(true)){
            setInversionAsLiquidada(inversion);
        }
        double valorTotalInversion = inversion.getPrecio().doubleValue() * inversion.getCantidad();
        double gananciaTotal = MetricaInversion.calcularGananciaEsperada(inversion.getRentabilidadEsperada().doubleValue(), valorTotalInversion);
        movimientosService.crearMovimientoHaciaDisponible(BigDecimal.valueOf(gananciaTotal), idUsuario, "inversion", "logo_inversion");
        setInversionAsLiquidada(inversion);
    }

    @Override
    public Boolean portafolioHasAnyInversion(UUID idPortafolio) {
        Example<Inversion> example = Example
                .of(Inversion.builder().liquidada(false).portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findOne(example).isPresent();
    }

    @Override
    public Boolean hasPortafolioAnyInversion(UUID idPortafolio) {
        Example<Inversion> example = Example.of(Inversion.builder().liquidada(false).portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return !inversionesRepository.findAll(example).isEmpty();
    }

    @Override
    public void setInversionAsLiquidada(Inversion inversion) {
        inversion.setLiquidada(true);
        inversionesRepository.save(inversion);
    }

}
