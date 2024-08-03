package de.keksuccino.melody.resources.audio;

import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.melody.resources.audio.openal.ALAudioBuffer;
import de.keksuccino.melody.resources.audio.openal.ALAudioClip;
import de.keksuccino.melody.resources.audio.openal.ALUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleAudioFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final SimpleAudioFactory.ConsumingSupplier<String, Boolean> BASIC_URL_TEXT_VALIDATOR = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && (consumes.startsWith("http://") || consumes.startsWith("https://")) && consumes.contains(".") ? true : false;

    @NotNull
    public static CompletableFuture<ALAudioClip> ogg(@NotNull String audioSource, @NotNull SimpleAudioFactory.SourceType sourceType) throws MelodyAudioException {
        Objects.requireNonNull(audioSource);
        Objects.requireNonNull(sourceType);
        RenderSystem.assertOnRenderThread();
        if (!ALUtils.isOpenAlReady()) {
            throw new MelodyAudioException("Failed to create OGG audio clip! OpenAL not ready! Audio source: " + audioSource);
        } else {
            ALAudioClip clip = ALAudioClip.create();
            if (clip == null) {
                throw new MelodyAudioException("Failed to create OGG audio clip! Clip was NULL for: " + audioSource);
            } else if (sourceType == SimpleAudioFactory.SourceType.RESOURCE_LOCATION) {
                ResourceLocation location = ResourceLocation.tryParse(audioSource);
                if (location == null) {
                    clip.closeQuietly();
                    throw new MelodyAudioException("Failed to create OGG audio clip! ResourceLocation parsing failed: " + audioSource);
                } else {
                    Optional<Resource> resource = Minecraft.getInstance().getResourceManager().m_213713_(location);
                    if (resource.isPresent()) {
                        try {
                            InputStream in = ((Resource) resource.get()).open();
                            CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                            new Thread(() -> {
                                Exception exx = tryCreateAndSetOggStaticBuffer(clip, in);
                                if (exx != null) {
                                    completableFuture.completeExceptionally(exx);
                                    clip.closeQuietly();
                                } else {
                                    completableFuture.complete(clip);
                                }
                            }).start();
                            return completableFuture;
                        } catch (Exception var7) {
                            clip.closeQuietly();
                            throw (MelodyAudioException) new MelodyAudioException("Failed to create OGG audio clip! Failed to open ResourceLocation input stream: " + audioSource).initCause(var7);
                        }
                    } else {
                        clip.closeQuietly();
                        throw new MelodyAudioException("Failed to create OGG audio clip! Resource for ResourceLocation not found: " + audioSource);
                    }
                }
            } else if (sourceType == SimpleAudioFactory.SourceType.LOCAL_FILE) {
                File file = new File(audioSource);
                if (!file.isFile()) {
                    clip.closeQuietly();
                    throw new MelodyAudioException("Failed to create OGG audio clip! File not found: " + audioSource);
                } else {
                    try {
                        InputStream in = new FileInputStream(file);
                        CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                        new Thread(() -> {
                            Exception exx = tryCreateAndSetOggStaticBuffer(clip, in);
                            if (exx != null) {
                                completableFuture.completeExceptionally(exx);
                                clip.closeQuietly();
                            } else {
                                completableFuture.complete(clip);
                            }
                        }).start();
                        return completableFuture;
                    } catch (Exception var8) {
                        clip.closeQuietly();
                        throw (MelodyAudioException) new MelodyAudioException("Failed to create OGG audio clip! Failed to open File input stream: " + audioSource).initCause(var8);
                    }
                }
            } else if (!BASIC_URL_TEXT_VALIDATOR.get(audioSource)) {
                clip.closeQuietly();
                throw new MelodyAudioException("Failed to create OGG audio clip! Invalid URL: " + audioSource);
            } else {
                try {
                    InputStream webIn = openWebResourceStream(audioSource);
                    CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                    new Thread(() -> {
                        ByteArrayInputStream in = null;
                        Exception streamReadException = null;
                        try {
                            in = new ByteArrayInputStream(webIn.readAllBytes());
                        } catch (Exception var6x) {
                            streamReadException = var6x;
                        }
                        IOUtils.closeQuietly(webIn);
                        if (in == null) {
                            completableFuture.completeExceptionally(streamReadException);
                            clip.closeQuietly();
                        } else {
                            Exception exx = tryCreateAndSetOggStaticBuffer(clip, in);
                            if (exx != null) {
                                completableFuture.completeExceptionally(exx);
                                clip.closeQuietly();
                            } else {
                                completableFuture.complete(clip);
                            }
                        }
                    }).start();
                    return completableFuture;
                } catch (Exception var9) {
                    clip.closeQuietly();
                    throw (MelodyAudioException) new MelodyAudioException("Failed to create OGG audio clip! Failed to open web input stream: " + audioSource).initCause(var9);
                }
            }
        }
    }

    @NotNull
    public static CompletableFuture<ALAudioClip> wav(@NotNull String audioSource, @NotNull SimpleAudioFactory.SourceType sourceType) throws MelodyAudioException {
        Objects.requireNonNull(audioSource);
        Objects.requireNonNull(sourceType);
        RenderSystem.assertOnRenderThread();
        if (!ALUtils.isOpenAlReady()) {
            throw new MelodyAudioException("Failed to create WAV audio clip! OpenAL not ready! Audio source: " + audioSource);
        } else {
            ALAudioClip clip = ALAudioClip.create();
            if (clip == null) {
                throw new MelodyAudioException("Failed to create WAV audio clip! Clip was NULL for: " + audioSource);
            } else if (sourceType == SimpleAudioFactory.SourceType.RESOURCE_LOCATION) {
                ResourceLocation location = ResourceLocation.tryParse(audioSource);
                if (location == null) {
                    clip.closeQuietly();
                    throw new MelodyAudioException("Failed to create WAV audio clip! ResourceLocation parsing failed: " + audioSource);
                } else {
                    Optional<Resource> resource = Minecraft.getInstance().getResourceManager().m_213713_(location);
                    if (resource.isPresent()) {
                        try {
                            InputStream in = ((Resource) resource.get()).open();
                            CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                            new Thread(() -> {
                                Exception exx = tryCreateAndSetWavStaticBuffer(clip, in);
                                if (exx != null) {
                                    completableFuture.completeExceptionally(exx);
                                    clip.closeQuietly();
                                } else {
                                    completableFuture.complete(clip);
                                }
                            }).start();
                            return completableFuture;
                        } catch (Exception var7) {
                            clip.closeQuietly();
                            throw (MelodyAudioException) new MelodyAudioException("Failed to create WAV audio clip! Failed to open ResourceLocation input stream: " + audioSource).initCause(var7);
                        }
                    } else {
                        clip.closeQuietly();
                        throw new MelodyAudioException("Failed to create WAV audio clip! Resource for ResourceLocation not found: " + audioSource);
                    }
                }
            } else if (sourceType == SimpleAudioFactory.SourceType.LOCAL_FILE) {
                File file = new File(audioSource);
                if (!file.isFile()) {
                    clip.closeQuietly();
                    throw new MelodyAudioException("Failed to create WAV audio clip! File not found: " + audioSource);
                } else {
                    try {
                        InputStream in = new FileInputStream(file);
                        CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                        new Thread(() -> {
                            Exception exx = tryCreateAndSetWavStaticBuffer(clip, in);
                            if (exx != null) {
                                completableFuture.completeExceptionally(exx);
                                clip.closeQuietly();
                            } else {
                                completableFuture.complete(clip);
                            }
                        }).start();
                        return completableFuture;
                    } catch (Exception var8) {
                        clip.closeQuietly();
                        throw (MelodyAudioException) new MelodyAudioException("Failed to create WAV audio clip! Failed to open File input stream: " + audioSource).initCause(var8);
                    }
                }
            } else if (!BASIC_URL_TEXT_VALIDATOR.get(audioSource)) {
                clip.closeQuietly();
                throw new MelodyAudioException("Failed to create WAV audio clip! Invalid URL: " + audioSource);
            } else {
                try {
                    InputStream webIn = openWebResourceStream(audioSource);
                    CompletableFuture<ALAudioClip> completableFuture = new CompletableFuture();
                    new Thread(() -> {
                        Exception exx = tryCreateAndSetWavStaticBuffer(clip, webIn);
                        if (exx != null) {
                            completableFuture.completeExceptionally(exx);
                            clip.closeQuietly();
                        } else {
                            completableFuture.complete(clip);
                        }
                    }).start();
                    return completableFuture;
                } catch (Exception var9) {
                    clip.closeQuietly();
                    throw (MelodyAudioException) new MelodyAudioException("Failed to create WAV audio clip! Failed to open web input stream: " + audioSource).initCause(var9);
                }
            }
        }
    }

    @Nullable
    private static Exception tryCreateAndSetOggStaticBuffer(@NotNull ALAudioClip setTo, @NotNull InputStream in) {
        OggAudioStream stream = null;
        Exception exception = null;
        try {
            stream = new OggAudioStream(in);
            ByteBuffer byteBuffer = stream.readAll();
            ALAudioBuffer audioBuffer = new ALAudioBuffer(byteBuffer, stream.getFormat());
            setTo.setStaticBuffer(audioBuffer);
        } catch (Exception var6) {
            exception = var6;
        }
        IOUtils.closeQuietly(stream);
        IOUtils.closeQuietly(in);
        return exception;
    }

    @Nullable
    private static Exception tryCreateAndSetWavStaticBuffer(@NotNull ALAudioClip setTo, @NotNull InputStream in) {
        AudioInputStream stream = null;
        ByteArrayInputStream byteIn = null;
        Exception exception = null;
        try {
            byteIn = new ByteArrayInputStream(in.readAllBytes());
            stream = AudioSystem.getAudioInputStream(byteIn);
            ByteBuffer byteBuffer = ALUtils.readStreamIntoBuffer(stream);
            ALAudioBuffer audioBuffer = new ALAudioBuffer(byteBuffer, stream.getFormat());
            setTo.setStaticBuffer(audioBuffer);
        } catch (Exception var7) {
            exception = var7;
        }
        IOUtils.closeQuietly(stream);
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(byteIn);
        return exception;
    }

    @NotNull
    private static InputStream openWebResourceStream(@NotNull String resourceURL) throws IOException {
        URL actualURL = new URL(resourceURL);
        HttpURLConnection connection = (HttpURLConnection) actualURL.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        return connection.getInputStream();
    }

    @FunctionalInterface
    private interface ConsumingSupplier<C, R> {

        R get(C var1);
    }

    public static enum SourceType {

        RESOURCE_LOCATION, LOCAL_FILE, WEB_FILE
    }
}