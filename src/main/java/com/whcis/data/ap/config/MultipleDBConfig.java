package com.whcis.data.ap.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MultipleDBConfig {
    @Bean(name = "xychinaDS")
    @ConfigurationProperties(prefix = "spring.ds_xychina")
    public DataSource xychinaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "xychinaJdbcTemplate")
    public JdbcTemplate xychinaJdbcTemplate(@Qualifier("xychinaDS") DataSource xychinaDS) {
        return new JdbcTemplate(xychinaDS);
    }

    @Bean(name = "tempDS")
    @ConfigurationProperties(prefix = "spring.ds_temp")
    public DataSource tempDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tempJdbcTemplate")
    public JdbcTemplate tempJdbcTemplate(@Qualifier("tempDS") DataSource tempDS) {
        return new JdbcTemplate(tempDS);
    }

    @Bean(name = "baseDS")
    @ConfigurationProperties(prefix = "spring.ds_base")
    public DataSource baseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "baseJdbcTemplate")
    public JdbcTemplate baseJdbcTemplate(@Qualifier("baseDS") DataSource baseDS) {
        return new JdbcTemplate(baseDS);
    }


}