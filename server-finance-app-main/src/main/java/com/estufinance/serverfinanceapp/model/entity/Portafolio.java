package com.estufinance.serverfinanceapp.model.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="portafolio")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 150, nullable = false)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy="portafolio", cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Inversion> inversiones;

    @ManyToOne(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "objetivo")
    private Objetivo objetivo;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;
    
}

