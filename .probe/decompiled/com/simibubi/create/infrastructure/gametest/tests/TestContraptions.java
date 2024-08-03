package com.simibubi.create.infrastructure.gametest.tests;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.contraptions.elevator.ElevatorPulleyBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlock;
import com.simibubi.create.infrastructure.gametest.CreateGameTestHelper;
import com.simibubi.create.infrastructure.gametest.GameTestGroup;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fluids.FluidStack;

@GameTestGroup(path = "contraptions")
public class TestContraptions {

    @GameTest(template = "arrow_dispenser", timeoutTicks = 200)
    public static void arrowDispenser(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(2, 3, 1);
        helper.m_177421_(lever);
        BlockPos pos1 = new BlockPos(0, 5, 0);
        BlockPos pos2 = new BlockPos(4, 5, 4);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(7);
            List<Arrow> arrows = helper.getEntitiesBetween(EntityType.ARROW, pos1, pos2);
            if (arrows.size() != 4) {
                helper.fail("Expected 4 arrows");
            }
            helper.powerLever(lever);
            BlockPos dispenser = new BlockPos(2, 5, 2);
            helper.assertContainerContains(dispenser, Items.ARROW);
        });
    }

    @GameTest(template = "crop_farming", timeoutTicks = 200)
    public static void cropFarming(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(4, 3, 1);
        helper.m_177421_(lever);
        BlockPos output = new BlockPos(1, 3, 12);
        helper.m_177361_(() -> helper.assertAnyContained(output, Items.WHEAT, Items.POTATO, Items.CARROT));
    }

    @GameTest(template = "mounted_item_extract", timeoutTicks = 400)
    public static void mountedItemExtract(CreateGameTestHelper helper) {
        BlockPos barrel = new BlockPos(1, 3, 2);
        Object2LongMap<Item> content = helper.getItemContent(barrel);
        BlockPos lever = new BlockPos(1, 5, 1);
        helper.m_177421_(lever);
        BlockPos outputPos = new BlockPos(4, 2, 1);
        helper.m_177361_(() -> {
            helper.assertContentPresent(content, outputPos);
            helper.powerLever(lever);
            helper.assertContainerEmpty(barrel);
        });
    }

    @GameTest(template = "mounted_fluid_drain", timeoutTicks = 200)
    public static void mountedFluidDrain(CreateGameTestHelper helper) {
        BlockPos tank = new BlockPos(1, 3, 2);
        FluidStack fluid = helper.getTankContents(tank);
        if (fluid.isEmpty()) {
            helper.fail("Tank empty");
        }
        BlockPos lever = new BlockPos(1, 5, 1);
        helper.m_177421_(lever);
        BlockPos output = new BlockPos(4, 2, 1);
        helper.m_177361_(() -> {
            helper.assertFluidPresent(fluid, output);
            helper.powerLever(lever);
            helper.assertTankEmpty(tank);
        });
    }

    @GameTest(template = "ploughing")
    public static void ploughing(CreateGameTestHelper helper) {
        BlockPos dirt = new BlockPos(4, 2, 1);
        BlockPos lever = new BlockPos(3, 3, 2);
        helper.m_177421_(lever);
        helper.m_177361_(() -> helper.m_177208_(Blocks.FARMLAND, dirt));
    }

    @GameTest(template = "redstone_contacts")
    public static void redstoneContacts(CreateGameTestHelper helper) {
        BlockPos end = new BlockPos(5, 10, 1);
        BlockPos lever = new BlockPos(1, 3, 2);
        helper.m_177421_(lever);
        helper.m_177361_(() -> helper.m_177208_(Blocks.DIAMOND_BLOCK, end));
    }

    @GameTest(template = "controls", timeoutTicks = 200)
    public static void controls(CreateGameTestHelper helper) {
        BlockPos button = new BlockPos(5, 5, 4);
        BlockPos gearshift = new BlockPos(4, 5, 4);
        BlockPos bearingPos = new BlockPos(4, 4, 4);
        AtomicInteger step = new AtomicInteger(1);
        List<BlockPos> dirt = List.of(new BlockPos(4, 2, 6), new BlockPos(2, 2, 4), new BlockPos(4, 2, 2));
        List<BlockPos> wheat = List.of(new BlockPos(4, 3, 7), new BlockPos(1, 3, 4), new BlockPos(4, 3, 1));
        helper.m_177385_(button);
        helper.m_177361_(() -> {
            helper.m_177255_(gearshift, SequencedGearshiftBlock.STATE, 0);
            if (step.get() != 4) {
                MechanicalBearingBlockEntity bearing = helper.getBlockEntity((BlockEntityType<MechanicalBearingBlockEntity>) AllBlockEntityTypes.MECHANICAL_BEARING.get(), bearingPos);
                if (bearing.getMovedContraption() == null) {
                    helper.fail("Contraption not assembled");
                }
                Contraption contraption = bearing.getMovedContraption().getContraption();
                switch(step.get()) {
                    case 1:
                        helper.m_177208_(Blocks.FARMLAND, (BlockPos) dirt.get(0));
                        helper.m_177255_((BlockPos) wheat.get(0), CropBlock.AGE, 0);
                        helper.toggleActorsOfType(contraption, (ItemLike) AllBlocks.MECHANICAL_HARVESTER.get());
                        helper.m_177385_(button);
                        step.incrementAndGet();
                        helper.fail("Entering step 2");
                        break;
                    case 2:
                        helper.m_177208_(Blocks.FARMLAND, (BlockPos) dirt.get(1));
                        helper.m_177255_((BlockPos) wheat.get(1), CropBlock.AGE, 7);
                        helper.toggleActorsOfType(contraption, (ItemLike) AllBlocks.MECHANICAL_PLOUGH.get());
                        helper.m_177385_(button);
                        step.incrementAndGet();
                        helper.fail("Entering step 3");
                        break;
                    case 3:
                        helper.m_177208_(Blocks.DIRT, (BlockPos) dirt.get(2));
                        helper.m_177255_((BlockPos) wheat.get(2), CropBlock.AGE, 7);
                        helper.m_177385_(button);
                        step.incrementAndGet();
                        helper.fail("Entering step 4");
                }
            }
        });
    }

    @GameTest(template = "elevator")
    public static void elevator(CreateGameTestHelper helper) {
        BlockPos pulley = new BlockPos(5, 12, 3);
        BlockPos secondaryPulley = new BlockPos(5, 12, 1);
        BlockPos bottomLamp = new BlockPos(2, 3, 2);
        BlockPos topLamp = new BlockPos(2, 12, 2);
        BlockPos lever = new BlockPos(1, 11, 2);
        BlockPos elevatorStart = new BlockPos(4, 2, 2);
        BlockPos cowSpawn = new BlockPos(4, 4, 2);
        BlockPos cowEnd = new BlockPos(4, 13, 2);
        helper.m_177127_(1L, () -> helper.m_177176_(EntityType.COW, cowSpawn));
        helper.m_177127_(15L, () -> helper.<ElevatorPulleyBlockEntity>getBlockEntity((BlockEntityType<ElevatorPulleyBlockEntity>) AllBlockEntityTypes.ELEVATOR_PULLEY.get(), pulley).clicked());
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(1);
            if (!(Boolean) helper.m_177232_(lever).m_61143_(LeverBlock.POWERED)) {
                helper.getFirstEntity((EntityType) AllEntityTypes.CONTROLLED_CONTRAPTION.get(), elevatorStart);
                helper.m_177255_(topLamp, RedstoneLampBlock.LIT, false);
                helper.m_177255_(bottomLamp, RedstoneLampBlock.LIT, true);
                ElevatorPulleyBlockEntity secondary = helper.getBlockEntity((BlockEntityType<ElevatorPulleyBlockEntity>) AllBlockEntityTypes.ELEVATOR_PULLEY.get(), secondaryPulley);
                if (secondary.getMirrorParent() == null) {
                    helper.fail("Secondary pulley has no parent");
                }
                helper.m_177421_(lever);
                helper.fail("Entering step 2");
            } else {
                helper.m_177255_(topLamp, RedstoneLampBlock.LIT, true);
                helper.m_177255_(bottomLamp, RedstoneLampBlock.LIT, false);
                helper.m_177374_(EntityType.COW, cowEnd);
                helper.<ElevatorPulleyBlockEntity>getBlockEntity((BlockEntityType<ElevatorPulleyBlockEntity>) AllBlockEntityTypes.ELEVATOR_PULLEY.get(), pulley).clicked();
            }
        });
    }

    @GameTest(template = "roller_filling")
    public static void rollerFilling(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(7, 6, 1);
        BlockPos barrelEnd = new BlockPos(2, 5, 2);
        List<BlockPos> existing = BlockPos.betweenClosedStream(new BlockPos(1, 3, 2), new BlockPos(4, 2, 2)).toList();
        List<BlockPos> filled = BlockPos.betweenClosedStream(new BlockPos(1, 2, 1), new BlockPos(4, 3, 3)).filter(pos -> !existing.contains(pos)).toList();
        List<BlockPos> tracks = BlockPos.betweenClosedStream(new BlockPos(1, 4, 2), new BlockPos(4, 4, 2)).toList();
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(4);
            existing.forEach(pos -> helper.m_177208_((Block) AllBlocks.RAILWAY_CASING.get(), pos));
            filled.forEach(pos -> helper.m_177208_((Block) AllBlocks.ANDESITE_CASING.get(), pos));
            tracks.forEach(pos -> helper.m_177208_((Block) AllBlocks.TRACK.get(), pos));
            helper.assertContainerEmpty(barrelEnd);
        });
    }

    @GameTest(template = "roller_paving_and_clearing", timeoutTicks = 200)
    public static void rollerPavingAndClearing(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(8, 5, 1);
        List<BlockPos> paved = BlockPos.betweenClosedStream(new BlockPos(1, 2, 1), new BlockPos(4, 2, 1)).toList();
        BlockPos cleared = new BlockPos(2, 3, 1);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(9);
            paved.forEach(pos -> helper.m_177208_((Block) AllBlocks.ANDESITE_CASING.get(), pos));
            helper.m_177208_(Blocks.AIR, cleared);
        });
    }
}