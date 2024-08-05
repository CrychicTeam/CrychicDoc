package org.embeddedt.modernfix.common.mixin.perf.dynamic_dfu;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DSL.TypeReference;
import java.util.Set;
import net.minecraft.util.datafix.DataFixers;
import org.embeddedt.modernfix.dfu.LazyDataFixer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ DataFixers.class })
public abstract class DataFixersMixin {

    private static LazyDataFixer lazyDataFixer;

    @Shadow
    protected static DataFixer createFixerUpper(Set<TypeReference> set) {
        throw new AssertionError();
    }

    @Inject(method = { "createFixerUpper" }, at = { @At("HEAD") }, cancellable = true)
    private static void createLazyFixerUpper(Set<TypeReference> set, CallbackInfoReturnable<DataFixer> cir) {
        if (lazyDataFixer == null) {
            lazyDataFixer = new LazyDataFixer(() -> createFixerUpper(set));
            cir.setReturnValue(lazyDataFixer);
        }
    }
}