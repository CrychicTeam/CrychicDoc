package com.mojang.realmsclient.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

public class RealmsTextureManager {

    private static final Map<String, RealmsTextureManager.RealmsTexture> TEXTURES = Maps.newHashMap();

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation TEMPLATE_ICON_LOCATION = new ResourceLocation("textures/gui/presets/isles.png");

    public static ResourceLocation worldTemplate(String string0, @Nullable String string1) {
        return string1 == null ? TEMPLATE_ICON_LOCATION : getTexture(string0, string1);
    }

    private static ResourceLocation getTexture(String string0, String string1) {
        RealmsTextureManager.RealmsTexture $$2 = (RealmsTextureManager.RealmsTexture) TEXTURES.get(string0);
        if ($$2 != null && $$2.image().equals(string1)) {
            return $$2.textureId;
        } else {
            NativeImage $$3 = loadImage(string1);
            if ($$3 == null) {
                ResourceLocation $$4 = MissingTextureAtlasSprite.getLocation();
                TEXTURES.put(string0, new RealmsTextureManager.RealmsTexture(string1, $$4));
                return $$4;
            } else {
                ResourceLocation $$5 = new ResourceLocation("realms", "dynamic/" + string0);
                Minecraft.getInstance().getTextureManager().register($$5, new DynamicTexture($$3));
                TEXTURES.put(string0, new RealmsTextureManager.RealmsTexture(string1, $$5));
                return $$5;
            }
        }
    }

    @Nullable
    private static NativeImage loadImage(String string0) {
        byte[] $$1 = Base64.getDecoder().decode(string0);
        ByteBuffer $$2 = MemoryUtil.memAlloc($$1.length);
        try {
            return NativeImage.read($$2.put($$1).flip());
        } catch (IOException var7) {
            LOGGER.warn("Failed to load world image: {}", string0, var7);
        } finally {
            MemoryUtil.memFree($$2);
        }
        return null;
    }

    public static record RealmsTexture(String f_90205_, ResourceLocation f_90206_) {

        private final String image;

        private final ResourceLocation textureId;

        public RealmsTexture(String f_90205_, ResourceLocation f_90206_) {
            this.image = f_90205_;
            this.textureId = f_90206_;
        }
    }
}