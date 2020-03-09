package com.merikan.testcontainers.todo.controller;


import com.merikan.testcontainers.todo.model.Todo;
import com.merikan.testcontainers.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService service;

    @Autowired
    public TodoController(TodoService service) {
        this.service = service;
    }

    /**
     * GET /todos -> get all todos
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        log.debug("getAll()");
        List<Todo> list = service.findAll();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * GET /todos/{id} -> get todo by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable("id") Long id) {
        log.debug("get(id: {})", id);
        Optional<Todo> todo = service.findById(id);
        return todo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST  /todos : Create a new Todo
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@Valid  @RequestBody Todo todo) {
        log.debug("create(todo: {})", todo);
        Todo created = service.save(todo);
        return created;
    }
    /**
     * DELETE  /todos/{id} : Delete todo by id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        log.debug("delete(id: {})", id);
        return service.findById(id)
                .map(todo -> {
                    service.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet( () -> ResponseEntity.notFound().build() );
    }

}
