package com.merikan.testcontainers.todo.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integrationtest")
public abstract class AbstractIntegrationTest {
    private static final DockerImageName MARIADB_IMAGE = DockerImageName.parse("mariadb:10.5.5");
    private static final MariaDBContainer mariadb;

    static {
        mariadb = new MariaDBContainer<>(MARIADB_IMAGE)
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
        mariadb.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }
}
