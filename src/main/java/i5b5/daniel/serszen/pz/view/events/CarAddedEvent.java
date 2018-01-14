package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import javafx.event.Event;
import javafx.event.EventType;

public class CarAddedEvent extends Event{
    public static final EventType<CarAddedEvent> CAR_ADDED_EVENT_TYPE = new EventType<>("CAR_ADDED_EVENT_TYPE");
    private Car car;

    public CarAddedEvent(EventType<? extends Event> eventType, Car car) {
        super(eventType);
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
