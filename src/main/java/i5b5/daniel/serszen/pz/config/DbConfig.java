package i5b5.daniel.serszen.pz.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan("i5b5.daniel.serszen.pz.model.mybatis.mappers")
public class DbConfig {
    @Value("${mybatis.url}")
    private String dsUrl;

    @Value("${mybatis.username}")
    private String dsUser;

    @Value("${mybatis.password}")
    private String dsPass;

    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl(dsUrl);
        dataSource.setUsername(dsUser);
        dataSource.setPassword(dsPass);
        dataSource.setDefaultAutoCommit(false);
        dataSource.setMaxIdle(10);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        org.apache.ibatis.logging.LogFactory.useLog4J2Logging();

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();

        factory.setDataSource(dataSource());
        factory.setConfigLocation(new ClassPathResource("i5b5/daniel/serszen/pz/model/mybatis/mappers/mybatis-config.xml"));

        return factory.getObject();
    }
}
