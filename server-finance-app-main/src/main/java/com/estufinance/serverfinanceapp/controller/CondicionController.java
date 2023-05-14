package com.estufinance.serverfinanceapp.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estufinance.serverfinanceapp.model.dto.CondicionDTO;
import com.estufinance.serverfinanceapp.model.entity.Condicion;
import com.estufinance.serverfinanceapp.model.mapper.CondicionMapper;
import com.estufinance.serverfinanceapp.service.AhorrosService;
import com.estufinance.serverfinanceapp.service.CondicionesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/condiciones")
@RequiredArgsConstructor
@Validated
public class CondicionController {
    
    private final CondicionMapper condicionMapper;
    private final CondicionesService condicionesService;
    private final AhorrosService ahorrosService;

    @PostMapping
    public ResponseEntity<HttpStatus> createCondicion(@RequestBody CondicionDTO condicionDTO) {
        Condicion condicion = condicionMapper.dtoToPojo(condicionDTO);
        condicionesService.save(condicion);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete-condicion")
    public ResponseEntity<HttpStatus> deleteCondicion(@RequestBody UUID idAhorro) {
        if(!ahorrosService.ahorroExistById(idAhorro)) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        condicionesService.deleteCondicion(idAhorro);
        ahorrosService.unableCondicion(idAhorro);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
