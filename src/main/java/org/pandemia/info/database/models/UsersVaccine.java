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
@Table(name = "users_vaccines")
public class UsersVaccine extends VaccineDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "INT")
    private int user_id;

    @Column(nullable = false, columnDefinition = "INT")
    private int vaccine_id;

    @Column(nullable = false, columnDefinition = "INT")
    private int dose;

    @Column(nullable = false, columnDefinition = "DATE")
    private String date;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updated_at;

    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private String deleted_at;

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;
}
