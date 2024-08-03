package com.github.alexthe666.citadel.client.texture;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.util.GsonHelper;

public class ColorMappedTexture extends SimpleTexture {

    private int[] colors;

    public ColorMappedTexture(ResourceLocation resourceLocation, int[] colors) {
        super(resourceLocation);
        this.colors = colors;
    }

    @Override
    public void load(ResourceManager resourceManager) throws IOException {
        NativeImage nativeimage = this.getNativeImage(resourceManager, this.f_118129_);
        if (nativeimage != null) {
            if (resourceManager.m_213713_(this.f_118129_).isPresent()) {
                Resource resource = (Resource) resourceManager.m_213713_(this.f_118129_).get();
                try {
                    ColorMappedTexture.ColorsMetadataSection section = (ColorMappedTexture.ColorsMetadataSection) resource.metadata().getSection(ColorMappedTexture.ColorsMetadataSection.SERIALIZER).orElse(new ColorMappedTexture.ColorsMetadataSection(null));
                    NativeImage nativeimage2 = this.getNativeImage(resourceManager, section.getColorRamp());
                    if (nativeimage2 != null) {
                        this.processColorMap(nativeimage, nativeimage2);
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
            TextureUtil.prepareImage(this.m_117963_(), nativeimage.getWidth(), nativeimage.getHeight());
            this.m_117966_();
            nativeimage.upload(0, 0, 0, false);
        }
    }

    private NativeImage getNativeImage(ResourceManager resourceManager, @Nullable ResourceLocation resourceLocation) {
        Resource resource = null;
        if (resourceLocation == null) {
            return null;
        } else {
            try {
                resource = resourceManager.m_215593_(resourceLocation);
                InputStream inputstream = resource.open();
                NativeImage nativeimage = NativeImage.read(inputstream);
                if (inputstream != null) {
                    inputstream.close();
                }
                return nativeimage;
            } catch (Throwable var6) {
                return null;
            }
        }
    }

    private void processColorMap(NativeImage nativeImage, NativeImage colorMap) {
        int[] fromColorMap = new int[colorMap.getHeight()];
        for (int i = 0; i < fromColorMap.length; i++) {
            fromColorMap[i] = colorMap.getPixelRGBA(0, i);
        }
        for (int i = 0; i < nativeImage.getWidth(); i++) {
            for (int j = 0; j < nativeImage.getHeight(); j++) {
                int colorAt = nativeImage.getPixelRGBA(i, j);
                if (FastColor.ABGR32.alpha(colorAt) != 0) {
                    int replaceIndex = -1;
                    for (int k = 0; k < fromColorMap.length; k++) {
                        if (colorAt == fromColorMap[k]) {
                            replaceIndex = k;
                        }
                    }
                    if (replaceIndex >= 0 && this.colors.length > replaceIndex) {
                        int r = this.colors[replaceIndex] >> 16 & 0xFF;
                        int g = this.colors[replaceIndex] >> 8 & 0xFF;
                        int b = this.colors[replaceIndex] & 0xFF;
                        nativeImage.setPixelRGBA(i, j, FastColor.ABGR32.color(FastColor.ABGR32.alpha(colorAt), b, g, r));
                    }
                }
            }
        }
    }

    private static class ColorsMetadataSection {

        public static final ColorMappedTexture.ColorsMetadataSectionSerializer SERIALIZER = new ColorMappedTexture.ColorsMetadataSectionSerializer();

        private ResourceLocation colorRamp;

        public ColorsMetadataSection(ResourceLocation colorRamp) {
            this.colorRamp = colorRamp;
        }

        private boolean areColorsEqual(int color1, int color2) {
            int r1 = color1 >> 16 & 0xFF;
            int g1 = color1 >> 8 & 0xFF;
            int b1 = color1 & 0xFF;
            int r2 = color2 >> 16 & 0xFF;
            int g2 = color2 >> 8 & 0xFF;
            int b2 = color2 & 0xFF;
            return r1 == r2 && g1 == g2 && b1 == b2;
        }

        public ResourceLocation getColorRamp() {
            return this.colorRamp;
        }
    }

    private static class ColorsMetadataSectionSerializer implements MetadataSectionSerializer<ColorMappedTexture.ColorsMetadataSection> {

        public ColorMappedTexture.ColorsMetadataSection fromJson(JsonObject json) {
            return new ColorMappedTexture.ColorsMetadataSection(new ResourceLocation(GsonHelper.getAsString(json, "color_ramp")));
        }

        @Override
        public String getMetadataSectionName() {
            return "colors";
        }
    }
}