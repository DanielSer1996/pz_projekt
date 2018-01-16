package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddCarScene extends Scene {
    private GridPane rootPane;

    private List<Car> carList;

    private ObservableList<String> carBrands = FXCollections.observableArrayList();
    private ObservableList<String> carModels = FXCollections.observableArrayList();

    private Label brandLabel;
    private Label modelLabel;
    private Label prodStartLabel;
    private Label prodEndLabel;
    private Label chosenFile;

    private ComboBox<String> cars;
    private TextField brand;
    private ComboBox<String> carModelsChoser;
    private TextField model;
    private DatePicker productionStart;
    private DatePicker productionEnd;
    private TextField imgUri;
    private Button chooseImg;

    private FileChooser fileChooser;

    private Button confirmButton;

    private final CarController carController;
    private Car addedCar;

    public AddCarScene(Parent root, double width, double height) {
        super(root, width, height);
        carController = BeanFactory.getCarController();
        init();
    }

    private void init() {


        initTextFields();
        initContent();
        initLabels();
        initFileChooser();
        initConfirmButton();
        initRoot();

        this.setRoot(rootPane);
    }

    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("*.png", "*.png"));
        chooseImg = new Button("Wybierz zdjęcie");
        chooseImg.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(this.getWindow());
            if (file != null) {
                imgUri.setText(file.getAbsolutePath());
            }
        });
    }

    private void initConfirmButton() {
        confirmButton = new Button("Dodaj");
        confirmButton.setOnAction(event -> {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    addedCar = new Car(
                            brand.getText(),
                            model.getText(),
                            productionStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            productionEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            imgUri.getText());
                    carController.insertCar(addedCar);
                    return null;
                }
            };
            task.setOnSucceeded(event1 -> {
                this.getWindow().fireEvent(new WindowEvent(this.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            });

            task.setOnFailed(event1 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Niepoprawne dane",
                        ButtonType.OK);
                alert.setX(this.getWindow().getX());
                alert.setY(this.getWindow().getY() + this.getWindow().getHeight() / 3);
                alert.showAndWait();
            });

            new Thread(task).start();
        });
    }

    private void initRoot() {
        rootPane = new GridPane();
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);

        rootPane.add(brandLabel, 2, 0);
        rootPane.add(cars, 0, 1);
        rootPane.add(brand, 2, 1);
        rootPane.add(modelLabel, 2, 2);
        rootPane.add(carModelsChoser, 0, 3);
        rootPane.add(model, 2, 3);
        rootPane.add(prodStartLabel, 2, 4);
        rootPane.add(productionStart, 2, 5);
        rootPane.add(prodEndLabel, 2, 6);
        rootPane.add(productionEnd, 2, 7);
        rootPane.add(chosenFile, 2, 8);
        rootPane.add(imgUri, 2, 9);
        rootPane.add(chooseImg, 2, 10);
        rootPane.add(confirmButton, 2, 12);
    }

    private void initTextFields() {
        brand = new TextField();
        brand.setEditable(false);
        model = new TextField();
        model.setEditable(false);
        productionStart = new DatePicker();
        productionEnd = new DatePicker();
        imgUri = new TextField();
        imgUri.setEditable(false);
    }


    private void initLabels() {
        brandLabel = new Label("Marka samochodu");
        modelLabel = new Label("Model samochodu");
        prodStartLabel = new Label("Początek produkcji");
        prodEndLabel = new Label("Koniec produkcji");
        chosenFile = new Label("Wybrany plik");
    }

    private void initContent() {
        cars = new ComboBox<>();
        carModelsChoser = new ComboBox<>();

        cars.setOnAction(event -> {
            carModels.clear();
            String selected = cars.getSelectionModel().getSelectedItem();
            setTextField(brand, selected);
            putContentIntoModelsArray(selected);
        });

        carModelsChoser.setOnAction(event -> {
            String selected = carModelsChoser.getSelectionModel().getSelectedItem();
            setTextField(model, selected);
        });

        Task<List<Car>> task = new Task<List<Car>>() {
            @Override
            protected List<Car> call() throws Exception {
                carList = carController.getAllCars();
                return carList;
            }
        };

        task.setOnSucceeded(event -> {
            putContentIntoBrandArray();
        });

        new Thread(task).start();
    }

    private void putContentIntoBrandArray() {
        for (Car car : carList) {
            if (!carBrands.contains(car.getBrand())) {
                carBrands.add(car.getBrand());
            }
        }
        initBrandItems();
    }

    private void setTextField(TextField toChange, String selected) {
        if ("Dodaj nowy".equals(selected)) {
            toChange.clear();
            toChange.setEditable(true);
        } else {
            toChange.setEditable(false);
            toChange.setText(selected);
        }
    }

    private void putContentIntoModelsArray(String brand) {
        for (Car car : carList) {
            if (car.getBrand().equals(brand) && !carModels.contains(car.getModel())) {
                carModels.add(car.getModel());
            }
        }
        initModelItem();
    }

    private void initBrandItems() {
        cars.setItems(carBrands);
        cars.getItems().add("Dodaj nowy");
    }

    private void initModelItem() {
        carModelsChoser.setItems(carModels);
        carModelsChoser.getItems().add("Dodaj nowy");
    }

    public Button getChooseImg() {
        return chooseImg;
    }

    public void setChooseImg(Button chooseImg) {
        this.chooseImg = chooseImg;
    }

    public TextField getImgUri() {
        return imgUri;
    }

    public void setImgUri(TextField imgUri) {
        this.imgUri = imgUri;
    }

    public Car getAddedCar() {
        return addedCar;
    }

    public void setAddedCar(Car addedCar) {
        this.addedCar = addedCar;
    }
}
