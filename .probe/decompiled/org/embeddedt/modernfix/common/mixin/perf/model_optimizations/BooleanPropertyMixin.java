package org.embeddedt.modernfix.common.mixin.perf.model_optimizations;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ BooleanProperty.class })
public class BooleanPropertyMixin {

    @Redirect(method = { "equals" }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;equals(Ljava/lang/Object;)Z", remap = false), remap = false)
    private boolean skipEqualityCheck(ImmutableSet instance, Object object) {
        return true;
    }
}