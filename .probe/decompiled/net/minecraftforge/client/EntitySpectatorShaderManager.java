package net.minecraftforge.client;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.RegisterEntitySpectatorShadersEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class EntitySpectatorShaderManager {

    private static Map<EntityType<?>, ResourceLocation> SHADERS;

    @Nullable
    public static ResourceLocation get(EntityType<?> entityType) {
        return (ResourceLocation) SHADERS.get(entityType);
    }

    @Internal
    public static void init() {
        HashMap<EntityType<?>, ResourceLocation> shaders = new HashMap();
        RegisterEntitySpectatorShadersEvent event = new RegisterEntitySpectatorShadersEvent(shaders);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        SHADERS = ImmutableMap.copyOf(shaders);
    }

    private EntitySpectatorShaderManager() {
    }
}