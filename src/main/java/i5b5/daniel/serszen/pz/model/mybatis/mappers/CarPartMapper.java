package i5b5.daniel.serszen.pz.model.mybatis.mappers;

import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarPartMapper {
    List<CarPart> getAllCarPartsForCar(@Param("car")Car car);
    void deleteCarPartsByCategoryNameAndProducer(@Param("category") String category,@Param("name") String name,@Param("producer") String producer);
    void deleteCarPartsByCategoryAndName(@Param("category") String category,@Param("name") String name);
    void deleteCarPartsByCategory(@Param("category") String producer);
    void cascadeDelete(@Param("id")long id);

    void insertCarPart(@Param("carPart") CarPart carPart);

    void insertCarPartRel(@Param("car") Car car, @Param("carPart") CarPart carPart);

    void insertCategory(@Param("name") String name, @Param("categoryDescriptionText") String categoryDescriptionText);

    List<CarPart> getAllCarParts();
}
