package net.mehvahdjukaar.supplementaries.client;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.misc.globe.GlobeData;
import net.mehvahdjukaar.supplementaries.common.utils.Credits;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GlobeManager {

    private static final Map<String, GlobeManager.TextureInstance> TEXTURE_CACHE = Maps.newHashMap();

    private static final HashMap<ResourceLocation, IntList> DIMENSION_COLOR_MAP = new HashMap();

    private static final IntList SEPIA_COLORS = new IntArrayList();

    public static void refreshTextures() {
        TEXTURE_CACHE.clear();
    }

    public static RenderType getRenderType(Level world, boolean sepia) {
        return getTextureInstance(world, sepia).renderType;
    }

    private static GlobeManager.TextureInstance getTextureInstance(Level world, boolean sepia) {
        return (GlobeManager.TextureInstance) TEXTURE_CACHE.computeIfAbsent(getTextureId(world, sepia), i -> new GlobeManager.TextureInstance(world, sepia));
    }

    private static String getTextureId(Level level, boolean sepia) {
        String id = level.dimension().location().getPath();
        if (sepia) {
            id = id + "_sepia";
        }
        return id;
    }

    public static void refreshColorsAndTextures(ResourceManager manager) {
        GlobeManager.Type.recomputeCache();
        DIMENSION_COLOR_MAP.clear();
        int targetColors = 13;
        for (ResourceLocation res : manager.listResources("textures/entity/globes/palettes", r -> r.getPath().endsWith(".png")).keySet()) {
            List<Integer> l = SpriteUtils.parsePaletteStrip(manager, res, targetColors);
            String name = res.getPath();
            name = name.substring(name.lastIndexOf("/") + 1).replace(".png", "");
            if (name.equals("sepia")) {
                SEPIA_COLORS.clear();
                SEPIA_COLORS.addAll(l);
            } else {
                DIMENSION_COLOR_MAP.put(new ResourceLocation(name.replace(".", ":")), new IntArrayList(l));
            }
        }
        if (DIMENSION_COLOR_MAP.isEmpty()) {
            Supplementaries.LOGGER.error("Could not find any globe palette in textures/entity/globes/palettes");
        }
        refreshTextures();
    }

    public static enum Model {

        GLOBE, FLAT, SNOW, SHEARED
    }

    private static class TextureInstance implements AutoCloseable {

        private final ResourceLocation textureLocation;

        private final DynamicTexture texture;

        private final RenderType renderType;

        private final ResourceLocation dimensionId;

        private final boolean sepia;

        private TextureInstance(Level world, boolean sepia) {
            this.sepia = sepia;
            this.dimensionId = world.dimension().location();
            RenderUtil.setDynamicTexturesToUseMipmap(true);
            this.texture = new DynamicTexture(32, 16, false);
            RenderUtil.setDynamicTexturesToUseMipmap(false);
            this.updateTexture(world);
            this.textureLocation = Minecraft.getInstance().getTextureManager().register("globe/" + this.dimensionId.toString().replace(":", "_"), this.texture);
            this.renderType = RenderUtil.getEntitySolidMipmapRenderType(this.textureLocation);
        }

        private void updateTexture(Level world) {
            GlobeData data = GlobeData.get(world);
            if (data != null) {
                byte[][] pixels = data.globePixels;
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 32; j++) {
                        this.texture.getPixels().setPixelRGBA(j, i, -13061505);
                    }
                }
                for (int y = 0; y < pixels.length; y++) {
                    for (int x = 0; x < pixels[y].length; x++) {
                        this.texture.getPixels().setPixelRGBA(y, x, getRGBA(pixels[y][x], this.dimensionId, this.sepia));
                    }
                }
                RenderUtil.setDynamicTexturesToUseMipmap(true);
                this.texture.upload();
                RenderUtil.setDynamicTexturesToUseMipmap(false);
            }
        }

        public void close() {
            this.texture.close();
            Minecraft.getInstance().getTextureManager().release(this.textureLocation);
        }

        private static int getRGBA(byte b, ResourceLocation dimension, boolean sepia) {
            if (sepia) {
                return GlobeManager.SEPIA_COLORS.getInt(b);
            } else {
                IntList l = (IntList) GlobeManager.DIMENSION_COLOR_MAP.getOrDefault(dimension, (IntList) GlobeManager.DIMENSION_COLOR_MAP.get(new ResourceLocation("overworld")));
                return l != null ? l.getInt(b) : 1;
            }
        }
    }

    public static enum Type {

        FLAT(new String[] { "flat", "flat earth" }, Component.translatable("globe.supplementaries.flat"), ModTextures.GLOBE_FLAT_TEXTURE), MOON(new String[] { "moon", "luna", "selene", "cynthia" }, Component.translatable("globe.supplementaries.moon"), ModTextures.GLOBE_MOON_TEXTURE), EARTH(new String[] { "earth", "terra", "gaia", "gaea", "tierra", "tellus", "terre" }, Component.translatable("globe.supplementaries.earth"), ModTextures.GLOBE_TEXTURE), SUN(new String[] { "sun", "sol", "helios" }, Component.translatable("globe.supplementaries.sun"), ModTextures.GLOBE_SUN_TEXTURE);

        private final String[] keyWords;

        public final Component transKeyWord;

        public final ResourceLocation texture;

        private static final Map<String, Pair<GlobeManager.Model, ResourceLocation>> nameCache = new HashMap();

        private static final Map<String, Integer> idMap = new HashMap();

        public static final List<ResourceLocation> textures = new ArrayList();

        private Type(String[] key, Component tr, ResourceLocation res) {
            this.keyWords = key;
            this.transKeyWord = tr;
            this.texture = res;
        }

        public static void recomputeCache() {
            nameCache.clear();
            for (GlobeManager.Type type : values()) {
                GlobeManager.Model model = type == FLAT ? GlobeManager.Model.FLAT : GlobeManager.Model.GLOBE;
                Pair<GlobeManager.Model, ResourceLocation> pair = Pair.of(model, type.texture);
                if (type.transKeyWord != null && !type.transKeyWord.getString().equals("")) {
                    nameCache.put(type.transKeyWord.getString().toLowerCase(Locale.ROOT), pair);
                }
                for (String s : type.keyWords) {
                    if (!s.equals("")) {
                        nameCache.put(s, pair);
                    }
                }
            }
            for (Entry<String, ResourceLocation> g : Credits.INSTANCE.globes().entrySet()) {
                ResourceLocation path = (ResourceLocation) g.getValue();
                GlobeManager.Model model = GlobeManager.Model.GLOBE;
                if (path.getPath().contains("globe_wais")) {
                    model = GlobeManager.Model.SNOW;
                }
                nameCache.put((String) g.getKey(), Pair.of(model, path));
            }
            textures.clear();
            nameCache.values().forEach(o -> {
                if (!textures.contains(o.getSecond())) {
                    textures.add((ResourceLocation) o.getSecond());
                }
            });
            Collections.sort(textures);
            idMap.clear();
            nameCache.forEach((key, value) -> idMap.put(key, textures.indexOf(value.getSecond())));
        }

        @Nullable
        public static Pair<GlobeManager.Model, ResourceLocation> getModelAndTexture(String text) {
            return (Pair<GlobeManager.Model, ResourceLocation>) nameCache.get(text.toLowerCase(Locale.ROOT));
        }

        @Nullable
        public static Integer getTextureID(String text) {
            return (Integer) idMap.get(text.toLowerCase(Locale.ROOT));
        }
    }
}