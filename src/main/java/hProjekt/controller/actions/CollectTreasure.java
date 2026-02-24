package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;
import javafx.scene.paint.Color;

/**
 * Represents the action of a player collecting a treasure on the game board.
 * <p>
 * This action executes {@link PlayerController#collectTreasure(Color)}.
 *
 * @param trailColor The color of the trail associated with the treasure to be
 *                   collected.
 */
public record CollectTreasure(Color trailColor) implements PlayerAction {
    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.collectTreasure(trailColor);
    }
}
