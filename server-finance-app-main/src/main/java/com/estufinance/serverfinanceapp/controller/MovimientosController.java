package com.estufinance.serverfinanceapp.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.model.domain.MetricaBalance;
import com.estufinance.serverfinanceapp.model.dto.MovimientoDTO;
import com.estufinance.serverfinanceapp.model.entity.Movimiento;
import com.estufinance.serverfinanceapp.model.entity.Presupuesto;
import com.estufinance.serverfinanceapp.model.entity.Usuario;
import com.estufinance.serverfinanceapp.model.mapper.MovimientoMapper;
import com.estufinance.serverfinanceapp.service.MovimientosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/movimientos")
public class MovimientosController {

    private final MovimientosService movimientosService;
    private final MovimientoMapper movimientoMapper;

    /**
    Obtiene la métrica de balance de un usuario especificado por su ID. La métrica de balance incluye el saldo
    actual del usuario y el total de ingresos y gastos registrados en su historial de movimientos.
    @param idUsuario El ID del usuario para el cual se quiere obtener la métrica de balance.
    @return Un objeto ResponseEntity con la métrica de balance del usuario y un código de estado HTTP que indica el
    resultado de la operación.
    */
    @GetMapping("/metrica/by-usuario")
    public ResponseEntity<MetricaBalance> handleGetMetricasBalance(@RequestParam final UUID idUsuario) {
        final MetricaBalance metrica = movimientosService.getMetricaBalanceByUsuario(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/has-any-movimiento/by-usuario")
    public ResponseEntity<Boolean> handleHasAnyMovimientoByUsuario(@RequestParam final UUID idUsuario) {
        final Boolean hasAnyMovimiento = movimientosService.hasAnyMovimientoByUsuario(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(hasAnyMovimiento);
    }

    @GetMapping("/has-any-movimiento/by-presupuesto")
    public ResponseEntity<Boolean> handleHasAnyMovimientoByPresupuesto(@RequestParam final UUID idPresupuesto) {
        final Boolean hasAnyMovimiento = movimientosService.hasAnyMovimientoByPresupuesto(idPresupuesto);
        return ResponseEntity.status(HttpStatus.OK).body(hasAnyMovimiento);
    }

    /**
    Obtiene una página de los movimientos registrados para un usuario especificado por su ID. La página incluirá una
    lista de objetos MovimientoDTO, que son los movimientos convertidos a un formato más legible y fácil de usar.
    @param page El número de página que se quiere obtener (por defecto es 0).
    @param size El tamaño de la página que se quiere obtener (por defecto es 9).
    @param idUsuario El ID del usuario para el cual se quieren obtener los movimientos.
    @return Un objeto ResponseEntity con la página de movimientos y un código de estado HTTP que indica el resultado
    de la operación.
    */
    @GetMapping("/by-usuario")
    public ResponseEntity<Page<MovimientoDTO>> handleGetMovimientosByUsuario(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size,
            @RequestParam final UUID idUsuario) {
        final Pageable paging = PageRequest.of(page, size);
        final Example<Movimiento> example = Example
                .of(Movimiento.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        final Page<Movimiento> pageMovimientos = movimientosService.getMovimientosPaginateByUsuario(paging, example);
        final Page<MovimientoDTO> pageMovimientosDTO = new PageImpl<MovimientoDTO>(
                pageMovimientos.map(movimientoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageMovimientosDTO);
    }

    /**
    Obtiene una página de los movimientos asociados a un presupuesto especificado por su ID. Se puede especificar el número
    de página y la cantidad de resultados por página usando los parámetros "page" y "size".
    @param page El número de página a obtener. El valor por defecto es 0.
    @param size La cantidad de resultados por página. El valor por defecto es 9.
    @param idPresupuesto El ID del presupuesto para el cual se quieren obtener los movimientos asociados.
    @return Un objeto ResponseEntity con una página de los movimientos asociados al presupuesto especificado y un código
    de estado HTTP que indica el resultado de la operación.
    */
    @GetMapping("/by-presupuesto")
    public ResponseEntity<Page<MovimientoDTO>> handleGetMovimientosByPresupuesto(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size,
            @RequestParam final UUID idPresupuesto) {
        final Pageable paging = PageRequest.of(page, size);
        final Example<Movimiento> example = Example
                .of(Movimiento.builder().presupuesto(Presupuesto.builder().id(idPresupuesto).build()).build());
        final Page<Movimiento> pageMovimientos = movimientosService.getMovimientosPaginateByPortafolio(paging, example);
        final Page<MovimientoDTO> pageMovimientosDTO = new PageImpl<MovimientoDTO>(
                pageMovimientos.map(movimientoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageMovimientosDTO);
    }

    /**
    Obtiene un movimiento por su ID.
    @param idMovimiento El ID del movimiento que se quiere obtener.
    @return Un objeto ResponseEntity con el movimiento solicitado en formato DTO y un código de estado HTTP que indica el
    resultado de la operación.
    @throws EntityNotFoundException Si el movimiento con el ID especificado no existe.
    */
    @GetMapping("/by-movimiento")
    public ResponseEntity<MovimientoDTO> handleGetMovimientoById(@RequestParam final UUID idMovimiento) {
        if (!movimientosService.movimientoAlreadyExist(idMovimiento)) {
            throw new EntityNotFoundException("Movimiento do not exist. UUID: " + idMovimiento);
        }
        final Movimiento movimiento = movimientosService.getMovimientoById(idMovimiento).get();
        final MovimientoDTO movimientoDTO = movimientoMapper.pojoToDto(movimiento);
        return ResponseEntity.status(HttpStatus.OK).body(movimientoDTO);
    }



    /**
    Crea un nuevo movimiento asociado a un presupuesto. El movimiento debe ser no contabilizable y debe tener
    especificado un ID de presupuesto. Si el movimiento es contabilizable o no tiene un ID de presupuesto, se
    lanzará una excepción.
    @param movimientoDTO El DTO del movimiento que se quiere crear.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws IllegalStateException Si el movimiento es contabilizable o no tiene un ID de presupuesto.
    */
    @PostMapping("/presupuesto")
    public ResponseEntity<HttpStatus> handleCreateMovimientoOfPresupuesto(
            @RequestBody final MovimientoDTO movimientoDTO)
            throws IllegalStateException {
        final Movimiento movimiento = movimientoMapper.dtoToPojo(movimientoDTO);
        if (movimientoDTO.getContabilizable().equals(true) || movimientoDTO.getIdPresupuesto() == null) {
            throw new IllegalStateException(
                    "Can not save this movimiento. Must not be contabilizable");
        }
        movimientosService.createMovimientoOfPresupuesto(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    Crea un nuevo movimiento, ya sea un ingreso o un gasto. Si se especifica "aplicaDescuentoEspecifico" como verdadero y el
    movimiento es un ingreso, se aplica un descuento a una cuenta de ahorro específica (especificada por "idCuentaAhorroEspecifica").
    @param movimientoDTO El objeto DTO que representa el movimiento a crear.
    @param aplicaDescuentoEspecifico Booleano que indica si se aplica un descuento a una cuenta de ahorro específica.
    @param idCuentaAhorroEspecifica El ID de la cuenta de ahorro específica a la que se le aplicará el descuento (si "aplicaDescuentoEspecifico" es verdadero).
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    */
    @PostMapping
    public ResponseEntity<HttpStatus> handleCreateMovimiento(
            @RequestBody final MovimientoDTO movimientoDTO,
            @RequestParam final Boolean aplicaDescuentoEspecifico,
            @RequestParam(required = false) final UUID idCuentaAhorroEspecifica) {
        final Movimiento movimiento = movimientoMapper.dtoToPojo(movimientoDTO);
        if (aplicaDescuentoEspecifico) {
            movimientosService.createMovimientoDescuentoACuentaEspecifica(movimiento, idCuentaAhorroEspecifica);
        } else {
            movimientosService.createMovimiento(movimiento);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    Elimina un movimiento existente en la base de datos.
    @param movimientoDTO El objeto DTO del movimiento a eliminar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws EntityNotFoundException si no existe ningún movimiento con el ID especificado en el objeto DTO.
    @throws IllegalCallerException si el movimiento es contabilizable y no se puede eliminar.
    */
    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeleteMovimiento(@RequestBody final MovimientoDTO movimientoDTO)
            throws EntityNotFoundException, IllegalCallerException {
        if (!movimientosService.movimientoAlreadyExist(movimientoDTO.getId())) {
            throw new EntityNotFoundException("Movimiento do not exist. UUID: " + movimientoDTO.getId());
        } else if (movimientoDTO.getContabilizable().equals(true)) {
            throw new IllegalCallerException(
                    "Can not delete this movimiento. UUID: " + movimientoDTO.getId() + " is contabilizable");
        }
        movimientosService.deleteMovimientoById(movimientoDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
    Elimina una lista de movimientos existentes en la base de datos.
    @param movimientosDTO Una lista de objetos DTO de los movimientos a eliminar.
    @return Un objeto ResponseEntity con un código de estado HTTP que indica el resultado de la operación.
    @throws IllegalCallerException si alguno de los movimientos es contabilizable y no se puede eliminar.
    */
    @DeleteMapping("/all-by-id")
    public ResponseEntity<HttpStatus> handleDeleteListOfMovimientosByIds(
            @RequestBody final List<MovimientoDTO> movimientosDTO) throws IllegalCallerException {
        final List<Movimiento> movimientos = movimientosDTO.stream().map(movimientoMapper::dtoToPojo).toList();
        movimientos.stream().forEach(movimiento -> {
            if (movimiento.getContabilizable().equals(true)) {
                throw new IllegalCallerException(
                        "Can not delete this movimiento. UUID: " + movimiento.getId() + " is contabilizable");
            }
        });
        movimientosService.deleteAllMovimientos(movimientos);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
