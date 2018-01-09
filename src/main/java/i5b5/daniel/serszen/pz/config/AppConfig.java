package i5b5.daniel.serszen.pz.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.InputStream;

@Configuration
@ComponentScan("i5b5.daniel.serszen.pz")
@EnableSpringConfigured
@MapperScan("i5b5.daniel.serszen.pz.model.mybatis.mappers")
@PropertySource("file:properties/app.properties")
public class AppConfig {
    private final String RESOURCE = "i5b5/daniel/serszen/pz/model/mybatis/mappers/mybatis-config.xml";
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(RESOURCE);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
