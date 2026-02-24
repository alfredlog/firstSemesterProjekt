package hProjekt.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;
import hProjekt.controller.actions.IllegalActionException;
import hProjekt.controller.actions.PlayerAction;
import hProjekt.model.GameState;
import hProjekt.model.Player;
import hProjekt.model.PlayerState;
import hProjekt.model.cards.CardType;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.RemoveTileCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.TilePosition;
import hProjekt.model.grid.Types;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * The PlayerController class represents a controller for a {@link Player} in
 * the game.
 * It manages the player's state, objectives, actions and all methods the UI
 * needs to interact with.
 * It receives objectives the player wants to achieve and waits for the UI or AI
 * to trigger any allowed actions. It then executes the actions and updates the
 * player's state.
 */
public class PlayerController {
    private final GameController gameController;
    private final BlockingDeque<PlayerAction> actions = new LinkedBlockingDeque<>();

    private final Player player;
    private final Property<PlayerState> playerStateProperty = new SimpleObjectProperty<>(
            new PlayerState(PlayerObjective.IDLE, List.of(), Set.of(), null, Map.of(), Set.of()));
    private PlayerObjective playerObjective = PlayerObjective.IDLE;

    private int treasureCardsToDraw = 0;
    private List<TreasureCard> drawnTreasureCards = List.of();
    private GoldCard offeredGoldCard;
    private boolean hasAcceptedTreasure = false;

    private int driveCount = 0;
    private Map<Color, List<PathCard>> validPathCards = Map.of();

    /**
     * Creates a new player controller with the given {@link GameController}
     * and {@link Player}.
     *
     * @param gameController the {@link GameController} that manages the game logic
     *                       and this controller is part of.
     * @param player         the {@link Player} this controller belongs to. It is
     *                       assumed that the player is valid.
     */
    @DoNotTouch
    public PlayerController(final GameController gameController, final Player player) {
        this.gameController = gameController;
        this.player = player;
    }

    /**
     * Returns the {@link Player}.
     *
     * @return the {@link Player}.
     */
    @DoNotTouch
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the {@link GameState}.
     *
     * @return the {@link GameState}.
     */

    private GameState getState() {
        return gameController.getState();
    }

    /**
     * Returns a {@link Property} with the current {@link PlayerState}.
     *
     * @return a {@link Property} with the current {@link PlayerState}.
     */
    @DoNotTouch
    public Property<PlayerState> getPlayerStateProperty() {
        return playerStateProperty;
    }

    /**
     * Returns the current {@link PlayerState}.
     *
     * @return the current {@link PlayerState}.
     */

    public PlayerState getPlayerState() {
        return playerStateProperty.getValue();
    }

    /**
     * Updates the {@link #playerStateProperty} with the current
     * {@link PlayerState}.
     */
    @DoNotTouch
    private void updatePlayerState() {
        validPathCards = getValidPathCards();
        playerStateProperty.setValue(
                new PlayerState(playerObjective, drawnTreasureCards, getDrivableTiles(), offeredGoldCard,
                        validPathCards, updateCollectableTreasure()));
    }

    /**
     * Returns the current {@link PlayerObjective}
     *
     * @return the current {@link PlayerObjective}
     */
    @DoNotTouch

    public PlayerObjective getPlayerObjective() {
        return playerObjective;
    }

    /**
     * Sets the player objective.
     * Also updates the player state, when the objective is IDLE.
     *
     * @param nextObjective the objective to set
     */
    @DoNotTouch

    public void setPlayerObjective(final PlayerObjective nextObjective) {
        playerObjective = nextObjective;
        if (PlayerObjective.IDLE.equals(nextObjective)) {
            updatePlayerState();
        }
    }

    /**
     * Returns the currently offered gold card.
     *
     * @return the {@link GoldCard} that is currently offered to the player
     */
    public GoldCard getOfferedGoldCard() {
        return offeredGoldCard;
    }

