package hProjekt.view.overlays;

import java.util.ArrayList;
import java.util.List;

import hProjekt.model.cards.PathCard;
import hProjekt.view.cards.PathCardPane;
import javafx.beans.property.Property;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * An overlay that allows viewing different treasure trails.
 * <p>
 * This component displays a set of colored buttons representing different
 * trails.
 * Selecting a button displays the sequence of {@link PathCard}s associated with
 * that color.
 * It observes changes in the map of treasure trails and updates the options
 * accordingly.
 * </p>
 */
public class TreasureTrailOverlay extends HBox {
    /**
     * Constructs a new {@code TreasureTrailOverlay}.
     *
     * @param treasureTrails an observable map where the key is a {@link Color}
     *                       identifying the trail
     *                       and the value is a list of {@link PathCard}s in that
     *                       trail
     * @param selectedTrail  a property that holds the currently selected trail
     *                       color; updated when the user selects a trail
     */
    public TreasureTrailOverlay(final ObservableMap<Color, List<PathCard>> treasureTrails,
            final Property<Color> selectedTrail) {
        getStylesheets().add("css/main.css");
        getStyleClass().add("box");
        setSpacing(10);

        final VBox buttonBox = new VBox(10);
        final ScrollPane pathCardContainer = new ScrollPane();
        pathCardContainer.setPrefHeight((PathCardPane.cardSize + 10) * 4);
        pathCardContainer.setPrefWidth(PathCardPane.cardSize * 1.5 + 20);
        pathCardContainer.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        pathCardContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        final VBox pathCardBox = new VBox(10);
        pathCardContainer.setContent(pathCardBox);

        final ToggleGroup treasureTrailsToggleGroup = new ToggleGroup();
        treasureTrailsToggleGroup.selectedToggleProperty()
                .subscribe(toggle -> {
                    if (toggle == null) {
                        pathCardBox.getChildren().clear();
                        selectedTrail.setValue(null);
                        return;
                    }
                    selectedTrail.setValue((Color) toggle.getUserData());
                    TreasureTrailOverlay.createPathCards(pathCardBox, treasureTrails.get(toggle.getUserData()));
                });

        treasureTrails
                .addListener((final MapChangeListener.Change<? extends Color, ? extends List<PathCard>> trails) -> {
                    if (treasureTrailsToggleGroup.getSelectedToggle() != null) {
                        TreasureTrailOverlay.createPathCards(pathCardBox,
                                treasureTrails.get(treasureTrailsToggleGroup.getSelectedToggle().getUserData()));
                    } else {
                        pathCardBox.getChildren().clear();
                    }
                });

        final List<ToggleButton> treasureTrailToggleButtons = new ArrayList<>();
        for (final Color color : treasureTrails.keySet()) {
            final ToggleButton treasureTrailButton = new ToggleButton();
            treasureTrailButton.setGraphic(new Circle(10, color));
            treasureTrailButton.setToggleGroup(treasureTrailsToggleGroup);
            treasureTrailButton.setUserData(color);
            treasureTrailToggleButtons.add(treasureTrailButton);
            buttonBox.getChildren().add(treasureTrailButton);
        }

        treasureTrailToggleButtons.getFirst().setSelected(true);
        getChildren().addAll(buttonBox, pathCardContainer);
    }

    /**
     * Creates and displays the path cards for the selected trail.
     * <p>
     * This method clears the current content of the provided container and
     * populates it with {@link PathCardPane}s representing the path cards in the
     * specified list.
     *
     * @param container the pane where the path card panes will be added
     * @param pathCards the list of {@link PathCard}s to display
     */
    private static void createPathCards(final Pane container, final List<PathCard> pathCards) {
        container.getChildren().clear();
        if (pathCards == null) {
            return;
        }
        for (final PathCard pathCard : pathCards) {
            container.getChildren().add(new PathCardPane(pathCard));
        }
    }
}
