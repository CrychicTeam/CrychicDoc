package net.minecraft.client.renderer.texture.atlas;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.renderer.texture.atlas.sources.SourceFilter;
import net.minecraft.client.renderer.texture.atlas.sources.Unstitcher;
import net.minecraft.resources.ResourceLocation;

public class SpriteSources {

    private static final BiMap<ResourceLocation, SpriteSourceType> TYPES = HashBiMap.create();

    public static final SpriteSourceType SINGLE_FILE = register("single", SingleFile.CODEC);

    public static final SpriteSourceType DIRECTORY = register("directory", DirectoryLister.CODEC);

    public static final SpriteSourceType FILTER = register("filter", SourceFilter.CODEC);

    public static final SpriteSourceType UNSTITCHER = register("unstitch", Unstitcher.CODEC);

    public static final SpriteSourceType PALETTED_PERMUTATIONS = register("paletted_permutations", PalettedPermutations.CODEC);

    public static Codec<SpriteSourceType> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(p_274717_ -> {
        SpriteSourceType $$1 = (SpriteSourceType) TYPES.get(p_274717_);
        return $$1 != null ? DataResult.success($$1) : DataResult.error(() -> "Unknown type " + p_274717_);
    }, p_274716_ -> {
        ResourceLocation $$1 = (ResourceLocation) TYPES.inverse().get(p_274716_);
        return p_274716_ != null ? DataResult.success($$1) : DataResult.error(() -> "Unknown type " + $$1);
    });

    public static Codec<SpriteSource> CODEC = TYPE_CODEC.dispatch(SpriteSource::m_260850_, SpriteSourceType::f_260449_);

    public static Codec<List<SpriteSource>> FILE_CODEC = CODEC.listOf().fieldOf("sources").codec();

    private static SpriteSourceType register(String string0, Codec<? extends SpriteSource> codecExtendsSpriteSource1) {
        SpriteSourceType $$2 = new SpriteSourceType(codecExtendsSpriteSource1);
        ResourceLocation $$3 = new ResourceLocation(string0);
        SpriteSourceType $$4 = (SpriteSourceType) TYPES.putIfAbsent($$3, $$2);
        if ($$4 != null) {
            throw new IllegalStateException("Duplicate registration " + $$3);
        } else {
            return $$2;
        }
    }
}