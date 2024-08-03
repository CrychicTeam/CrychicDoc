package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import journeymap.client.io.FileHandler;
import journeymap.client.io.IconSetFileHandler;
import journeymap.client.io.ThemeLoader;
import journeymap.client.task.main.ExpireTextureTask;
import journeymap.client.ui.theme.Theme;
import journeymap.common.Journeymap;
import journeymap.common.thread.JMThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class TextureCache {

    public static final ResourceLocation GridCheckers = uiImage("grid-checkers.png");

    public static final ResourceLocation GridDots = uiImage("grid-dots.png");

    public static final ResourceLocation GridSquares = uiImage("grid.png");

    public static final ResourceLocation GridRegionSquares = uiImage("grid-region.png");

    public static final ResourceLocation GridRegion = uiImage("region.png");

    public static final ResourceLocation ColorPicker = uiImage("colorpick.png");

    public static final ResourceLocation ColorPicker2 = uiImage("colorpick2.png");

    public static final ResourceLocation TileSampleDay = uiImage("tile-sample-day.png");

    public static final ResourceLocation TileSampleNight = uiImage("tile-sample-night.png");

    public static final ResourceLocation TileSampleUnderground = uiImage("tile-sample-underground.png");

    public static final ResourceLocation UnknownEntity = uiImage("unknown.png");

    public static final ResourceLocation Deathpoint = uiImage("waypoint-death-icon.png");

    public static final ResourceLocation MobDot = uiImage("marker-dot-32.png");

    public static final ResourceLocation MobDotArrow = uiImage("marker-dot-arrow-32.png");

    public static final ResourceLocation MobDotChevron = uiImage("marker-chevron-32.png");

    public static final ResourceLocation MobIconArrow = uiImage("marker-icon-arrow-32.png");

    public static final ResourceLocation PlayerArrow = uiImage("marker-player-32.png");

    public static final ResourceLocation PlayerArrowBG = uiImage("marker-player-bg-32.png");

    public static final ResourceLocation Logo = uiImage("ico/journeymap.png");

    public static final ResourceLocation MinimapSquare128 = uiImage("minimap/minimap-square-128.png");

    public static final ResourceLocation MinimapSquare256 = uiImage("minimap/minimap-square-256.png");

    public static final ResourceLocation MinimapSquare512 = uiImage("minimap/minimap-square-512.png");

    public static final ResourceLocation Discord = uiImage("discord.png");

    public static final ResourceLocation Waypoint = uiImage("waypoint-icon.png");

    public static final ResourceLocation WaypointEdit = uiImage("waypoint-edit.png");

    public static final ResourceLocation WaypointOffscreen = uiImage("waypoint-offscreen.png");

    private static final Map<String, ResourceLocation> dynamicTextureMap = Collections.synchronizedMap(new HashMap());

    public static final Map<ResourceLocation, ResourceLocation> modTextureMap = Collections.synchronizedMap(new HashMap());

    public static final Map<String, Texture> playerSkins = Collections.synchronizedMap(new HashMap());

    public static final Map<String, Texture> themeImages = Collections.synchronizedMap(new HashMap());

    private static ThreadPoolExecutor texExec = new ThreadPoolExecutor(2, 4, 15L, TimeUnit.SECONDS, new ArrayBlockingQueue(8), new JMThreadFactory("texture"), new CallerRunsPolicy());

    public static final Map<String, ResourceLocation> waypointIconCache = Collections.synchronizedMap(new HashMap<String, ResourceLocation>() {

        {
            this.put(TextureCache.Waypoint.toString(), TextureCache.Waypoint);
            this.put(TextureCache.Deathpoint.toString(), TextureCache.Deathpoint);
        }
    });

    public static ResourceLocation getTexture(String texturePath) {
        ResourceLocation tex = (ResourceLocation) dynamicTextureMap.get(texturePath);
        if (tex == null) {
            tex = uiImage(texturePath);
            dynamicTextureMap.put(texturePath, tex);
        }
        return tex;
    }

    public static ResourceLocation uiImage(String fileName) {
        return new ResourceLocation("journeymap", "ui/img/" + fileName);
    }

    public static Texture getTexture(ResourceLocation location) {
        if (location == null) {
            return null;
        } else {
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            AbstractTexture textureObject = textureManager.getTexture(location, null);
            if (needsNewTexture(textureObject)) {
                textureObject = new SimpleTextureImpl(location);
                textureManager.register(location, textureObject);
            }
            try {
                return (Texture) textureObject;
            } catch (Exception var5) {
                Journeymap.getLogger().error("Not a proper texture for Journeymap:{}", location);
                return (Texture) textureObject;
            }
        }
    }

    public static Texture getWaypointIcon(ResourceLocation location) {
        if ("journeymap".equals(location.getNamespace())) {
            return getTexture(location);
        } else {
            TextureManager manager = Minecraft.getInstance().getTextureManager();
            ResourceLocation fakeResource = (ResourceLocation) modTextureMap.get(location);
            if (fakeResource == null || manager.getTexture(fakeResource, null) == null) {
                fakeResource = new ResourceLocation("fake", location.getPath());
                modTextureMap.put(location, fakeResource);
                try (SimpleTextureImpl simpleTexture = new SimpleTextureImpl(location)) {
                    NativeImage img = ImageUtil.getScaledImage(4.0F, simpleTexture.getNativeImage(), false);
                    DynamicTextureImpl scaledTexture = new DynamicTextureImpl(img, fakeResource);
                    manager.register(fakeResource, scaledTexture);
                    scaledTexture.setDisplayHeight(simpleTexture.getHeight());
                    scaledTexture.setDisplayWidth(simpleTexture.getHeight());
                }
            }
            return (Texture) manager.getTexture(fakeResource, null);
        }
    }

    private static boolean needsNewTexture(AbstractTexture textureObject) {
        if (textureObject == null) {
            return true;
        } else {
            return textureObject instanceof Texture ? !((Texture) textureObject).hasImage() : textureObject instanceof SimpleTexture;
        }
    }

    public static <T extends Texture> Future<T> scheduleTextureTask(Callable<T> textureTask) {
        return texExec.submit(textureTask);
    }

    public static void reset() {
        playerSkins.clear();
        Arrays.asList(ColorPicker, ColorPicker2, Deathpoint, GridCheckers, GridDots, GridSquares, GridRegionSquares, GridRegion, Logo, MinimapSquare128, MinimapSquare256, MinimapSquare512, MobDot, MobDotArrow, MobDotChevron, PlayerArrow, PlayerArrowBG, PlayerArrowBG, TileSampleDay, TileSampleNight, TileSampleUnderground, UnknownEntity, Waypoint, WaypointEdit, WaypointOffscreen).stream().map(TextureCache::getTexture);
        Arrays.asList(ColorPicker, ColorPicker2, GridCheckers, GridDots, GridSquares, GridRegion, GridRegionSquares, TileSampleDay, TileSampleNight, TileSampleUnderground, UnknownEntity).stream().map(TextureCache::getTexture);
    }

    public static void purgeThemeImages(Map<String, Texture> themeImages) {
        synchronized (themeImages) {
            ExpireTextureTask.queue(themeImages.values());
            themeImages.clear();
        }
    }

    public static NativeImage resolveImage(ResourceLocation location) {
        if (location.getNamespace().equals("fake")) {
            return null;
        } else {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            try {
                Resource resource = (Resource) resourceManager.m_213713_(location).orElse(null);
                InputStream is = resource.open();
                return NativeImage.read(is);
            } catch (FileNotFoundException var5) {
                try {
                    if ("journeymap".equals(location.getNamespace())) {
                        Resource imgFile = (Resource) Minecraft.getInstance().getResourceManager().m_213713_(new ResourceLocation("../src/main/resources/assets/journeymap/" + location.getPath())).orElse(null);
                        if (imgFile != null) {
                            return NativeImage.read(imgFile.open());
                        }
                    }
                } catch (IOException var4) {
                    Journeymap.getLogger().warn("Image not found: " + var4.getMessage());
                }
                return null;
            } catch (Exception var6) {
                Journeymap.getLogger().warn("Resource not readable: {}", location);
                return null;
            }
        }
    }

    public static Texture getThemeTexture(Theme theme, String iconPath) {
        return getSizedThemeTexture(theme, iconPath, 0, 0, false, 1.0F);
    }

    public static Texture getSizedThemeTexture(Theme theme, String iconPath, int width, int height, boolean resize, float alpha) {
        String texName = String.format("%s/%s", theme.directory, iconPath);
        synchronized (themeImages) {
            Texture tex = (Texture) themeImages.get(texName);
            if (tex == null || !tex.hasImage() || resize && (width != tex.getWidth() || height != tex.getHeight()) || tex.getAlpha() != alpha) {
                if (tex != null) {
                    tex.remove();
                }
                File parentDir = ThemeLoader.getThemeIconDir();
                NativeImage nativeImage = FileHandler.getIconFromFile(parentDir, theme.directory, iconPath);
                if (nativeImage == null) {
                    String resourcePath = String.format("theme/%s/%s", theme.directory, iconPath);
                    nativeImage = resolveImage(new ResourceLocation("journeymap", resourcePath));
                }
                if (nativeImage == null || nativeImage.pixels <= 0L) {
                    Journeymap.getLogger().error("Unknown theme image: " + texName);
                    IconSetFileHandler.ensureEntityIconSet("Default");
                    return getTexture(UnknownEntity);
                }
                if ((resize || alpha < 1.0F) && (alpha < 1.0F || nativeImage.getWidth() != width || nativeImage.getHeight() != height)) {
                    NativeImage tmp = ImageUtil.getSizedImage(width, height, nativeImage, false);
                    nativeImage.close();
                    nativeImage = tmp;
                }
                tex = new DynamicTextureImpl(nativeImage);
                tex.setAlpha(alpha);
                themeImages.put(texName, tex);
            }
            return tex;
        }
    }

    public static Texture getScaledCopy(String texName, Texture original, int width, int height, float alpha) {
        synchronized (themeImages) {
            Texture tex = (Texture) themeImages.get(texName);
            if (tex == null || !tex.hasImage() || width != tex.getWidth() || height != tex.getHeight() || tex.getAlpha() != alpha) {
                if (original == null) {
                    Journeymap.getLogger().error("Unable to get scaled image: " + texName);
                    return getTexture(UnknownEntity);
                }
                if (!(alpha < 1.0F) && original.getWidth() == width && original.getHeight() == height) {
                    return original;
                }
                tex = new DynamicTextureImpl(ImageUtil.getSizedImage(width, height, original.getNativeImage(), true));
                tex.setAlpha(alpha);
                themeImages.put(texName, tex);
            }
            return tex;
        }
    }

    public static ResourceLocation coloredImageResource(ResourceLocation location, int color) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        ResourceLocation resourceLocation = new ResourceLocation("fake", "color-" + color + "-" + getIconName(location.getPath()) + ".png");
        if (textureManager != null && textureManager.getTexture(resourceLocation, null) == null) {
            SimpleTextureImpl image = new SimpleTextureImpl(location);
            if (!image.hasImage()) {
                return location;
            }
            NativeImage coloredImage = ImageUtil.recolorImage(image.getNativeImage(), color);
            DynamicTextureImpl texture = new DynamicTextureImpl(coloredImage);
            textureManager.register(resourceLocation, texture);
            image.close();
        }
        return resourceLocation;
    }

    private static String getIconName(String path) {
        String[] vals = path.split("/");
        String nameExtension = vals[vals.length - 1];
        return nameExtension.split("\\.")[0];
    }

    public static Texture getPlayerSkin(UUID playerId, String username) {
        Texture tex = null;
        DynamicTextureImpl var8;
        synchronized (playerSkins) {
            tex = (Texture) playerSkins.get(username);
            if (tex != null) {
                return tex;
            }
            NativeImage blank = new NativeImage(24, 24, false);
            var8 = new DynamicTextureImpl(blank);
            playerSkins.put(username, var8);
        }
        NativeImage img = IgnSkin.getFaceImage(playerId, username);
        if (img != null) {
            playerSkins.put(username, new DynamicTextureImpl(img));
        } else {
            playerSkins.remove(username);
        }
        return var8;
    }
}