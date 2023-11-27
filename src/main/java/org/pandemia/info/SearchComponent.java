package org.pandemia.info;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class SearchComponent<T> {

    private final ListView<T> searchList;
    private final TextField input;
    private final ScrollPane searchArea;
    private T selected;
    private final ISearch<T> search;
    private final Button btnSearch;

    public SearchComponent(Button btnSearch, AnchorPane body, TextField input,
                           ScrollPane searchArea, ListView<T> searchList, ISearch<T> search) {

        this.input = input;
        this.searchArea = searchArea;
        this.search = search;
        this.searchList = searchList;
        this.btnSearch = btnSearch;

        body.getChildren().forEach(this::closeOnClick);
        closeOnClick(body);

        input.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                searchArea.setVisible(true);
        });

        input.setOnKeyReleased(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                searchArea.setVisible(true);
                searchList.getItems().setAll(
                        search.find(input.getText())
                );
            }
        });

        btnSearch.setOnAction(e -> {
            searchArea.setVisible(true);
            searchList.getItems().setAll(
                    search.find(input.getText())
            );
        });

        searchList.setOnMouseClicked(e -> {
            T t = searchList.getSelectionModel().getSelectedItem();
            if (t == null) return;
            input.setText(search.text(t));
            searchArea.setVisible(false);
            selected = t;
        });

        searchList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(search.text((T) item));
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    private void closeOnClick(Node node) {
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getSource() != input && e.getSource() != searchList && e.getSource() != searchArea && e.getSource() != btnSearch)
                searchArea.setVisible(false);
        });
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
        this.input.setText(search.text(selected));
    }
}
