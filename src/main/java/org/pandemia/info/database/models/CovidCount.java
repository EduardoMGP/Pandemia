package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.pandemia.info.database.dao.CovidCaseDAO;
import org.pandemia.info.database.dao.CovidCountDAO;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NamedNativeQueries({
        @NamedNativeQuery(name = "getCasesByDate",
                query = """
                            SELECT COUNT(*) AS confirmed, case_date AS date
                            FROM covid_cases
                            WHERE case_date BETWEEN :date AND :date1
                            GROUP BY case_date
                            ORDER BY case_date ASC
                        """,
                resultSetMapping = "CovidByDateMapping"
        ),
        @NamedNativeQuery(name = "getCasesCount",
                query = """
                         SELECT
                             COALESCE((SELECT COUNT(*) AS cases FROM covid_cases WHERE status = 'confirmed'), 0) AS confirmed_total,
                             COALESCE((SELECT COUNT(*) AS cases FROM covid_cases WHERE status = 'deceased'), 0) AS deceased_total,
                             COALESCE(SUM(CASE WHEN status = 'confirmed' THEN 1 ELSE 0 END), 0) AS confirmed,
                             COALESCE(SUM(CASE WHEN status = 'suspect' THEN 1 ELSE 0 END), 0) AS suspect,
                             COALESCE(SUM(CASE WHEN status = 'recovered' THEN 1 ELSE 0 END), 0) AS recovered,
                             COALESCE(SUM(CASE WHEN status = 'deceased' THEN 1 ELSE 0 END), 0) AS deceased
                         FROM covid_cases
                         WHERE case_date BETWEEN :date AND :date1
                         ORDER BY case_date ASC;
                        """,
                resultSetMapping = "CovidDataStatisticsMapping"
        ),
})
@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "CovidByDateMapping",
                        classes = @ConstructorResult(
                                targetClass = CovidCount.class,
                                columns = {
                                        @ColumnResult(name = "confirmed", type = Integer.class),
                                        @ColumnResult(name = "date", type = Date.class),
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "CovidDataStatisticsMapping",
                        classes = @ConstructorResult(
                                targetClass = CovidCount.class,
                                columns = {
                                        @ColumnResult(name = "confirmed_total", type = Integer.class),
                                        @ColumnResult(name = "deceased_total", type = Integer.class),
                                        @ColumnResult(name = "confirmed", type = Integer.class),
                                        @ColumnResult(name = "suspect", type = Integer.class),
                                        @ColumnResult(name = "recovered", type = Integer.class),
                                        @ColumnResult(name = "deceased", type = Integer.class),
                                }
                        )
                )
        }
)
@Entity
@Immutable
public class CovidCount extends CovidCountDAO {

    @Id
    private int id;

    private int confirmed_total;
    private int deceased_total;
    private int confirmed;
    private int suspect;
    private int recovered;
    private int deceased;
    private Date date;

    public CovidCount() {
    }

    public CovidCount(int confirmed_total, int deceased_total, int confirmed, int suspect, int recovered, int deceased) {
        this.confirmed_total = confirmed_total;
        this.deceased_total = deceased_total;
        this.confirmed = confirmed;
        this.suspect = suspect;
        this.recovered = recovered;
        this.deceased = deceased;
    }

    public CovidCount(int confirmed, Date date) {
        this.confirmed = confirmed;
        this.date = date;
    }

    public float getLethality() {
        if (confirmed_total == 0)
            return 0;
        return (float) deceased_total / (float) confirmed_total * 100;
    }
}
