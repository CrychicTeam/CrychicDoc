package com.simibubi.create.foundation.data;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassBlock;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.GlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.WindowBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.simibubi.create.foundation.block.connected.GlassPaneCTBehaviour;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;

public class WindowGen {

    private static BlockBehaviour.Properties glassProperties(BlockBehaviour.Properties p) {
        return p.isValidSpawn(WindowGen::never).isRedstoneConductor(WindowGen::never).isSuffocating(WindowGen::never).isViewBlocking(WindowGen::never);
    }

    private static boolean never(BlockState p_235436_0_, BlockGetter p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }

    private static Boolean never(BlockState p_235427_0_, BlockGetter p_235427_1_, BlockPos p_235427_2_, EntityType<?> p_235427_3_) {
        return false;
    }

    public static BlockEntry<WindowBlock> woodenWindowBlock(WoodType woodType, Block planksBlock) {
        return woodenWindowBlock(woodType, planksBlock, () -> RenderType::m_110457_, false);
    }

    public static BlockEntry<WindowBlock> customWindowBlock(String name, Supplier<? extends ItemLike> ingredient, Supplier<CTSpriteShiftEntry> ct, Supplier<Supplier<RenderType>> renderType, boolean translucent, Supplier<MapColor> color) {
        NonNullFunction<String, ResourceLocation> end_texture = n -> Create.asResource(palettesDir() + name + "_end");
        NonNullFunction<String, ResourceLocation> side_texture = n -> Create.asResource(palettesDir() + n);
        return windowBlock(name, ingredient, ct, renderType, translucent, end_texture, side_texture, color);
    }

    public static BlockEntry<WindowBlock> woodenWindowBlock(WoodType woodType, Block planksBlock, Supplier<Supplier<RenderType>> renderType, boolean translucent) {
        String woodName = woodType.name();
        String name = woodName + "_window";
        NonNullFunction<String, ResourceLocation> end_texture = $ -> new ResourceLocation("block/" + woodName + "_planks");
        NonNullFunction<String, ResourceLocation> side_texture = n -> Create.asResource(palettesDir() + n);
        return windowBlock(name, () -> planksBlock, () -> AllSpriteShifts.getWoodenWindow(woodType), renderType, translucent, end_texture, side_texture, planksBlock::m_284356_);
    }

