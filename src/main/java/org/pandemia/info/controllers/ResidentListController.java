package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.pandemia.info.Pagination;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.User;
import org.pandemia.info.tables.ResidentTableView;
import java.net.URL;
import java.util.ResourceBundle;

public class ResidentListController implements Initializable {

    public Button btnAdd;
    public TextField search;
    public TableView<User> table;
    public Pane pagination;
    public Button btnSearch;

    private ResidentTableView userTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnAdd.setOnAction(event -> {
            PandemicApplication.openPage("resident/add");
        });

        userTableView = new ResidentTableView(table);

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
        int count = User.getCount(searchTerm);
        int pages = (int) Math.ceil(count / (double) 14);
        Pagination.paginate(pagination, pages, page -> {
            userTableView.setItems(User.getAll(page - 1, 14, searchTerm));
        });
    }
}
