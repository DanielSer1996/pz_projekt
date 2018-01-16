package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class StartingScene extends AbstractCustomScene {
    private GridPane rootPane;

    private Text startingSceneLabel;
    private Button adminButton;
    private Button observerButton;
    private ViewDelegate viewDelegate;


    public StartingScene(){
        this(new Pane());
    }

    public StartingScene(Parent root) {
        super(root);
        viewDelegate = ViewDelegate.getInstance();
        init();
    }

    private void init() {
        this.widthDim = 200;
        this.heightDim = 200;

        configRootPane();

        this.startingSceneLabel = new Text("Wybierz co chcesz zrobić");
        this.startingSceneLabel.setStyle("-fx-font: normal bold 12px 'serif';");

        this.adminButton = new Button("Zaloguj jako admin");
        this.adminButton.setTooltip(new Tooltip("Umożliwia zarządzanie katalogiem"));
        this.adminButton.setWrapText(true);
        this.adminButton.setStyle("-fx-background-color: #ffcc66; " +
                "-fx-text-fill: black;" +
                "-fx-font: normal bold 10px 'serif'");
        this.adminButton.setOnAction(event -> {
            viewDelegate.changeScene(viewDelegate.chooseSceneByName(AdminLoginScene.class.getSimpleName()),null);
        });


        this.observerButton = new Button("Przeglądaj katalog");
        this.observerButton.setTooltip(new Tooltip("Pozwala na przeglądanie katalogu bez możliwości dokonywania zmian, niewymagane logowanie"));
        this.observerButton.setWrapText(true);
        this.observerButton.setStyle("-fx-background-color: #ffcc66; " +
                "-fx-text-fill: black;" +
                "-fx-font: normal bold 10px 'serif'");
        this.observerButton.setOnAction(event -> {
            viewDelegate.changeScene(viewDelegate.chooseSceneByName(CatalogCarScene.class.getSimpleName()),null);
        });

        initRootPane();

        this.setRoot(rootPane);
    }

    @Override
    protected void initRootPane() {
        rootPane.add(startingSceneLabel,0,0);
        rootPane.add(adminButton,0,3);
        rootPane.add(observerButton,0,4);

    }

    private void configRootPane(){
        rootPane = new GridPane();
        rootPane.setPadding(new Insets(10, 10, 10, 10));

        rootPane.setVgap(15);
        rootPane.setHgap(5);

        rootPane.setAlignment(Pos.CENTER);
        rootPane.setStyle("-fx-background-color: #436994;");
    }


    public Text getStartingSceneLabel() {
        return startingSceneLabel;
    }

    public Button getAdminButton() {
        return adminButton;
    }

    public Button getObserverButton() {
        return observerButton;
    }
}
