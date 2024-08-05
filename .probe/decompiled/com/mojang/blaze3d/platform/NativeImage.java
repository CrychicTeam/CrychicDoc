package com.mojang.blaze3d.platform;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.util.FastColor;
import org.apache.commons.io.IOUtils;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

public final class NativeImage implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Set<StandardOpenOption> OPEN_OPTIONS = EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    private final NativeImage.Format format;

    private final int width;

    private final int height;

    private final boolean useStbFree;

    private long pixels;

    private final long size;

    public NativeImage(int int0, int int1, boolean boolean2) {
        this(NativeImage.Format.RGBA, int0, int1, boolean2);
    }

    public NativeImage(NativeImage.Format nativeImageFormat0, int int1, int int2, boolean boolean3) {
        if (int1 > 0 && int2 > 0) {
            this.format = nativeImageFormat0;
            this.width = int1;
            this.height = int2;
            this.size = (long) int1 * (long) int2 * (long) nativeImageFormat0.components();
            this.useStbFree = false;
            if (boolean3) {
                this.pixels = MemoryUtil.nmemCalloc(1L, this.size);
            } else {
                this.pixels = MemoryUtil.nmemAlloc(this.size);
            }
        } else {
            throw new IllegalArgumentException("Invalid texture size: " + int1 + "x" + int2);
        }
    }

    private NativeImage(NativeImage.Format nativeImageFormat0, int int1, int int2, boolean boolean3, long long4) {
        if (int1 > 0 && int2 > 0) {
            this.format = nativeImageFormat0;
            this.width = int1;
            this.height = int2;
            this.useStbFree = boolean3;
            this.pixels = long4;
            this.size = (long) int1 * (long) int2 * (long) nativeImageFormat0.components();
        } else {
            throw new IllegalArgumentException("Invalid texture size: " + int1 + "x" + int2);
        }
    }

    public String toString() {
        return "NativeImage[" + this.format + " " + this.width + "x" + this.height + "@" + this.pixels + (this.useStbFree ? "S" : "N") + "]";
    }

    private boolean isOutsideBounds(int int0, int int1) {
        return int0 < 0 || int0 >= this.width || int1 < 0 || int1 >= this.height;
    }

    public static NativeImage read(InputStream inputStream0) throws IOException {
        return read(NativeImage.Format.RGBA, inputStream0);
    }

    public static NativeImage read(@Nullable NativeImage.Format nativeImageFormat0, InputStream inputStream1) throws IOException {
        ByteBuffer $$2 = null;
        NativeImage var3;
        try {
            $$2 = TextureUtil.readResource(inputStream1);
            $$2.rewind();
            var3 = read(nativeImageFormat0, $$2);
        } finally {
            MemoryUtil.memFree($$2);
            IOUtils.closeQuietly(inputStream1);
        }
        return var3;
    }

    public static NativeImage read(ByteBuffer byteBuffer0) throws IOException {
        return read(NativeImage.Format.RGBA, byteBuffer0);
    }

    public static NativeImage read(byte[] byte0) throws IOException {
        MemoryStack $$1 = MemoryStack.stackPush();
        NativeImage var3;
        try {
            ByteBuffer $$2 = $$1.malloc(byte0.length);
            $$2.put(byte0);
            $$2.rewind();
            var3 = read($$2);
        } catch (Throwable var5) {
            if ($$1 != null) {
                try {
                    $$1.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if ($$1 != null) {
            $$1.close();
        }
        return var3;
    }

    public static NativeImage read(@Nullable NativeImage.Format nativeImageFormat0, ByteBuffer byteBuffer1) throws IOException {
        if (nativeImageFormat0 != null && !nativeImageFormat0.supportedByStb()) {
            throw new UnsupportedOperationException("Don't know how to read format " + nativeImageFormat0);
        } else if (MemoryUtil.memAddress(byteBuffer1) == 0L) {
            throw new IllegalArgumentException("Invalid buffer");
        } else {
            MemoryStack $$2 = MemoryStack.stackPush();
            NativeImage var7;
            try {
                IntBuffer $$3 = $$2.mallocInt(1);
                IntBuffer $$4 = $$2.mallocInt(1);
                IntBuffer $$5 = $$2.mallocInt(1);
                ByteBuffer $$6 = STBImage.stbi_load_from_memory(byteBuffer1, $$3, $$4, $$5, nativeImageFormat0 == null ? 0 : nativeImageFormat0.components);
                if ($$6 == null) {
                    throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
                }
                var7 = new NativeImage(nativeImageFormat0 == null ? NativeImage.Format.getStbFormat($$5.get(0)) : nativeImageFormat0, $$3.get(0), $$4.get(0), true, MemoryUtil.memAddress($$6));
            } catch (Throwable var9) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var7;
        }
    }

    private static void setFilter(boolean boolean0, boolean boolean1) {
        RenderSystem.assertOnRenderThreadOrInit();
        if (boolean0) {
            GlStateManager._texParameter(3553, 10241, boolean1 ? 9987 : 9729);
            GlStateManager._texParameter(3553, 10240, 9729);
        } else {
            GlStateManager._texParameter(3553, 10241, boolean1 ? 9986 : 9728);
            GlStateManager._texParameter(3553, 10240, 9728);
        }
    }

    private void checkAllocated() {
        if (this.pixels == 0L) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }

    public void close() {
        if (this.pixels != 0L) {
            if (this.useStbFree) {
                STBImage.nstbi_image_free(this.pixels);
            } else {
                MemoryUtil.nmemFree(this.pixels);
            }
        }
        this.pixels = 0L;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public NativeImage.Format format() {
        return this.format;
    }

    public int getPixelRGBA(int int0, int int1) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$2 = ((long) int0 + (long) int1 * (long) this.width) * 4L;
            return MemoryUtil.memGetInt(this.pixels + $$2);
        }
    }

    public void setPixelRGBA(int int0, int int1, int int2) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "setPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$3 = ((long) int0 + (long) int1 * (long) this.width) * 4L;
            MemoryUtil.memPutInt(this.pixels + $$3, int2);
        }
    }

    public NativeImage mappedCopy(IntUnaryOperator intUnaryOperator0) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "function application only works on RGBA images; have %s", this.format));
        } else {
            this.checkAllocated();
            NativeImage $$1 = new NativeImage(this.width, this.height, false);
            int $$2 = this.width * this.height;
            IntBuffer $$3 = MemoryUtil.memIntBuffer(this.pixels, $$2);
            IntBuffer $$4 = MemoryUtil.memIntBuffer($$1.pixels, $$2);
            for (int $$5 = 0; $$5 < $$2; $$5++) {
                $$4.put($$5, intUnaryOperator0.applyAsInt($$3.get($$5)));
            }
            return $$1;
        }
    }

    public void applyToAllPixels(IntUnaryOperator intUnaryOperator0) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "function application only works on RGBA images; have %s", this.format));
        } else {
            this.checkAllocated();
            int $$1 = this.width * this.height;
            IntBuffer $$2 = MemoryUtil.memIntBuffer(this.pixels, $$1);
            for (int $$3 = 0; $$3 < $$1; $$3++) {
                $$2.put($$3, intUnaryOperator0.applyAsInt($$2.get($$3)));
            }
        }
    }

    public int[] getPixelsRGBA() {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelsRGBA only works on RGBA images; have %s", this.format));
        } else {
            this.checkAllocated();
            int[] $$0 = new int[this.width * this.height];
            MemoryUtil.memIntBuffer(this.pixels, this.width * this.height).get($$0);
            return $$0;
        }
    }

    public void setPixelLuminance(int int0, int int1, byte byte2) {
        RenderSystem.assertOnRenderThread();
        if (!this.format.hasLuminance()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "setPixelLuminance only works on image with luminance; have %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$3 = ((long) int0 + (long) int1 * (long) this.width) * (long) this.format.components() + (long) (this.format.luminanceOffset() / 8);
            MemoryUtil.memPutByte(this.pixels + $$3, byte2);
        }
    }

    public byte getRedOrLuminance(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        if (!this.format.hasLuminanceOrRed()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no red or luminance in %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            int $$2 = (int0 + int1 * this.width) * this.format.components() + this.format.luminanceOrRedOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getGreenOrLuminance(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        if (!this.format.hasLuminanceOrGreen()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no green or luminance in %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            int $$2 = (int0 + int1 * this.width) * this.format.components() + this.format.luminanceOrGreenOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getBlueOrLuminance(int int0, int int1) {
        RenderSystem.assertOnRenderThread();
        if (!this.format.hasLuminanceOrBlue()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no blue or luminance in %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            int $$2 = (int0 + int1 * this.width) * this.format.components() + this.format.luminanceOrBlueOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getLuminanceOrAlpha(int int0, int int1) {
        if (!this.format.hasLuminanceOrAlpha()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no luminance or alpha in %s", this.format));
        } else if (this.isOutsideBounds(int0, int1)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", int0, int1, this.width, this.height));
        } else {
            int $$2 = (int0 + int1 * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public void blendPixel(int int0, int int1, int int2) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int $$3 = this.getPixelRGBA(int0, int1);
            float $$4 = (float) FastColor.ABGR32.alpha(int2) / 255.0F;
            float $$5 = (float) FastColor.ABGR32.blue(int2) / 255.0F;
            float $$6 = (float) FastColor.ABGR32.green(int2) / 255.0F;
            float $$7 = (float) FastColor.ABGR32.red(int2) / 255.0F;
            float $$8 = (float) FastColor.ABGR32.alpha($$3) / 255.0F;
            float $$9 = (float) FastColor.ABGR32.blue($$3) / 255.0F;
            float $$10 = (float) FastColor.ABGR32.green($$3) / 255.0F;
            float $$11 = (float) FastColor.ABGR32.red($$3) / 255.0F;
            float $$13 = 1.0F - $$4;
            float $$14 = $$4 * $$4 + $$8 * $$13;
            float $$15 = $$5 * $$4 + $$9 * $$13;
            float $$16 = $$6 * $$4 + $$10 * $$13;
            float $$17 = $$7 * $$4 + $$11 * $$13;
            if ($$14 > 1.0F) {
                $$14 = 1.0F;
            }
            if ($$15 > 1.0F) {
                $$15 = 1.0F;
            }
            if ($$16 > 1.0F) {
                $$16 = 1.0F;
            }
            if ($$17 > 1.0F) {
                $$17 = 1.0F;
            }
            int $$18 = (int) ($$14 * 255.0F);
            int $$19 = (int) ($$15 * 255.0F);
            int $$20 = (int) ($$16 * 255.0F);
            int $$21 = (int) ($$17 * 255.0F);
            this.setPixelRGBA(int0, int1, FastColor.ABGR32.color($$18, $$19, $$20, $$21));
        }
    }

    @Deprecated
    public int[] makePixelArray() {
        if (this.format != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
        } else {
            this.checkAllocated();
            int[] $$0 = new int[this.getWidth() * this.getHeight()];
            for (int $$1 = 0; $$1 < this.getHeight(); $$1++) {
                for (int $$2 = 0; $$2 < this.getWidth(); $$2++) {
                    int $$3 = this.getPixelRGBA($$2, $$1);
                    $$0[$$2 + $$1 * this.getWidth()] = FastColor.ARGB32.color(FastColor.ABGR32.alpha($$3), FastColor.ABGR32.red($$3), FastColor.ABGR32.green($$3), FastColor.ABGR32.blue($$3));
                }
            }
            return $$0;
        }
    }

    public void upload(int int0, int int1, int int2, boolean boolean3) {
        this.upload(int0, int1, int2, 0, 0, this.width, this.height, false, boolean3);
    }

    public void upload(int int0, int int1, int int2, int int3, int int4, int int5, int int6, boolean boolean7, boolean boolean8) {
        this.upload(int0, int1, int2, int3, int4, int5, int6, false, false, boolean7, boolean8);
    }

    public void upload(int int0, int int1, int int2, int int3, int int4, int int5, int int6, boolean boolean7, boolean boolean8, boolean boolean9, boolean boolean10) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> this._upload(int0, int1, int2, int3, int4, int5, int6, boolean7, boolean8, boolean9, boolean10));
        } else {
            this._upload(int0, int1, int2, int3, int4, int5, int6, boolean7, boolean8, boolean9, boolean10);
        }
    }

    private void _upload(int int0, int int1, int int2, int int3, int int4, int int5, int int6, boolean boolean7, boolean boolean8, boolean boolean9, boolean boolean10) {
        try {
            RenderSystem.assertOnRenderThreadOrInit();
            this.checkAllocated();
            setFilter(boolean7, boolean9);
            if (int5 == this.getWidth()) {
                GlStateManager._pixelStore(3314, 0);
            } else {
                GlStateManager._pixelStore(3314, this.getWidth());
            }
            GlStateManager._pixelStore(3316, int3);
            GlStateManager._pixelStore(3315, int4);
            this.format.setUnpackPixelStoreState();
            GlStateManager._texSubImage2D(3553, int0, int1, int2, int5, int6, this.format.glFormat(), 5121, this.pixels);
            if (boolean8) {
                GlStateManager._texParameter(3553, 10242, 33071);
                GlStateManager._texParameter(3553, 10243, 33071);
            }
        } finally {
            if (boolean10) {
                this.close();
            }
        }
    }

    public void downloadTexture(int int0, boolean boolean1) {
        RenderSystem.assertOnRenderThread();
        this.checkAllocated();
        this.format.setPackPixelStoreState();
        GlStateManager._getTexImage(3553, int0, this.format.glFormat(), 5121, this.pixels);
        if (boolean1 && this.format.hasAlpha()) {
            for (int $$2 = 0; $$2 < this.getHeight(); $$2++) {
                for (int $$3 = 0; $$3 < this.getWidth(); $$3++) {
                    this.setPixelRGBA($$3, $$2, this.getPixelRGBA($$3, $$2) | 255 << this.format.alphaOffset());
                }
            }
        }
    }

    public void downloadDepthBuffer(float float0) {
        RenderSystem.assertOnRenderThread();
        if (this.format.components() != 1) {
            throw new IllegalStateException("Depth buffer must be stored in NativeImage with 1 component.");
        } else {
            this.checkAllocated();
            this.format.setPackPixelStoreState();
            GlStateManager._readPixels(0, 0, this.width, this.height, 6402, 5121, this.pixels);
        }
    }

    public void drawPixels() {
        RenderSystem.assertOnRenderThread();
        this.format.setUnpackPixelStoreState();
        GlStateManager._glDrawPixels(this.width, this.height, this.format.glFormat(), 5121, this.pixels);
    }

    public void writeToFile(File file0) throws IOException {
        this.writeToFile(file0.toPath());
    }

    public void copyFromFont(STBTTFontinfo sTBTTFontinfo0, int int1, int int2, int int3, float float4, float float5, float float6, float float7, int int8, int int9) {
        if (int8 < 0 || int8 + int2 > this.getWidth() || int9 < 0 || int9 + int3 > this.getHeight()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Out of bounds: start: (%s, %s) (size: %sx%s); size: %sx%s", int8, int9, int2, int3, this.getWidth(), this.getHeight()));
        } else if (this.format.components() != 1) {
            throw new IllegalArgumentException("Can only write fonts into 1-component images.");
        } else {
            STBTruetype.nstbtt_MakeGlyphBitmapSubpixel(sTBTTFontinfo0.address(), this.pixels + (long) int8 + (long) (int9 * this.getWidth()), int2, int3, this.getWidth(), float4, float5, float6, float7, int1);
        }
    }

    public void writeToFile(Path path0) throws IOException {
        if (!this.format.supportedByStb()) {
            throw new UnsupportedOperationException("Don't know how to write format " + this.format);
        } else {
            this.checkAllocated();
            WritableByteChannel $$1 = Files.newByteChannel(path0, OPEN_OPTIONS);
            try {
                if (!this.writeToChannel($$1)) {
                    throw new IOException("Could not write image to the PNG file \"" + path0.toAbsolutePath() + "\": " + STBImage.stbi_failure_reason());
                }
            } catch (Throwable var6) {
                if ($$1 != null) {
                    try {
                        $$1.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if ($$1 != null) {
                $$1.close();
            }
        }
    }

    public byte[] asByteArray() throws IOException {
        ByteArrayOutputStream $$0 = new ByteArrayOutputStream();
        byte[] var3;
        try {
            WritableByteChannel $$1 = Channels.newChannel($$0);
            try {
                if (!this.writeToChannel($$1)) {
                    throw new IOException("Could not write image to byte array: " + STBImage.stbi_failure_reason());
                }
                var3 = $$0.toByteArray();
            } catch (Throwable var7) {
                if ($$1 != null) {
                    try {
                        $$1.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$1 != null) {
                $$1.close();
            }
        } catch (Throwable var8) {
            try {
                $$0.close();
            } catch (Throwable var5) {
                var8.addSuppressed(var5);
            }
            throw var8;
        }
        $$0.close();
        return var3;
    }

    private boolean writeToChannel(WritableByteChannel writableByteChannel0) throws IOException {
        NativeImage.WriteCallback $$1 = new NativeImage.WriteCallback(writableByteChannel0);
        boolean var4;
        try {
            int $$2 = Math.min(this.getHeight(), Integer.MAX_VALUE / this.getWidth() / this.format.components());
            if ($$2 < this.getHeight()) {
                LOGGER.warn("Dropping image height from {} to {} to fit the size into 32-bit signed int", this.getHeight(), $$2);
            }
            if (STBImageWrite.nstbi_write_png_to_func($$1.address(), 0L, this.getWidth(), $$2, this.format.components(), this.pixels, 0) != 0) {
                $$1.throwIfException();
                return true;
            }
            var4 = false;
        } finally {
            $$1.free();
        }
        return var4;
    }

    public void copyFrom(NativeImage nativeImage0) {
        if (nativeImage0.format() != this.format) {
            throw new UnsupportedOperationException("Image formats don't match.");
        } else {
            int $$1 = this.format.components();
            this.checkAllocated();
            nativeImage0.checkAllocated();
            if (this.width == nativeImage0.width) {
                MemoryUtil.memCopy(nativeImage0.pixels, this.pixels, Math.min(this.size, nativeImage0.size));
            } else {
                int $$2 = Math.min(this.getWidth(), nativeImage0.getWidth());
                int $$3 = Math.min(this.getHeight(), nativeImage0.getHeight());
                for (int $$4 = 0; $$4 < $$3; $$4++) {
                    int $$5 = $$4 * nativeImage0.getWidth() * $$1;
                    int $$6 = $$4 * this.getWidth() * $$1;
                    MemoryUtil.memCopy(nativeImage0.pixels + (long) $$5, this.pixels + (long) $$6, (long) $$2);
                }
            }
        }
    }

    public void fillRect(int int0, int int1, int int2, int int3, int int4) {
        for (int $$5 = int1; $$5 < int1 + int3; $$5++) {
            for (int $$6 = int0; $$6 < int0 + int2; $$6++) {
                this.setPixelRGBA($$6, $$5, int4);
            }
        }
    }

    public void copyRect(int int0, int int1, int int2, int int3, int int4, int int5, boolean boolean6, boolean boolean7) {
        this.copyRect(this, int0, int1, int0 + int2, int1 + int3, int4, int5, boolean6, boolean7);
    }

    public void copyRect(NativeImage nativeImage0, int int1, int int2, int int3, int int4, int int5, int int6, boolean boolean7, boolean boolean8) {
        for (int $$9 = 0; $$9 < int6; $$9++) {
            for (int $$10 = 0; $$10 < int5; $$10++) {
                int $$11 = boolean7 ? int5 - 1 - $$10 : $$10;
                int $$12 = boolean8 ? int6 - 1 - $$9 : $$9;
                int $$13 = this.getPixelRGBA(int1 + $$10, int2 + $$9);
                nativeImage0.setPixelRGBA(int3 + $$11, int4 + $$12, $$13);
            }
        }
    }

    public void flipY() {
        this.checkAllocated();
        MemoryStack $$0 = MemoryStack.stackPush();
        try {
            int $$1 = this.format.components();
            int $$2 = this.getWidth() * $$1;
            long $$3 = $$0.nmalloc($$2);
            for (int $$4 = 0; $$4 < this.getHeight() / 2; $$4++) {
                int $$5 = $$4 * this.getWidth() * $$1;
                int $$6 = (this.getHeight() - 1 - $$4) * this.getWidth() * $$1;
                MemoryUtil.memCopy(this.pixels + (long) $$5, $$3, (long) $$2);
                MemoryUtil.memCopy(this.pixels + (long) $$6, this.pixels + (long) $$5, (long) $$2);
                MemoryUtil.memCopy($$3, this.pixels + (long) $$6, (long) $$2);
            }
        } catch (Throwable var10) {
            if ($$0 != null) {
                try {
                    $$0.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }
            throw var10;
        }
        if ($$0 != null) {
            $$0.close();
        }
    }

    public void resizeSubRectTo(int int0, int int1, int int2, int int3, NativeImage nativeImage4) {
        this.checkAllocated();
        if (nativeImage4.format() != this.format) {
            throw new UnsupportedOperationException("resizeSubRectTo only works for images of the same format.");
        } else {
            int $$5 = this.format.components();
            STBImageResize.nstbir_resize_uint8(this.pixels + (long) ((int0 + int1 * this.getWidth()) * $$5), int2, int3, this.getWidth() * $$5, nativeImage4.pixels, nativeImage4.getWidth(), nativeImage4.getHeight(), 0, $$5);
        }
    }

    public void untrack() {
        DebugMemoryUntracker.untrack(this.pixels);
    }

    public static enum Format {

        RGBA(4, 6408, true, true, true, false, true, 0, 8, 16, 255, 24, true), RGB(3, 6407, true, true, true, false, false, 0, 8, 16, 255, 255, true), LUMINANCE_ALPHA(2, 33319, false, false, false, true, true, 255, 255, 255, 0, 8, true), LUMINANCE(1, 6403, false, false, false, true, false, 0, 0, 0, 0, 255, true);

        final int components;

        private final int glFormat;

        private final boolean hasRed;

        private final boolean hasGreen;

        private final boolean hasBlue;

        private final boolean hasLuminance;

        private final boolean hasAlpha;

        private final int redOffset;

        private final int greenOffset;

        private final int blueOffset;

        private final int luminanceOffset;

        private final int alphaOffset;

        private final boolean supportedByStb;

        private Format(int p_85148_, int p_85149_, boolean p_85150_, boolean p_85151_, boolean p_85152_, boolean p_85153_, boolean p_85154_, int p_85155_, int p_85156_, int p_85157_, int p_85158_, int p_85159_, boolean p_85160_) {
            this.components = p_85148_;
            this.glFormat = p_85149_;
            this.hasRed = p_85150_;
            this.hasGreen = p_85151_;
            this.hasBlue = p_85152_;
            this.hasLuminance = p_85153_;
            this.hasAlpha = p_85154_;
            this.redOffset = p_85155_;
            this.greenOffset = p_85156_;
            this.blueOffset = p_85157_;
            this.luminanceOffset = p_85158_;
            this.alphaOffset = p_85159_;
            this.supportedByStb = p_85160_;
        }

        public int components() {
            return this.components;
        }

        public void setPackPixelStoreState() {
            RenderSystem.assertOnRenderThread();
            GlStateManager._pixelStore(3333, this.components());
        }

        public void setUnpackPixelStoreState() {
            RenderSystem.assertOnRenderThreadOrInit();
            GlStateManager._pixelStore(3317, this.components());
        }

        public int glFormat() {
            return this.glFormat;
        }

        public boolean hasRed() {
            return this.hasRed;
        }

        public boolean hasGreen() {
            return this.hasGreen;
        }

        public boolean hasBlue() {
            return this.hasBlue;
        }

        public boolean hasLuminance() {
            return this.hasLuminance;
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }

        public int redOffset() {
            return this.redOffset;
        }

        public int greenOffset() {
            return this.greenOffset;
        }

        public int blueOffset() {
            return this.blueOffset;
        }

        public int luminanceOffset() {
            return this.luminanceOffset;
        }

        public int alphaOffset() {
            return this.alphaOffset;
        }

        public boolean hasLuminanceOrRed() {
            return this.hasLuminance || this.hasRed;
        }

        public boolean hasLuminanceOrGreen() {
            return this.hasLuminance || this.hasGreen;
        }

        public boolean hasLuminanceOrBlue() {
            return this.hasLuminance || this.hasBlue;
        }

        public boolean hasLuminanceOrAlpha() {
            return this.hasLuminance || this.hasAlpha;
        }

        public int luminanceOrRedOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.redOffset;
        }

        public int luminanceOrGreenOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
        }

        public int luminanceOrBlueOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
        }

        public int luminanceOrAlphaOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
        }

        public boolean supportedByStb() {
            return this.supportedByStb;
        }

        static NativeImage.Format getStbFormat(int p_85168_) {
            switch(p_85168_) {
                case 1:
                    return LUMINANCE;
                case 2:
                    return LUMINANCE_ALPHA;
                case 3:
                    return RGB;
                case 4:
                default:
                    return RGBA;
            }
        }
    }

    public static enum InternalGlFormat {

        RGBA(6408), RGB(6407), RG(33319), RED(6403);

        private final int glFormat;

        private InternalGlFormat(int p_85190_) {
            this.glFormat = p_85190_;
        }

        public int glFormat() {
            return this.glFormat;
        }
    }

    static class WriteCallback extends STBIWriteCallback {

        private final WritableByteChannel output;

        @Nullable
        private IOException exception;

        WriteCallback(WritableByteChannel writableByteChannel0) {
            this.output = writableByteChannel0;
        }

        public void invoke(long long0, long long1, int int2) {
            ByteBuffer $$3 = getData(long1, int2);
            try {
                this.output.write($$3);
            } catch (IOException var8) {
                this.exception = var8;
            }
        }

        public void throwIfException() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
        }
    }
}