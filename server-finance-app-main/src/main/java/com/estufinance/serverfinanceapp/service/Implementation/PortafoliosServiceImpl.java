package com.estufinance.serverfinanceapp.service.Implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.domain.MetricaPortafolio;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.PortafoliosRepository;
import com.estufinance.serverfinanceapp.service.InversionesService;
import com.estufinance.serverfinanceapp.service.PortafoliosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
@Transactional
public class PortafoliosServiceImpl implements PortafoliosService {

    private final PortafoliosRepository portafoliosRepository;
    private final InversionesService inversionesService;

    @Override
    public MetricaPortafolio getMetricaPortafolioByInversiones(Set<Inversion> inversiones) {
        return new MetricaPortafolio(inversiones);
    }

    @Override
    public List<MetricaPortafolio> getMetricasPortafolios(Set<Portafolio> portafolios) {
        return portafolios.stream().map(p -> new MetricaPortafolio(p.getInversiones())).toList();
    }

    @Override
    public Portafolio createPortafolio(Portafolio portafolio) {
        return portafoliosRepository.save(portafolio);
    }

    @Override
    public void deletePortafolioById(UUID idPortafolio) {
        portafoliosRepository.deleteById(idPortafolio);
    }

    @Override
    public void updatePortafolio(Portafolio portafolio) {
        portafoliosRepository.save(portafolio);
    }
    
    @Override
    public Boolean similarAlreadyExist(String nombre, UUID idUsuario) {
        Example<Portafolio> example = Example
                .of(Portafolio.builder().nombre(nombre).usuario(Usuario.builder().id(idUsuario).build()).build());
        return portafoliosRepository.findOne(example).isPresent();
    }

    @Override
    public boolean portafolioAlreadyExist(UUID idPortafolio) {
        return portafoliosRepository.existsById(idPortafolio);
    }

    @Override
    public List<Portafolio> getPortafoliosByUsuario(Usuario usuario) {
        return portafoliosRepository.findAllByUsuario(usuario);
    }

    @Override
    public Page<Portafolio> getPortafoliosPaginate(Pageable paging) {
        return portafoliosRepository.findAll(paging);
    }

    @Override
    public Optional<Portafolio> getPortafolioById(UUID idPortafolio) {
        return portafoliosRepository.findById(idPortafolio);
    }

    @Override
    public boolean hasAnyInversion(UUID id) {
        return inversionesService.portafolioHasAnyInversion(id);
    }

    @Override
    public Boolean hasAnyPortafolio(UUID idUsuario) {
        Example<Portafolio> example = Example.of(Portafolio.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return portafoliosRepository.findOne(example).isPresent();
    }
    
}