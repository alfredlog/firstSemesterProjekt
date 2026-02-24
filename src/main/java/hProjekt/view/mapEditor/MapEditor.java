package hProjekt.view.mapEditor;

import java.util.function.Supplier;

import hProjekt.model.grid.Types;
import hProjekt.model.mapEditor.MapEditorTools;
import hProjekt.view.overlays.ConfirmationOverlay;
import javafx.animation.PauseTransition;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The {@code MapEditor} class represents the main UI component for the map
 * editing interface.
 * It extends {@link StackPane} to layer the map view, various tool overlays,
 * and control buttons.
 *
 * <p>
 * This component assembles the following elements:
 * <ul>
 * <li>The map visualization itself.</li>
 * <li>A {@link ToolsOverlay} for selecting editing tools and tile types.</li>
 * <li>A {@link MapNameOverlay} for editing the map's name.</li>
 * <li>A {@link ConfirmationOverlay} for handling exit confirmations and save
 * errors.</li>
 * <li>Buttons for saving the map and returning to the main menu.</li>
 * </ul>
 *
 * @see ToolsOverlay
 * @see MapNameOverlay
 * @see ConfirmationOverlay
 */
public class MapEditor extends StackPane {

    /**
     * Constructs a new {@code MapEditor} instance.
     *
     * @param map                  The {@link Region} representing the visual map
     *                             component to be edited.
     * @param selectedToolProperty A property holding the currently selected editing
     *                             tool.
     * @param selectedTypeProperty A property holding the currently selected tile
     *                             type to place.
     * @param mapNameProperty      A property bound to the name of the current map.
     * @param saveAction           A supplier that performs the save operation. It
     *                             returns {@code null} or an empty string on
     *                             success, or an error message string if the save
     *                             fails.
     * @param exitAction           A runnable to be executed when the user confirms
     *                             they want to exit the editor.
     */
    public MapEditor(final Region map, final Property<MapEditorTools> selectedToolProperty,
            final Property<Types> selectedTypeProperty, final StringProperty mapNameProperty,
            final Supplier<String> saveAction,
            final Runnable exitAction) {
        getStylesheets().add("css/main.css");

        getChildren().add(map);
        final StackPane overlayContainer = new StackPane();
        overlayContainer.getStyleClass().add("container");
        overlayContainer.setPickOnBounds(false);

        final ToolsOverlay toolsOverlay = new ToolsOverlay(selectedToolProperty, selectedTypeProperty);
        toolsOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        toolsOverlay.setMaxHeight(Region.USE_PREF_SIZE);
        toolsOverlay.setPickOnBounds(false);
        overlayContainer.getChildren().add(toolsOverlay);

        final MapNameOverlay nameOverlay = new MapNameOverlay(mapNameProperty);
        nameOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        nameOverlay.setMaxHeight(Region.USE_PREF_SIZE);
        nameOverlay.setPickOnBounds(false);
        overlayContainer.getChildren().add(nameOverlay);

        final ConfirmationOverlay confirmationOverlay = new ConfirmationOverlay();
        confirmationOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        confirmationOverlay.setMaxHeight(Region.USE_PREF_SIZE);
        confirmationOverlay.setVisible(false);
        overlayContainer.getChildren().add(confirmationOverlay);

        final Button endButton = new Button("Back to Menu");
        endButton.setOnAction(e -> {
            confirmationOverlay.resetOverlay();
            confirmationOverlay.setMessage(
                    "Are you sure you want to exit?\nAll unsaved changes will be lost.");
            confirmationOverlay.setYesButtonAction("Yes, Exit", exitAction);
            confirmationOverlay.setNoButtonAction("No, Stay", () -> {
                confirmationOverlay.resetOverlay();
                confirmationOverlay.setVisible(false);
            });
            confirmationOverlay.setVisible(true);
        });
        endButton.getStyleClass().add("button-no");

        final Button saveButton = new Button(String.format("%c Save Map", 0xF0193));
        saveButton.setOnAction(e -> {
            final String error = saveAction.get();
            final String originalText = saveButton.getText();
            if (error == null || error.isBlank()) {
                saveButton.setText(String.format("%c Saved!", 0xF18EA));
            } else {
                saveButton.setText(String.format("%c Error!", 0xF0F42));
                confirmationOverlay.resetOverlay();
                confirmationOverlay.setMessage("Error saving map:\n" + error);
                confirmationOverlay.setYesButtonAction("OK", () -> confirmationOverlay.setVisible(false));
                confirmationOverlay.setNoButtonAction(null, null);
                confirmationOverlay.setVisible(true);
            }
            final PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(pe -> saveButton.setText(originalText));
            pause.play();
        });
        saveButton.getStyleClass().add("button-yes");
        overlayContainer.getChildren().addAll(endButton, saveButton);

        StackPane.setAlignment(endButton, Pos.TOP_LEFT);
        StackPane.setAlignment(saveButton, Pos.TOP_RIGHT);
        StackPane.setAlignment(nameOverlay, Pos.TOP_CENTER);
        StackPane.setAlignment(toolsOverlay, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(confirmationOverlay, Pos.CENTER);
        getChildren().add(overlayContainer);
    }
}
