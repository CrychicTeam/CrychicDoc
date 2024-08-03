package me.shedaniel.clothconfig2.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@FunctionalInterface
@Experimental
public interface Requirement {

    boolean check();

    @SafeVarargs
    static <T> Requirement isValue(ValueHolder<T> dependency, @Nullable T firstValue, @Nullable T... otherValues) {
        Set<T> values = (Set<T>) Stream.concat(Stream.of(firstValue), Arrays.stream(otherValues)).collect(Collectors.toCollection(HashSet::new));
        return () -> values.contains(dependency.getValue());
    }

    static <T> Requirement matches(ValueHolder<T> firstDependency, ValueHolder<T> secondDependency) {
        return () -> Objects.equals(firstDependency.getValue(), secondDependency.getValue());
    }

    static Requirement isTrue(ValueHolder<Boolean> dependency) {
        return () -> Boolean.TRUE.equals(dependency.getValue());
    }

    static Requirement isFalse(ValueHolder<Boolean> dependency) {
        return () -> Boolean.FALSE.equals(dependency.getValue());
    }

    static Requirement not(Requirement requirement) {
        return () -> !requirement.check();
    }

    static Requirement all(Requirement... requirements) {
        return () -> Arrays.stream(requirements).allMatch(Requirement::check);
    }

    static Requirement any(Requirement... requirements) {
        return () -> Arrays.stream(requirements).anyMatch(Requirement::check);
    }

    static Requirement none(Requirement... requirements) {
        return () -> Arrays.stream(requirements).noneMatch(Requirement::check);
    }

    static Requirement one(Requirement... requirements) {
        return () -> {
            boolean oneFound = false;
            for (Requirement requirement : requirements) {
                if (requirement.check()) {
                    if (oneFound) {
                        return false;
                    }
                    oneFound = true;
                }
            }
            return oneFound;
        };
    }
}