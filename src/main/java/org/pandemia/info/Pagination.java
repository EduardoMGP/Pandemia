package org.pandemia.info;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Pagination {

    public static void paginate(Pane node, int pages, IPagination pagination) {
        node.getChildren().clear();
        for (int i = 0; i < pages; i++) {

            Button btn = new Button(String.valueOf(i + 1));
            btn.setOnAction(event -> {
                pagination.execute(Integer.parseInt(btn.getText()));
            });

            btn.setPrefWidth(40);
            btn.setPrefHeight(40);
            btn.setLayoutX(42 * i);
            node.getChildren().add(btn);

        }

        pagination.execute(1);
    }

}

