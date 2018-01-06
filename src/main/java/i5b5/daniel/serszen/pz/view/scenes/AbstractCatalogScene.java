package i5b5.daniel.serszen.pz.view.scenes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;

public abstract class AbstractCatalogScene extends AbstractCustomScene{
    protected StringProperty userName = new SimpleStringProperty();
    protected BooleanProperty isAdminScene = new SimpleBooleanProperty();


    public AbstractCatalogScene(Parent root) {
        super(root);
    }

    public StringProperty getUserName() {
        return userName;
    }

    public void setUserName(StringProperty userName) {
        this.userName = userName;
    }

    public BooleanProperty isAdminScene() {
        return isAdminScene;
    }

    public void setAdminScene(BooleanProperty adminScene) {
        isAdminScene = adminScene;
    }
}
