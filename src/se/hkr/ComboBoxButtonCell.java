package se.hkr;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ListCell;

public class ComboBoxButtonCell<T> extends ListCell<T> {
    private SimpleStringProperty promptText = new SimpleStringProperty();

    public ComboBoxButtonCell(String promptText) {
        this.promptText.addListener((obs, oldText, newText) -> {
            if (isEmpty() || getItem() == null) {
                setText(newText);
            }
        });
        this.promptText.set(promptText);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(promptText.get());
        } else {
            setText(item.toString());
        }
    }
}
