package com.laa66.goodboi.repository;

import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@DataR2dbcTest
@Testcontainers
public class PostgreSQLBaseContainer {

    @Container
    protected final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest") .withDatabaseName("test")
            .withUsername("username")
            .withPassword("password")
            .withInitScript("user_repository_test_data.sql");;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" +
                postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

}
