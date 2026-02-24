package hProjekt.model.cards;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import hProjekt.model.Player;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;

/**
 * Represents a card to find tiles that are in a specific area of a specific
 * tile type.
 */
public class InAreaCard extends PathCard {
    /**
     * Constructs a new {@code InAreaCard} for the specified {@link Player} and
     * filter type.
     * <p>
     * This constructor initializes the card by filtering tiles based on the
     * provided {@code filterType}. It delegates the initialization to the
     * superclass constructor using the filtered tiles.
     * </p>
     *
     * @param player     the {@link Player} who owns this card.
     * @param filterType the {@link Types} used to filter the tiles associated with
     *                   this card. Must be an instance of {@link Tile.Type}. Must
     *                   not be null.
     */
    protected InAreaCard(final Player player, @NotNull final Types filterType) {
        super(player, InAreaCard.filterTileByType(filterType), filterType);
    }

    /**
     * Creates a filter function that checks if a {@link Tile} matches a specific
     * type.
     *
     * <p>
     * This method generates a {@link Predicate} which accepts a {@link Tile} and
     * returns {@code true} if the tile's type equals the provided
     * {@code filterType}.
     * </p>
     *
     * @param filterType the specific {@link Types} to match against tiles. This
     *                   must be an instance of {@link Tile.Type}.
     * @return a predicate that evaluates to {@code true} if a given tile matches
     *         the filter type, and {@code false} otherwise.
     * @throws IllegalArgumentException if the provided {@code filterType} is not an
     *                                  instance of {@link Tile.Type}.
     */
    private static Predicate<Tile> filterTileByType(final Types filterType) {
        if (!(filterType instanceof Tile.Type)) {
            throw new IllegalArgumentException("filterType must be an instance of Tile.Type");
        }
        return (tile) -> tile.getType().equals(filterType);
    }
}
