package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;
import hProjekt.model.grid.TilePosition;
import javafx.scene.paint.Color;

/**
 * Represents an action where a player selects a specific tile to remove from
 * a treasure trail.
 * <p>
 * This action executes
 * {@link PlayerController#selectTileToRemove(TilePosition, Color)}.
 *
 * @param tilePosition The position of the tile to be removed on the board.
 * @param color        The color of the treasure trail associated with the tile
 *                     to be removed.
 */
public record SelectTileToRemove(TilePosition tilePosition, Color color) implements PlayerAction {
    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.selectTileToRemove(tilePosition, color);
    }
}
