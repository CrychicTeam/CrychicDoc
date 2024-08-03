package net.minecraftforge.client.textures;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterTextureAtlasSpriteLoadersEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class TextureAtlasSpriteLoaderManager {

    private static ImmutableMap<ResourceLocation, ITextureAtlasSpriteLoader> LOADERS;

    @Nullable
    public static ITextureAtlasSpriteLoader get(ResourceLocation name) {
        return (ITextureAtlasSpriteLoader) LOADERS.get(name);
    }

    @Internal
    public static void init() {
        HashMap<ResourceLocation, ITextureAtlasSpriteLoader> loaders = new HashMap();
        RegisterTextureAtlasSpriteLoadersEvent event = new RegisterTextureAtlasSpriteLoadersEvent(loaders);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        LOADERS = ImmutableMap.copyOf(loaders);
    }

    private TextureAtlasSpriteLoaderManager() {
    }
}