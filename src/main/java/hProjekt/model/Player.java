package hProjekt.model;

import java.util.List;
import java.util.Set;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.Config;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.PathCard;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.TilePosition;
import javafx.scene.paint.Color;

/**
 * Represents a player in the game.
 * Players have a name, a color, a number of credits, and a set of rails.
 */
@DoNotTouch
public interface Player {

    /**
     * Returns the hexGrid instance
     *
     * @return the hexGrid instance
     */
    HexGrid getHexGrid();

    /**
     * Returns the position of the player on the hex grid.
     *
     * @return the position of the player on the hex grid
     */
    TilePosition getPosition();

    /**
     * Sets the position of the player on the hex grid.
     *
     * @param position the new position of the player
     */
    void setPosition(TilePosition position);

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    String getName();

    /**
     * Returns the Player ID, aka the Index of the Player, starting with 1
     *
     * @return the Player ID
     */
    int getID();

    /**
     * Returns the color of the player.
     *
     * @return the color of the player
     */
    Color getColor();

    /**
     * Returns true if the player is an AI, false otherwise.
     * <p>
     * The player is considered an AI if it has an associated AI controller.
     *
     * @return true if the player is an AI, false otherwise
     */
    default boolean isAi() {
        return false;
    }

    /**
     * Returns the ai controller for the player or null if the player is not an ai.
     *
     * @return the ai controller for the player or null if the player is not an ai
     */
    Config.AvailableAiControllers getAiController();

    /**
     * Returns the set of path cards owned by the player.
     *
     * @return a set of {@link PathCard} objects representing the player's path
     *         cards
     */
    Set<PathCard> getPathCards();

    /**
     * Returns the list of gold cards owned by the player.
     *
     * @return a list of {@link GoldCard} objects representing the player's gold
     *         cards
     */
    List<GoldCard> getGoldCards();

    /**
     * Removes the specified amount of amulets from the player's collection.
     * The removal will fail if the amount is negative or if the player doesn't
     * have enough amulets.
     *
     * @param amount the number of amulets to remove (must be non-negative)
     * @return true if the amulets were successfully removed, false otherwise
     */
    boolean removeAmulets(int amount);

    /**
     * Adds the specified amount of amulets to the player's collection.
     *
     * @param amount the number of amulets to add
     */
    void addAmulets(int amount);

    /**
     * Returns the current number of amulets owned by the player.
     *
     * @return the number of amulets the player currently has
     */
    int getAmulets();

    /**
     * Calculates and returns the total value of all gold cards owned by the player.
     * The total is computed by summing the individual values of all gold cards.
     *
     * @return the total value of all gold cards owned by the player
     */
    int getTotalGoldCardValue();
}
