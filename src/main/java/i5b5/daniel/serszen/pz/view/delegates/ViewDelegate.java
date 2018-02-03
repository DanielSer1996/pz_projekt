package i5b5.daniel.serszen.pz.view.delegates;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.scenes.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ViewDelegate {
    private static ViewDelegate viewDelegate;

    private final Logger logger = LogManager.getLogger(ViewDelegate.class);

    private Map<String, AbstractCustomScene> scenes = new HashMap<>();
    private final String SCENES_PACKAGE = "i5b5.daniel.serszen.pz.view.scenes.";
    private final Stage stage;
    private String skinFile;
    private String language;

    private ViewDelegate(Stage stage) {
        this.stage = stage;
        try {
            Properties prop = getProperties();
            skinFile = "css/" + prop.getProperty("startingSkin")+"_skin.css";
            language = prop.getProperty("startingLang");
        } catch (Throwable e) {
            logger.error(e);
            skinFile = "css/bright_skin.css";
            language = "pl";
        }
    }

    private Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("properties/app.properties"));

        return properties;
    }

    public void changeScene(AbstractCustomScene scene, Object param) {
        if(scene instanceof AbstractCatalogScene){
            AbstractCatalogScene acs = (AbstractCatalogScene) scene;
            acs.refreshTimerText();
        }

        if (scene instanceof AdminCatalogSceneCar && param != null) {
            String login = (String) param;
            AdminCatalogSceneCar scene1 = (AdminCatalogSceneCar) scene;
            scenes.remove(AdminLoginScene.class.getSimpleName());
            scene1.getLoggedUser().setText("Admin: " + login);
            scene1.setIsAdminScene(true);
            stage.setScene(scene1);
        } else if (scene instanceof CatalogCarPartScene && param != null) {
            Car car = (Car) param;
            CatalogCarPartScene scene1 = (CatalogCarPartScene) scene;
            scene1.setChosenCar(car);
            scene1.initAllCarParts();
            stage.setScene(scene1);
        } else {
            stage.setScene(scene);
        }
        stage.setWidth(scene.getWidthDim());
        stage.setHeight(scene.getHeightDim());
    }

    public void changeAppSkin(AbstractCustomScene scene) {
        if (!scene.getStylesheets().isEmpty()) {
            scene.getStylesheets().remove(0);
        }
        scene.getStylesheets().add(getClass().getClassLoader().getResource(this.skinFile).toExternalForm());
        for (Map.Entry<String, AbstractCustomScene> entry : scenes.entrySet()) {
            if (entry.getValue() instanceof AbstractCatalogScene) {
                if (!entry.getValue().getStylesheets().isEmpty()) {
                    entry.getValue().getStylesheets().remove(0);
                }
                entry.getValue().getStylesheets().add(getClass().getClassLoader().getResource(this.skinFile).toExternalForm());
            }
        }
    }

    public AbstractCustomScene chooseSceneByName(String name) {
        if (scenes.containsKey(name)) {
            return scenes.get(name);
        } else {
            try {
                Class clazz = Class.forName(SCENES_PACKAGE + name);
                AbstractCustomScene scene = (AbstractCustomScene) clazz.newInstance();
                scenes.put(name, scene);
                changeLanguage();
                return scene;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                logger.error("error", e);
            }
        }
        return null;
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (AbstractCustomScene.class.isAssignableFrom(type.getSuperclass())) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public void changeLanguage() {
        ResourceBundle bundle = ResourceBundle.getBundle("languages/AppMessages", new Locale(language, language.toUpperCase()));
        getStage().setTitle(bundle.getString("mainTitle"));
        List<Field> f = new ArrayList<>();
        for (Map.Entry<String, AbstractCustomScene> entry : scenes.entrySet()) {
            f.clear();
            f = getAllFields(f, entry.getValue().getClass());
            for (Field field : f) {
                setFieldLang(field, entry.getValue(), bundle);
            }
        }
    }

    public String getMessageForAlert(String name){
        ResourceBundle bundle = ResourceBundle.getBundle("languages/AppMessages", new Locale(language, language.toUpperCase()));

        return bundle.getString(name);
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

    private void setFieldLang(Field field, Scene scene, ResourceBundle bundle) {
        try {
            field.setAccessible(true);
            if (Button.class.equals(field.getType())) {
                Button b = (Button) field.get(scene);
                b.setText(bundle.getString(field.getName()));
            } else if (Text.class.equals(field.getType()) && !"loggedUser".equals(field.getName())) {
                Text t = (Text) field.get(scene);
                t.setText(bundle.getString(field.getName()));
            } else if (Label.class.equals(field.getType())) {
                Label l = (Label) field.get(scene);
                l.setText(bundle.getString(field.getName()));
            } else if (TableColumn.class.equals(field.getType())) {
                TableColumn tc = (TableColumn) field.get(scene);
                if (scene instanceof CatalogCarScene) {
                    tc.setText((bundle.getString(field.getName() + "Car")));
                    tc.getTableView().setPlaceholder(new Label(bundle.getString(field.getName() + "PlaceholderCar")));
                } else if (scene instanceof CatalogCarPartScene) {
                    tc.setText(bundle.getString(field.getName() + "CarPart"));
                    tc.getTableView().setPlaceholder(new Label(bundle.getString(field.getName() + "PlaceholderCarPart")));
                }
            } else if (Menu.class.equals(field.getType())) {
                Menu menu = (Menu) field.get(scene);
                menu.setText(bundle.getString(field.getName()));
            } else if (MenuItem.class.equals(field.getType())) {
                MenuItem menuItem = (MenuItem) field.get(scene);
                menuItem.setText(bundle.getString(field.getName()));
            }
        } catch (IllegalAccessException e) {
            logger.error("Error", e);
        }
    }

    public static ViewDelegate getInstance() {
        if (viewDelegate == null) {
            throw new IllegalStateException("You have to instantiate ViewDelegate with javafx Stage parameter");
        }
        return viewDelegate;
    }

    public Stage getStage() {
        return stage;
    }

    public String getSkinFile() {
        return skinFile;
    }

    public void setSkinFile(String skinFile) {
        this.skinFile = "css/" + skinFile + ".css";
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
