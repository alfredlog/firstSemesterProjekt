package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;
import hProjekt.model.cards.PathCard;
import javafx.scene.paint.Color;

/**
 * Represents an action where a player plays a {@link PathCard} on the game
 * board.
 * <p>
 * This action involves placing a specific card for a designated treasure trail.
 *
 * @param pathCard  The path card to be played.
 * @param pathColor The color of the treasure trail associated with the path
 *                  card.
 */
public record PlayPathCard(PathCard pathCard, Color pathColor) implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.playCard(pathCard, pathColor);
    }
}
