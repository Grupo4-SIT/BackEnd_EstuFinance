package com.estufinance.serverfinanceapp.service.Implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.estufinance.serverfinanceapp.model.entity.Ahorro;
import com.estufinance.serverfinanceapp.model.entity.Objetivo;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.repository.AhorroRepository;
import com.estufinance.serverfinanceapp.repository.ObjetivoRepository;
import com.estufinance.serverfinanceapp.repository.PortafoliosRepository;
import com.estufinance.serverfinanceapp.service.ObjetivoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObjetivoServiceImpl implements ObjetivoService {

    private final ObjetivoRepository objetivoRepository;
    private final AhorroRepository ahorroRepository;
    private final PortafoliosRepository portafoliosRepository; 

    @Override
    public Objetivo saveObjetivo(Objetivo objetivo) {
        return objetivoRepository.save(objetivo);
    }

    @Override
    public Objetivo getObjetivoById(UUID idObjetivo) {
        return objetivoRepository.getReferenceById(idObjetivo);
    }

    @Override
    public void deleteObjetivoById(UUID idObjetivo) {
        objetivoRepository.deleteById(idObjetivo);
    }

    @Override
    public void updateObjetivo(Objetivo objetivo) {
        objetivoRepository.save(objetivo);
    }

    @Override
    public List<Objetivo> findObjetivosBasedOnUserId(UUID idUsuario) {
        return objetivoRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public Boolean similarObjetivoExist(String name, UUID idUsuario) {
        Example<Objetivo> exampleObjetivos = Example.of(Objetivo.builder()
                                                                .nombre(name)
                                                                .usuario(Usuario.builder()
                                                                                .id(idUsuario)
                                                                                .build())
                                                                .build());
        return objetivoRepository.findOne(exampleObjetivos).isPresent();
    }

    @Override
    public Boolean isObjetivoOfUserDeletable(UUID idUsuario, UUID idObjetivo) {
        List<Ahorro> ahorrosWithObjetivo =  ahorroRepository.findAll()
                                                            .stream()
                                                            .filter(ahorro -> ahorro.getObjetivo().getId().equals(idObjetivo))
                                                            .toList();
        List<Portafolio> portafolioWithObjetivo =  portafoliosRepository.findAll()
                                                                    .stream()
                                                                    .filter(ahorro -> ahorro.getObjetivo().getId().equals(idObjetivo))
                                                                    .toList();
        
        return ahorrosWithObjetivo.isEmpty() && portafolioWithObjetivo.isEmpty();

        // return objetivoRepository.findObjetivoOfUserInAhorros(idUsuario, idObjetivo).isEmpty() 
        //         && objetivoRepository.findObjetivosInPortafoslioOfUser(idUsuario, idObjetivo).isEmpty();
    }

    @Override
    public Boolean objetivoExist(UUID idObjetivo) {
        return objetivoRepository.existsById(idObjetivo);
    }
    
}
