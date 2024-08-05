package net.mehvahdjukaar.moonlight.api.resources.textures;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.List;

public class ImageTransformer {

    protected final int originalW;

    protected final int originalH;

    protected final int targetW;

    protected final int targetH;

    private final List<ImageTransformer.Tpos> transforms;

    public void apply(TextureImage original, TextureImage target) {
        int oFrameW = original.frameWidth();
        int oFrameH = original.frameHeight();
        int tFrameW = target.frameWidth();
        int tFrameH = target.frameHeight();
        NativeImage orIm = original.getImage();
        for (ImageTransformer.Tpos tr : this.transforms) {
            ImageTransformer.Tpos t = tr.scaled(oFrameW, oFrameH, tFrameW, tFrameH, this.originalW, this.originalH, this.targetW, this.targetH);
            original.forEachFramePixel((frameIndex, globalX, globalY) -> {
                int frameX = globalX - original.getFrameStartX(frameIndex);
                int frameY = globalY - original.getFrameStartX(frameIndex);
                if (frameX >= t.startX() && frameX < t.maxX() && frameY >= t.startY() && frameY < t.maxY()) {
                    int col = orIm.getPixelRGBA(globalX, globalY);
                    int targetX = t.targetX + frameX - t.startX();
                    int targetY = t.targetY + frameY - t.startY();
                    if (targetX < tFrameW && targetY < tFrameH) {
                        target.setFramePixel(frameIndex, targetX, targetY, col);
                    }
                }
            });
        }
    }

    private ImageTransformer(int originalW, int originalH, int targetW, int targetH, List<ImageTransformer.Tpos> list) {
        this.originalW = originalW;
        this.originalH = originalH;
        this.targetW = targetW;
        this.targetH = targetH;
        this.transforms = list;
    }

    public static ImageTransformer.Builder builder(int originalW, int originalH, int targetW, int targetH) {
        return new ImageTransformer.Builder(originalW, originalH, targetW, targetH);
    }

    public static class Builder {

        protected final int originalImageW;

        protected final int originalImageH;

        protected final int targetImageW;

        protected final int targetImageH;

        private final List<ImageTransformer.Tpos> transforms = new ArrayList();

        protected Builder(int originalW, int originalH, int targetW, int targetH) {
            this.originalImageW = originalW;
            this.originalImageH = originalH;
            this.targetImageW = targetW;
            this.targetImageH = targetH;
        }

        public ImageTransformer.Builder copyRect(int startX, int startY, int width, int height, int targetX, int targetY) {
            return this.copyRect(startX, startY, width, height, targetX, targetY, width, height);
        }

        public ImageTransformer.Builder copyRect(int startX, int startY, int width, int height, int targetX, int targetY, int targetW, int targetH) {
            Preconditions.checkArgument(startX + width <= this.originalImageW, "Invalid dimensions: original width");
            Preconditions.checkArgument(startY + height <= this.originalImageH, "Invalid dimensions: original height");
            Preconditions.checkArgument(targetX <= this.targetImageW, "Invalid dimensions: target width");
            Preconditions.checkArgument(targetY <= this.targetImageH, "Invalid dimensions: target height");
            this.transforms.add(new ImageTransformer.Tpos(startX, startY, width, height, targetX, targetY, targetW, targetH));
            return this;
        }

        public ImageTransformer build() {
            return new ImageTransformer(this.originalImageW, this.originalImageH, this.targetImageW, this.targetImageH, this.transforms.stream().toList());
        }
    }

    private static record Tpos(int startX, int startY, int width, int height, int targetX, int targetY, int targetW, int targetH) {

        public ImageTransformer.Tpos scaled(int oFrameW, int oFrameH, int tFrameW, int tFrameH, int oW, int oH, int tW, int tH) {
            float scaleOW = (float) oFrameW / (float) oW;
            float scaleOH = (float) oFrameH / (float) oH;
            float scaleTW = (float) tFrameW / (float) tW;
            float scaleTH = (float) tFrameH / (float) tH;
            return new ImageTransformer.Tpos((int) (scaleOW * (float) this.startX), (int) (scaleOH * (float) this.startY), (int) (scaleOW * (float) this.width), (int) (scaleOH * (float) this.height), (int) (scaleTW * (float) this.targetX), (int) (scaleTH * (float) this.targetY), (int) (scaleTW * (float) this.targetW), (int) (scaleTH * (float) this.targetH));
        }

        public int maxX() {
            return this.startX + this.width;
        }

        public int maxY() {
            return this.startY + this.height;
        }
    }
}