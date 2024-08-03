package net.minecraft.commands;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;

public interface SharedSuggestionProvider {

    Collection<String> getOnlinePlayerNames();

    default Collection<String> getCustomTabSugggestions() {
        return this.getOnlinePlayerNames();
    }

    default Collection<String> getSelectedEntities() {
        return Collections.emptyList();
    }

    Collection<String> getAllTeams();

    Stream<ResourceLocation> getAvailableSounds();

    Stream<ResourceLocation> getRecipeNames();

    CompletableFuture<Suggestions> customSuggestion(CommandContext<?> var1);

    default Collection<SharedSuggestionProvider.TextCoordinates> getRelevantCoordinates() {
        return Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_GLOBAL);
    }

    default Collection<SharedSuggestionProvider.TextCoordinates> getAbsoluteCoordinates() {
        return Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_GLOBAL);
    }

    Set<ResourceKey<Level>> levels();

    RegistryAccess registryAccess();

    FeatureFlagSet enabledFeatures();

    default void suggestRegistryElements(Registry<?> registry0, SharedSuggestionProvider.ElementSuggestionType sharedSuggestionProviderElementSuggestionType1, SuggestionsBuilder suggestionsBuilder2) {
        if (sharedSuggestionProviderElementSuggestionType1.shouldSuggestTags()) {
            suggestResource(registry0.getTagNames().map(TagKey::f_203868_), suggestionsBuilder2, "#");
        }
        if (sharedSuggestionProviderElementSuggestionType1.shouldSuggestElements()) {
            suggestResource(registry0.keySet(), suggestionsBuilder2);
        }
    }

    CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> var1, SharedSuggestionProvider.ElementSuggestionType var2, SuggestionsBuilder var3, CommandContext<?> var4);

    boolean hasPermission(int var1);

    static <T> void filterResources(Iterable<T> iterableT0, String string1, Function<T, ResourceLocation> functionTResourceLocation2, Consumer<T> consumerT3) {
        boolean $$4 = string1.indexOf(58) > -1;
        for (T $$5 : iterableT0) {
            ResourceLocation $$6 = (ResourceLocation) functionTResourceLocation2.apply($$5);
            if ($$4) {
                String $$7 = $$6.toString();
                if (matchesSubStr(string1, $$7)) {
                    consumerT3.accept($$5);
                }
            } else if (matchesSubStr(string1, $$6.getNamespace()) || $$6.getNamespace().equals("minecraft") && matchesSubStr(string1, $$6.getPath())) {
                consumerT3.accept($$5);
            }
        }
    }

    static <T> void filterResources(Iterable<T> iterableT0, String string1, String string2, Function<T, ResourceLocation> functionTResourceLocation3, Consumer<T> consumerT4) {
        if (string1.isEmpty()) {
            iterableT0.forEach(consumerT4);
        } else {
            String $$5 = Strings.commonPrefix(string1, string2);
            if (!$$5.isEmpty()) {
                String $$6 = string1.substring($$5.length());
                filterResources(iterableT0, $$6, functionTResourceLocation3, consumerT4);
            }
        }
    }

    static CompletableFuture<Suggestions> suggestResource(Iterable<ResourceLocation> iterableResourceLocation0, SuggestionsBuilder suggestionsBuilder1, String string2) {
        String $$3 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        filterResources(iterableResourceLocation0, $$3, string2, p_82985_ -> p_82985_, p_82917_ -> suggestionsBuilder1.suggest(string2 + p_82917_));
        return suggestionsBuilder1.buildFuture();
    }

    static CompletableFuture<Suggestions> suggestResource(Stream<ResourceLocation> streamResourceLocation0, SuggestionsBuilder suggestionsBuilder1, String string2) {
        return suggestResource(streamResourceLocation0::iterator, suggestionsBuilder1, string2);
    }

    static CompletableFuture<Suggestions> suggestResource(Iterable<ResourceLocation> iterableResourceLocation0, SuggestionsBuilder suggestionsBuilder1) {
        String $$2 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        filterResources(iterableResourceLocation0, $$2, p_82966_ -> p_82966_, p_82925_ -> suggestionsBuilder1.suggest(p_82925_.toString()));
        return suggestionsBuilder1.buildFuture();
    }

    static <T> CompletableFuture<Suggestions> suggestResource(Iterable<T> iterableT0, SuggestionsBuilder suggestionsBuilder1, Function<T, ResourceLocation> functionTResourceLocation2, Function<T, Message> functionTMessage3) {
        String $$4 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        filterResources(iterableT0, $$4, functionTResourceLocation2, p_82922_ -> suggestionsBuilder1.suggest(((ResourceLocation) functionTResourceLocation2.apply(p_82922_)).toString(), (Message) functionTMessage3.apply(p_82922_)));
        return suggestionsBuilder1.buildFuture();
    }

    static CompletableFuture<Suggestions> suggestResource(Stream<ResourceLocation> streamResourceLocation0, SuggestionsBuilder suggestionsBuilder1) {
        return suggestResource(streamResourceLocation0::iterator, suggestionsBuilder1);
    }

    static <T> CompletableFuture<Suggestions> suggestResource(Stream<T> streamT0, SuggestionsBuilder suggestionsBuilder1, Function<T, ResourceLocation> functionTResourceLocation2, Function<T, Message> functionTMessage3) {
        return suggestResource(streamT0::iterator, suggestionsBuilder1, functionTResourceLocation2, functionTMessage3);
    }

    static CompletableFuture<Suggestions> suggestCoordinates(String string0, Collection<SharedSuggestionProvider.TextCoordinates> collectionSharedSuggestionProviderTextCoordinates1, SuggestionsBuilder suggestionsBuilder2, Predicate<String> predicateString3) {
        List<String> $$4 = Lists.newArrayList();
        if (Strings.isNullOrEmpty(string0)) {
            for (SharedSuggestionProvider.TextCoordinates $$5 : collectionSharedSuggestionProviderTextCoordinates1) {
                String $$6 = $$5.x + " " + $$5.y + " " + $$5.z;
                if (predicateString3.test($$6)) {
                    $$4.add($$5.x);
                    $$4.add($$5.x + " " + $$5.y);
                    $$4.add($$6);
                }
            }
        } else {
            String[] $$7 = string0.split(" ");
            if ($$7.length == 1) {
                for (SharedSuggestionProvider.TextCoordinates $$8 : collectionSharedSuggestionProviderTextCoordinates1) {
                    String $$9 = $$7[0] + " " + $$8.y + " " + $$8.z;
                    if (predicateString3.test($$9)) {
                        $$4.add($$7[0] + " " + $$8.y);
                        $$4.add($$9);
                    }
                }
            } else if ($$7.length == 2) {
                for (SharedSuggestionProvider.TextCoordinates $$10 : collectionSharedSuggestionProviderTextCoordinates1) {
                    String $$11 = $$7[0] + " " + $$7[1] + " " + $$10.z;
                    if (predicateString3.test($$11)) {
                        $$4.add($$11);
                    }
                }
            }
        }
        return suggest($$4, suggestionsBuilder2);
    }

    static CompletableFuture<Suggestions> suggest2DCoordinates(String string0, Collection<SharedSuggestionProvider.TextCoordinates> collectionSharedSuggestionProviderTextCoordinates1, SuggestionsBuilder suggestionsBuilder2, Predicate<String> predicateString3) {
        List<String> $$4 = Lists.newArrayList();
        if (Strings.isNullOrEmpty(string0)) {
            for (SharedSuggestionProvider.TextCoordinates $$5 : collectionSharedSuggestionProviderTextCoordinates1) {
                String $$6 = $$5.x + " " + $$5.z;
                if (predicateString3.test($$6)) {
                    $$4.add($$5.x);
                    $$4.add($$6);
                }
            }
        } else {
            String[] $$7 = string0.split(" ");
            if ($$7.length == 1) {
                for (SharedSuggestionProvider.TextCoordinates $$8 : collectionSharedSuggestionProviderTextCoordinates1) {
                    String $$9 = $$7[0] + " " + $$8.z;
                    if (predicateString3.test($$9)) {
                        $$4.add($$9);
                    }
                }
            }
        }
        return suggest($$4, suggestionsBuilder2);
    }

    static CompletableFuture<Suggestions> suggest(Iterable<String> iterableString0, SuggestionsBuilder suggestionsBuilder1) {
        String $$2 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        for (String $$3 : iterableString0) {
            if (matchesSubStr($$2, $$3.toLowerCase(Locale.ROOT))) {
                suggestionsBuilder1.suggest($$3);
            }
        }
        return suggestionsBuilder1.buildFuture();
    }

    static CompletableFuture<Suggestions> suggest(Stream<String> streamString0, SuggestionsBuilder suggestionsBuilder1) {
        String $$2 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        streamString0.filter(p_82975_ -> matchesSubStr($$2, p_82975_.toLowerCase(Locale.ROOT))).forEach(suggestionsBuilder1::suggest);
        return suggestionsBuilder1.buildFuture();
    }

    static CompletableFuture<Suggestions> suggest(String[] string0, SuggestionsBuilder suggestionsBuilder1) {
        String $$2 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        for (String $$3 : string0) {
            if (matchesSubStr($$2, $$3.toLowerCase(Locale.ROOT))) {
                suggestionsBuilder1.suggest($$3);
            }
        }
        return suggestionsBuilder1.buildFuture();
    }

    static <T> CompletableFuture<Suggestions> suggest(Iterable<T> iterableT0, SuggestionsBuilder suggestionsBuilder1, Function<T, String> functionTString2, Function<T, Message> functionTMessage3) {
        String $$4 = suggestionsBuilder1.getRemaining().toLowerCase(Locale.ROOT);
        for (T $$5 : iterableT0) {
            String $$6 = (String) functionTString2.apply($$5);
            if (matchesSubStr($$4, $$6.toLowerCase(Locale.ROOT))) {
                suggestionsBuilder1.suggest($$6, (Message) functionTMessage3.apply($$5));
            }
        }
        return suggestionsBuilder1.buildFuture();
    }

    static boolean matchesSubStr(String string0, String string1) {
        for (int $$2 = 0; !string1.startsWith(string0, $$2); $$2++) {
            $$2 = string1.indexOf(95, $$2);
            if ($$2 < 0) {
                return false;
            }
        }
        return true;
    }

    public static enum ElementSuggestionType {

        TAGS, ELEMENTS, ALL;

        public boolean shouldSuggestTags() {
            return this == TAGS || this == ALL;
        }

        public boolean shouldSuggestElements() {
            return this == ELEMENTS || this == ALL;
        }
    }

    public static class TextCoordinates {

        public static final SharedSuggestionProvider.TextCoordinates DEFAULT_LOCAL = new SharedSuggestionProvider.TextCoordinates("^", "^", "^");

        public static final SharedSuggestionProvider.TextCoordinates DEFAULT_GLOBAL = new SharedSuggestionProvider.TextCoordinates("~", "~", "~");

        public final String x;

        public final String y;

        public final String z;

        public TextCoordinates(String string0, String string1, String string2) {
            this.x = string0;
            this.y = string1;
            this.z = string2;
        }
    }
}