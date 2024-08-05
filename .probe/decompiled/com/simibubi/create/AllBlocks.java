package com.simibubi.create;

import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsBlock;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovement;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovingInteraction;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterBlock;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.actors.plough.PloughBlock;
import com.simibubi.create.content.contraptions.actors.plough.PloughMovementBehaviour;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlock;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlockItem;
import com.simibubi.create.content.contraptions.actors.roller.RollerMovementBehaviour;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatInteractionBehaviour;
import com.simibubi.create.content.contraptions.actors.seat.SeatMovementBehaviour;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsInteractionBehaviour;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsMovementBehaviour;
import com.simibubi.create.content.contraptions.bearing.BlankSailBlockItem;
import com.simibubi.create.content.contraptions.bearing.ClockworkBearingBlock;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlock;
import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.content.contraptions.bearing.StabilizedBearingMovementBehaviour;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlock;
import com.simibubi.create.content.contraptions.behaviour.BellMovementBehaviour;
import com.simibubi.create.content.contraptions.chassis.LinearChassisBlock;
import com.simibubi.create.content.contraptions.chassis.RadialChassisBlock;
import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.elevator.ElevatorContactBlock;
import com.simibubi.create.content.contraptions.elevator.ElevatorPulleyBlock;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlockItem;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonHeadBlock;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.TrainTrapdoorBlock;
import com.simibubi.create.content.decoration.TrapdoorCTBehaviour;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.decoration.bracket.BracketBlockItem;
import com.simibubi.create.content.decoration.bracket.BracketGenerator;
import com.simibubi.create.content.decoration.copycat.CopycatBarsModel;
import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import com.simibubi.create.content.decoration.copycat.CopycatPanelModel;
import com.simibubi.create.content.decoration.copycat.CopycatStepBlock;
import com.simibubi.create.content.decoration.copycat.CopycatStepModel;
import com.simibubi.create.content.decoration.copycat.SpecialCopycatPanelBlockState;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.decoration.girder.ConnectedGirderModel;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderBlockStateGenerator;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleGenerator;
import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.equipment.bell.HauntedBellBlock;
import com.simibubi.create.content.equipment.bell.HauntedBellMovementBehaviour;
import com.simibubi.create.content.equipment.bell.PeculiarBellBlock;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlock;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlockItem;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankGenerator;
import com.simibubi.create.content.fluids.tank.FluidTankItem;
import com.simibubi.create.content.fluids.tank.FluidTankModel;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltGenerator;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import com.simibubi.create.content.kinetics.chainDrive.ChainDriveGenerator;
import com.simibubi.create.content.kinetics.chainDrive.ChainGearshiftBlock;
import com.simibubi.create.content.kinetics.clock.CuckooClockBlock;
import com.simibubi.create.content.kinetics.crafter.CrafterCTBehaviour;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.kinetics.deployer.DeployerMovingInteraction;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.NozzleBlock;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gauge.GaugeBlock;
import com.simibubi.create.content.kinetics.gauge.GaugeGenerator;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmItem;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.content.kinetics.saw.SawGenerator;
import com.simibubi.create.content.kinetics.saw.SawMovementBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.transmission.ClutchBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlock;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftGenerator;
import com.simibubi.create.content.kinetics.turntable.TurntableBlock;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlock;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlockItem;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlock;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelStructuralBlock;
import com.simibubi.create.content.logistics.chute.ChuteBlock;
import com.simibubi.create.content.logistics.chute.ChuteGenerator;
import com.simibubi.create.content.logistics.chute.ChuteItem;
import com.simibubi.create.content.logistics.chute.SmartChuteBlock;
import com.simibubi.create.content.logistics.crate.CreativeCrateBlock;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.EjectorBlock;
import com.simibubi.create.content.logistics.depot.EjectorItem;
import com.simibubi.create.content.logistics.funnel.AndesiteFunnelBlock;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.BeltFunnelGenerator;
import com.simibubi.create.content.logistics.funnel.BrassFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelGenerator;
import com.simibubi.create.content.logistics.funnel.FunnelItem;
import com.simibubi.create.content.logistics.funnel.FunnelMovementBehaviour;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelCTBehaviour;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultCTBehaviour;
import com.simibubi.create.content.logistics.vault.ItemVaultItem;
import com.simibubi.create.content.materials.ExperienceBlock;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinGenerator;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.content.processing.burner.LitBlazeBurnerBlock;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlock;
import com.simibubi.create.content.redstone.contact.ContactMovementBehaviour;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.content.redstone.contact.RedstoneContactItem;
import com.simibubi.create.content.redstone.diodes.AbstractDiodeGenerator;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.content.redstone.diodes.BrassDiodeGenerator;
import com.simibubi.create.content.redstone.diodes.PoweredLatchBlock;
import com.simibubi.create.content.redstone.diodes.PoweredLatchGenerator;
import com.simibubi.create.content.redstone.diodes.ToggleLatchBlock;
import com.simibubi.create.content.redstone.diodes.ToggleLatchGenerator;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockItem;
import com.simibubi.create.content.redstone.displayLink.source.AccumulatedItemCountDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.BoilerDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.CurrentFloorDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.EntityNameDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.FillLevelDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.FluidAmountDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.FluidListDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.ItemCountDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.ItemListDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.ItemNameDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.ItemThroughputDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.KineticSpeedDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.KineticStressDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.ObservedTrainNameSource;
import com.simibubi.create.content.redstone.displayLink.source.StationSummaryDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.StopWatchDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.TimeOfDayDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.TrainStatusDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayBoardTarget;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkGenerator;
import com.simibubi.create.content.redstone.link.controller.LecternControllerBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeGenerator;
import com.simibubi.create.content.redstone.rail.ControllerRailBlock;
import com.simibubi.create.content.redstone.rail.ControllerRailGenerator;
import com.simibubi.create.content.redstone.smartObserver.SmartObserverBlock;
import com.simibubi.create.content.redstone.smartObserver.SmartObserverGenerator;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlock;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchGenerator;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlock;
import com.simibubi.create.content.schematics.table.SchematicTableBlock;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.StandardBogeyBlock;
import com.simibubi.create.content.trains.display.FlapDisplayBlock;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.observer.TrackObserverBlock;
import com.simibubi.create.content.trains.signal.SignalBlock;
import com.simibubi.create.content.trains.station.StationBlock;
import com.simibubi.create.content.trains.track.FakeTrackBlock;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackBlockItem;
import com.simibubi.create.content.trains.track.TrackBlockStateGenerator;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.content.trains.track.TrackModel;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.simibubi.create.foundation.block.CopperBlockSet;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.MetalBarsGen;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.UncontainableBlockItem;
import com.simibubi.create.foundation.utility.ColorHandlers;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.ForgeSoundType;

public class AllBlocks {

