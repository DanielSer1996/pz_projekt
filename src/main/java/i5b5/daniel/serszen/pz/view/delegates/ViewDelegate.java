package i5b5.daniel.serszen.pz.view.delegates;

import i5b5.daniel.serszen.pz.view.scenes.AbstractCatalogScene;
import i5b5.daniel.serszen.pz.view.scenes.AbstractCustomScene;
import i5b5.daniel.serszen.pz.view.scenes.AdminCarCatalogScene;
import i5b5.daniel.serszen.pz.view.scenes.AdminLoginScene;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ViewDelegate {
    private static ViewDelegate viewDelegate;

    private Map<String, AbstractCustomScene> scenes = new HashMap<>();
    private final String SCENES_PACKAGE = "i5b5.daniel.serszen.pz.view.scenes.";
    private final Stage stage;

    private ViewDelegate(Stage stage) {
        this.stage = stage;
    }

    public void changeScene(AbstractCustomScene scene, String login) {
        if (scene instanceof AdminCarCatalogScene && login != null) {
            AdminCarCatalogScene scene1 = (AdminCarCatalogScene) scene;
            scenes.remove(AdminLoginScene.class.getSimpleName());
            scene1.getLoggedUser().setText("Zalogowany admin: " + login);
            scene1.setIsAdminScene(true);
            stage.setScene(scene1);
        } else {
            stage.setScene(scene);
        }
        stage.setWidth(scene.getWidthDim());
        stage.setHeight(scene.getHeightDim());
    }

    public AbstractCustomScene chooseSceneByName(String name) {
        if (scenes.containsKey(name)) {
            return scenes.get(name);
        } else {
            try {
                Class clazz = Class.forName(SCENES_PACKAGE + name);
                AbstractCustomScene scene = (AbstractCustomScene) clazz.newInstance();
                scenes.put(name, scene);
                return scene;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, AbstractCustomScene> getScenes() {
        return scenes;
    }

    public static ViewDelegate getInstance(Stage stage) {
        if (viewDelegate == null) {
            viewDelegate = new ViewDelegate(stage);
            return viewDelegate;
        } else {
            return viewDelegate;
        }
    }

    public static ViewDelegate getInstance() {
        if (viewDelegate == null) {
            throw new NullPointerException("You have to instantiate ViewDelegate with javafx Stage parameter");
        }
        return viewDelegate;
    }

    public Stage getStage() {
        return stage;
    }
}
