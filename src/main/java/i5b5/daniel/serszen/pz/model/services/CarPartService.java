package i5b5.daniel.serszen.pz.model.services;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;

import java.util.List;

public interface CarPartService {
    List<CarPart> getAllCarPartsForCar(Car car);
    void deleteCarPartsByCategoryNameAndProducer(long id, String category, String name, String producer);
    void deleteCarPartsByCategoryAndName(long id, String category, String name);
    void deleteCarPartsByCategory(long id, String producer);
    void insertCarPart(CarPart carPart, Car car);

    void insertCategory(String name, String categoryDescriptionText);

    List<CarPart> getAllCarParts();
}
