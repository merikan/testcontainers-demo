package com.merikan.junit5.todo.service;

import com.merikan.junit5.todo.model.Todo;
import com.merikan.junit5.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class TodoServiceImplIT {
    private final EasyRandom random = new EasyRandom();

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
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
            assertThat(violations).isNotEmpty();
            assertThat(violations).extracting("interpolatedMessage", "propertyPath.currentLeafNode.name")
                .contains(tuple("must not be blank", "title"));

        }
    }
}
