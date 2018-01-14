package i5b5.daniel.serszen.pz.view.scenes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public abstract class AbstractCatalogScene extends AbstractCustomScene{
    protected Text loggedUser = new Text();
    protected BooleanProperty isAdminScene = new SimpleBooleanProperty();
    protected final GridPane rootPane = new GridPane();
    protected MenuBar menuBar;

    public AbstractCatalogScene(Parent root) {
        super(root);
        initMenuBar();
    }

    private void initMenuBar(){
        menuBar = new MenuBar();
        Menu menuFile = new Menu("Plik");
        Menu menuEdit = new Menu("Edytuj");
        Menu menuLogout = new Menu("Wyloguj");

        menuBar.getMenus().addAll(menuFile,menuEdit,menuLogout);

        rootPane.add(menuBar,0,0);
    }


    public BooleanProperty isAdminScene() {
        return isAdminScene;
    }

    public void setAdminScene(BooleanProperty adminScene) {
        isAdminScene = adminScene;
    }

    public GridPane getRootPane() {
        return rootPane;
    }

    public Text getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Text loggedUser) {
        this.loggedUser = loggedUser;
    }
}