    /**
     * Sets the currently offered gold card.
     *
     * @param offeredGoldCard the {@link GoldCard} to set as the offered card
     */
    public void setOfferedGoldCard(final GoldCard offeredGoldCard) {
        this.offeredGoldCard = offeredGoldCard;
    }

    /**
     * Sets the number of treasure cards the player is entitled to draw.
     *
     * @param treasureCardsToDraw the number of treasure cards to be drawn
     */
    public void setTreasureCardsToDraw(final int treasureCardsToDraw) {
        this.treasureCardsToDraw = treasureCardsToDraw;
    }

    /**
     * Retrieves the list of treasure cards currently drawn by the player.
     *
     * @return A list of {@link TreasureCard} objects representing the cards drawn.
     */
    public List<TreasureCard> getDrawnTreasureCards() {
        return drawnTreasureCards;
    }

    /**
     * Checks if the player has accepted the offered treasure.
     *
     * @return true if the player has accepted the treasure, false otherwise
     */
    public boolean hasAcceptedTreasure() {
        return hasAcceptedTreasure;
    }

    /**
     * Resets round-specific variables for the player.
     */
    public void resetRoundVariables() {
        driveCount = 0;
    }

    // Process Actions

    /**
     * Gets called from viewer thread to trigger an Action. This action will then be
     * waited for using the method {@link #waitForNextAction()}.
     *
     * @param action The Action that should be triggered next
     */
    @DoNotTouch
    public void triggerAction(final PlayerAction action) {
        actions.add(action);
    }

    /**
     * Takes the next action from the queue. This method blocks until an action is
     * in the queue.
     *
     * @return The next action
     * @throws InterruptedException if the thread is interrupted while waiting for
     *                              the next action
     */
    @DoNotTouch
    public PlayerAction blockingGetNextAction() throws InterruptedException {
        return actions.take();
    }

    /**
     * Waits for the next action and executes it.
     *
     * @param nextObjective the objective to set before the action is awaited
     * @return the executed action
     */
    @DoNotTouch
    public PlayerAction waitForNextAction(final PlayerObjective nextObjective) {
        setPlayerObjective(nextObjective);
        return waitForNextAction();
    }

    /**
     * Waits for a action to be triggered, checks if the action is allowed and then
     * executes it.
     * If a {@link IllegalActionException} is thrown, the action is ignored and the
     * next action is awaited. This is done to ensure only allowed actions are
     * executed.
     *
     * @return the executed action
     */
    @DoNotTouch
    public PlayerAction waitForNextAction() {
        try {
            updatePlayerState();
            // blocking, waiting for viewing thread
            final PlayerAction action = blockingGetNextAction();

            System.out.println("TRIGGER " + action + " [" + player.getName() + "]");

            if (!playerObjective.allowedActions.contains(action.getClass())) {
                throw new IllegalActionException(String.format("Illegal Action %s performed. Allowed Actions: %s",
                        action, playerObjective.getAllowedActions()));
            }
            action.execute(this);
            return action;
        } catch (final IllegalActionException e) {
            // Ignore and keep going
            e.printStackTrace();
            return waitForNextAction();
        } catch (final InterruptedException e) {
            throw new RuntimeException("Main thread was interrupted!", e);
        }
    }

    // Calculations

    /**
     * Calculates all drivable tiles from the player's current position.
     * <p>
     * A player can drive to all tiles within the current tile's area and all
     * neighboring tiles. The current tile is not included in the result.
     *
     * @return a set of drivable {@link Tile} objects
     */
    @StudentImplementationRequired("P2.1")
    public Set<Tile> getDrivableTiles() {
        // TODO: P2.1
        return org.tudalgo.algoutils.student.Student.crash("P2.1 - Remove if implemented");
    }

