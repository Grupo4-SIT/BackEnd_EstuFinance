package com.estufinance.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.enums.Expresion;
import com.estufinance.serverfinanceapp.model.entity.enums.TipoImporte;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="condicion")
public class Condicion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Expresion expresion;

    @Column(nullable = false, precision=30, scale=2)
    private BigDecimal importe;

    private Long cantidadDescontar;

    @Column(length = 25)
    private TipoImporte tipoImporte;

    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "ahorro_fk")
    private Ahorro ahorro;
}
