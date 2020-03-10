package com.merikan.junit5.todo.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Probably the best tests ever!")
public class DisplayNameTests {

    @Test
    @DisplayName("What an awesome test")
    void create() {
        //
    }

    @Test
    @DisplayName("another super test")
    void delete() {
        //
    }

}
