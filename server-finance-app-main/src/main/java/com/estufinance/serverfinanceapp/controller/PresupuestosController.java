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

import com.estufinance.serverfinanceapp.model.domain.MetricaPresupuesto;
import com.estufinance.serverfinanceapp.model.dto.PresupuestoDTO;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;
import com.estufinance.serverfinanceapp.model.entity.Presupuesto;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.mapper.PresupuestoMapper;
import com.estufinance.serverfinanceapp.service.MovimientosService;
import com.estufinance.serverfinanceapp.service.PresupuestosService;
import com.estufinance.serverfinanceapp.service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/presupuestos")
public class PresupuestosController {

    private final PresupuestosService presupuestosService;
    private final MovimientosService movimientosService;
    private final UsuariosService usuariosService;
    private final PresupuestoMapper presupuestoMapper;

    /**
    Retorna un objeto ResponseEntity con la MetricaPresupuesto asociada al ID de Presupuesto proporcionado en el path.
    @param idPresupuesto El UUID del presupuesto para buscar la métrica.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y el objeto MetricaPresupuesto.
    @throws EntityNotFoundException si no existe ningún presupuesto con el ID especificado.
    */
    @GetMapping("/metricas/{idPresupuesto}")
    public ResponseEntity<MetricaPresupuesto> handleGetMetricaPresupuesto(@PathVariable final UUID idPresupuesto)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(idPresupuesto)) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + idPresupuesto);
        }
        final List<Movimiento> movimientos = movimientosService.findMovimientosByPresupuestoId(idPresupuesto);
        final MetricaPresupuesto metrica = presupuestosService
                .getMetricaPresupuestoByMovimientos(new HashSet<Movimiento>(movimientos), idPresupuesto);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    /**
    Retorna un objeto ResponseEntity con una lista de objetos MetricaPresupuesto asociados al usuario con el nombre de usuario proporcionado como parámetro.
    @param username El nombre de usuario del usuario del cual se quieren obtener las métricas de los presupuestos.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y una lista de objetos MetricaPresupuesto.
    @throws NoSuchElementException si no existe ningún Usuario con el nombre de usuario proporcionado.
    */
    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaPresupuesto>> handleGetMetricasPresupuestos(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Presupuesto> presupuestos = presupuestosService.getPresupuestosByUsuario(usuario);
        final List<MetricaPresupuesto> metricas = presupuestosService
                .getMetricasPresupuestos(new HashSet<Presupuesto>(presupuestos));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    /**
    Retorna un objeto ResponseEntity con una lista de objetos PresupuestoDTO asociados al nombre de usuario proporcionado.
    @param username El nombre de usuario cuyos presupuestos se buscan.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y una lista de objetos PresupuestoDTO.
    @throws NoSuchElementException si no existe ningún usuario con el nombre de usuario especificado.
    */
    @GetMapping("/all")
    public ResponseEntity<List<PresupuestoDTO>> handleGetListPresupuestos(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Presupuesto> presupuestos = presupuestosService.getPresupuestosByUsuario(usuario);
        final List<PresupuestoDTO> portafoliosDTO = presupuestos.stream().map(presupuestoMapper::pojoToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(portafoliosDTO);
    }

    /**
    Retorna un objeto ResponseEntity con una página de objetos PresupuestoDTO paginados.
    @param page El número de página deseado. Por defecto, el valor es 0.
    @param size El tamaño de la página deseada. Por defecto, el valor es 9.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y una página de objetos PresupuestoDTO.
    */
    @GetMapping
    public ResponseEntity<Page<PresupuestoDTO>> handleGetPresupuestosPaginate(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size) {
        final Pageable paging = PageRequest.of(page, size);
        final Page<Presupuesto> pagePresupuestos = presupuestosService.getPresupuestosPaginate(paging);
        final Page<PresupuestoDTO> pagePresupuestosDTO = new PageImpl<PresupuestoDTO>(
                pagePresupuestos.map(presupuestoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagePresupuestosDTO);
    }

    /**
    Retorna un objeto ResponseEntity con un objeto PresupuestoDTO que corresponde al presupuesto con el id proporcionado como parámetro.
    @param idPresupuesto El UUID del presupuesto del cual se quiere obtener la información.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación y un objeto PresupuestoDTO.
    @throws EntityNotFoundException si no existe ningún Presupuesto con el UUID proporcionado.
    */
    @GetMapping("/{idPresupuesto}")
    public ResponseEntity<PresupuestoDTO> handleGetPresupuestoById(@PathVariable final UUID idPresupuesto)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(idPresupuesto)) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + idPresupuesto);
        }
        final Presupuesto presupuesto = presupuestosService.getPresupuestoById(idPresupuesto).get();
        final PresupuestoDTO presupuestoDTO = presupuestoMapper.pojoToDto(presupuesto);
        return ResponseEntity.status(HttpStatus.OK).body(presupuestoDTO);
    }

    /**
    Crea un nuevo presupuesto con la información proporcionada en el cuerpo de la solicitud.
    @param presupuestoDTO El objeto PresupuestoDTO con la información del presupuesto a crear.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws IllegalStateException si ya existe un presupuesto similar al que se está tratando de crear.
    */
    @PostMapping
    public ResponseEntity<HttpStatus> handleCreatePresupuesto(@RequestBody final PresupuestoDTO presupuestoDTO)
            throws IllegalStateException {
        if (presupuestosService.similarAlreadyExist(presupuestoDTO.getNombre(), presupuestoDTO.getIdUsuario())) {
            throw new IllegalStateException("Similar presupuesto already exist");
        }
        final Presupuesto presupuesto = presupuestoMapper.dtoToPojo(presupuestoDTO);
        presupuestosService.createPresupuesto(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    Actualiza un objeto Presupuesto con los valores proporcionados en el objeto PresupuestoDTO pasado como parámetro.
    @param presupuestoDTO El objeto PresupuestoDTO con los nuevos valores del Presupuesto a actualizar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException si no existe ningún Presupuesto con el UUID proporcionado en el objeto PresupuestoDTO.
    */
    @PutMapping
    public ResponseEntity<HttpStatus> handleGetUpdatePresupuestoById(@RequestBody final PresupuestoDTO presupuestoDTO)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(presupuestoDTO.getId())) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + presupuestoDTO.getId());
        }
        final Presupuesto presupuesto = presupuestoMapper.dtoToPojo(presupuestoDTO);
        presupuestosService.updatePresupuesto(presupuesto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
    Elimina el Presupuesto correspondiente al identificador proporcionado en el cuerpo de la petición.
    @param presupuestoDTO El objeto PresupuestoDTO que contiene el identificador del presupuesto a eliminar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException si no existe ningún Presupuesto con el identificador proporcionado.
    */
    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeletePresupuestoById(@RequestBody final PresupuestoDTO presupuestoDTO)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(presupuestoDTO.getId())) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + presupuestoDTO.getId());
        }
        presupuestosService.deletePresupuestoById(presupuestoDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
