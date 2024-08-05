package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class HttpTexture extends SimpleTexture {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int SKIN_WIDTH = 64;

    private static final int SKIN_HEIGHT = 64;

    private static final int LEGACY_SKIN_HEIGHT = 32;

    @Nullable
    private final File file;

    private final String urlString;

    private final boolean processLegacySkin;

    @Nullable
    private final Runnable onDownloaded;

    @Nullable
    private CompletableFuture<?> future;

    private boolean uploaded;

    public HttpTexture(@Nullable File file0, String string1, ResourceLocation resourceLocation2, boolean boolean3, @Nullable Runnable runnable4) {
        super(resourceLocation2);
        this.file = file0;
        this.urlString = string1;
        this.processLegacySkin = boolean3;
        this.onDownloaded = runnable4;
    }

    private void loadCallback(NativeImage nativeImage0) {
        if (this.onDownloaded != null) {
            this.onDownloaded.run();
        }
        Minecraft.getInstance().execute(() -> {
            this.uploaded = true;
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> this.upload(nativeImage0));
            } else {
                this.upload(nativeImage0);
            }
        });
    }

    private void upload(NativeImage nativeImage0) {
        TextureUtil.prepareImage(this.m_117963_(), nativeImage0.getWidth(), nativeImage0.getHeight());
        nativeImage0.upload(0, 0, 0, true);
    }

    @Override
    public void load(ResourceManager resourceManager0) throws IOException {
        Minecraft.getInstance().execute(() -> {
            if (!this.uploaded) {
                try {
                    super.load(resourceManager0);
                } catch (IOException var3x) {
                    LOGGER.warn("Failed to load texture: {}", this.f_118129_, var3x);
                }
                this.uploaded = true;
            }
        });
        if (this.future == null) {
            NativeImage $$2;
            if (this.file != null && this.file.isFile()) {
                LOGGER.debug("Loading http texture from local cache ({})", this.file);
                FileInputStream $$1 = new FileInputStream(this.file);
                $$2 = this.load($$1);
            } else {
                $$2 = null;
            }
            if ($$2 != null) {
                this.loadCallback($$2);
            } else {
                this.future = CompletableFuture.runAsync(() -> {
                    HttpURLConnection $$0 = null;
                    LOGGER.debug("Downloading http texture from {} to {}", this.urlString, this.file);
                    try {
                        $$0 = (HttpURLConnection) new URL(this.urlString).openConnection(Minecraft.getInstance().getProxy());
                        $$0.setDoInput(true);
                        $$0.setDoOutput(false);
                        $$0.connect();
                        if ($$0.getResponseCode() / 100 == 2) {
                            InputStream $$1x;
                            if (this.file != null) {
                                FileUtils.copyInputStreamToFile($$0.getInputStream(), this.file);
                                $$1x = new FileInputStream(this.file);
                            } else {
                                $$1x = $$0.getInputStream();
                            }
                            Minecraft.getInstance().execute(() -> {
                                NativeImage $$1xx = this.load($$1x);
                                if ($$1xx != null) {
                                    this.loadCallback($$1xx);
                                }
                            });
                            return;
                        }
                    } catch (Exception var6) {
                        LOGGER.error("Couldn't download http texture", var6);
                        return;
                    } finally {
                        if ($$0 != null) {
                            $$0.disconnect();
                        }
                    }
                }, Util.backgroundExecutor());
            }
        }
    }

    @Nullable
    private NativeImage load(InputStream inputStream0) {
        NativeImage $$1 = null;
        try {
            $$1 = NativeImage.read(inputStream0);
            if (this.processLegacySkin) {
                $$1 = this.processLegacySkin($$1);
            }
        } catch (Exception var4) {
            LOGGER.warn("Error while loading the skin texture", var4);
        }
        return $$1;
    }

    @Nullable
    private NativeImage processLegacySkin(NativeImage nativeImage0) {
        int $$1 = nativeImage0.getHeight();
        int $$2 = nativeImage0.getWidth();
        if ($$2 == 64 && ($$1 == 32 || $$1 == 64)) {
            boolean $$3 = $$1 == 32;
            if ($$3) {
                NativeImage $$4 = new NativeImage(64, 64, true);
                $$4.copyFrom(nativeImage0);
                nativeImage0.close();
                nativeImage0 = $$4;
                $$4.fillRect(0, 32, 64, 32, 0);
                $$4.copyRect(4, 16, 16, 32, 4, 4, true, false);
                $$4.copyRect(8, 16, 16, 32, 4, 4, true, false);
                $$4.copyRect(0, 20, 24, 32, 4, 12, true, false);
                $$4.copyRect(4, 20, 16, 32, 4, 12, true, false);
                $$4.copyRect(8, 20, 8, 32, 4, 12, true, false);
                $$4.copyRect(12, 20, 16, 32, 4, 12, true, false);
                $$4.copyRect(44, 16, -8, 32, 4, 4, true, false);
                $$4.copyRect(48, 16, -8, 32, 4, 4, true, false);
                $$4.copyRect(40, 20, 0, 32, 4, 12, true, false);
                $$4.copyRect(44, 20, -8, 32, 4, 12, true, false);
                $$4.copyRect(48, 20, -16, 32, 4, 12, true, false);
                $$4.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }
            setNoAlpha(nativeImage0, 0, 0, 32, 16);
            if ($$3) {
                doNotchTransparencyHack(nativeImage0, 32, 0, 64, 32);
            }
            setNoAlpha(nativeImage0, 0, 16, 64, 32);
            setNoAlpha(nativeImage0, 16, 48, 48, 64);
            return nativeImage0;
        } else {
            nativeImage0.close();
            LOGGER.warn("Discarding incorrectly sized ({}x{}) skin texture from {}", new Object[] { $$2, $$1, this.urlString });
            return null;
        }
    }

    private static void doNotchTransparencyHack(NativeImage nativeImage0, int int1, int int2, int int3, int int4) {
        for (int $$5 = int1; $$5 < int3; $$5++) {
            for (int $$6 = int2; $$6 < int4; $$6++) {
                int $$7 = nativeImage0.getPixelRGBA($$5, $$6);
                if (($$7 >> 24 & 0xFF) < 128) {
                    return;
                }
            }
        }
        for (int $$8 = int1; $$8 < int3; $$8++) {
            for (int $$9 = int2; $$9 < int4; $$9++) {
                nativeImage0.setPixelRGBA($$8, $$9, nativeImage0.getPixelRGBA($$8, $$9) & 16777215);
            }
        }
    }

    private static void setNoAlpha(NativeImage nativeImage0, int int1, int int2, int int3, int int4) {
        for (int $$5 = int1; $$5 < int3; $$5++) {
            for (int $$6 = int2; $$6 < int4; $$6++) {
                nativeImage0.setPixelRGBA($$5, $$6, nativeImage0.getPixelRGBA($$5, $$6) | 0xFF000000);
            }
        }
    }
}