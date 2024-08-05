package com.github.alexthe666.citadel.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.FastColor;

public class VideoFrameTexture extends DynamicTexture {

    public VideoFrameTexture(NativeImage image) {
        super(image);
    }

    @Override
    public void setPixels(NativeImage nativeImage) {
        super.setPixels(nativeImage);
        if (this.m_117991_() != null) {
            TextureUtil.prepareImage(this.m_117963_(), this.m_117991_().getWidth(), this.m_117991_().getHeight());
            this.m_117985_();
        }
    }

    public void setPixelsFromBufferedImage(BufferedImage bufferedImage) {
        for (int i = 0; i < Math.min(this.m_117991_().getWidth(), bufferedImage.getWidth()); i++) {
            for (int j = 0; j < Math.min(this.m_117991_().getHeight(), bufferedImage.getHeight()); j++) {
                int color = bufferedImage.getRGB(i, j);
                int r = color >> 16 & 0xFF;
                int g = color >> 8 & 0xFF;
                int b = color & 0xFF;
                this.m_117991_().setPixelRGBA(i, j, FastColor.ABGR32.color(255, b, g, r));
            }
        }
        this.m_117985_();
    }
}