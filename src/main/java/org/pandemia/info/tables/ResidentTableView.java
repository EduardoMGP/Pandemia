package org.pandemia.info.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.controllers.ResidentAddController;
import org.pandemia.info.database.models.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResidentTableView extends TableView<User> {

    private final TableView<User> tableView;
    private final List<User> usersList;

    public ResidentTableView(TableView<User> tableView) {

        this.usersList = new ArrayList<>();
        this.tableView = tableView;

        TableColumn<User, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEmail()));

        TableColumn<User, String> roleColumn = new TableColumn<>("Nível de acesso");
        roleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRole().getName()));

        TableColumn<User, String> neighborhoodColumn = new TableColumn<>("Bairro");
        neighborhoodColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getNeighborhoods().get(0) != null ?
                        param.getValue().getNeighborhoods().get(0).getName() : "Não informado"
        ));


        TableColumn<User, String> actions = new TableColumn<>("Ações");
        actions.setCellFactory(param -> new ActionsCell<>(
                user -> {


                    URL page = PandemicApplication.class.getResource("/views/resident/add.fxml");
                    if (page == null) return;

                    try {
                        FXMLLoader  loader = new FXMLLoader(page);
                        Node node = loader.load();
                        ResidentAddController controller = loader.getController();
                        controller.setResident(user);

                        Dialog<User> dialog = new Dialog<>();
                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getStyleClass().add("dialog");
                        dialogPane.getChildren().clear();
                        dialogPane.setContent(node);
                        dialogPane.getScene().getWindow().setOnCloseRequest(event -> dialog.close());

                        Button button = (Button) node.lookup("#btnRegister");
                        button.setOnAction(event -> {
                            controller.btnRegisterAction(false);
                            dialog.setResult(user);
                            dialog.close();
                            updateTable();
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                user -> {
                    Utils.showConfirm("Deseja realmente excluir este caso?", () -> {
                        User covid = new User().findById(User.class, user.getId());
                        if (covid.delete()) {
                            usersList.remove(user);
                            updateTable();
                        }
                    });
                }
        ));

        actions.setPrefWidth(140);
        actions.setMinWidth(140);
        actions.setMaxWidth(140);
        actions.setResizable(false);


        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(neighborhoodColumn);
        tableView.getColumns().add(emailColumn);
        tableView.getColumns().add(roleColumn);
        tableView.getColumns().add(actions);


    }

    public void setItems(List<User> usersList) {
        this.usersList.clear();
        this.usersList.addAll(usersList);
        updateTable();
    }

    public void add(User user) {
        usersList.add(user);
        updateTable();
    }

    private void updateTable() {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(usersList));
    }

}
