package org.pandemia.info.controllers.covid;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.pandemia.info.PandemicApplication;
import org.pandemia.info.Utils;
import org.pandemia.info.database.models.CovidCase;
import org.pandemia.info.database.models.CovidCount;
import org.pandemia.info.database.models.Neighborhood;

import java.net.URL;
import java.util.*;

public class CovidViewController implements Initializable {

    public WebView webView;
    public AreaChart<String, Integer> chart;
    public Label confirmedTotal;
    public Label confirmed;
    public Label deathsTotal;
    public Label deaths;
    public Label lethality;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        graph();
        heatMap();
        cards();
    }

    public void graph() {
        List<CovidCount> cases = CovidCount.getCountBetweenDates(Utils.nowDecrementedFormat(30), Utils.formatNowDate());
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (CovidCount covidCount : cases)
            series.getData().add(new XYChart.Data<>(Utils.formatDate(covidCount.getDate()), covidCount.getConfirmed()));
        chart.getData().add(series);

        chart.autosize();
    }

    public void heatMap() {
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(Objects.requireNonNull(PandemicApplication.class.getResource("/map/index.html")).toExternalForm());

        List<CovidCase> cases = CovidCase.getCasesBetweenDate(Utils.nowDecrementedFormat(30), Utils.formatNowDate());
        StringBuilder objects = new StringBuilder("[");
        for (CovidCase covidCase : cases) {
            Neighborhood neighborhood = covidCase.getNeighborhood();
            objects.append("{lat: ").append(neighborhood.getLatitude()).append(", lng: ").append(neighborhood.getLongitude()).append("},");
        }
        objects.append("]");

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                webView.getEngine().executeScript("addMarkers(" + objects + ")");
            }
        });
    }

    public void cards() {

        CovidCount count = CovidCount.getCasesCount(Utils.nowDecrementedFormat(30), Utils.formatNowDate());

        confirmedTotal.setText(String.valueOf(count.getConfirmed_total()));
        confirmed.setText("+" + count.getConfirmed());
        deathsTotal.setText(String.valueOf(count.getDeceased_total()));
        deaths.setText("+" + count.getDeceased());
        lethality.setText(String.format("%.1f", count.getLethality()) + "%");

    }
}
