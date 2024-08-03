package com.simibubi.create.foundation.data;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class MetalBarsGen {

    public static <P extends IronBarsBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> barsBlockState(String name, boolean specialEdge) {
        return (c, p) -> {
            ModelFile post_ends = barsSubModel(p, name, "post_ends", specialEdge);
            ModelFile post = barsSubModel(p, name, "post", specialEdge);
            ModelFile cap = barsSubModel(p, name, "cap", specialEdge);
            ModelFile cap_alt = barsSubModel(p, name, "cap_alt", specialEdge);
            ModelFile side = barsSubModel(p, name, "side", specialEdge);
            ModelFile side_alt = barsSubModel(p, name, "side_alt", specialEdge);
            p.getMultipartBuilder((Block) c.get()).part().modelFile(post_ends).addModel().end().part().modelFile(post).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.WEST, false).end().part().modelFile(cap).addModel().condition(BlockStateProperties.NORTH, true).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.WEST, false).end().part().modelFile(cap).rotationY(90).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.EAST, true).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.WEST, false).end().part().modelFile(cap_alt).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.SOUTH, true).condition(BlockStateProperties.WEST, false).end().part().modelFile(cap_alt).rotationY(90).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.EAST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.WEST, true).end().part().modelFile(side).addModel().condition(BlockStateProperties.NORTH, true).end().part().modelFile(side).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end().part().modelFile(side_alt).addModel().condition(BlockStateProperties.SOUTH, true).end().part().modelFile(side_alt).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
        };
    }

    private static ModelFile barsSubModel(RegistrateBlockstateProvider p, String name, String suffix, boolean specialEdge) {
        ResourceLocation barsTexture = p.modLoc("block/bars/" + name + "_bars");
        ResourceLocation edgeTexture = specialEdge ? p.modLoc("block/bars/" + name + "_bars_edge") : barsTexture;
        return p.models().withExistingParent(name + "_" + suffix, p.modLoc("block/bars/" + suffix)).texture("bars", barsTexture).texture("particle", barsTexture).texture("edge", edgeTexture);
    }

    public static BlockEntry<IronBarsBlock> createBars(String name, boolean specialEdge, Supplier<DataIngredient> ingredient, MapColor color) {
        return ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(name + "_bars", IronBarsBlock::new).addLayer(() -> RenderType::m_110457_).initialProperties(() -> Blocks.IRON_BARS).properties(p -> p.sound(SoundType.COPPER).mapColor(color)).tag(new TagKey[] { AllTags.AllBlockTags.WRENCH_PICKUP.tag }).tag(new TagKey[] { AllTags.AllBlockTags.FAN_TRANSPARENT.tag }).transform(TagGen.pickaxeOnly())).blockstate(barsBlockState(name, specialEdge)).item().model((c, p) -> {
            ResourceLocation barsTexture = p.modLoc("block/bars/" + name + "_bars");
            ((ItemModelBuilder) p.withExistingParent(c.getName(), Create.asResource("item/bars"))).texture("bars", barsTexture).texture("edge", specialEdge ? p.modLoc("block/bars/" + name + "_bars_edge") : barsTexture);
        }).recipe((c, p) -> p.stonecutting((DataIngredient) ingredient.get(), RecipeCategory.DECORATIONS, c::get, 4)).build()).register();
    }
}