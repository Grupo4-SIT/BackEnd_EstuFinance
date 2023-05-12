package com.estufinance.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.estufinance.serverfinanceapp.model.entity.enums.TipoMovimiento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="movimientos")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, precision=30, scale=2)
    private BigDecimal importe;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @Column(length = 50, nullable = false)
    private String concepto;

    @Column(length = 50)
    private String logoConcepto;

    @ManyToOne(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario", nullable = true)
    private Usuario usuario;

    @ManyToOne(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "presupuesto", nullable = true)
    private Presupuesto presupuesto;

    @Column(nullable = false)
    private Boolean contabilizable;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;
    
}
