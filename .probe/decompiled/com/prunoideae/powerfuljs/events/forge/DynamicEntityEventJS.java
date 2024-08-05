package com.prunoideae.powerfuljs.events.forge;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import com.prunoideae.powerfuljs.forge.CapabilityServiceForge;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;

public class DynamicEntityEventJS extends DynamicAttachEventJS<Entity> {

    @Override
    public DynamicAttachEventJS<Entity> add(Predicate<Entity> predicate, CapabilityBuilderForge<Entity, ?> provider) {
        CapabilityServiceForge.INSTANCE.addEntity(predicate, provider);
        return this;
    }
}