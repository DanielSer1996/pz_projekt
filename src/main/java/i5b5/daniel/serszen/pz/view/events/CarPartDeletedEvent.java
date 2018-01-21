package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import javafx.event.Event;
import javafx.event.EventType;

public class CarPartDeletedEvent extends Event {
    public static final EventType<CarPartDeletedEvent> CAR_PART_DELETED = new EventType<>("CAR_PART_DELETED");
    private CarPart carPart;

    public CarPartDeletedEvent(EventType<? extends Event> eventType, CarPart carPart) {
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
