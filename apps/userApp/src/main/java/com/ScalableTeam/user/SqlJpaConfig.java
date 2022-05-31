package com.ScalableTeam.user;

import com.ScalableTeam.services.managers.BaseDatabasePooling;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.ScalableTeam.user.repositories",
        transactionManagerRef = "sqlTransactionManager"
)
@Slf4j
public class SqlJpaConfig extends BaseDatabasePooling {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource-sql")
    public DataSourceProperties sqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource-sql.configuration")
    public HikariConfig sqlHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public JdbcTemplate sqlJdbcTemplate() {
        return new JdbcTemplate(sqlDataSource());
    }

    @Primary
    @Bean
    public DataSource sqlDataSource() {
        HikariConfig hikariConfig = sqlHikariConfig();
        HikariDataSource dataSource = sqlDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        dataSource.setMaximumPoolSize(hikariConfig.getMaximumPoolSize());
        dataSource.setMinimumIdle(hikariConfig.getMinimumIdle());
        dataSource.setMaxLifetime(hikariConfig.getMaxLifetime());
        dataSource.setConnectionTimeout(hikariConfig.getConnectionTimeout());
        dataSource.setPoolName(hikariConfig.getPoolName());
        dataSource.setAutoCommit(hikariConfig.isAutoCommit());
        return dataSource;
    }


    @Primary
    @Bean
    public PlatformTransactionManager sqlTransactionManager(
            EntityManagerFactory sqlEntityManagerFactory) {
        return new JpaTransactionManager(sqlEntityManagerFactory);
    }

    @Override
    public void changeMaxPoolSize(int max) {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        hikari.setMaximumPoolSize(max);
        log.info("Change Max Pool Size to {} for Hikari Data Source {} with url {}", max, hikari, hikari.getJdbcUrl());
    }

    @Override
    public boolean canChangeMaxPoolSize() {
        return true;
    }

    @Override
    public void changeMaxConnectionTimeout(long max) {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        hikari.setConnectionTimeout(max);
        log.info("Change Max Connection Timeout to {} for Hikari Data Source {} with url {}", max, hikari, hikari.getJdbcUrl());
    }

    @Override
    public boolean canChangeMaxConnectionTimeout() {
        return true;
    }

    @Override
    public void changeMinIdleConnectionSize(int min) {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        hikari.setMinimumIdle(min);
        log.info("Change Min Idle Connection Size to {} for Hikari Data Source {} with url {}", min, hikari, hikari.getJdbcUrl());
    }

    @Override
    public boolean canChangeMinIdleConnectionSize() {
        return true;
    }

    @Override
    public void changeMaxLifetime(long max) {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        hikari.setMaxLifetime(max);
        log.info("Change Max Lifetime to {} for Hikari Data Source {} with url {}", max, hikari, hikari.getJdbcUrl());
    }

    @Override
    public boolean canChangeMaxLifetime() {
        return true;
    }

    @Override
    public void changePoolName(String pool) {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        hikari.setPoolName(pool);
        log.info("Change Pool Name to {} for Hikari Data Source {} with url {}", pool, hikari, hikari.getJdbcUrl());
    }

    @Override
    public String getPoolName() {
        DataSource ds = sqlDataSource();
        HikariDataSource hikari = (HikariDataSource) ds;
        return hikari.getPoolName();
    }
}