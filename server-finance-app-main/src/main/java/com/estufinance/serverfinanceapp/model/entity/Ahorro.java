package com.estufinance.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.estufinance.serverfinanceapp.model.entity.enums.TipoAhorro;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="ahorros")
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    private String descripcion;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private TipoAhorro tipo;

    @Column(nullable = false, precision=30, scale=2)
    private BigDecimal importe;

    private boolean automatico;

    @ManyToOne
    @JoinColumn(name = "objetivo_fk")
    private Objetivo objetivo;

    @OneToOne(mappedBy = "ahorro", cascade = CascadeType.REMOVE)
    private Condicion condicion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;
}
