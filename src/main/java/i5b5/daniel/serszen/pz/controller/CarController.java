package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.exceptions.DataIncorrectException;
import i5b5.daniel.serszen.pz.model.exceptions.codes.DataIncorrectExceptionCodes;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.services.CarService;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CarController {

    private final Logger logger = LogManager.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    public CarController() {
    }


    public List<Car> getCarListByBrand(String brand) {

        return carService.getCarsByBrand(brand);
    }

    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    public List<Car> getCarListByModel(String model) {
        return carService.getCarsByModel(model);
    }

    public void deleteCarsByBrand(String brand) {
        carService.deleteCarsByBrand(brand);
    }

    public void deleteCarsByBrandAndModel(String brand, String model) {
        carService.deleteCarsByBrandAndModel(brand, model);
    }

    public void deleteCarsByBrandModelAndProductionDate(String brand,
                                                        String model,
                                                        String productionStartDate,
                                                        String productionEndDate) {
        carService.deleteCarsByBrandModelAndProductionDate(brand, model, productionStartDate, productionEndDate);
    }

    public void insertCar(Car car) throws DataIncorrectException {
        try {
            carService.insertCar(car);
        } catch (DataIncorrectException e) {
            logger.error("Date format incorrect",e);
            throw e;
        } catch (Exception e) {
            logger.error("error", e);
            throw new DataIncorrectException("Incomplete data", e, DataIncorrectExceptionCodes.CAR_DATA_INCOMPLETE);
        }
    }

    public CarService getCarService() {
        return carService;
    }
}
