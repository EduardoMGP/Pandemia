package org.pandemia.info.tables;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Pane;

public class ActionsCell<T> extends TableCell<T, String> {

    private final Button btnEdit;
    private final Button btnDelete;
    private final IActionCell<T> onEdit;
    private final IActionCell<T> onDelete;

    public ActionsCell(IActionCell<T> onEdit, IActionCell<T> onDelete) {
        super();

        this.onEdit = onEdit;
        this.onDelete = onDelete;

        btnEdit = new Button("Editar");
        btnEdit.setPrefWidth(60);
        btnEdit.getStyleClass().add("btn-edit");

        btnDelete = new Button("Excluir");
        btnDelete.getStyleClass().add("btn-danger");
        btnDelete.setPrefWidth(60);
        btnDelete.setTranslateX(70);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {

            T t = getTableView().getItems().get(getIndex());
            btnEdit.setOnAction(event -> onEdit.on(t));
            btnDelete.setOnAction(event -> onDelete.on(t));
            setGraphic(new Pane(btnEdit, btnDelete));

        } else {
            setText(null);
            setGraphic(null);
        }
    }

}
