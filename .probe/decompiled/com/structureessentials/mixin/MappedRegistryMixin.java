package com.structureessentials.mixin;

import com.structureessentials.StructureEssentials;
import java.util.List;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { MappedRegistry.class }, priority = 10000)
public abstract class MappedRegistryMixin implements Registry {

    @Redirect(method = { "freeze" }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), require = 0)
    private boolean onfreeze(List instance) {
        if (!StructureEssentials.config.getCommonConfig().warnMissingRegistryEntry) {
            return instance.isEmpty();
        } else {
            if (!instance.isEmpty()) {
                StructureEssentials.LOGGER.error("Unbound values in registry " + this.m_123023_() + ": " + instance);
            }
            return true;
        }
    }
}