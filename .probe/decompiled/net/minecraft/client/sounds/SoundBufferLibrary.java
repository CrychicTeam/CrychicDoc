package net.minecraft.client.sounds;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.audio.SoundBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

public class SoundBufferLibrary {

    private final ResourceProvider resourceManager;

    private final Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache = Maps.newHashMap();

    public SoundBufferLibrary(ResourceProvider resourceProvider0) {
        this.resourceManager = resourceProvider0;
    }

    public CompletableFuture<SoundBuffer> getCompleteBuffer(ResourceLocation resourceLocation0) {
        return (CompletableFuture<SoundBuffer>) this.cache.computeIfAbsent(resourceLocation0, p_120208_ -> CompletableFuture.supplyAsync(() -> {
            try {
                InputStream $$1 = this.resourceManager.open(p_120208_);
                SoundBuffer var5;
                try {
                    OggAudioStream $$2 = new OggAudioStream($$1);
                    try {
                        ByteBuffer $$3 = $$2.readAll();
                        var5 = new SoundBuffer($$3, $$2.getFormat());
                    } catch (Throwable var8) {
                        try {
                            $$2.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                        throw var8;
                    }
                    $$2.close();
                } catch (Throwable var9) {
                    if ($$1 != null) {
                        try {
                            $$1.close();
                        } catch (Throwable var6) {
                            var9.addSuppressed(var6);
                        }
                    }
                    throw var9;
                }
                if ($$1 != null) {
                    $$1.close();
                }
                return var5;
            } catch (IOException var10) {
                throw new CompletionException(var10);
            }
        }, Util.backgroundExecutor()));
    }

    public CompletableFuture<AudioStream> getStream(ResourceLocation resourceLocation0, boolean boolean1) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream $$2 = this.resourceManager.open(resourceLocation0);
                return (AudioStream) (boolean1 ? new LoopingAudioStream(OggAudioStream::new, $$2) : new OggAudioStream($$2));
            } catch (IOException var4) {
                throw new CompletionException(var4);
            }
        }, Util.backgroundExecutor());
    }

    public void clear() {
        this.cache.values().forEach(p_120201_ -> p_120201_.thenAccept(SoundBuffer::m_83801_));
        this.cache.clear();
    }

    public CompletableFuture<?> preload(Collection<Sound> collectionSound0) {
        return CompletableFuture.allOf((CompletableFuture[]) collectionSound0.stream().map(p_120197_ -> this.getCompleteBuffer(p_120197_.getPath())).toArray(CompletableFuture[]::new));
    }
}