package net.minecraft.world.level.storage.loot.providers.score;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.Serializer;

public class ScoreboardNameProviders {

    public static final LootScoreProviderType FIXED = register("fixed", new FixedScoreboardNameProvider.Serializer());

    public static final LootScoreProviderType CONTEXT = register("context", new ContextScoreboardNameProvider.Serializer());

    private static LootScoreProviderType register(String string0, Serializer<? extends ScoreboardNameProvider> serializerExtendsScoreboardNameProvider1) {
        return Registry.register(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE, new ResourceLocation(string0), new LootScoreProviderType(serializerExtendsScoreboardNameProvider1));
    }

    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<ScoreboardNameProvider, LootScoreProviderType>builder(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE, "provider", "type", ScoreboardNameProvider::m_142680_).withInlineSerializer(CONTEXT, new ContextScoreboardNameProvider.InlineSerializer()).build();
    }
}