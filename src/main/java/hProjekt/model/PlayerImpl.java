package hProjekt.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import hProjekt.Config;
import hProjekt.model.cards.GoldCard;
import hProjekt.model.cards.PathCard;
import hProjekt.model.grid.HexGrid;
import hProjekt.model.grid.TilePosition;
import javafx.scene.paint.Color;

/**
 * Default implementation of {@link Player}.
 */
public class PlayerImpl implements Player {
    private final HexGrid hexGrid;
    private final String name;
    private final int id;
    private final Color color;

    private TilePosition position;

    private final Config.AvailableAiControllers aiController;
    private int amulets;

    private final Set<PathCard> pathCards = new HashSet<>();
    private final List<GoldCard> goldCards = new ArrayList<>();

    @DoNotTouch("Please don't create a public Contructor, use the Builder instead.")
    private PlayerImpl(final HexGrid hexGrid, final Color color, final int id, final String name,
            final Config.AvailableAiControllers ai) {
        this.hexGrid = hexGrid;
        this.color = color;
        this.id = id;
        this.name = name;
        aiController = ai;
        amulets = 0;
    }

    @Override
    @StudentImplementationRequired("P1.1")
    public HexGrid getHexGrid() {
        // TODO: P1.1
        return org.tudalgo.algoutils.student.Student.crash("P1.1 - Remove if implemented");
    }

    @Override
    public TilePosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(final TilePosition position) {
        this.position = position;
    }

    @Override
    @StudentImplementationRequired("P1.1")
    public String getName() {
        // TODO: P1.1
        return org.tudalgo.algoutils.student.Student.crash("P1.1 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.1")
    public int getID() {
        // TODO: P1.1
        return org.tudalgo.algoutils.student.Student.crash("P1.1 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.1")
    public Color getColor() {
        // TODO: P1.1
        return org.tudalgo.algoutils.student.Student.crash("P1.1 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.1")
    public boolean isAi() {
        // TODO: P1.1
        return org.tudalgo.algoutils.student.Student.crash("P1.1 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.2")
    public int getAmulets() {
        // TODO: P1.2
        return org.tudalgo.algoutils.student.Student.crash("P1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.2")
    public void addAmulets(final int amount) {
        // TODO: P1.2
        org.tudalgo.algoutils.student.Student.crash("P1.2 - Remove if implemented");
    }

    @Override
    @StudentImplementationRequired("P1.2")
    public boolean removeAmulets(final int amount) {
        // TODO: P1.2
        return org.tudalgo.algoutils.student.Student.crash("P1.2 - Remove if implemented");
    }

    @Override
    public String toString() {
        return "PlayerImpl[" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", color=" + color +
                ", aiController=" + aiController +
                ", amulets=" + amulets +
                ", position=" + position +
                ", pathCards=" + pathCards +
                ", goldCards=" + goldCards +
                ']';
    }

    @Override
    public Config.AvailableAiControllers getAiController() {
        return aiController;
    }

    @Override
    public Set<PathCard> getPathCards() {
        return pathCards;
    }

    @Override
    public List<GoldCard> getGoldCards() {
        return goldCards;
    }

    @Override
    public int getTotalGoldCardValue() {
        return goldCards.stream().mapToInt(GoldCard::value).sum();
    }

    /**
     * Builder for {@link PlayerImpl}.
     * Allows to create a new player and modify its properties before building it.
     */
    @DoNotTouch
    public static class Builder {
        private int id;
        private Color color;
        private @Nullable String name;
        private @Nullable Config.AvailableAiControllers aiController;

        /**
         * Creates a new builder for a player with the given id.
         *
         * @param id the id of the player to create
         */
        public Builder(final int id) {
            this.id = id;
            color(null);
        }

        /**
         * Returns the color of the player.
         *
         * @return the color of the player
         */
        public Color getColor() {
            return color;
        }

        /**
         * Sets the color of the player.
         *
         * @param playerColor the color of the player
         * @return this builder
         */
        public Builder color(final Color playerColor) {
            color = playerColor == null
                    ? new Color(
                            Config.RANDOM.nextDouble(),
                            Config.RANDOM.nextDouble(),
                            Config.RANDOM.nextDouble(),
                            1)
                    : playerColor;
            return this;
        }

        /**
         * Returns the name of the player.
         *
         * @return the name of the player
         */
        public @Nullable String getName() {
            return name;
        }

        /**
         * Sets the name of the player.
         *
         * @param playerName the name of the player
         * @return this builder
         */
        public Builder name(final @Nullable String playerName) {
            name = playerName;
            return this;
        }

        /**
         * Returns the name of the player or a default name if no name was set.
         * The default name is "Player" followed by the id of the player.
         *
         * @return the name of the player or a default name if no name was set
         */
        public String nameOrDefault() {
            return name == null ? String.format("Player%d", id) : name;
        }

        /**
         * Sets the id of the player.
         *
         * @param newId the id of the player
         * @return this builder
         */
        public Builder id(final int newId) {
            id = newId;
            return this;
        }

        /**
         * Returns the id of the player.
         *
         * @return the id of the player
         */
        public int getId() {
            return id;
        }

        /**
         * Returns whether the player is an AI.
         *
         * @return whether the player is an AI
         */
        public boolean isAi() {
            return aiController != null;
        }

        /**
         * Sets whether the player is an AI.
         *
         * @param ai the ai controller of the player
         * @return this builder
         */
        public Builder ai(final Config.AvailableAiControllers ai) {
            aiController = ai;
            return this;
        }

        /**
         * Builds the player with the properties set in this builder.
         *
         * @param grid the grid the player is on
         * @return the player with the properties set in this builder
         */
        public Player build(final HexGrid grid) {
            return new PlayerImpl(grid, color, id, nameOrDefault(), aiController);
        }
    }
}
