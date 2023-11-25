package org.pandemia.info.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.controllers.NeighborhoodAddController;
import org.pandemia.info.database.models.Neighborhood;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NeighborhoodTableView extends TableView<Neighborhood> {

    private final TableView<Neighborhood> tableView;
    private final List<Neighborhood> covidCaseList;

    public NeighborhoodTableView(TableView<Neighborhood> tableView) {

        this.covidCaseList = new ArrayList<>();
        this.tableView = tableView;

        TableColumn<Neighborhood, String> nameColumn = new TableColumn<>("Bairro");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));

        TableColumn<Neighborhood, String> cityColumn = new TableColumn<>("Cidade");
        cityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCity()));

        TableColumn<Neighborhood, String> countryColumn = new TableColumn<>("País");
        countryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCountry()));

        TableColumn<Neighborhood, String> latColumn = new TableColumn<>("Latitude");
        latColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLatitude()));

        TableColumn<Neighborhood, String> lngColumn = new TableColumn<>("Longitude");
        lngColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLongitude()));


        TableColumn<Neighborhood, String> actions = new TableColumn<>("Ações");
        actions.setCellFactory(param -> new ActionsCell<>(
                neighborhood -> {

                    URL page = PandemicApplication.class.getResource("/views/neighborhood/add.fxml");
                    if (page == null) return;

                    try {
                        FXMLLoader loader = new FXMLLoader(page);
                        Node node = loader.load();
                        NeighborhoodAddController controller = loader.getController();
                        controller.setNeighborhood(neighborhood);

                        Dialog<Neighborhood> dialog = new Dialog<>();
                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getStyleClass().add("dialog");
                        dialogPane.getChildren().clear();
                        dialogPane.setContent(node);
                        dialogPane.getScene().getWindow().setOnCloseRequest(event -> dialog.close());

                        Button button = (Button) node.lookup("#btnRegister");
                        button.setOnAction(event -> {
                            controller.btnRegisterAction(false);
                            dialog.setResult(neighborhood);
                            dialog.close();
                            updateTable();
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                neighborhood -> {
                    Utils.showConfirm("Deseja realmente excluir este bairro?", () -> {
                        Neighborhood covid = new Neighborhood().findById(Neighborhood.class, neighborhood.getId());
                        if (covid.delete()) {
                            covidCaseList.remove(neighborhood);
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
        tableView.getColumns().add(cityColumn);
        tableView.getColumns().add(countryColumn);
        tableView.getColumns().add(latColumn);
        tableView.getColumns().add(lngColumn);
        tableView.getColumns().add(actions);


    }

    public void setItems(List<Neighborhood> covidCaseList) {
        this.covidCaseList.clear();
        this.covidCaseList.addAll(covidCaseList);
        updateTable();
    }

    public void add(Neighborhood neighborhood) {
        covidCaseList.add(neighborhood);
        updateTable();
    }

    private void updateTable() {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(covidCaseList));
    }

}