    /**
     * Retrieves a map of valid path cards that can be added to each treasure trail.
     * <p>
     * This method checks for each path card if it can be added to any treasure
     * trail. If a card can be added to a trail it is included in the resulting map
     * under the corresponding trail color.
     * </p>
     *
     * @return A {@code Map} where the key is the {@link Color} of the treasure
     *         trail and the value is a {@code List} of {@link PathCard}s the
     *         player can place on that trail. If no cards can be placed on a
     *         trail, the color is not present in the map.
     *
     * @see GameState#canAddCardToTreasureTrail(Color, PathCard)
     */
    @StudentImplementationRequired("P2.2")
    private Map<Color, List<PathCard>> getValidPathCards() {
        // TODO: P2.2
        return org.tudalgo.algoutils.student.Student.crash("P2.2 - Remove if implemented");
    }

    /**
     * Determines which treasures the player can collect at their current position
     * and saves them in the {@link PlayerState#collectableTreasures()} set.
     * <p>
     * A treasure can be collected if the treasure trail points to a unique tile.
     * </p>
     *
     * @see GameState#evaluateTreasureTrail(Color)
     */
    @StudentImplementationRequired("P2.7")
    public Set<Color> updateCollectableTreasure() {
        // TODO: P2.7
        return org.tudalgo.algoutils.student.Student.crash("P2.7 - Remove if implemented");
    }

    // Actions

    /**
     * Plays a card on the treasure trail of the specified color.
     * <p>
     * This method tries to add the given card to the treasure trail.
     * If the card cannot be added to the trail or the player does not own this
     * card, an IllegalActionException is thrown.
     * <p>
     * If the card is successfully played, it is removed from the player deck and a
     * new card is drawn.
     *
     * @param card       the card to be played
     * @param trailColor the color of the treasure trail to play the card on
     * @throws IllegalActionException if the card cannot be added to the trail or
     *                                the player does not own this card
     *
     * @see GameState#addCardToTreasureTrail(Color, PathCard)
     * @see #drawPathCard()
     */
    @StudentImplementationRequired("P2.2")
    public void playCard(final PathCard card, final Color trailColor) throws IllegalActionException {
        // TODO: P2.2
        org.tudalgo.algoutils.student.Student.crash("P2.2 - Remove if implemented");
    }

    /**
     * Attempts to collect a treasure associated with a specific trail color.
     * <p>
     * This method evaluates the treasure trail for the given color. It ensures that
     * the trail leads to a unique, single tile and that the player is currently
     * positioned on that specific tile.
     * If these conditions are met, the treasure is collected via the game
     * controller.
     * </p>
     *
     * @param trailColor the color of the treasure trail to evaluate and collect.
     * @throws IllegalActionException if the treasure trail does not lead to exactly
     *                                one specific tile, or if the player is not
     *                                currently located on the identified tile.
     *
     * @see GameState#evaluateTreasureTrail(Color)
     * @see GameController#collectTreasure(Color)
     */
    @StudentImplementationRequired("P2.3")
    public void collectTreasure(final Color trailColor) throws IllegalActionException {
        // TODO: P2.3
        org.tudalgo.algoutils.student.Student.crash("P2.3 - Remove if implemented");
    }

    /**
     * Sets the starting position for the player on the game grid.
     *
     * <p>
     * This method ensures that a tile within the games grid is selected. If valid,
     * the player's position is updated.
     * </p>
     *
     * @param position The position on the grid to be selected as the starting
     *                 point.
     * @throws IllegalActionException If the tile at the specified position does not
     *                                exist.
     */
    public void selectStartingPosition(final TilePosition position) throws IllegalActionException {
        if (getState().getGrid().getTileAt(position) == null) {
            throw new IllegalActionException("Invalid starting position selected.");
        }
        player.setPosition(position);
    }

    /**
     * Draws treasure cards for the player.
     * <p>
     * This method draws {@link #treasureCardsToDraw} treasure cards and stores them
     * in the {@link #drawnTreasureCards} set.
     * After drawing, the player is prompted to confirm the drawn cards.
     *
     * @see GameState#drawTreasureCard()
     */
    @StudentImplementationRequired("P2.2")
    public void drawTreasureCards() {
        // TODO: P2.2
        org.tudalgo.algoutils.student.Student.crash("P2.2 - Remove if implemented");
    }

