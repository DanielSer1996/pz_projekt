package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.view.App;
import javafx.beans.property.ReadOnlyStringWrapper;
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

import java.io.File;

public abstract class AbstractCatalogScene extends AbstractCustomScene{
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

    protected VBox searchingPanel;
    protected Label searchText;
    protected TextField searchField;
    protected Button searchButton;


    protected GridPane contentPane;


    public AbstractCatalogScene(Parent root) {
        super(root);
        init();
    }

    private void init(){
        this.widthDim = App.getScreenWidth();
        this.heightDim = App.getScreenHeight();

        initRootPane();
        initSkin();
        initMenuBar();
        configContentPane();
        configSearchingPanel();
        initTables();
        initContentPane();
        initSearchingPane();
        initTitleText();
        addContent();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(rootPane);

        this.setRoot(borderPane);
    }

    private void initSkin(){
        this.getStylesheets().add(getClass().getResource("/css/dark_skin.css").toExternalForm());
    }

    private void initTitleText() {
        titlePane = new HBox();
        titlePane.setAlignment(Pos.TOP_LEFT);
        titlePane.setSpacing(widthDim / 2);
        titleText = new Text("Katalog części samochodowych");
        titleText.setId("title");
        titlePane.getChildren().add(titleText);
    }

    private void addContent() {
        rootPane.add(titlePane,0,5);
        rootPane.add(contentPane, 0, 9);
    }

    protected void initRootPane(){
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);
    }

    private void initMenuBar(){
        menuBar = new MenuBar();
        Menu menuFile = new Menu("Plik");
        Menu menuEdit = new Menu("Edytuj");

        Menu changeSkinMenu = new Menu("Zmień skórkę");
        MenuItem darkSkin = new MenuItem("Ciemna skórka");
        darkSkin.setOnAction(event -> changeSkin("dark_skin"));

        MenuItem brightSkin = new MenuItem("Jasna skórka");
        brightSkin.setOnAction(event -> changeSkin("bright_skin"));

        MenuItem uglySkin = new MenuItem("Brzydka skórka");
        uglySkin.setOnAction(event -> changeSkin("ugly_skin"));

        changeSkinMenu.getItems().addAll(darkSkin,brightSkin,uglySkin);

        menuEdit.getItems().add(changeSkinMenu);

        menuBar.getMenus().addAll(menuFile,menuEdit);
    }

    private void changeSkin(String skin){
        this.getStylesheets().remove(0);
        this.getStylesheets().add(getClass().getResource("/css/"+skin+".css").toExternalForm());
    }

    private void configContentPane() {
        contentPane = new GridPane();
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.setVgap(5);
        contentPane.setHgap(40);
        contentPane.setAlignment(Pos.TOP_CENTER);
    }

    private void configSearchingPanel() {
        searchingPanel = new VBox();
        searchingPanel.setPadding(new Insets(10, 10, 10, 10));
        searchingPanel.setSpacing(20);
        searchingPanel.setAlignment(Pos.CENTER_LEFT);
    }

    private void initSearchingPane() {
        searchText = new Label("Wpisz szukaną frazę");
        searchField = new TextField();
        searchButton = new Button("Szukaj");
        initImageView();

        searchingPanel.getChildren().addAll(searchText, searchField, searchButton, itemImage);
    }

    private void initContentPane() {
        contentPane.add(leftTable, 0, 0);
        contentPane.add(centralTable, 1, 0);
        contentPane.add(rightTable, 2, 0);
        contentPane.add(searchingPanel, 3, 0);
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

    private Image loadNoPhotoImage() {
        File file = new File("img/brak-zdjecia.png");

        return new Image(file.toURI().toString(), 400, 300, false, false);
    }



    public GridPane getRootPane() {
        return rootPane;
    }

}
