package me.jellysquid.mods.sodium.mixin.features.textures.animations.upload;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.List;
import me.jellysquid.mods.sodium.client.util.NativeImageHelper;
import me.jellysquid.mods.sodium.mixin.features.textures.SpriteContentsInvoker;
import net.caffeinemc.mods.sodium.api.util.ColorMixer;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SpriteContents.InterpolationData.class })
public class SpriteContentsInterpolationMixin {

    @Shadow
    @Final
    private NativeImage[] activeFrame;

    @Unique
    private SpriteContents parent;

    @Unique
    private static final int STRIDE = 4;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void assignParent(SpriteContents parent, CallbackInfo ci) {
        this.parent = parent;
    }

    @Overwrite
    void uploadInterpolatedFrame(int x, int y, SpriteContents.Ticker arg) {
        SpriteContents.AnimatedTexture animation = ((SpriteContentsAnimatorImplAccessor) arg).getAnimationInfo();
        SpriteContentsAnimationAccessor animation2 = (SpriteContentsAnimationAccessor) ((SpriteContentsAnimatorImplAccessor) arg).getAnimationInfo();
        List<SpriteContents.FrameInfo> frames = ((SpriteContentsAnimationAccessor) animation).getFrames();
        SpriteContentsAnimatorImplAccessor accessor = (SpriteContentsAnimatorImplAccessor) arg;
        SpriteContentsAnimationFrameAccessor animationFrame = (SpriteContentsAnimationFrameAccessor) frames.get(accessor.getFrameIndex());
        int curIndex = animationFrame.getIndex();
        int nextIndex = ((SpriteContentsAnimationFrameAccessor) animation2.getFrames().get((accessor.getFrameIndex() + 1) % frames.size())).getIndex();
        if (curIndex != nextIndex) {
            float mix = 1.0F - (float) accessor.getFrameTicks() / (float) animationFrame.getTime();
            for (int layer = 0; layer < this.activeFrame.length; layer++) {
                int width = this.parent.width() >> layer;
                int height = this.parent.height() >> layer;
                int curX = curIndex % animation2.getFrameRowSize() * width;
                int curY = curIndex / animation2.getFrameRowSize() * height;
                int nextX = nextIndex % animation2.getFrameRowSize() * width;
                int nextY = nextIndex / animation2.getFrameRowSize() * height;
                NativeImage src = ((SpriteContentsAccessor) this.parent).getImages()[layer];
                NativeImage dst = this.activeFrame[layer];
                long ppSrcPixel = NativeImageHelper.getPointerRGBA(src);
                long ppDstPixel = NativeImageHelper.getPointerRGBA(dst);
                for (int layerY = 0; layerY < height; layerY++) {
                    long pRgba1 = ppSrcPixel + ((long) curX + (long) (curY + layerY) * (long) src.getWidth()) * 4L;
                    long pRgba2 = ppSrcPixel + ((long) nextX + (long) (nextY + layerY) * (long) src.getWidth()) * 4L;
                    for (int layerX = 0; layerX < width; layerX++) {
                        int rgba1 = MemoryUtil.memGetInt(pRgba1);
                        int rgba2 = MemoryUtil.memGetInt(pRgba2);
                        int mixedRgb = ColorMixer.mix(rgba1, rgba2, mix) & 16777215;
                        int alpha = rgba1 & 0xFF000000;
                        MemoryUtil.memPutInt(ppDstPixel, mixedRgb | alpha);
                        pRgba1 += 4L;
                        pRgba2 += 4L;
                        ppDstPixel += 4L;
                    }
                }
            }
            ((SpriteContentsInvoker) this.parent).invokeUpload(x, y, 0, 0, this.activeFrame);
        }
    }
}