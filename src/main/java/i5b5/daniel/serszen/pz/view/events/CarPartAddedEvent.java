package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import javafx.event.Event;
import javafx.event.EventType;

public class CarPartAddedEvent extends Event {
    public static final EventType<CarPartAddedEvent> CAR_PART_ADDED_EVENT = new EventType<>("CAR_PART_ADDED_EVENT");
    private CarPart carPart;

    public CarPartAddedEvent(EventType<? extends Event> eventType, CarPart carPart) {
        super(eventType);
        this.carPart = carPart;
    }

    public CarPart getCarPart() {
        return carPart;
    }

    public void setCarPart(CarPart carPart) {
        this.carPart = carPart;
    }
}
