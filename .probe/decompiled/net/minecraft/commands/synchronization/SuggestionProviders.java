package net.minecraft.commands.synchronization;

import com.google.common.collect.Maps;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class SuggestionProviders {

    private static final Map<ResourceLocation, SuggestionProvider<SharedSuggestionProvider>> PROVIDERS_BY_NAME = Maps.newHashMap();

    private static final ResourceLocation DEFAULT_NAME = new ResourceLocation("ask_server");

    public static final SuggestionProvider<SharedSuggestionProvider> ASK_SERVER = register(DEFAULT_NAME, (p_121673_, p_121674_) -> ((SharedSuggestionProvider) p_121673_.getSource()).customSuggestion(p_121673_));

    public static final SuggestionProvider<CommandSourceStack> ALL_RECIPES = register(new ResourceLocation("all_recipes"), (p_121670_, p_121671_) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider) p_121670_.getSource()).getRecipeNames(), p_121671_));

    public static final SuggestionProvider<CommandSourceStack> AVAILABLE_SOUNDS = register(new ResourceLocation("available_sounds"), (p_121667_, p_121668_) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider) p_121667_.getSource()).getAvailableSounds(), p_121668_));

    public static final SuggestionProvider<CommandSourceStack> SUMMONABLE_ENTITIES = register(new ResourceLocation("summonable_entities"), (p_258164_, p_258165_) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ENTITY_TYPE.m_123024_().filter(p_247987_ -> p_247987_.m_245993_(((SharedSuggestionProvider) p_258164_.getSource()).enabledFeatures()) && p_247987_.canSummon()), p_258165_, EntityType::m_20613_, p_212436_ -> Component.translatable(Util.makeDescriptionId("entity", EntityType.getKey(p_212436_)))));

    public static <S extends SharedSuggestionProvider> SuggestionProvider<S> register(ResourceLocation resourceLocation0, SuggestionProvider<SharedSuggestionProvider> suggestionProviderSharedSuggestionProvider1) {
        if (PROVIDERS_BY_NAME.containsKey(resourceLocation0)) {
            throw new IllegalArgumentException("A command suggestion provider is already registered with the name " + resourceLocation0);
        } else {
            PROVIDERS_BY_NAME.put(resourceLocation0, suggestionProviderSharedSuggestionProvider1);
            return new SuggestionProviders.Wrapper(resourceLocation0, suggestionProviderSharedSuggestionProvider1);
        }
    }

    public static SuggestionProvider<SharedSuggestionProvider> getProvider(ResourceLocation resourceLocation0) {
        return (SuggestionProvider<SharedSuggestionProvider>) PROVIDERS_BY_NAME.getOrDefault(resourceLocation0, ASK_SERVER);
    }

    public static ResourceLocation getName(SuggestionProvider<SharedSuggestionProvider> suggestionProviderSharedSuggestionProvider0) {
        return suggestionProviderSharedSuggestionProvider0 instanceof SuggestionProviders.Wrapper ? ((SuggestionProviders.Wrapper) suggestionProviderSharedSuggestionProvider0).name : DEFAULT_NAME;
    }

    public static SuggestionProvider<SharedSuggestionProvider> safelySwap(SuggestionProvider<SharedSuggestionProvider> suggestionProviderSharedSuggestionProvider0) {
        return suggestionProviderSharedSuggestionProvider0 instanceof SuggestionProviders.Wrapper ? suggestionProviderSharedSuggestionProvider0 : ASK_SERVER;
    }

    protected static class Wrapper implements SuggestionProvider<SharedSuggestionProvider> {

        private final SuggestionProvider<SharedSuggestionProvider> delegate;

        final ResourceLocation name;

        public Wrapper(ResourceLocation resourceLocation0, SuggestionProvider<SharedSuggestionProvider> suggestionProviderSharedSuggestionProvider1) {
            this.delegate = suggestionProviderSharedSuggestionProvider1;
            this.name = resourceLocation0;
        }

        public CompletableFuture<Suggestions> getSuggestions(CommandContext<SharedSuggestionProvider> commandContextSharedSuggestionProvider0, SuggestionsBuilder suggestionsBuilder1) throws CommandSyntaxException {
            return this.delegate.getSuggestions(commandContextSharedSuggestionProvider0, suggestionsBuilder1);
        }
    }
}