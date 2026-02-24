package hProjekt;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import hProjekt.controller.AiControllerFactory;
import hProjekt.controller.BasicAiController;

public class Config {
    /**
     * The global source of randomness.
     */
    public static final Random RANDOM = new Random();

    /**
     * The number of gold cards that are put on top of the lower pile. These will
     * not contain curse cards.
     */
    public static final int TOP_PILE_GOLD_CARDS = 39;

    /**
     * The total number of curse cards that are shuffled into the draw pile.
     */
    public static final int TOTAL_CURSE_CARDS = 2;

    /**
     * The number of gold cards at the bottom of the pile into which the curse cards
     * are shuffled.
     */
    public static final int LOWER_PILE_GOLD_CARDS = 27;

    /**
     * The maximum value of a gold card.
     */
    public static final int MAX_GOLD_CARD_VALUE = 6;

    /**
     * The minimum required number of players in a game.
     */
    public static final int MIN_PLAYERS = 2;

    /**
     * The maximum allowed number of players in a game.
     */
    public static final int MAX_PLAYERS = 6;

    /**
     * The maximum partial routes a player can drive per round.
     */
    public static final int DRIVE_LIMIT = 3;

    /**
     * The size of a tile.
     */
    public static final double TILE_SIZE = 60;

    /**
     * The width of a tile.
     */
    public static final double TILE_WIDTH = Math.sqrt(3) * Config.TILE_SIZE;

    /**
     * The height of a tile.
     */
    public static final double TILE_HEIGHT = 2 * Config.TILE_SIZE;

    /**
     * Represents the available AI controllers in the game.
     * Each AI controller has a factory to create instances of itself.
     *
     * @see AiControllerFactory
     */
    public enum AvailableAiControllers {
        DEFAULT(BasicAiController::new);

        /**
         * The factory to create instances of the AI controller.
         */
        public final AiControllerFactory factory;

        /**
         * Constructor for AvailableAiControllers.
         *
         * @param factory the factory to create AI controller instances
         */
        AvailableAiControllers(final AiControllerFactory factory) {
            this.factory = factory;
        }
    }

    /**
     * Defines the maximum number of cards a player can hold in their hand at any
     * given time during the game.
     */
    public static final int MAX_CARDS_IN_HAND = 4;

    /**
     * The maximum number of cards a player can hold in their hand when the game is
     * played with two players.
     */
    public static final int MAX_CARDS_IN_HAND_WITH_TWO_PLAYERS = 6;

    /**
     * The path where the leaderboard CSV file is stored.
     */
    public static final Path CSV_PATH = Paths.get("src/main/resources/leaderboard.csv");
}
