package org.pandemia.info.controllers;

import javafx.scene.control.Label;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.User;

public class IndexController {


    public Label name;

    public void initialize() {
        User user = PandemicApplication.getUser();
    }

    public void logout() {
        // TODO implement here
        PandemicApplication.setUser(null);
        PandemicApplication.navigate("auth/login");
    }

    public void openCovidCases() {
        PandemicApplication.openPage("covid/view");
    }

    public void openVaccinationStatistics() {
        System.out.println("openVaccinationStatistics");
    }

}