package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import javafx.event.Event;
import javafx.event.EventType;

public class CarChosenEvent extends Event{
    public static EventType<CarChosenEvent> CAR_CHOSEN_BASE = new EventType<>("CAR_CHOSEN_BASE");
    private Car car;

    public CarChosenEvent(EventType<? extends Event> eventType, Car car){
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
