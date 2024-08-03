package dev.architectury.extensions.injected;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;

public interface InjectedEntityTypeExtension extends InjectedRegistryEntryExtension<EntityType<?>> {

    @Override
    default Holder<EntityType<?>> arch$holder() {
        return ((EntityType) this).builtInRegistryHolder();
    }
}