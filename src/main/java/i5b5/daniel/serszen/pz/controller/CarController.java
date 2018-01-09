package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.services.CarService;
import org.apache.ibatis.annotations.Param;
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

    public List<Car> getCarListByModel(String model){
        return carService.getCarsByModel(model);
    }

    public void deleteCarsByBrand(String brand){
        carService.deleteCarsByBrand(brand);
    }
    public void deleteCarsByBrandAndModel(String brand, String model){
        carService.deleteCarsByBrandAndModel(brand,model);
    }
    public void deleteCarsByBrandModelAndProductionDate(String brand,
                                                 String model,
                                                 String productionStartDate,
                                                 String productionEndDate){
        carService.deleteCarsByBrandModelAndProductionDate(brand,model,productionStartDate,productionEndDate);
    }

    public CarService getCarService() {
        return carService;
    }
}
