package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.view.events.*;
import i5b5.daniel.serszen.pz.view.events.action.handlers.DeleteCarPartHandler;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdminCatalogSceneCarPart extends CatalogCarPartScene{
    private final Logger logger = LogManager.getLogger(AdminCatalogSceneCarPart.class);
    private DeleteCarPartHandler deleteCarPartHandler;
    private ContextMenu deleteItemContextMenu;
    private MenuItem deleteItemMenu;

    private Button addCarPartButton;
    private Button deleteSelectedButton;

    public AdminCatalogSceneCarPart(){
        this(new Pane());
    }

    public AdminCatalogSceneCarPart(Parent root){
        super(root);
        deleteCarPartHandler = new DeleteCarPartHandler(categories,
                parts,
                producers,
                carPartController,
                contentPane);
        init();
    }

    private void init(){
        initButtons();
        alterRootPane();
        alterSearchingPane();
        changeLabel();
        initContextMenu();
        alterTableClickingBehaviour();
        alterRootPaneContent();
    }

    private void alterRootPane() {
        rootPane.addEventHandler(CarPartDeletedEvent.CAR_PART_DELETED, new EventHandler<CarPartDeletedEvent>() {
            @Override
            public void handle(CarPartDeletedEvent event) {
                if (event.getCarPart().getCategory() != null) {
                    refreshAfterDeleteCarPart(event.getCarPart());
                }
            }
        });

    }

    private void refreshAfterDeleteCarPart(CarPart carPart) {

        Task<List<CarPart>> task = new Task<List<CarPart>>() {
            @Override
            protected List<CarPart> call() throws Exception {
                List<CarPart> carPartsList = new ArrayList<>(carParts);
                for (CarPart car1 : carParts) {
                    if (car1.getCategory().equals(carPart.getCategory())
                            && car1.getName().equals(carPart.getCategory())
                            && car1.getProducer().equals(carPart.getProducer())) {
                        carPartsList.remove(car1);
                    }
                }
                return carPartsList;
            }
        };

        task.setOnSucceeded(event1 -> {
            carParts.clear();
            categories.clear();
            setImage(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    viewDelegate.getMessageForAlert("carPartDeletedSuccess"),
                    ButtonType.OK);
            alert.setX(getWindow().getX() + getWindow().getWidth() / 3);
            alert.setY(getWindow().getY() + getWindow().getHeight() / 3);

            alert.showAndWait();
            try {
                carParts = task.get();
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
        rightPanel.addEventHandler(CarPartAddedEvent.CAR_PART_ADDED_EVENT, event -> {
            viewDelegate.getScenes().remove(AddCarPartScene.class.getSimpleName());
            parts.clear();
            producers.clear();
            carParts.add(event.getCarPart());
            createCategoriesList();
        });

        rightPanel.getChildren().add(addCarPartButton);
    }

    private void changeLabel() {
        menuBar.getMenus().get(1).getItems().get(1).setOnAction(
                event -> {
                    viewDelegate.getScenes().remove(AdminCatalogSceneCarPart.class.getSimpleName());
                    viewDelegate.changeScene(viewDelegate.chooseSceneByName(AdminCatalogSceneCar.class.getSimpleName()), null);
                });
    }

    private void initContextMenu() {
        deleteItemContextMenu = new ContextMenu();
        deleteItemMenu = new MenuItem();
        deleteItemContextMenu.getItems().add(deleteItemMenu);
        deleteItemMenu.setOnAction(deleteCarPartHandler);
    }

    private void alterTableClickingBehaviour() {
        leftTable.setContextMenu(deleteItemContextMenu);
        leftTable.setOnMouseClicked(event -> {
            String chosenBrand = leftTable.getSelectionModel().getSelectedItem();
            if (chosenBrand != null) {
                deleteCarPartHandler.getCarPart().setName(null);
                currentCarPart.setName(null);
                deleteCarPartHandler.getCarPart().setProducer(null);
                currentCarPart.setProducer(null);
                deleteCarPartHandler.getCarPart().setCategory(chosenBrand);
                currentCarPart.setCategory(chosenBrand);
                setCarPartsTable();
            }
            if(MouseButton.SECONDARY.equals(event.getButton())
                    && event.isSecondaryButtonDown()){
                deleteItemContextMenu.show(this.getWindow());
            }
        });

        centralTable.setContextMenu(deleteItemContextMenu);
        centralTable.setOnMouseClicked(event -> {
            String chosenModel = centralTable.getSelectionModel().getSelectedItem();
            if (chosenModel != null) {

                deleteCarPartHandler.getCarPart().setProducer(null);
                currentCarPart.setProducer(null);
                deleteCarPartHandler.getCarPart().setName(chosenModel);
                currentCarPart.setName(chosenModel);

                setCarPartProducersTable();

            }
            if(MouseButton.SECONDARY.equals(event.getButton())
                    && event.isSecondaryButtonDown()){
                deleteItemContextMenu.show(this.getWindow());
            }
        });

        rightTable.setContextMenu(deleteItemContextMenu);
        rightTable.setOnMouseClicked(event -> {
            String chosenModelVersion = rightTable.getSelectionModel().getSelectedItem();
            if (chosenModelVersion != null) {
                deleteCarPartHandler.getCarPart().setProducer(chosenModelVersion);
                currentCarPart.setProducer(chosenModelVersion);
                getUriForCurrentCarPart();
                deleteCarPartHandler.getCarPart().setId(currentCarPart.getId());
                rightTable.fireEvent(new CarPartChosenEvent(CarPartChosenEvent.CAR_PART_CHOSEN, currentCarPart));
            }
            if(MouseButton.SECONDARY.equals(event.getButton())
                    && event.isSecondaryButtonDown()){
                deleteItemContextMenu.show(this.getWindow());
            }
        });

        rightTable.addEventHandler(CarPartChosenEvent.CAR_PART_CHOSEN, event -> {
            setImage(event.getCarPart().getImgUri());
        });
    }

    private void alterRootPaneContent() {
        rootPane.add(deleteSelectedButton, 0, 7);
    }

    private void initButtons(){
        addCarPartButton = new Button();
        addCarPartButton.setOnAction(event -> {
            Window owner = ((Node) event.getTarget()).getScene().getWindow();

            Stage addCarPartStage = new Stage();
            addCarPartStage.initModality(Modality.APPLICATION_MODAL);
            addCarPartStage.initOwner(owner);
            addCarPartStage.setTitle(viewDelegate.getMessageForAlert("addingCarPart"));
            addCarPartStage.setResizable(false);
            AddCarPartScene addCarPartScene = (AddCarPartScene) viewDelegate.chooseSceneByName(AddCarPartScene.class.getSimpleName());
            viewDelegate.changeAppSkin(addCarPartScene);
            addCarPartScene.setAddedCar(getChosenCar());
            addCarPartStage.setScene(addCarPartScene);
            addCarPartScene.initContent();
            addCarPartStage.setOnCloseRequest(event1 -> {
                if (addCarPartScene.getAddedCarPart() != null) {
                    addCarPartButton.fireEvent(new CarPartAddedEvent(CarPartAddedEvent.CAR_PART_ADDED_EVENT,
                            addCarPartScene.getAddedCarPart()));
                }
            });

            addCarPartStage.show();
        });
        deleteSelectedButton = new Button();
        deleteSelectedButton.setOnAction(deleteCarPartHandler);
    }

    public DeleteCarPartHandler getDeleteCarPartHandler() {
        return deleteCarPartHandler;
    }

    public void setDeleteCarPartHandler(DeleteCarPartHandler deleteCarPartHandler) {
        this.deleteCarPartHandler = deleteCarPartHandler;
    }
}
