package com.estufinance.serverfinanceapp.model.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.estufinance.serverfinanceapp.model.entity.enums.PeriodoPresupuesto;

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
import jakarta.persistence.OneToMany;
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
@Table(name="presupuestos")
public class Presupuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    
    @Column(length = 150, nullable = false)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;
    
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private PeriodoPresupuesto periodo;
    
    @ManyToOne(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    
    @OneToMany(mappedBy="presupuesto", cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Movimiento> movimientos;
    
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;

}
