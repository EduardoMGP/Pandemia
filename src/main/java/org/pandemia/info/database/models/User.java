package org.pandemia.info.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.dao.UserDAO;
import org.pandemia.info.database.models.enums.Role;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(nullable = false, length = 250, unique = true, columnDefinition = "VARCHAR(250)")
    private String email;

    @Column(nullable = false, length = 32, columnDefinition = "VARCHAR(32)")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('RESIDENT', 'ADMIN', 'NURSE') DEFAULT 'RESIDENT'")
    private Role role;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String created_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updated_at;

    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private String deleted_at;

    @ManyToMany()
    @JoinTable(
            name = "users_neighborhood",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "neighborhood_id")
    )
    private List<Neighborhood> neighborhoods;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UsersVaccine> usersVaccines;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<CovidCase> covidCases;

    public User() {

    }

    public User(String name, String email, String password, Role role) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User validate(String email, String password) {

        User user = new UserDAO().findByEmail(email);
        if (user == null)
            return null;

        return user.getPassword().equals(password) ? user : null;
    }

    public static User register(String email, String password, String name) {

        if (email == null || password == null || name == null)
            throw new RuntimeException("Campos obrigatórios não preenchidos!");

        if (email.isEmpty() || password.isEmpty() || name.isEmpty())
            throw new RuntimeException("Campos obrigatórios não preenchidos!");

        if (email.length() > 250)
            throw new RuntimeException("Email muito longo!");

        if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
            throw new RuntimeException("Email inválido!");

        if (password.length() < 8)
            throw new RuntimeException("A senha deve conter no mínimo 8 caracteres!");

        if (name.length() < 3)
            throw new RuntimeException("Nome muito curto!");

        if (name.length() > 100)
            throw new RuntimeException("Nome muito longo!");

        User user = new UserDAO().findByEmail(email);
        if (user != null)
            throw new RuntimeException("Usuário já cadastrado!");

        user = new User(name, email, password, Role.RESIDENT);
        user.save();
        return user;
    }

    public void addCovidCase(CovidCase covidCase) {
        covidCases.add(covidCase);
        save();
    }
}
