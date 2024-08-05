package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BannerPatternTagGenerator extends TagsProvider<BannerPattern> {

    public static final TagKey<BannerPattern> FIRE_BANNER_PATTERN = create("pattern_item/fire");

    public static final TagKey<BannerPattern> ICE_BANNER_PATTERN = create("pattern_item/ice");

    public static final TagKey<BannerPattern> LIGHTNING_BANNER_PATTERN = create("pattern_item/lightning");

    public static final TagKey<BannerPattern> FIRE_HEAD_BANNER_PATTERN = create("pattern_item/fire_head");

    public static final TagKey<BannerPattern> ICE_HEAD_BANNER_PATTERN = create("pattern_item/ice_head");

    public static final TagKey<BannerPattern> LIGHTNING_HEAD_BANNER_PATTERN = create("pattern_item/lightning_head");

    public static final TagKey<BannerPattern> AMPHITHERE_BANNER_PATTERN = create("pattern_item/amphithere");

    public static final TagKey<BannerPattern> BIRD_BANNER_PATTERN = create("pattern_item/bird");

    public static final TagKey<BannerPattern> EYE_BANNER_PATTERN = create("pattern_item/eye");

    public static final TagKey<BannerPattern> FAE_BANNER_PATTERN = create("pattern_item/fae");

    public static final TagKey<BannerPattern> FEATHER_BANNER_PATTERN = create("pattern_item/feather");

    public static final TagKey<BannerPattern> GORGON_BANNER_PATTERN = create("pattern_item/gorgon");

    public static final TagKey<BannerPattern> HIPPOCAMPUS_BANNER_PATTERN = create("pattern_item/hippocampus");

    public static final TagKey<BannerPattern> HIPPOGRYPH_HEAD_BANNER_PATTERN = create("pattern_item/hippogryph_head");

    public static final TagKey<BannerPattern> MERMAID_BANNER_PATTERN = create("pattern_item/mermaid");

    public static final TagKey<BannerPattern> SEA_SERPENT_BANNER_PATTERN = create("pattern_item/sea_serpent");

    public static final TagKey<BannerPattern> TROLL_BANNER_PATTERN = create("pattern_item/troll");

    public static final TagKey<BannerPattern> WEEZER_BANNER_PATTERN = create("pattern_item/weezer");

    public static final TagKey<BannerPattern> DREAD_BANNER_PATTERN = create("pattern_item/dread");

    public BannerPatternTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BANNER_PATTERN, provider, "iceandfire", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(FIRE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE.getKey());
        this.m_206424_(ICE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE.getKey());
        this.m_206424_(LIGHTNING_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING.getKey());
        this.m_206424_(FIRE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE_HEAD.getKey());
        this.m_206424_(ICE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE_HEAD.getKey());
        this.m_206424_(LIGHTNING_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING_HEAD.getKey());
        this.m_206424_(AMPHITHERE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_AMPHITHERE.getKey());
        this.m_206424_(BIRD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_BIRD.getKey());
        this.m_206424_(EYE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_EYE.getKey());
        this.m_206424_(FAE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FAE.getKey());
        this.m_206424_(FEATHER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FEATHER.getKey());
        this.m_206424_(GORGON_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_GORGON.getKey());
        this.m_206424_(HIPPOCAMPUS_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOCAMPUS.getKey());
        this.m_206424_(HIPPOGRYPH_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOGRYPH_HEAD.getKey());
        this.m_206424_(MERMAID_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_MERMAID.getKey());
        this.m_206424_(SEA_SERPENT_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_SEA_SERPENT.getKey());
        this.m_206424_(TROLL_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_TROLL.getKey());
        this.m_206424_(WEEZER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_WEEZER.getKey());
        this.m_206424_(DREAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_DREAD.getKey());
    }

    private static TagKey<BannerPattern> create(String name) {
        return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation("iceandfire", name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Banner Pattern Tags";
    }
}