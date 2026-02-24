package hProjekt.model.cards;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import hProjekt.model.Player;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;

/**
 * Represents a card to find tiles that are next to a specific tile type or
 * structure.
 */
public class NextToCard extends PathCard {
    /**
     * Constructs a NextToCard with the given player and tile/structure type to
     * filter for.
     *
     * @param player     the player associated with this card
     * @param filterType the type of tile/structure this card is associated with,
     *                   can be null (null is the ocean)
     */
    protected NextToCard(final Player player, @Nullable final Types filterType) {
        super(player,
                filterType == null || filterType instanceof Tile.Type ? NextToCard.filterByTile((Tile.Type) filterType)
                        : NextToCard.filterByStructure((Structure.Type) filterType),
                filterType);
    }

    /**
     * Creates a {@link Predicate} to filter {@link Tile}s based on their type and
     * their neighbors' types.
     * <p>
     * The returned {@link Predicate} checks if a {@link Tile}:
     * <ol>
     * <li>Is <b>not</b> of the specified {@code tileType}.</li>
     * <li>Is adjacent to a {@link Tile} that matches the specified
     * {@link Tile.Type}.</li>
     * </ol>
     * <p>
     * If {@code tileType} is {@code null}, the {@link Predicate} checks if the
     * {@link Tile} is next to an empty space (i.e. the ocean).
     *
     * @param tileType the type of {@link Tile} to look for in the neighborhood; if
     *                 {@code null}, looks for empty spaces (ocean).
     * @return a {@link Predicate} that returns {@code true} if the input
     *         {@link Tile} is not of {@code tileType} and is adjacent to a
     *         {@link Tile} of type {@code tileType}.
     */
    private static Predicate<Tile> filterByTile(final Tile.Type tileType) {
        return (tile) -> !tile.getType().equals(tileType) && tile.nextTo(neighbor -> tileType != null
                ? neighbor.map(mappedNeighbor -> tileType.equals(mappedNeighbor.getType())).orElse(false)
                : neighbor.isEmpty());
    }

    /**
     * Creates a {@link Predicate} that filters {@link Tile}s based on whether they
     * are adjacent to a specific {@link Structure.Type}.
     * <p>
     * The returned {@link Predicate} evaluates to {@code true} if the given
     * {@link Tile} has at least one neighbor containing a structure of the
     * specified {@link Structure.Type}. Neighbors that are empty or out of bounds
     * are ignored.
     * </p>
     *
     * @param structureType the {@link Structure.Type} to look for in adjacent
     *                      tiles.
     * @return a {@link Predicate} that accepts a {@link Tile} and returns
     *         {@code true} if an adjacent {@link Tile} contains the specified
     *         {@link Structure.Type}, {@code false} otherwise.
     */
    private static Predicate<Tile> filterByStructure(final Structure.Type structureType) {
        return (tile) -> tile.nextTo(neighbor -> neighbor.map(mappedNeighbor -> mappedNeighbor.getStructure() != null
                && mappedNeighbor.getStructure().getType().equals(structureType)).orElse(false));
    }
}
