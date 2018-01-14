package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.App;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.events.CarAddedEvent;
import i5b5.daniel.serszen.pz.view.events.CarChosenEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogScene extends AbstractCatalogScene {
    private final Logger logger = LogManager.getLogger(CatalogScene.class);

    private HBox titlePane;
    private Text titleText;

    private GridPane contentPane;
    private TableView<String> carBrandTable;
    private TableView<String> carModelTable;
    private TableView<String> carModelVersionTable;

    private ObservableList<String> brands = FXCollections.observableArrayList();
    private ObservableList<String> models = FXCollections.observableArrayList();
    private ObservableList<String> modelVersions = FXCollections.observableArrayList();

    private Car currentCar;


    private VBox searchingPanel;
    private Label searchText;
    private TextField searchField;
    private Button searchButton;
    private ImageView carImage;
    private Button addNewCarButton;

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

        initRootPane();

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

        this.getStylesheets().add(getClass().getResource("/css/bright_skin.css").toExternalForm());

        this.setRoot(rootPane);
    }

    private void addContent() {
        rootPane.add(titlePane, 0, 3);
        rootPane.add(contentPane, 0, 9);
        rootPane.add(deleteButton, 0, 7);
    }

    @Override
    protected Parent initRootPane() {
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
                if (modelVersions.isEmpty()) {
                    models.remove(currentCar.getModel());
                }
                if (models.isEmpty()) {
                    brands.remove(currentCar.getBrand());
                }
                carController.deleteCarsByBrandModelAndProductionDate(currentCar.getBrand(),
                        currentCar.getModel(),
                        currentCar.getProductionStart(),
                        currentCar.getProductionEnd());
            } else if (currentCar.getBrand() != null
                    && currentCar.getModel() != null) {
                models.remove(currentCar.getModel());
                if (models.isEmpty()) {
                    brands.remove(currentCar.getBrand());
                }
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
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.setVgap(5);
        contentPane.setHgap(40);
        contentPane.setAlignment(Pos.TOP_CENTER);
    }

    private void initImageView() {
        carImage = new ImageView();
        carImage.setImage(loadNoPhotoImage());
    }

    private void setImage(String uri) {
        if (uri != null) {
            File file = new File(uri);
            Image image = new Image(file.toURI().toString(), 400, 300, false, false);

            if (!image.isError()) {
                carImage.setImage(image);
            } else {
                carImage.setImage(loadNoPhotoImage());
            }
        } else {
            carImage.setImage(loadNoPhotoImage());
        }
    }

    private Image loadNoPhotoImage() {
        File file = new File("img/brak-zdjecia.png");

        return new Image(file.toURI().toString(), 400, 300, false, false);
    }

    private void initContentPane() {
        contentPane.add(carBrandTable, 0, 0);
        contentPane.add(carModelTable, 1, 0);
        contentPane.add(carModelVersionTable, 2, 0);
        contentPane.add(searchingPanel, 3, 0);
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
        initAddCarPartButton();

        searchingPanel.getChildren().addAll(searchText, searchField, searchButton, carImage, addNewCarButton);
        searchingPanel.addEventHandler(CarAddedEvent.CAR_ADDED_EVENT_TYPE, new EventHandler<CarAddedEvent>() {
            @Override
            public void handle(CarAddedEvent event) {
                System.out.println(event.getCar().getBrand());
                System.out.println(event.getCar().getModel());
                System.out.println(event.getCar().getImgUri());
            }
        });
    }

    private void initAddCarPartButton() {
        addNewCarButton = new Button("Dodaj model samochodu");
        addNewCarButton.setOnAction(event -> {
            Window owner = ((Node) event.getTarget()).getScene().getWindow();

            Stage addCarStage = new Stage();
            addCarStage.initModality(Modality.APPLICATION_MODAL);
            addCarStage.initOwner(owner);
            addCarStage.setTitle("Dodawanie samochodu");
            addCarStage.setResizable(false);
            AddCarScene addCarScene = new AddCarScene(new Pane(), 400, 500);
            addCarStage.setScene(addCarScene);
            addCarStage.setOnCloseRequest(event1 -> {
                if(addCarScene.getAddedCar() != null) {
                    addNewCarButton.fireEvent(new CarAddedEvent(CarAddedEvent.CAR_ADDED_EVENT_TYPE,
                            addCarScene.getAddedCar()));
                }
            });

            addCarStage.show();
        });
    }

    private void initTables() {
        carBrandTable = new TableView<>();

        carBrandTable.setEditable(true);
        carBrandTable.setPlaceholder(new Label("Nie dodano samochodów do katalogu"));
        carBrandTable.setMinWidth(this.getWidthDim() / 6);

        TableColumn<String, String> brand = new TableColumn<>("Marka samochodu");
        brand.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        brand.setMinWidth(carBrandTable.getMinWidth());
        brand.setResizable(false);

        carBrandTable.setPrefHeight(this.heightDim - heightDim / 10);
        carBrandTable.getColumns().addAll(brand);
        carBrandTable.setItems(brands);

        carModelTable = new TableView<>();

        carModelTable.setEditable(true);
        carModelTable.setPlaceholder(new Label("Wybierz markę"));
        carModelTable.setMinWidth(this.getWidthDim() / 6);

        TableColumn<String, String> model = new TableColumn<>("Model samochodu");
        model.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        model.setMinWidth(carModelTable.getMinWidth());
        model.setResizable(false);

        carModelTable.setPrefHeight(this.heightDim - heightDim / 10);
        carModelTable.getColumns().addAll(model);
        carModelTable.setItems(models);

        carModelVersionTable = new TableView<>();

        carModelVersionTable.setEditable(true);
        carModelVersionTable.setPlaceholder(new Label("Wybierz model"));
        carModelVersionTable.setMinWidth(this.getWidthDim() / 6);

        TableColumn<String, String> prodDate = new TableColumn<>("Lata produkcji");
        prodDate.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        prodDate.setMinWidth(carModelVersionTable.getMinWidth());
        prodDate.setResizable(false);

        carModelVersionTable.setPrefHeight(this.heightDim - heightDim / 10);
        carModelVersionTable.getColumns().addAll(prodDate);
        carModelVersionTable.setItems(modelVersions);

        carBrandTable.setOnMouseClicked(event -> {
            String chosenBrand = carBrandTable.getSelectionModel().getSelectedItem();
            if (chosenBrand != null) {
                currentCar.setModel(null);
                currentCar.setProductionStart(null);
                currentCar.setProductionEnd(null);
                currentCar.setBrand(chosenBrand);

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
                        System.exit(-1);
                    }
                });

                Thread thread = new Thread(carByBrandTask);
                thread.start();
            }

        });

        carModelTable.setOnMouseClicked(event -> {
            String chosenModel = carModelTable.getSelectionModel().getSelectedItem();
            if (chosenModel != null) {

                currentCar.setProductionStart(null);
                currentCar.setProductionEnd(null);
                currentCar.setModel(chosenModel);

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

        carModelVersionTable.addEventHandler(CarChosenEvent.CAR_CHOSEN_BASE, new EventHandler<CarChosenEvent>() {
            @Override
            public void handle(CarChosenEvent event) {
                setImage(event.getCar().getImgUri());
            }
        });

        carModelVersionTable.setOnMouseClicked(event -> {
            String chosenModelVersion = carModelVersionTable.getSelectionModel().getSelectedItem();
            if (chosenModelVersion != null) {
                currentCar.setProductionStart(chosenModelVersion.substring(0, 10));
                currentCar.setProductionEnd(chosenModelVersion.substring(13));
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
        if (cars.size() > 0) {
            currentCar.setBrand(cars.get(0).getBrand());
        } else {
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
        if (cars.size() > 0) {
            currentCar.setModel(cars.get(0).getModel());
        } else {
            currentCar.setModel(null);
        }
        for (Car car : cars) {
            if (!modelVersions.contains(car.getProductionStart() + " - " + car.getProductionEnd())) {
                modelVersions.add(car.getProductionStart() + " - " + car.getProductionEnd());
            }
        }
    }

    private void initTitleText() {
        titlePane = new HBox();
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setSpacing(widthDim / 2);
        titleText = new Text("Katalog części samochodowych - panel administratora");
        titleText.setId("title");
        loggedUser.setId("logged");
        titlePane.getChildren().addAll(titleText, loggedUser);
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

    public ImageView getCarImage() {
        return carImage;
    }

    public void setCarImage(ImageView carImage) {
        this.carImage = carImage;
    }

    public Button getAddNewCarButton() {
        return addNewCarButton;
    }

    public void setAddNewCarButton(Button addNewCarButton) {
        this.addNewCarButton = addNewCarButton;
    }

    public Text getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Text loggedUser) {
        this.loggedUser = loggedUser;
    }
}
