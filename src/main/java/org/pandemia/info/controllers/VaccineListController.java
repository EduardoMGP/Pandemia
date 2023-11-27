package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.pandemia.info.Pagination;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.Vaccine;
import org.pandemia.info.tables.VaccineTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class VaccineListController implements Initializable {

    public TextField search;
    public Button btnAdd;
    public Button btnSearch;
    public TableView<Vaccine> table;
    public Pane pagination;

    public VaccineTableView vaccineTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnAdd.setOnAction(event -> {
            PandemicApplication.openPage("vaccine/add");
        });


        vaccineTableView = new VaccineTableView(table);

        search.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                updateTable();
            }
        });

        btnSearch.setOnAction(event -> updateTable());

        updateTable();
    }

    private void updateTable() {
        String searchTerm = search.getText();
        int count = Vaccine.getVaccinesCount(searchTerm);
        int pages = (int) Math.ceil(count / (double) 14);
        Pagination.paginate(pagination, pages, page -> {
            vaccineTableView.setItems(Vaccine.getAllVaccines(page - 1, 14, searchTerm));
        });
    }
}
