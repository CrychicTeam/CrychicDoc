package software.bernie.geckolib.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.util.RenderUtils;

public class AnimatableTexture extends SimpleTexture {

    private AnimatableTexture.AnimationContents animationContents = null;

    public AnimatableTexture(ResourceLocation location) {
        super(location);
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        Resource resource = manager.m_215593_(this.f_118129_);
        try {
            InputStream inputstream = resource.open();
            NativeImage nativeImage;
            try {
                nativeImage = NativeImage.read(inputstream);
            } catch (Throwable var8) {
                if (inputstream != null) {
                    try {
                        inputstream.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (inputstream != null) {
                inputstream.close();
            }
            this.animationContents = (AnimatableTexture.AnimationContents) resource.metadata().getSection(AnimationMetadataSection.SERIALIZER).map(animMeta -> new AnimatableTexture.AnimationContents(nativeImage, animMeta)).orElse(null);
            if (this.animationContents != null) {
                if (!this.animationContents.isValid()) {
                    nativeImage.close();
                    return;
                }
                onRenderThread(() -> {
                    TextureUtil.prepareImage(this.m_117963_(), 0, this.animationContents.frameSize.width(), this.animationContents.frameSize.height());
                    nativeImage.upload(0, 0, 0, 0, 0, this.animationContents.frameSize.width(), this.animationContents.frameSize.height(), false, false);
                });
                return;
            }
        } catch (RuntimeException var9) {
            GeckoLib.LOGGER.warn("Failed reading metadata of: {}", this.f_118129_, var9);
        }
        super.load(manager);
    }

    public static void setAndUpdate(ResourceLocation texturePath) {
        setAndUpdate(texturePath, (int) RenderUtils.getCurrentTick());
    }

    public static void setAndUpdate(ResourceLocation texturePath, int frameTick) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(texturePath);
        if (texture instanceof AnimatableTexture animatableTexture) {
            animatableTexture.setAnimationFrame(frameTick);
        }
        RenderSystem.setShaderTexture(0, texture.getId());
    }

    public void setAnimationFrame(int tick) {
        if (this.animationContents != null) {
            this.animationContents.animatedTexture.setCurrentFrame(tick);
        }
    }

    private static void onRenderThread(RenderCall renderCall) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(renderCall);
        } else {
            renderCall.execute();
        }
    }

    private class AnimationContents {

        private final FrameSize frameSize;

        private final AnimatableTexture.AnimationContents.Texture animatedTexture;

        private AnimationContents(NativeImage image, AnimationMetadataSection animMeta) {
            this.frameSize = animMeta.calculateFrameSize(image.getWidth(), image.getHeight());
            this.animatedTexture = this.generateAnimatedTexture(image, animMeta);
        }

        private boolean isValid() {
            return this.animatedTexture != null;
        }

        private AnimatableTexture.AnimationContents.Texture generateAnimatedTexture(NativeImage image, AnimationMetadataSection animMeta) {
            if (Mth.isMultipleOf(image.getWidth(), this.frameSize.width()) && Mth.isMultipleOf(image.getHeight(), this.frameSize.height())) {
                int columns = image.getWidth() / this.frameSize.width();
                int rows = image.getHeight() / this.frameSize.height();
                int frameCount = columns * rows;
                List<AnimatableTexture.AnimationContents.Frame> frames = new ObjectArrayList();
                animMeta.forEachFrame((framex, frameTime) -> frames.add(new AnimatableTexture.AnimationContents.Frame(framex, frameTime)));
                if (frames.isEmpty()) {
                    for (int frame = 0; frame < frameCount; frame++) {
                        frames.add(new AnimatableTexture.AnimationContents.Frame(frame, animMeta.getDefaultFrameTime()));
                    }
                } else {
                    int index = 0;
                    IntSet unusedFrames = new IntOpenHashSet();
                    for (AnimatableTexture.AnimationContents.Frame frame : frames) {
                        if (frame.time <= 0) {
                            GeckoLib.LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", AnimatableTexture.this.f_118129_, index, frame.time);
                            unusedFrames.add(frame.index);
                        } else if (frame.index < 0 || frame.index >= frameCount) {
                            GeckoLib.LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", AnimatableTexture.this.f_118129_, index, frame.index);
                            unusedFrames.add(frame.index);
                        }
                        index++;
                    }
                    if (!unusedFrames.isEmpty()) {
                        GeckoLib.LOGGER.warn("Unused frames in sprite {}: {}", AnimatableTexture.this.f_118129_, Arrays.toString(unusedFrames.toArray()));
                    }
                }
                return frames.size() <= 1 ? null : new AnimatableTexture.AnimationContents.Texture(image, (AnimatableTexture.AnimationContents.Frame[]) frames.toArray(new AnimatableTexture.AnimationContents.Frame[0]), columns, animMeta.isInterpolatedFrames());
            } else {
                GeckoLib.LOGGER.error("Image {} size {},{} is not multiple of frame size {},{}", AnimatableTexture.this.f_118129_, image.getWidth(), image.getHeight(), this.frameSize.width(), this.frameSize.height());
                return null;
            }
        }

