package com.merikan.junit5.todo.examples;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParameterizedTests {

    @ParameterizedTest
    @ValueSource(strings = { "12", "01", "99"})
    void isNumeric(String candidate) {
        assertTrue(StringUtils.isNumeric(candidate));
    }
}
