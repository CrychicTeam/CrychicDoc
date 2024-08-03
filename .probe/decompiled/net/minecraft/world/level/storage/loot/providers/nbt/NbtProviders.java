package net.minecraft.world.level.storage.loot.providers.nbt;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.Serializer;

public class NbtProviders {

    public static final LootNbtProviderType STORAGE = register("storage", new StorageNbtProvider.Serializer());

    public static final LootNbtProviderType CONTEXT = register("context", new ContextNbtProvider.Serializer());

    private static LootNbtProviderType register(String string0, Serializer<? extends NbtProvider> serializerExtendsNbtProvider1) {
        return Registry.register(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, new ResourceLocation(string0), new LootNbtProviderType(serializerExtendsNbtProvider1));
    }

    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<NbtProvider, LootNbtProviderType>builder(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, "provider", "type", NbtProvider::m_142624_).withInlineSerializer(CONTEXT, new ContextNbtProvider.InlineSerializer()).build();
    }
}