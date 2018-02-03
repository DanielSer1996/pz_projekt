package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.WebController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.view.App;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.utility.Clock;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public abstract class AbstractCatalogScene extends AbstractCustomScene {
    protected final GridPane rootPane = new GridPane();
    protected MenuBar menuBar;

    protected HBox titlePane;
    protected Text titleText;

    protected TableView<String> leftTable;
    protected TableColumn<String, String> leftTableColumn;

    protected TableView<String> centralTable;
    protected TableColumn<String, String> centralTableColumn;
    protected TableView<String> rightTable;
    protected TableColumn<String, String> rightTableColumn;
    protected ImageView itemImage;

    protected VBox rightPanel;
    protected Menu menuEdit;
    protected Menu changeSkinMenu;
    protected MenuItem darkSkin;
    protected MenuItem brightSkin;
    protected MenuItem uglySkin;
    protected Menu changeLanguageMenu;
    protected MenuItem polish;
    protected MenuItem english;

    protected Menu exitMenu;
    protected MenuItem exit;
    protected MenuItem back;

    protected Clock time;

    protected GridPane contentPane;

    protected ViewDelegate viewDelegate;

    public AbstractCatalogScene(Parent root) {
        super(root);
        viewDelegate = ViewDelegate.getInstance();
        init();
    }

    private void init() {
        this.widthDim = App.getScreenWidth();
        this.heightDim = App.getScreenHeight();

        initRootPane();
        initMenuBar();
        configContentPane();
        configSearchingPanel();
        initTables();
        initContentPane();
        initRightPane();
        initTitleText();
        addContent();
        addBackButton();
        changeSkin();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(rootPane);

        this.setRoot(borderPane);
    }

    private void initTitleText() {
        titlePane = new HBox();
        titlePane.setAlignment(Pos.TOP_LEFT);
        titlePane.setSpacing(widthDim / 2);
        titleText = new Text();
        titleText.setId("title");
        titlePane.getChildren().add(titleText);
    }

    private void addContent() {
        rootPane.add(titlePane, 0, 5);
        rootPane.add(contentPane, 0, 9);
        rootPane.add(time,1,11);
    }

    protected void initRootPane() {
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);
        initTime();
    }

    private void initMenuBar() {
        menuBar = new MenuBar();
        menuEdit = new Menu();

        changeSkinMenu = new Menu();
        darkSkin = new MenuItem();
        darkSkin.setOnAction(event -> {
            viewDelegate.setSkinFile("dark_skin");
            changeSkin();
        });

        brightSkin = new MenuItem();
        brightSkin.setOnAction(event -> {
            viewDelegate.setSkinFile("bright_skin");
            changeSkin();
        });

        uglySkin = new MenuItem();
        uglySkin.setOnAction(event -> {
            viewDelegate.setSkinFile("ugly_skin");
            changeSkin();
        });

        changeSkinMenu.getItems().addAll(darkSkin, brightSkin, uglySkin);

        changeLanguageMenu = new Menu();
        polish = new MenuItem();
        english = new MenuItem();

        polish.setOnAction(e -> {
            viewDelegate.setLanguage("pl");
            viewDelegate.changeLanguage();
        });
        english.setOnAction(e -> {
            viewDelegate.setLanguage("en");
            viewDelegate.changeLanguage();
        });

        changeLanguageMenu.getItems().addAll(polish, english);

        menuEdit.getItems().addAll(changeSkinMenu, changeLanguageMenu);

        menuBar.getMenus().addAll(menuEdit);
    }

    private void changeSkin() {
        viewDelegate.changeAppSkin(this);
    }

    private void configContentPane() {
        contentPane = new GridPane();
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.setVgap(5);
        contentPane.setHgap(40);
        contentPane.setAlignment(Pos.TOP_CENTER);
    }

    private void configSearchingPanel() {
        rightPanel = new VBox();
        rightPanel.setPadding(new Insets(10, 10, 10, 10));
        rightPanel.setSpacing(20);
        rightPanel.setAlignment(Pos.CENTER_LEFT);
    }

    private void initRightPane() {
        initImageView();
        rightPanel.getChildren().addAll(itemImage);
    }

    private void initContentPane() {
        contentPane.add(leftTable, 0, 0);
        contentPane.add(centralTable, 1, 0);
        contentPane.add(rightTable, 2, 0);
        contentPane.add(rightPanel, 3, 0);
    }

    private void initTables() {
        leftTable = new TableView<>();

        leftTable.setEditable(true);
        leftTable.setMinWidth(this.getWidthDim() / 6);

        leftTableColumn = new TableColumn<>();
        leftTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        leftTableColumn.setMinWidth(leftTable.getMinWidth());
        leftTableColumn.setResizable(false);

        leftTable.setPrefHeight(this.heightDim - heightDim / 10);
        leftTable.getColumns().addAll(leftTableColumn);

        centralTable = new TableView<>();

        centralTable.setEditable(true);
        centralTable.setMinWidth(this.getWidthDim() / 6);

        centralTableColumn = new TableColumn<>();
        centralTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        centralTableColumn.setMinWidth(centralTable.getMinWidth());
        centralTableColumn.setResizable(false);

        centralTable.setPrefHeight(this.heightDim - heightDim / 10);
        centralTable.getColumns().addAll(centralTableColumn);

        rightTable = new TableView<>();

        rightTable.setEditable(true);
        rightTable.setMinWidth(this.getWidthDim() / 6);

        rightTableColumn = new TableColumn<>();
        rightTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        rightTableColumn.setMinWidth(rightTable.getMinWidth());
        rightTableColumn.setResizable(false);

        rightTable.setPrefHeight(this.heightDim - heightDim / 10);
        rightTable.getColumns().addAll(rightTableColumn);

    }

    public void refreshTimerText(){
        rootPane.getChildren().remove(time);
        rootPane.add(time,1,11);
    }

    protected void addBackButton() {
        exitMenu = new Menu();
        exit = new MenuItem();
        back = new MenuItem();
        exitMenu.getItems().addAll(exit, back);

        exit.setOnAction(event -> this.getWindow().fireEvent(new WindowEvent(this.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));

        menuBar.getMenus().add(exitMenu);
    }

    private void initImageView() {
        itemImage = new ImageView();
        itemImage.setImage(loadNoPhotoImage());
    }

    protected void setImage(String uri) {
        if (uri != null) {
            File file = new File(uri);
            Image image = new Image(file.toURI().toString(), 400, 300, false, false);

            if (!image.isError()) {
                itemImage.setImage(image);
            } else {
                itemImage.setImage(loadNoPhotoImage());
            }
        } else {
            itemImage.setImage(loadNoPhotoImage());
        }
    }

    private void initTime(){
        time = Clock.getInstance();
    }

    private Image loadNoPhotoImage() {
        File file = new File("img/brak-zdjecia.png");

        return new Image(file.toURI().toString(), 400, 300, false, false);
    }

    public GridPane getRootPane() {
        return rootPane;
    }

    public ViewDelegate getViewDelegate() {
        return viewDelegate;
    }

}
