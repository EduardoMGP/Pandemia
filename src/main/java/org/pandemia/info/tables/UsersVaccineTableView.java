package org.pandemia.info.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.controllers.VaccineAppliedAddController;
import org.pandemia.info.database.models.UsersVaccine;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UsersVaccineTableView extends TableView<UsersVaccine> {

    private final TableView<UsersVaccine> tableView;
    private final List<UsersVaccine> vaccineList;

    public UsersVaccineTableView(TableView<UsersVaccine> tableView) {

        this.vaccineList = new ArrayList<>();
        this.tableView = tableView;

        TableColumn<UsersVaccine, String> userColumn = new TableColumn<>("Usuário");
        userColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getName()));

        TableColumn<UsersVaccine, String> emailColumn = new TableColumn<>("email");
        emailColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getEmail()));

        TableColumn<UsersVaccine, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVaccine().getName()));

        TableColumn<UsersVaccine, String> manufacturerColumn = new TableColumn<>("Fabricante");
        manufacturerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVaccine().getManufacturer()));

        TableColumn<UsersVaccine, String> batchColumn = new TableColumn<>("Lote");
        batchColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVaccine().getBatch()));

        TableColumn<UsersVaccine, String> applicationDate = new TableColumn<>("Data de aplicação");
        applicationDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate()));

        TableColumn<UsersVaccine, String> fabricationDate = new TableColumn<>("Data de fabricação");
        fabricationDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVaccine().getFabrication_date()));

        TableColumn<UsersVaccine, String> expirationDate = new TableColumn<>("Data de vencimento");
        expirationDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVaccine().getExpiration_date()));


        TableColumn<UsersVaccine, String> actions = new TableColumn<>("Ações");
        actions.setCellFactory(param -> new ActionsCell<>(
                vaccine -> {

                    URL page = PandemicApplication.class.getResource("/views/vaccine/applied/add.fxml");
                    if (page == null) return;

                    try {
                        FXMLLoader  loader = new FXMLLoader(page);
                        Node node = loader.load();
                        VaccineAppliedAddController controller = loader.getController();
                        controller.setUsersVaccine(vaccine);

                        Dialog<UsersVaccine> dialog = new Dialog<>();
                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getStyleClass().add("dialog");
                        dialogPane.getChildren().clear();
                        dialogPane.setContent(node);
                        dialogPane.getScene().getWindow().setOnCloseRequest(event -> dialog.close());

                        Button button = (Button) node.lookup("#btnRegister");
                        button.setOnAction(event -> {
                            controller.btnRegisterAction(false);
                            dialog.setResult(vaccine);
                            dialog.close();
                            updateTable();
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                vaccine -> {
                    Utils.showConfirm("Deseja realmente excluir esta vacinação?", () -> {
                        UsersVaccine covid = new UsersVaccine().findById(UsersVaccine.class, vaccine.getId());
                        if (covid.delete()) {
                            vaccineList.remove(vaccine);
                            updateTable();
                        }
                    });
                }
        ));

        actions.setPrefWidth(140);
        actions.setMinWidth(140);
        actions.setMaxWidth(140);
        actions.setResizable(false);

        tableView.getColumns().add(userColumn);
        tableView.getColumns().add(emailColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(manufacturerColumn);
        tableView.getColumns().add(batchColumn);
        tableView.getColumns().add(applicationDate);
        tableView.getColumns().add(fabricationDate);
        tableView.getColumns().add(expirationDate);
        tableView.getColumns().add(actions);

    }

    public void setItems(List<UsersVaccine> vaccineList) {
        this.vaccineList.clear();
        this.vaccineList.addAll(vaccineList);
        updateTable();
    }

    public void add(UsersVaccine vaccine) {
        vaccineList.add(vaccine);
        updateTable();
    }

    private void updateTable() {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(vaccineList));
    }

}
