package de.keksuccino.fancymenu.util.resource.resources.texture;

import com.madgag.gif.fmsware.GifDecoder;
import com.mojang.blaze3d.platform.NativeImage;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.threading.MainThreadTaskExecutor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GifTexture implements ITexture, PlayableResource {

    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    protected volatile List<GifTexture.GifFrame> frames = new ArrayList();

    @Nullable
    protected volatile GifTexture.GifFrame current = null;

    @NotNull
    protected volatile AspectRatio aspectRatio = new AspectRatio(10, 10);

    protected volatile int width = 10;

    protected volatile int height = 10;

    protected volatile long lastResourceLocationCall = -1L;

    protected final AtomicBoolean tickerThreadRunning = new AtomicBoolean(false);

    protected final AtomicBoolean decoded = new AtomicBoolean(false);

    protected volatile boolean allFramesDecoded = false;

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
    public static GifTexture location(@NotNull ResourceLocation location) {
        return location(location, null);
    }

    @NotNull
    public static GifTexture location(@NotNull ResourceLocation location, @Nullable GifTexture writeTo) {
        Objects.requireNonNull(location);
        GifTexture texture = writeTo != null ? writeTo : new GifTexture();
        texture.sourceLocation = location;
        try {
            of(Minecraft.getInstance().getResourceManager().m_215595_(location), location.toString(), texture);
        } catch (Exception var4) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read GIF image from ResourceLocation: " + location, var4);
        }
        return texture;
    }

    @NotNull
    public static GifTexture local(@NotNull File apngFile) {
        return local(apngFile, null);
    }

    @NotNull
    public static GifTexture local(@NotNull File gifFile, @Nullable GifTexture writeTo) {
        Objects.requireNonNull(gifFile);
        GifTexture texture = writeTo != null ? writeTo : new GifTexture();
        texture.sourceFile = gifFile;
        if (!gifFile.isFile()) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read GIF image from file! File not found: " + gifFile.getPath());
            return texture;
        } else {
            new Thread(() -> {
                try {
                    InputStream in = new FileInputStream(gifFile);
                    of(in, gifFile.getPath(), texture);
                } catch (Exception var3) {
                    texture.loadingFailed.set(true);
                    LOGGER.error("[FANCYMENU] Failed to read GIF image from file: " + gifFile.getPath(), var3);
                }
            }).start();
            return texture;
        }
    }

    @NotNull
    public static GifTexture web(@NotNull String apngUrl) {
        return web(apngUrl, null);
    }

    @NotNull
    public static GifTexture web(@NotNull String gifUrl, @Nullable GifTexture writeTo) {
        Objects.requireNonNull(gifUrl);
        GifTexture texture = writeTo != null ? writeTo : new GifTexture();
        texture.sourceURL = gifUrl;
        if (!TextValidators.BASIC_URL_TEXT_VALIDATOR.get((String) Objects.requireNonNull(gifUrl))) {
            texture.loadingFailed.set(true);
            LOGGER.error("[FANCYMENU] Failed to read GIF image from URL! Invalid URL: " + gifUrl);
            return texture;
        } else {
            new Thread(() -> {
                InputStream in = null;
                ByteArrayInputStream byteIn = null;
                try {
                    in = WebUtils.openResourceStream(gifUrl);
                    if (in == null) {
                        throw new NullPointerException("Web resource input stream was NULL!");
                    }
                    byteIn = new ByteArrayInputStream(in.readAllBytes());
                } catch (Exception var5) {
                    texture.loadingFailed.set(true);
                    LOGGER.error("[FANCYMENU] Failed to read GIF image from URL: " + gifUrl, var5);
                }
                if (byteIn != null) {
                    of(byteIn, gifUrl, texture);
                }
                CloseableUtils.closeQuietly(in);
            }).start();
            return texture;
        }
    }

    @NotNull
    public static GifTexture of(@NotNull InputStream in, @Nullable String gifTextureName, @Nullable GifTexture writeTo) {
        Objects.requireNonNull(in);
        GifTexture texture = writeTo != null ? writeTo : new GifTexture();
        new Thread(() -> {
            populateTexture(texture, in, gifTextureName != null ? gifTextureName : "[Generic InputStream Source]");
            if (texture.closed.get()) {
                MainThreadTaskExecutor.executeInMainThread(texture::close, MainThreadTaskExecutor.ExecuteTiming.PRE_CLIENT_TICK);
            }
        }).start();
        return texture;
    }

    @NotNull
    public static GifTexture of(@NotNull InputStream in) {
        return of(in, null, null);
    }

    protected static void populateTexture(@NotNull GifTexture texture, @NotNull InputStream in, @NotNull String gifTextureName) {
        if (!texture.closed.get()) {
            GifTexture.DecodedGifImage decodedImage = decodeGif(in, gifTextureName);
            if (decodedImage == null) {
                LOGGER.error("[FANCYMENU] Failed to read GIF image, because DecodedGifImage was NULL: " + gifTextureName);
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
                deliverGifFrames(decodedImage.decoder(), gifTextureName, frame -> {
                    if (frame != null) {
                        try {
                            frame.nativeImage = NativeImage.read(frame.frameInputStream);
                        } catch (Exception var4) {
                            LOGGER.error("[FANCYMENU] Failed to read frame of GIF image into NativeImage: " + gifTextureName, var4);
                        }
                        CloseableUtils.closeQuietly(frame.closeAfterLoading);
                        CloseableUtils.closeQuietly(frame.frameInputStream);
                        texture.frames.add(frame);
                    }
                });
                texture.loadingCompleted.set(true);
            } catch (Exception var5) {
                texture.loadingFailed.set(true);
                LOGGER.error("[FANCYMENU] Failed to read frames of GIF image: " + gifTextureName, var5);
            }
            texture.allFramesDecoded = true;
        }
        texture.decoded.set(true);
        CloseableUtils.closeQuietly(in);
    }

    protected GifTexture() {
    }

    protected void startTickerIfNeeded() {
        if (!this.tickerThreadRunning.get() && !this.frames.isEmpty() && !this.maxLoopsReached && !this.closed.get()) {
            this.tickerThreadRunning.set(true);
            this.lastResourceLocationCall = System.currentTimeMillis();
            new Thread(() -> {
                while (this.lastResourceLocationCall + 10000L > System.currentTimeMillis() && !this.frames.isEmpty() && !this.closed.get() && !this.maxLoopsReached) {
                    boolean sleep = false;
                    try {
                        boolean cachedAllDecoded = this.allFramesDecoded;
                        List<GifTexture.GifFrame> cachedFrames = new ArrayList(this.frames);
                        if (!cachedFrames.isEmpty()) {
                            if (this.current == null) {
                                this.current = (GifTexture.GifFrame) cachedFrames.get(0);
                                Thread.sleep(Math.max(20L, ((GifTexture.GifFrame) cachedFrames.get(0)).delayMs));
                            }
                            GifTexture.GifFrame cachedCurrent = this.current;
                            if (cachedCurrent != null) {
                                GifTexture.GifFrame newCurrent = null;
                                int currentIndexIncrement = cachedCurrent.index + 1;
                                if (currentIndexIncrement < cachedFrames.size()) {
                                    newCurrent = (GifTexture.GifFrame) cachedFrames.get(currentIndexIncrement);
                                } else if (cachedAllDecoded) {
                                    int cachedNumPlays = this.numPlays.get();
                                    if (cachedNumPlays > 0) {
                                        int newCycles = this.cycles.incrementAndGet();
                                        if (newCycles >= cachedNumPlays) {
                                            this.maxLoopsReached = true;
                                            break;
                                        }
                                        newCurrent = (GifTexture.GifFrame) cachedFrames.get(0);
                                    } else {
                                        newCurrent = (GifTexture.GifFrame) cachedFrames.get(0);
                                    }
                                }
                                if (newCurrent != null) {
                                    this.current = newCurrent;
                                }
                                Thread.sleep(Math.max(20L, newCurrent != null ? newCurrent.delayMs : 100L));
                            } else {
                                sleep = true;
                            }
                        } else {
                            sleep = true;
                        }
                    } catch (Exception var10) {
                        sleep = true;
                        LOGGER.error("[FANCYMENU] An error happened in the frame ticker thread on an GIF!", var10);
                    }
                    if (sleep) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception var9) {
                            LOGGER.error("[FANCYMENU] An error happened in the frame ticker thread on an GIF!", var9);
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
            GifTexture.GifFrame frame = this.current;
            if (frame != null) {
                if (frame.resourceLocation == null && !frame.loaded && frame.nativeImage != null) {
                    try {
                        this.frameRegistrationCounter++;
                        frame.dynamicTexture = new DynamicTexture(frame.nativeImage);
                        frame.resourceLocation = Minecraft.getInstance().getTextureManager().register("fancymenu_gif_frame_" + this.uniqueId + "_" + this.frameRegistrationCounter, frame.dynamicTexture);
                    } catch (Exception var3) {
                        LOGGER.error("[FANCYMENU] Failed to register GIF frame to Minecraft's TextureManager!", var3);
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
        this.current = null;
        List<GifTexture.GifFrame> l = new ArrayList(this.frames);
        if (!l.isEmpty()) {
            this.current = (GifTexture.GifFrame) l.get(0);
            this.cycles.set(0);
        }
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
        for (GifTexture.GifFrame frame : new ArrayList(this.frames)) {
            try {
                if (frame.dynamicTexture != null) {
                    frame.dynamicTexture.close();
                }
            } catch (Exception var5) {
                LOGGER.error("[FANCYMENU] Failed to close DynamicTexture of GIF frame!", var5);
            }
            try {
                if (frame.nativeImage != null) {
                    frame.nativeImage.close();
                }
            } catch (Exception var4) {
                LOGGER.error("[FANCYMENU] Failed to close NativeImage of GIF frame!", var4);
            }
            frame.dynamicTexture = null;
            frame.nativeImage = null;
        }
        this.frames = new ArrayList();
        this.current = null;
    }

    @Nullable
    public static GifTexture.DecodedGifImage decodeGif(@NotNull InputStream in, @NotNull String gifName) {
        try {
            GifDecoder decoder = new GifDecoder();
            decoder.read(in);
            BufferedImage firstFrame = decoder.getImage();
            return new GifTexture.DecodedGifImage(decoder, firstFrame.getWidth(), firstFrame.getHeight(), decoder.getLoopCount());
        } catch (Exception var4) {
            LOGGER.error("[FANCYMENU] Failed to decode GIF image: " + gifName, var4);
            return null;
        }
    }

    public static void deliverGifFrames(@NotNull GifDecoder decoder, @NotNull String gifName, @NotNull Consumer<GifTexture.GifFrame> frameDelivery) {
        int gifFrameCount = decoder.getFrameCount();
        int i = 0;
        for (int index = 0; i < gifFrameCount; i++) {
            try {
                double delay = (double) decoder.getDelay(i);
                BufferedImage image = decoder.getFrame(i);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", os);
                ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
                frameDelivery.accept(new GifTexture.GifFrame(index, bis, (long) delay, os));
                index++;
            } catch (Exception var11) {
                LOGGER.error("[FANCYMENU] Failed to get frame '" + i + "' of GIF image '" + gifName + "!", var11);
            }
        }
    }

    public static record DecodedGifImage(@NotNull GifDecoder decoder, int imageWidth, int imageHeight, int numPlays) {
    }

    public static class GifFrame {

        protected final int index;

        protected final ByteArrayInputStream frameInputStream;

        protected final long delayMs;

        protected final ByteArrayOutputStream closeAfterLoading;

        protected DynamicTexture dynamicTexture;

        protected volatile NativeImage nativeImage;

        protected ResourceLocation resourceLocation;

        protected boolean loaded = false;

        protected GifFrame(int index, ByteArrayInputStream frameInputStream, long delayMs, ByteArrayOutputStream closeAfterLoading) {
            this.index = index;
            this.frameInputStream = frameInputStream;
            this.delayMs = delayMs;
            this.closeAfterLoading = closeAfterLoading;
        }
    }
}