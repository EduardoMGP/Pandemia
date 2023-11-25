package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.pandemia.info.database.dao.NeighborgoodDAO;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "neighborhoods")
public class Neighborhood extends NeighborgoodDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String city;

    @Column(nullable = false, length = 2, columnDefinition = "VARCHAR(2)")
    private String state;

    @Column(nullable = false, length = 2, columnDefinition = "VARCHAR(2)")
    private String country;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String latitude;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String longitude;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updated_at;

    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private String deleted_at;

    @ManyToMany(mappedBy = "neighborhoods")
    private List<User> users;
}
