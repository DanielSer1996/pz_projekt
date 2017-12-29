package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarService carService;

    public CarController() {
    }


    public List<Car> getCarListByBrand(String brand){
        List<Car> cars = carService.getCarsByBrand(brand);
        for(Car car : cars){
            System.out.println(car.getBrand()+ " " + car.getModel());
            System.out.print("Produced from: " + car.getProductionStart().getTime().toString());
            System.out.println(" to: " + car.getProductionEnd().getTime().toString());
        }

        return cars;
    }

    public CarService getCarService() {
        return carService;
    }
}
