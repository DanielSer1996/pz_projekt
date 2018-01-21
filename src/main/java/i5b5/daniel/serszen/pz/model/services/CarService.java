package i5b5.daniel.serszen.pz.model.services;

import i5b5.daniel.serszen.pz.model.exceptions.DataIncorrectException;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CarService {
    List<Car> getCarsByBrand(String brand);
    List<Car> getCarsByModel(String model);
    List<Car> getAllCars();

    void deleteCarsByBrand(long id, String brand);
    void deleteCarsByBrandAndModel(long id, String brand, String model);
    void deleteCarsByBrandModelAndProductionDate(long id,
                                                 String brand,
                                                 String model,
                                                 String productionStartDate,
                                                 String productionEndDate);
    void insertCar(Car car) throws DataIncorrectException;
}
