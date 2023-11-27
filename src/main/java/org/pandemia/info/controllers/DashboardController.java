package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import org.pandemia.info.PandemicApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public Button btnCovid;
    public Button btnListCovid;
    public Button btnVacineApplied;
    public Button btnVaccine;
    public Button btnListVaccines;
    public Button btnListVacineApplied;
    public Button btnNeighborhood;
    public Button btnListNeighborhood;
    public Button btnResidents;
    public Button btnListResidents;
    public Hyperlink report;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnCovid.setOnAction(event -> PandemicApplication.openPage("covid/add"));
        btnListCovid.setOnAction(event -> PandemicApplication.openPage("covid/list"));

        btnVaccine.setOnAction(event -> PandemicApplication.openPage("vaccine/add"));
        btnListVaccines.setOnAction(event -> PandemicApplication.openPage("vaccine/list"));
        btnVacineApplied.setOnAction(event -> PandemicApplication.openPage("vaccine/applied/add"));
        btnListVacineApplied.setOnAction(event -> PandemicApplication.openPage("vaccine/applied/list"));

        btnNeighborhood.setOnAction(event -> PandemicApplication.openPage("neighborhood/add"));
        btnListNeighborhood.setOnAction(event -> PandemicApplication.openPage("neighborhood/list"));

        btnResidents.setOnAction(event -> PandemicApplication.openPage("resident/add"));
        btnListResidents.setOnAction(event -> PandemicApplication.openPage("resident/list"));

    }
}