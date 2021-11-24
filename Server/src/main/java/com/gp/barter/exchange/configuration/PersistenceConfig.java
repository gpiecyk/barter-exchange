package com.gp.barter.exchange.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan(basePackages = "com.gp.barter.exchange")
@PropertySource({"classpath:persistence-postgres.properties"})
@EnableTransactionManagement
public class PersistenceConfig {

    @Autowired
    private Environment env;

    @Bean
    public JpaTransactionManager JpaTransactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPersistenceUnitName(Objects.requireNonNull(env.getProperty("persistence.unit.name")));
        LoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        factoryBean.setLoadTimeWeaver(loadTimeWeaver);
        return factoryBean;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(Objects.requireNonNull(env.getProperty("jdbc.url")));
        dataSource.setUsername(Objects.requireNonNull(env.getProperty("jdbc.user")));
        dataSource.setPassword(Objects.requireNonNull(env.getProperty("jdbc.pass")));
        return dataSource;
    }
}
