package org.violetmoon.quark.mixin.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;

@Mixin({ RegistrySetBuilder.BuildState.class })
public class RegistrySetBuilderHackMixin {

    @WrapOperation(method = { "reportRemainingUnreferencedValues" }, at = { @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0) })
    public <E> boolean quark$preventInvalidGWBiome(List<RuntimeException> instance, E error, Operation<Boolean> original, @Local ResourceKey<Object> resourcekey) {
        return resourcekey.location().equals(GlimmeringWealdModule.BIOME_NAME) ? false : (Boolean) original.call(new Object[] { instance, error });
    }
}