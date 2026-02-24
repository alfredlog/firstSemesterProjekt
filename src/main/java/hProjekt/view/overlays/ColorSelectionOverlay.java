package hProjekt.view.overlays;

import java.util.List;
import java.util.Map;

import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * An overlay that allows the user to select a {@link Color} from a set of
 * options.
 * This class extends {@link SelectionOverlay} to specialize in color-based
 * selections, displaying options as toggle buttons with circular color
 * indicators.
 */
public class ColorSelectionOverlay extends SelectionOverlay<Color> {

    /**
     * Populates the overlay with a list of selectable colors.
     * Each color is represented by a {@link ToggleButton} containing a
     * {@link Circle} filled with that color. The buttons are added to the selection
     * button box and associated with a shared toggle group.
     *
     * @param colors A list of {@link Color} objects to be displayed as selectable
     *               options.
     */
    public void setColors(final List<Color> colors) {
        selectionButtonBox.getChildren().clear();

        for (final Color color : colors) {
            final ToggleButton treasureTrailButton = new ToggleButton();
            treasureTrailButton.setGraphic(new Circle(10, color));
            treasureTrailButton.setToggleGroup(selectionToggleGroup);
            treasureTrailButton.setUserData(color);
            selectionButtonBox.getChildren().add(treasureTrailButton);
        }
    }

    /**
     * Populates the overlay with a set of selectable colors accompanied by textual
     * descriptions.
     * Each entry in the map creates a {@link ToggleButton} showing both the text
     * value and a {@link Circle} graphic representing the color key.
     *
     * @param selections A map where the key is the {@link Color} and the value is a
     *                   {@link String}.
     */
    @Override
    public void setSelection(final Map<Color, String> selections) {
        selectionButtonBox.getChildren().clear();

        for (final Map.Entry<Color, String> entry : selections.entrySet()) {
            final ToggleButton selectionButton = new ToggleButton();
            selectionButton.setText(entry.getValue());
            selectionButton.setGraphic(new Circle(10, entry.getKey()));
            selectionButton.setToggleGroup(selectionToggleGroup);
            selectionButton.setUserData(entry.getKey());
            selectionButtonBox.getChildren().add(selectionButton);
        }
    }
}
