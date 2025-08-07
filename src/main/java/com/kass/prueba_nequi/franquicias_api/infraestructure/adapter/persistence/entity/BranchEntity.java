package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "sucursales")
@Data
public class BranchEntity {
    @Id
    private Long id;

    @Column("franquicia_id")
    private Long franchiseId;

    @Column("nombre")
    private String name;
}
