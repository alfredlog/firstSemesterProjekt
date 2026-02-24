package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;

/**
 * An action to let the player draw treasure cards when distributing the
 * treasure.
 * <p>
 * This action executes {@link PlayerController#drawTreasureCards()}.
 */
public class DrawTreasureCards implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) {
        pc.drawTreasureCards();
    }
}
