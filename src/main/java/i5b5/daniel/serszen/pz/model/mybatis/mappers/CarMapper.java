package i5b5.daniel.serszen.pz.model.mybatis.mappers;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarMapper {
    List<Car> getCarsByBrand(@Param("brand") String brand);
    List<Car> getCarsByModel(@Param("model") String model);
    List<Car> getAllCars();

    void deleteCarsByBrand(@Param("brand") String brand);
    void deleteCarsByBrandAndModel(@Param("brand")String brand ,@Param("model")String model);
    void deleteCarsByBrandModelAndProductionDate(@Param("brand")String brand,
                                                 @Param("model")String model,
                                                 @Param("productionStartDate")String productionStartDate,
                                                 @Param("productionEndDate")String productionEndDate);

    void insertCar(@Param("car") Car car);
}
