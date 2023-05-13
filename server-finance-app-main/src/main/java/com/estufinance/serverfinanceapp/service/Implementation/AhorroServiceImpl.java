package com.estufinance.serverfinanceapp.service.Implementation;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.domain.MetricaAhorro;
import com.estufinance.serverfinanceapp.model.domain.MetricaAhorros;
import com.estufinance.serverfinanceapp.model.entity.Ahorro;
import com.estufinance.serverfinanceapp.model.entity.Condicion;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.AhorroRepository;
import com.estufinance.serverfinanceapp.repository.CondicionRepository;
import com.estufinance.serverfinanceapp.service.AhorrosService;
import com.estufinance.serverfinanceapp.service.MovimientosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AhorroServiceImpl implements AhorrosService {
    
    private final AhorroRepository ahorroRepository;
    private final MovimientosService movimientosService;
    private final CondicionRepository condicionRepository;

    @Override
    public Ahorro createBolsilloAhorro(Ahorro ahorro) {
        return ahorroRepository.save(ahorro);
    }

    @Override
    public List<Ahorro> getAllAhorros() {
        return new ArrayList<Ahorro>(ahorroRepository.findAll());
    }

    @Override
    public void updateBolsilloAhorro(Ahorro ahorro) {
        ahorroRepository.save(ahorro);
    }

    @Override
    public void deleteBolsilloAhorro(UUID idAhorro) {
        ahorroRepository.deleteById(idAhorro);
    }

    @Override
    public boolean ahorroExistById(UUID idAhorro) {
        return ahorroRepository.existsById(idAhorro);
    }

    @Override
    public MetricaAhorros getMetricaAhorros(UUID idUsuario) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("automatico");
        Ahorro ahorro = Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build();
        Example<Ahorro> example = Example.of(ahorro, exampleMatcher);
        List<Ahorro> allAhorros = ahorroRepository.findAll(example);
        return new MetricaAhorros(allAhorros);
    }

    @Override
    public Page<Ahorro> getAllAhorrosOfUserPaginated(Pageable pageable, UUID idUser) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("automatico");
        Ahorro ahorro = Ahorro.builder().usuario(Usuario.builder().id(idUser).build()).build();
        Example<Ahorro> example = Example.of(ahorro, exampleMatcher);
        return ahorroRepository.findAll(example, pageable);
    }

    @Override
    public List<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().automatico(true).usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findAll(example);
    }

    @Override
    public Ahorro findAhorroAutomaticoDefaultByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findOne(example).get();
    }

    @Override
    public Optional<Ahorro> findAhorroById(UUID idAhorro) {
        return ahorroRepository.findById(idAhorro);
    }

    @Override
    public void transferAhorroToDisponible(Ahorro ahorro, BigDecimal ImporteToTransfer) {
        ahorro.setImporte(ahorro.getImporte().subtract(ImporteToTransfer));
        ahorroRepository.save(ahorro);
        movimientosService.crearMovimientoHaciaDisponibleDesdeAhorro(ImporteToTransfer, ahorro.getUsuario().getId());
    }

    @Override
    public void transferDisponibleToAhorro(Ahorro ahorro, BigDecimal ImporteToTransfer) {
        ahorro.setImporte(ahorro.getImporte().add(ImporteToTransfer));
        ahorroRepository.save(ahorro);
        movimientosService.crearMovimientoDesdeDisponibleParaAhorrro(ImporteToTransfer, ahorro.getUsuario().getId());
    }

     

    @Override
    public boolean hasCondition(UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
        return ahorro.getCondicion() != null;
    }

    @Override
    public MetricaAhorro getMetricaAhorro(UUID idAhorro) {
        return new MetricaAhorro(ahorroRepository.getReferenceById(idAhorro));
    }

    @Override
    public boolean ahorroExistByNameAndUser(String name, UUID idUser) {
        Example<Ahorro> example = Example.of(Ahorro.builder()
                                                   .nombre(name)
                                                   .usuario(Usuario.builder().id(idUser).build())
                                                   .build());
        return ahorroRepository.findOne(example).isPresent();
    }

    @Override
    public void unableCondicion(UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
        ahorro.setAutomatico(false);
        ahorro.setCondicion(null);
        ahorroRepository.save(ahorro);
    }

    @Override
    public List<Ahorro> findAhorrosByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findAll(example);
    }

    @Override
    public void saveCondicion(Condicion condicion) {
        Ahorro ahorro = ahorroRepository.findById(condicion.getAhorro().getId()).get();
        ahorro.setCondicion(condicionRepository.save(condicion));
        ahorroRepository.save(ahorro);
    }
    
}
