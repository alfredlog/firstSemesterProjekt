package hProjekt.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import hProjekt.controller.PlayerObjective;
import hProjekt.model.cards.PathCard;
import hProjekt.model.cards.TreasureCard;
import hProjekt.model.grid.Tile;
import javafx.scene.paint.Color;

/**
 * This record is used to transmit general information about a player between
 * the controller and the GUI.
 *
 * @param playerObjective    The {@link PlayerObjective} assigned to the player.
 * @param drawnTreasureCards The list of {@link TreasureCard}s currently drawn
 *                           by the player.
 * @param drivableTiles      The set of {@link Tile}s the player can currently
 *                           drive to.
 * @param offeredCard        The {@link TreasureCard} currently offered to the
 *                           player.
 * @param validPathCards     A map of {@link PathCard}s that can be played for
 *                           each treasure trail.
 */
@DoNotTouch
public record PlayerState(
        PlayerObjective playerObjective,
        List<TreasureCard> drawnTreasureCards,
        Set<Tile> drivableTiles,
        TreasureCard offeredCard,
        Map<Color, List<PathCard>> validPathCards,
        Set<Color> collectableTreasures) {}
