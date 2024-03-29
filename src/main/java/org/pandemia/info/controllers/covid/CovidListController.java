package org.pandemia.info.controllers.covid;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.pandemia.info.tables.CovidTableView;
import org.pandemia.info.Pagination;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.database.models.CovidCase;

import java.net.URL;
import java.util.ResourceBundle;

public class CovidListController implements Initializable {

    public TextField search;
    public Button btnCovid;
    public Button btnSearch;
    public TableView<CovidCase> table;
    public Pane pagination;

    private CovidTableView covidTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnCovid.setOnAction(event -> {
            PandemicApplication.openPage("covid/add");
        });


        covidTableView = new CovidTableView(table);

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
        int count = CovidCase.getCasesCount(searchTerm);
        int pages = (int) Math.ceil(count / (double) 14);
        Pagination.paginate(pagination, pages, page -> {
            covidTableView.setItems(CovidCase.getAllCases(page - 1, 14, searchTerm));
        });
    }
}
