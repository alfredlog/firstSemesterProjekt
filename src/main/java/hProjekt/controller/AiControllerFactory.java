package hProjekt.controller;

import hProjekt.model.GameState;
import hProjekt.model.grid.HexGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;

/**
 * Factory interface for creating AI controller instances.
 *
 * @see AiController
 */
public interface AiControllerFactory {
    /**
     * Creates a new AI controller instance.
     *
     * @param playerController       the player controller associated with the AI
     * @param hexGrid                the hex grid of the game
     * @param gameState              the current game state
     * @param activePlayerController the property representing the active player
     *                               controller
     * @param roundCounterProperty   the property representing the round counter
     * @return a new AI controller instance
     */
    AiController createAiController(final PlayerController playerController, final HexGrid hexGrid,
            final GameState gameState, final Property<PlayerController> activePlayerController,
            final IntegerProperty roundCounterProperty);
}
