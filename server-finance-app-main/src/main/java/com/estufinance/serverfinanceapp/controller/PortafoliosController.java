package com.estufinance.serverfinanceapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.model.domain.MetricaPortafolio;
import com.estufinance.serverfinanceapp.model.dto.PortafolioDTO;
import com.estufinance.serverfinanceapp.model.entity.Inversion;
import com.estufinance.serverfinanceapp.model.entity.Portafolio;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.mapper.PortafolioMapper;
import com.estufinance.serverfinanceapp.service.InversionesService;
import com.estufinance.serverfinanceapp.service.PortafoliosService;
import com.estufinance.serverfinanceapp.service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/portafolios")
public class PortafoliosController {

    private final PortafoliosService portafoliosService;
    private final InversionesService inversionesService;
    private final UsuariosService usuariosService;
    private final PortafolioMapper portafolioMapper;

    /**
    Obtiene la métrica de un portafolio a partir de su ID.
    @param idPortafolio El UUID del portafolio del cual se desea obtener la métrica.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación, y el objeto MetricaPortafolio que representa la métrica del portafolio.
    @throws EntityNotFoundException si no existe ningún portafolio con el ID especificado.
    */
    @GetMapping("/metricas/{idPortafolio}")
    public ResponseEntity<MetricaPortafolio> handleGetMetricaPortafolio(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final List<Inversion> inversiones = inversionesService.findInversionesByPortafolioId(idPortafolio);
        final MetricaPortafolio metrica = portafoliosService
                .getMetricaPortafolioByInversiones(new HashSet<Inversion>(inversiones));
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    /**
    Obtiene las métricas de todos los portafolios de un usuario.
    @param username El nombre de usuario del usuario del que se quieren obtener las métricas.
    @return Un objeto ResponseEntity que contiene una lista de las métricas de los portafolios del usuario.
    */
    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaPortafolio>> handleGetMetricasPortafolios(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Portafolio> portafolios = portafoliosService.getPortafoliosByUsuario(usuario);
        final List<MetricaPortafolio> metricas = portafoliosService
                .getMetricasPortafolios(new HashSet<Portafolio>(portafolios));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    /**
    Obtiene una lista de objetos DTO de los portafolios pertenecientes a un usuario dado.
    @param username El nombre de usuario del usuario propietario de los portafolios.
    @return Un objeto ResponseEntity que contiene una lista de objetos PortafolioDTO y un código de estado HTTP que indica el resultado de la operación.
    */
    @GetMapping("/all")
    public ResponseEntity<List<PortafolioDTO>> handleGetListPortafolios(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Portafolio> portafolios = portafoliosService.getPortafoliosByUsuario(usuario);
        final List<PortafolioDTO> portafoliosDTO = portafolios.stream().map(portafolioMapper::pojoToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(portafoliosDTO);
    }

    /**
    Obtiene una página de PortafolioDTOs de la base de datos, según los parámetros de paginación especificados.
    @param page El número de página a obtener (comenzando en 0). Valor predeterminado es 0.
    @param size El tamaño de la página a obtener. Valor predeterminado es 9.
    @return Un objeto ResponseEntity con una página de PortafolioDTOs y un código de estado HTTP que indica el resultado de la operación.
    */
    @GetMapping
    public ResponseEntity<Page<PortafolioDTO>> handleGetPortafoliosPaginate(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size) {
        final Pageable paging = PageRequest.of(page, size);
        final Page<Portafolio> pagePortafolios = portafoliosService.getPortafoliosPaginate(paging);
        final Page<PortafolioDTO> pagePortafoliosDTO = new PageImpl<PortafolioDTO>(
                pagePortafolios.map(portafolioMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagePortafoliosDTO);
    }

    /**
    Retorna un objeto ResponseEntity con el PortafolioDTO asociado al id proporcionado en el path.
    @param idPortafolio El UUID del Portafolio a buscar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y el objeto PortafolioDTO.
    @throws EntityNotFoundException si no existe ningún Portafolio con el ID especificado.
    */
    @GetMapping("/{idPortafolio}")
    public ResponseEntity<PortafolioDTO> handleGetPortafolioById(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final Portafolio portafolio = portafoliosService.getPortafolioById(idPortafolio).get();
        final PortafolioDTO portafolioDTO = portafolioMapper.pojoToDto(portafolio);
        return ResponseEntity.status(HttpStatus.OK).body(portafolioDTO);
    }

    /**
    Crea un nuevo Portafolio con la información proporcionada en el cuerpo de la solicitud.
    @param portafolioDTO El objeto PortafolioDTO con la información del Portafolio a crear.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws IllegalStateException si ya existe un Portafolio con un nombre similar para el usuario especificado.
    */
    @PostMapping
    public ResponseEntity<HttpStatus> handleCreatePortafolio(@RequestBody final PortafolioDTO portafolioDTO)
            throws IllegalStateException {
        if (portafoliosService.similarAlreadyExist(portafolioDTO.getNombre(), portafolioDTO.getIdUsuario())) {
            throw new IllegalStateException("Similar portafolio already exist");
        }
        final Portafolio portafolio = portafolioMapper.dtoToPojo(portafolioDTO);
        portafoliosService.createPortafolio(portafolio);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    Actualiza un objeto Portafolio en la base de datos y retorna un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @param portafolioDTO El objeto PortafolioDTO con la información actualizada del Portafolio.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException si no existe ningún Portafolio con el ID especificado en el PortafolioDTO.
    */
    @PutMapping
    public ResponseEntity<HttpStatus> handleGetUpdatePortafolioById(@RequestBody final PortafolioDTO portafolioDTO)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(portafolioDTO.getId())) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + portafolioDTO.getId());
        }
        final Portafolio portafolio = portafolioMapper.dtoToPojo(portafolioDTO);
        portafoliosService.updatePortafolio(portafolio);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
    Retorna un objeto ResponseEntity con un valor booleano que indica si el Portafolio con el ID proporcionado tiene o no alguna inversión asociada.
    @param idPortafolio El UUID del Portafolio a buscar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y un valor booleano que indica si el Portafolio tiene o no alguna inversión asociada.
    @throws EntityNotFoundException si no existe ningún Portafolio con el ID especificado.
    */
    @GetMapping("/has-any-inversion/{idPortafolio}")
    public ResponseEntity<Boolean> handleHasAnyInversionByPortafolioId(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final Boolean hasAnyInversion = portafoliosService.hasAnyInversion(idPortafolio);
        return ResponseEntity.status(HttpStatus.OK).body(hasAnyInversion);
    }

    @GetMapping("/has-any-portafolio/{idUsuario}")
    public ResponseEntity<Boolean> handleHasAnyPortafolioByUsuario(@PathVariable final UUID idUsuario) {
        final Boolean hasAnyPortafolio = portafoliosService.hasAnyPortafolio(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(hasAnyPortafolio);
    }

    /**
    Elimina un Portafolio según su ID.
    @param portafolioDTO El objeto PortafolioDTO que contiene el UUID del Portafolio a eliminar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException si no existe ningún Portafolio con el ID especificado.
    @throws IllegalCallerException si el Portafolio a eliminar tiene alguna Inversión asociada.
    */
    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeletePortafolioById(@RequestBody final PortafolioDTO portafolioDTO)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(portafolioDTO.getId())) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + portafolioDTO.getId());
        } else if (portafoliosService.hasAnyInversion(portafolioDTO.getId())) {
            throw new IllegalCallerException(
                    "Can not delete this portafolio. UUID: " + portafolioDTO.getId() + " has inversiones");
        }
        portafoliosService.deletePortafolioById(portafolioDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
