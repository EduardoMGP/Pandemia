package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.pandemia.info.ISearch;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.SearchComponent;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.User;
import org.pandemia.info.database.models.UsersVaccine;
import org.pandemia.info.database.models.Vaccine;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class VaccineAppliedAddController implements Initializable {


    public Button btnRegister;
    public TextField inputName;
    public DatePicker inputDate;
    public ChoiceBox<Vaccine> vaccine;
    public ScrollPane searchArea;
    public ListView<User> searchList;
    public TextField inputDose;
    public AnchorPane body;
    public Button btnSearch;
    private UsersVaccine usersVaccine;
    private SearchComponent<User> searchComponent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Vaccine> vaccines = Vaccine.getAll();
        this.usersVaccine = new UsersVaccine();
        this.btnRegister.setOnAction(e -> btnRegisterAction(true));
        this.vaccine.getItems().setAll(vaccines);
        this.vaccine.setValue(vaccines.get(0));
        this.vaccine.setConverter(new StringConverter<>() {
            @Override
            public String toString(Vaccine object) {
                return object.getName() + " (" + object.getManufacturer() + ")";
            }

            @Override
            public Vaccine fromString(String string) {
                return null;
            }
        });
        this.inputDate.setValue(LocalDate.now());

        searchComponent = new SearchComponent<>(btnSearch, body, inputName, searchArea,  searchList,new ISearch<User>() {
            @Override
            public List<User> find(String search) {
                return User.searchByName(search);
            }

            @Override
            public String text(User user) {
                return user.getName() + " (" + user.getEmail() + ")";
            }
        });
    }

    private void closeOnClick(Node node) {
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getSource() != inputName && e.getSource() != searchList && e.getSource() != searchArea)
                searchArea.setVisible(false);
        });
    }

    public void btnRegisterAction(boolean redirect) {
        if (
                vaccine.getValue() == null
                        || inputDose.getText().isEmpty()
                        || inputDate.getValue() == null
                        || searchComponent.getSelected() == null
        ) {
            Utils.showAlert("Erro", "Erro ao cadastrar a vacinação", "Preencha todos os campos", Alert.AlertType.ERROR);
            return;
        }

        if (!inputDose.getText().matches("[0-9]+")) {
            Utils.showAlert("Erro", "Erro ao cadastrar a vacinação", "A dose deve ser um número", Alert.AlertType.ERROR);
            return;
        }

        usersVaccine.setVaccine_id(vaccine.getValue().getId());
        usersVaccine.setUser_id(searchComponent.getSelected().getId());
        usersVaccine.setDose(Integer.parseInt(inputDose.getText()));
        usersVaccine.setDate(inputDate.getValue().toString());
        usersVaccine.setVaccine(vaccine.getValue());
        usersVaccine.setUser(searchComponent.getSelected());
        usersVaccine.save();

        Utils.showAlert("Sucesso", "Vacina cadastrada com sucesso", "Vacina cadastrada com sucesso", Alert.AlertType.INFORMATION);

        if (redirect) {
            PandemicApplication.openPage("vaccine/applied/list");
        }
    }

    public void setUsersVaccine(UsersVaccine usersVaccine) {
        this.usersVaccine = usersVaccine;
        this.vaccine.setValue(usersVaccine.getVaccine());
        this.inputDose.setText(String.valueOf(usersVaccine.getDose()));
        this.inputDate.setValue(LocalDate.parse(usersVaccine.getDate()));
        this.searchComponent.setSelected(usersVaccine.getUser());
    }
}
