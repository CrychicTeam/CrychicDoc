package com.simibubi.create.content.decoration.palettes;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import java.util.function.Function;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.ForgeRegistries;

public enum AllPaletteStoneTypes {

    GRANITE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.GRANITE),
    DIORITE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.DIORITE),
    ANDESITE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.ANDESITE),
    CALCITE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.CALCITE),
    DRIPSTONE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.DRIPSTONE_BLOCK),
    DEEPSLATE(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.DEEPSLATE),
    TUFF(PaletteBlockPattern.VANILLA_RANGE, r -> () -> Blocks.TUFF),
    ASURINE(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("asurine", () -> Blocks.DEEPSLATE, true, true).properties(p -> p.destroyTime(1.25F).mapColor(MapColor.COLOR_BLUE)).register()),
    CRIMSITE(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("crimsite", () -> Blocks.DEEPSLATE, true, true).properties(p -> p.destroyTime(1.25F).mapColor(MapColor.COLOR_RED)).register()),
    LIMESTONE(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("limestone", () -> Blocks.SANDSTONE, true, false).properties(p -> p.destroyTime(1.25F).mapColor(MapColor.SAND)).register()),
    OCHRUM(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("ochrum", () -> Blocks.CALCITE, true, true).properties(p -> p.destroyTime(1.25F).mapColor(MapColor.TERRACOTTA_YELLOW)).register()),
    SCORIA(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("scoria", () -> Blocks.BLACKSTONE, true, false).properties(p -> p.mapColor(MapColor.COLOR_BROWN)).register()),
    SCORCHIA(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("scorchia", () -> Blocks.BLACKSTONE, true, false).properties(p -> p.mapColor(MapColor.TERRACOTTA_GRAY)).register()),
    VERIDIUM(PaletteBlockPattern.STANDARD_RANGE, r -> r.paletteStoneBlock("veridium", () -> Blocks.TUFF, true, true).properties(p -> p.destroyTime(1.25F).mapColor(MapColor.WARPED_NYLIUM)).register());

    private Function<CreateRegistrate, NonNullSupplier<Block>> factory;

    private PalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;

    public PaletteBlockPattern[] variantTypes;

    public TagKey<Item> materialTag;

    private AllPaletteStoneTypes(PaletteBlockPattern[] variantTypes, Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
        this.factory = factory;
        this.variantTypes = variantTypes;
    }

    public NonNullSupplier<Block> getBaseBlock() {
        return this.baseBlock;
    }

    public PalettesVariantEntry getVariants() {
        return this.variants;
    }

    public static void register(CreateRegistrate registrate) {
        for (AllPaletteStoneTypes paletteStoneVariants : values()) {
            NonNullSupplier<Block> baseBlock = (NonNullSupplier<Block>) paletteStoneVariants.factory.apply(registrate);
            paletteStoneVariants.baseBlock = baseBlock;
            String id = Lang.asId(paletteStoneVariants.name());
            paletteStoneVariants.materialTag = AllTags.optionalTag(ForgeRegistries.ITEMS, Create.asResource("stone_types/" + id));
            paletteStoneVariants.variants = new PalettesVariantEntry(id, paletteStoneVariants);
        }
    }
}