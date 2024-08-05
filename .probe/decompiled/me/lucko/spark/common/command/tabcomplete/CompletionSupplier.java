package me.lucko.spark.common.command.tabcomplete;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface CompletionSupplier {

    CompletionSupplier EMPTY = partial -> Collections.emptyList();

    static CompletionSupplier startsWith(Collection<String> strings) {
        return strings.isEmpty() ? EMPTY : partial -> (List<String>) strings.stream().filter(startsWithIgnoreCasePredicate(partial)).collect(Collectors.toList());
    }

    static Predicate<String> startsWithIgnoreCasePredicate(String prefix) {
        return string -> string.length() < prefix.length() ? false : string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    List<String> supplyCompletions(String var1);
}