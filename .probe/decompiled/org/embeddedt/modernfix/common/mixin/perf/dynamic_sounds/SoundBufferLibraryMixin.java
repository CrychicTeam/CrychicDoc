package org.embeddedt.modernfix.common.mixin.perf.dynamic_sounds;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalNotification;
import com.mojang.blaze3d.audio.SoundBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ SoundBufferLibrary.class })
@ClientOnlyMixin
public abstract class SoundBufferLibraryMixin {

    private static final boolean debugDynamicSoundLoading = Boolean.getBoolean("modernfix.debugDynamicSoundLoading");

    @Shadow
    @Final
    @Mutable
    private Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache = CacheBuilder.newBuilder().expireAfterAccess(300L, TimeUnit.SECONDS).concurrencyLevel(1).removalListener(this::onSoundRemoval).build().asMap();

    private <K extends ResourceLocation, V extends CompletableFuture<SoundBuffer>> void onSoundRemoval(RemovalNotification<K, V> notification) {
        if (notification.getCause() != RemovalCause.REPLACED || notification.getValue() != this.cache.get(notification.getKey())) {
            ((CompletableFuture) notification.getValue()).thenAccept(SoundBuffer::m_83801_);
            if (debugDynamicSoundLoading) {
                K k = (K) notification.getKey();
                if (k != null) {
                    ModernFix.LOGGER.warn("Evicted sound {}", k);
                }
            }
        }
    }
}