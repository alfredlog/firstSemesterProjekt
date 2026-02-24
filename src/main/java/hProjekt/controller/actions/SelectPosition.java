package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;
import hProjekt.model.grid.TilePosition;

/**
 * An action representing a player selecting a specific position on the board.
 * <p>
 * This action executes
 * {@link PlayerController#selectStartingPosition(TilePosition)}.
 *
 * @param position The position on the board selected by the player.
 */
public record SelectPosition(TilePosition position) implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.selectStartingPosition(position);
    }

}
