package com.simibubi.create.content.decoration.palettes;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.WindowGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

public class AllPaletteBlocks {

    public static final BlockEntry<GlassBlock> TILED_GLASS = ((BlockBuilder) Create.REGISTRATE.block("tiled_glass", GlassBlock::new).initialProperties(() -> Blocks.GLASS).addLayer(() -> RenderType::m_110463_).recipe((c, p) -> p.stonecutting(DataIngredient.tag(Tags.Items.GLASS_COLORLESS), RecipeCategory.BUILDING_BLOCKS, c::get)).blockstate((c, p) -> BlockStateGen.cubeAll(c, p, "palettes/")).tag(new TagKey[] { Tags.Blocks.GLASS_COLORLESS, BlockTags.IMPERMEABLE }).item().tag(new TagKey[] { Tags.Items.GLASS_COLORLESS }).build()).register();

    public static final BlockEntry<ConnectedGlassBlock> FRAMED_GLASS = WindowGen.framedGlass("framed_glass", () -> new SimpleCTBehaviour(AllSpriteShifts.FRAMED_GLASS));

    public static final BlockEntry<ConnectedGlassBlock> HORIZONTAL_FRAMED_GLASS = WindowGen.framedGlass("horizontal_framed_glass", () -> new HorizontalCTBehaviour(AllSpriteShifts.HORIZONTAL_FRAMED_GLASS, AllSpriteShifts.FRAMED_GLASS));

    public static final BlockEntry<ConnectedGlassBlock> VERTICAL_FRAMED_GLASS = WindowGen.framedGlass("vertical_framed_glass", () -> new HorizontalCTBehaviour(AllSpriteShifts.VERTICAL_FRAMED_GLASS));

    public static final BlockEntry<GlassPaneBlock> TILED_GLASS_PANE = WindowGen.standardGlassPane("tiled_glass", TILED_GLASS, Create.asResource("block/palettes/tiled_glass"), new ResourceLocation("block/glass_pane_top"), () -> RenderType::m_110457_);

    public static final BlockEntry<ConnectedGlassPaneBlock> FRAMED_GLASS_PANE = WindowGen.framedGlassPane("framed_glass", FRAMED_GLASS, () -> AllSpriteShifts.FRAMED_GLASS);

    public static final BlockEntry<ConnectedGlassPaneBlock> HORIZONTAL_FRAMED_GLASS_PANE = WindowGen.framedGlassPane("horizontal_framed_glass", HORIZONTAL_FRAMED_GLASS, () -> AllSpriteShifts.HORIZONTAL_FRAMED_GLASS);

    public static final BlockEntry<ConnectedGlassPaneBlock> VERTICAL_FRAMED_GLASS_PANE = WindowGen.framedGlassPane("vertical_framed_glass", VERTICAL_FRAMED_GLASS, () -> AllSpriteShifts.VERTICAL_FRAMED_GLASS);

    public static final BlockEntry<WindowBlock> OAK_WINDOW = WindowGen.woodenWindowBlock(WoodType.OAK, Blocks.OAK_PLANKS);

    public static final BlockEntry<WindowBlock> SPRUCE_WINDOW = WindowGen.woodenWindowBlock(WoodType.SPRUCE, Blocks.SPRUCE_PLANKS);

    public static final BlockEntry<WindowBlock> BIRCH_WINDOW = WindowGen.woodenWindowBlock(WoodType.BIRCH, Blocks.BIRCH_PLANKS, () -> RenderType::m_110466_, true);

    public static final BlockEntry<WindowBlock> JUNGLE_WINDOW = WindowGen.woodenWindowBlock(WoodType.JUNGLE, Blocks.JUNGLE_PLANKS);

    public static final BlockEntry<WindowBlock> ACACIA_WINDOW = WindowGen.woodenWindowBlock(WoodType.ACACIA, Blocks.ACACIA_PLANKS);

    public static final BlockEntry<WindowBlock> DARK_OAK_WINDOW = WindowGen.woodenWindowBlock(WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS);

    public static final BlockEntry<WindowBlock> MANGROVE_WINDOW = WindowGen.woodenWindowBlock(WoodType.MANGROVE, Blocks.MANGROVE_PLANKS);

    public static final BlockEntry<WindowBlock> CRIMSON_WINDOW = WindowGen.woodenWindowBlock(WoodType.CRIMSON, Blocks.CRIMSON_PLANKS);

    public static final BlockEntry<WindowBlock> WARPED_WINDOW = WindowGen.woodenWindowBlock(WoodType.WARPED, Blocks.WARPED_PLANKS);

    public static final BlockEntry<WindowBlock> ORNATE_IRON_WINDOW = WindowGen.customWindowBlock("ornate_iron_window", () -> Items.IRON_NUGGET, () -> AllSpriteShifts.ORNATE_IRON_WINDOW, () -> RenderType::m_110463_, false, () -> MapColor.TERRACOTTA_LIGHT_GRAY);

    public static final BlockEntry<ConnectedGlassPaneBlock> OAK_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.OAK, OAK_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> SPRUCE_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.SPRUCE, SPRUCE_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> BIRCH_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.BIRCH, BIRCH_WINDOW, () -> RenderType::m_110466_);

    public static final BlockEntry<ConnectedGlassPaneBlock> JUNGLE_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.JUNGLE, JUNGLE_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> ACACIA_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.ACACIA, ACACIA_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> DARK_OAK_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.DARK_OAK, DARK_OAK_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> MANGROVE_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.MANGROVE, MANGROVE_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> CRIMSON_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.CRIMSON, CRIMSON_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> WARPED_WINDOW_PANE = WindowGen.woodenWindowPane(WoodType.WARPED, WARPED_WINDOW);

    public static final BlockEntry<ConnectedGlassPaneBlock> ORNATE_IRON_WINDOW_PANE = WindowGen.customWindowPane("ornate_iron_window", ORNATE_IRON_WINDOW, () -> AllSpriteShifts.ORNATE_IRON_WINDOW, () -> RenderType::m_110457_);

    public static void register() {
    }

    static {
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.PALETTES_CREATIVE_TAB);
        AllPaletteStoneTypes.register(Create.REGISTRATE);
    }
}