    /**
     * Accepts or rejects the offered treasure.
     * <p>
     * If the treasure is accepted, it is added to the player's gold cards.
     * Does nothing otherwise.
     *
     * @param accept true to accept the treasure, false to reject it
     */
    public void acceptTreasure(final boolean accept) {
        hasAcceptedTreasure = accept;
        if (!accept) {
            return;
        }
        player.getGoldCards().add(offeredGoldCard);
    }

    /**
     * Accepts a curse.
     * <p>
     * If the player has amulets, one is consumed to negate the curse.
     * Otherwise, the most valuable gold card is removed from the player's deck.
     */
    @StudentImplementationRequired("P2.3")
    public void acceptCurse() {
        // TODO: P2.3
        org.tudalgo.algoutils.student.Student.crash("P2.3 - Remove if implemented");
    }

    /**
     * Collects an amulet from the player's current tile and adds it to the player.
     * <p>
     * This method checks if the current tile has an amulet. If it does, the
     * amulet is added to the player and the amulet is removed from the tile. If no
     * amulet is present on the tile, an {@link IllegalActionException} is thrown.
     *
     * @throws IllegalActionException if the player cannot collect amulets, or if no
     *                                amulet is present on the current tile
     */
    @StudentImplementationRequired("P2.4")
    public void collectAmulet() throws IllegalActionException {
        // TODO: P2.4
        org.tudalgo.algoutils.student.Student.crash("P2.4 - Remove if implemented");
    }

    /**
     * Executes the logic for using an amulet.
     * <p>
     * If the player has no amulets, an {@link IllegalActionException} is thrown.
     * Otherwise the player's amulet count is decremented and actions based on the
     * type of {@link AmuletAction} are performed.
     * <ul>
     * <li>{@link AmuletAction#REMOVE_TILE}: Triggers a state waiting for the player
     * to select a tile to remove
     * ({@link PlayerObjective#SELECT_TILE_TO_REMOVE}).</li>
     * <li>{@link AmuletAction#PLAY_HINT}: Triggers a state waiting for the player
     * to play a path card ({@link PlayerObjective#PLAY_PATH_CARD}).</li>
     * <li>{@link AmuletAction#EXTRA_DRIVE}: Temporarily enables an extra drive
     * move, resetting the drive count for this specific action and triggering a
     * state waiting for the player to perform an extra drive
     * ({@link PlayerObjective#EXTRA_DRIVE}). After the extra drive action is
     * performed, the drive count is restored to its previous state.</li>
     * <li>{@link AmuletAction#REDRAW_PATH_CARDS}: Discards the player's current
     * hand and draws a fresh set of path cards up to
     * {@link Config#MAX_CARDS_IN_HAND}/{@link Config#MAX_CARDS_IN_HAND_WITH_TWO_PLAYERS}.</li>
     * </ul>
     * After the specific amulet action was performed, the previous objective is
     * awaited.
     *
     * @param amuletAction The specific type of amulet action to perform
     * @throws IllegalActionException If the player has no amulets remaining in
     *                                their inventory.
     *
     * @see #drawPathCards(int)
     */
    @StudentImplementationRequired("P2.4")
    public void useAmulet(final AmuletAction amuletAction) throws IllegalActionException {
        // TODO: P2.4
        org.tudalgo.algoutils.student.Student.crash("P2.4 - Remove if implemented");
    }

    /**
     * Selects a tile to be removed from the specified treasure trail.
     * <p>
     * This method verifies if the specified tile at {@code tilePosition} is part of
     * the treasure trail for the given {@code color}.
     * If it is, it adds a {@link RemoveTileCard} action to the specified treasure
     * trail.
     * If the tile is not part of the treasure trail, an
     * {@link IllegalActionException} is thrown.
     * </p>
     *
     * @param tilePosition The position of the tile to be removed.
     * @param color        The color associated with the treasure trail being
     *                     evaluated.
     * @throws IllegalActionException If the tile at the specified position is not
     *                                part of the evaluated treasure trail.
     *
     * @see GameState#evaluateTreasureTrail(Color)
     * @see GameState#addCardToTreasureTrail(Color, PathCard)
     * @see RemoveTileCard
     */
    @StudentImplementationRequired("P2.4")
    public void selectTileToRemove(final TilePosition tilePosition, final Color color) throws IllegalActionException {
        // TODO: P2.4
        org.tudalgo.algoutils.student.Student.crash("P2.4 - Remove if implemented");
    }

