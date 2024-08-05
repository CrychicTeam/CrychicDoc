package net.mehvahdjukaar.moonlight.api.resources.textures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.Rotation;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

public class TextureImage implements AutoCloseable {

    @Nullable
    private final AnimationMetadataSection metadata;

    private final NativeImage image;

    private final FrameSize frameSize;

    private final int frameCount;

    private final int frameScale;

    private TextureImage(NativeImage image, @Nullable AnimationMetadataSection metadata) {
        this.image = image;
        this.metadata = metadata;
        int imgWidth = this.imageWidth();
        int imgHeight = this.imageHeight();
        this.frameSize = metadata == null ? new FrameSize(imgWidth, imgHeight) : metadata.calculateFrameSize(imgWidth, imgHeight);
        this.frameScale = imgWidth / this.frameSize.width();
        int frameScaleHeight = imgHeight / this.frameSize.height();
        this.frameCount = this.frameScale * frameScaleHeight;
    }

    public void forEachFramePixel(TextureImage.FramePixelConsumer framePixelConsumer) {
        for (int ind = 0; ind < this.frameCount; ind++) {
            int xOff = this.getFrameStartX(ind);
            int yOff = this.getFrameStartY(ind);
            for (int x = 0; x < this.frameWidth(); x++) {
                for (int y = 0; y < this.frameHeight(); y++) {
                    framePixelConsumer.accept(ind, x + xOff, y + yOff);
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public void forEachFrame(TextureImage.FramePixelConsumer e) {
        this.forEachFramePixel(e);
    }

    public void toGrayscale() {
        SpriteUtils.grayscaleImage(this.image);
    }

    public RGBColor getAverageColor() {
        return SpriteUtils.averageColor(this.image);
    }

    public int frameWidth() {
        return this.frameSize.width();
    }

    public int frameHeight() {
        return this.frameSize.height();
    }

    public int getFrameStartX(int frameIndex) {
        return frameIndex % this.frameScale * this.frameWidth();
    }

    public int getFrameStartY(int frameIndex) {
        return frameIndex / this.frameScale * this.frameHeight();
    }

    public int getFramePixel(int frameIndex, int x, int y) {
        return this.image.getPixelRGBA(this.getFrameStartX(frameIndex) + x, this.getFrameStartY(frameIndex) + y);
    }

    public void setFramePixel(int frameIndex, int x, int y, int color) {
        this.image.setPixelRGBA(this.getFrameStartX(frameIndex) + x, this.getFrameStartY(frameIndex) + y, color);
    }

    public NativeImage getImage() {
        return this.image;
    }

    public int frameCount() {
        return this.frameCount;
    }

    @Nullable
    public AnimationMetadataSection getMetadata() {
        return this.metadata;
    }

    public TextureImage makeCopy() {
        NativeImage im = new NativeImage(this.imageWidth(), this.imageHeight(), false);
        im.copyFrom(this.image);
        return new TextureImage(im, this.metadata);
    }

    public TextureImage createAnimationTemplate(int length, AnimationMetadataSection useDataFrom) {
        List<AnimationFrame> frameData = new ArrayList();
        useDataFrom.forEachFrame((i, t) -> frameData.add(new AnimationFrame(i, t)));
        return this.createAnimationTemplate(length, frameData, useDataFrom.getDefaultFrameTime(), useDataFrom.isInterpolatedFrames());
    }

    public TextureImage createAnimationTemplate(int length, List<AnimationFrame> frameData, int frameTime, boolean interpolate) {
        NativeImage im = new NativeImage(this.frameWidth(), this.frameHeight() * length, false);
        TextureImage t = new TextureImage(im, new AnimationMetadataSection(frameData, this.frameWidth(), this.frameHeight(), frameTime, interpolate));
        t.forEachFramePixel((i, x, y) -> {
            int xo = this.getFrameX(i, x);
            int yo = this.getFrameY(i, y);
            t.image.setPixelRGBA(x, y, this.image.getPixelRGBA(xo, yo));
        });
        return t;
    }

    public static TextureImage open(ResourceManager manager, ResourceLocation relativePath) throws IOException {
        try {
            ResourceLocation textureLoc = ResType.TEXTURES.getPath(relativePath);
            NativeImage i = SpriteUtils.readImage(manager, textureLoc);
            ResourceLocation metadataLoc = ResType.MCMETA.getPath(relativePath);
            AnimationMetadataSection metadata = null;
            Optional<Resource> res = manager.m_213713_(metadataLoc);
            if (res.isPresent()) {
                try {
                    InputStream metadataStream = ((Resource) res.get()).open();
                    try {
                        metadata = AbstractPackResources.getMetadataFromStream(AnimationMetadataSection.SERIALIZER, metadataStream);
                    } catch (Throwable var11) {
                        if (metadataStream != null) {
                            try {
                                metadataStream.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                        }
                        throw var11;
                    }
                    if (metadataStream != null) {
                        metadataStream.close();
                    }
                } catch (Exception var12) {
                    throw new IOException("Failed to open mcmeta file at location " + metadataLoc);
                }
            }
            return new TextureImage(i, metadata);
        } catch (Exception var13) {
            throw new IOException("Failed to open texture at location " + relativePath + ": no such file");
        }
    }

    public static TextureImage createNew(int width, int height, @Nullable AnimationMetadataSection animation) {
        TextureImage v = new TextureImage(new NativeImage(width, height, false), animation);
        v.clear();
        return v;
    }

    public static TextureImage createMask(TextureImage original, Palette palette) {
        TextureImage copy = original.makeCopy();
        NativeImage nativeImage = copy.getImage();
        SpriteUtils.forEachPixel(nativeImage, (x, y) -> {
            int color = nativeImage.getPixelRGBA(x, y);
            if (palette.hasColor(color)) {
                nativeImage.setPixelRGBA(x, y, 0);
            } else {
                nativeImage.setPixelRGBA(x, y, -16777216);
            }
        });
        return copy;
    }

    public TextureImage createResized(float widthScale, float heightScale) {
        int newW = (int) ((float) this.imageWidth() * widthScale);
        int newH = (int) ((float) this.imageHeight() * heightScale);
        AnimationMetadataSection meta = null;
        if (this.metadata != null) {
            int mW = (int) ((float) this.metadata.frameWidth * widthScale);
            int mH = (int) ((float) this.metadata.frameHeight * heightScale);
            meta = new AnimationMetadataSection(this.metadata.frames, mW, mH, this.metadata.getDefaultFrameTime(), this.metadata.isInterpolatedFrames());
        }
        TextureImage im = createNew(newW, newH, meta);
        ImageTransformer t = ImageTransformer.builder(this.frameWidth(), this.frameHeight(), im.frameWidth(), im.frameHeight()).copyRect(0, 0, this.frameWidth(), this.frameHeight(), 0, 0).build();
        t.apply(this, im);
        return im;
    }

    public void clear() {
        this.image.fillRect(0, 0, this.image.getWidth(), this.image.getHeight(), 0);
    }

    public static TextureImage of(NativeImage image, @Nullable AnimationMetadataSection animation) {
        return new TextureImage(image, animation);
    }

    public void close() {
        this.image.close();
    }

    public int imageWidth() {
        return this.image.getWidth();
    }

    public int imageHeight() {
        return this.image.getHeight();
    }

    public ImmutableList<NativeImage> splitFrames() {
        Builder<NativeImage> builder = ImmutableList.builder();
        if (this.metadata == null) {
            builder.add(this.image);
            return builder.build();
        } else {
            int imgWidth = this.imageWidth();
            int imgHeight = this.imageHeight();
            FrameSize fs = this.metadata.calculateFrameSize(imgWidth, imgHeight);
            int frameScaleWidth = imgWidth / fs.width();
            int frameScaleHeight = imgHeight / fs.height();
            int maxFrames = frameScaleWidth * frameScaleHeight;
            List<Integer> indexList = Lists.newArrayList();
            this.metadata.forEachFrame((indexx, time) -> indexList.add(indexx));
            if (indexList.isEmpty()) {
                for (int l = 0; l < maxFrames; l++) {
                    indexList.add(l);
                }
            }
            if (indexList.size() <= 1) {
                builder.add(this.image);
            } else {
                for (int index : indexList) {
                    int xOffset = index % frameScaleWidth * this.frameWidth();
                    int yOffset = index / frameScaleWidth * this.frameHeight();
                    if (index >= 0 && xOffset + this.frameWidth() < imgWidth && yOffset + this.frameHeight() < imgHeight) {
                        NativeImage f = new NativeImage(this.frameWidth(), this.frameHeight(), false);
                        for (int x = 0; x < this.frameWidth(); x++) {
                            for (int y = 0; y < this.frameHeight(); y++) {
                                f.setPixelRGBA(x, y, this.image.getPixelRGBA(x + xOffset, y + yOffset));
                            }
                        }
                        builder.add(f);
                    }
                }
            }
            return builder.build();
        }
    }

    @Nullable
    public JsonObject serializeMcMeta() {
        if (this.metadata == null) {
            return null;
        } else {
            JsonObject obj = new JsonObject();
            JsonObject animation = new JsonObject();
            animation.addProperty("frametime", this.metadata.getDefaultFrameTime());
            animation.addProperty("interpolate", this.metadata.isInterpolatedFrames());
            animation.addProperty("height", this.frameSize.height());
            animation.addProperty("width", this.frameSize.width());
            JsonArray frames = new JsonArray();
            this.metadata.forEachFrame((i, t) -> {
                if (t != -1) {
                    JsonObject o = new JsonObject();
                    o.addProperty("time", t);
                    o.addProperty("index", i);
                    frames.add(o);
                } else {
                    frames.add(i);
                }
            });
            animation.add("frames", frames);
            obj.add("animation", animation);
            return obj;
        }
    }

    private void applyOverlay(boolean onlyOnExisting, TextureImage... overlays) throws IllegalStateException {
        for (TextureImage o : overlays) {
            if (o.frameWidth() < this.frameWidth()) {
                throw new IllegalStateException("Could not apply overlay onto images because overlay was too small (overlay W: " + o.frameWidth() + ", image W: " + this.frameWidth());
            }
            if (o.frameHeight() < this.frameHeight()) {
                throw new IllegalStateException("Could not apply overlay onto images because overlay was too small (overlay H: " + o.frameHeight() + ", image H: " + this.frameHeight());
            }
        }
        for (TextureImage o : overlays) {
            this.forEachFramePixel((frameIndex, globalX, globalY) -> {
                int frameX = this.getFrameX(frameIndex, globalX);
                int frameY = this.getFrameY(frameIndex, globalY);
                int targetOverlayFrame = Math.min(frameIndex, o.frameCount - 1);
                int overlayPixel = o.getFramePixel(targetOverlayFrame, frameX, frameY);
                if (!onlyOnExisting || FastColor.ABGR32.alpha(overlayPixel) != 0) {
                    this.image.blendPixel(globalX, globalY, overlayPixel);
                }
            });
            o.close();
        }
    }

    private int getFrameY(Integer frameIndex, Integer globalY) {
        return globalY - this.getFrameStartY(frameIndex);
    }

    private int getFrameX(Integer frameIndex, Integer globalX) {
        return globalX - this.getFrameStartX(frameIndex);
    }

    public void applyOverlay(TextureImage... overlays) throws IllegalStateException {
        this.applyOverlay(false, overlays);
    }

    public void applyOverlayOnExisting(TextureImage... overlays) throws IllegalStateException {
        this.applyOverlay(true, overlays);
    }

    public void removeAlpha(int backgroundColor) {
        for (int x = 0; x < this.image.getWidth(); x++) {
            for (int y = 0; y < this.image.getHeight(); y++) {
                int oldValue = this.image.getPixelRGBA(x, y);
                int a = FastColor.ABGR32.alpha(oldValue);
                if (a == 0) {
                    this.image.setPixelRGBA(x, y, backgroundColor);
                } else {
                    this.image.setPixelRGBA(x, y, FastColor.ABGR32.color(255, FastColor.ABGR32.blue(oldValue), FastColor.ABGR32.green(oldValue), FastColor.ABGR32.red(oldValue)));
                }
            }
        }
    }

    public void crop(TextureImage mask) {
        this.crop(mask, true);
    }

    public void crop(TextureImage mask, boolean discardInner) {
        int width = this.imageWidth();
        int height = this.imageHeight();
        if (mask.imageHeight() >= height && mask.imageWidth() >= width) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (FastColor.ABGR32.alpha(mask.image.getPixelRGBA(x, y)) != 0 == discardInner) {
                        this.image.setPixelRGBA(x, y, 0);
                    }
                }
            }
            mask.close();
        } else {
            throw new IllegalStateException("Could not merge images because they had different dimensions");
        }
    }

    public TextureImage createRotated(Rotation rotation) {
        TextureImage flippedImage = createNew(this.frameHeight(), this.frameWidth() * this.frameCount, this.metadata);
        this.forEachFramePixel((frameIndex, globalX, globalY) -> {
            int frameX = this.getFrameX(frameIndex, globalX);
            int frameY = this.getFrameY(frameIndex, globalY);
            int newFrameX = frameX;
            int newFrameY = frameY;
            int frameWidth = this.frameWidth();
            int frameHeight = this.frameHeight();
            if (rotation == Rotation.CLOCKWISE_90) {
                newFrameX = frameHeight - frameY - 1;
                newFrameY = frameX;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                newFrameX = frameWidth - frameX - 1;
                newFrameY = frameHeight - frameY - 1;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                newFrameX = frameY;
                newFrameY = frameWidth - frameX - 1;
            }
            int newGlobalX = flippedImage.getFrameStartX(frameIndex) + newFrameX;
            int newGlobalY = flippedImage.getFrameStartY(frameIndex) + newFrameY;
            int pixel = this.getImage().getPixelRGBA(globalX, globalY);
            flippedImage.getImage().setPixelRGBA(newGlobalX, newGlobalY, pixel);
        });
        return flippedImage;
    }

    @FunctionalInterface
    public interface FramePixelConsumer extends TriConsumer<Integer, Integer, Integer> {

        void accept(Integer var1, Integer var2, Integer var3);
    }
}