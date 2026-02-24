package hProjekt.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;
import hProjekt.controller.actions.PlayerAction;
import hProjekt.controller.actions.UseAmulet;
import hProjekt.model.GameState;
import hProjekt.model.Player;
import hProjekt.model.cards.CurseCard;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * The central controller responsible for managing the game flow, logic, and
 * component interaction. All cross player interactions are coordinated through
 * this class.
 * <p>
 * The {@code GameController} initializes
 * player and AI controllers, manages the main game loop, turn order, round
 * counting, and victory conditions.
 * <p>
 * Main functionalities:
 * <ul>
 * <li>Initializing the game and verifying player requirements.</li>
 * <li>Managing the lifecycle of a game round, including dealing cards and
 * processing player actions.</li>
 * <li>Handling the logic of treasure collection, including path validation,
 * distribution of {@link GoldCard}s, and application of
 * {@link CurseCard}s.</li>
 * <li>Coordinating control transfer between different {@link PlayerController}
 * instances using the {@link #activePlayerControllerProperty()}.</li>
 * </ul>
 */
public class GameController {
    private final GameState state;

    private final Map<Player, PlayerController> playerControllers;
    private final List<AiController> aiControllers = new ArrayList<>();
    private final IntegerProperty roundCounter = new SimpleIntegerProperty(0);

    private final Property<PlayerController> activePlayerController = new SimpleObjectProperty<>();

    private boolean stopped = false;

    /**
     * Creates a new GameController with the given game state.
     *
     * @param state the game state
     */
    public GameController(final GameState state) {
        this.state = state;
        playerControllers = new HashMap<>();
    }

    /**
     * Returns the game state.
     *
     * @return the game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Returns a map from players to player controllers.
     *
     * @return a map from players to player controllers
     */
    public Map<Player, PlayerController> getPlayerControllers() {
        return playerControllers;
    }

    /**
     * Returns a property that contains the active player controller.
     *
     * @return a property that contains the active player controller
     */
    public Property<PlayerController> activePlayerControllerProperty() {
        return activePlayerController;
    }

    /**
     * Returns the active player controller.
     *
     * @return the active player controller
     */
    public PlayerController getActivePlayerController() {
        return activePlayerController.getValue();
    }

    /**
     * Returns the round counter property.
     *
     * @return the round counter property
     */
    public IntegerProperty roundCounterProperty() {
        return roundCounter;
    }

    /**
     * Stops the game and the Thread.
     */
    public void stop() {
        stopped = true;
    }

    /**
     * Initializes the player controllers for each player in the game state. If a
     * player is an AI, it creates an AI controller for the player.
     */
    @DoNotTouch
    private void initPlayerControllers() {
        for (final Player player : state.getPlayers()) {
            playerControllers.put(player, new PlayerController(this, player));
            if (player.isAi()) {
                aiControllers.add(player.getAiController().factory.createAiController(playerControllers.get(player),
                        state.getGrid(), state, activePlayerController, roundCounter));
            }
        }
    }

    /**
     * Starts the game and handles the game loop.
     *
     * @throws IllegalStateException if there are not enough players
     */
    public void startGame() {
        if (state.getPlayers().size() < Config.MIN_PLAYERS) {
            throw new IllegalStateException("Not enough players");
        }
        if (playerControllers.isEmpty()) {
            initPlayerControllers();
        }

        roundCounter.set(0);

        for (final PlayerController pc : playerControllers.values()) {
            pc.drawPathCards(playerControllers.size() == 2 ? Config.MAX_CARDS_IN_HAND_WITH_TWO_PLAYERS
                    : Config.MAX_CARDS_IN_HAND);
            withActivePlayer(pc, () -> pc.waitForNextAction(PlayerObjective.SELECT_POSITION));
        }

        while (!state.getTreasureDeck().isEmpty()) {
            roundCounter.set(roundCounter.get() + 1);
            for (final PlayerController pc : playerControllers.values()) {
                pc.resetRoundVariables();
                withActivePlayer(pc, () -> {
                    PlayerAction action;
                    do {
                        action = pc.waitForNextAction(PlayerObjective.REGULAR_TURN);
                    } while (action instanceof UseAmulet);
                });
            }
        }

        state.getWinnerProperty().setValue(state.getPlayers().stream()
                .max(Comparator.comparingInt(Player::getTotalGoldCardValue))
                .get());
    }

    /**
     * Handles the collection of the treasure for a given color of the treasure
     * trail.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Retrieves the treasure trail for the specified color from the game
     * state.</li>
     * <li>Draws and shuffles the treasure cards for each player on the trail.</li>
     * <li>Distributes the drawn treasure cards among the players on the trail.</li>
     * <li>Clears the treasure trail for the specified color from the game
     * state.</li>
     * <li>If no player collected a gold card, randomly selects a player from the
     * trail to be the last collector.</li>
     * <li>Prompts the last collector to play a new {@link PathCard}.</li>
     * <li>Spawns amulets on the grid after the treasure collection process is
     * complete.</li>
     * </ol>
     *
     * @param color The color of the treasure trail being collected.
     *
     * @see #drawAndShuffleTreasureCards(List)
     * @see #distributeTreasureCards(List, Stack)
     * @see #withActivePlayer(PlayerController, Runnable)
     * @see PlayerController#waitForNextAction(PlayerObjective)
     * @see Config#RANDOM
     */
    @StudentImplementationRequired("P2.6")
    public void collectTreasure(final Color color) {
        // TODO: P2.6
        org.tudalgo.algoutils.student.Student.crash("P2.6 - Remove if implemented");
    }

    /**
     * Distributes treasure cards among players based on the provided treasure trail
     * and the drawn treasure cards.
     * <p>
     * This method iterates through the treasure cards and offers each card to the
     * players that contributed to the treasure trail in reverse order.
     * If a {@link CurseCard} is drawn, the method applies the curse effect to all
     * players that still have windroses on the trail and stops the distribution.
     * If a {@link GoldCard} is drawn, it is offered to the players via
     * {@link #offerTreasure(GoldCard, List)}.
     * <p>
     * The method returns the player who collected the last gold card, or null if no
     * gold was collected.
     *
     *
     * @param trail         The list of {@link PathCard}s that form the treasure
     *                      trail.
     * @param treasureCards The stack of {@link TreasureCard}s to be distributed
     *                      among the players on the trail.
     * @return the player who collected the last gold card, or null if no gold was
     *         collected.
     *
     * @see #curse(List)
     * @see #offerTreasure(GoldCard, List)
     */
    @StudentImplementationRequired("P2.6")
    private Player distributeTreasureCards(final List<PathCard> trail, final Stack<TreasureCard> treasureCards) {
        // TODO: P2.6
        return org.tudalgo.algoutils.student.Student.crash("P2.6 - Remove if implemented");
    }

    /**
     * Draws treasure cards for each player on the provided treasure trail and
     * shuffles them together with one additional card from the treasure deck.
     * <p>
     * For each player on the trail, the method calculates how many treasure cards
     * they are entitled to draw based on their contribution to the trail (i.e., how
     * many path cards they have on the trail).
     * Each player is then prompted to draw their allotted number of treasure cards.
     * <p>
     * The amount of treasure cards a player should draw is set using
     * {@link PlayerController#setTreasureCardsToDraw(int)}, and the player is
     * prompted to draw the cards by waiting for the
     * {@link PlayerObjective#DRAW_TREASURE_CARDS} objective. The drawn cards for
     * each player are collected and shuffled together with one additional card
     * drawn from the treasure deck.
     *
     * @param trail The list of {@link PathCard}s that form the treasure trail, used
     *              to determine how many treasure cards each player should draw.
     * @return A stack of drawn and shuffled {@link TreasureCard}s, including an
     *         extra card from the treasure deck.
     *
     * @see GameState#drawTreasureCard()
     * @see PlayerController#drawTreasureCards()
     * @see PlayerController#getDrawnTreasureCards()
     * @see PlayerController#setTreasureCardsToDraw(int)
     * @see PlayerController#waitForNextAction(PlayerObjective)
     * @see #withActivePlayer(PlayerController, Runnable)
     * @see PlayerObjective#DRAW_TREASURE_CARDS
     */
    @StudentImplementationRequired("P2.6")
    private Stack<TreasureCard> drawAndShuffleTreasureCards(final List<PathCard> trail) {
        // TODO: P2.6
        return org.tudalgo.algoutils.student.Student.crash("P2.6 - Remove if implemented");
    }

    /**
     * Applies a curse effect to the specified list of players.
     * <p>
     * Each player in the provided list is prompted once to acknowledge or accept
     * the curse {@link PlayerObjective#ACCEPT_CURSE}.
     * </p>
     *
     * @param windroses the list of players to be cursed; duplicate players in this
     *                  list are ignored.
     *
     * @see #withActivePlayer(PlayerController, Runnable)
     * @see PlayerController#waitForNextAction(PlayerObjective)
     */
    @StudentImplementationRequired("P2.5")
    private void curse(final List<Player> windroses) {
        // TODO: P2.5
        org.tudalgo.algoutils.student.Student.crash("P2.5 - Remove if implemented");
    }

    /**
     * Offers a {@link GoldCard} to a list of eligible players.
     * <p>
     * Each player is offered the gold card in the order they appear in the provided
     * list.
     * A player can be offered the card multiple times if they have multiple
     * windroses on the treasure trail.
     * The first player to accept the treasure claims the card and the windrose at
     * the current index is removed from the list.
     * <p>
     * The player who accepts the treasure is returned. If no player accepts the
     * treasure, the method returns {@code null}.
     *
     * @param card      The {@link GoldCard} on offer.
     * @param windroses A {@link List} of {@link Player}s who are eligible or
     *                  positioned to receive the treasure.
     *                  The player that accepts the treasure will have one of his
     *                  windroses removed from this list.
     * @return The {@link Player} who accepted the treasure, or {@code null} if all
     *         players declined.
     *
     * @see PlayerController#waitForNextAction(PlayerObjective)
     * @see PlayerController#hasAcceptedTreasure()
     * @see PlayerController#setOfferedGoldCard(GoldCard)
     * @see #withActivePlayer(PlayerController, Runnable)
     */
    @StudentImplementationRequired("P2.6")
    private Player offerTreasure(final GoldCard card, final List<Player> windroses) {
        // TODO: P2.6
        return org.tudalgo.algoutils.student.Student.crash("P2.6 - Remove if implemented");
    }

    /**
     * Executes a runnable action within the context of a specific active player
     * controller.
     * <p>
     * This method temporarily sets the provided {@link PlayerController} as
     * the active controller, executes the given {@link Runnable}, and then resets
     * the player's objective to {@link PlayerObjective#IDLE} before clearing the
     * active controller.
     * </p>
     *
     * @param pc The {@link PlayerController} to set as active during the execution.
     * @param r  The {@link Runnable} action to execute while the player is active.
     */
    @DoNotTouch
    public void withActivePlayer(final PlayerController pc, final Runnable r) {
        if (stopped) {
            throw new RuntimeException("Game was stopped");
        }
        activePlayerController.setValue(pc);
        r.run();
        pc.setPlayerObjective(PlayerObjective.IDLE);
        activePlayerController.setValue(null);
    }

    @Override
    public String toString() {
        return "GameController[" + "state=" + state + ", playerControllers=" + playerControllers
                + ", activePlayerController=" + activePlayerController + ", stopped=" + stopped + ']';
    }
}
