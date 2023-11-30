package org.pandemia.info.controllers;

import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.pandemia.info.database.models.VaccineStatistics;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VaccineViewController implements Initializable {

    public PieChart chart;
    public Pane legend;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graph();
    }

    public void graph() {
        List<VaccineStatistics> statistics = VaccineStatistics.getVaccinesStatistics();
        for (VaccineStatistics statistic : statistics) {
            PieChart.Data slice = new PieChart.Data(statistic.getName(), statistic.getCount_vaccine());
            chart.getData().add(slice);
        }

        int position = 0;
        for (PieChart.Data data : chart.getData()) {
            Label label = new Label();
            label.setMinWidth(100);
            label.setPrefHeight(20);
            label.setLayoutY(20 * position);
            label.setText(data.getName() + " : " + (int) data.getPieValue() + " pessoas");
            legend.getChildren().add(label);
            position++;
        }
        chart.autosize();
    }

}
