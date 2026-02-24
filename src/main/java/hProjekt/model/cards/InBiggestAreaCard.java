package hProjekt.model.cards;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import hProjekt.model.Player;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;

/**
 * Represents a card to find tiles that are in the biggest area of a specific
 * tile type.
 */
public class InBiggestAreaCard extends PathCard {
    /**
     * Constructs a new {@code InBiggestAreaCard}.
     * <p>
     * This constructor initializes the card with the specified {@link Player} and
     * filters the target tiles based on the largest connected area of the given
     * type.
     * </p>
     *
     * @param player     the {@link Player} who owns or plays this card
     * @param filterType the {@link Types} used to filter the tiles associated with
     *                   this card. Must be an instance of {@link Tile.Type}. Must
     *                   not be null.
     */
    protected InBiggestAreaCard(final Player player, @NotNull final Types filterType) {
        super(player, InBiggestAreaCard.filterTileByBiggestArea(filterType), filterType);
    }

    /**
     * Creates a filtering predicate that determines if a given {@link Tile} is part
     * of the largest area of a specific {@link Tile.Type}.
     * <p>
     * This method is used to generate a {@link Predicate} for tiles based on
     * whether they belong to the biggest contiguous area of the specified
     * {@link Tile.Type} on the board.
     *
     * @param filterType The terrain type to check for connection to the largest
     *                   area. Must be an instance of {@link Tile.Type}.
     * @return A {@link Predicate} that takes a {@link Tile} and returns
     *         {@code true} if the tile is part of the biggest area of the specified
     *         type, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code filterType} is not an instance of
     *                                  {@link Tile.Type}.
     */
    private static Predicate<Tile> filterTileByBiggestArea(final Types filterType) {
        if (!(filterType instanceof Tile.Type)) {
            throw new IllegalArgumentException("filterType must be an instance of Tile.Type");
        }
        return (tile) -> tile.inBiggestArea((Tile.Type) filterType);
    }
}
