package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import journeymap.client.cartography.color.RGB;
import journeymap.common.Journeymap;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class DynamicTextureImpl extends DynamicTexture implements Texture {

    private float alpha;

    protected String description;

    private Integer renderWidth;

    private Integer renderHeight;

    private final Float2ObjectMap<Texture> scaledImageMap = new Float2ObjectOpenHashMap();

    @Nullable
    private ResourceLocation resourceLocation;

    public DynamicTextureImpl(NativeImage nativeImage) {
        super(nativeImage);
    }

    public DynamicTextureImpl(NativeImage nativeImage, ResourceLocation location) {
        this(nativeImage);
        this.resourceLocation = location;
    }

    public DynamicTextureImpl(int width, int height, boolean useCalloc) {
        this(new NativeImage(width, height, useCalloc));
    }

    @Nullable
    @Override
    public ResourceLocation getLocation() {
        return this.resourceLocation;
    }

    @Override
    public void upload() {
        try {
            if (super.getPixels() != null) {
                super.m_117966_();
                super.getPixels().upload(0, 0, 0, false);
            }
        } catch (Exception var2) {
            Journeymap.getLogger().error("Error uploading image to framebuffer:", var2);
        }
    }

    @Override
    public int getWidth() {
        return this.renderWidth == null ? super.getPixels().getWidth() : this.renderWidth;
    }

    @Override
    public int getHeight() {
        return this.renderHeight == null ? super.getPixels().getHeight() : this.renderHeight;
    }

    @Override
    public void setDisplayWidth(int width) {
        this.renderWidth = width;
    }

    @Override
    public void setDisplayHeight(int height) {
        this.renderHeight = height;
    }

    @Override
    public Texture getScaledImage(float drawScale) {
        if (drawScale == 1.0F) {
            return this;
        } else {
            Texture scaledTexture = (Texture) this.scaledImageMap.get(drawScale);
            try {
                if (scaledTexture == null) {
                    NativeImage img = ImageUtil.getScaledImage(drawScale, super.getPixels(), false);
                    scaledTexture = new DynamicTextureImpl(img);
                    this.scaledImageMap.put(drawScale, scaledTexture);
                }
                return scaledTexture;
            } catch (Exception var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    @Override
    public Integer getRGB(int x, int y) {
        int rgba = this.getNativeImage().getPixelRGBA(x, y);
        return RGB.rgbaToRgb(rgba);
    }

    @Override
    public int getTextureId() {
        return this.f_117950_;
    }

    @Override
    public void release() {
        super.m_117964_();
    }

    @Override
    public boolean hasImage() {
        return super.getPixels() != null && super.getPixels().pixels > 0L;
    }

    @Override
    public void remove() {
        ImageUtil.clearAndClose(super.getPixels());
    }

    @Override
    public void setNativeImage(NativeImage image) {
        super.setPixels(image);
    }

    @Override
    public NativeImage getNativeImage() {
        return super.getPixels();
    }

    @Override
    public float getAlpha() {
        return this.alpha;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}