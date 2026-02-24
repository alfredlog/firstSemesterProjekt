package hProjekt.model.cards;

import java.util.Optional;
import java.util.function.Predicate;

import hProjekt.model.Player;
import hProjekt.model.grid.Structure;
import hProjekt.model.grid.Tile;
import hProjekt.model.grid.Types;

/**
 * Represents a card to find tiles that are in sight (i.e. max. two tiles away)
 * of a specific tile/structure type.
 */
public class CanSeeCard extends PathCard {
    /**
     * Constructs a CanSeeCard with the given player and tile/structure type to
     * filter for.
     *
     * @param player     the player associated with this card
     * @param filterType the type of tile/structure this card is associated with,
     *                   can be null (null is the ocean)
     */
    protected CanSeeCard(final Player player, final Types filterType) {
        super(player,
                filterType instanceof Tile.Type || filterType == null ? (tile) -> !tile.getType().equals(filterType)
                        && tile.canSeeTileType(CanSeeCard.canSeeTileFilterFunction((Tile.Type) filterType))
                        : (tile) -> !(tile.getStructure() != null && tile.getStructure().getType().equals(filterType))
                                && tile.canSeeTileType(
                                        CanSeeCard.canSeeStructureFilterFunction((Structure.Type) filterType)),
                filterType);
    }

    /**
     * Helper function to determine if a neighbor tile matches the specified tile
     * type. If the tile type is null, it checks if the neighbor is not present aka
     * the ocean.
     *
     * @param tileType the type of tile to match against, can be null
     * @return a predicate that checks if a neighbor tile matches the specified type
     */
    private static Predicate<Optional<Tile>> canSeeTileFilterFunction(final Tile.Type tileType) {
        return (neighbor) -> {
            if (tileType == null) {
                return neighbor.isEmpty();
            } else {
                return neighbor.map(mappedNeighbor -> tileType.equals(mappedNeighbor.getType())).orElse(false);
            }
        };
    }

    /**
     * Helper function to determine if a neighbor tile has a structure matching the
     * specified structure type.
     *
     * @param structureType the type of structure to match against
     * @return a predicate that checks if a neighbor tile has a structure of the
     *         specified type
     */
    private static Predicate<Optional<Tile>> canSeeStructureFilterFunction(final Structure.Type structureType) {
        return neighbor -> neighbor.map(mappedNeighbor -> mappedNeighbor.getStructure() != null
                && mappedNeighbor.getStructure().getType().equals(structureType)).orElse(false);
    }
}
