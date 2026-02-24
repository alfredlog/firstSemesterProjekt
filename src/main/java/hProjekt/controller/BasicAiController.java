package hProjekt.controller;

import java.util.Set;

import hProjekt.controller.actions.AcceptCurse;
import hProjekt.controller.actions.AcceptTreasure;
import hProjekt.controller.actions.CollectTreasure;
import hProjekt.controller.actions.ConfirmTreasureCards;
import hProjekt.controller.actions.DrawTreasureCards;
import hProjekt.controller.actions.EndTurn;
import hProjekt.controller.actions.PlayPathCard;
import hProjekt.controller.actions.PlayerAction;
import hProjekt.controller.actions.SelectPosition;
import hProjekt.controller.actions.StartDrive;
import hProjekt.model.GameState;
import hProjekt.model.cards.PathCard;
import hProjekt.model.grid.HexGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.scene.paint.Color;

/**
 * A basic AI controller as an example for how to implement an AI controller.
 */
public class BasicAiController extends AiController {

    /**
     * Creates a new basic AI controller with the given player controller, hex grid,
     * game state and active player controller.
     * Adds a subscription to the player objective property to execute actions when
     * the player's objective changes.
     *
     * @param playerController       the player controller
     * @param hexGrid                the hex grid
     * @param gameState              the game state
     * @param activePlayerController the active player controller
     */
    public BasicAiController(final PlayerController playerController, final HexGrid hexGrid, final GameState gameState,
            final Property<PlayerController> activePlayerController,
            final IntegerProperty roundCounterProperty) {
        super(playerController, hexGrid, gameState, activePlayerController, roundCounterProperty);
    }

    @Override
    protected void executeActionBasedOnObjective(final PlayerObjective objective) {
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new RuntimeException("Main thread was interrupted", e);
        }

        final Set<Class<? extends PlayerAction>> allowedActions = playerController.getPlayerObjective()
                .getAllowedActions();

        if (allowedActions.contains(PlayPathCard.class)) {
            if (playerController.getPlayerState().validPathCards().isEmpty()) {
                playerController.triggerAction(new StartDrive());
                return;
            }
            playerController.getPlayerState().validPathCards().entrySet().stream().findAny().ifPresent(entry -> {
                final Color card = entry.getKey();
                final PathCard color = entry.getValue().stream().findAny().orElseThrow();
                playerController.triggerAction(new PlayPathCard(color, card));
            });
        }
        if (allowedActions.contains(DrawTreasureCards.class)) {
            playerController.triggerAction(new DrawTreasureCards());
        }
        if (allowedActions.contains(ConfirmTreasureCards.class)) {
            playerController.triggerAction(new ConfirmTreasureCards());
        }
        if (allowedActions.contains(AcceptTreasure.class)) {
            playerController.triggerAction(new AcceptTreasure(true));
        }
        if (allowedActions.contains(AcceptCurse.class)) {
            playerController.triggerAction(new AcceptCurse());
        }
        if (allowedActions.contains(SelectPosition.class)) {
            playerController.triggerAction(new SelectPosition(
                    hexGrid.getTiles().values().stream().findAny().orElseThrow().getPosition()));
        }
        if (allowedActions.contains(CollectTreasure.class)
                && !playerController.getPlayerState().collectableTreasures().isEmpty()) {
            playerController.triggerAction(
                    new CollectTreasure(playerController.getPlayerState().collectableTreasures().iterator().next()));
        }
        if (allowedActions.contains(EndTurn.class)) {
            playerController.triggerAction(new EndTurn());
        }
    }
}
