package i5b5.daniel.serszen.pz.view.scenes;

import com.sun.javafx.collections.ObservableListWrapper;
import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.App;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogScene extends AbstractCatalogScene{
    private Pane titlePane;
    private Text titleText;

    private GridPane contentPane;
    private TableView<Car> carBrandTable;
    private ObservableList<Car> cars = FXCollections.observableArrayList();

    private GridPane searchingPanel;
    private Label searchText;
    private TextField searchField;
    private Button searchButton;

    private ViewDelegate viewDelegate;
    private CarController carController;

    public CatalogScene(){
        this(new GridPane());
    }

    private CatalogScene(Parent root) {
        super(root);
        viewDelegate = ViewDelegate.getInstance();
        carController = BeanFactory.getCarController();
        initialize();
    }


    private void initialize(){
        this.widthDim = App.getScreenWidth();
        this.heightDim = App.getScreenHeight();

        configContentPane();
        configSearchingPanel();
        initSearchingPane();
        initTable();
        initTitleText();

        this.setRoot(initRootPane());
    }

    @Override
    protected Parent initRootPane() {
        GridPane gridPane = new GridPane();

        gridPane.setStyle("-fx-background-color: #ffffcc");
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(titlePane,0,2);
        gridPane.add(contentPane,0,4);

        return gridPane;
    }

    private void configContentPane(){
        contentPane = new GridPane();
        contentPane.setStyle("-fx-background-color: #ffffcc");
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.setVgap(5);
        contentPane.setHgap(40);
        contentPane.setAlignment(Pos.TOP_CENTER);
    }

    private void configSearchingPanel(){
        searchingPanel = new GridPane();
        searchingPanel.setStyle("-fx-background-color: #ffffcc");
        searchingPanel.setPadding(new Insets(10, 10, 10, 10));
        searchingPanel.setVgap(5);
        searchingPanel.setHgap(5);
        searchingPanel.setAlignment(Pos.TOP_CENTER);
    }

    private void initSearchingPane(){
        searchingPanel = new GridPane();
        searchText = new Label("Wpisz szukaną frazę");
        searchField = new TextField();
        searchButton = new Button("Szukaj");

        searchingPanel.add(searchText,0,0);
        searchingPanel.add(searchField,0,1);
        searchingPanel.add(searchButton,0,2);

        contentPane.add(searchingPanel,3,0);
    }

    private void initTable(){
        carBrandTable = new TableView<>();

        carBrandTable.setEditable(true);
        carBrandTable.setPlaceholder(new Label("Nie dodano samochodów do katalogu"));
        carBrandTable.setPrefWidth(this.getWidthDim()/2);

        TableColumn<Car,String> brand = new TableColumn<>("Marka samochodu");
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brand.setPrefWidth(carBrandTable.getPrefWidth()/4);

        TableColumn<Car,String> model = new TableColumn<>("Model samochodu");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        model.setPrefWidth(carBrandTable.getPrefWidth()/4);

        TableColumn<Car,String> startProdDate = new TableColumn<>("Początek produkcji");
        startProdDate.setCellValueFactory(new PropertyValueFactory<>("productionStart"));
        startProdDate.setPrefWidth(carBrandTable.getPrefWidth()/4);

        TableColumn<Car,String> endProdDate = new TableColumn<>("Koniec produkcji");
        endProdDate.setCellValueFactory(new PropertyValueFactory<>("productionEnd"));
        endProdDate.setPrefWidth(carBrandTable.getPrefWidth()/4);

        carBrandTable.setPrefHeight(this.heightDim-heightDim/10);
        carBrandTable.getColumns().addAll(brand,model,startProdDate,endProdDate);

        carBrandTable.setOnMouseClicked(event -> {
            System.out.println(carBrandTable.getSelectionModel().getSelectedCells().get(0).getTableColumn().getCellObservableValue(2).getValue().toString());
        });

        carBrandTable.setItems(cars);

        Task<List<Car>> carsTask = new Task<List<Car>>() {
            @Override
            protected List<Car> call() throws Exception {
                return carController.getAllCars();
            }
        };

        carsTask.setOnSucceeded(event -> {
            try {
                createObservableList(carsTask.get());
            } catch (InterruptedException | ExecutionException e) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Błąd podczas pobierania listy samochodów",
                        ButtonType.OK
                );
                alert.showAndWait();
            }
        });

        Thread thread = new Thread(carsTask);
        thread.start();

        contentPane.add(carBrandTable,0,0);
    }

    private void createObservableList(List<Car> list){
        cars.addAll(list);
        carBrandTable.setItems(cars);
    }

    private void initTitleText(){
        titlePane = new Pane();
        titleText = new Text("Katalog części samochodowych - panel administratora");
        titleText.setStyle("-fx-font: normal bold 15px 'serif' ");
        titlePane.getChildren().add(titleText);
    }

    public Text getTitleText() {
        return titleText;
    }

    public void setTitleText(Text titleText) {
        this.titleText = titleText;
    }
}
