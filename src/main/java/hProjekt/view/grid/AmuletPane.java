package hProjekt.view.grid;

import hProjekt.view.utils.IconView;
import hProjekt.view.utils.Images;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A Pane that displays an amulet icon on a circular base.
 */
public class AmuletPane extends StackPane {
    /**
     * Creates a new AmuletPane with a default size of 30.
     */
    public AmuletPane() {
        this(30);
    }

    /**
     * Creates a new AmuletPane with the given size.
     *
     * @param size size of the amulet
     */
    public AmuletPane(final double size) {
        final Circle base = new Circle(size * 0.85, Color.web("#E8601C"));
        base.setStroke(Color.BLACK);
        base.setStrokeWidth(size / 10);
        final IconView amuletIcon = new IconView(Images.AMULET_ICON, size);
        getChildren().addAll(base, amuletIcon);
    }
}
