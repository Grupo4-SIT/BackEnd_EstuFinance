package com.estufinance.serverfinanceapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.model.domain.MetricaInversion;
import com.estufinance.serverfinanceapp.model.dto.InversionDTO;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.mapper.InversionMapper;
import com.estufinance.serverfinanceapp.service.InversionesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/inversiones")
public class InversionesController {

    private final InversionesService inversionesService;
    private final InversionMapper inversionMapper;

    /**
    Obtiene las métricas de una inversión especificada por su ID.
    @param idInversion El ID de la inversión de la que se desean obtener las métricas.
    @return Un objeto ResponseEntity con un cuerpo que contiene las métricas de la inversión y un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException Si la inversión especificada no existe.
    */
    @GetMapping("/metricas/{idInversion}")
    public ResponseEntity<MetricaInversion> handleGetMetricasInversion(@PathVariable final UUID idInversion)
            throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(idInversion)) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: " + idInversion);
        }
        final Inversion inversion = inversionesService.getInversionById(idInversion).get();
        final MetricaInversion metrica = inversionesService.getMetricaInversion(inversion);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    /**
    Obtiene las métricas de inversión para un portafolio especificado por su ID.
    @param idPortafolio El ID del portafolio del que se desean obtener las métricas de inversión.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y la lista de métricas de inversión obtenida.
    */    
    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaInversion>> handleGetMetricasInversiones(@RequestParam final UUID idPortafolio) {
        final List<Inversion> inversiones = inversionesService.findInversionesByPortafolioId(idPortafolio);
        final List<MetricaInversion> metricas = inversionesService
                .getMetricasInversiones(new HashSet<Inversion>(inversiones));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    /**
    Obtiene una página de inversiones de un portafolio especificado por su ID.
    @param page El número de página solicitado (por defecto es 0).
    @param size El tamaño de la página (por defecto es 9).
    @param idPortafolio El ID del portafolio al que pertenecen las inversiones.
    @return Un objeto ResponseEntity con una página de objetos InversionDTO que corresponden a las inversiones del portafolio especificado.
    */
    @GetMapping
    public ResponseEntity<Page<InversionDTO>> handleGetInversiones(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size,
            @RequestParam final UUID idPortafolio) {
        final Pageable paging = PageRequest.of(page, size);
        final Example<Inversion> example = Example
                .of(Inversion.builder().portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        final Page<Inversion> pageInversiones = inversionesService.getInversionesPaginateByPortafolio(paging, example);
        final Page<InversionDTO> pageInversionesDTO = new PageImpl<InversionDTO>(
                pageInversiones.map(inversionMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageInversionesDTO);
    }

    @GetMapping("/metricas/{idPortafolio}")
    public ResponseEntity<Boolean> handleHasPortafolioAnyInversion(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        final Boolean hasPortafolioAnyInversion = inversionesService.hasPortafolioAnyInversion(idPortafolio);
        return ResponseEntity.status(HttpStatus.OK).body(hasPortafolioAnyInversion);
    }

    /**
    Obtiene una inversión específica a partir de su ID.
    @param idInversion El ID de la inversión que se desea obtener.
    @return Un objeto ResponseEntity que contiene un objeto InversionDTO y un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException Si la inversión especificada no existe.
    */
    @GetMapping("/{idInversion}")
    public ResponseEntity<InversionDTO> handleGetInversionById(@PathVariable final UUID idInversion) {
        if (!inversionesService.inversionAlreadyExist(idInversion)) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: " + idInversion);
        }
        final Inversion inversion = inversionesService.getInversionById(idInversion).get();
        final InversionDTO inversionDTO = inversionMapper.pojoToDto(inversion);
        return ResponseEntity.status(HttpStatus.OK).body(inversionDTO);
    }

    /**
    Crea una nueva inversion en el sistema.
    @param inversionDTO El objeto InversionDTO que contiene los detalles de la nueva inversion a crear.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws IllegalStateException Si ya existe una inversion con el mismo nombre en el portafolio especificado.
    */
    @PostMapping
    public ResponseEntity<HttpStatus> handleCreateInversion(@RequestBody final InversionDTO inversionDTO)
            throws IllegalStateException {
        if (inversionesService.alreadyExistOnPortafolio(inversionDTO.getIdPortafolio(), inversionDTO.getNombre())) {
            throw new IllegalStateException("Similar portafolio already exist");
        }
        final Inversion inversion = inversionMapper.dtoToPojo(inversionDTO);
        inversionesService.createInversion(inversion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    Actualiza una inversion existente con la información proporcionada en un objeto InversionDTO.
    @param inversionDTO El objeto InversionDTO que contiene la información de la inversion a actualizar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException Si la inversion especificada no existe.
    */
    @PutMapping
    public ResponseEntity<HttpStatus> handleUpdateInversion(@RequestBody final InversionDTO inversionDTO)
            throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(inversionDTO.getId())) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: " + inversionDTO.getId());
        }
        final Inversion inversion = inversionMapper.dtoToPojo(inversionDTO);
        inversionesService.updateInversion(inversion);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Liquida una inversion especificada por su ID
     * @param inversionDTO El objeto InversionDTO que contiene el ID de la inversion a liquidar.
     * @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
     * @throws EntityNotFoundException Si la inversion especificada no existe.
     */
    @PatchMapping("/liquidar-inversion")
    public ResponseEntity<HttpStatus> handleDeleteInversionById(@RequestBody final InversionDTO inversionDTO, @RequestParam final UUID idUsuario)
            throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(inversionDTO.getId())) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: " + inversionDTO.getId());
        }
        final Inversion inversion = inversionMapper.dtoToPojo(inversionDTO);
        inversionesService.liquidarInversion(inversion, idUsuario);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
