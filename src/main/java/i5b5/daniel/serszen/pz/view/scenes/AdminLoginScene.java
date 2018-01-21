package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.UtilController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.events.LoginSuccessfulEvent;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;


public class AdminLoginScene extends AbstractCustomScene {
    private final Logger logger = LogManager.getLogger(AdminLoginScene.class);

    private GridPane gridPane;
    private UtilController utilController;
    private Text loginText;
    private Text passwordText;
    private StackPane loginTooltip;
    private TextField loginTextField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button clearButton;
    private Button backButton;
    private ViewDelegate viewDelegate;

    public AdminLoginScene() {
        this(new GridPane());
    }

    public AdminLoginScene(Parent root) {
        super(root);
        utilController = BeanFactory.getUtilController();
        viewDelegate = ViewDelegate.getInstance();
        initialize();
    }

    private void initialize() {
        this.widthDim = 500;
        this.heightDim = 500;

        configRootPane();
        initTexts();
        initTextFields();
        initButtons();
        initLoginTooltip();

        initRootPane();

        this.setRoot(gridPane);
    }

    private void initButtons() {
        loginButton = new Button("Loguj");
        clearButton = new Button("Wyczyść");
        backButton = new Button("Cofnij");

        loginButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
        clearButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
        backButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");

        loginButton.setOnAction(event -> {
            loginButtonFire(loginTextField.getText().toLowerCase(), passwordField.getText(), loginButton);
        });

        clearButton.setOnAction(event -> {
            loginTextField.clear();
            passwordField.clear();
            });

        backButton.setOnAction(event -> {
            viewDelegate.changeScene(viewDelegate.chooseSceneByName(StartingScene.class.getSimpleName()),null);
        });
    }

    private void initTextFields() {
        loginTextField = new TextField();
        passwordField = new PasswordField();

        passwordField.setOnKeyPressed(event -> {
            if (KeyCode.ENTER.equals(event.getCode()))
                loginButton.fire();
        });

        loginTextField.setOnKeyPressed(event -> {
            if (KeyCode.ENTER.equals(event.getCode()))
                loginButton.fire();
        });
    }

    private void initTexts() {
        loginText = new Text("Login");
        passwordText = new Text("Hasło");

        loginText.setStyle("-fx-font: normal bold 20px 'serif' ");
        passwordText.setStyle("-fx-font: normal bold 20px 'serif' ");
    }

    private void initLoginTooltip() {
        Circle circle = new Circle(12);
        circle.setStyle("-fx-fill: #9CBE83;" +
                "-fx-text-fill: black");

        Text questionMark = new Text("?");
        questionMark.setStyle("-fx-font: normal bold 12px 'serif'");
        questionMark.setBoundsType(TextBoundsType.VISUAL);
        loginTooltip = new StackPane();
        loginTooltip.getChildren().add(circle);
        loginTooltip.getChildren().add(questionMark);

        circle.setOnMouseEntered(event -> {
            Tooltip tooltip = new Tooltip("Wielkość liter nie ma znaczenia");
            Tooltip.install(loginTooltip, tooltip);
        });
    }

    private void configRootPane(){
        gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.setAlignment(Pos.CENTER);
    }

    @Override
    protected void initRootPane() {

        gridPane.add(loginText, 0, 0);
        gridPane.add(loginTextField, 1, 0);
        gridPane.add(loginTooltip, 2, 0);
        gridPane.add(passwordText, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 0, 2);
        gridPane.add(clearButton, 1, 2);
        gridPane.add(backButton, 0, 6);


        gridPane.addEventHandler(LoginSuccessfulEvent.LOGIN_SUCCESSFUL_BASE,
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        viewDelegate.changeScene(viewDelegate.chooseSceneByName(AdminCatalogSceneCar.class.getSimpleName()),
                                loginTextField.getText());
                    }
                });

        gridPane.setStyle("-fx-background-color: #436994;");

    }

    private void loginButtonFire(String login, String password, Button source)  {
        Task<Boolean> check = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return getUtilController().checkLoginData(login, password);
            }
        };

        check.setOnSucceeded(event -> {
            try {
                loginTaskSucceed(check.get(), source, login);
            } catch (InterruptedException | ExecutionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Błąd krytyczny, program zostanie zamknięty",
                        ButtonType.OK);
                alert.showAndWait();
                System.exit(-1);
            }
        });

        check.setOnFailed(event -> {
            loginTaskFailed(check.getException().getMessage());
        });

        Thread thread = new Thread(check);
        thread.setDaemon(true);
        thread.start();
    }

    private void loginTaskSucceed(boolean credentialsCheck, Button source, String login) {
        if (!credentialsCheck) {
            logger.info("Login attempt failed for admin "+login+": wrong password");
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Nieprawidłowe hasło",
                    ButtonType.OK);
            alert.setHeight(191.0);
            alert.setWidth(360.0);
            alert.setX(viewDelegate.getStage().getX() + viewDelegate.getStage().getWidth() / 2 - alert.getWidth());
            alert.setY(viewDelegate.getStage().getY() + viewDelegate.getStage().getHeight() / 2 - alert.getHeight());
            alert.showAndWait();
        } else {
            source.fireEvent(new LoginSuccessfulEvent(LoginSuccessfulEvent.LOGIN_SUCCESSFUL_BASE,login));
        }

    }

    private void loginTaskFailed(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                message,
                ButtonType.OK);

        alert.setHeight(191.0);
        alert.setWidth(360.0);
        alert.setX(viewDelegate.getStage().getX() + viewDelegate.getStage().getWidth() / 2 - alert.getWidth());
        alert.setY(viewDelegate.getStage().getY() + viewDelegate.getStage().getHeight() / 2 - alert.getHeight());
        alert.showAndWait();
    }

    public UtilController getUtilController() {
        return utilController;
    }

    public double getWidthDim() {
        return widthDim;
    }

    public void setWidthDim(double widthDim) {
        this.widthDim = widthDim;
    }

    public double getHeightDim() {
        return heightDim;
    }

    public void setHeightDim(double heightDim) {
        this.heightDim = heightDim;
    }
}
