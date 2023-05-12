package com.estufinance.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="objetivos")
public class Objetivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(length = 50)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;
    
    private LocalDateTime fechaEstimada;

    private BigDecimal monto;
    
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;

}
