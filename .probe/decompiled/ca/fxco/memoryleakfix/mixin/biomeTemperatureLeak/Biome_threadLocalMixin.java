package ca.fxco.memoryleakfix.mixin.biomeTemperatureLeak;

import ca.fxco.memoryleakfix.config.MinecraftRequirement;
import ca.fxco.memoryleakfix.config.VersionRange;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.Operation;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import java.util.function.Supplier;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MinecraftRequirement({ @VersionRange(minVersion = "1.14.4") })
@Mixin({ Biome.class })
public abstract class Biome_threadLocalMixin {

    private static ThreadLocal<Long2FloatLinkedOpenHashMap> memoryLeakFix$betterTempCache;

    @WrapOperation(method = { "<init>" }, at = { @At(value = "INVOKE", target = "Ljava/lang/ThreadLocal;withInitial(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;") })
    private ThreadLocal<Long2FloatLinkedOpenHashMap> memoryLeakFix$useStaticThreadLocal(Supplier<?> supplier, Operation<ThreadLocal<Long2FloatLinkedOpenHashMap>> original) {
        if (memoryLeakFix$betterTempCache == null) {
            memoryLeakFix$betterTempCache = original.call(supplier);
        }
        return memoryLeakFix$betterTempCache;
    }
}