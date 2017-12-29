package i5b5.daniel.serszen.pz.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
@ComponentScan("i5b5.daniel.serszen.pz")
@MapperScan("i5b5.daniel.serszen.pz.model.mybatis.mappers")
public class AppConfig {
    private final String RESOURCE = "i5b5/daniel/serszen/pz/model/mybatis/mappers/mybatis-config.xml";
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(RESOURCE);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}
