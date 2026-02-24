package hProjekt.view.menus;

import hProjekt.view.utils.Images;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;

/**
 * Builder for the About screen.
 */
public class AboutBuilder implements Builder<Region> {

    private final Runnable loadMainMenuAction;

    /**
     * Constructor for the AboutBuilder.
     *
     * @param loadMainMenuAction the action to load the main menu
     */
    public AboutBuilder(final Runnable loadMainMenuAction) {
        this.loadMainMenuAction = loadMainMenuAction;
    }

    @Override
    public VBox build() {
        final VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Logo
        final ImageView logo = new ImageView(Images.TITLE_IMAGE);
        logo.setFitWidth(300);
        logo.setPreserveRatio(true);

        final Text gameDescription = new Text(
                "This years FOP-Projekt is based on the board game 'Tobago' by Bruce Allen.");
        gameDescription.getStyleClass().add("text-description");
        gameDescription.setFill(Color.WHITE);

        // Developer Information
        final Text developerInfo = new Text(
                "Developed by Technical University of Darmstadt, Department of Computer Science.");
        developerInfo.getStyleClass().add("text-subheading");
        developerInfo.setFill(Color.WHITE);

        // License Information
        final Text licenseInfo = new Text("""
                Built using JavaFX.
                Icons from:
                    - https://github.com/Templarian/MaterialDesign
                    - Amulet Icon Created by wahyu fadil from the Noun Project
                    - Beach Icon Created by Anggara Putra from the Noun Project
                    - Ocean/Wave Icon Created by Muhammad Nur Auliady Pamungkas from the Noun Project
                    - River Icon Created by Gulalicon from the Noun Project
                    - Mountain Icon Created by Puspa Kusuma from the Noun Project
                    - Lake Icon Created by Pong Pong from the Noun Project
                    - Jungle Icon Created by Eucalyp from the Noun Project
                    - Grass Icon Created by Deemak Daksina from the Noun Project
                    - Statue Icon Created by metami septiana from the Noun Project
                    - Palm Icon Created by Yeep from the Noun Project
                    - Hut Icon Created by Fabien from the Noun Project
                    - Move Icon Created by MihiMihi from the Noun Project
                    - Eraser Icon Created by MihiMihi from the Noun Project
                    - Paint Brush Icon Created by MihiMihi from the Noun Project
                All Icons from the Noun Project are licensed under CC BY 3.0
                """);
        licenseInfo.getStyleClass().add("text-license");
        licenseInfo.setFill(Color.WHITE);

        // Back to Main Menu Button
        final Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> loadMainMenuAction.run());
        backButton.getStyleClass().add("button");
        root.getChildren().addAll(logo, gameDescription, developerInfo, licenseInfo, backButton);

        // Add css style
        root.getStylesheets().add(getClass().getResource("/css/about.css").toExternalForm());

        return root;
    }
}
