package com.blamejared.searchables.api;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class SearchableComponent<T> {

    private final String key;

    private final Function<T, Optional<String>> toString;

    private final BiPredicate<T, String> filter;

    private SearchableComponent(String key, Function<T, Optional<String>> toString, BiPredicate<T, String> filter) {
        this.key = key;
        this.toString = toString.andThen(s -> s.filter(SearchablesConstants.VALID_SUGGESTION));
        this.filter = filter;
    }

    public static <T> SearchableComponent<T> create(String key, BiPredicate<T, String> filter) {
        return create(key, t -> Optional.empty(), filter);
    }

    public static <T> SearchableComponent<T> create(String key, Function<T, Optional<String>> toString, BiPredicate<T, String> filter) {
        return new SearchableComponent<>(key, toString, filter);
    }

    public static <T> SearchableComponent<T> create(String key, Function<T, Optional<String>> toString) {
        return new SearchableComponent<>(key, toString, (t, search) -> (Boolean) ((Optional) toString.apply(t)).map(tStr -> StringUtils.containsIgnoreCase(tStr, search)).orElse(false));
    }

    public String key() {
        return this.key;
    }

    public BiPredicate<T, String> filter() {
        return this.filter;
    }

    public Function<T, Optional<String>> getToString() {
        return this.toString;
    }

    public String toString() {
        return new StringJoiner(", ", SearchableComponent.class.getSimpleName() + "[", "]").add("key='" + this.key + "'").toString();
    }
}