package org.pandemia.info.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.controllers.covid.CovidAddController;
import org.pandemia.info.database.models.CovidCase;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CovidTableView extends TableView<CovidCase> {

    private final TableView<CovidCase> tableView;
    private final List<CovidCase> covidCaseList;

    public CovidTableView(TableView<CovidCase> tableView) {

        this.covidCaseList = new ArrayList<>();
        this.tableView = tableView;

        TableColumn<CovidCase, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getName()));

        TableColumn<CovidCase, String> neighborhoodColumn = new TableColumn<>("Bairro");
        neighborhoodColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNeighborhood().getName()));

        TableColumn<CovidCase, String> symptomsColumn = new TableColumn<>("Sintomas");
        symptomsColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSymptoms()));

        TableColumn<CovidCase, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getStatus()));

        TableColumn<CovidCase, String> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCase_date()));

        TableColumn<CovidCase, String> inQuarantineColumn = new TableColumn<>("Em quarentena");
        inQuarantineColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().isIn_quarantine() ? "Sim" : "Não"));


        TableColumn<CovidCase, String> actions = new TableColumn<>("Ações");
        actions.setCellFactory(param -> new ActionsCell<>(
                covidCase -> {


                    URL page = PandemicApplication.class.getResource("/views/covid/edit.fxml");
                    if (page == null) return;

                    try {
                        FXMLLoader  loader = new FXMLLoader(page);
                        Node node = loader.load();
                        CovidAddController controller = loader.getController();
                        controller.setCovidCase(covidCase);

                        Dialog<CovidCase> dialog = new Dialog<>();
                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getStyleClass().add("dialog");
                        dialogPane.getChildren().clear();
                        dialogPane.setContent(node);
                        dialogPane.getScene().getWindow().setOnCloseRequest(event -> dialog.close());

                        Button button = (Button) node.lookup("#btnRegister");
                        button.setOnAction(event -> {
                            controller.btnRegisterAction(false);
                            dialog.setResult(covidCase);
                            dialog.close();
                            updateTable();
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                covidCase -> {
                    Utils.showConfirm("Deseja realmente excluir este caso?", () -> {
                        CovidCase covid = new CovidCase().findById(CovidCase.class, covidCase.getId());
                        if (covid.delete()) {
                            covidCaseList.remove(covidCase);
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
        tableView.getColumns().add(symptomsColumn);
        tableView.getColumns().add(statusColumn);
        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(inQuarantineColumn);
        tableView.getColumns().add(actions);


    }

    public void setItems(List<CovidCase> covidCaseList) {
        this.covidCaseList.clear();
        this.covidCaseList.addAll(covidCaseList);
        updateTable();
    }

    public void add(CovidCase covidCase) {
        covidCaseList.add(covidCase);
        updateTable();
    }

    private void updateTable() {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(covidCaseList));
    }

}
