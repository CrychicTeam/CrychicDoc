package org.embeddedt.modernfix.common.mixin.feature.cause_lag_by_disabling_threads;

import java.util.concurrent.ExecutorService;
import net.minecraft.Util;
import org.embeddedt.modernfix.util.DirectExecutorService;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Util.class })
public class UtilMixin {

    @Shadow
    @Final
    @Mutable
    private static final ExecutorService BACKGROUND_EXECUTOR = new DirectExecutorService();
}