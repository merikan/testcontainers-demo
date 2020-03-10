package com.merikan.junit5.todo.examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTestExecution {

    @Test
    @EnabledOnOs({ OS.WINDOWS, OS.MAC })
    void onWindowsAndMac() {
        // ...
    }

    @Test
    @DisabledOnOs({ OS.LINUX, OS.SOLARIS })
    void disabledOnLinuxAndSolaris() {
        // ...
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    void onlyOn64BitArchitectures() {
        // ...
    }
}
