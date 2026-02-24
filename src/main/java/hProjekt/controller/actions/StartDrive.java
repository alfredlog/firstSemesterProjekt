package hProjekt.controller.actions;

import hProjekt.controller.PlayerController;

/**
 * Represents the action of starting the drive mode in the game.
 * <p>
 * This action executes {@link PlayerController#startDrive()}.
 */
public record StartDrive() implements PlayerAction {

    @Override
    public void execute(final PlayerController pc) {
        pc.startDrive();
    }

}
