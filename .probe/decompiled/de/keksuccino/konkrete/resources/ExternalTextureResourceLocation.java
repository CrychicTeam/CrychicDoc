package de.keksuccino.konkrete.resources;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ExternalTextureResourceLocation implements ITextureResourceLocation {

    private InputStream in;

    private String path;

    private ResourceLocation location;

    private boolean loaded = false;

    private int width = 0;

    private int height = 0;

    public ExternalTextureResourceLocation(String path) {
        this.path = path;
    }

    public ExternalTextureResourceLocation(InputStream in) {
        this.in = in;
    }

    @Override
    public void loadTexture() {
        if (!this.loaded) {
            try {
                if (Minecraft.getInstance().getTextureManager() == null) {
                    System.out.println("################################ WARNING ################################");
                    System.out.println("Can't load texture '" + this.path + "'! Minecraft TextureManager instance not ready yet!");
                    return;
                }
                if (this.in == null) {
                    File f = new File(this.path);
                    this.in = new FileInputStream(f);
                }
                NativeImage i = NativeImage.read(this.in);
                this.width = i.getWidth();
                this.height = i.getHeight();
                this.location = Minecraft.getInstance().getTextureManager().register("externaltexture", new SelfcleaningDynamicTexture(i));
                this.in.close();
                this.loaded = true;
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.location;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public boolean isReady() {
        return this.loaded;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }
}