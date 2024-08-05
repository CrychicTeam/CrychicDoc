package net.minecraft.client.resources.metadata.animation;

import com.google.common.collect.Lists;
import java.util.List;

public class AnimationMetadataSection {

    public static final AnimationMetadataSectionSerializer SERIALIZER = new AnimationMetadataSectionSerializer();

    public static final String SECTION_NAME = "animation";

    public static final int DEFAULT_FRAME_TIME = 1;

    public static final int UNKNOWN_SIZE = -1;

    public static final AnimationMetadataSection EMPTY = new AnimationMetadataSection(Lists.newArrayList(), -1, -1, 1, false) {

        @Override
        public FrameSize calculateFrameSize(int p_251622_, int p_252064_) {
            return new FrameSize(p_251622_, p_252064_);
        }
    };

    private final List<AnimationFrame> frames;

    private final int frameWidth;

    private final int frameHeight;

    private final int defaultFrameTime;

    private final boolean interpolatedFrames;

    public AnimationMetadataSection(List<AnimationFrame> listAnimationFrame0, int int1, int int2, int int3, boolean boolean4) {
        this.frames = listAnimationFrame0;
        this.frameWidth = int1;
        this.frameHeight = int2;
        this.defaultFrameTime = int3;
        this.interpolatedFrames = boolean4;
    }

    public FrameSize calculateFrameSize(int int0, int int1) {
        if (this.frameWidth != -1) {
            return this.frameHeight != -1 ? new FrameSize(this.frameWidth, this.frameHeight) : new FrameSize(this.frameWidth, int1);
        } else if (this.frameHeight != -1) {
            return new FrameSize(int0, this.frameHeight);
        } else {
            int $$2 = Math.min(int0, int1);
            return new FrameSize($$2, $$2);
        }
    }

    public int getDefaultFrameTime() {
        return this.defaultFrameTime;
    }

    public boolean isInterpolatedFrames() {
        return this.interpolatedFrames;
    }

    public void forEachFrame(AnimationMetadataSection.FrameOutput animationMetadataSectionFrameOutput0) {
        for (AnimationFrame $$1 : this.frames) {
            animationMetadataSectionFrameOutput0.accept($$1.getIndex(), $$1.getTime(this.defaultFrameTime));
        }
    }

    @FunctionalInterface
    public interface FrameOutput {

        void accept(int var1, int var2);
    }
}