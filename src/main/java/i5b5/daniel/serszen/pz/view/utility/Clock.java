package i5b5.daniel.serszen.pz.view.utility;

import i5b5.daniel.serszen.pz.controller.WebController;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.view.App;
import javafx.concurrent.Task;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Clock extends Text {
    private final Logger logger = LogManager.getLogger(Clock.class);

    private int counterToRefresh;

    private String time;
    private static Clock instance;
    private WebController webController;

    private Clock() {
        counterToRefresh = 0;
        webController = BeanFactory.getWebController();
        timer();
    }


    private void timer(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                time = webController.getTimeAsString();
                setText(time);

                while (App.isRunning){
                    if(counterToRefresh >= 600){
                        time = webController.getTimeAsString();
                    }
                    Thread.sleep(1000);
                    int hour = Integer.valueOf(time.substring(0,2));
                    int minute = Integer.valueOf(time.substring(3,5));
                    int second = Integer.valueOf(time.substring(6,8));
                    if(second == 59){
                        second = 0;
                        if(minute == 59){
                            minute = 0;
                            hour++;
                        }else {
                            minute++;
                        }
                    }else {
                        second++;
                    }
                    String h = hour/10==0 ? "0"+hour : String.valueOf(hour);
                    String m = minute/10==0 ? "0"+minute : String.valueOf(minute);
                    String s = second/10==0 ? "0"+second : String.valueOf(second);
                    time = h +":"+m+":"+s;
                    setText(time);
                    counterToRefresh++;
                }

                return null;
            }
        };

        task.setOnFailed(event -> {
            logger.error(task.getException());
        });

        new Thread(task).start();
    }

    public static Clock getInstance() {
        if (instance == null){
            instance = new Clock();
        }
        return instance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
