package hProjekt.controller;

import java.util.Set;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.controller.actions.AcceptCurse;
import hProjekt.controller.actions.AcceptTreasure;
import hProjekt.controller.actions.CollectAmulet;
import hProjekt.controller.actions.CollectTreasure;
import hProjekt.controller.actions.ConfirmTreasureCards;
import hProjekt.controller.actions.DrawTreasureCards;
import hProjekt.controller.actions.DriveAction;
import hProjekt.controller.actions.EndTurn;
import hProjekt.controller.actions.PlayPathCard;
import hProjekt.controller.actions.PlayerAction;
import hProjekt.controller.actions.SelectPosition;
import hProjekt.controller.actions.SelectTileToRemove;
import hProjekt.controller.actions.StartDrive;
import hProjekt.controller.actions.UseAmulet;

/**
 * This enum represents the different objectives a player can have and what
 * actions are allowed when the player has this objective.
 */
@DoNotTouch
public enum PlayerObjective {
    DRIVE(Set.of(DriveAction.class, CollectAmulet.class, UseAmulet.class, CollectTreasure.class, EndTurn.class)),
    EXTRA_DRIVE(Set.of(DriveAction.class, UseAmulet.class, CollectTreasure.class, EndTurn.class)),
    REGULAR_TURN(
            Set.of(CollectAmulet.class, PlayPathCard.class, UseAmulet.class, StartDrive.class, CollectTreasure.class)),
    DRAW_TREASURE_CARDS(Set.of(DrawTreasureCards.class)),
    CONFIRM_TREASURE_CARDS(Set.of(ConfirmTreasureCards.class)),
    ACCEPT_TREASURE(Set.of(AcceptTreasure.class)),
    ACCEPT_CURSE(Set.of(AcceptCurse.class)),
    SELECT_TILE_TO_REMOVE(Set.of(SelectTileToRemove.class)),
    PLAY_PATH_CARD(Set.of(PlayPathCard.class)),
    SELECT_POSITION(Set.of(SelectPosition.class)),
    IDLE(Set.of());

    final Set<Class<? extends PlayerAction>> allowedActions;

    /**
     * Constructs a new PlayerObjective with the specified set of allowed actions.
     *
     * @param allowedActions the set of action classes that define the valid moves
     *                       or behaviors associated with this objective. Each class
     *                       in the set must extend {@link PlayerAction}.
     */
    @DoNotTouch
    PlayerObjective(final Set<Class<? extends PlayerAction>> allowedActions) {
        this.allowedActions = allowedActions;
    }

    /**
     * Returns the actions that are allowed when the player has this objective.
     *
     * @return the actions that are allowed when the player has this objective
     */
    @DoNotTouch
    public Set<Class<? extends PlayerAction>> getAllowedActions() {
        return allowedActions;
    }
}
