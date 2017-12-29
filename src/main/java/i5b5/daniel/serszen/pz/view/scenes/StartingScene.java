package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.view.App;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class StartingScene extends Scene {
    private static StartingScene startingScene = null;


    private StartingScene(Parent root, double width, double height) {
        super(root, width, height);
    }

    private static void init(double width, double height) {
        Pane root = new Pane();

        root.setStyle("-fx-background-color: #436994;");

        Text startingSceneLabel = new Text("Wybierz co chcesz zrobić");
        startingSceneLabel.setX(15);
        startingSceneLabel.setY(20);
        startingSceneLabel.setStyle("-fx-font: normal bold 12px 'serif';");

        Button adminButton = new Button("Zaloguj jako admin");
        adminButton.setWrapText(true);
        adminButton.setMaxWidth(100);
        adminButton.setLayoutX(width / 2 - 50);
        adminButton.setLayoutY(height / 2 - 50);
        adminButton.setStyle("-fx-background-color: #ffcc66; " +
                "-fx-text-fill: black;" +
                "-fx-font: normal bold 10px 'serif'");
        adminButton.setOnAction(event -> {
            App.changeScene(AdminLoginScene.getAdminLoginScene(400,400));
        });

        Button observerButton = new Button("Przeglądaj katalog");
        observerButton.setWrapText(true);
        observerButton.setMaxWidth(100);
        observerButton.setLayoutX(width / 2 - 50);
        observerButton.setLayoutY(height / 2);
        observerButton.setStyle("-fx-background-color: #ffcc66; " +
                "-fx-text-fill: black;" +
                "-fx-font: normal bold 10px 'serif'");

        root.getChildren().add(startingSceneLabel);
        root.getChildren().add(adminButton);
        root.getChildren().add(observerButton);

        startingScene = new StartingScene(root, width, height);
    }

    public static StartingScene getStartingScene(double width, double height) {
        if (startingScene != null) {
            return startingScene;
        } else {
            init(width, height);
            return startingScene;
        }
    }
}
