package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.pandemia.info.database.dao.CovidCaseDAO;
import org.pandemia.info.database.models.enums.CaseStatus;

@Getter
@Setter
@Entity
@Table(name = "covid_cases")
public class CovidCase extends CovidCaseDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "neighborhood_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Neighborhood neighborhood;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false, columnDefinition = "DATE NOT NULL")
    private String case_date;

    @Column(nullable = false, columnDefinition = "VARCHAR(250) NOT NULL")
    private String symptoms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('suspect', 'confirmed', 'recovered', 'deceased') DEFAULT 'suspect'")
    private CaseStatus status;

    @Column(columnDefinition = "INT DEFAULT 0")
    private boolean in_quarantine;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updated_at;

    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private String deleted_at;

}
