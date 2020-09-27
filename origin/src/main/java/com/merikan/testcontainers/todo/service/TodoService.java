package com.merikan.testcontainers.todo.service;

import com.merikan.testcontainers.todo.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    Todo save(Todo todo);
    Optional<Todo> findById(Long id);
    List<Todo> findAll();
    void deleteById(Long id);
}
