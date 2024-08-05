package net.minecraftforge.client.event;

import com.google.common.base.Preconditions;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.textures.ITextureAtlasSpriteLoader;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterTextureAtlasSpriteLoadersEvent extends Event implements IModBusEvent {

    private final Map<ResourceLocation, ITextureAtlasSpriteLoader> loaders;

    @Internal
    public RegisterTextureAtlasSpriteLoadersEvent(Map<ResourceLocation, ITextureAtlasSpriteLoader> loaders) {
        this.loaders = loaders;
    }

    public void register(String name, ITextureAtlasSpriteLoader loader) {
        ResourceLocation key = new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), name);
        Preconditions.checkArgument(!this.loaders.containsKey(key), "Sprite loader already registered: " + key);
        this.loaders.put(key, loader);
    }
}