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

    public CarMapper getCarMapper() {
        return carMapper;
    }
}
