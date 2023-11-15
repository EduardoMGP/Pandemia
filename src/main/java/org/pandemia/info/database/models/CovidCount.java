package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.Date;

@Getter
@Setter
@NamedNativeQueries({
        @NamedNativeQuery(name = "getCasesByDate",
                query = """
                    SELECT COUNT(*) AS confirmed, covid_case_date AS date
                    FROM covid_cases
                    WHERE covid_case_date BETWEEN :date AND :date1
                    GROUP BY covid_case_date
                    ORDER BY covid_case_date ASC
                """,
                resultSetMapping = "CovidByDateMapping"
        ),
        @NamedNativeQuery(name = "getCasesCount",
                query = """
                        SELECT
                            (SELECT COUNT(*) AS cases FROM covid_cases WHERE covid_case_status = 'confirmed') AS confirmed_total,
                            (SELECT COUNT(*) AS cases FROM covid_cases WHERE covid_case_status = 'deceased') AS deceased_total,
                            SUM(CASE WHEN covid_case_status = 'confirmed' THEN 1 ELSE 0 END) AS confirmed,
                            SUM(CASE WHEN covid_case_status = 'suspect' THEN 1 ELSE 0 END) AS suspect,
                            SUM(CASE WHEN covid_case_status = 'recovered' THEN 1 ELSE 0 END) AS recovered,
                            SUM(CASE WHEN covid_case_status = 'deceased' THEN 1 ELSE 0 END) AS deceased
                        FROM covid_cases
                        WHERE covid_case_date BETWEEN :date AND :date1
                        ORDER BY covid_case_date ASC
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
public class CovidCount {

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
        return (float) deceased_total / (float) confirmed_total * 100;
    }
}
