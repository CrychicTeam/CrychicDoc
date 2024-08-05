package net.mehvahdjukaar.supplementaries.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlackboardManager {

    private static final LoadingCache<BlackboardManager.Key, BlackboardManager.Blackboard> TEXTURE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(2L, TimeUnit.MINUTES).removalListener(i -> {
        BlackboardManager.Blackboard value = (BlackboardManager.Blackboard) i.getValue();
        if (value != null) {
            RenderSystem.recordRenderCall(value::close);
        }
    }).build(new CacheLoader<BlackboardManager.Key, BlackboardManager.Blackboard>() {

        public BlackboardManager.Blackboard load(BlackboardManager.Key key) {
            return null;
        }
    });

    public static BlackboardManager.Blackboard getInstance(BlackboardManager.Key key) {
        BlackboardManager.Blackboard textureInstance = (BlackboardManager.Blackboard) TEXTURE_CACHE.getIfPresent(key);
        if (textureInstance == null) {
            textureInstance = new BlackboardManager.Blackboard(BlackboardBlockTile.unpackPixels(key.values), key.glow);
            TEXTURE_CACHE.put(key, textureInstance);
        }
        return textureInstance;
    }

    public static class Blackboard implements AutoCloseable {

        private static final int WIDTH = 16;

        private final Map<Direction, List<BakedQuad>> quadsCache = new EnumMap(Direction.class);

        private final byte[][] pixels;

        private final boolean glow;

        @Nullable
        private DynamicTexture texture;

        @Nullable
        private RenderType renderType;

        @Nullable
        private ResourceLocation textureLocation;

        private Blackboard(byte[][] pixels, boolean glow) {
            this.pixels = pixels;
            this.glow = glow;
        }

        public byte[][] getPixels() {
            return this.pixels;
        }

        public boolean isGlow() {
            return this.glow;
        }

        private void initializeTexture() {
            this.texture = new DynamicTexture(16, 16, false);
            for (int y = 0; y < this.pixels.length && y < 16; y++) {
                for (int x = 0; x < this.pixels[y].length && x < 16; x++) {
                    this.texture.getPixels().setPixelRGBA(x, y, getColoredPixel(this.pixels[x][y], x, y));
                }
            }
            this.texture.upload();
            this.textureLocation = Minecraft.getInstance().getTextureManager().register("blackboard/", this.texture);
            this.renderType = RenderType.entitySolid(this.textureLocation);
        }

        private static int getColoredPixel(byte i, int x, int y) {
            int offset = i > 0 ? 16 : 0;
            int tint = BlackboardBlock.colorFromByte(i);
            TextureAtlas textureMap = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
            TextureAtlasSprite sprite = textureMap.getSprite(ModTextures.BLACKBOARD_TEXTURE);
            return getTintedColor(sprite, x, y, offset, tint);
        }

        private static int getTintedColor(TextureAtlasSprite sprite, int x, int y, int offset, int tint) {
            if (sprite != null && sprite.contents().getFrameCount() != 0) {
                int tintR = FastColor.ABGR32.red(tint);
                int tintG = FastColor.ABGR32.green(tint);
                int tintB = FastColor.ABGR32.blue(tint);
                int pixel = ClientHelper.getPixelRGBA(sprite, 0, Math.min(sprite.contents().width() - 1, x + offset), Math.min(sprite.contents().height() - 1, y));
                int totalB = FastColor.ARGB32.blue(pixel);
                int totalG = FastColor.ARGB32.green(pixel);
                int totalR = FastColor.ARGB32.red(pixel);
                return FastColor.ARGB32.color(255, totalR * tintR / 255, totalG * tintG / 255, totalB * tintB / 255);
            } else {
                return -1;
            }
        }

        @NotNull
        public List<BakedQuad> getOrCreateModel(Direction dir, BiFunction<BlackboardManager.Blackboard, Direction, List<BakedQuad>> modelFactory) {
            return (List<BakedQuad>) this.quadsCache.computeIfAbsent(dir, d -> (List) modelFactory.apply(this, d));
        }

        @NotNull
        public ResourceLocation getTextureLocation() {
            if (this.textureLocation == null) {
                this.initializeTexture();
            }
            return this.textureLocation;
        }

        @NotNull
        public RenderType getRenderType() {
            if (this.renderType == null) {
                this.initializeTexture();
            }
            return this.renderType;
        }

        public void close() {
            if (this.texture != null) {
                this.texture.close();
            }
            if (this.textureLocation != null) {
                Minecraft.getInstance().getTextureManager().release(this.textureLocation);
            }
        }
    }

    public static class Key implements TooltipComponent {

        private final long[] values;

        private final boolean glow;

        Key(long[] packed, boolean glowing) {
            this.values = packed;
            this.glow = glowing;
        }

        public static BlackboardManager.Key of(long[] packPixels, boolean glowing) {
            return new BlackboardManager.Key(packPixels, glowing);
        }

        public static BlackboardManager.Key of(long[] packPixels) {
            return new BlackboardManager.Key(packPixels, false);
        }

        public boolean equals(Object another) {
            if (another == this) {
                return true;
            } else if (another == null) {
                return false;
            } else if (another.getClass() != this.getClass()) {
                return false;
            } else {
                BlackboardManager.Key key = (BlackboardManager.Key) another;
                return Arrays.equals(this.values, key.values) && this.glow == key.glow;
            }
        }

        public int hashCode() {
            return Arrays.hashCode(this.values);
        }
    }
}