package hProjekt.view.overlays;

import hProjekt.model.Player;
import hProjekt.view.grid.AmuletPane;
import hProjekt.view.utils.PlayerLabel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * An overlay component that displays real-time information about the current
 * game state, specifically focusing on the active player and the current round.
 * <p>
 * This class extends {@link HBox} and arranges the following elements
 * horizontally:
 * <ul>
 * <li>A {@link PlayerLabel} showing the current player's name.</li>
 * <li>A label displaying the number of amulets held by the current player,
 * including a graphical icon.</li>
 * <li>A label displaying the current round number.</li>
 * </ul>
 * <p>
 * The component utilizes property binding and subscriptions to automatically
 * update UI elements whenever the underlying data (current player or round
 * number) changes.
 *
 * @see Player
 * @see Property
 * @see IntegerProperty
 */
public class GameInformationOverlay extends HBox {
    /**
     * Constructs a new GameInformationOverlay.
     * <p>
     * This overlay displays real-time information about the current state of the
     * game, including:
     * <ul>
     * <li>The current active player (name and color).</li>
     * <li>The number of amulets held by the current player.</li>
     * <li>The current round number.</li>
     * </ul>
     * The displayed information is bound to the provided properties and updates
     * automatically when the game state changes.
     *
     * @param currentPlayer A property holding the currently active {@link Player}.
     *                      The overlay subscribes to changes on this property to
     *                      update the player label and amulet count.
     * @param currentRound  An integer property representing the current game round.
     *                      The round label is bound to this property.
     */
    public GameInformationOverlay(final Property<Player> currentPlayer, final IntegerProperty currentRound) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setStyle("-fx-text-fill: white;");

        final PlayerLabel playerLabel = new PlayerLabel();
        currentPlayer.subscribe(playerLabel::updatePlayer);
        playerLabel.updatePlayer(currentPlayer.getValue());
        playerLabel.setStyle("-fx-text-fill: white;");

        final Label amuletLabel = new Label();
        amuletLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            final Player player = currentPlayer.getValue();
            if (player != null) {
                return "Amulets: " + player.getAmulets();
            } else {
                return "Amulets: 0";
            }
        }, currentPlayer));
        amuletLabel.setStyle("-fx-text-fill: white;");
        amuletLabel.setGraphic(new AmuletPane(10));

        final Label currentRoundLabel = new Label();
        currentRoundLabel.textProperty().bind(currentRound.asString("Round: %d"));
        currentRoundLabel.setStyle("-fx-text-fill: white;");
        getChildren().addAll(playerLabel, amuletLabel, currentRoundLabel);
    }
}
