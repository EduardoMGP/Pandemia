package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.Neighborhood;
import org.pandemia.info.database.models.User;
import org.pandemia.info.database.models.enums.Role;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ResidentAddController implements Initializable {
    public Button btnRegister;
    public TextField inputName;
    public ChoiceBox<Neighborhood> selectNeighborhood;
    public ChoiceBox<Role> selectRole;
    public TextField inputEmail;
    public PasswordField inputPassword;

    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Neighborhood> neighborhoods = new Neighborhood().findAll(Neighborhood.class);
        this.selectNeighborhood.getItems().addAll(neighborhoods);
        if (neighborhoods.size() > 0) {
            this.selectNeighborhood.setValue(neighborhoods.get(0));
            this.selectNeighborhood.setConverter(new StringConverter<>() {
                @Override
                public String toString(Neighborhood object) {
                    return object.getName();
                }

                @Override
                public Neighborhood fromString(String string) {
                    return neighborhoods.stream().filter(neighborhood -> neighborhood.getName().equals(string)).findFirst().orElse(null);
                }
            });
        }

        this.selectRole.getItems().add(Role.ADMIN);
        this.selectRole.getItems().add(Role.RESIDENT);
        this.selectRole.setValue(Role.RESIDENT);
        this.selectRole.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role object) {
                return object.getName();
            }

            @Override
            public Role fromString(String string) {
                return Role.fromString(string);
            }
        });

        this.btnRegister.setOnAction(e -> btnRegisterAction(true));

    }

    public void btnRegisterAction(boolean redirect) {
        if (
                this.user == null && (
                        this.selectNeighborhood.getValue() == null
                                || this.selectRole.getValue() == null
                                || this.inputName.getText().isEmpty()
                                || this.inputEmail.getText().isEmpty()
                                || (this.user == null && this.inputPassword.getText().isEmpty())
                )
        ) {
            Utils.showAlert("Erro", "Erro ao cadastrar usuário", "Preencha todos os campos", Alert.AlertType.ERROR);
        } else if (!this.inputEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Utils.showAlert("Erro", "Erro ao cadastrar usuário", "Email inválido", Alert.AlertType.ERROR);
        } else if (user == null && new User().findByEmail(this.inputEmail.getText()) != null) {
            Utils.showAlert("Erro", "Erro ao cadastrar usuário", "Email já cadastrado", Alert.AlertType.ERROR);
        } else {

            if (this.user == null)
                this.user = new User();
            this.user.setName(this.inputName.getText());
            this.user.setNeighborhoods(Collections.singletonList(this.selectNeighborhood.getValue()));
            this.user.setRole(this.selectRole.getValue());
            this.user.setEmail(this.inputEmail.getText());
            if (!this.inputPassword.getText().isEmpty())
                this.user.setPassword(this.inputPassword.getText());
            this.user.save();

            Utils.showAlert("Sucesso", "Usuário salvo com sucesso", "Usuário salvo com sucesso", Alert.AlertType.INFORMATION);

            if (redirect) {
                PandemicApplication.openPage("resident/list");
            }
        }
    }

    public void setResident(User user) {
        this.user = user;

        Neighborhood neighborhood = user.getNeighborhoods().get(0);
        this.inputName.setText(user.getName());
        this.selectNeighborhood.setValue(neighborhood);
        this.selectRole.setValue(user.getRole());
        this.inputEmail.setText(user.getEmail());

    }
}
