package io.github.edwinmindcraft.origins.common.condition;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.common.condition.configuration.OriginConfiguration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class OriginCondition extends EntityCondition<OriginConfiguration> {

    public OriginCondition() {
        super(OriginConfiguration.CODEC);
    }

    public boolean check(@NotNull OriginConfiguration configuration, @NotNull Entity entity) {
        return (Boolean) IOriginContainer.get(entity).resolve().map(container -> {
            if (!configuration.origin().isBound()) {
                return false;
            } else {
                return configuration.layer() != null && configuration.layer().isBound() ? configuration.layer().unwrapKey().isPresent() && configuration.origin().is(container.getOrigin((ResourceKey<OriginLayer>) configuration.layer().unwrapKey().get())) : container.getOrigins().values().stream().anyMatch(configuration.origin()::m_203565_);
            }
        }).orElse(false);
    }
}