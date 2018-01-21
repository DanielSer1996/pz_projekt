package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.events.CarAddedEvent;
import i5b5.daniel.serszen.pz.view.events.CarChosenEvent;
import i5b5.daniel.serszen.pz.view.events.CarDeletedEvent;
import i5b5.daniel.serszen.pz.view.events.action.handlers.DeleteCarHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdminCatalogSceneCar extends CatalogCarScene {
    private final Logger logger = LogManager.getLogger(AdminCatalogSceneCar.class);

    private Text loggedUser = new Text();
    private BooleanProperty isAdminScene = new SimpleBooleanProperty();

    private Button addNewCarButton;

    private Button deleteButton;

    private ContextMenu deleteItemContextMenu;

    private MenuItem deleteItemMenu;

    private DeleteCarHandler deleteCarHandler;

    public AdminCatalogSceneCar() {
        this(new GridPane());
    }

    private AdminCatalogSceneCar(Parent root) {
        super(root);
        deleteCarHandler = new DeleteCarHandler(brands,
                models,
                modelVersions,
                carController,
                contentPane);
        initialize();
    }


    private void initialize() {
        alterRootPane();
        alterSearchingPane();
        changeLabel();
        alterTitleText();
        initContextMenu();
        alterTableClickingBehaviour();
        alterRootPaneContent();
    }

    private void changeLabel() {
        menuBar.getMenus().get(2).getItems().get(1).setOnAction(
                event -> {
                    viewDelegate.getScenes().remove(AdminCatalogSceneCar.class.getSimpleName());
                    viewDelegate.getScenes().remove(AdminCatalogSceneCarPart.class.getSimpleName());
                    viewDelegate.changeScene(viewDelegate.chooseSceneByName(AdminLoginScene.class.getSimpleName()), null);
                });
    }

    private void alterRootPaneContent() {
        rootPane.add(deleteButton, 0, 7);
    }

    private void alterRootPane() {
        deleteButton = new Button();
        rootPane.addEventHandler(CarDeletedEvent.CAR_DELETED_EVENT_TYPE, new EventHandler<CarDeletedEvent>() {
            @Override
            public void handle(CarDeletedEvent event) {
                if (event.getCar().getBrand() != null) {
                    refreshAfterDeleteCar(event.getCar());
                }
            }
        });
        deleteButton.setOnAction(deleteCarHandler);

    }

    private void refreshAfterDeleteCar(Car car) {

        Task<List<Car>> task = new Task<List<Car>>() {
            @Override
            protected List<Car> call() throws Exception {
                List<Car> carsList = new ArrayList<>(cars);
                for (Car car1 : cars) {
                    if (car1.equals(car)) {
                        carsList.remove(car1);
                    }
                }
                return carsList;
            }
        };

        task.setOnSucceeded(event1 -> {
            models.clear();
            cars.clear();
            setImage(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Pomyślnie usunięto samochód!",
                    ButtonType.OK);
            alert.setX(getWindow().getX() + getWindow().getWidth() / 3);
            alert.setY(getWindow().getY() + getWindow().getHeight() / 3);

            alert.showAndWait();
            try {
                cars = task.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("error", e);
            }
        });

        task.setOnFailed(event -> {
            logger.error("eror in thread" + task.getTitle(), task.getException());
        });

        new Thread(task).start();
    }

    private void alterSearchingPane() {
        searchingPanel.addEventHandler(CarAddedEvent.CAR_ADDED_EVENT_TYPE, event -> {
            models.clear();
            modelVersions.clear();
            cars.add(event.getCar());
            createObservableBrandLists();
        });

        initAddCarButton();

        searchingPanel.getChildren().add(addNewCarButton);
    }

    private void alterTableClickingBehaviour() {
        leftTable.setContextMenu(deleteItemContextMenu);
        leftTable.setOnMouseClicked(event -> {
            String chosenBrand = leftTable.getSelectionModel().getSelectedItem();
            if (chosenBrand != null) {
                deleteCarHandler.getCar().setModel(null);
                currentCar.setModel(null);
                deleteCarHandler.getCar().setProductionStart(null);
                currentCar.setProductionStart(null);
                deleteCarHandler.getCar().setProductionEnd(null);
                currentCar.setProductionEnd(null);
                deleteCarHandler.getCar().setBrand(chosenBrand);
                currentCar.setBrand(chosenBrand);
                setCarModelTable();
            }
            if(MouseButton.SECONDARY.equals(event.getButton())){
                deleteItemContextMenu.show(this.getWindow());
            }
        });

        centralTable.setContextMenu(deleteItemContextMenu);
        centralTable.setOnMouseClicked(event -> {
            String chosenModel = centralTable.getSelectionModel().getSelectedItem();
            if (chosenModel != null) {

                deleteCarHandler.getCar().setProductionStart(null);
                currentCar.setProductionStart(null);
                deleteCarHandler.getCar().setProductionEnd(null);
                currentCar.setProductionEnd(null);
                deleteCarHandler.getCar().setModel(chosenModel);
                currentCar.setModel(chosenModel);

                setCarModelVersionTable();

            }
            if(MouseButton.SECONDARY.equals(event.getButton())){
                deleteItemContextMenu.show(this.getWindow());
            }
        });

        rightTable.setContextMenu(deleteItemContextMenu);
        rightTable.setOnMouseClicked(event -> {
            String chosenModelVersion = rightTable.getSelectionModel().getSelectedItem();
            if (chosenModelVersion != null) {
                deleteCarHandler.getCar().setProductionStart(chosenModelVersion.substring(0, 10));
                currentCar.setProductionStart(chosenModelVersion.substring(0, 10));
                deleteCarHandler.getCar().setProductionEnd(chosenModelVersion.substring(13));
                currentCar.setProductionEnd(chosenModelVersion.substring(13));
                getUriAndIdForCurrentCar();
                deleteCarHandler.getCar().setId(currentCar.getId());
                rightTable.fireEvent(new CarChosenEvent(CarChosenEvent.CAR_CHOSEN_BASE, currentCar));
            }
            if(MouseButton.SECONDARY.equals(event.getButton())){
                deleteItemContextMenu.show(this.getWindow());
            }
            if(event.getClickCount() == 2 && currentCar.getProductionStart() != null){
                viewDelegate.changeScene(viewDelegate.chooseSceneByName(AdminCatalogSceneCarPart.class.getSimpleName()),currentCar);
            }
        });

        rightTable.addEventHandler(CarChosenEvent.CAR_CHOSEN_BASE, new EventHandler<CarChosenEvent>() {
            @Override
            public void handle(CarChosenEvent event) {
                setImage(event.getCar().getImgUri());
            }
        });
    }

    private void initAddCarButton() {
        addNewCarButton = new Button("Dodaj model samochodu");
        addNewCarButton.setOnAction(event -> {
            Window owner = ((Node) event.getTarget()).getScene().getWindow();

            Stage addCarStage = new Stage();
            addCarStage.initModality(Modality.APPLICATION_MODAL);
            addCarStage.initOwner(owner);
            addCarStage.setTitle("Dodawanie samochodu");
            addCarStage.setResizable(false);
            AddCarScene addCarScene = (AddCarScene) viewDelegate.chooseSceneByName(AddCarScene.class.getSimpleName());
            addCarStage.setScene(addCarScene);
            addCarStage.setOnCloseRequest(event1 -> {
                if (addCarScene.getAddedCar() != null) {
                    addNewCarButton.fireEvent(new CarAddedEvent(CarAddedEvent.CAR_ADDED_EVENT_TYPE,
                            addCarScene.getAddedCar()));
                }
            });

            addCarStage.show();
        });

    }

    private void initContextMenu() {
        deleteItemContextMenu = new ContextMenu();
        deleteItemMenu = new MenuItem();
        deleteItemContextMenu.getItems().add(deleteItemMenu);
        deleteItemMenu.setOnAction(deleteCarHandler);
    }

    private void alterTitleText() {
        titlePane.setAlignment(Pos.CENTER);
        loggedUser.setId("logged");
        titlePane.getChildren().add(loggedUser);
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
        return itemImage;
    }

    public void setCarImage(ImageView carImage) {
        this.itemImage = carImage;
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

    public List<Car> getCars() {
        return cars;
    }

    public boolean isIsAdminScene() {
        return isAdminScene.get();
    }

    public BooleanProperty isAdminSceneProperty() {
        return isAdminScene;
    }

    public void setIsAdminScene(boolean isAdminScene) {
        this.isAdminScene.set(isAdminScene);
    }
}
