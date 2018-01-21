package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import javafx.event.Event;
import javafx.event.EventType;

public class CarPartChosenEvent extends Event {
    public static final EventType<CarPartChosenEvent> CAR_PART_CHOSEN = new EventType<>("CAR_PART_CHOSEN");
    private CarPart carPart;

    public CarPartChosenEvent(EventType<? extends Event> eventType, CarPart carPart) {
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
