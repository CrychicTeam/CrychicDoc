package com.structureessentials.mixin;

import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { LegacyRandomSource.class }, priority = 5)
public class LegacyRandomSourceMixin {

    @Redirect(method = { "next" }, at = @At(value = "INVOKE", target = "Ljava/util/concurrent/atomic/AtomicLong;compareAndSet(JJ)Z"), require = 0)
    private boolean on(AtomicLong instance, long expectedValue, long newValue) {
        instance.compareAndSet(expectedValue, newValue);
        return true;
    }
}