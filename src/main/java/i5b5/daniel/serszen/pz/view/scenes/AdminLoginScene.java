package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.UtilController;
import i5b5.daniel.serszen.pz.model.exceptions.LoginException;
import i5b5.daniel.serszen.pz.model.exceptions.ResourceException;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.view.App;
import i5b5.daniel.serszen.pz.view.events.CustomEventHandler;
import i5b5.daniel.serszen.pz.view.events.LoginSuccessfulEvent;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;


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

        Circle circle = new Circle(12);
        circle.setStyle("-fx-fill: #9CBE83;" +
                "-fx-text-fill: black");

        Text questionMark = new Text("?");
        questionMark.setStyle("-fx-font: normal bold 12px 'serif'");
        questionMark.setBoundsType(TextBoundsType.VISUAL);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(circle);
        stackPane.getChildren().add(questionMark);

        circle.setOnMouseEntered(event -> {
            Tooltip tooltip = new Tooltip("Wielkość liter nie ma znaczenia");
            Tooltip.install(circle,tooltip);
        });

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

        passwordField.setOnKeyPressed(event -> {
            if(KeyCode.ENTER.equals(event.getCode()))
                loginButton.fire();
        });

        loginTextField.setOnKeyPressed(event -> {
            if(KeyCode.ENTER.equals(event.getCode()))
                loginButton.fire();
        });

        loginButton.setOnAction(event -> {
            loginButtonFire(loginTextField.getText().toLowerCase(),passwordField.getText(),loginButton);
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
        gridPane.add(stackPane,2,0);
        gridPane.add(passwordText, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 0, 2);
        gridPane.add(clearButton, 1, 2);
        gridPane.add(backButton, 0, 6);

        gridPane.addEventHandler(LoginSuccessfulEvent.LOGIN_SUCCESSFUL_BASE,
                new CustomEventHandler() {
                    @Override
                    public void onLoginSuccessful() {
                        App.changeScene(AdminPanelScene.getAdminPanelScene(500,500,loginText.getText()));
                    }
                });

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

    private static void loginButtonFire(String login, String password, Button source){
        try {
            boolean credentialsCheck = adminLoginScene.getUtilController().checkLoginData(login, password);
            if (!credentialsCheck) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Nieprawidłowe hasło",
                        ButtonType.OK);
                alert.showAndWait();
            }else {
                source.fireEvent(new LoginSuccessfulEvent(LoginSuccessfulEvent.LOGIN_SUCCESSFUL_BASE));
            }
        } catch (LoginException | ResourceException e){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public UtilController getUtilController() {
        return utilController;
    }
}