    public static BlockEntry<WindowBlock> windowBlock(String name, Supplier<? extends ItemLike> ingredient, Supplier<CTSpriteShiftEntry> ct, Supplier<Supplier<RenderType>> renderType, boolean translucent, NonNullFunction<String, ResourceLocation> endTexture, NonNullFunction<String, ResourceLocation> sideTexture, Supplier<MapColor> color) {
        return ((BlockBuilder) Create.REGISTRATE.block(name, p -> new WindowBlock(p, translucent)).onRegister(CreateRegistrate.connectedTextures(() -> new HorizontalCTBehaviour((CTSpriteShiftEntry) ct.get())))).addLayer(renderType).recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike) c.get(), 2).pattern(" # ").pattern("#X#").define('#', (ItemLike) ingredient.get()).define('X', DataIngredient.tag(Tags.Items.GLASS_COLORLESS)).unlockedBy("has_ingredient", RegistrateRecipeProvider.m_125977_((ItemLike) ingredient.get())).m_176498_(p::accept)).initialProperties(() -> Blocks.GLASS).properties(WindowGen::glassProperties).properties(p -> p.mapColor((MapColor) color.get())).loot((t, g) -> t.m_245644_(g)).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().cubeColumn(c.getName(), (ResourceLocation) sideTexture.apply(c.getName()), (ResourceLocation) endTexture.apply(c.getName())))).tag(new TagKey[] { BlockTags.IMPERMEABLE }).simpleItem().register();
    }

    public static BlockEntry<ConnectedGlassBlock> framedGlass(String name, Supplier<ConnectedTextureBehaviour> behaviour) {
        return ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(name, ConnectedGlassBlock::new).onRegister(CreateRegistrate.connectedTextures(behaviour))).addLayer(() -> RenderType::m_110463_).initialProperties(() -> Blocks.GLASS).properties(WindowGen::glassProperties).loot((t, g) -> t.m_245644_(g)).recipe((c, p) -> p.stonecutting(DataIngredient.tag(Tags.Items.GLASS_COLORLESS), RecipeCategory.BUILDING_BLOCKS, c::get)).blockstate((c, p) -> BlockStateGen.cubeAll(c, p, "palettes/", "framed_glass")).tag(new TagKey[] { Tags.Blocks.GLASS_COLORLESS, BlockTags.IMPERMEABLE }).item().tag(new TagKey[] { Tags.Items.GLASS_COLORLESS }).model((c, p) -> p.cubeColumn(c.getName(), p.modLoc(palettesDir() + c.getName()), p.modLoc("block/palettes/framed_glass"))).build()).register();
    }

    public static BlockEntry<ConnectedGlassPaneBlock> framedGlassPane(String name, Supplier<? extends Block> parent, Supplier<CTSpriteShiftEntry> ctshift) {
        ResourceLocation sideTexture = Create.asResource(palettesDir() + "framed_glass");
        ResourceLocation itemSideTexture = Create.asResource(palettesDir() + name);
        ResourceLocation topTexture = Create.asResource(palettesDir() + "framed_glass_pane_top");
        Supplier<Supplier<RenderType>> renderType = () -> RenderType::m_110457_;
        return connectedGlassPane(name, parent, ctshift, sideTexture, itemSideTexture, topTexture, renderType);
    }

    public static BlockEntry<ConnectedGlassPaneBlock> customWindowPane(String name, Supplier<? extends Block> parent, Supplier<CTSpriteShiftEntry> ctshift, Supplier<Supplier<RenderType>> renderType) {
        ResourceLocation topTexture = Create.asResource(palettesDir() + name + "_pane_top");
        ResourceLocation sideTexture = Create.asResource(palettesDir() + name);
        return connectedGlassPane(name, parent, ctshift, sideTexture, sideTexture, topTexture, renderType);
    }

    public static BlockEntry<ConnectedGlassPaneBlock> woodenWindowPane(WoodType woodType, Supplier<? extends Block> parent) {
        return woodenWindowPane(woodType, parent, () -> RenderType::m_110457_);
    }

    public static BlockEntry<ConnectedGlassPaneBlock> woodenWindowPane(WoodType woodType, Supplier<? extends Block> parent, Supplier<Supplier<RenderType>> renderType) {
        String woodName = woodType.name();
        String name = woodName + "_window";
        ResourceLocation topTexture = new ResourceLocation("block/" + woodName + "_planks");
        ResourceLocation sideTexture = Create.asResource(palettesDir() + name);
        return connectedGlassPane(name, parent, () -> AllSpriteShifts.getWoodenWindow(woodType), sideTexture, sideTexture, topTexture, renderType);
    }

    public static BlockEntry<GlassPaneBlock> standardGlassPane(String name, Supplier<? extends Block> parent, ResourceLocation sideTexture, ResourceLocation topTexture, Supplier<Supplier<RenderType>> renderType) {
        NonNullBiConsumer<DataGenContext<Block, GlassPaneBlock>, RegistrateBlockstateProvider> stateProvider = (c, p) -> p.paneBlock((IronBarsBlock) c.get(), sideTexture, topTexture);
        return glassPane(name, parent, sideTexture, topTexture, GlassPaneBlock::new, renderType, $ -> {
        }, stateProvider);
    }

    private static BlockEntry<ConnectedGlassPaneBlock> connectedGlassPane(String name, Supplier<? extends Block> parent, Supplier<CTSpriteShiftEntry> ctshift, ResourceLocation sideTexture, ResourceLocation itemSideTexture, ResourceLocation topTexture, Supplier<Supplier<RenderType>> renderType) {
        NonNullConsumer<? super ConnectedGlassPaneBlock> connectedTextures = CreateRegistrate.connectedTextures(() -> new GlassPaneCTBehaviour((CTSpriteShiftEntry) ctshift.get()));
        String CGPparents = "block/connected_glass_pane/";
        String prefix = name + "_pane_";
        Function<RegistrateBlockstateProvider, ModelFile> post = getPaneModelProvider(CGPparents, prefix, "post", sideTexture, topTexture);
        Function<RegistrateBlockstateProvider, ModelFile> side = getPaneModelProvider(CGPparents, prefix, "side", sideTexture, topTexture);
        Function<RegistrateBlockstateProvider, ModelFile> sideAlt = getPaneModelProvider(CGPparents, prefix, "side_alt", sideTexture, topTexture);
        Function<RegistrateBlockstateProvider, ModelFile> noSide = getPaneModelProvider(CGPparents, prefix, "noside", sideTexture, topTexture);
        Function<RegistrateBlockstateProvider, ModelFile> noSideAlt = getPaneModelProvider(CGPparents, prefix, "noside_alt", sideTexture, topTexture);
        NonNullBiConsumer<DataGenContext<Block, ConnectedGlassPaneBlock>, RegistrateBlockstateProvider> stateProvider = (c, p) -> p.paneBlock((IronBarsBlock) c.get(), (ModelFile) post.apply(p), (ModelFile) side.apply(p), (ModelFile) sideAlt.apply(p), (ModelFile) noSide.apply(p), (ModelFile) noSideAlt.apply(p));
        return glassPane(name, parent, itemSideTexture, topTexture, ConnectedGlassPaneBlock::new, renderType, connectedTextures, stateProvider);
    }

    private static Function<RegistrateBlockstateProvider, ModelFile> getPaneModelProvider(String CGPparents, String prefix, String partial, ResourceLocation sideTexture, ResourceLocation topTexture) {
        return p -> p.models().withExistingParent(prefix + partial, Create.asResource(CGPparents + partial)).texture("pane", sideTexture).texture("edge", topTexture);
    }

    private static <G extends GlassPaneBlock> BlockEntry<G> glassPane(String name, Supplier<? extends Block> parent, ResourceLocation sideTexture, ResourceLocation topTexture, NonNullFunction<BlockBehaviour.Properties, G> factory, Supplier<Supplier<RenderType>> renderType, NonNullConsumer<? super G> connectedTextures, NonNullBiConsumer<DataGenContext<Block, G>, RegistrateBlockstateProvider> stateProvider) {
        name = name + "_pane";
        return ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(name, factory).onRegister(connectedTextures)).addLayer(renderType).initialProperties(() -> Blocks.GLASS_PANE).properties(p -> p.mapColor(((Block) parent.get()).m_284356_())).blockstate(stateProvider).recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike) c.get(), 16).pattern("###").pattern("###").define('#', (ItemLike) parent.get()).unlockedBy("has_ingredient", RegistrateRecipeProvider.m_125977_((ItemLike) parent.get())).m_176498_(p::accept)).tag(new TagKey[] { Tags.Blocks.GLASS_PANES }).loot((t, g) -> t.m_245644_(g)).item().tag(new TagKey[] { Tags.Items.GLASS_PANES }).model((c, p) -> ((ItemModelBuilder) p.withExistingParent(c.getName(), Create.asResource("item/pane"))).texture("pane", sideTexture).texture("edge", topTexture)).build()).register();
    }

    private static String palettesDir() {
        return "block/palettes/";
    }
}