package com.zcx.demo.shiroweb.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//@Configuration
//@ConfigurationProperties("spring.datasource.druid")
public class DataSourceConfig {

//    @Bean
    public DataSource dataSource(){
        return new DruidDataSource();
    }
}
