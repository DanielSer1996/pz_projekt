package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import i5b5.daniel.serszen.pz.view.events.CarChosenEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogCarScene extends AbstractCatalogScene {

    private Logger logger = LogManager.getLogger(CatalogCarScene.class);

    protected CarController carController;
    protected ViewDelegate viewDelegate;

    protected List<Car> cars = new ArrayList<>();

    protected ObservableList<String> brands = FXCollections.observableArrayList();
    protected ObservableList<String> models = FXCollections.observableArrayList();
    protected ObservableList<String> modelVersions = FXCollections.observableArrayList();

    protected Car currentCar;

    public CatalogCarScene() {
        this(new Pane());
    }

    public CatalogCarScene(Parent root) {
        super(root);
        viewDelegate = ViewDelegate.getInstance();
        carController = BeanFactory.getCarController();
        currentCar = new Car();
        setTableClickingBehaviour();
        setTableDetails();
        addLogoutButton();
        setTableContent();
        initAllCars();
    }

    private void setTableDetails(){
        leftTable.setPlaceholder(new Label("Nie dodano samochodów do katalogu"));
        leftTableColumn.setText("Marka samochodu");
        centralTable.setPlaceholder(new Label("Wybierz markę"));
        centralTableColumn.setText("Model samochodu");
        rightTable.setPlaceholder(new Label("Wybierz model"));
        rightTableColumn.setText("Lata produkcji");
    }

    private void setTableContent() {
        leftTable.setItems(brands);
        centralTable.setItems(models);
        rightTable.setItems(modelVersions);
    }

    private void setTableClickingBehaviour() {
        leftTable.setOnMouseClicked(event -> {
            String chosenBrand = leftTable.getSelectionModel().getSelectedItem();
            if (chosenBrand != null) {
                currentCar.setModel(null);
                currentCar.setProductionStart(null);
                currentCar.setProductionEnd(null);
                currentCar.setBrand(chosenBrand);
                setCarModelTable();
            }

        });

        centralTable.setOnMouseClicked(event -> {
            String chosenModel = centralTable.getSelectionModel().getSelectedItem();
            if (chosenModel != null) {

                currentCar.setProductionStart(null);
                currentCar.setProductionEnd(null);
                currentCar.setModel(chosenModel);

                setCarModelVersionTable();

            }
        });

        rightTable.setOnMouseClicked(event -> {
            String chosenModelVersion = rightTable.getSelectionModel().getSelectedItem();
            if (chosenModelVersion != null) {
                currentCar.setProductionStart(chosenModelVersion.substring(0, 10));
                currentCar.setProductionEnd(chosenModelVersion.substring(13));
                getUriForCurrentCar();
                rightTable.fireEvent(new CarChosenEvent(CarChosenEvent.CAR_CHOSEN_BASE, currentCar));
            }
        });

        rightTable.addEventHandler(CarChosenEvent.CAR_CHOSEN_BASE, new EventHandler<CarChosenEvent>() {
            @Override
            public void handle(CarChosenEvent event) {
                setImage(event.getCar().getImgUri());
            }
        });
    }

    private void initAllCars() {
        Task<ObservableList<Car>> carsTask = new Task<ObservableList<Car>>() {
            @Override
            protected ObservableList<Car> call() throws Exception {
                return FXCollections.observableArrayList(carController.getAllCars());
            }
        };

        carsTask.setOnSucceeded(event -> {
            try {
                cars = carsTask.get();
                createObservableBrandLists();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("error", e);
            }
        });

        Thread thread = new Thread(carsTask);
        thread.start();
    }

    protected void setCarModelTable() {
        models.clear();
        modelVersions.clear();
        for (Car car : cars) {
            if (!models.contains(car.getModel())
                    && car.getBrand().equals(currentCar.getBrand())) {
                models.add(car.getModel());
            }
        }
    }

    protected void addLogoutButton() {
        Menu ex = new Menu("Nawigacja");
        MenuItem exit = new MenuItem("Wyjdź");
        MenuItem logout = new MenuItem("Wstecz");
        ex.getItems().addAll(exit, logout);

        exit.setOnAction(event -> this.getWindow().fireEvent(new WindowEvent(this.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));

        logout.setOnAction(event -> {
            viewDelegate.getScenes().remove(CatalogCarScene.class.getSimpleName());
            viewDelegate.changeScene(viewDelegate.chooseSceneByName(StartingScene.class.getSimpleName()), null);
        });

        menuBar.getMenus().add(ex);
    }

    protected void setCarModelVersionTable() {
        modelVersions.clear();
        for (Car car : cars) {
            if (!modelVersions.contains(car.getProductionStart() + " - " + car.getProductionEnd())
                    && car.getBrand().equals(currentCar.getBrand())
                    && car.getModel().equals(currentCar.getModel())) {
                modelVersions.add(car.getProductionStart() + " - " + car.getProductionEnd());
            }
        }
    }

    private void getUriForCurrentCar() {
        for (Car car : cars) {
            if (car.equals(currentCar)) {
                currentCar.setImgUri(car.getImgUri());
            }
        }
    }

    protected void createObservableBrandLists() {
        for (Car car : cars) {
            if (!brands.contains(car.getBrand())) {
                brands.add(car.getBrand());
            }
        }
    }
}
