package com.merikan.junit5.todo.service;

import com.merikan.junit5.todo.model.Todo;
import com.merikan.junit5.todo.repository.TodoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TodoServiceImplTest {

    private TodoServiceImpl uut;
    @Mock
    private TodoRepository repositoryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        uut = new TodoServiceImpl(repositoryMock);
    }

    @Test
    public void save_shouldSave() {
        Todo todo = newTodo();
        Todo persisted = newTodo();
        when(repositoryMock.save(todo)).thenReturn(persisted);
        Todo saved = uut.save(todo);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(persisted.getId());
        verify(repositoryMock, times(1)).save(todo);
    }

    @Test
    @Ignore("This test is ignored")
    public void get_shouldReturn() {
        // ignored test method
    }

    @Test
    public void findAll() {
        // should be implemented
    }

    @Test
    public void deleteById() {
        // should be implemented
    }

    private Todo newTodo() {
        return Todo.builder()
                .title("fix bug")
                .note("a bug was found and we have to fix it")
                .owner("Homer Simpson")
                .build();
    }
}
