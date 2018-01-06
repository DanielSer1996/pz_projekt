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

        return carService.getCarsByBrand(brand);
    }

    public List<Car> getAllCars(){
        return carService.getAllCars();
    }

    public CarService getCarService() {
        return carService;
    }
}
