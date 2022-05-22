package com.ScalableTeam.reddit.config;

import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.ScalableTeam.reddit.app.repository.vote",
        entityManagerFactoryRef = "sqlEntityManagerFactory",
        transactionManagerRef = "sqlTransactionManager"
)
public class SqlJpaConfig {

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
    public LocalContainerEntityManagerFactoryBean sqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(sqlDataSource())
                .packages(PostVote.class)
                .persistenceUnit("sql")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager sqlTransactionManager(
            @Qualifier("sqlEntityManagerFactory")
                    EntityManagerFactory sqlEntityManagerFactory) {
        return new JpaTransactionManager(sqlEntityManagerFactory);
    }
}