package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarPartController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.view.events.CarPartChosenEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CatalogCarPartScene extends AbstractCatalogScene{

    private Logger logger = LogManager.getLogger(CatalogCarScene.class);

    private Car chosenCar;

    protected CarPartController carPartController;
    protected List<CarPart> carParts = new ArrayList<>();

    protected ObservableList<String> categories = FXCollections.observableArrayList();
    protected ObservableList<String> parts = FXCollections.observableArrayList();
    protected ObservableList<String> producers = FXCollections.observableArrayList();

    protected CarPart currentCarPart;

    public CatalogCarPartScene(){
        this(new Pane());
    }

    protected CatalogCarPartScene(Parent root) {
        super(root);
        carPartController = BeanFactory.getCarPartController();
        currentCarPart = new CarPart();
        init();
    }

    private void init(){
        setTableDetails();
        setTableContent();
        setTableClickingBehaviour();
        changeBackButtonBehaviour();
        initAllCars();
    }

    private void setTableContent() {
        leftTable.setItems(categories);
        centralTable.setItems(parts);
        rightTable.setItems(producers);
    }

    private void setTableDetails(){
        leftTable.setPlaceholder(new Label("Nie dodano części samochodowych do katalogu"));
        leftTableColumn.setText("Kategoria");
        centralTable.setPlaceholder(new Label("Wybierz kategorię"));
        centralTableColumn.setText("Nazwa części");
        rightTable.setPlaceholder(new Label("Wybierz część"));
        rightTableColumn.setText("Producent");
    }

    private void setTableClickingBehaviour() {
        leftTable.setOnMouseClicked(event -> {
            String chosenCategory = leftTable.getSelectionModel().getSelectedItem();
            if (chosenCategory != null) {
                currentCarPart.setCategory(chosenCategory);
                currentCarPart.setName(null);
                currentCarPart.setProducer(null);
                currentCarPart.setProducerModelCode(null);
                setCarPartsTable();
            }

        });

        centralTable.setOnMouseClicked(event -> {
            String chosenCarPart = centralTable.getSelectionModel().getSelectedItem();
            if (chosenCarPart != null) {
                currentCarPart.setName(chosenCarPart);
                currentCarPart.setProducer(null);
                currentCarPart.setProducerModelCode(null);
                currentCarPart.setName(chosenCarPart);

                setCarPartProducersTable();

            }
        });

        rightTable.setOnMouseClicked(event -> {
            String chosenProducer = rightTable.getSelectionModel().getSelectedItem();
            if (chosenProducer != null) {
                currentCarPart.setProducer(chosenProducer);
                getUriForCurrentCarPart();
                rightTable.fireEvent(new CarPartChosenEvent(CarPartChosenEvent.CAR_PART_CHOSEN, currentCarPart));
            }
        });

        rightTable.addEventHandler(CarPartChosenEvent.CAR_PART_CHOSEN, event -> {
            setImage(event.getCarPart().getImgUri());
        });
    }

    private void initAllCars() {
        Task<List<CarPart>> carPartsTask = new Task<List<CarPart>>() {
            @Override
            protected List<CarPart> call() throws Exception {
                return carPartController.getAllCarParts(chosenCar);
            }
        };

        carPartsTask.setOnSucceeded(event -> {
            try {
                carParts = carPartsTask.get();
                createCategoriesList();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("error", e);
            }
        });

        carPartsTask.setOnFailed(event -> logger.error("error",carPartsTask.getException()));

        Thread thread = new Thread(carPartsTask);
        thread.start();
    }

    private void changeBackButtonBehaviour() {
        menuBar.getMenus().get(2).getItems().get(1).setOnAction(
                event -> {
                    viewDelegate.getScenes().remove(CatalogCarPartScene.class.getSimpleName());
                    viewDelegate.changeScene(viewDelegate.chooseSceneByName(CatalogCarScene.class.getSimpleName()), null);
                });
    }

    protected void setCarPartsTable() {
        parts.clear();
        producers.clear();
        for (CarPart carPart : carParts) {
            if (!parts.contains(carPart.getName())
                    && carPart.getCategory().equals(currentCarPart.getCategory())) {
                parts.add(carPart.getName());
            }
        }
    }

    protected void setCarPartProducersTable() {
        producers.clear();
        for (CarPart carPart : carParts) {
            if (!producers.contains(carPart.getProducer())
                    && carPart.getCategory().equals(currentCarPart.getCategory())
                    && carPart.getName().equals(currentCarPart.getName())) {
                producers.add(carPart.getProducer());
            }
        }
    }

    protected void getUriForCurrentCarPart() {
        for (CarPart carPart : carParts) {
            if (carPart.equals(currentCarPart)) {
                currentCarPart.setId(carPart.getId());
                currentCarPart.setImgUri(carPart.getImgUri());
            }
        }
    }

    protected void createCategoriesList() {
        for (CarPart carPart : carParts) {
            if (!categories.contains(carPart.getCategory())) {
                categories.add(carPart.getCategory());
            }
        }
    }

    public Car getChosenCar() {
        return chosenCar;
    }

    public void setChosenCar(Car chosenCar) {
        this.chosenCar = chosenCar;
    }
}
