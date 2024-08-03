package org.violetmoon.quark.content.world.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.block.GlowLichenGrowthBlock;
import org.violetmoon.quark.content.world.block.GlowShroomBlock;
import org.violetmoon.quark.content.world.block.GlowShroomRingBlock;
import org.violetmoon.quark.content.world.block.HugeGlowShroomBlock;
import org.violetmoon.quark.content.world.feature.GlowExtrasFeature;
import org.violetmoon.quark.content.world.feature.GlowShroomsFeature;
import org.violetmoon.zeta.advancement.modifier.AdventuringTimeModifier;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "world")
public class GlimmeringWealdModule extends ZetaModule {

    public static final ResourceLocation BIOME_NAME = new ResourceLocation("quark", "glimmering_weald");

    public static final ResourceKey<Biome> BIOME_KEY = ResourceKey.create(Registries.BIOME, BIOME_NAME);

    public static GlowShroomsFeature glow_shrooms_feature;

    public static GlowExtrasFeature glow_shrooms_extra_feature;

    public static Holder<PlacedFeature> ore_lapis_extra;

    public static Holder<PlacedFeature> placed_glow_shrooms;

    public static Holder<PlacedFeature> placed_glow_extras;

    @Hint
    public static Block glow_shroom;

    @Hint
    public static Block glow_lichen_growth;

    public static Block glow_shroom_block;

    public static Block glow_shroom_stem;

    public static Block glow_shroom_ring;

    public static TagKey<Item> glowShroomFeedablesTag;

    @LoadEvent
    public final void register(ZRegister event) {
        CreativeTabManager.daisyChain();
        glow_shroom = new GlowShroomBlock(this).setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.HANGING_ROOTS, false);
        glow_shroom_block = new HugeGlowShroomBlock("glow_shroom_block", this, true);
        glow_shroom_stem = new HugeGlowShroomBlock("glow_shroom_stem", this, false);
        glow_shroom_ring = new GlowShroomRingBlock(this);
        glow_lichen_growth = new GlowLichenGrowthBlock(this);
        CreativeTabManager.endDaisyChain();
        event.getVariantRegistry().addFlowerPot(glow_lichen_growth, "glow_lichen_growth", prop -> prop.lightLevel(state -> 8));
        event.getVariantRegistry().addFlowerPot(glow_shroom, "glow_shroom", prop -> prop.lightLevel(state -> 10));
        glow_shrooms_feature = new GlowShroomsFeature();
        event.getRegistry().register(glow_shrooms_feature, "glow_shrooms", Registries.FEATURE);
        glow_shrooms_extra_feature = new GlowExtrasFeature();
        event.getRegistry().register(glow_shrooms_extra_feature, "glow_shrooms_extras", Registries.FEATURE);
    }

    @LoadEvent
    public void postRegister(ZRegister.Post e) {
        Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.ParameterPoint climatePoint = Climate.parameters(FULL_RANGE, FULL_RANGE, Climate.Parameter.span(-1.2F, 0.75F), Climate.Parameter.span(-0.28F, 0.08F), Climate.Parameter.span(1.03F, 1.1F), FULL_RANGE, 0.0F);
        Quark.TERRABLENDER_INTEGRATION.registerUndergroundBiome(this, BIOME_NAME, climatePoint);
        Quark.ZETA.advancementModifierRegistry.addModifier(new AdventuringTimeModifier(this, ImmutableSet.of(BIOME_KEY)));
    }

    @LoadEvent
    public void setup(ZCommonSetup e) {
        glowShroomFeedablesTag = ItemTags.create(new ResourceLocation("quark", "glow_shroom_feedables"));
        e.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(glow_shroom.asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(glow_shroom_block.asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(glow_shroom_stem.asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(glow_shroom_ring.asItem(), 0.65F);
            ComposterBlock.COMPOSTABLES.put(glow_lichen_growth.asItem(), 0.5F);
        });
    }
}