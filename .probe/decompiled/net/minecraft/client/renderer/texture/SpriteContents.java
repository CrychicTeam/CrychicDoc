package net.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class SpriteContents implements Stitcher.Entry, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation name;

    final int width;

    final int height;

    private final NativeImage originalImage;

    NativeImage[] byMipLevel;

    @Nullable
    private final SpriteContents.AnimatedTexture animatedTexture;

    public SpriteContents(ResourceLocation resourceLocation0, FrameSize frameSize1, NativeImage nativeImage2, AnimationMetadataSection animationMetadataSection3) {
        this.name = resourceLocation0;
        this.width = frameSize1.width();
        this.height = frameSize1.height();
        this.animatedTexture = this.createAnimatedTexture(frameSize1, nativeImage2.getWidth(), nativeImage2.getHeight(), animationMetadataSection3);
        this.originalImage = nativeImage2;
        this.byMipLevel = new NativeImage[] { this.originalImage };
    }

    public void increaseMipLevel(int int0) {
        try {
            this.byMipLevel = MipmapGenerator.generateMipLevels(this.byMipLevel, int0);
        } catch (Throwable var6) {
            CrashReport $$2 = CrashReport.forThrowable(var6, "Generating mipmaps for frame");
            CrashReportCategory $$3 = $$2.addCategory("Sprite being mipmapped");
            $$3.setDetail("First frame", (CrashReportDetail<String>) (() -> {
                StringBuilder $$0 = new StringBuilder();
                if ($$0.length() > 0) {
                    $$0.append(", ");
                }
                $$0.append(this.originalImage.getWidth()).append("x").append(this.originalImage.getHeight());
                return $$0.toString();
            }));
            CrashReportCategory $$4 = $$2.addCategory("Frame being iterated");
            $$4.setDetail("Sprite name", this.name);
            $$4.setDetail("Sprite size", (CrashReportDetail<String>) (() -> this.width + " x " + this.height));
            $$4.setDetail("Sprite frames", (CrashReportDetail<String>) (() -> this.getFrameCount() + " frames"));
            $$4.setDetail("Mipmap levels", int0);
            throw new ReportedException($$2);
        }
    }

    private int getFrameCount() {
        return this.animatedTexture != null ? this.animatedTexture.frames.size() : 1;
    }

    @Nullable
    private SpriteContents.AnimatedTexture createAnimatedTexture(FrameSize frameSize0, int int1, int int2, AnimationMetadataSection animationMetadataSection3) {
        int $$4 = int1 / frameSize0.width();
        int $$5 = int2 / frameSize0.height();
        int $$6 = $$4 * $$5;
        List<SpriteContents.FrameInfo> $$7 = new ArrayList();
        animationMetadataSection3.forEachFrame((p_251291_, p_251837_) -> $$7.add(new SpriteContents.FrameInfo(p_251291_, p_251837_)));
        if ($$7.isEmpty()) {
            for (int $$8 = 0; $$8 < $$6; $$8++) {
                $$7.add(new SpriteContents.FrameInfo($$8, animationMetadataSection3.getDefaultFrameTime()));
            }
        } else {
            int $$9 = 0;
            IntSet $$10 = new IntOpenHashSet();
            for (Iterator<SpriteContents.FrameInfo> $$11 = $$7.iterator(); $$11.hasNext(); $$9++) {
                SpriteContents.FrameInfo $$12 = (SpriteContents.FrameInfo) $$11.next();
                boolean $$13 = true;
                if ($$12.time <= 0) {
                    LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", new Object[] { this.name, $$9, $$12.time });
                    $$13 = false;
                }
                if ($$12.index < 0 || $$12.index >= $$6) {
                    LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", new Object[] { this.name, $$9, $$12.index });
                    $$13 = false;
                }
                if ($$13) {
                    $$10.add($$12.index);
                } else {
                    $$11.remove();
                }
            }
            int[] $$14 = IntStream.range(0, $$6).filter(p_251185_ -> !$$10.contains(p_251185_)).toArray();
            if ($$14.length > 0) {
                LOGGER.warn("Unused frames in sprite {}: {}", this.name, Arrays.toString($$14));
            }
        }
        return $$7.size() <= 1 ? null : new SpriteContents.AnimatedTexture(ImmutableList.copyOf($$7), $$4, animationMetadataSection3.isInterpolatedFrames());
    }

    void upload(int int0, int int1, int int2, int int3, NativeImage[] nativeImage4) {
        for (int $$5 = 0; $$5 < this.byMipLevel.length; $$5++) {
            nativeImage4[$$5].upload($$5, int0 >> $$5, int1 >> $$5, int2 >> $$5, int3 >> $$5, this.width >> $$5, this.height >> $$5, this.byMipLevel.length > 1, false);
        }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public ResourceLocation name() {
        return this.name;
    }

    public IntStream getUniqueFrames() {
        return this.animatedTexture != null ? this.animatedTexture.getUniqueFrames() : IntStream.of(1);
    }

    @Nullable
    public SpriteTicker createTicker() {
        return this.animatedTexture != null ? this.animatedTexture.createTicker() : null;
    }

    public void close() {
        for (NativeImage $$0 : this.byMipLevel) {
            $$0.close();
        }
    }

    public String toString() {
        return "SpriteContents{name=" + this.name + ", frameCount=" + this.getFrameCount() + ", height=" + this.height + ", width=" + this.width + "}";
    }

    public boolean isTransparent(int int0, int int1, int int2) {
        int $$3 = int1;
        int $$4 = int2;
        if (this.animatedTexture != null) {
            $$3 = int1 + this.animatedTexture.getFrameX(int0) * this.width;
            $$4 = int2 + this.animatedTexture.getFrameY(int0) * this.height;
        }
        return (this.originalImage.getPixelRGBA($$3, $$4) >> 24 & 0xFF) == 0;
    }

    public void uploadFirstFrame(int int0, int int1) {
        if (this.animatedTexture != null) {
            this.animatedTexture.uploadFirstFrame(int0, int1);
        } else {
            this.upload(int0, int1, 0, 0, this.byMipLevel);
        }
    }

    class AnimatedTexture {

        final List<SpriteContents.FrameInfo> frames;

        private final int frameRowSize;

        private final boolean interpolateFrames;

        AnimatedTexture(List<SpriteContents.FrameInfo> listSpriteContentsFrameInfo0, int int1, boolean boolean2) {
            this.frames = listSpriteContentsFrameInfo0;
            this.frameRowSize = int1;
            this.interpolateFrames = boolean2;
        }

        int getFrameX(int int0) {
            return int0 % this.frameRowSize;
        }

        int getFrameY(int int0) {
            return int0 / this.frameRowSize;
        }

        void uploadFrame(int int0, int int1, int int2) {
            int $$3 = this.getFrameX(int2) * SpriteContents.this.width;
            int $$4 = this.getFrameY(int2) * SpriteContents.this.height;
            SpriteContents.this.upload(int0, int1, $$3, $$4, SpriteContents.this.byMipLevel);
        }

        public SpriteTicker createTicker() {
            return SpriteContents.this.new Ticker(this, this.interpolateFrames ? SpriteContents.this.new InterpolationData() : null);
        }

        public void uploadFirstFrame(int int0, int int1) {
            this.uploadFrame(int0, int1, ((SpriteContents.FrameInfo) this.frames.get(0)).index);
        }

        public IntStream getUniqueFrames() {
            return this.frames.stream().mapToInt(p_249981_ -> p_249981_.index).distinct();
        }
    }

    static class FrameInfo {

        final int index;

        final int time;

        FrameInfo(int int0, int int1) {
            this.index = int0;
            this.time = int1;
        }
    }

    final class InterpolationData implements AutoCloseable {

        private final NativeImage[] activeFrame = new NativeImage[SpriteContents.this.byMipLevel.length];

        InterpolationData() {
            for (int $$0 = 0; $$0 < this.activeFrame.length; $$0++) {
                int $$1 = SpriteContents.this.width >> $$0;
                int $$2 = SpriteContents.this.height >> $$0;
                this.activeFrame[$$0] = new NativeImage($$1, $$2, false);
            }
        }

        void uploadInterpolatedFrame(int int0, int int1, SpriteContents.Ticker spriteContentsTicker2) {
            SpriteContents.AnimatedTexture $$3 = spriteContentsTicker2.animationInfo;
            List<SpriteContents.FrameInfo> $$4 = $$3.frames;
            SpriteContents.FrameInfo $$5 = (SpriteContents.FrameInfo) $$4.get(spriteContentsTicker2.frame);
            double $$6 = 1.0 - (double) spriteContentsTicker2.subFrame / (double) $$5.time;
            int $$7 = $$5.index;
            int $$8 = ((SpriteContents.FrameInfo) $$4.get((spriteContentsTicker2.frame + 1) % $$4.size())).index;
            if ($$7 != $$8) {
                for (int $$9 = 0; $$9 < this.activeFrame.length; $$9++) {
                    int $$10 = SpriteContents.this.width >> $$9;
                    int $$11 = SpriteContents.this.height >> $$9;
                    for (int $$12 = 0; $$12 < $$11; $$12++) {
                        for (int $$13 = 0; $$13 < $$10; $$13++) {
                            int $$14 = this.getPixel($$3, $$7, $$9, $$13, $$12);
                            int $$15 = this.getPixel($$3, $$8, $$9, $$13, $$12);
                            int $$16 = this.mix($$6, $$14 >> 16 & 0xFF, $$15 >> 16 & 0xFF);
                            int $$17 = this.mix($$6, $$14 >> 8 & 0xFF, $$15 >> 8 & 0xFF);
                            int $$18 = this.mix($$6, $$14 & 0xFF, $$15 & 0xFF);
                            this.activeFrame[$$9].setPixelRGBA($$13, $$12, $$14 & 0xFF000000 | $$16 << 16 | $$17 << 8 | $$18);
                        }
                    }
                }
                SpriteContents.this.upload(int0, int1, 0, 0, this.activeFrame);
            }
        }

        private int getPixel(SpriteContents.AnimatedTexture spriteContentsAnimatedTexture0, int int1, int int2, int int3, int int4) {
            return SpriteContents.this.byMipLevel[int2].getPixelRGBA(int3 + (spriteContentsAnimatedTexture0.getFrameX(int1) * SpriteContents.this.width >> int2), int4 + (spriteContentsAnimatedTexture0.getFrameY(int1) * SpriteContents.this.height >> int2));
        }

        private int mix(double double0, int int1, int int2) {
            return (int) (double0 * (double) int1 + (1.0 - double0) * (double) int2);
        }

        public void close() {
            for (NativeImage $$0 : this.activeFrame) {
                $$0.close();
            }
        }
    }

    class Ticker implements SpriteTicker {

        int frame;

        int subFrame;

        final SpriteContents.AnimatedTexture animationInfo;

        @Nullable
        private final SpriteContents.InterpolationData interpolationData;

        Ticker(@Nullable SpriteContents.AnimatedTexture spriteContentsAnimatedTexture0, SpriteContents.InterpolationData spriteContentsInterpolationData1) {
            this.animationInfo = spriteContentsAnimatedTexture0;
            this.interpolationData = spriteContentsInterpolationData1;
        }

        @Override
        public void tickAndUpload(int int0, int int1) {
            this.subFrame++;
            SpriteContents.FrameInfo $$2 = (SpriteContents.FrameInfo) this.animationInfo.frames.get(this.frame);
            if (this.subFrame >= $$2.time) {
                int $$3 = $$2.index;
                this.frame = (this.frame + 1) % this.animationInfo.frames.size();
                this.subFrame = 0;
                int $$4 = ((SpriteContents.FrameInfo) this.animationInfo.frames.get(this.frame)).index;
                if ($$3 != $$4) {
                    this.animationInfo.uploadFrame(int0, int1, $$4);
                }
            } else if (this.interpolationData != null) {
                if (!RenderSystem.isOnRenderThread()) {
                    RenderSystem.recordRenderCall(() -> this.interpolationData.uploadInterpolatedFrame(int0, int1, this));
                } else {
                    this.interpolationData.uploadInterpolatedFrame(int0, int1, this);
                }
            }
        }

        @Override
        public void close() {
            if (this.interpolationData != null) {
                this.interpolationData.close();
            }
        }
    }
}