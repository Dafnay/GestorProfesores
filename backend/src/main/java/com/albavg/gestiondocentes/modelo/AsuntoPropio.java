package com.albavg.gestiondocentes.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "docente")
@EqualsAndHashCode(exclude = "docente")
@Entity
@Table(name = "asunto_propio")
public class AsuntoPropio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate diaSolicitado;

    private LocalDate fechaTramitacion;

    private String descripcion;

    @Column(nullable = false)
    private Boolean aprobado = false;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;
}
