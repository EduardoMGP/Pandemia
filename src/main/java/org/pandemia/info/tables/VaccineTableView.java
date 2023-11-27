package org.pandemia.info.tables;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.controllers.VaccineAddController;
import org.pandemia.info.database.models.Vaccine;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VaccineTableView extends TableView<Vaccine> {

    private final TableView<Vaccine> tableView;
    private final List<Vaccine> vaccineList;

    public VaccineTableView(TableView<Vaccine> tableView) {

        this.vaccineList = new ArrayList<>();
        this.tableView = tableView;

        TableColumn<Vaccine, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));

        TableColumn<Vaccine, String> manufacturerColumn = new TableColumn<>("Fabricante");
        manufacturerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getManufacturer()));

        TableColumn<Vaccine, String> batchColumn = new TableColumn<>("Lote");
        batchColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBatch()));

        TableColumn<Vaccine, Integer> stockColumn = new TableColumn<>("Estoque");
        stockColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getStock()));

        TableColumn<Vaccine, String> fabricationDate = new TableColumn<>("Data de fabricação");
        fabricationDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFabrication_date()));

        TableColumn<Vaccine, String> expirationDate = new TableColumn<>("Data de vencimento");
        expirationDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getExpiration_date()));


        TableColumn<Vaccine, String> actions = new TableColumn<>("Ações");
        actions.setCellFactory(param -> new ActionsCell<>(
                vaccine -> {

                    URL page = PandemicApplication.class.getResource("/views/vaccine/add.fxml");
                    if (page == null) return;

                    try {
                        FXMLLoader  loader = new FXMLLoader(page);
                        Node node = loader.load();
                        VaccineAddController controller = loader.getController();
                        controller.setVaccine(vaccine);

                        Dialog<Vaccine> dialog = new Dialog<>();
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
                    Utils.showConfirm("Deseja realmente excluir esta vacina?", () -> {
                        Vaccine covid = new Vaccine().findById(Vaccine.class, vaccine.getId());
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


        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(manufacturerColumn);
        tableView.getColumns().add(batchColumn);
        tableView.getColumns().add(stockColumn);
        tableView.getColumns().add(fabricationDate);
        tableView.getColumns().add(expirationDate);
        tableView.getColumns().add(actions);


    }

    public void setItems(List<Vaccine> vaccineList) {
        this.vaccineList.clear();
        this.vaccineList.addAll(vaccineList);
        updateTable();
    }

    public void add(Vaccine vaccine) {
        vaccineList.add(vaccine);
        updateTable();
    }

    private void updateTable() {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(vaccineList));
    }

}
