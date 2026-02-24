package hProjekt.model;

import static hProjekt.Config.RANDOM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;
import hProjekt.model.cards.CurseCard;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.Tile;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * Holds information on the game's state.
 */
@DoNotTouch
public final class GameState {

    /**
     * The {@link HexGrid} instance of this game state.
     */
    private HexGrid grid;

    /**
     * The {@link Player}s of this game state.
     */
    private final List<Player> players;

    /**
     * A map that stores the list of {@link PathCard}s for each treasure trail
     * color.
     */
    private final Map<Color, List<PathCard>> treasureTrails = new HashMap<>();

    /**
     * A map that caches the evaluated treasure positions for each color.
     */
    private final Map<Color, Set<Tile>> treasurePositions = new HashMap<>();

    /**
     * The winner of this game state, if the game is over.
     */
    private final Property<Player> winnerProperty = new SimpleObjectProperty<>();

    /**
     * The treasure deck, which contains the {@link TreasureCard}s.
     */
    private final Stack<TreasureCard> treasureDeck;

    /**
     * Creates a new game state with the given {@link HexGrid} and
     * {@link Player}s.
     *
     * @param grid    the {@link HexGrid}
     * @param players the {@link Player}s
     */
    public GameState(final HexGrid grid, final List<Player> players) {
        this.grid = grid;
        this.players = players;
        treasureDeck = initTreasureDeck();
        initTreasureTrails();
    }

    /**
     * Initializes the {@link #treasureTrails} map with empty lists for each color.
     */
    private void initTreasureTrails() {
        for (final Color c : new Color[] { Color.GRAY, Color.BROWN, Color.BLACK, Color.WHITE }) {
            treasureTrails.put(c, new ArrayList<>());
        }
    }

    /**
     * Initializes the treasure deck with {@link GoldCard}s and {@link CurseCard}s.
     * <p>
     * The deck is created by first generating {@link Config#LOWER_PILE_GOLD_CARDS}
     * {@link GoldCard}s, followed by {@link Config#TOTAL_CURSE_CARDS}
     * {@link CurseCard}s, shuffling the deck, and finally adding
     * {@link Config#TOP_PILE_GOLD_CARDS} {@link GoldCard}s on top.
     *
     * @return the initialized treasure deck
     */
    private Stack<TreasureCard> initTreasureDeck() {
        final Stack<TreasureCard> treasureDeck = new Stack<>();
        treasureDeck.addAll(generateGoldCards(Config.LOWER_PILE_GOLD_CARDS));
        treasureDeck.addAll(Stream.generate(CurseCard::new).limit(Config.TOTAL_CURSE_CARDS).toList());
        Collections.shuffle(treasureDeck);

        treasureDeck.addAll(generateGoldCards(Config.TOP_PILE_GOLD_CARDS));
        return treasureDeck;
    }

    /**
     * Generates a list of {@link GoldCard}s with random values.
     *
     * @param amount the number of {@link GoldCard}s to generate
     * @return a list of generated {@link GoldCard}s
     */
    private static List<GoldCard> generateGoldCards(final int amount) {
        return IntStream.rangeClosed(1, amount)
                .mapToObj(i -> new GoldCard(RANDOM.nextInt(1, Config.MAX_GOLD_CARD_VALUE + 1)))
                .toList();
    }

    /**
     * Returns the {@link HexGrid} instance of this game state.
     *
     * @return the {@link HexGrid} instance of this game state.
     */
    public HexGrid getGrid() {
        return grid;
    }

    /**
     * Sets the {@link HexGrid} instance of this game state.
     *
     * @param grid the {@link HexGrid} instance to set
     */
    public void setGrid(final HexGrid grid) {
        this.grid = grid;
    }

    /**
     * Returns an unmodifiable list of all {@link Player}s in this
     * game state.
     *
     * @return an unmodifiable list of all {@link Player}s in this
     *         game state.
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Returns a {@link Property} containing the winner of this game state.
     *
     * @return a {@link Property} containing the winner of this game state.
     */
    public Property<Player> getWinnerProperty() {
        return winnerProperty;
    }

    /**
     * Returns {@code true} if the game is over, {@code false} otherwise.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() {
        return winnerProperty.getValue() != null;
    }

    /**
     * Sets the winner of this game state.
     *
     * @param winner the winner of this game state
     */
    public void setWinner(final Player winner) {
        winnerProperty.setValue(winner);
    }

    /**
     * Retrieves the treasure trail for the given {@link Color}.
     * <p>
     * If no trail exists for the specified color, a new empty list is created and
     * returned.
     *
     * @param color the {@link Color} of the treasure trail
     * @return the list of {@link PathCard}s in the treasure trail
     */
    @StudentImplementationRequired("P1.5")
    public List<PathCard> getTreasureTrail(final Color color) {
        // TODO: P1.5
        return org.tudalgo.algoutils.student.Student.crash("P1.5 - Remove if implemented");
    }

