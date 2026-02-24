package hProjekt.model.cards;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.Player;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;

/**
 * Represents a card that is part of a treasure trail.
 * Each card filters the tiles based on the tileFilterFunction.
 * This way the the treasure trail can be constructed step by step and the tile
 * where the treasure is hidden can be found.
 */
public class PathCard {
    private final Player player;
    private final @Nullable Types filterType;
    private final Predicate<Tile> tileFilterFunction;
    private CardType type;

    /**
     * Constructs a PathCard with the given player, tile filter function, and
     * optional tile type.
     *
     * @param player     the player associated with this card
     * @param tileFilter the function to filter tiles
     * @param filterType the type of tile this card is associated with, can be null
     *                   (null is the ocean)
     */
    protected PathCard(final Player player, final Predicate<Tile> tileFilter, @Nullable final Types filterType) {
        this.player = player;
        tileFilterFunction = tileFilter;
        this.filterType = filterType;
    }

    /**
     * Sets the type of this card.
     * Should only be used by CardType enum.
     *
     * @param type the type to set
     */
    protected void setType(final CardType type) {
        this.type = type;
    }

    /**
     * Returns the type of this card.
     *
     * @return the type of this card
     */
    public CardType getType() {
        return type;
    }

    /**
     * Returns the player that placed this card.
     *
     * @return the player that placed this card
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Filters the given set of tiles based on the tile filter function.
     *
     * @param tiles the set of tiles to filter
     * @return a set of tiles that match the filter criteria
     */
    public Set<Tile> filter(final Set<Tile> tiles) {
        return tiles.stream()
                .filter(tileFilterFunction)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the tile filter function used by this card.
     *
     * @return the tile filter function
     */
    public Predicate<Tile> getTileFilterFunction() {
        return tileFilterFunction;
    }

    /**
     * Returns the tile type associated with this card, if any.
     *
     * @return the tile type, or null (null is the ocean) if not specified
     */
    @Nullable
    public Types getFilterType() {
        return filterType;
    }

    @Override
    public String toString() {
        return "PathCard[" +
                "cardType=" + type +
                ", tileType=" + filterType +
                ']';
    }
}
