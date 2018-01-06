package i5b5.daniel.serszen.pz.view.events;

import javafx.event.EventHandler;

public abstract class CustomEventHandler implements EventHandler<CustomEvent> {
    public abstract void onLoginSuccessful(String login);

    @Override
    public void handle(CustomEvent event) {
        event.invokeHandler(this);
    }
}
