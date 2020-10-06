package com.merikan.testcontainers.todo.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayNameGenerator;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * {@code DisplayNameGenerator} that replaces underscores with spaces.
 *
 * <p>This generator extends the functionality of {@link Standard} by
 * replacing all underscores ({@code '_'}) found in class and method names
 * with spaces ({@code ' '}) and splitting all camel case found in nested class
 * and method names, inserts a space ({@code ' '}) and change capital letter
 * to lower case.
 */
public  class ReplaceCamelCaseAndUnderScores extends DisplayNameGenerator.Standard {
    public ReplaceCamelCaseAndUnderScores() {
    }
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceUnderscores(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replace(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        String displayName = replace(testMethod.getName());
        if (hasParameters(testMethod)) {
            displayName += ' ' + DisplayNameGenerator.parameterTypesAsString(testMethod);
        }
        return displayName;
    }

    private static String replace(String name) {
        return replaceCamelCase(replaceUnderscores(name));
    }

    private static String replaceUnderscores(String name) {
        return name.replace('_', ' ');
    }

    private static String replaceCamelCase(String camelCase) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(camelCase);
        words = Arrays.stream(words).filter(w -> !w.equals(" ")).toArray(String[]::new);
        return String
            .join(" ", words)
            .toLowerCase();
    }

    private static boolean hasParameters(Method method) {
        return method.getParameterCount() > 0;
    }
}
