package org.pandemia.info;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.pandemia.info.database.ConnectionJPA;
import org.pandemia.info.database.models.User;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class PandemicApplication extends Application {

    static Scene scene;
    public static final Logger logger = Logger.getLogger(PandemicApplication.class.getName());
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        PandemicApplication.user = user;
    }

    public static void navigate(String pageName) {
        try {
            if (scene != null) {

                if (pageName.equals("login") || pageName.equals("register")) {
                    if (user != null) return;
                } else if (user == null)
                    pageName = "auth/login";

                URL page = PandemicApplication.class.getResource("/views/" + pageName + ".fxml");
                if (page == null)
                    throw new IOException("Page not found");
                scene.setRoot(FXMLLoader.load(page));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openPage(String page) {
        if (user != null) {
            ScrollPane scrollPane = (ScrollPane) scene.lookup("#content");
            URL pageUrl = PandemicApplication.class.getResource("/views/" + page + ".fxml");
            if (pageUrl != null && scrollPane != null) {
                try {
                    scrollPane.setContent(FXMLLoader.load(pageUrl));

                    final double SPEED = 0.01;
                    scrollPane.getContent().setOnScroll(scrollEvent -> {
                        double deltaY = scrollEvent.getDeltaY() * SPEED;
                        scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        setUser(new User().findById(User.class, 1));
        FXMLLoader fxmlLoader = new FXMLLoader(PandemicApplication.class.getResource("/views/index.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(PandemicApplication.class.getResource("/views/auth/login.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        PandemicApplication.openPage("case");

    }

    public static void main(String[] args) {
        ConnectionJPA.init();
        Application.launch();
    }
}