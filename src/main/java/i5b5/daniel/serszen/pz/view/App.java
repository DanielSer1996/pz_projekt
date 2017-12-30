package i5b5.daniel.serszen.pz.view;

import i5b5.daniel.serszen.pz.view.scenes.StartingScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App  extends Application{
    private static Stage stage;

    private static final double SCREEN_WIDTH = Screen.getScreens().get(0).getBounds().getWidth();
    private static final double SCREEN_HEIGHT = Screen.getScreens().get(0).getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Katalog części");

        primaryStage.setResizable(false);
        primaryStage.setScene(StartingScene.getStartingScene(200,200));

        primaryStage.show();
    }

    public static void changeScene(Scene scene){
        stage.setScene(scene);
        stage.sizeToScene();
    }
}
