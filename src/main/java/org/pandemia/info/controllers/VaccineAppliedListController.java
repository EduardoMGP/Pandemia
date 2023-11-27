package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.pandemia.info.Pagination;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.UsersVaccine;
import org.pandemia.info.database.models.Vaccine;
import org.pandemia.info.tables.UsersVaccineTableView;
import java.net.URL;
import java.util.ResourceBundle;

public class VaccineAppliedListController implements Initializable {

    public TextField search;
    public Button btnAdd;
    public Button btnSearch;
    public TableView<UsersVaccine> table;
    public Pane pagination;

    public UsersVaccineTableView usersVaccineTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnAdd.setOnAction(event -> {
            PandemicApplication.openPage("vaccine/applied/add");
        });


        usersVaccineTableView = new UsersVaccineTableView(table);

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
        int count = Vaccine.getUsersVaccinesCount(searchTerm);
        int pages = (int) Math.ceil(count / (double) 14);
        Pagination.paginate(pagination, pages, page -> {
            usersVaccineTableView.setItems(Vaccine.getAllUsersVaccines(page - 1, 14, searchTerm));
        });
    }
}
