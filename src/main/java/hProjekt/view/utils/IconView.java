package hProjekt.view.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A view for displaying icons.
 * It ensures that the aspect ratio is preserved and fits the icon to the given
 * width.
 */
public class IconView extends ImageView {
    /**
     * Creates a new IconView with the icon at the given path and width.
     *
     * @param iconPath path to the icon
     * @param width    width of the icon
     */
    public IconView(final String iconPath, final double width) {
        this(new Image(iconPath), width);
    }

    /**
     * Creates a new IconView with the given icon and width.
     *
     * @param icon  icon to display
     * @param width width of the icon
     */
    public IconView(final Image icon, final double width) {
        super(icon);
        setPreserveRatio(true);
        setFitWidth(width);
    }
}
