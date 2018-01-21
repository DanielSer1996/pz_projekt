package i5b5.daniel.serszen.pz.view.events.action.handlers;

import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.view.events.CarDeletedEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class DeleteCarHandler implements EventHandler<ActionEvent> {
    private Car car;
    private ObservableList<String> brands;
    private ObservableList<String> models;
    private ObservableList<String> modelVersions;
    private CarController carController;
    private Node source;

    public DeleteCarHandler(ObservableList<String> brands,
                            ObservableList<String> models,
                            ObservableList<String> modelVersions,
                            CarController carController,
                            Node source) {
        this.car= new Car();
        this.brands = brands;
        this.models = models;
        this.modelVersions = modelVersions;
        this.carController = carController;
        this.source = source;
    }

    @Override
    public void handle(ActionEvent event) {
        if (car.getBrand() != null
                && car.getModel() != null
                && car.getProductionStart() != null && car.getProductionEnd() != null) {
            modelVersions.remove(car.getProductionStart() + " - " + car.getProductionEnd());
            if (modelVersions.isEmpty()) {
                models.remove(car.getModel());
            }
            if (models.isEmpty()) {
                brands.remove(car.getBrand());
            }
            carController.deleteCarsByBrandModelAndProductionDate(car.getId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getProductionStart(),
                    car.getProductionEnd());
        } else if (car.getBrand() != null
                && car.getModel() != null) {
            models.remove(car.getModel());
            if (models.isEmpty()) {
                brands.remove(car.getBrand());
            }
            carController.deleteCarsByBrandAndModel(car.getId(), car.getBrand(), car.getModel());
        } else if (car.getBrand() != null) {
            brands.remove(car.getBrand());
            carController.deleteCarsByBrand(car.getId(), car.getBrand());
        }
        source.fireEvent(new CarDeletedEvent(CarDeletedEvent.CAR_DELETED_EVENT_TYPE, car));
        car = new Car();
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
