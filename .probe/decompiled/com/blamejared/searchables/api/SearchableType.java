package com.blamejared.searchables.api;

import com.blamejared.searchables.api.autcomplete.CompletionSuggestion;
import com.blamejared.searchables.api.context.ContextVisitor;
import com.blamejared.searchables.api.context.SearchContext;
import com.blamejared.searchables.lang.StringSearcher;
import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public final class SearchableType<T> {

    private final Map<String, SearchableComponent<T>> components;

    @Nullable
    private final SearchableComponent<T> defaultComponent;

    private SearchableType(Map<String, SearchableComponent<T>> components, @Nullable SearchableComponent<T> defaultComponent) {
        this.components = components;
        this.defaultComponent = defaultComponent;
    }

    public Map<String, SearchableComponent<T>> components() {
        return this.components;
    }

    public Optional<SearchableComponent<T>> component(String key) {
        return Optional.ofNullable((SearchableComponent) this.components.get(key));
    }

    public Optional<SearchableComponent<T>> defaultComponent() {
        return Optional.ofNullable(this.defaultComponent);
    }

    public List<CompletionSuggestion> getSuggestionsFor(List<T> entries, String currentToken, int position, TokenRange replacementRange) {
        TokenRange suggestionRange = replacementRange.rangeAtPosition(position);
        String suggestionFrom = suggestionRange.substring(currentToken, position);
        if (!replacementRange.contains(position)) {
            return List.of();
        } else {
            int suggestionIndex = replacementRange.rangeIndexAtPosition(position);
            return switch(suggestionIndex) {
                case 0 ->
                    this.getSuggestionsForComponent(suggestionFrom, replacementRange.simplify());
                case 1 ->
                    this.getSuggestionsForTerm(entries, replacementRange.range(0).substring(currentToken), "", replacementRange.simplify());
                case 2 ->
                    this.getSuggestionsForTerm(entries, replacementRange.range(0).substring(currentToken), suggestionFrom, replacementRange.simplify());
                default ->
                    List.of();
            };
        }
    }

    public List<CompletionSuggestion> getSuggestionsForComponent(String componentName, TokenRange replacementRange) {
        return (List<CompletionSuggestion>) this.components().keySet().stream().filter(s -> StringUtils.startsWithIgnoreCase(s, componentName)).sorted(Comparator.naturalOrder()).map(s -> new CompletionSuggestion(s, Component.literal(s), ":", replacementRange)).distinct().collect(Collectors.toList());
    }

    public List<CompletionSuggestion> getSuggestionsForTerm(List<T> entries, String componentName, String current, TokenRange replacementRange) {
        Function<T, Optional<String>> mapper = (Function<T, Optional<String>>) this.component(componentName).map(SearchableComponent::getToString).orElseGet(() -> t -> Optional.empty());
        boolean startsWithQuote = !current.isEmpty() && CharMatcher.anyOf("`'\"").matches(current.charAt(0));
        String termString = startsWithQuote ? current.substring(1) : current;
        return (List<CompletionSuggestion>) entries.stream().map(mapper).filter(Optional::isPresent).map(Optional::get).filter(s -> StringUtils.startsWithIgnoreCase(s, termString)).sorted(Comparator.naturalOrder()).map(SearchablesConstants.QUOTE).map(s -> new CompletionSuggestion(componentName + ":" + s, Component.literal(s), " ", replacementRange)).distinct().collect(Collectors.toList());
    }

    public List<T> filterEntries(List<T> entries, String search) {
        return this.filterEntries(entries, search, t -> true);
    }

    public List<T> filterEntries(List<T> entries, String search, Predicate<T> extraPredicate) {
        Optional<SearchContext<T>> context = StringSearcher.search(search, new ContextVisitor<>());
        return entries.stream().filter(((Predicate) context.map(tSearchContext -> tSearchContext.createPredicate(this)).orElse((Predicate) t -> true)).and(extraPredicate)).toList();
    }

    public static class Builder<T> {

        private final com.google.common.collect.ImmutableMap.Builder<String, SearchableComponent<T>> components = ImmutableMap.builder();

        @Nullable
        private SearchableComponent<T> defaultComponent = null;

        public SearchableType.Builder<T> component(SearchableComponent<T> component) {
            return this.component(component.key(), component);
        }

        public SearchableType.Builder<T> component(String key, SearchableComponent<T> component) {
            this.components.put(key, component);
            return this;
        }

        public SearchableType.Builder<T> defaultComponent(SearchableComponent<T> component) {
            return this.defaultComponent(component.key(), component);
        }

        public SearchableType.Builder<T> defaultComponent(String key, SearchableComponent<T> component) {
            if (this.defaultComponent != null) {
                throw new IllegalStateException("Cannot mark multiple components as a default component!");
            } else {
                this.components.put(key, component);
                this.defaultComponent = component;
                return this;
            }
        }

        public SearchableType<T> build() {
            return new SearchableType<>(this.components.buildOrThrow(), this.defaultComponent);
        }
    }
}