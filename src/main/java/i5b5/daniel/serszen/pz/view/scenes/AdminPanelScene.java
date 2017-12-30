package i5b5.daniel.serszen.pz.view.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class AdminPanelScene extends Scene{
    private static AdminPanelScene adminPanelScene;
    private String username;

    private AdminPanelScene(Parent root, double width, double height, String username) {
        super(root, width, height);
        this.username=username;
    }

    public static AdminPanelScene getAdminPanelScene(double width, double height, String username) {
        if (adminPanelScene != null) {
            return adminPanelScene;
        } else {
            init(width, height, username);
            return adminPanelScene;
        }
    }

    private static void init(double width, double height, String username){
        GridPane gridPane = new GridPane();


        adminPanelScene = new AdminPanelScene(gridPane,width,height, username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
