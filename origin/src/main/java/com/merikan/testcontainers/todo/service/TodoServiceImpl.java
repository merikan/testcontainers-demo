package com.merikan.testcontainers.todo.service;

import com.merikan.testcontainers.todo.model.Todo;
import com.merikan.testcontainers.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    @Autowired
    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Todo> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
