package hProjekt.controller.actions;

import hProjekt.controller.AmuletAction;
import hProjekt.controller.PlayerController;

/**
 * Represents an action where a player uses an amulet during their turn.
 * <p>
 * This action executes {@link PlayerController#useAmulet(AmuletAction)}.
 *
 * @param amuletAction The specific amulet action to be performed.
 */
public record UseAmulet(AmuletAction amuletAction) implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) throws IllegalActionException {
        pc.useAmulet(amuletAction);
    }
}
