package net.mehvahdjukaar.moonlight.api.client;

import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class TextureCache {

    private static final Map<ItemLike, Set<Pair<String, String>>> SPECIAL_TEXTURES = new IdentityHashMap();

    private static final Map<ItemLike, Set<String>> CACHED_TEXTURES = new IdentityHashMap();

    public static void registerSpecialTextureForBlock(ItemLike block, String id, ResourceLocation texturePath) {
        ((Set) SPECIAL_TEXTURES.computeIfAbsent(block, b -> new HashSet())).add(new Pair(id, texturePath.toString()));
    }

    public static void clear() {
        CACHED_TEXTURES.clear();
    }

    @Nullable
    public static String getCached(ItemLike block, Predicate<String> texturePredicate) {
        Set<Pair<String, String>> special = (Set<Pair<String, String>>) SPECIAL_TEXTURES.get(block);
        if (special != null) {
            for (Pair<String, String> e : special) {
                if (texturePredicate.test((String) e.getFirst())) {
                    return (String) e.getSecond();
                }
            }
        }
        Set<String> list = (Set<String>) CACHED_TEXTURES.get(block);
        if (list != null) {
            for (String ex : list) {
                if (texturePredicate.test(ex)) {
                    return ex;
                }
            }
        }
        return null;
    }

    public static void add(ItemLike block, String t) {
        ((Set) CACHED_TEXTURES.computeIfAbsent(block, b -> new HashSet())).add(t);
    }
}