package i5b5.daniel.serszen.pz.model.services.impl;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.model.mybatis.mappers.CarPartMapper;
import i5b5.daniel.serszen.pz.model.services.CarPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarPartServiceImpl implements CarPartService{

    @Autowired
    private CarPartMapper carPartMapper;

    @Override
    public List<CarPart> getAllCarPartsForCar(Car car) {
        return carPartMapper.getAllCarPartsForCar(car);
    }

    @Override
    public void deleteCarPartsByCategoryNameAndProducer(long id, String category, String name, String producer) {
        carPartMapper.cascadeDelete(id);
        carPartMapper.deleteCarPartsByCategoryNameAndProducer(category,name,producer);
    }

    @Override
    public void deleteCarPartsByCategoryAndName(long id, String category, String name) {
        carPartMapper.cascadeDelete(id);
        carPartMapper.deleteCarPartsByCategoryAndName(category,name);
    }

    @Override
    public void deleteCarPartsByCategory(long id, String category) {
        carPartMapper.cascadeDelete(id);
        carPartMapper.deleteCarPartsByCategory(category);
    }

    @Override
    public void insertCarPart(CarPart carPart, Car car) {
        carPartMapper.insertCarPart(carPart);
        carPartMapper.insertCarPartRel(car,carPart);
    }

    @Override
    public void insertCategory(String name, String categoryDescriptionText) {
        carPartMapper.insertCategory(name, categoryDescriptionText);
    }

    @Override
    public List<CarPart> getAllCarParts() {
        return carPartMapper.getAllCarParts();
    }


}
