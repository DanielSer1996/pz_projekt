package i5b5.daniel.serszen.pz.view.events.action.handlers;

import i5b5.daniel.serszen.pz.controller.CarPartController;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.view.events.CarPartDeletedEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class DeleteCarPartHandler implements EventHandler<ActionEvent> {
    private CarPart carPart;
    private final ObservableList<String> categories;
    private final ObservableList<String> parts;
    private final ObservableList<String> producers;
    private final CarPartController carPartController;
    private final Node source;

    public DeleteCarPartHandler(ObservableList<String> categories,
                                ObservableList<String> parts,
                                ObservableList<String> producers,
                                CarPartController carPartController,
                                Node source) {
        this.carPart = new CarPart();
        this.categories = categories;
        this.parts = parts;
        this.producers = producers;
        this.carPartController = carPartController;
        this.source = source;
    }

    @Override
    public void handle(ActionEvent event) {
        if (carPart.getCategory() != null
                && carPart.getName() != null
                && carPart.getProducer() != null) {
            producers.remove(carPart.getProducer());
            if (producers.isEmpty()) {
                parts.remove(carPart.getName());
            }
            if (parts.isEmpty()) {
                categories.remove(carPart.getCategory());
            }
            carPartController.deleteCarPartsByCategoryNameAndProducer(carPart.getId(),
                    carPart.getCategory(),
                    carPart.getName(),
                    carPart.getProducer());
        } else if (carPart.getCategory() != null
                && carPart.getName() != null) {
            parts.remove(carPart.getName());
            if (parts.isEmpty()) {
                categories.remove(carPart.getCategory());
            }
            carPartController.deleteCarPartsByCategoryAndName(carPart.getId(),
                    carPart.getCategory(), carPart.getName());
        } else if (carPart.getCategory() != null) {
            categories.remove(carPart.getCategory());
            carPartController.deleteCarPartsByCategory(carPart.getId(),
                    carPart.getCategory());
        }
        source.fireEvent(new CarPartDeletedEvent(CarPartDeletedEvent.CAR_PART_DELETED, carPart));
        carPart = new CarPart();
    }

    public CarPart getCarPart() {
        return carPart;
    }

    public void setCarPart(CarPart carPart) {
        this.carPart = carPart;
    }
}
