package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.Vaccine;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VaccineController implements Initializable {

    public TextField inputName;
    public TextField inputManufacturer;
    public TextField inputStock;
    public TextField inputBatch;
    public DatePicker inputFabrication;
    public DatePicker inputExpiration;
    public Button btnRegister;

    private Vaccine vaccine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.vaccine = new Vaccine();
    }

    public void btnRegisterAction(boolean redirect) {
        if(
                inputName.getText().isEmpty()
                || inputManufacturer.getText().isEmpty()
                || inputStock.getText().isEmpty()
                || inputBatch.getText().isEmpty()
                || inputFabrication.getValue() == null
                || inputExpiration.getValue() == null
        ) {
            Utils.showAlert("Erro", "Erro ao cadastrar a vacina", "Preencha todos os campos", Alert.AlertType.ERROR);
            return;
        }
        this.vaccine.setName(this.inputName.getText());
        this.vaccine.setManufacturer(this.inputManufacturer.getText());
        this.vaccine.setStock(Integer.parseInt(this.inputStock.getText()));
        this.vaccine.setBatch(this.inputBatch.getText());
        this.vaccine.setFabrication_date(this.inputFabrication.getValue().toString());
        this.vaccine.setExpiration_date(this.inputExpiration.getValue().toString());
        this.vaccine.save();
        if (redirect) {
            PandemicApplication.openPage("vaccine/list");
        }
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
        LocalDate fabrication = LocalDate.parse(vaccine.getFabrication_date());
        LocalDate expiration = LocalDate.parse(vaccine.getExpiration_date());
        this.inputName.setText(vaccine.getName());
        this.inputManufacturer.setText(vaccine.getManufacturer());
        this.inputStock.setText(String.valueOf(vaccine.getStock()));
        this.inputBatch.setText(vaccine.getBatch());
        this.inputFabrication.setValue(fabrication);
        this.inputExpiration.setValue(expiration);
    }
}
