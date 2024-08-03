package net.minecraftforge.client.event;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterEntitySpectatorShadersEvent extends Event implements IModBusEvent {

    private final Map<EntityType<?>, ResourceLocation> shaders;

    @Internal
    public RegisterEntitySpectatorShadersEvent(Map<EntityType<?>, ResourceLocation> shaders) {
        this.shaders = shaders;
    }

    public void register(EntityType<?> entityType, ResourceLocation shader) {
        this.shaders.put(entityType, shader);
    }
}