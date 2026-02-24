package hProjekt.view.mapEditor;

import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;
import hProjekt.model.mapEditor.MapEditorTools;
import hProjekt.view.grid.TilePane;
import hProjekt.view.utils.IconView;
import hProjekt.view.utils.Images;
import javafx.beans.property.Property;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

/**
 * A custom UI component that displays an overlay of tools for the map editor.
 * This component extends {@link HBox} and arranges tool selection buttons
 * horizontally.
 * <p>
 * The overlay manages a {@link ToggleGroup} that handles the selection of
 * different map editing tools, such as moving, erasing, or brushing specific
 * tiles and structures. It updates the provided properties when a selection
 * changes.
 * </p>
 * <p>
 * The tools included are:
 * <ul>
 * <li><b>{@link MapEditorTools#MOVE}:</b> Allows navigation or movement within
 * the editor.</li>
 * <li><b>{@link MapEditorTools#ERASER}:</b> General eraser for removing
 * tiles.</li>
 * <li><b>{@link MapEditorTools#ERASER} with {@link Structure.Type}:</b>
 * specialized eraser for structures.</li>
 * <li><b>{@link MapEditorTools#BRUSH} with {@link Tile.Type}:</b> Buttons for
 * painting specific {@link Tile.Type}s.</li>
 * <li><b>{@link MapEditorTools#BRUSH} with {@link Structure.Type}:</b> Buttons
 * for placing specific {@link Structure.Type}s.</li>
 * </ul>
 *
 * @see HBox
 * @see ToggleGroup
 */
public class ToolsOverlay extends HBox {
    /**
     * Constructs a new ToolsOverlay, initializing the UI components and setting up
     * tool selection logic.
     * <p>
     * This constructor configures the layout and styling of the overlay, creates a
     * {@link ToggleGroup} for handling
     * exclusive tool selection, and populates the overlay with toggle buttons for
     * various map editor tools.
     * <p>
     * A subscription is added to the toggle group's selected property. When a
     * toggle is selected, the corresponding {@link MapEditorTools} and specific
     * type (e.g., specific tile or structure type) are extracted from the toggle's
     * user data and updated in the provided properties.
     *
     * @param selectedToolProperty The property to update with the currently
     *                             selected {@link MapEditorTools}.
     * @param selectedTypeProperty The property to update with the currently
     *                             selected specific type ({@link Types}),
     *                             such as a {@link Tile.Type} or
     *                             {@link Structure.Type}.
     */
    public ToolsOverlay(final Property<MapEditorTools> selectedToolProperty,
            final Property<Types> selectedTypeProperty) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        setSpacing(10);

        final ToggleGroup toolToggleGroup = new ToggleGroup();

        toolToggleGroup.selectedToggleProperty().subscribe(newToggle -> {
            if (newToggle != null) {
                final Pair<MapEditorTools, Types> toolData = (Pair<MapEditorTools, Types>) newToggle.getUserData();
                final MapEditorTools tool = toolData.getKey();
                final Types type = toolData.getValue();
                selectedToolProperty.setValue(tool);
                selectedTypeProperty.setValue(type);
            } else {
                selectedToolProperty.setValue(null);
                selectedTypeProperty.setValue(null);
            }
        });

        final ToggleButton moveButton = new ToggleButton();
        moveButton.setToggleGroup(toolToggleGroup);
        moveButton.setUserData(new Pair<>(MapEditorTools.MOVE, null));
        moveButton.setGraphic(new IconView("images/mapEditor/move.png", 40));
        moveButton.setSelected(true);
        getChildren().add(moveButton);

        final ToggleButton eraserButton = new ToggleButton();
        eraserButton.setToggleGroup(toolToggleGroup);
        eraserButton.setUserData(new Pair<>(MapEditorTools.ERASER, null));
        eraserButton.setGraphic(new IconView("images/mapEditor/eraser.png", 40));
        getChildren().add(eraserButton);

        final ToggleButton structuresEraserButton = new ToggleButton();
        structuresEraserButton.setToggleGroup(toolToggleGroup);
        structuresEraserButton.setUserData(new Pair<>(MapEditorTools.ERASER, Structure.Type.HUT));
        structuresEraserButton.setGraphic(new IconView("images/mapEditor/eraser_structures.png", 40));
        getChildren().add(structuresEraserButton);

        for (final Tile.Type tileType : Tile.Type.values()) {
            final ToggleButton tileBrushButton = new ToggleButton();
            tileBrushButton.setToggleGroup(toolToggleGroup);
            tileBrushButton.setUserData(new Pair<>(MapEditorTools.BRUSH, tileType));
            tileBrushButton.setGraphic(new TilePane(tileType, 20));
            getChildren().add(tileBrushButton);
        }

        for (final Structure.Type structureType : Structure.Type.values()) {
            final ToggleButton structureBrushButton = new ToggleButton();
            structureBrushButton.setToggleGroup(toolToggleGroup);
            structureBrushButton.setUserData(new Pair<>(MapEditorTools.BRUSH, structureType));
            structureBrushButton.setGraphic(new IconView(Images.STRUCTURE_ICONS.get(structureType), 40));
            getChildren().add(structureBrushButton);
        }
    }
}
