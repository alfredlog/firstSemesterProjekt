package hProjekt.view.utils;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.Player;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A label that displays the name of a player and a circle with their color.
 */
public class PlayerLabel extends Label {
    private final Circle colorCircle = new Circle(10);

    /**
     * Creates a new PlayerLabel for the given player.
     *
     * @param player the player to display
     */
    public PlayerLabel(final Player player) {
        setGraphic(colorCircle);
        updatePlayer(player);
    }

    /**
     * Creates a new PlayerLabel with no player.
     */
    public PlayerLabel() {
        setGraphic(colorCircle);
        updatePlayer(null);
    }

    /**
     * Updates the label to display the given player.
     * <p>
     * If the player is null, the label displays "No Player" and the circle is
     * transparent.
     *
     * @param player the player to display
     */
    public void updatePlayer(@Nullable final Player player) {
        if (player == null) {
            setText("No Player");
            colorCircle.setFill(Color.TRANSPARENT);
            return;
        }
        setText(player.getName());
        colorCircle.setFill(player.getColor());
    }
}
