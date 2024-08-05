package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.block.ChamberBlockBase;
import com.rekindled.embers.block.EmberEmitterBlock;
import com.rekindled.embers.block.FieldChartBlock;
import com.rekindled.embers.block.ItemTransferBlock;
import com.rekindled.embers.block.MechEdgeBlockBase;
import com.rekindled.embers.block.MnemonicInscriberBlock;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.render.PipeModelBuilder;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EmbersBlockStates extends BlockStateProvider {

    public EmbersBlockStates(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, "embers", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryManager.FluidStuff fluid : RegistryManager.fluidList) {
            this.fluid(fluid.FLUID_BLOCK, fluid.name);
        }
        this.blockWithItemTexture(RegistryManager.LEAD_ORE, "ore_lead");
        this.blockWithItemTexture(RegistryManager.DEEPSLATE_LEAD_ORE, "deepslate_ore_lead");
        this.blockWithItemTexture(RegistryManager.RAW_LEAD_BLOCK, "material_lead");
        this.blockWithItemTexture(RegistryManager.LEAD_BLOCK, "block_lead");
        this.blockWithItemTexture(RegistryManager.SILVER_ORE, "ore_silver");
        this.blockWithItemTexture(RegistryManager.DEEPSLATE_SILVER_ORE, "deepslate_ore_silver");
        this.blockWithItemTexture(RegistryManager.RAW_SILVER_BLOCK, "material_silver");
        this.blockWithItemTexture(RegistryManager.SILVER_BLOCK, "block_silver");
        this.blockWithItemTexture(RegistryManager.DAWNSTONE_BLOCK, "block_dawnstone");
        this.blockWithItem(RegistryManager.MITHRIL_BLOCK, "mithril_block");
        this.blockWithItem(RegistryManager.CAMINITE_BRICKS);
        this.decoBlocks(RegistryManager.CAMINITE_BRICKS_DECO);
        this.blockWithItem(RegistryManager.CAMINITE_LARGE_BRICKS);
        this.decoBlocks(RegistryManager.CAMINITE_LARGE_BRICKS_DECO);
        this.blockWithItem(RegistryManager.RAW_CAMINITE_BLOCK);
        this.blockWithItem(RegistryManager.CAMINITE_LARGE_TILE);
        this.decoBlocks(RegistryManager.CAMINITE_LARGE_TILE_DECO);
        this.blockWithItem(RegistryManager.CAMINITE_TILES);
        this.decoBlocks(RegistryManager.CAMINITE_TILES_DECO);
        this.blockWithItem(RegistryManager.ARCHAIC_BRICKS);
        this.decoBlocks(RegistryManager.ARCHAIC_BRICKS_DECO);
        this.blockWithItem(RegistryManager.ARCHAIC_EDGE, "archaic_edge");
        this.blockWithItem(RegistryManager.ARCHAIC_TILE);
        this.decoBlocks(RegistryManager.ARCHAIC_TILE_DECO);
        this.blockWithItem(RegistryManager.ARCHAIC_LARGE_BRICKS);
        this.decoBlocks(RegistryManager.ARCHAIC_LARGE_BRICKS_DECO);
        this.blockWithItem(RegistryManager.ARCHAIC_LIGHT, "archaic_light");
        this.blockWithItem(RegistryManager.ASHEN_STONE);
        this.decoBlocks(RegistryManager.ASHEN_STONE_DECO);
        this.blockWithItem(RegistryManager.ASHEN_BRICK);
        this.decoBlocks(RegistryManager.ASHEN_BRICK_DECO);
        this.blockWithItem(RegistryManager.ASHEN_TILE);
        this.decoBlocks(RegistryManager.ASHEN_TILE_DECO);
        this.blockWithItem(RegistryManager.SEALED_PLANKS);
        this.decoBlocks(RegistryManager.SEALED_PLANKS_DECO);
        this.blockWithItem(RegistryManager.REINFORCED_SEALED_PLANKS);
        this.blockWithItem(RegistryManager.SEALED_WOOD_TILE);
        this.decoBlocks(RegistryManager.SEALED_WOOD_TILE_DECO);
        this.pillarBlockWithItem(RegistryManager.SEALED_WOOD_PILLAR, "sealed_planks", "sealed_keg_top");
        this.pillarBlockWithItem(RegistryManager.SEALED_WOOD_KEG, "reinforced_sealed_planks", "sealed_keg_top");
        this.blockWithItem(RegistryManager.SOLIDIFIED_METAL);
        this.columnBlockWithItem(RegistryManager.METAL_PLATFORM, "metal_platform_side", "metal_platform");
        this.slabBlock(RegistryManager.METAL_PLATFORM_DECO.slab.get(), new ResourceLocation("embers", "metal_platform"), new ResourceLocation("embers", "block/metal_platform_side"), new ResourceLocation("embers", "block/metal_platform"), new ResourceLocation("embers", "block/metal_platform"));
        this.simpleBlockItem(RegistryManager.METAL_PLATFORM_DECO.slab.get(), this.models().getExistingFile(new ResourceLocation("embers", "metal_platform_slab")));
        this.blockWithItem(RegistryManager.EMBER_LANTERN, "ember_lantern");
        this.blockWithItem(RegistryManager.COPPER_CELL, "copper_cell");
        this.blockWithItem(RegistryManager.CREATIVE_EMBER);
        this.dial(RegistryManager.EMBER_DIAL, "ember_dial");
        this.dial(RegistryManager.ITEM_DIAL, "item_dial");
        this.dial(RegistryManager.FLUID_DIAL, "fluid_dial");
        this.dial(RegistryManager.ATMOSPHERIC_GAUGE, "atmospheric_gauge");
        this.dial(RegistryManager.CLOCKWORK_ATTENUATOR, "clockwork_attenuator");
        ModelFile fluidPipeCenterModel = this.models().withExistingParent("fluid_pipe_center", new ResourceLocation("embers", "pipe_center")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile fluidPipeEndModel = this.models().withExistingParent("fluid_pipe_end", new ResourceLocation("embers", "pipe_end")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile fluidPipeConnectionModel = this.models().withExistingParent("fluid_pipe_connection", new ResourceLocation("embers", "pipe_connection")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile fluidPipeEndModel2 = this.models().withExistingParent("fluid_pipe_end_2", new ResourceLocation("embers", "pipe_end_2")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile fluidPipeConnectionModel2 = this.models().withExistingParent("fluid_pipe_connection_2", new ResourceLocation("embers", "pipe_connection_2")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile.ExistingModelFile emitterModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_emitter"));
        this.simpleBlockItem(RegistryManager.EMBER_EMITTER.get(), emitterModel);
        MultiPartBlockStateBuilder emitterBuilder = this.getMultipartBuilder(RegistryManager.EMBER_EMITTER.get()).part().modelFile(emitterModel).addModel().condition(BlockStateProperties.FACING, Direction.UP).end().part().modelFile(emitterModel).rotationX(180).addModel().condition(BlockStateProperties.FACING, Direction.DOWN).end().part().modelFile(emitterModel).rotationX(90).addModel().condition(BlockStateProperties.FACING, Direction.NORTH).end().part().modelFile(emitterModel).rotationX(90).rotationY(180).addModel().condition(BlockStateProperties.FACING, Direction.SOUTH).end().part().modelFile(emitterModel).rotationX(90).rotationY(90).addModel().condition(BlockStateProperties.FACING, Direction.EAST).end().part().modelFile(emitterModel).rotationX(90).rotationY(270).addModel().condition(BlockStateProperties.FACING, Direction.WEST).end();
        addEmitterConnections(emitterBuilder, fluidPipeEndModel, fluidPipeEndModel2);
        ModelFile.ExistingModelFile receiverModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_receiver"));
        this.directionalBlock(RegistryManager.EMBER_RECEIVER.get(), receiverModel);
        this.simpleBlockItem(RegistryManager.EMBER_RECEIVER.get(), receiverModel);
        ModelFile.ExistingModelFile leverModel = this.models().getExistingFile(new ResourceLocation("embers", "caminite_lever"));
        this.leverBlock(RegistryManager.CAMINITE_LEVER.get(), leverModel, this.models().getExistingFile(new ResourceLocation("embers", "caminite_lever_on")));
        this.simpleBlockItem(RegistryManager.CAMINITE_LEVER.get(), leverModel);
        this.buttonBlock(RegistryManager.CAMINITE_BUTTON.get(), new ResourceLocation("embers", "block/caminite_button"));
        ModelFile itemPipeCenterModel = this.models().withExistingParent("item_pipe_center", new ResourceLocation("embers", "pipe_center")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        ModelFile itemPipeEndModel = this.models().withExistingParent("item_pipe_end", new ResourceLocation("embers", "pipe_end")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        ModelFile itemPipeConnectionModel = this.models().withExistingParent("item_pipe_connection", new ResourceLocation("embers", "pipe_connection")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        ModelFile itemPipeEndModel2 = this.models().withExistingParent("item_pipe_end_2", new ResourceLocation("embers", "pipe_end_2")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        ModelFile itemPipeConnectionModel2 = this.models().withExistingParent("item_pipe_connection_2", new ResourceLocation("embers", "pipe_connection_2")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        this.simpleBlockItem(RegistryManager.ITEM_PIPE.get(), this.models().withExistingParent("item_pipe_inventory", new ResourceLocation("embers", "pipe_inventory")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")));
        ModelFile itemPipeModel = ((PipeModelBuilder) this.models().withExistingParent("item_pipe", "block").customLoader(PipeModelBuilder::begin)).parts(this.models().nested().parent(itemPipeCenterModel), this.models().nested().parent(itemPipeConnectionModel), this.models().nested().parent(itemPipeConnectionModel2), this.models().nested().parent(itemPipeEndModel), this.models().nested().parent(itemPipeEndModel2)).end();
        this.simpleBlock(RegistryManager.ITEM_PIPE.get(), itemPipeModel);
        ModelFile itemExtractorCenterModel = this.models().withExistingParent("item_extractor_center", new ResourceLocation("embers", "extractor_center")).texture("pipe", new ResourceLocation("embers", "block/item_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/item_pipe_tex"));
        ModelFile itemExtractorModel = ((PipeModelBuilder) this.models().withExistingParent("item_extractor", "block").customLoader(PipeModelBuilder::begin)).parts(this.models().nested().parent(itemExtractorCenterModel), this.models().nested().parent(itemPipeConnectionModel), this.models().nested().parent(itemPipeConnectionModel2), this.models().nested().parent(itemPipeEndModel), this.models().nested().parent(itemPipeEndModel2)).end();
        this.simpleBlockItem(RegistryManager.ITEM_EXTRACTOR.get(), itemExtractorCenterModel);
        this.simpleBlock(RegistryManager.ITEM_EXTRACTOR.get(), itemExtractorModel);
        ModelFile.ExistingModelFile emberBoreModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_bore_center"));
        this.getVariantBuilder(RegistryManager.EMBER_BORE.get()).forAllStates(state -> {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
            return ConfiguredModel.builder().modelFile(emberBoreModel).rotationY(axis == Direction.Axis.Z ? 90 : 0).uvLock(false).build();
        });
        this.simpleBlockItem(RegistryManager.EMBER_BORE.get(), this.models().cubeAll("ember_bore", new ResourceLocation("embers", "block/crate_bore")));
        ModelFile.ExistingModelFile mechEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "mech_edge_straight"));
        ModelFile.ExistingModelFile boreEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_bore_edge"));
        ModelFile.ExistingModelFile mechCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "mech_edge_corner"));
        this.getVariantBuilder(RegistryManager.EMBER_BORE_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
            return ConfiguredModel.builder().modelFile(edge.corner ? mechCornerModel : ((edge != MechEdgeBlockBase.MechEdge.NORTH && edge != MechEdgeBlockBase.MechEdge.SOUTH || axis != Direction.Axis.Z) && (edge != MechEdgeBlockBase.MechEdge.EAST && edge != MechEdgeBlockBase.MechEdge.WEST || axis != Direction.Axis.X) ? boreEdgeModel : mechEdgeModel)).rotationY(edge.rotation).uvLock(true).build();
        });
        ModelFile.ExistingModelFile mechCoreModel = this.models().getExistingFile(new ResourceLocation("embers", "mech_core"));
        this.betterDirectionalBlock(RegistryManager.MECHANICAL_CORE.get(), $ -> mechCoreModel, 0);
        this.simpleBlockItem(RegistryManager.MECHANICAL_CORE.get(), mechCoreModel);
        ModelFile.ExistingModelFile activatorTopModel = this.models().getExistingFile(new ResourceLocation("embers", "activator_top"));
        this.simpleBlockItem(RegistryManager.EMBER_ACTIVATOR.get(), activatorTopModel);
        this.getVariantBuilder(RegistryManager.EMBER_ACTIVATOR.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? this.models().getExistingFile(new ResourceLocation("embers", "activator_bottom")) : activatorTopModel).uvLock(false).build());
        ModelFile.ExistingModelFile melterBottomModel = this.models().getExistingFile(new ResourceLocation("embers", "melter_bottom"));
        this.simpleBlockItem(RegistryManager.MELTER.get(), melterBottomModel);
        this.getVariantBuilder(RegistryManager.MELTER.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? melterBottomModel : this.models().getExistingFile(new ResourceLocation("embers", "melter_top"))).uvLock(false).build());
        this.simpleBlockItem(RegistryManager.FLUID_PIPE.get(), this.models().withExistingParent("fluid_pipe_inventory", new ResourceLocation("embers", "pipe_inventory")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")));
        ModelFile fluidPipeModel = ((PipeModelBuilder) this.models().withExistingParent("fluid_pipe", "block").customLoader(PipeModelBuilder::begin)).parts(this.models().nested().parent(fluidPipeCenterModel), this.models().nested().parent(fluidPipeConnectionModel), this.models().nested().parent(fluidPipeConnectionModel2), this.models().nested().parent(fluidPipeEndModel), this.models().nested().parent(fluidPipeEndModel2)).end();
        this.simpleBlock(RegistryManager.FLUID_PIPE.get(), fluidPipeModel);
        ModelFile fluidExtractorCenterModel = this.models().withExistingParent("fluid_extractor_center", new ResourceLocation("embers", "extractor_center")).texture("pipe", new ResourceLocation("embers", "block/fluid_pipe_tex")).texture("particle", new ResourceLocation("embers", "block/fluid_pipe_tex"));
        ModelFile fluidExtractorModel = ((PipeModelBuilder) this.models().withExistingParent("fluid_extractor", "block").customLoader(PipeModelBuilder::begin)).parts(this.models().nested().parent(fluidExtractorCenterModel), this.models().nested().parent(fluidPipeConnectionModel), this.models().nested().parent(fluidPipeConnectionModel2), this.models().nested().parent(fluidPipeEndModel), this.models().nested().parent(fluidPipeEndModel2)).end();
        this.simpleBlockItem(RegistryManager.FLUID_EXTRACTOR.get(), fluidExtractorCenterModel);
        this.simpleBlock(RegistryManager.FLUID_EXTRACTOR.get(), fluidExtractorModel);
        this.blockWithItem(RegistryManager.FLUID_VESSEL, "fluid_vessel");
        ModelFile.ExistingModelFile stamperModel = this.models().getExistingFile(new ResourceLocation("embers", "stamper"));
        this.simpleBlock(RegistryManager.STAMPER.get(), stamperModel);
        this.simpleBlockItem(RegistryManager.STAMPER.get(), stamperModel);
        this.blockWithItem(RegistryManager.STAMP_BASE, "stamp_base");
        this.blockWithItem(RegistryManager.BIN, "bin");
        ModelFile.ExistingModelFile mixerBottomModel = this.models().getExistingFile(new ResourceLocation("embers", "mixer_bottom"));
        this.simpleBlockItem(RegistryManager.MIXER_CENTRIFUGE.get(), mixerBottomModel);
        this.getVariantBuilder(RegistryManager.MIXER_CENTRIFUGE.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? mixerBottomModel : this.models().getExistingFile(new ResourceLocation("embers", "mixer_top"))).uvLock(false).build());
        ModelFile dropperModel = this.models().withExistingParent(RegistryManager.ITEM_DROPPER.getId().toString(), new ResourceLocation("embers", "dropper")).texture("dropper", new ResourceLocation("embers", "block/plates_lead")).texture("particle", new ResourceLocation("embers", "block/plates_lead"));
        this.simpleBlock(RegistryManager.ITEM_DROPPER.get(), dropperModel);
        this.simpleBlockItem(RegistryManager.ITEM_DROPPER.get(), dropperModel);
        ModelFile.ExistingModelFile refineryBottomModel = this.models().getExistingFile(new ResourceLocation("embers", "refinery_bottom"));
        this.simpleBlockItem(RegistryManager.PRESSURE_REFINERY.get(), refineryBottomModel);
        this.getVariantBuilder(RegistryManager.PRESSURE_REFINERY.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? refineryBottomModel : this.models().getExistingFile(new ResourceLocation("embers", "refinery_top"))).uvLock(false).build());
        ModelFile.ExistingModelFile ejectorModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_ejector"));
        this.simpleBlockItem(RegistryManager.EMBER_EJECTOR.get(), ejectorModel);
        MultiPartBlockStateBuilder ejectorBuilder = this.getMultipartBuilder(RegistryManager.EMBER_EJECTOR.get()).part().modelFile(ejectorModel).addModel().condition(BlockStateProperties.FACING, Direction.UP).end().part().modelFile(ejectorModel).rotationX(180).addModel().condition(BlockStateProperties.FACING, Direction.DOWN).end().part().modelFile(ejectorModel).rotationX(90).addModel().condition(BlockStateProperties.FACING, Direction.NORTH).end().part().modelFile(ejectorModel).rotationX(90).rotationY(180).addModel().condition(BlockStateProperties.FACING, Direction.SOUTH).end().part().modelFile(ejectorModel).rotationX(90).rotationY(90).addModel().condition(BlockStateProperties.FACING, Direction.EAST).end().part().modelFile(ejectorModel).rotationX(90).rotationY(270).addModel().condition(BlockStateProperties.FACING, Direction.WEST).end();
        addEmitterConnections(ejectorBuilder, fluidPipeEndModel, fluidPipeEndModel2);
        ModelFile.ExistingModelFile funnelModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_funnel"));
        this.directionalBlock(RegistryManager.EMBER_FUNNEL.get(), funnelModel);
        this.simpleBlockItem(RegistryManager.EMBER_FUNNEL.get(), funnelModel);
        ModelFile.ExistingModelFile relayModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_relay"));
        this.directionalBlock(RegistryManager.EMBER_RELAY.get(), relayModel);
        this.simpleBlockItem(RegistryManager.EMBER_RELAY.get(), relayModel);
        ModelFile.ExistingModelFile mirrorModel = this.models().getExistingFile(new ResourceLocation("embers", "mirror_relay"));
        this.directionalBlock(RegistryManager.MIRROR_RELAY.get(), mirrorModel);
        this.simpleBlockItem(RegistryManager.MIRROR_RELAY.get(), mirrorModel);
        ModelFile.ExistingModelFile splitterModelX = this.models().getExistingFile(new ResourceLocation("embers", "beam_splitter_x"));
        ModelFile.ExistingModelFile splitterModelZ = this.models().getExistingFile(new ResourceLocation("embers", "beam_splitter_z"));
        this.simpleBlockItem(RegistryManager.BEAM_SPLITTER.get(), splitterModelZ);
        this.getVariantBuilder(RegistryManager.BEAM_SPLITTER.get()).forAllStates(state -> {
            Direction face = (Direction) state.m_61143_(BlockStateProperties.FACING);
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
            return ConfiguredModel.builder().modelFile((axis != Direction.Axis.X || face.getAxis() != Direction.Axis.Y) && (axis == Direction.Axis.Y || face.getAxis() == Direction.Axis.Y) ? splitterModelZ : splitterModelX).rotationX(face == Direction.DOWN ? 180 : (face == Direction.UP ? 0 : 90)).rotationY(face == Direction.SOUTH ? 180 : (face == Direction.WEST ? 270 : (face == Direction.EAST ? 90 : 0))).uvLock(false).build();
        });
        ModelFile.ExistingModelFile vacuumModel = this.models().getExistingFile(new ResourceLocation("embers", "item_vacuum"));
        this.directionalBlock(RegistryManager.ITEM_VACUUM.get(), vacuumModel);
        this.simpleBlockItem(RegistryManager.ITEM_VACUUM.get(), vacuumModel);
        this.simpleBlock(RegistryManager.HEARTH_COIL.get(), this.models().getExistingFile(new ResourceLocation("embers", "hearth_coil_center")));
        this.simpleBlockItem(RegistryManager.HEARTH_COIL.get(), this.models().cubeAll("hearth_coil", new ResourceLocation("embers", "block/crate_coil")));
        ModelFile.ExistingModelFile coilEdgeModelX = this.models().getExistingFile(new ResourceLocation("embers", "hearth_coil_edge_x"));
        ModelFile.ExistingModelFile coilEdgeModelZ = this.models().getExistingFile(new ResourceLocation("embers", "hearth_coil_edge_z"));
        ModelFile.ExistingModelFile coilCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "hearth_coil_corner"));
        this.getVariantBuilder(RegistryManager.HEARTH_COIL_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? coilCornerModel : (edge != MechEdgeBlockBase.MechEdge.EAST && edge != MechEdgeBlockBase.MechEdge.WEST ? coilEdgeModelX : coilEdgeModelZ)).rotationY(edge.rotation).uvLock(true).build();
        });
        this.simpleBlock(RegistryManager.RESERVOIR.get(), this.models().getExistingFile(new ResourceLocation("embers", "reservoir_center")));
        this.simpleBlockItem(RegistryManager.RESERVOIR.get(), this.models().cubeAll("reservoir", new ResourceLocation("embers", "block/crate_tank")));
        this.getVariantBuilder(RegistryManager.RESERVOIR_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? mechCornerModel : mechEdgeModel).rotationY(edge.rotation).uvLock(true).build();
        });
        this.simpleBlock(RegistryManager.CAMINITE_RING.get(), this.models().getExistingFile(new ResourceLocation("embers", "caminite_ring_center")));
        this.flatItem(RegistryManager.CAMINITE_RING, "caminite_ring");
        ModelFile.ExistingModelFile ringEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "caminite_ring_edge"));
        ModelFile.ExistingModelFile ringCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "caminite_ring_corner"));
        this.getVariantBuilder(RegistryManager.CAMINITE_RING_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? ringCornerModel : ringEdgeModel).rotationY(edge.rotation).uvLock(false).build();
        });
        this.simpleBlock(RegistryManager.CAMINITE_GAUGE.get(), this.models().getExistingFile(new ResourceLocation("embers", "caminite_ring_center")));
        this.flatItem(RegistryManager.CAMINITE_GAUGE, "caminite_gauge");
        ModelFile gaugeEdgeModel = ((CompositeModelBuilder) this.models().withExistingParent("caminite_gauge_edge", new ResourceLocation("embers", "caminite_gauge_edge_base")).customLoader(CompositeModelBuilder::begin)).child("base", this.models().nested().parent(this.models().getExistingFile(new ResourceLocation("embers", "caminite_gauge_edge_base")))).child("glass", this.models().nested().parent(this.models().getExistingFile(new ResourceLocation("embers", "caminite_gauge_edge_glass")))).end();
        this.getVariantBuilder(RegistryManager.CAMINITE_GAUGE_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile((ModelFile) (edge.corner ? ringCornerModel : gaugeEdgeModel)).rotationY(edge.rotation).uvLock(false).build();
        });
        this.simpleBlock(RegistryManager.CAMINITE_VALVE.get(), this.models().getExistingFile(new ResourceLocation("embers", "caminite_ring_center")));
        this.flatItem(RegistryManager.CAMINITE_VALVE, "caminite_valve");
        ModelFile.ExistingModelFile valveEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "caminite_valve_edge"));
        this.getVariantBuilder(RegistryManager.CAMINITE_VALVE_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? ringCornerModel : valveEdgeModel).rotationY(edge.rotation).uvLock(false).build();
        });
        this.simpleBlock(RegistryManager.CRYSTAL_CELL.get(), this.models().getExistingFile(new ResourceLocation("embers", "crystal_cell_center")));
        this.simpleBlockItem(RegistryManager.CRYSTAL_CELL.get(), this.models().cubeAll("crystal_cell", new ResourceLocation("embers", "block/crate_crystal")));
        ModelFile.ExistingModelFile cellEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "crystal_cell_edge"));
        ModelFile.ExistingModelFile cellCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "crystal_cell_corner"));
        this.getVariantBuilder(RegistryManager.CRYSTAL_CELL_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? cellCornerModel : cellEdgeModel).rotationY(edge.rotation).uvLock(false).build();
        });
        ModelFile.ExistingModelFile separatorModel = this.models().getExistingFile(new ResourceLocation("embers", "geologic_separator"));
        this.horizontalBlock(RegistryManager.GEOLOGIC_SEPARATOR.get(), separatorModel);
        this.simpleBlockItem(RegistryManager.GEOLOGIC_SEPARATOR.get(), separatorModel);
        ModelFile.ExistingModelFile chargerModel = this.models().getExistingFile(new ResourceLocation("embers", "copper_charger"));
        this.horizontalBlock(RegistryManager.COPPER_CHARGER.get(), chargerModel);
        this.simpleBlockItem(RegistryManager.COPPER_CHARGER.get(), chargerModel);
        ModelFile.ExistingModelFile siphonModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_siphon"));
        this.simpleBlock(RegistryManager.EMBER_SIPHON.get(), siphonModel);
        this.simpleBlockItem(RegistryManager.EMBER_SIPHON.get(), siphonModel);
        ModelFile.ExistingModelFile itemTransferModel = this.models().getExistingFile(new ResourceLocation("embers", "item_transfer"));
        ModelFile.ExistingModelFile itemTransferFilterModel = this.models().getExistingFile(new ResourceLocation("embers", "item_transfer_filtered"));
        this.directionalBlock(RegistryManager.ITEM_TRANSFER.get(), state -> state.m_61143_(ItemTransferBlock.FILTER) ? itemTransferFilterModel : itemTransferModel, 180);
        this.simpleBlockItem(RegistryManager.ITEM_TRANSFER.get(), itemTransferModel);
        ModelFile fluidTransferModel = this.models().withExistingParent("fluid_transfer", new ResourceLocation("embers", "item_transfer")).texture("top", new ResourceLocation("embers", "block/fluid_transfer_top")).texture("side", new ResourceLocation("embers", "block/fluid_transfer_side")).texture("bottom", new ResourceLocation("embers", "block/fluid_transfer_bottom")).texture("particle", new ResourceLocation("embers", "block/fluid_transfer_side"));
        ModelFile fluidTransferFilterModel = this.models().withExistingParent("fluid_transfer_filtered", new ResourceLocation("embers", "item_transfer_filtered")).texture("top", new ResourceLocation("embers", "block/fluid_transfer_top")).texture("side", new ResourceLocation("embers", "block/fluid_transfer_side")).texture("bottom", new ResourceLocation("embers", "block/fluid_transfer_bottom")).texture("particle", new ResourceLocation("embers", "block/fluid_transfer_side"));
        this.directionalBlock(RegistryManager.FLUID_TRANSFER.get(), state -> state.m_61143_(ItemTransferBlock.FILTER) ? fluidTransferFilterModel : fluidTransferModel, 180);
        this.simpleBlockItem(RegistryManager.FLUID_TRANSFER.get(), fluidTransferModel);
        this.getVariantBuilder(RegistryManager.ALCHEMY_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? this.models().getExistingFile(new ResourceLocation("embers", "alchemy_pedestal_bottom")) : this.models().getExistingFile(new ResourceLocation("embers", "alchemy_pedestal_top"))).uvLock(false).build());
        this.getVariantBuilder(CuriosCompat.EXPLOSION_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? this.models().getExistingFile(new ResourceLocation("embers", "alchemy_pedestal_bottom")) : this.models().getExistingFile(new ResourceLocation("embers", "explosion_pedestal_top"))).uvLock(false).build());
        this.blockWithItem(RegistryManager.ALCHEMY_TABLET, "alchemy_tablet");
        ModelFile.ExistingModelFile cannonModel = this.models().getExistingFile(new ResourceLocation("embers", "beam_cannon"));
        this.simpleBlockItem(RegistryManager.BEAM_CANNON.get(), cannonModel);
        MultiPartBlockStateBuilder cannonBuilder = this.getMultipartBuilder(RegistryManager.BEAM_CANNON.get()).part().modelFile(cannonModel).addModel().condition(BlockStateProperties.FACING, Direction.UP).end().part().modelFile(cannonModel).rotationX(180).addModel().condition(BlockStateProperties.FACING, Direction.DOWN).end().part().modelFile(cannonModel).rotationX(90).addModel().condition(BlockStateProperties.FACING, Direction.NORTH).end().part().modelFile(cannonModel).rotationX(90).rotationY(180).addModel().condition(BlockStateProperties.FACING, Direction.SOUTH).end().part().modelFile(cannonModel).rotationX(90).rotationY(90).addModel().condition(BlockStateProperties.FACING, Direction.EAST).end().part().modelFile(cannonModel).rotationX(90).rotationY(270).addModel().condition(BlockStateProperties.FACING, Direction.WEST).end();
        addEmitterConnections(cannonBuilder, fluidPipeEndModel, fluidPipeEndModel2);
        ModelFile.ExistingModelFile pumpBottomModel = this.models().getExistingFile(new ResourceLocation("embers", "mechanical_pump_bottom"));
        ModelFile.ExistingModelFile pumpTopModel = this.models().getExistingFile(new ResourceLocation("embers", "mechanical_pump_top"));
        this.getVariantBuilder(RegistryManager.MECHANICAL_PUMP.get()).forAllStates(state -> {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
            return ConfiguredModel.builder().modelFile(state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? pumpBottomModel : pumpTopModel).rotationY(axis == Direction.Axis.Z ? 0 : 90).uvLock(false).build();
        });
        this.simpleBlockItem(RegistryManager.MECHANICAL_PUMP.get(), pumpBottomModel);
        ModelFile.ExistingModelFile boilerModel = this.models().getExistingFile(new ResourceLocation("embers", "mini_boiler"));
        ModelFile boilerPipeModel = ((PipeModelBuilder) this.models().withExistingParent("mini_boiler_pipes", "block").customLoader(PipeModelBuilder::begin)).parts(this.models().nested().parent(boilerModel), this.models().nested().parent(fluidPipeConnectionModel), this.models().nested().parent(fluidPipeConnectionModel2), this.models().nested().parent(fluidPipeEndModel), this.models().nested().parent(fluidPipeEndModel2)).end();
        this.horizontalBlock(RegistryManager.MINI_BOILER.get(), boilerPipeModel);
        this.simpleBlockItem(RegistryManager.MINI_BOILER.get(), boilerModel);
        ModelFile catalyticPlugModel = ((CompositeModelBuilder) this.models().withExistingParent("catalytic_plug", new ResourceLocation("embers", "catalytic_plug_base")).customLoader(CompositeModelBuilder::begin)).child("base", this.models().nested().parent(this.models().getExistingFile(new ResourceLocation("embers", "catalytic_plug_base")))).child("glass", this.models().nested().parent(this.models().getExistingFile(new ResourceLocation("embers", "catalytic_plug_glass")))).end();
        this.directionalBlock(RegistryManager.CATALYTIC_PLUG.get(), catalyticPlugModel);
        this.simpleBlockItem(RegistryManager.CATALYTIC_PLUG.get(), catalyticPlugModel);
        ModelFile.ExistingModelFile stirlingModel = this.models().getExistingFile(new ResourceLocation("embers", "wildfire_stirling"));
        this.directionalBlock(RegistryManager.WILDFIRE_STIRLING.get(), stirlingModel);
        this.simpleBlockItem(RegistryManager.WILDFIRE_STIRLING.get(), stirlingModel);
        ModelFile.ExistingModelFile injectorModel = this.models().getExistingFile(new ResourceLocation("embers", "ember_injector"));
        this.directionalBlock(RegistryManager.EMBER_INJECTOR.get(), injectorModel);
        this.simpleBlockItem(RegistryManager.EMBER_INJECTOR.get(), injectorModel);
        this.metalSeed(RegistryManager.COPPER_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.IRON_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.GOLD_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.LEAD_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.SILVER_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.NICKEL_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.TIN_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.ALUMINUM_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.ZINC_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.PLATINUM_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.URANIUM_CRYSTAL_SEED);
        this.metalSeed(RegistryManager.DAWNSTONE_CRYSTAL_SEED);
        ModelFile.ExistingModelFile chartModel = this.models().getExistingFile(new ResourceLocation("embers", "field_chart_center"));
        ModelFile.ExistingModelFile invertedChartModel = this.models().getExistingFile(new ResourceLocation("embers", "field_chart_center_inverted"));
        this.getVariantBuilder(RegistryManager.FIELD_CHART.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(FieldChartBlock.INVERTED) ? invertedChartModel : chartModel).build());
        this.simpleBlockItem(RegistryManager.FIELD_CHART.get(), this.models().cubeAll("field_chart", new ResourceLocation("embers", "block/crate_chart")));
        ModelFile.ExistingModelFile chartEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "field_chart_edge"));
        ModelFile.ExistingModelFile chartCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "field_chart_corner"));
        this.getVariantBuilder(RegistryManager.FIELD_CHART_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            return ConfiguredModel.builder().modelFile(edge.corner ? chartCornerModel : chartEdgeModel).rotationY(edge.rotation).uvLock(false).build();
        });
        this.blockWithItem(RegistryManager.IGNEM_REACTOR, "ignem_reactor");
        ModelFile.ExistingModelFile chamberModel = this.models().getExistingFile(new ResourceLocation("embers", "chamber_top"));
        ModelFile.ExistingModelFile chamberConnectionModel = this.models().getExistingFile(new ResourceLocation("embers", "chamber_top_connection"));
        ModelFile.ExistingModelFile catalysisChamberModel = this.models().getExistingFile(new ResourceLocation("embers", "catalysis_chamber"));
        this.simpleBlockItem(RegistryManager.CATALYSIS_CHAMBER.get(), catalysisChamberModel);
        this.getVariantBuilder(RegistryManager.CATALYSIS_CHAMBER.get()).forAllStates(state -> {
            ChamberBlockBase.ChamberConnection connection = (ChamberBlockBase.ChamberConnection) state.m_61143_(ChamberBlockBase.CONNECTION);
            return ConfiguredModel.builder().modelFile(connection == ChamberBlockBase.ChamberConnection.BOTTOM ? catalysisChamberModel : (connection == ChamberBlockBase.ChamberConnection.TOP ? chamberModel : chamberConnectionModel)).rotationY(((int) connection.direction.toYRot() + 180) % 360).uvLock(false).build();
        });
        ModelFile.ExistingModelFile combustionChamberModel = this.models().getExistingFile(new ResourceLocation("embers", "combustion_chamber"));
        this.simpleBlockItem(RegistryManager.COMBUSTION_CHAMBER.get(), combustionChamberModel);
        this.getVariantBuilder(RegistryManager.COMBUSTION_CHAMBER.get()).forAllStates(state -> {
            ChamberBlockBase.ChamberConnection connection = (ChamberBlockBase.ChamberConnection) state.m_61143_(ChamberBlockBase.CONNECTION);
            return ConfiguredModel.builder().modelFile(connection == ChamberBlockBase.ChamberConnection.BOTTOM ? combustionChamberModel : (connection == ChamberBlockBase.ChamberConnection.TOP ? chamberModel : chamberConnectionModel)).rotationY(((int) connection.direction.toYRot() + 180) % 360).uvLock(false).build();
        });
        ModelFile.ExistingModelFile emptyModel = this.models().getExistingFile(new ResourceLocation("embers", "empty"));
        this.simpleBlock(RegistryManager.GLIMMER.get(), emptyModel);
        this.blockWithItem(RegistryManager.CINDER_PLINTH, "cinder_plinth");
        ModelFile.ExistingModelFile anvilModel = this.models().getExistingFile(new ResourceLocation("embers", "dawnstone_anvil"));
        this.horizontalBlock(RegistryManager.DAWNSTONE_ANVIL.get(), anvilModel, 90);
        this.simpleBlockItem(RegistryManager.DAWNSTONE_ANVIL.get(), anvilModel);
        ModelFile.ExistingModelFile hammerModel = this.models().getExistingFile(new ResourceLocation("embers", "automatic_hammer"));
        ModelFile.ExistingModelFile hammerItemModel = this.models().getExistingFile(new ResourceLocation("embers", "automatic_hammer_item"));
        this.horizontalBlock(RegistryManager.AUTOMATIC_HAMMER.get(), hammerModel);
        this.simpleBlockItem(RegistryManager.AUTOMATIC_HAMMER.get(), hammerItemModel);
        ModelFile.ExistingModelFile infernoForgeModel = this.models().getExistingFile(new ResourceLocation("embers", "inferno_forge"));
        this.getVariantBuilder(RegistryManager.INFERNO_FORGE.get()).forAllStates(state -> {
            DoubleBlockHalf half = (DoubleBlockHalf) state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF);
            return ConfiguredModel.builder().modelFile(half == DoubleBlockHalf.LOWER ? infernoForgeModel : emptyModel).build();
        });
        this.simpleBlockItem(RegistryManager.INFERNO_FORGE.get(), this.models().cubeAll("crate_inferno_forge", new ResourceLocation("embers", "block/crate_inferno_forge")));
        ModelFile.ExistingModelFile forgeEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "inferno_forge_edge_bottom"));
        ModelFile.ExistingModelFile forgeTopEdgeModel = this.models().getExistingFile(new ResourceLocation("embers", "inferno_forge_edge_top"));
        ModelFile.ExistingModelFile forgeCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "inferno_forge_corner_bottom"));
        ModelFile.ExistingModelFile forgeTopCornerModel = this.models().getExistingFile(new ResourceLocation("embers", "inferno_forge_corner_top"));
        this.getVariantBuilder(RegistryManager.INFERNO_FORGE_EDGE.get()).forAllStates(state -> {
            MechEdgeBlockBase.MechEdge edge = (MechEdgeBlockBase.MechEdge) state.m_61143_(MechEdgeBlockBase.EDGE);
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().rotationY(edge.rotation);
            return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? builder.modelFile(edge.corner ? forgeCornerModel : forgeEdgeModel).build() : builder.modelFile(edge.corner ? forgeTopCornerModel : forgeTopEdgeModel).build();
        });
        ModelFile.ExistingModelFile inscriberModel = this.models().getExistingFile(new ResourceLocation("embers", "mnemonic_inscriber"));
        ModelFile inscriberActiveModel = this.models().withExistingParent("embers:mnemonic_inscriber_active", new ResourceLocation("embers", "mnemonic_inscriber")).texture("4", new ResourceLocation("embers", "block/mnemonic_on"));
        this.getVariantBuilder(RegistryManager.MNEMONIC_INSCRIBER.get()).forAllStates(state -> {
            Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile) (state.m_61143_(MnemonicInscriberBlock.ACTIVE) ? inscriberActiveModel : inscriberModel)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360).build();
        });
        this.simpleBlockItem(RegistryManager.MNEMONIC_INSCRIBER.get(), inscriberModel);
        ModelFile.ExistingModelFile instillerModel = this.models().getExistingFile(new ResourceLocation("embers", "char_instiller"));
        this.horizontalBlock(RegistryManager.CHAR_INSTILLER.get(), instillerModel);
        this.simpleBlockItem(RegistryManager.CHAR_INSTILLER.get(), instillerModel);
        this.horizontalBlock(RegistryManager.ATMOSPHERIC_BELLOWS.get(), this.models().getExistingFile(new ResourceLocation("embers", "atmospheric_bellows_bottom")));
        this.simpleBlockItem(RegistryManager.ATMOSPHERIC_BELLOWS.get(), this.models().getExistingFile(new ResourceLocation("embers", "atmospheric_bellows")));
        this.directionalBlock(RegistryManager.ENTROPIC_ENUMERATOR.get(), this.models().getExistingFile(new ResourceLocation("embers", "entropic_enumerator_base")));
        this.simpleBlockItem(RegistryManager.ENTROPIC_ENUMERATOR.get(), this.models().getExistingFile(new ResourceLocation("embers", "entropic_enumerator")));
        ModelFile.ExistingModelFile exchangererModel = this.models().getExistingFile(new ResourceLocation("embers", "heat_exchanger"));
        this.horizontalBlock(RegistryManager.HEAT_EXCHANGER.get(), exchangererModel);
        this.simpleBlockItem(RegistryManager.HEAT_EXCHANGER.get(), exchangererModel);
        ModelFile.ExistingModelFile insulationModel = this.models().getExistingFile(new ResourceLocation("embers", "heat_insulation"));
        this.directionalBlock(RegistryManager.HEAT_INSULATION.get(), insulationModel);
        this.simpleBlockItem(RegistryManager.HEAT_INSULATION.get(), insulationModel);
        this.directionalBlock(RegistryManager.EXCAVATION_BUCKETS.get(), this.models().getExistingFile(new ResourceLocation("embers", "excavation_buckets")));
        this.simpleBlockItem(RegistryManager.EXCAVATION_BUCKETS.get(), this.models().getExistingFile(new ResourceLocation("embers", "excavation_buckets_inventory")));
    }

    public void blockWithItem(RegistryObject<? extends Block> registryObject) {
        this.simpleBlock(registryObject.get());
        this.simpleBlockItem(registryObject.get(), this.cubeAll(registryObject.get()));
    }

    public ModelFile columnBlockWithItem(RegistryObject<? extends Block> registryObject, String sideTex, String topTex) {
        ResourceLocation side = new ResourceLocation("embers", "block/" + sideTex);
        ResourceLocation end = new ResourceLocation("embers", "block/" + topTex);
        ModelFile model = this.models().cubeColumn(registryObject.getId().getPath(), side, end);
        this.simpleBlock(registryObject.get(), model);
        this.simpleBlockItem(registryObject.get(), model);
        return model;
    }

    public ModelFile pillarBlockWithItem(RegistryObject<? extends RotatedPillarBlock> registryObject, String sideTex, String topTex) {
        ResourceLocation side = new ResourceLocation("embers", "block/" + sideTex);
        ResourceLocation end = new ResourceLocation("embers", "block/" + topTex);
        ModelFile model = this.models().cubeColumn(registryObject.getId().getPath(), side, end);
        this.axisBlock(registryObject.get(), model, model);
        this.simpleBlockItem(registryObject.get(), model);
        return model;
    }

    public void blockWithItem(RegistryObject<? extends Block> registryObject, String model) {
        ModelFile.ExistingModelFile modelFile = this.models().getExistingFile(new ResourceLocation("embers", model));
        this.simpleBlock(registryObject.get(), modelFile);
        this.simpleBlockItem(registryObject.get(), modelFile);
    }

    public void blockWithItemTexture(RegistryObject<? extends Block> registryObject, String texture) {
        ModelFile modelFile = this.models().cubeAll(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath(), new ResourceLocation("embers", "block/" + texture));
        this.simpleBlock(registryObject.get(), modelFile);
        this.simpleBlockItem(registryObject.get(), modelFile);
    }

    public void dial(RegistryObject<? extends Block> registryObject, String texture) {
        ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
        ModelFile model = this.models().withExistingParent(loc.toString(), new ResourceLocation("embers", "dial")).texture("dial", new ResourceLocation("embers", "block/" + texture)).texture("particle", new ResourceLocation("embers", "block/" + texture));
        this.directionalBlock(registryObject.get(), model);
        this.flatItem(registryObject, texture);
    }

    public void flatItem(RegistryObject<? extends Block> registryObject, String texture) {
        ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
        this.itemModels().getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(loc.getNamespace(), "item/" + texture));
    }

    public void betterDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        this.getVariantBuilder(block).forAllStates(state -> {
            Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 270 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + angleOffset) % 360).build();
        });
    }

    public void leverBlock(Block block, ModelFile lever, ModelFile leverFlipped) {
        this.getVariantBuilder(block).forAllStates(state -> {
            Direction facing = (Direction) state.m_61143_(ButtonBlock.f_54117_);
            AttachFace face = (AttachFace) state.m_61143_(ButtonBlock.f_53179_);
            boolean powered = (Boolean) state.m_61143_(ButtonBlock.POWERED);
            return ConfiguredModel.builder().modelFile(powered ? leverFlipped : lever).rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180)).rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot()).uvLock(false).build();
        });
    }

    public void fluid(RegistryObject<? extends Block> fluid, String name) {
        this.simpleBlock(fluid.get(), this.models().cubeAll(name, new ResourceLocation("embers", "block/fluid/" + name + "_still")));
    }

    public void decoBlocks(RegistryManager.StoneDecoBlocks deco) {
        ResourceLocation resourceLocation = this.blockTexture(deco.block.get());
        if (deco.stairs != null) {
            this.stairsBlock(deco.stairs.get(), resourceLocation);
            this.itemModels().stairs(deco.stairs.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
        }
        if (deco.slab != null) {
            this.slabBlock(deco.slab.get(), deco.block.getId(), resourceLocation);
            this.itemModels().slab(deco.slab.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
        }
        if (deco.wall != null) {
            this.wallBlock(deco.wall.get(), resourceLocation);
            this.itemModels().wallInventory(deco.wall.getId().getPath(), resourceLocation);
        }
    }

    public void metalSeed(RegistryManager.MetalCrystalSeed seed) {
        this.simpleBlock(seed.BLOCK.get(), this.models().cubeAll(seed.name, new ResourceLocation("embers", "block/material_" + seed.name)));
        this.flatItem(seed.BLOCK, "seed_" + seed.name);
    }

    public static void addEmitterConnections(MultiPartBlockStateBuilder builder, ModelFile pipe1, ModelFile pipe2) {
        for (Direction direction : Direction.values()) {
            Direction.Axis axis1;
            Direction.Axis axis2;
            switch(direction.getAxis()) {
                case X:
                    axis1 = Direction.Axis.Y;
                    axis2 = Direction.Axis.Z;
                    break;
                case Y:
                    axis1 = Direction.Axis.X;
                    axis2 = Direction.Axis.Z;
                    break;
                case Z:
                default:
                    axis1 = Direction.Axis.X;
                    axis2 = Direction.Axis.Y;
            }
            rotationsForDirection(builder.part().modelFile(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? pipe2 : pipe1), direction).addModel().nestedGroup().useOr().nestedGroup().condition(BlockStateProperties.FACING, Direction.fromAxisAndDirection(axis1, Direction.AxisDirection.POSITIVE), Direction.fromAxisAndDirection(axis1, Direction.AxisDirection.NEGATIVE)).condition(EmberEmitterBlock.DIRECTIONS[EmberEmitterBlock.getIndexForDirection(axis1, direction)], true).endNestedGroup().nestedGroup().condition(BlockStateProperties.FACING, Direction.fromAxisAndDirection(axis2, Direction.AxisDirection.POSITIVE), Direction.fromAxisAndDirection(axis2, Direction.AxisDirection.NEGATIVE)).condition(EmberEmitterBlock.DIRECTIONS[EmberEmitterBlock.getIndexForDirection(axis2, direction)], true).endNestedGroup().end();
        }
    }

    public static <T> ConfiguredModel.Builder<T> rotationsForDirection(ConfiguredModel.Builder<T> builder, Direction direction) {
        switch(direction) {
            case DOWN:
            default:
                return builder;
            case UP:
                return builder.rotationX(180);
            case EAST:
                return builder.rotationX(90).rotationY(270);
            case WEST:
                return builder.rotationX(90).rotationY(90);
            case SOUTH:
                return builder.rotationX(90);
            case NORTH:
                return builder.rotationX(90).rotationY(180);
        }
    }
}