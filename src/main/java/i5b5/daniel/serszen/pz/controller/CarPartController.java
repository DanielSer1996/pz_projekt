package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.model.services.CarPartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CarPartController {
    private final Logger logger = LogManager.getLogger(CarPartController.class);

    @Autowired
    private CarPartService carPartService;

    public List<CarPart> getAllCarParts(Car car) {
        return carPartService.getAllCarPartsForCar(car);
    }

    public void deleteCarPartsByCategoryNameAndProducer(long id, String category, String name, String producer) {
        carPartService.deleteCarPartsByCategoryNameAndProducer(id,category, name, producer);
    }

    public void deleteCarPartsByCategoryAndName(long id, String category, String name) {
        carPartService.deleteCarPartsByCategoryAndName(id,category, name);
    }

    public void deleteCarPartsByCategory(long id, String category) {
        carPartService.deleteCarPartsByCategory(id, category);
    }

}
