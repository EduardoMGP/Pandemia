package org.pandemia.info.controllers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.User;

import java.util.Timer;
import java.util.TimerTask;

public class AuthController {


    public PasswordField password;
    public TextField email;
    public TextField name;
    public Label alert_message;
    public Timer timerAlert;


    public void registerPage() {
        // TODO implement here
        PandemicApplication.navigate("auth/register");
    }

    public void loginPage() {
        // TODO implement here
        PandemicApplication.navigate("auth/login");
    }

    public void register() {
        try {
            User.register(email.getText(), password.getText(), name.getText());
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }

    public void login() {
        if (User.validate(email.getText(), password.getText())) {
            PandemicApplication.navigate("home");
        } else alert("Usuário ou senha incorretos!");
    }

    private void alert(String message) {
        alert_message.setText(message);
        if (timerAlert != null) {
            timerAlert.cancel();
            timerAlert = null;
        }

        timerAlert = new Timer();
        timerAlert.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    alert_message.setText("");
                    timerAlert = null;
                });
            }
        }, 5000);
    }
}