    public static final BlockEntry<SchematicannonBlock> SCHEMATICANNON = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("schematicannon", SchematicannonBlock::new).initialProperties(() -> Blocks.DISPENSER).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).transform(TagGen.pickaxeOnly())).blockstate((ctx, prov) -> prov.simpleBlock((Block) ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov))).loot((lt, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        lt.m_247577_(block, builder.withPool(LootPool.lootPool().when(survivesExplosion).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(((SchematicannonBlock) SCHEMATICANNON.get()).m_5456_()).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Options", "BlockEntityTag.Options")))));
    }).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SchematicTableBlock> SCHEMATIC_TABLE = ((BlockBuilder) Create.REGISTRATE.block("schematic_table", SchematicTableBlock::new).initialProperties(() -> Blocks.LECTERN).properties(p -> p.mapColor(MapColor.PODZOL).forceSolidOn()).transform(TagGen.axeOrPickaxe())).blockstate((ctx, prov) -> prov.horizontalBlock((Block) ctx.getEntry(), prov.models().getExistingFile(ctx.getId()), 0)).simpleItem().register();

    public static final BlockEntry<ShaftBlock> SHAFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("shaft", ShaftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.METAL).forceSolidOn()).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.axisBlockProvider(false)).onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))).simpleItem().register();

    public static final BlockEntry<CogWheelBlock> COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("cogwheel", CogWheelBlock::small).initialProperties(SharedProperties::stone).properties(p -> p.sound(SoundType.WOOD).mapColor(MapColor.DIRT)).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.axisBlockProvider(false)).onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))).item(CogwheelBlockItem::new).build()).register();

    public static final BlockEntry<CogWheelBlock> LARGE_COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("large_cogwheel", CogWheelBlock::large).initialProperties(SharedProperties::stone).properties(p -> p.sound(SoundType.WOOD).mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).transform(BlockStressDefaults.setNoImpact())).blockstate(BlockStateGen.axisBlockProvider(false)).onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))).item(CogwheelBlockItem::new).build()).register();

    public static final BlockEntry<EncasedShaftBlock> ANDESITE_ENCASED_SHAFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_encased_shaft", p -> new EncasedShaftBlock(p, AllBlocks.ANDESITE_CASING::get)).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.encasedShaft("andesite", () -> AllSpriteShifts.ANDESITE_CASING))).transform(EncasingRegistry.addVariantTo(SHAFT))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<EncasedShaftBlock> BRASS_ENCASED_SHAFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_encased_shaft", p -> new EncasedShaftBlock(p, AllBlocks.BRASS_CASING::get)).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(BuilderTransformers.encasedShaft("brass", () -> AllSpriteShifts.BRASS_CASING))).transform(EncasingRegistry.addVariantTo(SHAFT))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<EncasedCogwheelBlock> ANDESITE_ENCASED_COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_encased_cogwheel", p -> new EncasedCogwheelBlock(p, false, AllBlocks.ANDESITE_CASING::get)).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.encasedCogwheel("andesite", () -> AllSpriteShifts.ANDESITE_CASING))).transform(EncasingRegistry.addVariantTo(COGWHEEL))).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(AllSpriteShifts.ANDESITE_CASING, Couple.create(AllSpriteShifts.ANDESITE_ENCASED_COGWHEEL_SIDE, AllSpriteShifts.ANDESITE_ENCASED_COGWHEEL_OTHERSIDE))))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<EncasedCogwheelBlock> BRASS_ENCASED_COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_encased_cogwheel", p -> new EncasedCogwheelBlock(p, false, AllBlocks.BRASS_CASING::get)).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(BuilderTransformers.encasedCogwheel("brass", () -> AllSpriteShifts.BRASS_CASING))).transform(EncasingRegistry.addVariantTo(COGWHEEL))).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(AllSpriteShifts.BRASS_CASING, Couple.create(AllSpriteShifts.BRASS_ENCASED_COGWHEEL_SIDE, AllSpriteShifts.BRASS_ENCASED_COGWHEEL_OTHERSIDE))))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<EncasedCogwheelBlock> ANDESITE_ENCASED_LARGE_COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_encased_large_cogwheel", p -> new EncasedCogwheelBlock(p, true, AllBlocks.ANDESITE_CASING::get)).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.encasedLargeCogwheel("andesite", () -> AllSpriteShifts.ANDESITE_CASING))).transform(EncasingRegistry.addVariantTo(LARGE_COGWHEEL))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<EncasedCogwheelBlock> BRASS_ENCASED_LARGE_COGWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_encased_large_cogwheel", p -> new EncasedCogwheelBlock(p, true, AllBlocks.BRASS_CASING::get)).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(BuilderTransformers.encasedLargeCogwheel("brass", () -> AllSpriteShifts.BRASS_CASING))).transform(EncasingRegistry.addVariantTo(LARGE_COGWHEEL))).transform(TagGen.axeOrPickaxe())).register();

    public static final BlockEntry<GearboxBlock> GEARBOX = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("gearbox", GearboxBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING)))).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.ANDESITE_CASING, (s, f) -> f.getAxis() == s.m_61143_(GearboxBlock.AXIS))))).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ClutchBlock> CLUTCH = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("clutch", ClutchBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<GearshiftBlock> GEARSHIFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("gearshift", GearshiftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ChainDriveBlock> ENCASED_CHAIN_DRIVE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("encased_chain_drive", ChainDriveBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> new ChainDriveGenerator((state, suffix) -> p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/" + suffix))).generate(c, p)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ChainGearshiftBlock> ADJUSTABLE_CHAIN_GEARSHIFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("adjustable_chain_gearshift", ChainGearshiftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.NETHER)).transform(BlockStressDefaults.setNoImpact())).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> new ChainDriveGenerator((state, suffix) -> {
        String powered = state.m_61143_(ChainGearshiftBlock.POWERED) ? "_powered" : "";
        return p.models().withExistingParent(c.getName() + "_" + suffix + powered, p.modLoc("block/encased_chain_drive/" + suffix)).texture("side", p.modLoc("block/" + c.getName() + powered));
    }).generate(c, p)).item().model((c, p) -> ((ItemModelBuilder) p.withExistingParent(c.getName(), p.modLoc("block/encased_chain_drive/item"))).texture("side", p.modLoc("block/" + c.getName()))).build()).register();

    public static final BlockEntry<BeltBlock> BELT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("belt", BeltBlock::new).properties(p -> p.sound(SoundType.WOOL).strength(0.8F).mapColor(MapColor.COLOR_GRAY)).addLayer(() -> RenderType::m_110457_).transform(TagGen.axeOrPickaxe())).blockstate(new BeltGenerator()::generate).transform(BlockStressDefaults.setImpact(0.0))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemNameDisplaySource(), "combine_item_names"))).onRegister(CreateRegistrate.blockModel(() -> BeltModel::new))).register();

    public static final BlockEntry<CreativeMotorBlock> CREATIVE_MOTOR = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("creative_motor", CreativeMotorBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_PURPLE).forceSolidOn()).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).transform(TagGen.pickaxeOnly())).blockstate(new CreativeMotorGenerator()::generate).transform(BlockStressDefaults.setCapacity(16384.0))).transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))).item().properties(p -> p.rarity(Rarity.EPIC)).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<WaterWheelBlock> WATER_WHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("water_wheel", WaterWheelBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.noOcclusion().mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setCapacity(32.0))).transform(BlockStressDefaults.setGeneratorSpeed(WaterWheelBlock::getSpeedRange))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<LargeWaterWheelBlock> LARGE_WATER_WHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("large_water_wheel", LargeWaterWheelBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.noOcclusion().mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, s -> s.m_61143_(LargeWaterWheelBlock.EXTENSION) ? AssetLookup.partialBaseModel(c, p, "extension") : AssetLookup.partialBaseModel(c, p))).transform(BlockStressDefaults.setCapacity(128.0))).transform(BlockStressDefaults.setGeneratorSpeed(LargeWaterWheelBlock::getSpeedRange))).item(LargeWaterWheelBlockItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<WaterWheelStructuralBlock> WATER_WHEEL_STRUCTURAL = ((BlockBuilder) Create.REGISTRATE.block("water_wheel_structure", WaterWheelStructuralBlock::new).initialProperties(SharedProperties::wooden).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStatesExcept(BlockStateGen.mapToAir(p), WaterWheelStructuralBlock.f_52588_)).properties(p -> p.noOcclusion().mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).lang("Large Water Wheel").register();

    public static final BlockEntry<EncasedFanBlock> ENCASED_FAN = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("encased_fan", EncasedFanBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).blockstate(BlockStateGen.directionalBlockProvider(true)).addLayer(() -> RenderType::m_110457_).transform(TagGen.axeOrPickaxe())).transform(BlockStressDefaults.setImpact(2.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<NozzleBlock> NOZZLE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("nozzle", NozzleBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY)).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag }).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalBlockProvider(true)).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<TurntableBlock> TURNTABLE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("turntable", TurntableBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.standardModel(c, p))).transform(BlockStressDefaults.setImpact(4.0))).simpleItem().register();

    public static final BlockEntry<HandCrankBlock> HAND_CRANK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("hand_crank", HandCrankBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalBlockProvider(true)).transform(BlockStressDefaults.setCapacity(8.0))).transform(BlockStressDefaults.setGeneratorSpeed(HandCrankBlock::getSpeedRange))).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag }).onRegister(ItemUseOverrides::addBlock)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<CuckooClockBlock> CUCKOO_CLOCK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("cuckoo_clock", CuckooClockBlock::regular).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.cuckooClock())).onRegister(AllDisplayBehaviours.assignDataBehaviour(new TimeOfDayDisplaySource(), "time_of_day"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new StopWatchDisplaySource(), "stop_watch"))).register();

    public static final BlockEntry<CuckooClockBlock> MYSTERIOUS_CUCKOO_CLOCK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mysterious_cuckoo_clock", CuckooClockBlock::mysterious).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.cuckooClock())).lang("Cuckoo Clock").onRegisterAfter(Registries.ITEM, c -> ItemDescription.referKey(c, CUCKOO_CLOCK))).register();

    public static final BlockEntry<MillstoneBlock> MILLSTONE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("millstone", MillstoneBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.METAL)).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<CrushingWheelBlock> CRUSHING_WHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("crushing_wheel", CrushingWheelBlock::new).properties(p -> p.mapColor(MapColor.METAL)).initialProperties(SharedProperties::stone).properties(BlockBehaviour.Properties::m_60955_).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, s -> AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(8.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<CrushingWheelControllerBlock> CRUSHING_WHEEL_CONTROLLER = Create.REGISTRATE.block("crushing_wheel_controller", CrushingWheelControllerBlock::new).properties(p -> p.mapColor(MapColor.STONE).noOcclusion().noLootTable().air().noCollission().pushReaction(PushReaction.BLOCK)).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStatesExcept(BlockStateGen.mapToAir(p), CrushingWheelControllerBlock.f_52588_)).register();

    public static final BlockEntry<MechanicalPressBlock> MECHANICAL_PRESS = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_press", MechanicalPressBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.horizontalBlockProvider(true)).transform(BlockStressDefaults.setImpact(8.0))).item(AssemblyOperatorBlockItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<MechanicalMixerBlock> MECHANICAL_MIXER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_mixer", MechanicalMixerBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.STONE)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(4.0))).item(AssemblyOperatorBlockItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<BasinBlock> BASIN = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("basin", BasinBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).blockstate(new BasinGenerator()::generate).addLayer(() -> RenderType::m_110457_).onRegister(AllMovementBehaviours.movementBehaviour(new BasinMovementBehaviour()))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<BlazeBurnerBlock> BLAZE_BURNER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("blaze_burner", BlazeBurnerBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).lightLevel(BlazeBurnerBlock::getLight)).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).tag(new TagKey[] { AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_BLASTING.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag, AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.tag }).loot((lt, block) -> lt.m_247577_(block, BlazeBurnerBlock.buildLootTable())).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).onRegister(AllMovementBehaviours.movementBehaviour(new BlazeBurnerMovementBehaviour()))).onRegister(AllInteractionBehaviours.interactionBehaviour(new BlazeBurnerInteractionBehaviour()))).item(BlazeBurnerBlockItem::withBlaze).model(AssetLookup.customBlockItemModel("blaze_burner", "block_with_blaze")).build()).register();

    public static final BlockEntry<LitBlazeBurnerBlock> LIT_BLAZE_BURNER = ((BlockBuilder) Create.REGISTRATE.block("lit_blaze_burner", LitBlazeBurnerBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY).lightLevel(LitBlazeBurnerBlock::getLight)).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).tag(new TagKey[] { AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_HAUNTING.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag, AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.tag }).loot((lt, block) -> lt.m_246125_(block, (ItemLike) AllItems.EMPTY_BLAZE_BURNER.get())).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.modLoc("block/blaze_burner/" + (state.m_61143_(LitBlazeBurnerBlock.FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.SOUL ? "block_with_soul_fire" : "block_with_fire")))).build())).register();

    public static final BlockEntry<DepotBlock> DEPOT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("depot", DepotBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemNameDisplaySource(), "combine_item_names"))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<EjectorBlock> WEIGHTED_EJECTOR = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("weighted_ejector", EjectorBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.COLOR_GRAY)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.horizontalBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p), 180)).transform(BlockStressDefaults.setImpact(2.0))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemNameDisplaySource(), "combine_item_names"))).item(EjectorItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ChuteBlock> CHUTE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("chute", ChuteBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).blockstate(new ChuteGenerator()::generate).item(ChuteItem::new).transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<SmartChuteBlock> SMART_CHUTE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("smart_chute", SmartChuteBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).noOcclusion().isRedstoneConductor((level, pos, state) -> false)).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, AssetLookup.forPowered(c, p))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<GaugeBlock> SPEEDOMETER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("speedometer", GaugeBlock::speed).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).transform(BlockStressDefaults.setNoImpact())).blockstate(new GaugeGenerator()::generate).onRegister(AllDisplayBehaviours.assignDataBehaviour(new KineticSpeedDisplaySource(), "kinetic_speed"))).item().transform(ModelGen.customItemModel("gauge", "_", "item"))).register();

    public static final BlockEntry<GaugeBlock> STRESSOMETER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("stressometer", GaugeBlock::stress).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).transform(BlockStressDefaults.setNoImpact())).blockstate(new GaugeGenerator()::generate).onRegister(AllDisplayBehaviours.assignDataBehaviour(new KineticStressDisplaySource(), "kinetic_stress"))).item().transform(ModelGen.customItemModel("gauge", "_", "item"))).register();

    public static final BlockEntry<BracketBlock> WOODEN_BRACKET = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("wooden_bracket", BracketBlock::new).blockstate(new BracketGenerator("wooden")::generate).properties(p -> p.sound(SoundType.SCAFFOLDING)).transform(TagGen.axeOrPickaxe())).item(BracketBlockItem::new).transform(BracketGenerator.itemModel("wooden"))).register();

    public static final BlockEntry<BracketBlock> METAL_BRACKET = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("metal_bracket", BracketBlock::new).blockstate(new BracketGenerator("metal")::generate).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).item(BracketBlockItem::new).transform(BracketGenerator.itemModel("metal"))).register();

    public static final BlockEntry<FluidPipeBlock> FLUID_PIPE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("fluid_pipe", FluidPipeBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.pipe()).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<EncasedPipeBlock> ENCASED_FLUID_PIPE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("encased_fluid_pipe", p -> new EncasedPipeBlock(p, AllBlocks.COPPER_CASING::get)).initialProperties(SharedProperties::copperMetal).properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.encasedPipe()).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING, (s, f) -> !(Boolean) s.m_61143_((Property) EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).loot((p, b) -> p.m_246125_(b, (ItemLike) FLUID_PIPE.get())).transform(EncasingRegistry.addVariantTo(FLUID_PIPE))).register();

    public static final BlockEntry<GlassFluidPipeBlock> GLASS_FLUID_PIPE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("glass_fluid_pipe", GlassFluidPipeBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.forceSolidOn()).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.getVariantBuilder((Block) c.getEntry()).forAllStatesExcept(state -> {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
        return ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.modLoc("block/fluid_pipe/window"))).uvLock(false).rotationX(axis == Direction.Axis.Y ? 0 : 90).rotationY(axis == Direction.Axis.X ? 90 : 0).build();
    }, BlockStateProperties.WATERLOGGED)).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).loot((p, b) -> p.m_246125_(b, (ItemLike) FLUID_PIPE.get())).register();

    public static final BlockEntry<PumpBlock> MECHANICAL_PUMP = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_pump", PumpBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.mapColor(MapColor.STONE)).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true)).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SmartFluidPipeBlock> SMART_FLUID_PIPE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("smart_fluid_pipe", SmartFluidPipeBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.pickaxeOnly())).blockstate(new SmartFluidPipeGenerator()::generate).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<FluidValveBlock> FLUID_VALVE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("fluid_valve", FluidValveBlock::new).initialProperties(SharedProperties::copperMetal).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p, (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal", state.m_61143_(FluidValveBlock.ENABLED) ? "open" : "closed"))).onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ValveHandleBlock> COPPER_VALVE_HANDLE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("copper_valve_handle", ValveHandleBlock::copper).transform(TagGen.pickaxeOnly())).transform(BuilderTransformers.valveHandle(null))).transform(BlockStressDefaults.setCapacity(8.0))).register();

    public static final DyedBlockList<ValveHandleBlock> DYED_VALVE_HANDLES = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(colourName + "_valve_handle", p -> ValveHandleBlock.dyed(p, colour)).properties(p -> p.mapColor(colour.getMapColor())).transform(TagGen.pickaxeOnly())).transform(BuilderTransformers.valveHandle(colour))).recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) c.get()).requires(colour.getTag()).requires(AllTags.AllItemTags.VALVE_HANDLES.tag).unlockedBy("has_valve", RegistrateRecipeProvider.m_206406_(AllTags.AllItemTags.VALVE_HANDLES.tag)).save(p, Create.asResource("crafting/kinetics/" + c.getName() + "_from_other_valve_handle"))).register();
    });

    public static final BlockEntry<FluidTankBlock> FLUID_TANK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("fluid_tank", FluidTankBlock::regular).initialProperties(SharedProperties::copperMetal).properties(p -> p.noOcclusion().isRedstoneConductor((p1, p2, p3) -> true)).transform(TagGen.pickaxeOnly())).blockstate(new FluidTankGenerator()::generate).onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::standard))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new BoilerDisplaySource(), "boiler_status"))).addLayer(() -> RenderType::m_110457_).item(FluidTankItem::new).model(AssetLookup.customBlockItemModel("_", "block_single_window")).build()).register();

    public static final BlockEntry<FluidTankBlock> CREATIVE_FLUID_TANK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("creative_fluid_tank", FluidTankBlock::creative).initialProperties(SharedProperties::copperMetal).properties(p -> p.noOcclusion().mapColor(MapColor.COLOR_PURPLE)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new FluidTankGenerator("creative_")::generate).onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::creative))).addLayer(() -> RenderType::m_110457_).item(FluidTankItem::new).properties(p -> p.rarity(Rarity.EPIC)).model((c, p) -> ((ItemModelBuilder) p.withExistingParent(c.getName(), p.modLoc("block/fluid_tank/block_single_window"))).texture("5", p.modLoc("block/creative_fluid_tank_window_single")).texture("1", p.modLoc("block/creative_fluid_tank")).texture("particle", p.modLoc("block/creative_fluid_tank")).texture("4", p.modLoc("block/creative_casing")).texture("0", p.modLoc("block/creative_casing"))).build()).register();

    public static final BlockEntry<HosePulleyBlock> HOSE_PULLEY = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("hose_pulley", HosePulleyBlock::new).initialProperties(SharedProperties::copperMetal).properties(BlockBehaviour.Properties::m_60955_).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.horizontalBlockProvider(true)).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ItemDrainBlock> ITEM_DRAIN = ((BlockBuilder) Create.REGISTRATE.block("item_drain", ItemDrainBlock::new).initialProperties(SharedProperties::copperMetal).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).blockstate((c, p) -> p.simpleBlock((Block) c.get(), AssetLookup.standardModel(c, p))).simpleItem().register();

    public static final BlockEntry<SpoutBlock> SPOUT = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("spout", SpoutBlock::new).initialProperties(SharedProperties::copperMetal).transform(TagGen.pickaxeOnly())).blockstate((ctx, prov) -> prov.simpleBlock((Block) ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov))).addLayer(() -> RenderType::m_110457_).item(AssemblyOperatorBlockItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<PortableStorageInterfaceBlock> PORTABLE_FLUID_INTERFACE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("portable_fluid_interface", PortableStorageInterfaceBlock::forFluids).initialProperties(SharedProperties::copperMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).onRegister(AllMovementBehaviours.movementBehaviour(new PortableStorageInterfaceMovement()))).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SteamEngineBlock> STEAM_ENGINE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("steam_engine", SteamEngineBlock::new).initialProperties(SharedProperties::copperMetal).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.horizontalFaceBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).transform(BlockStressDefaults.setCapacity(1024.0))).transform(BlockStressDefaults.setGeneratorSpeed(SteamEngineBlock::getSpeedRange))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<WhistleBlock> STEAM_WHISTLE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("steam_whistle", WhistleBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.mapColor(MapColor.GOLD)).transform(TagGen.pickaxeOnly())).blockstate(new WhistleGenerator()::generate).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<WhistleExtenderBlock> STEAM_WHISTLE_EXTENSION = ((BlockBuilder) Create.REGISTRATE.block("steam_whistle_extension", WhistleExtenderBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.mapColor(MapColor.GOLD).forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.whistleExtender()).register();

    public static final BlockEntry<PoweredShaftBlock> POWERED_SHAFT = ((BlockBuilder) Create.REGISTRATE.block("powered_shaft", PoweredShaftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.METAL).forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.axisBlockProvider(false)).loot((lt, block) -> lt.m_246125_(block, (ItemLike) SHAFT.get())).register();

    public static final BlockEntry<MechanicalPistonBlock> MECHANICAL_PISTON = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_piston", MechanicalPistonBlock::normal).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.mechanicalPiston(PistonType.DEFAULT))).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).register();

    public static final BlockEntry<MechanicalPistonBlock> STICKY_MECHANICAL_PISTON = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("sticky_mechanical_piston", MechanicalPistonBlock::sticky).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.mechanicalPiston(PistonType.STICKY))).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).register();

    public static final BlockEntry<PistonExtensionPoleBlock> PISTON_EXTENSION_POLE = ((BlockBuilder) Create.REGISTRATE.block("piston_extension_pole", PistonExtensionPoleBlock::new).initialProperties(() -> Blocks.PISTON_HEAD).properties(p -> p.sound(SoundType.SCAFFOLDING).mapColor(MapColor.DIRT).forceSolidOn()).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(false)).simpleItem().register();

    public static final BlockEntry<MechanicalPistonHeadBlock> MECHANICAL_PISTON_HEAD = ((BlockBuilder) Create.REGISTRATE.block("mechanical_piston_head", MechanicalPistonHeadBlock::new).initialProperties(() -> Blocks.PISTON_HEAD).properties(p -> p.mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).loot((p, b) -> p.m_246125_(b, (ItemLike) PISTON_EXTENSION_POLE.get())).blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, state -> p.models().getExistingFile(p.modLoc("block/mechanical_piston/" + ((PistonType) state.m_61143_(MechanicalPistonHeadBlock.TYPE)).getSerializedName() + "/head")))).register();

    public static final BlockEntry<GantryCarriageBlock> GANTRY_CARRIAGE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("gantry_carriage", GantryCarriageBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalAxisBlockProvider()).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<GantryShaftBlock> GANTRY_SHAFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("gantry_shaft", GantryShaftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.NETHER).forceSolidOn()).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), s -> {
        boolean isPowered = (Boolean) s.m_61143_(GantryShaftBlock.POWERED);
        boolean isFlipped = ((Direction) s.m_61143_(GantryShaftBlock.FACING)).getAxisDirection() == Direction.AxisDirection.NEGATIVE;
        String partName = ((GantryShaftBlock.Part) s.m_61143_(GantryShaftBlock.PART)).getSerializedName();
        String flipped = isFlipped ? "_flipped" : "";
        String powered = isPowered ? "_powered" : "";
        ModelFile existing = AssetLookup.partialBaseModel(c, p, partName);
        return (ModelFile) (!isPowered && !isFlipped ? existing : p.models().withExistingParent("block/" + c.getName() + "_" + partName + powered + flipped, existing.getLocation()).texture("2", p.modLoc("block/" + c.getName() + powered + flipped)));
    })).transform(BlockStressDefaults.setNoImpact())).item().transform(ModelGen.customItemModel("_", "block_single"))).register();

    public static final BlockEntry<WindmillBearingBlock> WINDMILL_BEARING = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("windmill_bearing", WindmillBearingBlock::new).transform(TagGen.axeOrPickaxe())).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.bearing("windmill", "gearbox"))).transform(BlockStressDefaults.setCapacity(512.0))).transform(BlockStressDefaults.setGeneratorSpeed(WindmillBearingBlock::getSpeedRange))).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).register();

    public static final BlockEntry<MechanicalBearingBlock> MECHANICAL_BEARING = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_bearing", MechanicalBearingBlock::new).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.bearing("mechanical", "gearbox"))).transform(BlockStressDefaults.setImpact(4.0))).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).onRegister(AllMovementBehaviours.movementBehaviour(new StabilizedBearingMovementBehaviour()))).register();

    public static final BlockEntry<ClockworkBearingBlock> CLOCKWORK_BEARING = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("clockwork_bearing", ClockworkBearingBlock::new).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(TagGen.axeOrPickaxe())).transform(BuilderTransformers.bearing("clockwork", "brass_gearbox"))).transform(BlockStressDefaults.setImpact(4.0))).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).register();

    public static final BlockEntry<PulleyBlock> ROPE_PULLEY = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("rope_pulley", PulleyBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(BlockStateGen.horizontalAxisBlockProvider(true)).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<PulleyBlock.RopeBlock> ROPE = Create.REGISTRATE.block("rope", PulleyBlock.RopeBlock::new).properties(p -> p.sound(SoundType.WOOL).mapColor(MapColor.COLOR_BROWN)).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag }).tag(new TagKey[] { BlockTags.CLIMBABLE }).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().getExistingFile(p.modLoc("block/rope_pulley/" + c.getName())))).register();

    public static final BlockEntry<PulleyBlock.MagnetBlock> PULLEY_MAGNET = Create.REGISTRATE.block("pulley_magnet", PulleyBlock.MagnetBlock::new).initialProperties(SharedProperties::stone).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag }).tag(new TagKey[] { BlockTags.CLIMBABLE }).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().getExistingFile(p.modLoc("block/rope_pulley/" + c.getName())))).register();

    public static final BlockEntry<ElevatorPulleyBlock> ELEVATOR_PULLEY = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("elevator_pulley", ElevatorPulleyBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.horizontalBlockProvider(true)).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<CartAssemblerBlock> CART_ASSEMBLER = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("cart_assembler", CartAssemblerBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion().mapColor(MapColor.COLOR_GRAY)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.cartAssembler()).addLayer(() -> RenderType::m_110457_).tag(new TagKey[] { BlockTags.RAILS, AllTags.AllBlockTags.SAFE_NBT.tag }).item(CartAssemblerBlockItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ControllerRailBlock> CONTROLLER_RAIL = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("controller_rail", ControllerRailBlock::new).initialProperties(() -> Blocks.POWERED_RAIL).transform(TagGen.pickaxeOnly())).blockstate(new ControllerRailGenerator()::generate).addLayer(() -> RenderType::m_110457_).color(() -> ColorHandlers::getRedstonePower).tag(new TagKey[] { BlockTags.RAILS }).item().model((c, p) -> p.generated(c, new ResourceLocation[] { Create.asResource("block/" + c.getName()) })).build()).register();

    public static final BlockEntry<CartAssemblerBlock.MinecartAnchorBlock> MINECART_ANCHOR = Create.REGISTRATE.block("minecart_anchor", CartAssemblerBlock.MinecartAnchorBlock::new).initialProperties(SharedProperties::stone).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().getExistingFile(p.modLoc("block/cart_assembler/" + c.getName())))).register();

    public static final BlockEntry<LinearChassisBlock> LINEAR_CHASSIS = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("linear_chassis", LinearChassisBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(BlockStateGen.linearChassis()).onRegister(CreateRegistrate.connectedTextures(LinearChassisBlock.ChassisCTBehaviour::new))).lang("Linear Chassis").simpleItem().register();

    public static final BlockEntry<LinearChassisBlock> SECONDARY_LINEAR_CHASSIS = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("secondary_linear_chassis", LinearChassisBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(BlockStateGen.linearChassis()).onRegister(CreateRegistrate.connectedTextures(LinearChassisBlock.ChassisCTBehaviour::new))).simpleItem().register();

    public static final BlockEntry<RadialChassisBlock> RADIAL_CHASSIS = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("radial_chassis", RadialChassisBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.DIRT)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(BlockStateGen.radialChassis()).item().model((c, p) -> {
        String path = "block/" + c.getName();
        p.cubeColumn(c.getName(), p.modLoc(path + "_side"), p.modLoc(path + "_end"));
    }).build()).register();

    public static final BlockEntry<StickerBlock> STICKER = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("sticker", StickerBlock::new).initialProperties(SharedProperties::stone).transform(TagGen.pickaxeOnly())).properties(BlockBehaviour.Properties::m_60955_).addLayer(() -> RenderType::m_110457_).blockstate((c, p) -> p.directionalBlock((Block) c.get(), AssetLookup.forPowered(c, p))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ContraptionControlsBlock> CONTRAPTION_CONTROLS = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("contraption_controls", ContraptionControlsBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).addLayer(() -> RenderType::m_110457_).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), s -> AssetLookup.partialBaseModel(c, p))).onRegister(AllMovementBehaviours.movementBehaviour(new ContraptionControlsMovement()))).onRegister(AllInteractionBehaviours.interactionBehaviour(new ContraptionControlsMovingInteraction()))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<DrillBlock> MECHANICAL_DRILL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_drill", DrillBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalBlockProvider(true)).transform(BlockStressDefaults.setImpact(4.0))).onRegister(AllMovementBehaviours.movementBehaviour(new DrillMovementBehaviour()))).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SawBlock> MECHANICAL_SAW = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_saw", SawBlock::new).initialProperties(SharedProperties::stone).addLayer(() -> RenderType::m_110457_).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(new SawGenerator()::generate).transform(BlockStressDefaults.setImpact(4.0))).onRegister(AllMovementBehaviours.movementBehaviour(new SawMovementBehaviour()))).addLayer(() -> RenderType::m_110457_).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<DeployerBlock> DEPLOYER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("deployer", DeployerBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.directionalAxisBlockProvider()).transform(BlockStressDefaults.setImpact(4.0))).onRegister(AllMovementBehaviours.movementBehaviour(new DeployerMovementBehaviour()))).onRegister(AllInteractionBehaviours.interactionBehaviour(new DeployerMovingInteraction()))).item(AssemblyOperatorBlockItem::new).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<PortableStorageInterfaceBlock> PORTABLE_STORAGE_INTERFACE = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("portable_storage_interface", PortableStorageInterfaceBlock::forItems).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.PODZOL)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).onRegister(AllMovementBehaviours.movementBehaviour(new PortableStorageInterfaceMovement()))).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<RedstoneContactBlock> REDSTONE_CONTACT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("redstone_contact", RedstoneContactBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).transform(TagGen.axeOrPickaxe())).onRegister(AllMovementBehaviours.movementBehaviour(new ContactMovementBehaviour()))).blockstate((c, p) -> p.directionalBlock((Block) c.get(), AssetLookup.forPowered(c, p))).item(RedstoneContactItem::new).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<ElevatorContactBlock> ELEVATOR_CONTACT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("elevator_contact", ElevatorContactBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW).lightLevel(ElevatorContactBlock::getLight)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), state -> {
        Boolean calling = (Boolean) state.m_61143_(ElevatorContactBlock.CALLING);
        Boolean powering = (Boolean) state.m_61143_(ElevatorContactBlock.POWERING);
        return powering ? AssetLookup.partialBaseModel(c, p, "powered") : (calling ? AssetLookup.partialBaseModel(c, p, "dim") : AssetLookup.partialBaseModel(c, p));
    })).loot((p, b) -> p.m_246125_(b, (ItemLike) REDSTONE_CONTACT.get())).onRegister(AllDisplayBehaviours.assignDataBehaviour(new CurrentFloorDisplaySource(), "current_floor"))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<HarvesterBlock> MECHANICAL_HARVESTER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_harvester", HarvesterBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.METAL).forceSolidOn()).transform(TagGen.axeOrPickaxe())).onRegister(AllMovementBehaviours.movementBehaviour(new HarvesterMovementBehaviour()))).blockstate(BlockStateGen.horizontalBlockProvider(true)).addLayer(() -> RenderType::m_110457_).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<PloughBlock> MECHANICAL_PLOUGH = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_plough", PloughBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY).forceSolidOn()).transform(TagGen.axeOrPickaxe())).onRegister(AllMovementBehaviours.movementBehaviour(new PloughMovementBehaviour()))).blockstate(BlockStateGen.horizontalBlockProvider(false)).item().tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).build()).register();

    public static final BlockEntry<RollerBlock> MECHANICAL_ROLLER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_roller", RollerBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion()).transform(TagGen.axeOrPickaxe())).onRegister(AllMovementBehaviours.movementBehaviour(new RollerMovementBehaviour()))).blockstate(BlockStateGen.horizontalBlockProvider(true)).addLayer(() -> RenderType::m_110457_).item(RollerBlockItem::new).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SailBlock> SAIL_FRAME = ((BlockBuilder) Create.REGISTRATE.block("sail_frame", p -> SailBlock.frame(p)).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.DIRT).sound(SoundType.SCAFFOLDING).noOcclusion()).transform(TagGen.axeOnly())).blockstate(BlockStateGen.directionalBlockProvider(false)).lang("Windmill Sail Frame").tag(new TagKey[] { AllTags.AllBlockTags.WINDMILL_SAILS.tag }).tag(new TagKey[] { AllTags.AllBlockTags.FAN_TRANSPARENT.tag }).simpleItem().register();

    public static final BlockEntry<SailBlock> SAIL = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("white_sail", p -> SailBlock.withCanvas(p, DyeColor.WHITE)).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.SNOW).sound(SoundType.SCAFFOLDING).noOcclusion()).transform(TagGen.axeOnly())).blockstate(BlockStateGen.directionalBlockProvider(false)).lang("Windmill Sail").tag(new TagKey[] { AllTags.AllBlockTags.WINDMILL_SAILS.tag }).item(BlankSailBlockItem::new).build()).register();

    public static final DyedBlockList<SailBlock> DYED_SAILS = new DyedBlockList<>(colour -> {
        if (colour == DyeColor.WHITE) {
            return SAIL;
        } else {
            String colourName = colour.getSerializedName();
            return ((BlockBuilder) Create.REGISTRATE.block(colourName + "_sail", p -> SailBlock.withCanvas(p, colour)).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(colour.getMapColor()).sound(SoundType.SCAFFOLDING).noOcclusion()).transform(TagGen.axeOnly())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), p.models().withExistingParent(colourName + "_sail", p.modLoc("block/white_sail")).texture("0", p.modLoc("block/sail/canvas_" + colourName)))).tag(new TagKey[] { AllTags.AllBlockTags.WINDMILL_SAILS.tag }).loot((p, b) -> p.m_246125_(b, (ItemLike) SAIL.get())).register();
        }
    });

    public static final BlockEntry<CasingBlock> ANDESITE_CASING = ((BlockBuilder) Create.REGISTRATE.block("andesite_casing", CasingBlock::new).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.casing(() -> AllSpriteShifts.ANDESITE_CASING))).register();

    public static final BlockEntry<CasingBlock> BRASS_CASING = ((BlockBuilder) Create.REGISTRATE.block("brass_casing", CasingBlock::new).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(BuilderTransformers.casing(() -> AllSpriteShifts.BRASS_CASING))).register();

    public static final BlockEntry<CasingBlock> COPPER_CASING = ((BlockBuilder) Create.REGISTRATE.block("copper_casing", CasingBlock::new).properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.COPPER)).transform(BuilderTransformers.casing(() -> AllSpriteShifts.COPPER_CASING))).register();

    public static final BlockEntry<CasingBlock> SHADOW_STEEL_CASING = ((BlockBuilder) Create.REGISTRATE.block("shadow_steel_casing", CasingBlock::new).properties(p -> p.mapColor(MapColor.COLOR_BLACK)).transform(BuilderTransformers.casing(() -> AllSpriteShifts.SHADOW_STEEL_CASING))).lang("Shadow Casing").register();

    public static final BlockEntry<CasingBlock> REFINED_RADIANCE_CASING = ((BlockBuilder) Create.REGISTRATE.block("refined_radiance_casing", CasingBlock::new).properties(p -> p.mapColor(MapColor.SNOW)).transform(BuilderTransformers.casing(() -> AllSpriteShifts.REFINED_RADIANCE_CASING))).properties(p -> p.lightLevel($ -> 12)).lang("Radiant Casing").register();

    public static final BlockEntry<MechanicalCrafterBlock> MECHANICAL_CRAFTER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_crafter", MechanicalCrafterBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).blockstate(BlockStateGen.horizontalBlockProvider(true)).transform(BlockStressDefaults.setImpact(2.0))).onRegister(CreateRegistrate.connectedTextures(CrafterCTBehaviour::new))).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SequencedGearshiftBlock> SEQUENCED_GEARSHIFT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("sequenced_gearshift", SequencedGearshiftBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).properties(BlockBehaviour.Properties::m_60955_).transform(BlockStressDefaults.setNoImpact())).blockstate(new SequencedGearshiftGenerator()::generate).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<FlywheelBlock> FLYWHEEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("flywheel", FlywheelBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).transform(BlockStressDefaults.setNoImpact())).blockstate(BlockStateGen.axisBlockProvider(true)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SpeedControllerBlock> ROTATION_SPEED_CONTROLLER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("rotation_speed_controller", SpeedControllerBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).transform(BlockStressDefaults.setNoImpact())).blockstate(BlockStateGen.horizontalAxisBlockProvider(true)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ArmBlock> MECHANICAL_ARM = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("mechanical_arm", ArmBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(s -> ConfiguredModel.builder().modelFile(AssetLookup.partialBaseModel(c, p)).rotationX(s.m_61143_(ArmBlock.CEILING) ? 180 : 0).build())).transform(BlockStressDefaults.setImpact(2.0))).item(ArmItem::new).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<TrackBlock> TRACK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("track", TrackMaterial.ANDESITE::createBlock).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.METAL).strength(0.8F).sound(SoundType.METAL).noOcclusion().forceSolidOn()).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).onRegister(CreateRegistrate.blockModel(() -> TrackModel::new))).blockstate(new TrackBlockStateGenerator()::generate).tag(new TagKey[] { AllTags.AllBlockTags.RELOCATION_NOT_SUPPORTED.tag }).tag(new TagKey[] { AllTags.AllBlockTags.TRACKS.tag }).tag(new TagKey[] { AllTags.AllBlockTags.GIRDABLE_TRACKS.tag }).lang("Train Track").item(TrackBlockItem::new).model((c, p) -> p.generated(c, new ResourceLocation[] { Create.asResource("item/" + c.getName()) })).build()).register();

    public static final BlockEntry<FakeTrackBlock> FAKE_TRACK = Create.REGISTRATE.block("fake_track", FakeTrackBlock::new).properties(p -> p.mapColor(MapColor.METAL).noCollission().noOcclusion().replaceable()).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().withExistingParent(c.getName(), p.mcLoc("block/air")))).lang("Track Marker for Maps").register();

    public static final BlockEntry<CasingBlock> RAILWAY_CASING = ((BlockBuilder) Create.REGISTRATE.block("railway_casing", CasingBlock::new).transform(BuilderTransformers.layeredCasing(() -> AllSpriteShifts.RAILWAY_CASING_SIDE, () -> AllSpriteShifts.RAILWAY_CASING))).properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.NETHERITE_BLOCK)).lang("Train Casing").register();

    public static final BlockEntry<StationBlock> TRACK_STATION = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("track_station", StationBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.simpleBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new StationSummaryDisplaySource(), "station_summary"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new TrainStatusDisplaySource(), "train_status"))).lang("Train Station").item(TrackTargetingBlockItem.ofType(EdgePointType.STATION)).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<SignalBlock> TRACK_SIGNAL = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("track_signal", SignalBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.PODZOL).noOcclusion().sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(AssetLookup.partialBaseModel(c, p, ((SignalBlock.SignalType) state.m_61143_(SignalBlock.TYPE)).getSerializedName())).build())).lang("Train Signal").item(TrackTargetingBlockItem.ofType(EdgePointType.SIGNAL)).transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<TrackObserverBlock> TRACK_OBSERVER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("track_observer", TrackObserverBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.PODZOL).noOcclusion().sound(SoundType.NETHERITE_BLOCK)).blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, AssetLookup.forPowered(c, p))).transform(TagGen.pickaxeOnly())).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ObservedTrainNameSource(), "observed_train_name"))).lang("Train Observer").item(TrackTargetingBlockItem.ofType(EdgePointType.OBSERVER)).transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<StandardBogeyBlock> SMALL_BOGEY = ((BlockBuilder) Create.REGISTRATE.block("small_bogey", p -> new StandardBogeyBlock(p, BogeySizes.SMALL)).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.bogey())).register();

    public static final BlockEntry<StandardBogeyBlock> LARGE_BOGEY = ((BlockBuilder) Create.REGISTRATE.block("large_bogey", p -> new StandardBogeyBlock(p, BogeySizes.LARGE)).properties(p -> p.mapColor(MapColor.PODZOL)).transform(BuilderTransformers.bogey())).register();

    public static final BlockEntry<ControlsBlock> TRAIN_CONTROLS = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("controls", ControlsBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.NETHERITE_BLOCK)).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), s -> AssetLookup.partialBaseModel(c, p, s.m_61143_(ControlsBlock.VIRTUAL) ? "virtual" : (s.m_61143_(ControlsBlock.OPEN) ? "open" : "closed")))).onRegister(AllMovementBehaviours.movementBehaviour(new ControlsMovementBehaviour()))).onRegister(AllInteractionBehaviours.interactionBehaviour(new ControlsInteractionBehaviour()))).lang("Train Controls").item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<ItemVaultBlock> ITEM_VAULT = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("item_vault", ItemVaultBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.NETHERITE_BLOCK).explosionResistance(1200.0F)).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(s -> ConfiguredModel.builder().modelFile(AssetLookup.standardModel(c, p)).rotationY(s.m_61143_(ItemVaultBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0).build())).onRegister(CreateRegistrate.connectedTextures(ItemVaultCTBehaviour::new))).item(ItemVaultItem::new).build()).register();

    public static final BlockEntry<AndesiteFunnelBlock> ANDESITE_FUNNEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_funnel", AndesiteFunnelBlock::new).addLayer(() -> RenderType::m_110457_).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.STONE)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).onRegister(AllMovementBehaviours.movementBehaviour(FunnelMovementBehaviour.andesite()))).blockstate(new FunnelGenerator("andesite", false)::generate).item(FunnelItem::new).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).model(FunnelGenerator.itemModel("andesite")).build()).register();

    public static final BlockEntry<BeltFunnelBlock> ANDESITE_BELT_FUNNEL = ((BlockBuilder) Create.REGISTRATE.block("andesite_belt_funnel", p -> new BeltFunnelBlock(ANDESITE_FUNNEL, p)).addLayer(() -> RenderType::m_110457_).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.STONE)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new BeltFunnelGenerator("andesite")::generate).loot((p, b) -> p.m_246125_(b, (ItemLike) ANDESITE_FUNNEL.get())).register();

    public static final BlockEntry<BrassFunnelBlock> BRASS_FUNNEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_funnel", BrassFunnelBlock::new).addLayer(() -> RenderType::m_110457_).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).onRegister(AllMovementBehaviours.movementBehaviour(FunnelMovementBehaviour.brass()))).blockstate(new FunnelGenerator("brass", true)::generate).item(FunnelItem::new).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).model(FunnelGenerator.itemModel("brass")).build()).register();

    public static final BlockEntry<BeltFunnelBlock> BRASS_BELT_FUNNEL = ((BlockBuilder) Create.REGISTRATE.block("brass_belt_funnel", p -> new BeltFunnelBlock(BRASS_FUNNEL, p)).addLayer(() -> RenderType::m_110457_).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new BeltFunnelGenerator("brass")::generate).loot((p, b) -> p.m_246125_(b, (ItemLike) BRASS_FUNNEL.get())).register();

    public static final BlockEntry<BeltTunnelBlock> ANDESITE_TUNNEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_tunnel", BeltTunnelBlock::new).properties(p -> p.mapColor(MapColor.STONE)).transform(BuilderTransformers.beltTunnel("andesite", new ResourceLocation("block/polished_andesite")))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new AccumulatedItemCountDisplaySource(), "accumulate_items"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemThroughputDisplaySource(), "item_throughput"))).register();

    public static final BlockEntry<BrassTunnelBlock> BRASS_TUNNEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_tunnel", BrassTunnelBlock::new).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)).transform(BuilderTransformers.beltTunnel("brass", Create.asResource("block/brass_block")))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new AccumulatedItemCountDisplaySource(), "accumulate_items"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemThroughputDisplaySource(), "item_throughput"))).onRegister(CreateRegistrate.connectedTextures(BrassTunnelCTBehaviour::new))).register();

    public static final BlockEntry<SmartObserverBlock> SMART_OBSERVER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("content_observer", SmartObserverBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).noOcclusion()).transform(TagGen.axeOrPickaxe())).blockstate(new SmartObserverGenerator()::generate).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemCountDisplaySource(), "count_items"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new ItemListDisplaySource(), "list_items"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new FluidAmountDisplaySource(), "count_fluids"))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new FluidListDisplaySource(), "list_fluids"))).lang("Smart Observer").item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<ThresholdSwitchBlock> THRESHOLD_SWITCH = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("stockpile_switch", ThresholdSwitchBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).noOcclusion()).transform(TagGen.axeOrPickaxe())).blockstate(new ThresholdSwitchGenerator()::generate).onRegister(AllDisplayBehaviours.assignDataBehaviour(new FillLevelDisplaySource(), "fill_level"))).lang("Threshold Switch").item().transform(ModelGen.customItemModel("threshold_switch", "block_wall"))).register();

    public static final BlockEntry<CreativeCrateBlock> CREATIVE_CRATE = ((BlockBuilder) Create.REGISTRATE.block("creative_crate", CreativeCrateBlock::new).transform(BuilderTransformers.crate("creative"))).properties(p -> p.mapColor(MapColor.COLOR_PURPLE)).register();

    public static final BlockEntry<DisplayLinkBlock> DISPLAY_LINK = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("display_link", DisplayLinkBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)).addLayer(() -> RenderType::m_110466_).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.directionalBlock((Block) c.get(), AssetLookup.forPowered(c, p))).item(DisplayLinkBlockItem::new).transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<FlapDisplayBlock> DISPLAY_BOARD = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("display_board", FlapDisplayBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).transform(BlockStressDefaults.setImpact(0.0))).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new DisplayBoardTarget()))).lang("Display Board").item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<NixieTubeBlock> ORANGE_NIXIE_TUBE = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("nixie_tube", p -> new NixieTubeBlock(p, DyeColor.ORANGE)).initialProperties(SharedProperties::softMetal).properties(p -> p.lightLevel($ -> 5).mapColor(DyeColor.ORANGE).forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate(new NixieTubeGenerator()::generate).addLayer(() -> RenderType::m_110466_).item().transform(ModelGen.customItemModel())).register();

    public static final DyedBlockList<NixieTubeBlock> NIXIE_TUBES = new DyedBlockList<>(colour -> {
        if (colour == DyeColor.ORANGE) {
            return ORANGE_NIXIE_TUBE;
        } else {
            String colourName = colour.getSerializedName();
            return ((BlockBuilder) Create.REGISTRATE.block(colourName + "_nixie_tube", p -> new NixieTubeBlock(p, colour)).initialProperties(SharedProperties::softMetal).properties(p -> p.lightLevel($ -> 5).mapColor(colour).forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate(new NixieTubeGenerator()::generate).loot((p, b) -> p.m_246125_(b, (ItemLike) ORANGE_NIXIE_TUBE.get())).addLayer(() -> RenderType::m_110466_).register();
        }
    });

    public static final BlockEntry<RoseQuartzLampBlock> ROSE_QUARTZ_LAMP = ((BlockBuilder) Create.REGISTRATE.block("rose_quartz_lamp", RoseQuartzLampBlock::new).initialProperties(() -> Blocks.REDSTONE_LAMP).properties(p -> p.mapColor(MapColor.TERRACOTTA_PINK).lightLevel(s -> s.m_61143_(RoseQuartzLampBlock.POWERING) ? 15 : 0)).blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, s -> {
        boolean powered = (Boolean) s.m_61143_(RoseQuartzLampBlock.POWERING);
        String name = c.getName() + (powered ? "_powered" : "");
        return p.models().cubeAll(name, p.modLoc("block/" + name));
    })).transform(TagGen.pickaxeOnly())).simpleItem().register();

    public static final BlockEntry<RedstoneLinkBlock> REDSTONE_LINK = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("redstone_link", RedstoneLinkBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).forceSolidOn()).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag, AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new RedstoneLinkGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel("_", "transmitter"))).register();

    public static final BlockEntry<AnalogLeverBlock> ANALOG_LEVER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("analog_lever", AnalogLeverBlock::new).initialProperties(() -> Blocks.LEVER).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate((c, p) -> p.horizontalFaceBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).onRegister(ItemUseOverrides::addBlock)).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<PlacardBlock> PLACARD = ((BlockBuilder) Create.REGISTRATE.block("placard", PlacardBlock::new).initialProperties(SharedProperties::copperMetal).properties(p -> p.forceSolidOn()).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.horizontalFaceBlock((Block) c.get(), AssetLookup.standardModel(c, p))).simpleItem().register();

    public static final BlockEntry<BrassDiodeBlock> PULSE_REPEATER = ((BlockBuilder) Create.REGISTRATE.block("pulse_repeater", BrassDiodeBlock::new).initialProperties(() -> Blocks.REPEATER).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new BrassDiodeGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().model(AbstractDiodeGenerator::diodeItemModel).build()).register();

    public static final BlockEntry<BrassDiodeBlock> PULSE_EXTENDER = ((BlockBuilder) Create.REGISTRATE.block("pulse_extender", BrassDiodeBlock::new).initialProperties(() -> Blocks.REPEATER).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate(new BrassDiodeGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().model(AbstractDiodeGenerator::diodeItemModel).build()).register();

    public static final BlockEntry<PoweredLatchBlock> POWERED_LATCH = Create.REGISTRATE.block("powered_latch", PoweredLatchBlock::new).initialProperties(() -> Blocks.REPEATER).blockstate(new PoweredLatchGenerator()::generate).addLayer(() -> RenderType::m_110457_).simpleItem().register();

    public static final BlockEntry<ToggleLatchBlock> POWERED_TOGGLE_LATCH = ((BlockBuilder) Create.REGISTRATE.block("powered_toggle_latch", ToggleLatchBlock::new).initialProperties(() -> Blocks.REPEATER).blockstate(new ToggleLatchGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel("diodes", "latch_off"))).register();

    public static final BlockEntry<LecternControllerBlock> LECTERN_CONTROLLER = ((BlockBuilder) Create.REGISTRATE.block("lectern_controller", LecternControllerBlock::new).initialProperties(() -> Blocks.LECTERN).transform(TagGen.axeOnly())).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), p.models().getExistingFile(p.mcLoc("block/lectern")))).loot((lt, block) -> lt.m_246125_(block, Blocks.LECTERN)).register();

    public static final BlockEntry<BacktankBlock> COPPER_BACKTANK = ((BlockBuilder) Create.REGISTRATE.block("copper_backtank", BacktankBlock::new).initialProperties(SharedProperties::copperMetal).transform(BuilderTransformers.backtank(AllItems.COPPER_BACKTANK::get))).register();

    public static final BlockEntry<BacktankBlock> NETHERITE_BACKTANK = ((BlockBuilder) Create.REGISTRATE.block("netherite_backtank", BacktankBlock::new).initialProperties(SharedProperties::netheriteMetal).transform(BuilderTransformers.backtank(AllItems.NETHERITE_BACKTANK::get))).register();

    public static final BlockEntry<PeculiarBellBlock> PECULIAR_BELL = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("peculiar_bell", PeculiarBellBlock::new).properties(p -> p.mapColor(MapColor.GOLD).forceSolidOn()).transform(BuilderTransformers.bell())).onRegister(AllMovementBehaviours.movementBehaviour(new BellMovementBehaviour()))).register();

    public static final BlockEntry<HauntedBellBlock> HAUNTED_BELL = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("haunted_bell", HauntedBellBlock::new).properties(p -> p.mapColor(MapColor.SAND).forceSolidOn()).transform(BuilderTransformers.bell())).onRegister(AllMovementBehaviours.movementBehaviour(new HauntedBellMovementBehaviour()))).register();

    public static final DyedBlockList<ToolboxBlock> TOOLBOXES = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(colourName + "_toolbox", p -> new ToolboxBlock(p, colour)).initialProperties(SharedProperties::wooden).properties(p -> p.sound(SoundType.WOOD).mapColor(colour).forceSolidOn()).addLayer(() -> RenderType::m_110457_).loot((lt, block) -> {
            LootTable.Builder builder = LootTable.lootTable();
            LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
            lt.m_247577_(block, builder.withPool(LootPool.lootPool().when(survivesExplosion).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("UniqueId", "UniqueId")).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Inventory", "Inventory")))));
        }).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), p.models().withExistingParent(colourName + "_toolbox", p.modLoc("block/toolbox/block")).texture("0", p.modLoc("block/toolbox/" + colourName)))).onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.toolbox"))).tag(new TagKey[] { AllTags.AllBlockTags.TOOLBOXES.tag }).item(UncontainableBlockItem::new).model((c, p) -> ((ItemModelBuilder) p.withExistingParent(colourName + "_toolbox", p.modLoc("block/toolbox/item"))).texture("0", p.modLoc("block/toolbox/" + colourName))).tag(new TagKey[] { AllTags.AllItemTags.TOOLBOXES.tag }).build()).register();
    });

    public static final BlockEntry<ClipboardBlock> CLIPBOARD = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("clipboard", ClipboardBlock::new).initialProperties(SharedProperties::wooden).properties(p -> p.forceSolidOn()).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.AllBlockTags.SAFE_NBT.tag }).blockstate((c, p) -> p.horizontalFaceBlock((Block) c.get(), s -> AssetLookup.partialBaseModel(c, p, s.m_61143_(ClipboardBlock.WRITTEN) ? "written" : "empty"))).loot((lt, b) -> lt.m_247577_(b, BlockLootSubProvider.noDrop())).item(ClipboardBlockItem::new).onRegister(ClipboardBlockItem::registerModelOverrides)).model((c, p) -> ClipboardOverrides.addOverrideModels(c, p)).build()).register();

    public static final BlockEntry<MetalLadderBlock> ANDESITE_LADDER = ((BlockBuilder) Create.REGISTRATE.block("andesite_ladder", MetalLadderBlock::new).transform(BuilderTransformers.ladder("andesite", () -> DataIngredient.items((Item) AllItems.ANDESITE_ALLOY.get(), new Item[0]), MapColor.STONE))).register();

    public static final BlockEntry<MetalLadderBlock> BRASS_LADDER = ((BlockBuilder) Create.REGISTRATE.block("brass_ladder", MetalLadderBlock::new).transform(BuilderTransformers.ladder("brass", () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")), MapColor.TERRACOTTA_YELLOW))).register();

    public static final BlockEntry<MetalLadderBlock> COPPER_LADDER = ((BlockBuilder) Create.REGISTRATE.block("copper_ladder", MetalLadderBlock::new).transform(BuilderTransformers.ladder("copper", () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), MapColor.COLOR_ORANGE))).register();

    public static final BlockEntry<IronBarsBlock> ANDESITE_BARS = MetalBarsGen.createBars("andesite", true, () -> DataIngredient.items((Item) AllItems.ANDESITE_ALLOY.get(), new Item[0]), MapColor.STONE);

    public static final BlockEntry<IronBarsBlock> BRASS_BARS = MetalBarsGen.createBars("brass", true, () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")), MapColor.TERRACOTTA_YELLOW);

    public static final BlockEntry<IronBarsBlock> COPPER_BARS = MetalBarsGen.createBars("copper", true, () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), MapColor.COLOR_ORANGE);

    public static final BlockEntry<MetalScaffoldingBlock> ANDESITE_SCAFFOLD = ((BlockBuilder) Create.REGISTRATE.block("andesite_scaffolding", MetalScaffoldingBlock::new).transform(BuilderTransformers.scaffold("andesite", () -> DataIngredient.items((Item) AllItems.ANDESITE_ALLOY.get(), new Item[0]), MapColor.STONE, AllSpriteShifts.ANDESITE_SCAFFOLD, AllSpriteShifts.ANDESITE_SCAFFOLD_INSIDE, AllSpriteShifts.ANDESITE_CASING))).register();

    public static final BlockEntry<MetalScaffoldingBlock> BRASS_SCAFFOLD = ((BlockBuilder) Create.REGISTRATE.block("brass_scaffolding", MetalScaffoldingBlock::new).transform(BuilderTransformers.scaffold("brass", () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")), MapColor.TERRACOTTA_YELLOW, AllSpriteShifts.BRASS_SCAFFOLD, AllSpriteShifts.BRASS_SCAFFOLD_INSIDE, AllSpriteShifts.BRASS_CASING))).register();

    public static final BlockEntry<MetalScaffoldingBlock> COPPER_SCAFFOLD = ((BlockBuilder) Create.REGISTRATE.block("copper_scaffolding", MetalScaffoldingBlock::new).transform(BuilderTransformers.scaffold("copper", () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), MapColor.COLOR_ORANGE, AllSpriteShifts.COPPER_SCAFFOLD, AllSpriteShifts.COPPER_SCAFFOLD_INSIDE, AllSpriteShifts.COPPER_CASING))).register();

    public static final BlockEntry<GirderBlock> METAL_GIRDER = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("metal_girder", GirderBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).blockstate(GirderBlockStateGenerator::blockState).onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))).item().transform(ModelGen.customItemModel())).register();

    public static final BlockEntry<GirderEncasedShaftBlock> METAL_GIRDER_ENCASED_SHAFT = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("metal_girder_encased_shaft", GirderEncasedShaftBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK)).transform(TagGen.pickaxeOnly())).blockstate(GirderBlockStateGenerator::blockStateWithShaft).loot((p, b) -> p.m_247577_(b, p.m_247033_((ItemLike) METAL_GIRDER.get()).withPool((LootPool.Builder) p.m_247733_((ItemLike) SHAFT.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem((ItemLike) SHAFT.get())))))).onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))).register();

    public static final BlockEntry<Block> COPYCAT_BASE = ((BlockBuilder) Create.REGISTRATE.block("copycat_base", Block::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.GLOW_LICHEN)).addLayer(() -> RenderType::m_110457_).tag(new TagKey[] { AllTags.AllBlockTags.FAN_TRANSPARENT.tag }).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.simpleBlock((Block) c.get(), AssetLookup.partialBaseModel(c, p))).register();

    public static final BlockEntry<CopycatStepBlock> COPYCAT_STEP = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("copycat_step", CopycatStepBlock::new).properties(p -> p.forceSolidOn()).transform(BuilderTransformers.copycat())).onRegister(CreateRegistrate.blockModel(() -> CopycatStepModel::new))).item().recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/zinc")), RecipeCategory.BUILDING_BLOCKS, c::get, 4)).transform(ModelGen.customItemModel("copycat_base", "step"))).register();

    public static final BlockEntry<CopycatPanelBlock> COPYCAT_PANEL = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("copycat_panel", CopycatPanelBlock::new).transform(BuilderTransformers.copycat())).onRegister(CreateRegistrate.blockModel(() -> CopycatPanelModel::new))).item().recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/zinc")), RecipeCategory.BUILDING_BLOCKS, c::get, 4)).transform(ModelGen.customItemModel("copycat_base", "panel"))).register();

    public static final BlockEntry<WrenchableDirectionalBlock> COPYCAT_BARS = ((BlockBuilder) Create.REGISTRATE.block("copycat_bars", WrenchableDirectionalBlock::new).blockstate(new SpecialCopycatPanelBlockState("bars")::generate).onRegister(CreateRegistrate.blockModel(() -> CopycatBarsModel::new))).register();

    public static final DyedBlockList<SeatBlock> SEATS = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        SeatMovementBehaviour movementBehaviour = new SeatMovementBehaviour();
        SeatInteractionBehaviour interactionBehaviour = new SeatInteractionBehaviour();
        return ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block(colourName + "_seat", p -> new SeatBlock(p, colour)).initialProperties(SharedProperties::wooden).properties(p -> p.mapColor(colour)).transform(TagGen.axeOnly())).onRegister(AllMovementBehaviours.movementBehaviour(movementBehaviour))).onRegister(AllInteractionBehaviours.interactionBehaviour(interactionBehaviour))).onRegister(AllDisplayBehaviours.assignDataBehaviour(new EntityNameDisplaySource(), "entity_name"))).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().withExistingParent(colourName + "_seat", p.modLoc("block/seat")).texture("1", p.modLoc("block/seat/top_" + colourName)).texture("2", p.modLoc("block/seat/side_" + colourName)))).recipe((c, p) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike) c.get()).requires(DyeHelper.getWoolOfDye(colour)).requires(ItemTags.WOODEN_SLABS).unlockedBy("has_wool", RegistrateRecipeProvider.m_206406_(ItemTags.WOOL)).save(p, Create.asResource("crafting/kinetics/" + c.getName()));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike) c.get()).requires(colour.getTag()).requires(AllTags.AllItemTags.SEATS.tag).unlockedBy("has_seat", RegistrateRecipeProvider.m_206406_(AllTags.AllItemTags.SEATS.tag)).save(p, Create.asResource("crafting/kinetics/" + c.getName() + "_from_other_seat"));
        }).onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.seat"))).tag(new TagKey[] { AllTags.AllBlockTags.SEATS.tag }).item().tag(new TagKey[] { AllTags.AllItemTags.SEATS.tag }).build()).register();
    });

    public static final BlockEntry<SlidingDoorBlock> ANDESITE_DOOR = ((BlockBuilder) Create.REGISTRATE.block("andesite_door", p -> SlidingDoorBlock.metal(p, true)).transform(BuilderTransformers.slidingDoor("andesite"))).properties(p -> p.mapColor(MapColor.STONE).sound(SoundType.STONE).noOcclusion()).register();

    public static final BlockEntry<SlidingDoorBlock> BRASS_DOOR = ((BlockBuilder) Create.REGISTRATE.block("brass_door", p -> SlidingDoorBlock.metal(p, false)).transform(BuilderTransformers.slidingDoor("brass"))).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW).sound(SoundType.STONE).noOcclusion()).register();

    public static final BlockEntry<SlidingDoorBlock> COPPER_DOOR = ((BlockBuilder) Create.REGISTRATE.block("copper_door", p -> SlidingDoorBlock.metal(p, true)).transform(BuilderTransformers.slidingDoor("copper"))).properties(p -> p.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.STONE).noOcclusion()).register();

    public static final BlockEntry<SlidingDoorBlock> TRAIN_DOOR = ((BlockBuilder) Create.REGISTRATE.block("train_door", p -> SlidingDoorBlock.metal(p, false)).transform(BuilderTransformers.slidingDoor("train"))).properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.NETHERITE_BLOCK).noOcclusion()).register();

    public static final BlockEntry<TrainTrapdoorBlock> TRAIN_TRAPDOOR = ((BlockBuilder) Create.REGISTRATE.block("train_trapdoor", TrainTrapdoorBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.NETHERITE_BLOCK)).transform(BuilderTransformers.trapdoor(true))).register();

    public static final BlockEntry<SlidingDoorBlock> FRAMED_GLASS_DOOR = ((BlockBuilder) Create.REGISTRATE.block("framed_glass_door", p -> SlidingDoorBlock.glass(p, false)).transform(BuilderTransformers.slidingDoor("glass"))).properties(p -> p.mapColor(MapColor.NONE).sound(SoundType.GLASS).noOcclusion()).register();

    public static final BlockEntry<TrainTrapdoorBlock> FRAMED_GLASS_TRAPDOOR = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("framed_glass_trapdoor", TrainTrapdoorBlock::new).initialProperties(SharedProperties::softMetal).transform(BuilderTransformers.trapdoor(false))).properties(p -> p.mapColor(MapColor.NONE).sound(SoundType.GLASS).noOcclusion()).onRegister(CreateRegistrate.connectedTextures(TrapdoorCTBehaviour::new))).addLayer(() -> RenderType::m_110457_).register();

    public static final BlockEntry<Block> ZINC_ORE = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("zinc_ore", Block::new).initialProperties(() -> Blocks.GOLD_ORE).properties(p -> p.mapColor(MapColor.METAL).requiresCorrectToolForDrops().sound(SoundType.STONE)).transform(TagGen.pickaxeOnly())).loot((lt, b) -> lt.m_247577_(b, RegistrateBlockLootTables.m_247502_(b, (LootPoolEntryContainer.Builder) lt.m_246108_(b, LootItem.lootTableItem((ItemLike) AllItems.RAW_ZINC.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))).tag(new TagKey[] { BlockTags.NEEDS_IRON_TOOL }).tag(new TagKey[] { Tags.Blocks.ORES }).transform(TagGen.tagBlockAndItem("ores/zinc", "ores_in_ground/stone"))).tag(new TagKey[] { Tags.Items.ORES }).build()).register();

    public static final BlockEntry<Block> DEEPSLATE_ZINC_ORE = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("deepslate_zinc_ore", Block::new).initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE).properties(p -> p.mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)).transform(TagGen.pickaxeOnly())).loot((lt, b) -> lt.m_247577_(b, RegistrateBlockLootTables.m_247502_(b, (LootPoolEntryContainer.Builder) lt.m_246108_(b, LootItem.lootTableItem((ItemLike) AllItems.RAW_ZINC.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))).tag(new TagKey[] { BlockTags.NEEDS_IRON_TOOL }).tag(new TagKey[] { Tags.Blocks.ORES }).transform(TagGen.tagBlockAndItem("ores/zinc", "ores_in_ground/deepslate"))).tag(new TagKey[] { Tags.Items.ORES }).build()).register();

    public static final BlockEntry<Block> RAW_ZINC_BLOCK = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("raw_zinc_block", Block::new).initialProperties(() -> Blocks.RAW_GOLD_BLOCK).properties(p -> p.mapColor(MapColor.GLOW_LICHEN).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).tag(new TagKey[] { BlockTags.NEEDS_IRON_TOOL }).lang("Block of Raw Zinc").transform(TagGen.tagBlockAndItem("storage_blocks/raw_zinc"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<Block> ZINC_BLOCK = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("zinc_block", Block::new).initialProperties(() -> Blocks.IRON_BLOCK).properties(p -> p.mapColor(MapColor.GLOW_LICHEN).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { BlockTags.NEEDS_IRON_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).tag(new TagKey[] { BlockTags.BEACON_BASE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/zinc"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).lang("Block of Zinc").register();

    public static final BlockEntry<Block> ANDESITE_ALLOY_BLOCK = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("andesite_alloy_block", Block::new).initialProperties(() -> Blocks.ANDESITE).properties(p -> p.mapColor(MapColor.STONE).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.simpleCubeAll("andesite_block")).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/andesite_alloy"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).lang("Block of Andesite Alloy").register();

    public static final BlockEntry<Block> INDUSTRIAL_IRON_BLOCK = ((BlockBuilder) Create.REGISTRATE.block("industrial_iron_block", Block::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().cubeColumn(c.getName(), p.modLoc("block/industrial_iron_block"), p.modLoc("block/industrial_iron_block_top")))).tag(new TagKey[] { AllTags.AllBlockTags.WRENCH_PICKUP.tag }).lang("Block of Industrial Iron").recipe((c, p) -> p.stonecutting(DataIngredient.tag(Tags.Items.INGOTS_IRON), RecipeCategory.BUILDING_BLOCKS, c::get, 2)).simpleItem().register();

    public static final BlockEntry<Block> BRASS_BLOCK = ((BlockBuilder) ((ItemBuilder) ((BlockBuilder) Create.REGISTRATE.block("brass_block", Block::new).initialProperties(() -> Blocks.IRON_BLOCK).properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.simpleCubeAll("brass_block")).tag(new TagKey[] { BlockTags.NEEDS_IRON_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).tag(new TagKey[] { BlockTags.BEACON_BASE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/brass"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).lang("Block of Brass").register();

    public static final BlockEntry<ExperienceBlock> EXPERIENCE_BLOCK = ((BlockBuilder) ((BlockBuilder) Create.REGISTRATE.block("experience_block", ExperienceBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.PLANT).sound(new ForgeSoundType(1.0F, 0.5F, () -> SoundEvents.AMETHYST_BLOCK_BREAK, () -> SoundEvents.AMETHYST_BLOCK_STEP, () -> SoundEvents.AMETHYST_BLOCK_PLACE, () -> SoundEvents.AMETHYST_BLOCK_HIT, () -> SoundEvents.AMETHYST_BLOCK_FALL)).requiresCorrectToolForDrops().lightLevel(s -> 15)).blockstate((c, p) -> p.simpleBlock((Block) c.get(), AssetLookup.standardModel(c, p))).transform(TagGen.pickaxeOnly())).lang("Block of Experience").tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).tag(new TagKey[] { BlockTags.BEACON_BASE_BLOCKS }).item().properties(p -> p.rarity(Rarity.UNCOMMON)).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<RotatedPillarBlock> ROSE_QUARTZ_BLOCK = ((BlockBuilder) Create.REGISTRATE.block("rose_quartz_block", RotatedPillarBlock::new).initialProperties(() -> Blocks.AMETHYST_BLOCK).properties(p -> p.mapColor(MapColor.TERRACOTTA_PINK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.axisBlock((RotatedPillarBlock) c.get(), p.modLoc("block/palettes/rose_quartz_side"), p.modLoc("block/palettes/rose_quartz_top"))).recipe((c, p) -> p.stonecutting(DataIngredient.items((Item) AllItems.ROSE_QUARTZ.get(), new Item[0]), RecipeCategory.BUILDING_BLOCKS, c::get, 2)).simpleItem().lang("Block of Rose Quartz").register();

    public static final BlockEntry<Block> ROSE_QUARTZ_TILES = ((BlockBuilder) Create.REGISTRATE.block("rose_quartz_tiles", Block::new).initialProperties(() -> Blocks.DEEPSLATE).properties(p -> p.mapColor(MapColor.TERRACOTTA_PINK).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.simpleCubeAll("palettes/rose_quartz_tiles")).recipe((c, p) -> p.stonecutting(DataIngredient.items((Item) AllItems.POLISHED_ROSE_QUARTZ.get(), new Item[0]), RecipeCategory.BUILDING_BLOCKS, c::get, 2)).simpleItem().register();

    public static final BlockEntry<Block> SMALL_ROSE_QUARTZ_TILES = ((BlockBuilder) Create.REGISTRATE.block("small_rose_quartz_tiles", Block::new).initialProperties(() -> Blocks.DEEPSLATE).properties(p -> p.mapColor(MapColor.TERRACOTTA_PINK).requiresCorrectToolForDrops()).transform(TagGen.pickaxeOnly())).blockstate(BlockStateGen.simpleCubeAll("palettes/small_rose_quartz_tiles")).recipe((c, p) -> p.stonecutting(DataIngredient.items((Item) AllItems.POLISHED_ROSE_QUARTZ.get(), new Item[0]), RecipeCategory.BUILDING_BLOCKS, c::get, 2)).simpleItem().register();

    public static final CopperBlockSet COPPER_SHINGLES = new CopperBlockSet(Create.REGISTRATE, "copper_shingles", "copper_roof_top", CopperBlockSet.DEFAULT_VARIANTS, (NonNullBiConsumer<DataGenContext<Block, ?>, RegistrateRecipeProvider>) ((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), RecipeCategory.BUILDING_BLOCKS, c::get, 2)));

    public static final CopperBlockSet COPPER_TILES = new CopperBlockSet(Create.REGISTRATE, "copper_tiles", "copper_roof_top", CopperBlockSet.DEFAULT_VARIANTS, (NonNullBiConsumer<DataGenContext<Block, ?>, RegistrateRecipeProvider>) ((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), RecipeCategory.BUILDING_BLOCKS, c::get, 2)));

    public static void register() {
    }

    static {
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.PALETTES_CREATIVE_TAB);
    }
}