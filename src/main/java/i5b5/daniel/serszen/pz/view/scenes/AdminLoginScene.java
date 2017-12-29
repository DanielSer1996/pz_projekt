package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.UtilController;
import i5b5.daniel.serszen.pz.model.exceptions.UserNotFoundException;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.view.App;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


public class AdminLoginScene extends Scene {
    private UtilController utilController;
    private static AdminLoginScene adminLoginScene = null;

    private AdminLoginScene(Parent root, double width, double height) {
        super(root, width, height);
        utilController = BeanFactory.getUtilController();
    }

    private static void init(double width, double height) {
        Text loginText = new Text("Login");

        Text passwordText = new Text("Hasło");

        TextField loginTextField = new TextField();

        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Loguj");
        Button clearButton = new Button("Wyczyść");
        Button backButton = new Button("Cofnij");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(width, height);

        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.setAlignment(Pos.CENTER);

        loginButton.setOnAction(event -> {
            loginButtonFire(loginTextField.getText(),passwordField.getText());
        });

        clearButton.setOnAction(event -> {
            loginTextField.clear();
            passwordField.clear();
        });

        backButton.setOnAction(event -> {
            App.changeScene(StartingScene.getStartingScene(200, 200));
        });

        gridPane.add(loginText, 0, 0);
        gridPane.add(loginTextField, 1, 0);
        gridPane.add(passwordText, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 0, 2);
        gridPane.add(clearButton, 1, 2);
        gridPane.add(backButton, 0, 3);

        loginButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
        clearButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
        backButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");

        loginText.setStyle("-fx-font: normal bold 20px 'serif' ");
        passwordText.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridPane.setStyle("-fx-background-color: #436994;");

        adminLoginScene = new AdminLoginScene(gridPane, width, height);
    }

    public static AdminLoginScene getAdminLoginScene(double width, double height) {
        if (adminLoginScene != null) {
            return adminLoginScene;
        } else {
            init(width, height);
            return adminLoginScene;
        }
    }

    private static void loginButtonFire(String login, String password){
        try {
            boolean credentialsCheck = adminLoginScene.getUtilController().checkLoginData(login, password);
            if (!credentialsCheck) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Nieprawidłowe hasło",
                        ButtonType.OK);
                alert.showAndWait();
            }
        } catch (UserNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Admin o podanym loginie nie istnieje",
                    ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Hasło nie może być puste",
                    ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Błąd podczas sprawdzania danych logowania",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public UtilController getUtilController() {
        return utilController;
    }
}
