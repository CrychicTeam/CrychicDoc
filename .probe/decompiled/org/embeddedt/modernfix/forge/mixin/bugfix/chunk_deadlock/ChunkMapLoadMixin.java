package org.embeddedt.modernfix.forge.mixin.bugfix.chunk_deadlock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.lang.reflect.Field;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFix;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ChunkMap.class })
public abstract class ChunkMapLoadMixin {

    private static final Field currentlyLoadingField = ObfuscationReflectionHelper.findField(ChunkHolder.class, "currentlyLoading");

    @Shadow
    @Nullable
    protected abstract ChunkHolder getVisibleChunkIfPresent(long var1);

    private static void setCurrentlyLoading(ChunkHolder holder, LevelChunk value) {
        try {
            currentlyLoadingField.set(holder, value);
        } catch (ReflectiveOperationException var3) {
            var3.printStackTrace();
        }
    }

    @WrapOperation(method = { "*" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;runPostLoad()V") })
    private void setCurrentLoadingThenPostLoad(LevelChunk chunk, Operation<Void> operation) {
        ChunkHolder holder = this.getVisibleChunkIfPresent(chunk.m_7697_().toLong());
        if (holder != null) {
            LevelChunk prevLoading = null;
            try {
                prevLoading = (LevelChunk) currentlyLoadingField.get(holder);
            } catch (ReflectiveOperationException var10) {
                var10.printStackTrace();
            }
            try {
                setCurrentlyLoading(holder, chunk);
                operation.call(new Object[] { chunk });
            } finally {
                setCurrentlyLoading(holder, prevLoading);
            }
        } else {
            ModernFix.LOGGER.warn("Unable to find chunk holder for loading chunk");
            operation.call(new Object[] { chunk });
        }
    }
}