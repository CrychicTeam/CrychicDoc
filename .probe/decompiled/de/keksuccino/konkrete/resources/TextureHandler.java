package de.keksuccino.konkrete.resources;

import de.keksuccino.konkrete.rendering.animation.ExternalGifAnimationRenderer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TextureHandler {

    private static final Map<String, ITextureResourceLocation> TEXTURES = new HashMap();

    private static final Map<String, ExternalGifAnimationRenderer> GIFS = new HashMap();

    public static ExternalTextureResourceLocation getResource(String path) {
        File f = new File(path);
        if (!TEXTURES.containsKey(f.getAbsolutePath())) {
            if (f.exists() && f.isFile()) {
                ExternalTextureResourceLocation r = new ExternalTextureResourceLocation(f.getAbsolutePath());
                r.loadTexture();
                TEXTURES.put(f.getAbsolutePath(), r);
                return r;
            } else {
                return null;
            }
        } else {
            return (ExternalTextureResourceLocation) TEXTURES.get(f.getAbsolutePath());
        }
    }

    public static WebTextureResourceLocation getWebResource(String url) {
        return getWebResource(url, true);
    }

    public static WebTextureResourceLocation getWebResource(String url, boolean loadTexture) {
        if (!TEXTURES.containsKey(url)) {
            try {
                WebTextureResourceLocation r = new WebTextureResourceLocation(url);
                if (loadTexture) {
                    r.loadTexture();
                    if (!r.isReady()) {
                        return null;
                    }
                }
                TEXTURES.put(url, r);
                return r;
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return (WebTextureResourceLocation) TEXTURES.get(url);
        }
    }

    public static ExternalGifAnimationRenderer getGifResource(String path) {
        File f = new File(path);
        if (!GIFS.containsKey(f.getAbsolutePath())) {
            if (f.exists() && f.isFile() && f.getPath().toLowerCase().replace(" ", "").endsWith(".gif")) {
                ExternalGifAnimationRenderer r = new ExternalGifAnimationRenderer(f.getPath(), true, 0, 0, 0, 0);
                r.prepareAnimation();
                GIFS.put(f.getAbsolutePath(), r);
                return r;
            } else {
                return null;
            }
        } else {
            return (ExternalGifAnimationRenderer) GIFS.get(f.getAbsolutePath());
        }
    }

    public static void removeResource(String path) {
        File f = new File(path);
        if (TEXTURES.containsKey(f.getAbsolutePath())) {
            TEXTURES.remove(f.getAbsolutePath());
        }
    }
}