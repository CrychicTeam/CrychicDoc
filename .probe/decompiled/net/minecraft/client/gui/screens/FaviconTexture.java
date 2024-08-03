package net.minecraft.client.gui.screens;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class FaviconTexture implements AutoCloseable {

    private static final ResourceLocation MISSING_LOCATION = new ResourceLocation("textures/misc/unknown_server.png");

    private static final int WIDTH = 64;

    private static final int HEIGHT = 64;

    private final TextureManager textureManager;

    private final ResourceLocation textureLocation;

    @Nullable
    private DynamicTexture texture;

    private boolean closed;

    private FaviconTexture(TextureManager textureManager0, ResourceLocation resourceLocation1) {
        this.textureManager = textureManager0;
        this.textureLocation = resourceLocation1;
    }

    public static FaviconTexture forWorld(TextureManager textureManager0, String string1) {
        return new FaviconTexture(textureManager0, new ResourceLocation("minecraft", "worlds/" + Util.sanitizeName(string1, ResourceLocation::m_135828_) + "/" + Hashing.sha1().hashUnencodedChars(string1) + "/icon"));
    }

    public static FaviconTexture forServer(TextureManager textureManager0, String string1) {
        return new FaviconTexture(textureManager0, new ResourceLocation("minecraft", "servers/" + Hashing.sha1().hashUnencodedChars(string1) + "/icon"));
    }

    public void upload(NativeImage nativeImage0) {
        if (nativeImage0.getWidth() == 64 && nativeImage0.getHeight() == 64) {
            try {
                this.checkOpen();
                if (this.texture == null) {
                    this.texture = new DynamicTexture(nativeImage0);
                } else {
                    this.texture.setPixels(nativeImage0);
                    this.texture.upload();
                }
                this.textureManager.register(this.textureLocation, this.texture);
            } catch (Throwable var3) {
                nativeImage0.close();
                this.clear();
                throw var3;
            }
        } else {
            nativeImage0.close();
            throw new IllegalArgumentException("Icon must be 64x64, but was " + nativeImage0.getWidth() + "x" + nativeImage0.getHeight());
        }
    }

    public void clear() {
        this.checkOpen();
        if (this.texture != null) {
            this.textureManager.release(this.textureLocation);
            this.texture.close();
            this.texture = null;
        }
    }

    public ResourceLocation textureLocation() {
        return this.texture != null ? this.textureLocation : MISSING_LOCATION;
    }

    public void close() {
        this.clear();
        this.closed = true;
    }

    private void checkOpen() {
        if (this.closed) {
            throw new IllegalStateException("Icon already closed");
        }
    }
}