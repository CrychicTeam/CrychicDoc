package com.simibubi.create.infrastructure.gametest.tests;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyFluidHandler;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import com.simibubi.create.infrastructure.gametest.CreateGameTestHelper;
import com.simibubi.create.infrastructure.gametest.GameTestGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

@GameTestGroup(path = "fluids")
public class TestFluids {

    @GameTest(template = "hose_pulley_transfer", timeoutTicks = 400)
    public static void hosePulleyTransfer(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(7, 7, 5);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(15);
            BlockPos filledLowerCorner = new BlockPos(2, 3, 2);
            BlockPos filledUpperCorner = new BlockPos(4, 5, 4);
            BlockPos.betweenClosed(filledLowerCorner, filledUpperCorner).forEach(pos -> helper.m_177208_(Blocks.WATER, pos));
            BlockPos emptiedLowerCorner = new BlockPos(8, 3, 2);
            BlockPos emptiedUpperCorner = new BlockPos(10, 5, 4);
            BlockPos.betweenClosed(emptiedLowerCorner, emptiedUpperCorner).forEach(pos -> helper.m_177208_(Blocks.AIR, pos));
            BlockPos pulleyPos = new BlockPos(4, 7, 3);
            if (helper.fluidStorageAt(pulleyPos) instanceof HosePulleyFluidHandler hose) {
                IFluidHandler internalTank = hose.getInternalTank();
                if (!internalTank.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    helper.fail("Pulley not empty");
                }
            } else {
                helper.fail("Not a pulley");
            }
        });
    }

    @GameTest(template = "in_world_pumping_out")
    public static void inWorldPumpingOut(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(4, 3, 3);
        BlockPos basin = new BlockPos(5, 2, 2);
        BlockPos output = new BlockPos(2, 2, 2);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.m_177208_(Blocks.WATER, output);
            helper.assertTankEmpty(basin);
        });
    }

    @GameTest(template = "in_world_pumping_in")
    public static void inWorldPumpingIn(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(4, 3, 3);
        BlockPos basin = new BlockPos(5, 2, 2);
        BlockPos water = new BlockPos(2, 2, 2);
        FluidStack expectedResult = new FluidStack(Fluids.WATER, 1000);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.m_177208_(Blocks.AIR, water);
            helper.assertFluidPresent(expectedResult, basin);
        });
    }

    @GameTest(template = "steam_engine")
    public static void steamEngine(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(4, 3, 3);
        helper.m_177421_(lever);
        BlockPos stressometer = new BlockPos(5, 2, 5);
        BlockPos speedometer = new BlockPos(4, 2, 5);
        helper.m_177361_(() -> {
            StressGaugeBlockEntity stress = helper.getBlockEntity((BlockEntityType<StressGaugeBlockEntity>) AllBlockEntityTypes.STRESSOMETER.get(), stressometer);
            SpeedGaugeBlockEntity speed = helper.getBlockEntity((BlockEntityType<SpeedGaugeBlockEntity>) AllBlockEntityTypes.SPEEDOMETER.get(), speedometer);
            float capacity = stress.getNetworkCapacity();
            helper.assertCloseEnoughTo((double) capacity, 2048.0);
            float rotationSpeed = Mth.abs(speed.getSpeed());
            helper.assertCloseEnoughTo((double) rotationSpeed, 16.0);
        });
    }

    @GameTest(template = "3_pipe_combine", timeoutTicks = 400)
    public static void threePipeCombine(CreateGameTestHelper helper) {
        BlockPos tank1Pos = new BlockPos(5, 2, 1);
        BlockPos tank2Pos = tank1Pos.south();
        BlockPos tank3Pos = tank2Pos.south();
        long initialContents = helper.getFluidInTanks(tank1Pos, tank2Pos, tank3Pos);
        BlockPos pumpPos = new BlockPos(2, 2, 2);
        helper.flipBlock(pumpPos);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(13);
            helper.assertTanksEmpty(tank1Pos, tank2Pos, tank3Pos);
            BlockPos outputTankPos = new BlockPos(1, 2, 2);
            long moved = helper.getFluidInTanks(outputTankPos);
            if (moved != initialContents) {
                helper.fail("Wrong amount of fluid amount. expected [%s], got [%s]".formatted(initialContents, moved));
            }
        });
    }

    @GameTest(template = "3_pipe_split", timeoutTicks = 200)
    public static void threePipeSplit(CreateGameTestHelper helper) {
        BlockPos pumpPos = new BlockPos(2, 2, 2);
        BlockPos tank1Pos = new BlockPos(5, 2, 1);
        BlockPos tank2Pos = tank1Pos.south();
        BlockPos tank3Pos = tank2Pos.south();
        BlockPos outputTankPos = new BlockPos(1, 2, 2);
        long totalContents = helper.getFluidInTanks(tank1Pos, tank2Pos, tank3Pos, outputTankPos);
        helper.flipBlock(pumpPos);
        helper.m_177361_(() -> {
            helper.assertSecondsPassed(7);
            FluidStack contents = helper.getTankContents(outputTankPos);
            if (!contents.isEmpty()) {
                helper.fail("Tank not empty: " + contents.getAmount());
            }
            long newTotalContents = helper.getFluidInTanks(tank1Pos, tank2Pos, tank3Pos);
            if (newTotalContents != totalContents) {
                helper.fail("Wrong total fluid amount. expected [%s], got [%s]".formatted(totalContents, newTotalContents));
            }
        });
    }

    @GameTest(template = "large_waterwheel", timeoutTicks = 200)
    public static void largeWaterwheel(CreateGameTestHelper helper) {
        BlockPos wheel = new BlockPos(4, 3, 2);
        BlockPos leftEnd = new BlockPos(6, 2, 2);
        BlockPos rightEnd = new BlockPos(2, 2, 2);
        List<BlockPos> edges = List.of(new BlockPos(4, 5, 1), new BlockPos(4, 5, 3));
        BlockPos openLever = new BlockPos(3, 8, 1);
        BlockPos leftLever = new BlockPos(5, 7, 1);
        waterwheel(helper, wheel, 4.0F, 512.0F, leftEnd, rightEnd, edges, openLever, leftLever);
    }

    @GameTest(template = "small_waterwheel", timeoutTicks = 200)
    public static void smallWaterwheel(CreateGameTestHelper helper) {
        BlockPos wheel = new BlockPos(3, 2, 2);
        BlockPos leftEnd = new BlockPos(4, 2, 2);
        BlockPos rightEnd = new BlockPos(2, 2, 2);
        List<BlockPos> edges = List.of(new BlockPos(3, 3, 1), new BlockPos(3, 3, 3));
        BlockPos openLever = new BlockPos(2, 6, 1);
        BlockPos leftLever = new BlockPos(4, 5, 1);
        waterwheel(helper, wheel, 8.0F, 256.0F, leftEnd, rightEnd, edges, openLever, leftLever);
    }

    private static void waterwheel(CreateGameTestHelper helper, BlockPos wheel, float expectedRpm, float expectedSU, BlockPos leftEnd, BlockPos rightEnd, List<BlockPos> edges, BlockPos openLever, BlockPos leftLever) {
        BlockPos speedometer = wheel.north();
        BlockPos stressometer = wheel.south();
        helper.m_177421_(openLever);
        helper.m_177361_(() -> {
            edges.forEach(pos -> helper.m_177341_(Blocks.WATER, pos));
            helper.m_177208_(Blocks.WATER, rightEnd);
            if (!(Boolean) helper.m_177232_(leftLever).m_61143_(LeverBlock.POWERED)) {
                helper.m_177208_(Blocks.WATER, leftEnd);
                helper.assertSpeedometerSpeed(speedometer, 0.0F);
                helper.assertStressometerCapacity(stressometer, 0.0F);
                helper.powerLever(leftLever);
                helper.fail("Entering step 2");
            } else {
                helper.m_177341_(Blocks.WATER, leftEnd);
                helper.assertSpeedometerSpeed(speedometer, expectedRpm);
                helper.assertStressometerCapacity(stressometer, expectedSU);
            }
        });
    }

    @GameTest(template = "waterwheel_materials", timeoutTicks = 300)
    public static void waterwheelMaterials(CreateGameTestHelper helper) {
        List<Item> planks = (List<Item>) ForgeRegistries.BLOCKS.tags().getTag(BlockTags.PLANKS).stream().map(ItemLike::m_5456_).collect(Collectors.toCollection(ArrayList::new));
        List<BlockPos> chests = List.of(new BlockPos(6, 4, 2), new BlockPos(6, 4, 3));
        List<BlockPos> deployers = chests.stream().map(pos -> pos.below(2)).toList();
        helper.m_177306_(3L, () -> chests.forEach(chest -> planks.forEach(plank -> ItemHandlerHelper.insertItem(helper.itemStorageAt(chest), new ItemStack(plank), false))));
        BlockPos smallWheel = new BlockPos(4, 2, 2);
        BlockPos largeWheel = new BlockPos(3, 3, 3);
        BlockPos lever = new BlockPos(5, 3, 1);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            Item plank = (Item) planks.get(0);
            if (plank instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                WaterWheelBlockEntity smallWheelBe = helper.getBlockEntity((BlockEntityType<WaterWheelBlockEntity>) AllBlockEntityTypes.WATER_WHEEL.get(), smallWheel);
                if (!smallWheelBe.material.m_60713_(block)) {
                    helper.fail("Small waterwheel has not consumed " + ForgeRegistries.ITEMS.getKey(plank));
                }
                WaterWheelBlockEntity largeWheelBe = helper.getBlockEntity((BlockEntityType<WaterWheelBlockEntity>) AllBlockEntityTypes.LARGE_WATER_WHEEL.get(), largeWheel);
                if (!largeWheelBe.material.m_60713_(block)) {
                    helper.fail("Large waterwheel has not consumed " + ForgeRegistries.ITEMS.getKey(plank));
                }
                planks.remove(0);
                deployers.forEach(pos -> {
                    IItemHandler handler = helper.itemStorageAt(pos);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        handler.extractItem(i, Integer.MAX_VALUE, false);
                    }
                });
                if (!planks.isEmpty()) {
                    helper.fail("Not all planks have been consumed");
                }
            } else {
                throw new GameTestAssertException(ForgeRegistries.ITEMS.getKey(plank) + " is not a BlockItem");
            }
        });
    }

    @GameTest(template = "smart_observer_pipes")
    public static void smartObserverPipes(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(3, 3, 1);
        BlockPos output = new BlockPos(3, 4, 4);
        BlockPos tankOutput = new BlockPos(1, 2, 4);
        FluidStack expected = new FluidStack(Fluids.WATER, 2000);
        helper.m_177421_(lever);
        helper.m_177361_(() -> {
            helper.assertFluidPresent(expected, tankOutput);
            helper.m_177208_(Blocks.DIAMOND_BLOCK, output);
        });
    }

    @GameTest(template = "threshold_switch", timeoutTicks = 400)
    public static void thresholdSwitch(CreateGameTestHelper helper) {
        BlockPos leftHandle = new BlockPos(4, 2, 4);
        BlockPos leftValve = new BlockPos(4, 2, 3);
        BlockPos leftTank = new BlockPos(5, 2, 3);
        BlockPos rightHandle = new BlockPos(2, 2, 4);
        BlockPos rightValve = new BlockPos(2, 2, 3);
        BlockPos rightTank = new BlockPos(1, 2, 3);
        BlockPos drainHandle = new BlockPos(3, 3, 2);
        BlockPos drainValve = new BlockPos(3, 3, 1);
        BlockPos lamp = new BlockPos(1, 3, 1);
        BlockPos tank = new BlockPos(2, 2, 1);
        helper.m_177361_(() -> {
            if (!(Boolean) helper.m_177232_(leftValve).m_61143_(FluidValveBlock.ENABLED)) {
                helper.<ValveHandleBlockEntity>getBlockEntity((BlockEntityType<ValveHandleBlockEntity>) AllBlockEntityTypes.VALVE_HANDLE.get(), leftHandle).activate(false);
                helper.fail("Entering step 2");
            } else if (!(Boolean) helper.m_177232_(rightValve).m_61143_(FluidValveBlock.ENABLED)) {
                helper.assertFluidPresent(FluidStack.EMPTY, leftTank);
                helper.m_177255_(lamp, RedstoneLampBlock.LIT, false);
                helper.<ValveHandleBlockEntity>getBlockEntity((BlockEntityType<ValveHandleBlockEntity>) AllBlockEntityTypes.VALVE_HANDLE.get(), rightHandle).activate(false);
                helper.fail("Entering step 3");
            } else if (!(Boolean) helper.m_177232_(drainValve).m_61143_(FluidValveBlock.ENABLED)) {
                helper.assertFluidPresent(FluidStack.EMPTY, rightTank);
                helper.m_177255_(lamp, RedstoneLampBlock.LIT, true);
                helper.<ValveHandleBlockEntity>getBlockEntity((BlockEntityType<ValveHandleBlockEntity>) AllBlockEntityTypes.VALVE_HANDLE.get(), drainHandle).activate(false);
                helper.fail("Entering step 4");
            } else {
                helper.assertTankEmpty(tank);
                helper.m_177255_(lamp, RedstoneLampBlock.LIT, false);
            }
        });
    }
}