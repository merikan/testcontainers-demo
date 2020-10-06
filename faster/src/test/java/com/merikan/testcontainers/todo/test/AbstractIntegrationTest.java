package com.merikan.testcontainers.todo.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Testcontainers
@Slf4j
public abstract class AbstractIntegrationTest {

    static final MariaDBContainer mariadb1;
    static final MariaDBContainer mariadb2;
    static final GenericContainer redis;
    static final GenericContainer sftp;

    static {
        Instant start = Instant.now();

        mariadb1 = (MariaDBContainer) new MariaDBContainer("mariadb:10.5.5")
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withReuse(true)
            .withLabel("reuse.UUID", "e06d7a87-7d7d-472e-a047-e6c81f61d2a4");
        mariadb2 = (MariaDBContainer) new MariaDBContainer("mariadb:10.5.5")
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withReuse(true)
            .withLabel("reuse.UUID", "282b8993-097c-4fd4-98f1-94daf3466dd6");
        redis = new GenericContainer("redis:5.0.5")
            .withExposedPorts(6379)
            .withReuse(true)
            .withLabel("reuse.UUID", "0429783b-c855-4b32-8239-258cba232b63");
        sftp = new GenericContainer("atmoz/sftp:latest")
            .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"))
            .withReuse(true)
            .withLabel("reuse.UUID", "0293a405-e435-4f03-9e4b-b6160d9e60fe");
        Stream.of(mariadb1, mariadb2, redis, sftp).parallel().forEach(GenericContainer::start);

        log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb1::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb1::getUsername);
        registry.add("spring.datasource.password", mariadb1::getPassword);
        //....
    }
}
