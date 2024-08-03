package de.keksuccino.konkrete.resources;

import com.mojang.blaze3d.platform.NativeImage;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.input.CharacterFilter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class WebTextureResourceLocation implements ITextureResourceLocation {

    private String url;

    private ResourceLocation location;

    private boolean loaded = false;

    private int width = 0;

    private int height = 0;

    public WebTextureResourceLocation(String url) {
        this.url = url;
    }

    @Override
    public void loadTexture() {
        if (!this.loaded) {
            try {
                if (Minecraft.getInstance().getTextureManager() == null) {
                    Konkrete.LOGGER.error("[KONKRETE] ERROR: Can't load texture '" + this.url + "'! Minecraft TextureManager instance not ready yet!");
                    return;
                }
                URL u = new URL(this.url);
                HttpURLConnection httpcon = (HttpURLConnection) u.openConnection();
                httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
                InputStream s = httpcon.getInputStream();
                if (s == null) {
                    return;
                }
                NativeImage i = NativeImage.read(s);
                this.width = i.getWidth();
                this.height = i.getHeight();
                this.location = Minecraft.getInstance().getTextureManager().register(this.filterUrl(this.url), new DynamicTexture(i));
                s.close();
                this.loaded = true;
            } catch (Exception var6) {
                Konkrete.LOGGER.error("[KONKRETE] ERROR: Can't load texture '" + this.url + "'! Invalid URL!");
                this.loaded = false;
                for (StackTraceElement st : var6.getStackTrace()) {
                    Konkrete.LOGGER.error(st.toString());
                }
            }
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.location;
    }

    public String getURL() {
        return this.url;
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

    private String filterUrl(String url) {
        CharacterFilter c = new CharacterFilter();
        c.addAllowedCharacters("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".");
        return c.filterForAllowedChars(url.toLowerCase());
    }
}