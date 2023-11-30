package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.pandemia.info.database.dao.VaccineStatisticsDAO;

@Getter
@Setter
@NamedNativeQueries({
        @NamedNativeQuery(name = "getVaccinesStatistics",
                query = """
                        SELECT vaccines.name, COUNT(users_vaccines.vaccine_id) as count_vaccine
                        FROM users_vaccines
                        INNER JOIN vaccines
                        ON vaccines.id = users_vaccines.vaccine_id
                        INNER JOIN users
                        ON users.id = users_vaccines.user_id
                        GROUP BY users_vaccines.vaccine_id;
                        """,
                resultSetMapping = "VaccineDataStatisticsMapping"
        ),
})
@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "VaccineDataStatisticsMapping",
                        classes = @ConstructorResult(
                                targetClass = VaccineStatistics.class,
                                columns = {
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "count_vaccine", type = Integer.class)
                                }
                        )
                )
        }
)
@Entity
@Immutable
public class VaccineStatistics extends VaccineStatisticsDAO {

    @Id
    private int id;

    private String name;
    private int count_vaccine;

    public VaccineStatistics(String name, int count_vaccine) {
        this.name = name;
        this.count_vaccine = count_vaccine;
    }

    public VaccineStatistics() {
    }

}
