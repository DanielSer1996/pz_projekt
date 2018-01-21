package i5b5.daniel.serszen.pz.model.services.impl;

import i5b5.daniel.serszen.pz.model.exceptions.DataIncorrectException;
import i5b5.daniel.serszen.pz.model.exceptions.codes.DataIncorrectExceptionCodes;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.mappers.CarMapper;
import i5b5.daniel.serszen.pz.model.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService{

    private final CarMapper carMapper;

    @Autowired
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
    public void deleteCarsByBrand(long id, String brand) {
        carMapper.cascadeDelete(id);
        carMapper.deleteCarsByBrand(brand);
    }

    @Override
    public void deleteCarsByBrandAndModel(long id, String brand, String model) {
        carMapper.cascadeDelete(id);
        carMapper.deleteCarsByBrandAndModel(brand,model);
    }

    @Override
    public void deleteCarsByBrandModelAndProductionDate(long id,String brand, String model, String productionStartDate, String productionEndDate) {
        carMapper.cascadeDelete(id);
        carMapper.deleteCarsByBrandModelAndProductionDate(brand, model, productionStartDate, productionEndDate);
    }

    @Override
    public void insertCar(Car car) throws DataIncorrectException {
        validateDateFormat(car);
        carMapper.insertCar(car);
    }

    private void validateDateFormat(Car car) throws DataIncorrectException {
        if(!car.getProductionStart().matches("\\d{4}-\\d{2}-\\d{2}")
                && !car.getProductionEnd().matches("\\d{4}-\\d{2}-\\d{2}")){
            throw new DataIncorrectException("Date format incorrect", DataIncorrectExceptionCodes.CAR_DATA_INCORRECT);

        }
    }


    public CarMapper getCarMapper() {
        return carMapper;
    }
}
