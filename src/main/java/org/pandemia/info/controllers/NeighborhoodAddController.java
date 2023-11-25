package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.Neighborhood;

import java.net.URL;
import java.util.ResourceBundle;

public class NeighborhoodAddController implements Initializable {

    public Button btnRegister;
    public TextField inputName;
    public TextField inputLat;
    public TextField inputLng;
    public ChoiceBox<String> selectCity;
    public ChoiceBox<String> selectState;

    private Neighborhood neighborhood;

    public NeighborhoodAddController() {
        this.neighborhood = new Neighborhood();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectCity.getItems().addAll("Rio Pomba");
        selectState.getItems().addAll("MG");
        selectCity.setValue("Rio Pomba");
        selectState.setValue("MG");
        btnRegister.setOnAction(event -> btnRegisterAction(true));

    }

    public void btnRegisterAction(boolean redirect) {

        String name = inputName.getText();
        String lat = inputLat.getText();
        String lng = inputLng.getText();
        String city = selectCity.getValue();
        String state = selectState.getValue();

        if (name.isEmpty() || lat.isEmpty() || lng.isEmpty() || city.isEmpty() || state.isEmpty()) {
            Utils.showAlert("Erro", "Erro ao cadastrar bairro", "Preencha todos os campos", Alert.AlertType.ERROR);
        } else if (!lat.matches("^-?\\d*\\.?\\d+$") || !lng.matches("^-?\\d*\\.?\\d+$")) {
            Utils.showAlert("Erro", "Erro ao cadastrar bairro", "Latitude e longitude devem ser números", Alert.AlertType.ERROR);
        } else {
            neighborhood.setName(name);
            neighborhood.setLatitude(lat);
            neighborhood.setLongitude(lng);
            neighborhood.setCity(city);
            neighborhood.setState(state);
            neighborhood.setCountry("BR");
            neighborhood.save();
            Utils.showAlert("Sucesso", "Bairro cadastrado com sucesso", "O bairro " + name + " foi cadastrado com sucesso", Alert.AlertType.INFORMATION);
            if (redirect) PandemicApplication.openPage("neighborhood/list");
        }

    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;

        inputName.setText(neighborhood.getName());
        inputLat.setText(neighborhood.getLatitude());
        inputLng.setText(neighborhood.getLongitude());
        selectCity.setValue(neighborhood.getCity());
        selectState.setValue(neighborhood.getState());

    }
}
