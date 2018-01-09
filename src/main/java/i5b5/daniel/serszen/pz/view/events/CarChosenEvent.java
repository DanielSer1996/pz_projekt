package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import javafx.event.Event;
import javafx.event.EventType;

public class CarChosenEvent extends CustomEvent{
    public static EventType<CarChosenEvent> CAR_CHOSEN_BASE = new EventType<>(CustomEvent.CUSTOM_EVENT_TYPE);
    private Car car;

    public CarChosenEvent(EventType<? extends Event> eventType, Car car){
        super(eventType);
        this.car = car;
    }

    @Override
    public void invokeHandler(CustomEventHandler eventHandler) {

    }
}
