package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.pandemia.info.database.dao.VaccineDAO;

@ToString
@Getter
@Setter
@Entity
@Table(name = "vaccines")
public class Vaccine extends VaccineDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(nullable = false, columnDefinition = "INT")
    private int stock;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String batch;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String manufacturer;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private String expiration_date;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private String fabrication_date;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updated_at;

    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private String deleted_at;

}
