package com.merikan.testcontainers.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("integrationtest")
class ApplicationIT {

    @Test
    void contextLoads() {
    }

}
