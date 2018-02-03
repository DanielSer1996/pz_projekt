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
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogCarScene extends AbstractCatalogScene {

    private Logger logger = LogManager.getLogger(CatalogCarScene.class);

    protected CarController carController;

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
        carController = BeanFactory.getCarController();
        currentCar = new Car();
        setTableClickingBehaviour();
        setTableDetails();
        setTableContent();
        alterBackButton();
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
                getUriAndIdForCurrentCar();
                rightTable.fireEvent(new CarChosenEvent(CarChosenEvent.CAR_CHOSEN_BASE, currentCar));
                if(event.getClickCount() == 2) {
                    viewDelegate.changeScene(viewDelegate.chooseSceneByName(CatalogCarPartScene.class.getSimpleName()),currentCar);
                }
            }
        });

        rightTable.addEventHandler(CarChosenEvent.CAR_CHOSEN_BASE, new EventHandler<CarChosenEvent>() {
            @Override
            public void handle(CarChosenEvent event) {
                setImage(event.getCar().getImgUri());
            }
        });
    }

    public void initAllCars() {
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

    protected void alterBackButton() {
        menuBar.getMenus().get(1).getItems().get(1).setOnAction(
                event -> {
                    viewDelegate.getScenes().remove(CatalogCarScene.class.getSimpleName());
                    viewDelegate.getScenes().remove(CatalogCarPartScene.class.getSimpleName());
                    viewDelegate.changeScene(viewDelegate.chooseSceneByName(StartingScene.class.getSimpleName()), null);
                });
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

    protected void getUriAndIdForCurrentCar() {
        for (Car car : cars) {
            if (car.equals(currentCar)) {
                currentCar.setId(car.getId());
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
