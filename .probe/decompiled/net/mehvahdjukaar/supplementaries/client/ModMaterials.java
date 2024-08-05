package net.mehvahdjukaar.supplementaries.client;

import com.google.common.base.Suppliers;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.Nullable;

public class ModMaterials {

    public static final ResourceLocation SIGN_SHEET = new ResourceLocation("textures/atlas/signs.png");

    public static final ResourceLocation BANNER_SHEET = new ResourceLocation("textures/atlas/banner_patterns.png");

    public static final Material CLOCK_HAND = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.CLOCK_HAND_TEXTURE);

    public static final Material FISHIES = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.FISHIES_TEXTURE);

    public static final Material BLACKBOARD_OUTLINE = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.BLACKBOARD_GRID);

    public static final Material BELLOWS_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.BELLOWS_TEXTURE);

    public static final Material BUBBLE_BLOCK_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.BUBBLE_BLOCK_TEXTURE);

    public static final Material BLACKBOARD_WHITE = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.BLACKBOARD_WHITE_TEXTURE);

    public static final Material BLACKBOARD_BLACK = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.BLACKBOARD_BLACK_TEXTURE);

    public static final Material SAND_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.SAND_TEXTURE);

    public static final Material CANNON_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.CANNON_TEXTURE);

    public static final Material BOOK_GLINT_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Supplementaries.res("block/books/book_enchanted"));

    public static final Material CANNON_TARGET_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Supplementaries.res("block/cannon_target"));

    public static final Material CANNON_TARGET_RED_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Supplementaries.res("block/cannon_target_charging"));

    public static final Material WIND_VANE_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Supplementaries.res("block/wind_vane"));

    public static final Map<DyeColor, Material> BUNTING_MATERIAL = (Map<DyeColor, Material>) Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), c -> new Material(TextureAtlas.LOCATION_BLOCKS, Supplementaries.res("block/buntings/bunting_" + c.getName()))));

    public static final Supplier<Map<BannerPattern, Material>> FLAG_MATERIALS = Suppliers.memoize(() -> {
        IdentityHashMap<BannerPattern, Material> map = new IdentityHashMap();
        for (Entry<BannerPattern, ResourceLocation> v : ModTextures.FLAG_TEXTURES.entrySet()) {
            map.put((BannerPattern) v.getKey(), new Material(BANNER_SHEET, (ResourceLocation) v.getValue()));
        }
        return map;
    });

    private static final Map<BannerPatternItem, BannerPattern> ITEM_TO_PATTERNS = new IdentityHashMap();

    private static final Cache<ResourceLocation, Material> CACHED_MATERIALS = CacheBuilder.newBuilder().expireAfterAccess(2L, TimeUnit.MINUTES).build();

    @Nullable
    public static Material getFlagMaterialForPatternItem(BannerPatternItem item) {
        BannerPattern p = (BannerPattern) ITEM_TO_PATTERNS.get(item);
        if (p == null) {
            Iterator var2 = ((HolderSet.Named) BuiltInRegistries.BANNER_PATTERN.getTag(item.getBannerPattern()).get()).iterator();
            if (var2.hasNext()) {
                Holder<BannerPattern> j = (Holder<BannerPattern>) var2.next();
                ITEM_TO_PATTERNS.put(item, j.value());
                return (Material) ((Map) FLAG_MATERIALS.get()).get(j.value());
            } else {
                return null;
            }
        } else {
            return (Material) ((Map) FLAG_MATERIALS.get()).get(p);
        }
    }

    public static Material get(ResourceLocation bockTexture) {
        try {
            return (Material) CACHED_MATERIALS.get(bockTexture, () -> new Material(TextureAtlas.LOCATION_BLOCKS, bockTexture));
        } catch (ExecutionException var2) {
            throw new RuntimeException(var2);
        }
    }
}