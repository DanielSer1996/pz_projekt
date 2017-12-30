package i5b5.daniel.serszen.pz.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LoginSuccessfulEvent extends CustomEvent{
    public static EventType<LoginSuccessfulEvent> LOGIN_SUCCESSFUL_BASE = new EventType<>(CustomEvent.CUSTOM_EVENT_TYPE);

    public LoginSuccessfulEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    @Override
    public void invokeHandler(CustomEventHandler eventHandler) {
        eventHandler.onLoginSuccessful();
    }
}
