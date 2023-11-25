package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.pandemia.info.Pagination;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.CovidCase;
import org.pandemia.info.database.models.Neighborhood;
import org.pandemia.info.tables.NeighborhoodTableView;
import java.net.URL;
import java.util.ResourceBundle;

public class NeighborhoodListController implements Initializable {

    public Button btnAdd;
    public TextField search;
    public TableView<Neighborhood> table;
    public Pane pagination;
    public Button btnSearch;

    private NeighborhoodTableView neighborhoodTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnAdd.setOnAction(event -> {
            PandemicApplication.openPage("neighborhood/add");
        });


        neighborhoodTableView = new NeighborhoodTableView(table);

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
        int count = Neighborhood.getCount(searchTerm);
        int pages = (int) Math.ceil(count / (double) 14);
        Pagination.paginate(pagination, pages, page -> {
            neighborhoodTableView.setItems(Neighborhood.getAll(page - 1, 14, searchTerm));
        });
    }
}
