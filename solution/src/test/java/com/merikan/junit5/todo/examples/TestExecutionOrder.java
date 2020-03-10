package com.merikan.junit5.todo.examples;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestExecutionOrder {
    @Test
    @Order(1)
    void runningFirst() {
        System.out.println("Running first");
    }
    @Test
    @Order(2)
    void runningSecond() {
        System.out.println("Running second");
    }
}
