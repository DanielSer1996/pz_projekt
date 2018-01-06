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
    List<Car> getAllCars();
}
