package com.merikan.testcontainers.todo.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integrationtest")
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
@Testcontainers
public abstract class AbstractIntegrationTest {

    static final MariaDBContainer mariadb;

    static {
        mariadb = new MariaDBContainer("mariadb:10.3.6");
        mariadb.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "spring.datasource.url=" + mariadb.getJdbcUrl(),
                "spring.datasource.username=" + mariadb.getUsername(),
                "spring.datasource.password=" + mariadb.getPassword()
            ).applyTo(context.getEnvironment());
        }
    }
}
