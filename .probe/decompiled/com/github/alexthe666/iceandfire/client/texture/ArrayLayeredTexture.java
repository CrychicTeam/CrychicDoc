package com.github.alexthe666.iceandfire.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ArrayLayeredTexture extends AbstractTexture {

    private static final Logger LOGGER = LogManager.getLogger();

    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(List<String> textureNames) {
        this.layeredTextureNames = textureNames;
    }

    @Override
    public void load(@NotNull ResourceManager manager) {
        Iterator<String> iterator = this.layeredTextureNames.iterator();
        String s = (String) iterator.next();
        Optional<Resource> iresource = manager.m_213713_(new ResourceLocation(s));
        if (iresource.isPresent()) {
            try {
                NativeImage nativeimage = NativeImage.read(((Resource) iresource.get()).open());
                while (iterator.hasNext()) {
                    String s1 = (String) iterator.next();
                    if (s1 != null) {
                        Optional<Resource> iresource1 = manager.m_213713_(new ResourceLocation(s1));
                        NativeImage nativeimage1 = NativeImage.read(((Resource) iresource1.get()).open());
                        for (int i = 0; i < Math.min(nativeimage1.getHeight(), nativeimage.getHeight()); i++) {
                            for (int j = 0; j < Math.min(nativeimage1.getWidth(), nativeimage.getWidth()); j++) {
                                nativeimage.blendPixel(j, i, nativeimage1.getPixelRGBA(j, i));
                            }
                        }
                    }
                }
                if (!RenderSystem.isOnRenderThreadOrInit()) {
                    RenderSystem.recordRenderCall(() -> this.loadImage(nativeimage));
                } else {
                    this.loadImage(nativeimage);
                }
            } catch (IOException var11) {
                LOGGER.error("Couldn't load layered image", var11);
            }
        } else {
            LOGGER.error("Couldn't load layered image");
        }
    }

    private void loadImage(NativeImage imageIn) {
        TextureUtil.prepareImage(this.m_117963_(), imageIn.getWidth(), imageIn.getHeight());
        imageIn.upload(0, 0, 0, true);
    }
}