package i5b5.daniel.serszen.pz.model.factories;

import i5b5.daniel.serszen.pz.config.AppConfig;
import i5b5.daniel.serszen.pz.controller.CarController;
import i5b5.daniel.serszen.pz.controller.UtilController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanFactory {
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    public static CarController getCarController(){
        return applicationContext.getBean(CarController.class);
    }
    public static UtilController getUtilController(){
        return applicationContext.getBean(UtilController.class);
    }
}
