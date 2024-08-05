package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import java.io.IOException;
import journeymap.client.cartography.color.RGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class SimpleTextureImpl extends SimpleTexture implements Texture {

    private final ResourceLocation location;

    private final Float2ObjectMap<Texture> scaledImageMap = new Float2ObjectOpenHashMap();

    private float alpha;

    private SimpleTexture.TextureImage textureData;

    private Integer renderWidth;

    private Integer renderHeight;

    public SimpleTextureImpl(ResourceLocation location) {
        super(location);
        this.location = location;
        this.textureData = super.getTextureImage(Minecraft.getInstance().getResourceManager());
    }

    @Override
    public int getWidth() {
        try {
            return this.renderWidth == null ? this.textureData.getImage().getWidth() : this.renderWidth;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    @Override
    public int getHeight() {
        try {
            return this.renderHeight == null ? this.textureData.getImage().getHeight() : this.renderHeight;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    @Override
    public void setDisplayWidth(int width) {
        this.renderWidth = width;
    }

    @Override
    public void setDisplayHeight(int height) {
        this.renderHeight = height;
    }

    public void resize(float scale) {
        try {
            NativeImage newImg = ImageUtil.getScaledImage(scale, this.textureData.getImage(), false);
            this.setNativeImage(newImg);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public Texture getScaledImage(float drawScale) {
        if (drawScale == 1.0F) {
            return this;
        } else {
            Texture scaledTexture = (Texture) this.scaledImageMap.get(drawScale);
            try {
                if (scaledTexture == null) {
                    scaledTexture = new DynamicTextureImpl(ImageUtil.getScaledImage(drawScale, this.textureData.getImage(), false));
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
        return super.m_117963_();
    }

    @Override
    public boolean hasImage() {
        return this.textureData != null;
    }

    @Override
    public void remove() {
        this.close();
    }

    @Override
    public void setNativeImage(NativeImage image) {
        this.textureData.close();
        this.textureData = this.updateImage(Minecraft.getInstance().getResourceManager(), image);
    }

    @Override
    public void close() {
        if (this.getNativeImage() != null) {
            this.scaledImageMap.values().forEach(Texture::remove);
            this.getNativeImage().close();
            this.textureData.close();
            this.m_117964_();
            this.textureData = null;
        }
    }

    @Override
    public NativeImage getNativeImage() {
        try {
            return this.textureData.getImage();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    @Override
    public ResourceLocation getLocation() {
        return this.location;
    }

    @Override
    public float getAlpha() {
        return this.alpha;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    private SimpleTexture.TextureImage updateImage(ResourceManager pResourceManager, NativeImage nativeimage) {
        try {
            Resource resource = (Resource) pResourceManager.m_213713_(this.location).get();
            SimpleTexture.TextureImage image;
            try {
                TextureMetadataSection texturemetadatasection = null;
                try {
                    texturemetadatasection = (TextureMetadataSection) resource.metadata().getSection(TextureMetadataSection.SERIALIZER).orElse(null);
                } catch (RuntimeException var7) {
                }
                image = new SimpleTexture.TextureImage(texturemetadatasection, nativeimage);
            } catch (Throwable var8) {
                throw var8;
            }
            return image;
        } catch (IOException var9) {
            return new SimpleTexture.TextureImage(var9);
        }
    }

    @Override
    public void release() {
        super.m_117964_();
    }
}