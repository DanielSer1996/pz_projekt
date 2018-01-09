package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.App;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.events.CarChosenEvent;
import i5b5.daniel.serszen.pz.view.events.CustomEventHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogScene extends AbstractCatalogScene {
    private GridPane rootPane;

    private Pane titlePane;
    private Text titleText;

    private GridPane contentPane;
    private TableView<String> carBrandTable;
    private TableView<String> carModelTable;
    private TableView<String> carModelVersionTable;

    private ObservableList<String> brands = FXCollections.observableArrayList();
    private ObservableList<String> models = FXCollections.observableArrayList();
    private ObservableList<String> modelVersions = FXCollections.observableArrayList();

    private Car currentCar;


    private GridPane searchingPanel;
    private Label searchText;
    private TextField searchField;
    private Button searchButton;

    private Button deleteButton;

    private ViewDelegate viewDelegate;
    private CarController carController;

    public CatalogScene() {
        this(new GridPane());
    }

    private CatalogScene(Parent root) {
        super(root);
        viewDelegate = ViewDelegate.getInstance();
        carController = BeanFactory.getCarController();
        currentCar = new Car();
        initialize();
    }


    private void initialize() {
        this.widthDim = App.getScreenWidth();
        this.heightDim = App.getScreenHeight();

        this.setRoot(initRootPane());

        configContentPane();
        configSearchingPanel();
        initSearchingPane();
        initTables();
        initTitleText();
        initContentPane();

        addContent();


        Task<List<Car>> carsTask = new Task<List<Car>>() {
            @Override
            protected List<Car> call() throws Exception {
                return carController.getAllCars();
            }
        };

        carsTask.setOnSucceeded(event -> {
            try {
                createObservableBrandLists(carsTask.get());
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
    }

    private void addContent() {
        rootPane.add(titlePane, 0, 2);
        rootPane.add(contentPane, 0, 4);
        rootPane.add(deleteButton, 0, 3);
    }

    @Override
    protected Parent initRootPane() {
        rootPane = new GridPane();

        rootPane.setStyle("-fx-background-color: #ffffcc");
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);

        deleteButton = new Button("Usuń zaznaczoną pozycję");
        deleteButton.setOnAction(event -> {
            if (currentCar.getBrand() != null
                    && currentCar.getModel() != null
                    && currentCar.getProductionStart() != null && currentCar.getProductionEnd() != null) {
                modelVersions.remove(currentCar.getProductionStart() + " - " + currentCar.getProductionEnd());
                carController.deleteCarsByBrandModelAndProductionDate(currentCar.getBrand(),
                        currentCar.getModel(),
                        currentCar.getProductionStart(),
                        currentCar.getProductionEnd());
            } else if (currentCar.getBrand() != null
                    && currentCar.getModel() != null) {
                models.remove(currentCar.getModel());
                carController.deleteCarsByBrandAndModel(currentCar.getBrand(), currentCar.getModel());
            } else if (currentCar.getBrand() != null) {
                brands.remove(currentCar.getBrand());
                carController.deleteCarsByBrand(currentCar.getBrand());
            }
        });

        return rootPane;
    }

    private void configContentPane() {
        contentPane = new GridPane();
        contentPane.setStyle("-fx-background-color: #ffffcc");
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.setVgap(5);
        contentPane.setHgap(40);
        contentPane.setAlignment(Pos.TOP_CENTER);
    }

    private void initContentPane() {
        contentPane.add(carBrandTable, 0, 0);
        contentPane.add(carModelTable, 1, 0);
        contentPane.add(carModelVersionTable, 2, 0);

        contentPane.add(searchingPanel, 3, 0);
    }

    private void configSearchingPanel() {
        searchingPanel = new GridPane();
        searchingPanel.setStyle("-fx-background-color: #ffffcc");
        searchingPanel.setPadding(new Insets(10, 10, 10, 10));
        searchingPanel.setVgap(5);
        searchingPanel.setHgap(5);
        searchingPanel.setAlignment(Pos.TOP_CENTER);
    }

    private void initSearchingPane() {
        searchText = new Label("Wpisz szukaną frazę");
        searchField = new TextField();
        searchButton = new Button("Szukaj");

        searchingPanel.add(searchText, 0, 0);
        searchingPanel.add(searchField, 0, 1);
        searchingPanel.add(searchButton, 0, 2);

    }

    private void initTables() {
        carBrandTable = new TableView<>();

        carBrandTable.setEditable(true);
        carBrandTable.setPlaceholder(new Label("Nie dodano samochodów do katalogu"));
        carBrandTable.setPrefWidth(this.getWidthDim() / 4);

        TableColumn<String, String> brand = new TableColumn<>("Marka samochodu");
        brand.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        brand.setPrefWidth(carBrandTable.getPrefWidth());
        brand.setResizable(false);

        carBrandTable.setPrefHeight(this.heightDim - heightDim / 10);
        carBrandTable.getColumns().addAll(brand);
        carBrandTable.setItems(brands);

        carModelTable = new TableView<>();

        carModelTable.setEditable(true);
        carModelTable.setPlaceholder(new Label("Wybierz markę"));
        carModelTable.setPrefWidth(this.getWidthDim() / 4);

        TableColumn<String, String> model = new TableColumn<>("Model samochodu");
        model.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        model.setPrefWidth(carModelTable.getPrefWidth());
        model.setResizable(false);

        carModelTable.setPrefHeight(this.heightDim - heightDim / 10);
        carModelTable.getColumns().addAll(model);
        carModelTable.setItems(models);

        carModelVersionTable = new TableView<>();

        carModelVersionTable.setEditable(true);
        carModelVersionTable.setPlaceholder(new Label("Wybierz model"));
        carModelVersionTable.setPrefWidth(this.getWidthDim() / 4);

        TableColumn<String, String> prodDate = new TableColumn<>("Lata produkcji");
        prodDate.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        prodDate.setPrefWidth(carModelVersionTable.getPrefWidth());
        prodDate.setResizable(false);

        carModelVersionTable.setPrefHeight(this.heightDim - heightDim / 10);
        carModelVersionTable.getColumns().addAll(prodDate);
        carModelVersionTable.setItems(modelVersions);

        carBrandTable.setOnMouseClicked(event -> {
            currentCar.setModel(null);
            currentCar.setProductionStart(null);
            currentCar.setProductionEnd(null);
            TablePosition tablePosition = carBrandTable.getSelectionModel().getSelectedCells().get(0);
            String chosenBrand = carBrandTable.getSelectionModel()
                    .getSelectedCells().get(0)
                    .getTableColumn()
                    .getCellObservableValue(tablePosition.getRow())
                    .getValue().toString();
            currentCar.setBrand(chosenBrand);
            if (event.getClickCount() == 2) {

                Task<List<Car>> carByBrandTask = new Task<List<Car>>() {
                    @Override
                    protected List<Car> call() throws Exception {
                        return carController.getCarListByBrand(chosenBrand);
                    }
                };
                carByBrandTask.setOnSucceeded(event1 -> {
                    try {
                        setCarModelTable(carByBrandTask.get());
                    } catch (InterruptedException | ExecutionException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Błąd krytyczny, program zostanie zamknięty",
                                ButtonType.OK);
                        alert.showAndWait();
                    }
                });

                Thread thread = new Thread(carByBrandTask);
                thread.start();
            }
        });

        carModelTable.setOnMouseClicked(event -> {
            currentCar.setProductionStart(null);
            currentCar.setProductionEnd(null);
            TablePosition tablePosition = carModelTable.getSelectionModel().getSelectedCells().get(0);
            String chosenModel = carModelTable.getSelectionModel()
                    .getSelectedCells().get(0)
                    .getTableColumn()
                    .getCellObservableValue(tablePosition.getRow())
                    .getValue().toString();
            currentCar.setModel(chosenModel);
            if (event.getClickCount() == 2) {

                Task<List<Car>> carByModelTask = new Task<List<Car>>() {
                    @Override
                    protected List<Car> call() throws Exception {
                        return carController.getCarListByModel(chosenModel);
                    }
                };

                carByModelTask.setOnSucceeded(event1 -> {
                    try {
                        setCarModelVersionTable(carByModelTask.get());
                    } catch (InterruptedException | ExecutionException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Błąd krytyczny, program zostanie zamknięty",
                                ButtonType.OK);
                        alert.showAndWait();
                    }
                });

                Thread thread = new Thread(carByModelTask);
                thread.start();
            }
        });

        carModelVersionTable.addEventHandler(CarChosenEvent.CAR_CHOSEN_BASE, new CustomEventHandler() {
            @Override
            public void onLoginSuccessful(String login) {
            }

            @Override
            public void carChosen(Car car) {
                System.out.println(car.getBrand()+" "+car.getModel());
            }
        });

        carModelVersionTable.setOnMouseClicked(event -> {
            TablePosition tablePosition = carModelVersionTable.getSelectionModel().getSelectedCells().get(0);
            String chosenModelVersion = carModelVersionTable.getSelectionModel()
                    .getSelectedCells().get(0)
                    .getTableColumn()
                    .getCellObservableValue(tablePosition.getRow())
                    .getValue().toString();
            currentCar.setProductionStart(chosenModelVersion.substring(0, 10));
            currentCar.setProductionEnd(chosenModelVersion.substring(13));
            if (event.getClickCount() == 2) {
                carModelVersionTable.fireEvent(new CarChosenEvent(CarChosenEvent.CAR_CHOSEN_BASE, currentCar));
            }
        });
    }

    private void createObservableBrandLists(List<Car> cars) {
        for (Car car : cars) {
            if (!brands.contains(car.getBrand())) {
                brands.add(car.getBrand());
            }
        }
    }

    private void setCarModelTable(List<Car> cars) {
        models.clear();
        modelVersions.clear();
        if(cars.size() > 0) {
            currentCar.setBrand(cars.get(0).getBrand());
        }else {
            currentCar.setBrand(null);
        }
        for (Car car : cars) {
            if (!models.contains(car.getModel())) {
                models.add(car.getModel());
            }
        }
    }

    private void setCarModelVersionTable(List<Car> cars) {
        modelVersions.clear();
        if(cars.size() > 0) {
            currentCar.setModel(cars.get(0).getModel());
        }else{
            currentCar.setModel(null);
        }
        for (Car car : cars) {
            if (!modelVersions.contains(car.getProductionStart() + " - " + car.getProductionEnd())) {
                modelVersions.add(car.getProductionStart() + " - " + car.getProductionEnd());
            }
        }
    }

    private void initTitleText() {
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

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Car getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(Car currentCar) {
        this.currentCar = currentCar;
    }
}