    /**
     * Evaluates and returns the set of {@link Tile}s that are part of the treasure
     * trail for the given {@link Color}.
     * <p>
     * If the {@link #treasurePositions} for the specified color have already been
     * evaluated and cached, it returns the cached set.
     * Otherwise it computes the set by filtering the tiles through each
     * {@link PathCard} in the treasure trail reducing the set of possible tiles at
     * each step.
     * The result is then cached for future calls.
     *
     * @param color the {@link Color} of the treasure trail
     * @return the set of {@link Tile}s in the treasure trail
     *
     * @see PathCard#filter(Set)
     */
    @StudentImplementationRequired("P1.5")
    public Set<Tile> evaluateTreasureTrail(final Color color) {
        // TODO: P1.5
        return org.tudalgo.algoutils.student.Student.crash("P1.5 - Remove if implemented");
    }

    /**
     * Checks if a {@link PathCard} can be added to the treasure trail for the given
     * {@link Color}.
     * <p>
     * A card can be added if it reduces the number of possible treasure positions
     * in the trail and does not eliminate all possibilities. If the trail is empty,
     * any card can be added.
     *
     * @param color the {@link Color} of the treasure trail
     * @param card  the {@link PathCard} to check
     * @return {@code true} if the card can be added, {@code false} otherwise
     */
    @StudentImplementationRequired("P1.5")
    public boolean canAddCardToTreasureTrail(final Color color, final PathCard card) {
        // TODO: P1.5
        return org.tudalgo.algoutils.student.Student.crash("P1.5 - Remove if implemented");
    }

    /**
     * Adds a {@link PathCard} to the treasure trail for the given {@link Color} if
     * possible. Resets the cached {@link #treasurePositions} for that color upon
     * successful addition.
     *
     * @param color the {@link Color} of the treasure trail
     * @param card  the {@link PathCard} to add
     * @return {@code true} if the card was added, {@code false} otherwise
     */
    public boolean addCardToTreasureTrail(final Color color, final PathCard card) {
        if (!canAddCardToTreasureTrail(color, card)) {
            return false;
        }
        getTreasureTrail(color).add(card);
        treasurePositions.put(color, Set.of());
        return true;
    }

    /**
     * Clears the treasure trail for the given {@link Color} and resets the cached
     * {@link #treasurePositions} for that color.
     *
     * @param color the {@link Color} of the treasure trail to clear
     */
    public void clearTreasureTrail(final Color color) {
        getTreasureTrail(color).clear();
        treasurePositions.put(color, Set.of());
    }

    /**
     * Returns an unmodifiable view of the {@link #treasureTrails} map.
     *
     * @return an unmodifiable map of {@link Color} to list of {@link PathCard}s
     */
    public Map<Color, List<PathCard>> getTreasureTrails() {
        return Collections.unmodifiableMap(treasureTrails);
    }

    /**
     * Returns the treasure deck.
     *
     * @return the treasure deck
     */
    public Stack<TreasureCard> getTreasureDeck() {
        return treasureDeck;
    }

    /**
     * Draws a {@link TreasureCard} from the treasure deck. If the deck is empty, a
     * {@link GoldCard} with a random value is generated and returned instead.
     *
     * @return the drawn {@link TreasureCard}
     * @throws IllegalStateException if the deck is empty and a card cannot be
     *                               generated
     */
    public TreasureCard drawTreasureCard() throws IllegalStateException {
        if (treasureDeck.isEmpty()) {
            return GameState.generateGoldCards(1).getFirst();
        }
        return treasureDeck.pop();
    }

    /**
     * Adds the given {@link Player}s to this game state.
     * <p>
     * If adding the players would exceed {@link Config#MAX_PLAYERS}, no players
     * are added and the method returns false.
     *
     * @param players the {@link Player}s to add
     * @return true if the {@link Player}s were added successfully, false otherwise
     */
    public boolean addPlayers(final Set<Player> players) {
        if (players.size() + this.players.size() > Config.MAX_PLAYERS) {
            return false;
        }
        this.players.addAll(players);
        return true;
    }

    /**
     * Adds the given {@link Player} to this game state.
     *
     * @param player the {@link Player} to add
     * @return true if the {@link Player} was added successfully, false otherwise
     */
    public boolean addPlayer(final Player player) {
        return addPlayers(Set.of(player));
    }

    @Override
    public int hashCode() {
        return Objects.hash(grid, players);
    }

    @Override
    public String toString() {
        return "GameState[" + "grid=" + grid + ", " + "players=" + players + ", " + "winnerProperty=" + winnerProperty
                + ", " + ']';
    }
}
