package i5b5.daniel.serszen.pz.view.events;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import javafx.event.EventHandler;

public abstract class CustomEventHandler implements EventHandler<CustomEvent> {
    public abstract void onLoginSuccessful(String login);
    public abstract void carChosen(Car car);

    @Override
    public void handle(CustomEvent event) {
        event.invokeHandler(this);
    }
}