    /**
     * Initiates the driving action for the player.
     * <p>
     * This method sets the player's current objective to
     * {@link PlayerObjective#DRIVE}.
     */
    public void startDrive() {
        waitForNextAction(PlayerObjective.DRIVE);
    }

    /**
     * Moves the player to the specified target tile if it is drivable.
     * <p>
     * This method checks if the target tile is in the list of drivable tiles and
     * if the player has not exceeded the {@link Config#DRIVE_LIMIT drive limit} for
     * the current turn. If any of these conditions are not met, an
     * {@link IllegalActionException} is thrown.
     * <p>
     * If the move is allowed, the player's position is updated to the target tile's
     * position and the {@link #driveCount drive count} is incremented. If the drive
     * count after moving is still less than the drive limit, the player is prompted
     * to perform another action with the same {@link #playerObjective objective}.
     *
     * @param targetTile the tile to which the player should be moved
     * @throws IllegalActionException if the player has reached the drive limit for
     *                                the turn or if the target tile cannot be
     *                                driven to
     */
    @StudentImplementationRequired("P2.1")
    public void drive(final Tile targetTile) throws IllegalActionException {
        // TODO: P2.1
        org.tudalgo.algoutils.student.Student.crash("P2.1 - Remove if implemented");
    }

    // Path Cards

    /**
     * Draws a specified number of path cards and adds them to the player's deck.
     *
     * @see #drawPathCard()
     *
     * @param amount the number of path cards to draw
     */
    public void drawPathCards(final int amount) {
        for (int i = 0; i < amount; i++) {
            drawPathCard();
        }
    }

    /**
     * Draws a random path card and adds it to the player's collection.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Selects a random {@link CardType}.</li>
     * <li>Constructs a list of possible {@link Types} (initially containing all
     * {@link Tile.Type} values)</li>
     * <li>Depending on the selected {@link CardType}, {@code null}
     * and all {@link Structure.Type} are added to the list of possible types.
     * Area card types only allow {@link Tile.Type} values.</li>
     * <li>Selects a random type from the constructed list.</li>
     * <li>Instantiates the new card using the {@link CardType}.</li>
     * <li>Adds the newly created card to the player's path cards.</li>
     * </ol>
     */
    private void drawPathCard() {
        final CardType cardType = CardType.values()[Config.RANDOM.nextInt(CardType.values().length)];
        final List<Types> types = new ArrayList<>(Arrays.asList(Tile.Type.values()));
        if (!cardType.equals(CardType.IN_AREA) && !cardType.equals(CardType.IN_BIGGEST_AREA)
                && !cardType.equals(CardType.NOT_IN_AREA) && !cardType.equals(CardType.NOT_IN_BIGGEST_AREA)) {
            types.add(null);
            types.addAll(Arrays.asList(Structure.Type.values()));
        }
        final Types tileType = types.get(Config.RANDOM.nextInt(types.size()));
        player.getPathCards().add(cardType.cardConstructor.apply(player, tileType));
    }

    @Override
    public String toString() {
        return "PlayerController[" +
                "gameController=" + gameController +
                ", actions=" + actions +
                ", player=" + player +
                ", playerStateProperty=" + playerStateProperty +
                ", playerObjective=" + playerObjective +
                ", treasureCardsToDraw=" + treasureCardsToDraw +
                ", drawnTreasureCards=" + drawnTreasureCards +
                ", offeredGoldCard=" + offeredGoldCard +
                ", hasAcceptedTreasure=" + hasAcceptedTreasure +
                ", driveCount=" + driveCount +
                ", validPathCards=" + validPathCards +
                ']';
    }
}
