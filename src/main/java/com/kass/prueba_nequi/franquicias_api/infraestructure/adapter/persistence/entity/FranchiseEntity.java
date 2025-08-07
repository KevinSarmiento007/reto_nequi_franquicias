package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "franquicias")
@Data
public class FranchiseEntity {
    @Id
    private Long id;
    @Column("nombre")
    private String name;
}
