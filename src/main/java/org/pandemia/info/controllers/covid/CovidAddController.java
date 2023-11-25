package org.pandemia.info.controllers.covid;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.CovidCase;
import org.pandemia.info.database.models.Neighborhood;
import org.pandemia.info.database.models.User;
import org.pandemia.info.database.models.enums.CaseStatus;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CovidAddController implements Initializable {

    public ChoiceBox<CaseStatus> status;
    public CheckBox inputQuarantine;
    public DatePicker inputDate;
    public TextField inputName;
    public TextArea inputSymptoms;
    public Button btnRegister;
    public AnchorPane body;

    public ScrollPane searchArea;
    public ListView<User> searchList;
    public ChoiceBox<Neighborhood> neighborhood;

    private User selectedUser;
    private CovidCase covidCase;

    public CovidAddController() {
        covidCase = new CovidCase();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        neighborhood.getItems().setAll(new Neighborhood().findAll(Neighborhood.class));
        neighborhood.setValue(neighborhood.getItems().get(0));

        body.getChildren().forEach(this::closeOnClick);
        closeOnClick(body);

        inputName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                searchArea.setVisible(true);
        });

        inputName.setOnKeyPressed(e -> searchList.getItems().setAll(User.searchByName(inputName.getText())));

        searchList.setOnMouseClicked(e -> {
            User user = searchList.getSelectionModel().getSelectedItem();
            if (user == null) return;
            inputName.setText(user.getName());
            searchArea.setVisible(false);
            selectedUser = user;
        });

        searchList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.getName() + " (" + item.getEmail() + ")");
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        status.getItems().addAll(CaseStatus.values());
        status.setValue(CaseStatus.suspect);
        status.setConverter(new StringConverter<>() {
            @Override
            public String toString(CaseStatus object) {
                return object.getStatus();
            }

            @Override
            public CaseStatus fromString(String string) {
                return CaseStatus.valueOf(string);
            }
        });

        neighborhood.setConverter(new StringConverter<>() {
            @Override
            public String toString(Neighborhood object) {
                return object.getName();
            }

            @Override
            public Neighborhood fromString(String string) {
                return null;
            }
        });

        btnRegister.setOnAction(e -> btnRegisterAction(true));
    }

    private void closeOnClick(Node node) {
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getSource() != inputName && e.getSource() != searchList && e.getSource() != searchArea)
                searchArea.setVisible(false);
        });
    }


    public void btnRegisterAction(boolean redirect) {

        if (selectedUser == null || inputDate.getValue() == null || inputSymptoms.getText().isEmpty() || status.getValue() == null || neighborhood.getValue() == null) {
            Utils.showAlert("Erro", "Erro ao cadastrar caso", "Preencha todos os campos", Alert.AlertType.ERROR);
        } else {

            String date = inputDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            covidCase.setCase_date(date);
            covidCase.setSymptoms(inputSymptoms.getText());
            covidCase.setStatus(status.getValue());
            covidCase.setIn_quarantine(inputQuarantine.isSelected());
            covidCase.setNeighborhood(neighborhood.getValue());
            covidCase.setUser(selectedUser);
            selectedUser.addCovidCase(covidCase);

            Utils.showAlert("Sucesso", "Caso salvo com sucesso", "O caso foi salvo com sucesso", Alert.AlertType.INFORMATION);

            if (redirect)
                PandemicApplication.openPage("covid/list");
        }

    }

    public void setCovidCase(CovidCase covidCase) {
        this.covidCase = covidCase;
        LocalDate localDate = LocalDate.parse(covidCase.getCase_date());

        inputName.setText(covidCase.getUser().getName());
        inputDate.setValue(localDate);
        inputSymptoms.setText(covidCase.getSymptoms());
        status.setValue(covidCase.getStatus());
        inputQuarantine.setSelected(covidCase.isIn_quarantine());
        neighborhood.setValue(covidCase.getNeighborhood());
        selectedUser = covidCase.getUser();
    }
}
