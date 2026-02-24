package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;

/**
 * An action to let the player accept a curse.
 * The player will then either lose an amulet or their highest valued gold card.
 */
public class AcceptCurse implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) {
        pc.acceptCurse();
    }
}
