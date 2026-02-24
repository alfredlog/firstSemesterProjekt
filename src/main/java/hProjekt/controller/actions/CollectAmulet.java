package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;

/**
 * Represents the action of a player collecting an amulet.
 * <p>
 * This action executes {@link PlayerController#collectAmulet()}.
 */
public class CollectAmulet implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.collectAmulet();
    }
}
