package dev.latvian.mods.rhino.util;

import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public record CustomJavaToJsWrapperProviderHolder<T>(Predicate<T> predicate, CustomJavaToJsWrapperProvider<T> provider) {

    @Nullable
    public CustomJavaToJsWrapperProvider<T> create(T object) {
        return this.predicate.test(object) ? this.provider : null;
    }

    public static record PredicateFromClass<T>(Class<T> type) implements Predicate<T> {

        public boolean test(T object) {
            return this.type.isAssignableFrom(object.getClass());
        }
    }
}