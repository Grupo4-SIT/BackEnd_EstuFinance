package com.estufinance.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.domain.ConditionHandler;
import com.estufinance.serverfinanceapp.model.entity.Ahorro;
import com.estufinance.serverfinanceapp.model.entity.Condicion;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.AhorroRepository;
import com.estufinance.serverfinanceapp.repository.CondicionRepository;
import com.estufinance.serverfinanceapp.service.CondicionesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CondicionesServiceImpl implements CondicionesService {

    private final ConditionHandler conditionHandler;
    private final CondicionRepository conditionRepository;
    private final AhorroRepository ahorroRepository;


    @Override
    public void save(Condicion condicion) {
        conditionRepository.save(condicion);
    }

    @Override
    public Movimiento applyCondicionIfExist(Movimiento movimiento) {
        Example<Ahorro> example = Example.of(Ahorro.builder().automatico(true).usuario(Usuario.builder().id(movimiento.getUsuario().getId()).build()).build());
        List<Ahorro> ahorros = ahorroRepository.findAll(example);
        if (!ahorros.isEmpty()) {
            Ahorro ahorro = ahorros.stream().findFirst().get();
            Condicion condicion = ahorro.getCondicion();
            if(!conditionHandler.fullfitCondition(movimiento.getImporte(),
                condicion))
            {
                movimiento.setImporte(BigDecimal.valueOf(0.0));
            }else{
                movimiento.setImporte(conditionHandler.buildConditionBasedOn(movimiento.getImporte(),
                ahorro));
            }
        }
        return movimiento;
    }

    @Override
    public Movimiento applyCondicionToSpecificAhorro(Movimiento movimiento, UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
        if(!conditionHandler.fullfitCondition(movimiento.getImporte(),
                 ahorro.getCondicion())) 
        {
            movimiento.setImporte(BigDecimal.valueOf(0.0));
        }else{
            movimiento.setImporte(conditionHandler.buildConditionBasedOn(movimiento.getImporte(), ahorro));
        }
        return movimiento;
    }

    @Override
    public void deleteCondicion(UUID idAhorro) {
        Example<Condicion> exampleCondicion = Example.of(Condicion.builder()
                                                                  .ahorro(Ahorro.builder()
                                                                                .id(idAhorro)
                                                                                .build())
                                                                  .build());                                                                                                                                    
        conditionRepository.delete(conditionRepository.findOne(exampleCondicion).get());
    }
    
}
