package org.embeddedt.modernfix.forge.mixin.perf.forge_registry_lambda;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { RegistryObject.class }, remap = false)
public class RegistryObjectMixin<T> {

    @Shadow
    @Nullable
    private T value;

    @Shadow
    @Final
    private ResourceLocation name;

    @Overwrite
    public T get() {
        T ret = this.value;
        if (ret == null) {
            throw new NullPointerException("Registry Object not present: " + this.name);
        } else {
            return ret;
        }
    }
}