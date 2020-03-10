package com.merikan.testcontainers.todo.service;

import com.merikan.testcontainers.todo.model.Todo;
import com.merikan.testcontainers.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = TodoServiceImplStandardIT.Initializer.class)
@ActiveProfiles("integrationtest")
@Transactional
@Testcontainers
public class TodoServiceImplStandardIT {
    private final EasyRandom random = new EasyRandom();

    @Container
    public static MariaDBContainer mariaDB = new MariaDBContainer("mariadb:10.3.6");

    @Autowired
    private TodoService uut;

    @Autowired
    private TodoRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository.deleteAll();
    }

    @Nested
    class Save {
        @Test
        public void shouldSave() {
            Todo todo = random.nextObject(Todo.class);
            Todo persisted = uut.save(todo);
            assertThat(persisted).isEqualToIgnoringGivenFields(todo, "id");
            assertThat(repository.findById(persisted.getId())).isNotEmpty();

        }

        @Test
        public void shouldThrowExceptionWhenTitleIsEmpty() {
            Todo todo = random.nextObject(Todo.class);
            todo.setTitle("");
            ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> {
                uut.save(todo);
            });
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            assertThat(violations).isNotEmpty();
            assertThat(violations).extracting("interpolatedMessage", "propertyPath.currentLeafNode.name")
                .contains(tuple("must not be blank", "title"));

        }
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "spring.datasource.url=" + mariaDB.getJdbcUrl(),
                "spring.datasource.username=" + mariaDB.getUsername(),
                "spring.datasource.password=" + mariaDB.getPassword()
            ).applyTo(context.getEnvironment());
        }
    }

}
