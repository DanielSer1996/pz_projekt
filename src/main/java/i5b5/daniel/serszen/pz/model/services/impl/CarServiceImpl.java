package i5b5.daniel.serszen.pz.model.services.impl;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.mappers.CarMapper;
import i5b5.daniel.serszen.pz.model.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private final CarMapper carMapper;

    public CarServiceImpl(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @Override
    public List<Car> getCarsByBrand(String brand) {
        return carMapper.getCarsByBrand(brand);
    }

    @Override
    public List<Car> getCarsByModel(String model) {
        return carMapper.getCarsByModel(model);
    }

    @Override
    public List<Car> getAllCars() {
        return carMapper.getAllCars();
    }

    @Override
    public void deleteCarsByBrand(String brand) {
        carMapper.deleteCarsByBrand(brand);
    }

    @Override
    public void deleteCarsByBrandAndModel(String brand, String model) {
        carMapper.deleteCarsByBrandAndModel(brand,model);
    }

    @Override
    public void deleteCarsByBrandModelAndProductionDate(String brand, String model, String productionStartDate, String productionEndDate) {
        carMapper.deleteCarsByBrandModelAndProductionDate(brand, model, productionStartDate, productionEndDate);
    }

    public CarMapper getCarMapper() {
        return carMapper;
    }
}
