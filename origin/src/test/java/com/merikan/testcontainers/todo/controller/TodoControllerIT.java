package com.merikan.testcontainers.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merikan.testcontainers.todo.model.Todo;
import com.merikan.testcontainers.todo.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
public class TodoControllerIT {

    private static String basePath;
    private final EasyRandom random = new EasyRandom();
    @LocalServerPort
    protected int localPort;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TodoRepository repository;

    @BeforeAll
    public static void beforeClass() throws Exception {
        basePath = TodoController.class.getAnnotation(RequestMapping.class).value()[0];
    }

    @AfterAll
    public static void afterClass() {
        // nothing
    }

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = localPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBasePath(basePath);
        RestAssured.requestSpecification = requestSpecBuilder.build();
        repository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        RestAssured.reset();
    }

    @Nested
    class Create {
        @Test
        public void shouldCreateAndReturn201() throws Exception {
            Todo todo = random.nextObject(Todo.class);
            //@formatter:off
            String json =
                given()
                    .contentType(ContentType.JSON)
                    .body(todo)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract().body().asString();
            //@formatter:on
            Todo response = objectMapper.readValue(json, Todo.class);
            assertThat(response)
                .usingComparatorForFields((a, b) -> 0, "id")
                .isEqualToComparingFieldByField(todo);
        }

        @Test
        public void shouldFailWhenInvalid() throws Exception {
            Todo todo = random.nextObject(Todo.class);
            todo.setTitle(null);
            //@formatter:off
            given()
                .contentType(ContentType.JSON)
                .body(todo)
            .when()
                .post()
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
            //@formatter:on
        }
    }

    @Nested
    class Delete {
        @Test
        public void delete_shouldDelete() {
            Todo todo = random.nextObject(Todo.class);
            Todo persisted = repository.save(todo);
            //@formatter:off
            given()
                .contentType(ContentType.JSON)
            .when()
                .delete("/{id}", persisted.getId())
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
            //@formatter:on

            assertThat(repository.count()).isEqualTo(0);

        }

        @Test
        public void delete_shouldReturn404WhenMissing() {
            repository.deleteAll();
            //@formatter:off
            given()
                .contentType(ContentType.JSON)
            .when()
                .delete("/{id}", 666)
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
            //@formatter:on

        }
    }

    @Nested
    class GetAll {

        @Test
        public void shouldReturnZero() {
            repository.deleteAll();
            //@formatter:off
            List<Todo> result =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract().body()
                    .jsonPath().getList(".", Todo.class);
            //@formatter:on
            assertThat(result).isEmpty();

        }

        @Test
        public void shouldReturnOne() {
            Todo todo = random.nextObject(Todo.class);
            repository.save(todo);
            //@formatter:off
            List<Todo> result =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract().body()
                    .jsonPath().getList(".", Todo.class);
            //@formatter:on
            assertThat(result).hasSize(1);

        }

        @Test
        public void shouldReturnAll() {
            int numbers = 42;
            Stream<Todo> todos = random.objects(Todo.class, numbers);
            todos.forEach(todo -> repository.save(todo));
            //@formatter:off
            List<Todo> result =
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract().body()
                    .jsonPath().getList(".", Todo.class);
            //@formatter:on
            assertThat(result).hasSize(numbers);

        }
    }

    @Nested
    class Get {
        @Test
        public void shouldReturnFound() {
            Todo todo = random.nextObject(Todo.class);
            Todo persisted = repository.save(todo);
            //@formatter:off
        Todo result =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/{id}", persisted.getId())
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(Todo.class);
        //@formatter:on
            assertThat(result).isNotNull();
            assertThat(result)
                .isEqualToComparingFieldByField(persisted);

        }

        @Test
        public void shouldReturn404WhenNotFound() {
            repository.deleteAll();
            //@formatter:off
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/{id}", "666")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
        //@formatter:on

        }
    }
}
