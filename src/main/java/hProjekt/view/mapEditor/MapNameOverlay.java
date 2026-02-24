package hProjekt.view.mapEditor;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * An overlay component that allows the user to edit the name of a map.
 * <p>
 * This component extends {@link HBox} and contains a data-bound
 * {@link TextField}.
 * Changes made in the text field are bi-directionally synchronized with the
 * provided {@link StringProperty}, ensuring that the map's name model and the
 * UI remain consistent.
 * </p>
 * <p>
 * It applies the "box" style class and loads the main CSS sheet for consistent
 * styling.
 * </p>
 */
public class MapNameOverlay extends HBox {

    /**
     * Constructs a new {@code MapNameOverlay} bound to the specified map name
     * property.
     *
     * @param mapNameProperty the {@link StringProperty} representing the map's name
     */
    public MapNameOverlay(final StringProperty mapNameProperty) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        setSpacing(10);

        final TextField nameField = new TextField();
        nameField.textProperty().bindBidirectional(mapNameProperty);
        nameField.setPromptText("Enter map name...");
        getChildren().add(nameField);
    }
}
