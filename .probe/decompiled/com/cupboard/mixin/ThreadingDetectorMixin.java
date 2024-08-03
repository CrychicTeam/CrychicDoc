package com.cupboard.mixin;

import java.util.stream.Stream;
import net.minecraft.util.ThreadingDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ThreadingDetector.class })
public class ThreadingDetectorMixin {

    @Redirect(method = { "makeThreadingException" }, at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;"))
    private static <T> Stream<T> cupboard$writeAllThreads(T[] values) {
        return Thread.getAllStackTraces().keySet().stream();
    }
}