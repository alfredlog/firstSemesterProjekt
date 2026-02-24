package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;

/**
 * An action to let the player accept or reject the treasure offered by the
 * game.
 * <p>
 * This action executes {@link PlayerController#acceptTreasure(boolean)}.
 *
 * @param accept Whether the player accepts the treasure or not.
 */
public record AcceptTreasure(boolean accept) implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) {
        pc.acceptTreasure(accept);
    }
}
