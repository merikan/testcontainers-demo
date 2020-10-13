package com.merikan.testcontainers.todo.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Slf4j
public abstract class AbstractIntegrationTest {

    private static final DockerImageName MARIADB_IMAGE = DockerImageName.parse("mariadb:10.5.5");
    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:5.0.5");
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:5.2.1");
    private static final DockerImageName SFTP_IMAGE = DockerImageName.parse("atmoz/sftp@sha256:975dc193e066e4226a3c4299536bb6c3d98cec27d06583055c25ccbdc30d0b61");

    private static final MariaDBContainer mariadb1;
    private static final MariaDBContainer mariadb2;
    private static final GenericContainer redis;
    private static final KafkaContainer kafka;
    private static final GenericContainer sftp;

    static {
        Instant start = Instant.now();

        // start in parallel and with reuse
        mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withReuse(true)
            .withLabel("reuse.UUID", "e06d7a87-7d7d-472e-a047-e6c81f61d2a4");
        mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withReuse(true)
            .withLabel("reuse.UUID", "282b8993-097c-4fd4-98f1-94daf3466dd6");
        redis = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379)
            .withReuse(true)
            .withLabel("reuse.UUID", "0429783b-c855-4b32-8239-258cba232b63");
        kafka = new KafkaContainer(KAFKA_IMAGE)
            .withReuse(true)
            .withLabel("reuse.UUID", "f8724ec0-2f66-4684-80cd-1b24a7399366");
        sftp = new GenericContainer<>(SFTP_IMAGE)
            .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"))
            .withReuse(true)
            .withLabel("reuse.UUID", "0293a405-e435-4f03-9e4b-b6160d9e60fe");
        Stream.of(mariadb1, mariadb2, redis, kafka, sftp).parallel().forEach(GenericContainer::start);

        log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));

        // start in sequence
//        mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
//            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
//        mariadb1.start();
//        mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
//            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
//        mariadb2.start();
//        redis = new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);
//        redis.start();
//        kafka = new KafkaContainer(KAFKA_IMAGE);
//        kafka.start();
//        sftp = new GenericContainer<>(SFTP_IMAGE)
//            .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"));
//        sftp.start();

        // start in parallel
//        mariadb1 = new MariaDBContainer<>(MARIADB_IMAGE)
//            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
//        mariadb2 = new MariaDBContainer<>(MARIADB_IMAGE)
//            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
//        redis = new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);
//        kafka = new KafkaContainer(KAFKA_IMAGE);
//        sftp = new GenericContainer<>(SFTP_IMAGE)
//            .withCommand(String.format("%s:%s:::upload", "SFTP_USER", "SFTP_PASSWORD"));
//        Stream.of(mariadb1, mariadb2, redis, kafka, sftp).parallel().forEach(GenericContainer::start);

    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb1::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb1::getUsername);
        registry.add("spring.datasource.password", mariadb1::getPassword);
        //.... and the others come here
    }
}
