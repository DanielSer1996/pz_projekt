package i5b5.daniel.serszen.pz.model.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LoginMapper {
    String getEncryptedPassByLogin(@Param("login") String login);

    void insertAdminUser(@Param("login") String login,
                         @Param("password") String password);
}
