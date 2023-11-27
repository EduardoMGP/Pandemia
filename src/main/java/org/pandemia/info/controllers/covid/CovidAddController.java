package org.pandemia.info.controllers.covid;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.pandemia.info.ISearch;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.SearchComponent;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.CovidCase;
import org.pandemia.info.database.models.Neighborhood;
import org.pandemia.info.database.models.User;
import org.pandemia.info.database.models.enums.CaseStatus;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CovidAddController implements Initializable {

    public ChoiceBox<CaseStatus> status;
    public CheckBox inputQuarantine;
    public DatePicker inputDate;
    public TextField inputName;
    public TextArea inputSymptoms;
    public Button btnRegister;
    public AnchorPane body;
    public Button btnSearch;

    public ScrollPane searchArea;
    public ListView<User> searchList;
    public ChoiceBox<Neighborhood> neighborhood;

    private CovidCase covidCase;
    private SearchComponent<User> searchComponent;

    public CovidAddController() {
        covidCase = new CovidCase();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        neighborhood.getItems().setAll(new Neighborhood().findAll(Neighborhood.class));
        neighborhood.setValue(neighborhood.getItems().get(0));

        searchComponent = new SearchComponent<>(btnSearch, body, inputName, searchArea, searchList, new ISearch<User>() {
            @Override
            public List<User> find(String search) {
                return User.searchByName(search);
            }

            @Override
            public String text(User user) {
                return user.getName() + " (" + user.getEmail() + ")";
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


    public void btnRegisterAction(boolean redirect) {

        if (searchComponent.getSelected() == null || inputDate.getValue() == null || inputSymptoms.getText().isEmpty() || status.getValue() == null || neighborhood.getValue() == null) {
            Utils.showAlert("Erro", "Erro ao cadastrar caso", "Preencha todos os campos", Alert.AlertType.ERROR);
        } else {

            String date = inputDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            covidCase.setCase_date(date);
            covidCase.setSymptoms(inputSymptoms.getText());
            covidCase.setStatus(status.getValue());
            covidCase.setIn_quarantine(inputQuarantine.isSelected());
            covidCase.setNeighborhood(neighborhood.getValue());
            covidCase.setUser(searchComponent.getSelected());
            searchComponent.getSelected().addCovidCase(covidCase);

            Utils.showAlert("Sucesso", "Caso salvo com sucesso", "O caso foi salvo com sucesso", Alert.AlertType.INFORMATION);

            if (redirect)
                PandemicApplication.openPage("covid/list");
        }

    }

    public void setCovidCase(CovidCase covidCase) {
        this.covidCase = covidCase;
        LocalDate localDate = LocalDate.parse(covidCase.getCase_date());

        inputDate.setValue(localDate);
        inputSymptoms.setText(covidCase.getSymptoms());
        status.setValue(covidCase.getStatus());
        inputQuarantine.setSelected(covidCase.isIn_quarantine());
        neighborhood.setValue(covidCase.getNeighborhood());
        searchComponent.setSelected(covidCase.getUser());
    }
}
