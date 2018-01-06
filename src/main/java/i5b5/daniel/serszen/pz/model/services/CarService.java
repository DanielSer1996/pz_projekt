package i5b5.daniel.serszen.pz.model.services;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CarService {
    List<Car> getCarsByBrand(String brand);
    List<Car> getAllCars();
}