        private static record Frame(int index, int time) {
        }

        private class Texture implements AutoCloseable {

            private final NativeImage baseImage;

            private final AnimatableTexture.AnimationContents.Frame[] frames;

            private final int framePanelSize;

            private final boolean interpolating;

            private final NativeImage interpolatedFrame;

            private final int totalFrameTime;

            private int currentFrame;

            private int currentSubframe;

            private Texture(NativeImage baseImage, AnimatableTexture.AnimationContents.Frame[] frames, int framePanelSize, boolean interpolating) {
                this.baseImage = baseImage;
                this.frames = frames;
                this.framePanelSize = framePanelSize;
                this.interpolating = interpolating;
                this.interpolatedFrame = interpolating ? new NativeImage(AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false) : null;
                int time = 0;
                for (AnimatableTexture.AnimationContents.Frame frame : this.frames) {
                    time += frame.time;
                }
                this.totalFrameTime = time;
            }

            private int getFrameX(int frameIndex) {
                return frameIndex % this.framePanelSize;
            }

            private int getFrameY(int frameIndex) {
                return frameIndex / this.framePanelSize;
            }

            public void setCurrentFrame(int ticks) {
                ticks %= this.totalFrameTime;
                if (ticks != this.currentSubframe) {
                    int lastSubframe = this.currentSubframe;
                    int lastFrame = this.currentFrame;
                    int time = 0;
                    for (AnimatableTexture.AnimationContents.Frame frame : this.frames) {
                        time += frame.time;
                        if (ticks < time) {
                            this.currentFrame = frame.index;
                            this.currentSubframe = ticks % frame.time;
                            break;
                        }
                    }
                    if (this.currentFrame != lastFrame && this.currentSubframe == 0) {
                        AnimatableTexture.onRenderThread(() -> {
                            TextureUtil.prepareImage(AnimatableTexture.this.m_117963_(), 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height());
                            this.baseImage.upload(0, 0, 0, this.getFrameX(this.currentFrame) * AnimationContents.this.frameSize.width(), this.getFrameY(this.currentFrame) * AnimationContents.this.frameSize.height(), AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false, false);
                        });
                    } else if (this.currentSubframe != lastSubframe && this.interpolating) {
                        AnimatableTexture.onRenderThread(this::generateInterpolatedFrame);
                    }
                }
            }

            private void generateInterpolatedFrame() {
                AnimatableTexture.AnimationContents.Frame frame = this.frames[this.currentFrame];
                double frameProgress = 1.0 - (double) this.currentSubframe / (double) frame.time;
                int nextFrameIndex = this.frames[(this.currentFrame + 1) % this.frames.length].index;
                if (frame.index != nextFrameIndex) {
                    for (int y = 0; y < this.interpolatedFrame.getHeight(); y++) {
                        for (int x = 0; x < this.interpolatedFrame.getWidth(); x++) {
                            int prevFramePixel = this.getPixel(frame.index, x, y);
                            int nextFramePixel = this.getPixel(nextFrameIndex, x, y);
                            int blendedRed = this.interpolate(frameProgress, (double) (prevFramePixel >> 16 & 0xFF), (double) (nextFramePixel >> 16 & 0xFF));
                            int blendedGreen = this.interpolate(frameProgress, (double) (prevFramePixel >> 8 & 0xFF), (double) (nextFramePixel >> 8 & 0xFF));
                            int blendedBlue = this.interpolate(frameProgress, (double) (prevFramePixel & 0xFF), (double) (nextFramePixel & 0xFF));
                            this.interpolatedFrame.setPixelRGBA(x, y, prevFramePixel & 0xFF000000 | blendedRed << 16 | blendedGreen << 8 | blendedBlue);
                        }
                    }
                    TextureUtil.prepareImage(AnimatableTexture.this.m_117963_(), 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height());
                    this.interpolatedFrame.upload(0, 0, 0, 0, 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false, false);
                }
            }

            private int getPixel(int frameIndex, int x, int y) {
                return this.baseImage.getPixelRGBA(x + this.getFrameX(frameIndex) * AnimationContents.this.frameSize.width(), y + this.getFrameY(frameIndex) * AnimationContents.this.frameSize.height());
            }

            private int interpolate(double frameProgress, double prevColour, double nextColour) {
                return (int) (frameProgress * prevColour + (1.0 - frameProgress) * nextColour);
            }

            public void close() {
                this.baseImage.close();
                if (this.interpolatedFrame != null) {
                    this.interpolatedFrame.close();
                }
            }
        }
    }
}