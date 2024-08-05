package com.mojang.blaze3d.platform;

import com.mojang.blaze3d.DontObfuscate;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

@DontObfuscate
public class TextureUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int MIN_MIPMAP_LEVEL = 0;

    private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;

    public static int generateTextureId() {
        RenderSystem.assertOnRenderThreadOrInit();
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            int[] $$0 = new int[ThreadLocalRandom.current().nextInt(15) + 1];
            GlStateManager._genTextures($$0);
            int $$1 = GlStateManager._genTexture();
            GlStateManager._deleteTextures($$0);
            return $$1;
        } else {
            return GlStateManager._genTexture();
        }
    }

    public static void releaseTextureId(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._deleteTexture(int0);
    }

    public static void prepareImage(int int0, int int1, int int2) {
        prepareImage(NativeImage.InternalGlFormat.RGBA, int0, 0, int1, int2);
    }

    public static void prepareImage(NativeImage.InternalGlFormat nativeImageInternalGlFormat0, int int1, int int2, int int3) {
        prepareImage(nativeImageInternalGlFormat0, int1, 0, int2, int3);
    }

    public static void prepareImage(int int0, int int1, int int2, int int3) {
        prepareImage(NativeImage.InternalGlFormat.RGBA, int0, int1, int2, int3);
    }

    public static void prepareImage(NativeImage.InternalGlFormat nativeImageInternalGlFormat0, int int1, int int2, int int3, int int4) {
        RenderSystem.assertOnRenderThreadOrInit();
        bind(int1);
        if (int2 >= 0) {
            GlStateManager._texParameter(3553, 33085, int2);
            GlStateManager._texParameter(3553, 33082, 0);
            GlStateManager._texParameter(3553, 33083, int2);
            GlStateManager._texParameter(3553, 34049, 0.0F);
        }
        for (int $$5 = 0; $$5 <= int2; $$5++) {
            GlStateManager._texImage2D(3553, $$5, nativeImageInternalGlFormat0.glFormat(), int3 >> $$5, int4 >> $$5, 0, 6408, 5121, null);
        }
    }

    private static void bind(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._bindTexture(int0);
    }

    public static ByteBuffer readResource(InputStream inputStream0) throws IOException {
        ReadableByteChannel $$1 = Channels.newChannel(inputStream0);
        return $$1 instanceof SeekableByteChannel $$2 ? readResource($$1, (int) $$2.size() + 1) : readResource($$1, 8192);
    }

    private static ByteBuffer readResource(ReadableByteChannel readableByteChannel0, int int1) throws IOException {
        ByteBuffer $$2 = MemoryUtil.memAlloc(int1);
        try {
            while (readableByteChannel0.read($$2) != -1) {
                if (!$$2.hasRemaining()) {
                    $$2 = MemoryUtil.memRealloc($$2, $$2.capacity() * 2);
                }
            }
            return $$2;
        } catch (IOException var4) {
            MemoryUtil.memFree($$2);
            throw var4;
        }
    }

    public static void writeAsPNG(Path path0, String string1, int int2, int int3, int int4, int int5) {
        writeAsPNG(path0, string1, int2, int3, int4, int5, null);
    }

    public static void writeAsPNG(Path path0, String string1, int int2, int int3, int int4, int int5, @Nullable IntUnaryOperator intUnaryOperator6) {
        RenderSystem.assertOnRenderThread();
        bind(int2);
        for (int $$7 = 0; $$7 <= int3; $$7++) {
            int $$8 = int4 >> $$7;
            int $$9 = int5 >> $$7;
            try (NativeImage $$10 = new NativeImage($$8, $$9, false)) {
                $$10.downloadTexture($$7, false);
                if (intUnaryOperator6 != null) {
                    $$10.applyToAllPixels(intUnaryOperator6);
                }
                Path $$11 = path0.resolve(string1 + "_" + $$7 + ".png");
                $$10.writeToFile($$11);
                LOGGER.debug("Exported png to: {}", $$11.toAbsolutePath());
            } catch (IOException var15) {
                LOGGER.debug("Unable to write: ", var15);
            }
        }
    }

    public static Path getDebugTexturePath(Path path0) {
        return path0.resolve("screenshots").resolve("debug");
    }

    public static Path getDebugTexturePath() {
        return getDebugTexturePath(Path.of("."));
    }
}