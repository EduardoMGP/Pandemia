package org.pandemia.info.controllers;

import javafx.scene.control.Hyperlink;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.User;
import org.pandemia.info.database.models.enums.Role;

public class IndexController {


    public Hyperlink dashboard;

    public void initialize() {
        User user = PandemicApplication.getUser();
        dashboard.setVisible(user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.NURSE));
        dashboard.onActionProperty().setValue(event -> {
            PandemicApplication.openPage("dashboard");
        });
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