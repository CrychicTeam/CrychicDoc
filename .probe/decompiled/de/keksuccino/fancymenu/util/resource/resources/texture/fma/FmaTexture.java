package de.keksuccino.fancymenu.util.resource.resources.texture.fma;

import com.mojang.blaze3d.platform.NativeImage;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.ThreadUtils;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.threading.MainThreadTaskExecutor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FmaTexture implements ITexture, PlayableResource {

    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    protected volatile List<FmaTexture.FmaFrame> frames = new ArrayList();

    @NotNull
    protected volatile List<FmaTexture.FmaFrame> introFrames = new ArrayList();

    @Nullable
    protected volatile FmaTexture.FmaFrame current = null;

    protected volatile boolean introFinishedPlaying = false;

    protected volatile boolean skipToFirstNormalAfterIntro = false;

    @NotNull
    protected volatile AspectRatio aspectRatio = new AspectRatio(10, 10);

    protected volatile int width = 10;

    protected volatile int height = 10;

    protected volatile long lastResourceLocationCall = -1L;

    protected final AtomicBoolean tickerThreadRunning = new AtomicBoolean(false);

    protected final AtomicBoolean decoded = new AtomicBoolean(false);

    protected volatile boolean allFramesDecoded = false;

    protected volatile boolean allIntroFramesDecoded = false;

    protected final AtomicInteger cycles = new AtomicInteger(0);

    protected final AtomicInteger numPlays = new AtomicInteger(0);

    protected ResourceLocation sourceLocation;

    protected File sourceFile;

    protected String sourceURL;

    protected final AtomicBoolean loadingCompleted = new AtomicBoolean(false);

    protected final AtomicBoolean loadingFailed = new AtomicBoolean(false);

    protected final String uniqueId = ScreenCustomization.generateUniqueIdentifier();

    protected int frameRegistrationCounter = 0;

    protected volatile boolean maxLoopsReached = false;

    protected final AtomicBoolean closed = new AtomicBoolean(false);

    @NotNull
    public static FmaTexture location(@NotNull ResourceLocation location) {
        return location(location, null);
    }

    @NotNull
    public static FmaTexture location(@NotNull ResourceLocation location, @Nullable FmaTexture writeTo) {
        Objects.requireNonNull(location);
        FmaTexture texture = writeTo != null ? writeTo : new FmaTexture();
        texture.sourceLocation = location;
        try {
            of(Minecraft.getInstance().getResourceManager().m_215595_(location), location.toString(), texture);
        } catch (Exception var4) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read FMA image from ResourceLocation: " + location, var4);
        }
        return texture;
    }

    @NotNull
    public static FmaTexture local(@NotNull File fmaFile) {
        return local(fmaFile, null);
    }

    @NotNull
    public static FmaTexture local(@NotNull File fmaFile, @Nullable FmaTexture writeTo) {
        Objects.requireNonNull(fmaFile);
        FmaTexture texture = writeTo != null ? writeTo : new FmaTexture();
        texture.sourceFile = fmaFile;
        if (!fmaFile.isFile()) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read FMA image from file! File not found: " + fmaFile.getPath());
            return texture;
        } else {
            new Thread(() -> {
                try {
                    InputStream in = new FileInputStream(fmaFile);
                    of(in, fmaFile.getPath(), texture);
                } catch (Exception var3) {
                    texture.loadingFailed.set(true);
                    LOGGER.error("[FANCYMENU] Failed to read FMA image from file: " + fmaFile.getPath(), var3);
                }
            }).start();
            return texture;
        }
    }

    @NotNull
    public static FmaTexture web(@NotNull String fmaUrl) {
        return web(fmaUrl, null);
    }

    @NotNull
    public static FmaTexture web(@NotNull String fmaUrl, @Nullable FmaTexture writeTo) {
        Objects.requireNonNull(fmaUrl);
        FmaTexture texture = writeTo != null ? writeTo : new FmaTexture();
        texture.sourceURL = fmaUrl;
        if (!TextValidators.BASIC_URL_TEXT_VALIDATOR.get((String) Objects.requireNonNull(fmaUrl))) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read FMA image from URL! Invalid URL: " + fmaUrl);
            return texture;
        } else {
            new Thread(() -> {
                InputStream in = null;
                ByteArrayInputStream byteIn = null;
                try {
                    in = WebUtils.openResourceStream(fmaUrl);
                    if (in == null) {
                        throw new NullPointerException("Web resource input stream was NULL!");
                    }
                    byteIn = new ByteArrayInputStream(in.readAllBytes());
                } catch (Exception var5) {
                    texture.loadingFailed.set(true);
                    LOGGER.error("[FANCYMENU] Failed to read FMA image from URL: " + fmaUrl, var5);
                }
                if (byteIn != null) {
                    of(byteIn, fmaUrl, texture);
                }
                CloseableUtils.closeQuietly(in);
            }).start();
            return texture;
        }
    }

    @NotNull
    public static FmaTexture of(@NotNull InputStream in, @Nullable String gifTextureName, @Nullable FmaTexture writeTo) {
        Objects.requireNonNull(in);
        FmaTexture texture = writeTo != null ? writeTo : new FmaTexture();
        new Thread(() -> {
            populateTexture(texture, in, gifTextureName != null ? gifTextureName : "[Generic InputStream Source]");
            if (texture.closed.get()) {
                MainThreadTaskExecutor.executeInMainThread(texture::close, MainThreadTaskExecutor.ExecuteTiming.PRE_CLIENT_TICK);
            }
        }).start();
        return texture;
    }

    @NotNull
    public static FmaTexture of(@NotNull InputStream in) {
        return of(in, null, null);
    }

    protected static void populateTexture(@NotNull FmaTexture texture, @NotNull InputStream in, @NotNull String fmaTextureName) {
        FmaTexture.DecodedFmaImage decodedImage = null;
        if (!texture.closed.get()) {
            decodedImage = decodeFma(in, fmaTextureName);
            if (decodedImage == null) {
                LOGGER.error("[FANCYMENU] Failed to read FMA image, because DecodedFmaImage was NULL: " + fmaTextureName);
                texture.decoded.set(true);
                texture.loadingFailed.set(true);
                return;
            }
            texture.width = decodedImage.imageWidth;
            texture.height = decodedImage.imageHeight;
            texture.aspectRatio = new AspectRatio(decodedImage.imageWidth, decodedImage.imageHeight);
            texture.numPlays.set(decodedImage.numPlays);
            texture.decoded.set(true);
            try {
                if (decodedImage.decoder().hasIntroFrames()) {
                    deliverFmaIntroFrames(decodedImage.decoder(), fmaTextureName, frame -> {
                        if (frame != null) {
                            try {
                                frame.nativeImage = NativeImage.read(frame.frameInputStream);
                            } catch (Exception var4) {
                                LOGGER.error("[FANCYMENU] Failed to read intro frame of FMA image into NativeImage: " + fmaTextureName, var4);
                            }
                            CloseableUtils.closeQuietly(frame.frameInputStream);
                            texture.introFrames.add(frame);
                        }
                    });
                }
                texture.allIntroFramesDecoded = true;
                deliverFmaFrames(decodedImage.decoder(), fmaTextureName, frame -> {
                    if (frame != null) {
                        try {
                            frame.nativeImage = NativeImage.read(frame.frameInputStream);
                        } catch (Exception var4) {
                            LOGGER.error("[FANCYMENU] Failed to read frame of FMA image into NativeImage: " + fmaTextureName, var4);
                        }
                        CloseableUtils.closeQuietly(frame.frameInputStream);
                        texture.frames.add(frame);
                    }
                });
                texture.loadingCompleted.set(true);
            } catch (Exception var5) {
                texture.loadingFailed.set(true);
                LOGGER.error("[FANCYMENU] Failed to read frames of FMA image: " + fmaTextureName, var5);
            }
            texture.allFramesDecoded = true;
            texture.allIntroFramesDecoded = true;
        }
        texture.decoded.set(true);
        CloseableUtils.closeQuietly(in);
        if (decodedImage != null) {
            CloseableUtils.closeQuietly(decodedImage.decoder());
        }
    }

    protected FmaTexture() {
    }

    protected void startTickerIfNeeded() {
        if (!this.tickerThreadRunning.get() && (!this.frames.isEmpty() || !this.introFrames.isEmpty()) && !this.maxLoopsReached && !this.closed.get()) {
            this.tickerThreadRunning.set(true);
            this.lastResourceLocationCall = System.currentTimeMillis();
            new Thread(() -> {
                while (this.lastResourceLocationCall + 10000L > System.currentTimeMillis() && (!this.frames.isEmpty() || !this.introFrames.isEmpty()) && !this.closed.get() && !this.maxLoopsReached) {
                    boolean sleep = false;
                    try {
                        boolean cachedAllDecoded = this.allFramesDecoded;
                        boolean cachedAllIntroDecoded = this.allIntroFramesDecoded;
                        boolean cachedIntroFinished = this.introFinishedPlaying;
                        boolean cachedSkipToFirstAfterIntro = this.skipToFirstNormalAfterIntro;
                        List<FmaTexture.FmaFrame> cachedFrames = new ArrayList(this.frames);
                        List<FmaTexture.FmaFrame> cachedIntroFrames = new ArrayList(this.introFrames);
                        if (cachedFrames.isEmpty() && cachedIntroFrames.isEmpty()) {
                            sleep = true;
                        } else {
                            if (this.current == null) {
                                FmaTexture.FmaFrame first = !cachedIntroFrames.isEmpty() ? (FmaTexture.FmaFrame) cachedIntroFrames.get(0) : (FmaTexture.FmaFrame) cachedFrames.get(0);
                                this.current = first;
                                Thread.sleep(Math.max(10L, first.delayMs));
                            } else if (cachedSkipToFirstAfterIntro) {
                                if (cachedFrames.isEmpty()) {
                                    ThreadUtils.sleep(100L);
                                    continue;
                                }
                                this.skipToFirstNormalAfterIntro = false;
                                FmaTexture.FmaFrame firstNormal = (FmaTexture.FmaFrame) cachedFrames.get(0);
                                this.current = firstNormal;
                                Thread.sleep(Math.max(10L, firstNormal.delayMs));
                            }
                            FmaTexture.FmaFrame cachedCurrent = this.current;
                            if (cachedCurrent == null) {
                                sleep = true;
                            } else {
                                FmaTexture.FmaFrame newCurrent = null;
                                int currentIndexIncrement = cachedCurrent.index + 1;
                                boolean pickNextIntroFrame = currentIndexIncrement < cachedIntroFrames.size() && !cachedIntroFinished;
                                if (!pickNextIntroFrame && !cachedIntroFinished && !cachedSkipToFirstAfterIntro && cachedAllIntroDecoded) {
                                    this.introFinishedPlaying = true;
                                    this.skipToFirstNormalAfterIntro = true;
                                    continue;
                                }
                                if (!pickNextIntroFrame && currentIndexIncrement >= cachedFrames.size()) {
                                    if (cachedAllDecoded) {
                                        int cachedNumPlays = this.numPlays.get();
                                        if (cachedNumPlays > 0) {
                                            int newCycles = this.cycles.incrementAndGet();
                                            if (newCycles >= cachedNumPlays) {
                                                this.maxLoopsReached = true;
                                                break;
                                            }
                                            newCurrent = (FmaTexture.FmaFrame) cachedFrames.get(0);
                                        } else {
                                            newCurrent = (FmaTexture.FmaFrame) cachedFrames.get(0);
                                        }
                                    }
                                } else {
                                    newCurrent = pickNextIntroFrame ? (FmaTexture.FmaFrame) cachedIntroFrames.get(currentIndexIncrement) : (FmaTexture.FmaFrame) cachedFrames.get(currentIndexIncrement);
                                }
                                if (newCurrent != null) {
                                    this.current = newCurrent;
                                }
                                Thread.sleep(Math.max(10L, newCurrent != null ? newCurrent.delayMs : 100L));
                            }
                        }
                    } catch (Exception var15) {
                        sleep = true;
                        LOGGER.error("[FANCYMENU] An error happened in the frame ticker thread of an FMA texture!", var15);
                    }
                    if (sleep) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception var14) {
                            LOGGER.error("[FANCYMENU] An error happened in the frame ticker thread of an FMA texture!", var14);
                        }
                    }
                }
                this.tickerThreadRunning.set(false);
            }).start();
        }
    }

    @Nullable
    @Override
    public ResourceLocation getResourceLocation() {
        if (this.closed.get()) {
            return FULLY_TRANSPARENT_TEXTURE;
        } else {
            this.lastResourceLocationCall = System.currentTimeMillis();
            this.startTickerIfNeeded();
            FmaTexture.FmaFrame frame = this.current;
            if (frame != null) {
                if (frame.resourceLocation == null && !frame.loaded && frame.nativeImage != null) {
                    try {
                        this.frameRegistrationCounter++;
                        frame.dynamicTexture = new DynamicTexture(frame.nativeImage);
                        frame.resourceLocation = Minecraft.getInstance().getTextureManager().register("fancymenu_fma_frame_" + this.uniqueId + "_" + this.frameRegistrationCounter, frame.dynamicTexture);
                    } catch (Exception var3) {
                        LOGGER.error("[FANCYMENU] Failed to register FMA frame to Minecraft's TextureManager!", var3);
                    }
                    frame.loaded = true;
                }
                return frame.resourceLocation != null ? frame.resourceLocation : FULLY_TRANSPARENT_TEXTURE;
            } else {
                return null;
            }
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @NotNull
    @Override
    public AspectRatio getAspectRatio() {
        return this.aspectRatio;
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
        return this.decoded.get();
    }

    @Override
    public boolean isLoadingCompleted() {
        return !this.closed.get() && !this.loadingFailed.get() && this.loadingCompleted.get();
    }

    @Override
    public boolean isLoadingFailed() {
        return this.loadingFailed.get();
    }

    @Override
    public void reset() {
        this.introFinishedPlaying = false;
        this.skipToFirstNormalAfterIntro = false;
        this.current = null;
        List<FmaTexture.FmaFrame> normalFrames = new ArrayList(this.frames);
        List<FmaTexture.FmaFrame> introFrames = new ArrayList(this.introFrames);
        if (!introFrames.isEmpty()) {
            this.current = (FmaTexture.FmaFrame) introFrames.get(0);
        } else if (!normalFrames.isEmpty()) {
            this.current = (FmaTexture.FmaFrame) normalFrames.get(0);
        }
        this.cycles.set(0);
    }

    @Override
    public void play() {
    }

    @Override
    public boolean isPlaying() {
        return !this.maxLoopsReached;
    }

    @Override
    public void pause() {
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void stop() {
        this.reset();
    }

    @Override
    public boolean isClosed() {
        return this.closed.get();
    }

    public void close() {
        this.closed.set(true);
        this.sourceLocation = null;
        for (FmaTexture.FmaFrame frame : new ArrayList(this.frames)) {
            try {
                if (frame.dynamicTexture != null) {
                    frame.dynamicTexture.close();
                }
            } catch (Exception var7) {
                LOGGER.error("[FANCYMENU] Failed to close DynamicTexture of FMA frame!", var7);
            }
            try {
                if (frame.nativeImage != null) {
                    frame.nativeImage.close();
                }
            } catch (Exception var6) {
                LOGGER.error("[FANCYMENU] Failed to close NativeImage of FMA frame!", var6);
            }
            frame.dynamicTexture = null;
            frame.nativeImage = null;
        }
        for (FmaTexture.FmaFrame frame : new ArrayList(this.introFrames)) {
            try {
                if (frame.dynamicTexture != null) {
                    frame.dynamicTexture.close();
                }
            } catch (Exception var5) {
                LOGGER.error("[FANCYMENU] Failed to close DynamicTexture of FMA intro frame!", var5);
            }
            try {
                if (frame.nativeImage != null) {
                    frame.nativeImage.close();
                }
            } catch (Exception var4) {
                LOGGER.error("[FANCYMENU] Failed to close NativeImage of FMA intro frame!", var4);
            }
            frame.dynamicTexture = null;
            frame.nativeImage = null;
        }
        this.frames = new ArrayList();
        this.introFrames = new ArrayList();
        this.current = null;
    }

    @Nullable
    public static FmaTexture.DecodedFmaImage decodeFma(@NotNull InputStream in, @NotNull String fmaName) {
        try {
            FmaDecoder decoder = new FmaDecoder();
            decoder.read(in);
            BufferedImage firstFrame = (BufferedImage) Objects.requireNonNull(decoder.getFirstFrameAsBufferedImage(), "Failed to get first frame of FMA image!");
            return new FmaTexture.DecodedFmaImage(decoder, firstFrame.getWidth(), firstFrame.getHeight(), ((FmaDecoder.FmaMetadata) Objects.requireNonNull(decoder.getMetadata(), "FmaDecoder returned NULL for metadata!")).getLoopCount());
        } catch (Exception var4) {
            LOGGER.error("[FANCYMENU] Failed to decode FMA image: " + fmaName, var4);
            return null;
        }
    }

    public static void deliverFmaFrames(@NotNull FmaDecoder decoder, @NotNull String fmaName, @NotNull Consumer<FmaTexture.FmaFrame> frameDelivery) {
        int gifFrameCount = decoder.getFrameCount();
        int i = 0;
        for (int index = 0; i < gifFrameCount; i++) {
            try {
                long delay = ((FmaDecoder.FmaMetadata) Objects.requireNonNull(decoder.getMetadata(), "FmaDecoder returned NULL for metadata!")).getFrameTimeForFrame(i, false);
                InputStream image = (InputStream) Objects.requireNonNull(decoder.getFrame(i), "FmaDecoder returned NULL for frame!");
                frameDelivery.accept(new FmaTexture.FmaFrame(index, image, delay));
                index++;
            } catch (Exception var9) {
                LOGGER.error("[FANCYMENU] Failed to get frame '" + i + "' of FMA image '" + fmaName + "!", var9);
            }
        }
    }

    public static void deliverFmaIntroFrames(@NotNull FmaDecoder decoder, @NotNull String fmaName, @NotNull Consumer<FmaTexture.FmaFrame> frameDelivery) {
        if (decoder.hasIntroFrames()) {
            int gifFrameCount = decoder.getIntroFrameCount();
            int i = 0;
            for (int index = 0; i < gifFrameCount; i++) {
                try {
                    long delay = ((FmaDecoder.FmaMetadata) Objects.requireNonNull(decoder.getMetadata(), "FmaDecoder returned NULL for metadata!")).getFrameTimeForFrame(i, true);
                    InputStream image = (InputStream) Objects.requireNonNull(decoder.getIntroFrame(i), "FmaDecoder returned NULL for intro frame!");
                    frameDelivery.accept(new FmaTexture.FmaFrame(index, image, delay));
                    index++;
                } catch (Exception var9) {
                    LOGGER.error("[FANCYMENU] Failed to get intro frame '" + i + "' of FMA image '" + fmaName + "!", var9);
                }
            }
        }
    }

    public static record DecodedFmaImage(@NotNull FmaDecoder decoder, int imageWidth, int imageHeight, int numPlays) {
    }

    public static class FmaFrame {

        protected final int index;

        protected final InputStream frameInputStream;

        protected final long delayMs;

        protected DynamicTexture dynamicTexture;

        protected volatile NativeImage nativeImage;

        protected ResourceLocation resourceLocation;

        protected boolean loaded = false;

        protected FmaFrame(int index, InputStream frameInputStream, long delayMs) {
            this.index = index;
            this.frameInputStream = frameInputStream;
            this.delayMs = delayMs;
        }
    }
}