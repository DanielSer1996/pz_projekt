package i5b5.daniel.serszen.pz.view.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LoginSuccessfulEvent extends CustomEvent {
    public static EventType<LoginSuccessfulEvent> LOGIN_SUCCESSFUL_BASE = new EventType<>(CustomEvent.CUSTOM_EVENT_TYPE);
    private String login;

    public LoginSuccessfulEvent(EventType<? extends Event> eventType, String login) {
        super(eventType);
        this.login=login;
    }

    @Override
    public void invokeHandler(CustomEventHandler eventHandler) {
        eventHandler.onLoginSuccessful(login);
    }
}
