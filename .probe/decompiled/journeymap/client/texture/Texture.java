package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;

public interface Texture {

    int getWidth();

    int getHeight();

    void setDisplayWidth(int var1);

    void setDisplayHeight(int var1);

    Texture getScaledImage(float var1);

    int getTextureId();

    void release();

    boolean hasImage();

    void remove();

    void setNativeImage(NativeImage var1);

    default void setNativeImage(NativeImage image, boolean retainImage) {
        this.setNativeImage(image);
    }

    NativeImage getNativeImage();

    float getAlpha();

    void setAlpha(float var1);

    default Integer getRGB(int x, int y) {
        return 0;
    }

    default ResourceLocation getLocation() {
        return null;
    }
}