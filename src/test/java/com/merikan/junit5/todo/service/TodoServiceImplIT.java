package com.merikan.junit5.todo.service;

import com.merikan.junit5.todo.model.Todo;
import com.merikan.junit5.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class TodoServiceImplIT {
    private final EasyRandom random = new EasyRandom();

    @Autowired
    private TodoService uut;

    @Autowired
    private TodoRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void save_shouldSave() {
        Todo todo = random.nextObject(Todo.class);
        Todo persisted = uut.save(todo);
        assertThat(persisted).isEqualToIgnoringGivenFields(todo, "id");
        assertThat(repository.findById(persisted.getId())).isNotEmpty();

    }

    @Test
    public void save_shouldThrowExceptionWhenTitleIsEmpty() {
        Todo todo = random.nextObject(Todo.class);
        todo.setTitle("");
        try {
            uut.save(todo);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ConstraintViolationException.class);
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
            assertThat(violations).isNotEmpty();
            assertThat(violations).extracting("interpolatedMessage", "propertyPath.currentLeafNode.name")
                .contains(tuple("must not be blank", "title"));
        }

    }
}
