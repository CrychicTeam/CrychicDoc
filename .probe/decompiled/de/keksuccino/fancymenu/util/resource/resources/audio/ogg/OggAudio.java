package de.keksuccino.fancymenu.util.resource.resources.audio.ogg;

import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import de.keksuccino.melody.resources.audio.openal.ALAudioBuffer;
import de.keksuccino.melody.resources.audio.openal.ALAudioClip;
import de.keksuccino.melody.resources.audio.openal.ALUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OggAudio implements IAudio {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    protected volatile ALAudioClip clip;

    @Nullable
    protected volatile ALAudioBuffer audioBuffer;

    protected ResourceLocation sourceLocation;

    protected File sourceFile;

    protected String sourceURL;

    protected volatile boolean decoded = false;

    protected volatile boolean loadingCompleted = false;

    protected volatile boolean loadingFailed = false;

    protected volatile boolean closed = false;

    @NotNull
    public static OggAudio location(@NotNull ResourceLocation location) {
        return location(location, null);
    }

    @NotNull
    public static OggAudio location(@NotNull ResourceLocation location, @Nullable OggAudio writeTo) {
        Objects.requireNonNull(location);
        OggAudio audio = writeTo != null ? writeTo : new OggAudio();
        audio.sourceLocation = location;
        RenderSystem.assertOnRenderThread();
        if (!ALUtils.isOpenAlReady()) {
            audio.loadingFailed = true;
            LOGGER.error("[FANCYMENU] Failed to read OGG audio! OpenAL not ready! Returning empty audio for: " + location);
            return audio;
        } else {
            ALAudioClip clip;
            try {
                clip = ALAudioClip.create();
            } catch (Exception var7) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! Failed to create clip: " + location, var7);
                return audio;
            }
            if (clip == null) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! Clip was NULL: " + location);
                return audio;
            } else {
                try {
                    Optional<Resource> resource = Minecraft.getInstance().getResourceManager().m_213713_(location);
                    if (resource.isPresent()) {
                        InputStream in = ((Resource) resource.get()).open();
                        of(in, location.toString(), audio, clip);
                    }
                } catch (Exception var6) {
                    audio.loadingFailed = true;
                    LOGGER.error("[FANCYMENU] Failed to read OGG audio from ResourceLocation: " + location, var6);
                }
                return audio;
            }
        }
    }

    @NotNull
    public static OggAudio local(@NotNull File oggAudioFile) {
        return local(oggAudioFile, null);
    }

    @NotNull
    public static OggAudio local(@NotNull File oggAudioFile, @Nullable OggAudio writeTo) {
        Objects.requireNonNull(oggAudioFile);
        OggAudio audio = writeTo != null ? writeTo : new OggAudio();
        audio.sourceFile = oggAudioFile;
        if (!oggAudioFile.isFile()) {
            audio.loadingFailed = true;
            LOGGER.error("[FANCYMENU] Failed to read OGG audio from file! File not found: " + oggAudioFile.getPath());
            return audio;
        } else {
            RenderSystem.assertOnRenderThread();
            if (!ALUtils.isOpenAlReady()) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! OpenAL not ready! Returning empty audio for: " + oggAudioFile.getPath());
                return audio;
            } else {
                ALAudioClip clip;
                try {
                    clip = ALAudioClip.create();
                } catch (Exception var6) {
                    audio.loadingFailed = true;
                    LOGGER.error("[FANCYMENU] Failed to read OGG audio! Failed to create clip: " + oggAudioFile.getPath(), var6);
                    return audio;
                }
                if (clip == null) {
                    audio.loadingFailed = true;
                    LOGGER.error("[FANCYMENU] Failed to read OGG audio! Clip was NULL: " + oggAudioFile.getPath());
                    return audio;
                } else {
                    try {
                        InputStream in = new FileInputStream(oggAudioFile);
                        of(in, oggAudioFile.getPath(), audio, clip);
                    } catch (Exception var5) {
                        audio.loadingFailed = true;
                        LOGGER.error("[FANCYMENU] Failed to read OGG audio from file: " + oggAudioFile.getPath(), var5);
                    }
                    return audio;
                }
            }
        }
    }

    @NotNull
    public static OggAudio web(@NotNull String oggAudioURL) {
        return web(oggAudioURL, null);
    }

    @NotNull
    public static OggAudio web(@NotNull String oggAudioURL, @Nullable OggAudio writeTo) {
        Objects.requireNonNull(oggAudioURL);
        OggAudio audio = writeTo != null ? writeTo : new OggAudio();
        audio.sourceURL = oggAudioURL;
        if (!TextValidators.BASIC_URL_TEXT_VALIDATOR.get(oggAudioURL)) {
            audio.loadingFailed = true;
            LOGGER.error("[FANCYMENU] Failed to read OGG audio from URL! Invalid URL: " + oggAudioURL);
            return audio;
        } else {
            RenderSystem.assertOnRenderThread();
            if (!ALUtils.isOpenAlReady()) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! OpenAL not ready! Returning empty audio for: " + oggAudioURL);
                return audio;
            } else {
                ALAudioClip clip;
                try {
                    clip = ALAudioClip.create();
                } catch (Exception var5) {
                    audio.loadingFailed = true;
                    LOGGER.error("[FANCYMENU] Failed to read OGG audio! Failed to create clip: " + oggAudioURL, var5);
                    return audio;
                }
                if (clip == null) {
                    audio.loadingFailed = true;
                    LOGGER.error("[FANCYMENU] Failed to read OGG audio! Clip was NULL: " + oggAudioURL);
                    return audio;
                } else {
                    new Thread(() -> {
                        InputStream webIn = null;
                        try {
                            webIn = WebUtils.openResourceStream(oggAudioURL);
                            if (webIn == null) {
                                throw new NullPointerException("Web resource input stream was NULL!");
                            }
                            ByteArrayInputStream byteIn = new ByteArrayInputStream(webIn.readAllBytes());
                            of(byteIn, oggAudioURL, audio, clip);
                        } catch (Exception var5x) {
                            audio.loadingFailed = true;
                            LOGGER.error("[FANCYMENU] Failed to read OGG audio from URL: " + oggAudioURL, var5x);
                        }
                        CloseableUtils.closeQuietly(webIn);
                    }).start();
                    return audio;
                }
            }
        }
    }

    @NotNull
    public static OggAudio of(@NotNull InputStream in, @Nullable String oggAudioName, @Nullable OggAudio writeTo, @Nullable ALAudioClip clip) {
        String name = oggAudioName != null ? oggAudioName : "[Generic InputStream Source]";
        OggAudio audio = writeTo != null ? writeTo : new OggAudio();
        if (clip == null) {
            RenderSystem.assertOnRenderThread();
        }
        if (!ALUtils.isOpenAlReady()) {
            audio.loadingFailed = true;
            LOGGER.error("[FANCYMENU] Failed to read OGG audio! OpenAL not ready! Returning empty audio for: " + name);
            return audio;
        } else {
            try {
                audio.clip = clip != null ? clip : ALAudioClip.create();
            } catch (Exception var7) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! Failed to create clip: " + name, var7);
                return audio;
            }
            ALAudioClip cachedClip = audio.clip;
            if (cachedClip == null) {
                audio.loadingFailed = true;
                LOGGER.error("[FANCYMENU] Failed to read OGG audio! Clip was NULL: " + name);
                return audio;
            } else {
                new Thread(() -> {
                    OggAudioStream stream = null;
                    try {
                        stream = new OggAudioStream(in);
                        ByteBuffer byteBuffer = stream.readAll();
                        ALAudioBuffer audioBuffer = new ALAudioBuffer(byteBuffer, stream.getFormat());
                        audio.audioBuffer = audioBuffer;
                        cachedClip.setStaticBuffer(audioBuffer);
                        audio.decoded = true;
                        audio.loadingCompleted = true;
                    } catch (Exception var7x) {
                        audio.loadingFailed = true;
                        LOGGER.error("[FANCYMENU] Failed to read OGG audio: " + name, var7x);
                    }
                    CloseableUtils.closeQuietly(stream);
                    CloseableUtils.closeQuietly(in);
                }).start();
                return audio;
            }
        }
    }

    @NotNull
    public static OggAudio of(@NotNull InputStream in) {
        return of(in, null, null, null);
    }

    protected OggAudio() {
    }

    protected void forClip(@NotNull Consumer<ALAudioClip> clip) {
        ALAudioClip cached = this.clip;
        if (cached != null) {
            clip.accept(cached);
        }
    }

    @Nullable
    public ALAudioClip getClip() {
        return this.clip;
    }

    @Override
    public void play() {
        this.forClip(oggAudioClip -> {
            try {
                oggAudioClip.play();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        });
    }

    @Override
    public boolean isPlaying() {
        try {
            ALAudioClip cached = this.clip;
            if (cached != null) {
                return cached.isPlaying();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        return false;
    }

    @Override
    public void pause() {
        this.forClip(oggAudioClip -> {
            try {
                oggAudioClip.pause();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        });
    }

    @Override
    public boolean isPaused() {
        try {
            ALAudioClip cached = this.clip;
            if (cached != null) {
                return cached.isPaused();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        return false;
    }

    @Override
    public void stop() {
        this.forClip(oggAudioClip -> {
            try {
                oggAudioClip.stop();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        });
    }

    @Override
    public void setVolume(float volume) {
        this.forClip(oggAudioClip -> {
            try {
                oggAudioClip.setVolume(volume);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        });
    }

    @Override
    public float getVolume() {
        ALAudioClip cached = this.clip;
        return cached != null ? cached.getVolume() : 0.0F;
    }

    @Override
    public void setSoundChannel(@NotNull SoundSource channel) {
        this.forClip(oggAudioClip -> oggAudioClip.setSoundChannel(channel));
    }

    @NotNull
    @Override
    public SoundSource getSoundChannel() {
        ALAudioClip cached = this.clip;
        return cached != null ? cached.getSoundChannel() : SoundSource.MASTER;
    }

    @Nullable
    @Override
    public InputStream open() throws IOException {
        if (this.sourceURL != null) {
            return WebUtils.openResourceStream(this.sourceURL);
        } else if (this.sourceFile != null) {
            return new FileInputStream(this.sourceFile);
        } else {
            return this.sourceLocation != null ? Minecraft.getInstance().getResourceManager().m_215595_(this.sourceLocation) : null;
        }
    }

    @Override
    public boolean isReady() {
        if (!this.closed && this.decoded) {
            ALAudioClip cachedClip = this.clip;
            if (cachedClip != null) {
                if (cachedClip.isClosed()) {
                    return false;
                }
                if (cachedClip.isValidOpenAlSource()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLoadingCompleted() {
        return !this.closed && !this.loadingFailed && this.loadingCompleted;
    }

    @Override
    public boolean isLoadingFailed() {
        return this.loadingFailed;
    }

    public boolean isValidOpenAlSource() {
        ALAudioClip cached = this.clip;
        return cached != null && cached.isValidOpenAlSource();
    }

    public void close() {
        this.closed = true;
        try {
            ALAudioClip cachedClip = this.clip;
            if (cachedClip != null) {
                cachedClip.close();
            }
        } catch (Exception var3) {
            LOGGER.error("[FANCYMENU] Failed to close OGG audio clip!", var3);
        }
        this.clip = null;
        try {
            ALAudioBuffer cachedBuffer = this.audioBuffer;
            if (cachedBuffer != null) {
                cachedBuffer.delete();
            }
        } catch (Exception var2) {
            LOGGER.error("[FANCYMENU] Failed to delete OGG audio buffer!", var2);
        }
        this.audioBuffer = null;
        this.decoded = false;
    }

    @Override
    public boolean isClosed() {
        if (!this.closed) {
            ALAudioClip cachedClip = this.clip;
            if (cachedClip != null && !cachedClip.isValidOpenAlSource()) {
                this.close();
            }
        }
        return this.closed;
    }
}