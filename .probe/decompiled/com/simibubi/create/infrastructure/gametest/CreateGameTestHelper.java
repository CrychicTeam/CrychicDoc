package com.simibubi.create.infrastructure.gametest;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovement;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlockEntity;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.mixin.accessor.GameTestHelperAccessor;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CreateGameTestHelper extends GameTestHelper {

    public static final int TICKS_PER_SECOND = 20;

    public static final int TEN_SECONDS = 200;

    public static final int FIFTEEN_SECONDS = 300;

    public static final int TWENTY_SECONDS = 400;

    private CreateGameTestHelper(GameTestInfo testInfo) {
        super(testInfo);
    }

    public static CreateGameTestHelper of(GameTestHelper original) {
        GameTestHelperAccessor access = (GameTestHelperAccessor) original;
        CreateGameTestHelper helper = new CreateGameTestHelper(access.getTestInfo());
        GameTestHelperAccessor newAccess = (GameTestHelperAccessor) helper;
        newAccess.setFinalCheckAdded(access.getFinalCheckAdded());
        return helper;
    }

    public void flipBlock(BlockPos pos) {
        BlockState original = this.m_177232_(pos);
        if (!original.m_61138_(BlockStateProperties.FACING)) {
            this.fail("FACING property not in block: " + ForgeRegistries.BLOCKS.getKey(original.m_60734_()));
        }
        Direction facing = (Direction) original.m_61143_(BlockStateProperties.FACING);
        BlockState reversed = (BlockState) original.m_61124_(BlockStateProperties.FACING, facing.getOpposite());
        this.m_177252_(pos, reversed);
    }

    public void assertNixiePower(BlockPos pos, int strength) {
        NixieTubeBlockEntity nixie = this.getBlockEntity((BlockEntityType<NixieTubeBlockEntity>) AllBlockEntityTypes.NIXIE_TUBE.get(), pos);
        int actualStrength = nixie.getRedstoneStrength();
        if (actualStrength != strength) {
            this.fail("Expected nixie tube at %s to have power of %s, got %s".formatted(pos, strength, actualStrength));
        }
    }

    public void powerLever(BlockPos pos) {
        this.m_177208_(Blocks.LEVER, pos);
        if (!(Boolean) this.m_177232_(pos).m_61143_(LeverBlock.POWERED)) {
            this.m_177421_(pos);
        }
    }

    public void unpowerLever(BlockPos pos) {
        this.m_177208_(Blocks.LEVER, pos);
        if ((Boolean) this.m_177232_(pos).m_61143_(LeverBlock.POWERED)) {
            this.m_177421_(pos);
        }
    }

    public void setTunnelMode(BlockPos pos, BrassTunnelBlockEntity.SelectionMode mode) {
        ScrollValueBehaviour behavior = this.getBehavior(pos, ScrollOptionBehaviour.TYPE);
        behavior.setValue(mode.ordinal());
    }

    public void assertSpeedometerSpeed(BlockPos speedometer, float value) {
        SpeedGaugeBlockEntity be = this.getBlockEntity((BlockEntityType<SpeedGaugeBlockEntity>) AllBlockEntityTypes.SPEEDOMETER.get(), speedometer);
        this.assertInRange((double) be.getSpeed(), (double) value - 0.01, (double) value + 0.01);
    }

    public void assertStressometerCapacity(BlockPos stressometer, float value) {
        StressGaugeBlockEntity be = this.getBlockEntity((BlockEntityType<StressGaugeBlockEntity>) AllBlockEntityTypes.STRESSOMETER.get(), stressometer);
        this.assertInRange((double) be.getNetworkCapacity(), (double) value - 0.01, (double) value + 0.01);
    }

    public void toggleActorsOfType(Contraption contraption, ItemLike item) {
        AtomicBoolean toggled = new AtomicBoolean(false);
        contraption.getInteractors().forEach((localPos, behavior) -> {
            if (!toggled.get() && behavior instanceof ContraptionControlsMovingInteraction controls) {
                MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> actor = contraption.getActorAt(localPos);
                if (actor != null) {
                    ItemStack filter = ContraptionControlsMovement.getFilter((MovementContext) actor.right);
                    if (filter != null && filter.is(item.asItem())) {
                        controls.handlePlayerInteraction(this.m_177368_(), InteractionHand.MAIN_HAND, localPos, contraption.entity);
                        toggled.set(true);
                    }
                }
            }
        });
    }

    public <T extends BlockEntity> T getBlockEntity(BlockEntityType<T> type, BlockPos pos) {
        BlockEntity be = this.m_177347_(pos);
        BlockEntityType<?> actualType = be == null ? null : be.getType();
        if (actualType != type) {
            String actualId = actualType == null ? "null" : RegisteredObjects.getKeyOrThrow(actualType).toString();
            String error = "Expected block entity at pos [%s] with type [%s], got [%s]".formatted(pos, RegisteredObjects.getKeyOrThrow(type), actualId);
            this.fail(error);
        }
        return (T) be;
    }

    public <T extends BlockEntity & IMultiBlockEntityContainer> T getControllerBlockEntity(BlockEntityType<T> type, BlockPos anySegment) {
        T be = this.<T>getBlockEntity(type, anySegment).getControllerBE();
        if (be == null) {
            this.fail("Could not get block entity controller with type [%s] from pos [%s]".formatted(RegisteredObjects.getKeyOrThrow(type), anySegment));
        }
        return be;
    }

    public <T extends BlockEntityBehaviour> T getBehavior(BlockPos pos, BehaviourType<T> type) {
        T behavior = BlockEntityBehaviour.get(this.m_177100_(), this.m_177449_(pos), type);
        if (behavior == null) {
            this.fail("Behavior at " + pos + " missing, expected " + type.getName());
        }
        return behavior;
    }

    public ItemEntity spawnItem(BlockPos pos, ItemStack stack) {
        Vec3 spawn = Vec3.atCenterOf(this.m_177449_(pos));
        ServerLevel level = this.m_177100_();
        ItemEntity item = new ItemEntity(level, spawn.x, spawn.y, spawn.z, stack, 0.0, 0.0, 0.0);
        level.addFreshEntity(item);
        return item;
    }

    public void spawnItems(BlockPos pos, Item item, int amount) {
        while (amount > 0) {
            int toSpawn = Math.min(amount, item.getMaxStackSize());
            amount -= toSpawn;
            ItemStack stack = new ItemStack(item, toSpawn);
            this.spawnItem(pos, stack);
        }
    }

    public <T extends Entity> T getFirstEntity(EntityType<T> type, BlockPos pos) {
        List<T> list = this.getEntitiesBetween(type, pos.north().east().above(), pos.south().west().below());
        if (list.isEmpty()) {
            this.fail("No entities at pos: " + pos);
        }
        return (T) list.get(0);
    }

    public <T extends Entity> List<T> getEntitiesBetween(EntityType<T> type, BlockPos pos1, BlockPos pos2) {
        BoundingBox box = BoundingBox.fromCorners(this.m_177449_(pos1), this.m_177449_(pos2));
        return (List<T>) this.m_177100_().getEntities(type, e -> box.isInside(e.blockPosition()));
    }

    public IFluidHandler fluidStorageAt(BlockPos pos) {
        BlockEntity be = this.m_177347_(pos);
        if (be == null) {
            this.fail("BlockEntity not present");
        }
        Optional<IFluidHandler> handler = be.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
        if (handler.isEmpty()) {
            this.fail("handler not present");
        }
        return (IFluidHandler) handler.get();
    }

    public FluidStack getTankContents(BlockPos tank) {
        IFluidHandler handler = this.fluidStorageAt(tank);
        return handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
    }

    public long getTankCapacity(BlockPos pos) {
        IFluidHandler handler = this.fluidStorageAt(pos);
        long total = 0L;
        for (int i = 0; i < handler.getTanks(); i++) {
            total += (long) handler.getTankCapacity(i);
        }
        return total;
    }

    public long getFluidInTanks(BlockPos... tanks) {
        long total = 0L;
        for (BlockPos tank : tanks) {
            total += (long) this.getTankContents(tank).getAmount();
        }
        return total;
    }

    public void assertFluidPresent(FluidStack fluid, BlockPos pos) {
        FluidStack contained = this.getTankContents(pos);
        if (!fluid.isFluidEqual(contained)) {
            this.fail("Different fluids");
        }
        if (fluid.getAmount() != contained.getAmount()) {
            this.fail("Different amounts");
        }
    }

    public void assertTankEmpty(BlockPos pos) {
        this.assertFluidPresent(FluidStack.EMPTY, pos);
    }

    public void assertTanksEmpty(BlockPos... tanks) {
        for (BlockPos tank : tanks) {
            this.assertTankEmpty(tank);
        }
    }

    public IItemHandler itemStorageAt(BlockPos pos) {
        BlockEntity be = this.m_177347_(pos);
        if (be == null) {
            this.fail("BlockEntity not present");
        }
        Optional<IItemHandler> handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (handler.isEmpty()) {
            this.fail("handler not present");
        }
        return (IItemHandler) handler.get();
    }

    public Object2LongMap<Item> getItemContent(BlockPos pos) {
        IItemHandler handler = this.itemStorageAt(pos);
        Object2LongMap<Item> map = new Object2LongArrayMap();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                long amount = map.getLong(item);
                amount += (long) stack.getCount();
                map.put(item, amount);
            }
        }
        return map;
    }

    public long getTotalItems(BlockPos pos) {
        IItemHandler storage = this.itemStorageAt(pos);
        long total = 0L;
        for (int i = 0; i < storage.getSlots(); i++) {
            total += (long) storage.getStackInSlot(i).getCount();
        }
        return total;
    }

    public void assertAnyContained(BlockPos pos, Item... items) {
        IItemHandler handler = this.itemStorageAt(pos);
        boolean noneFound = true;
        for (int i = 0; i < handler.getSlots(); i++) {
            for (Item item : items) {
                if (handler.getStackInSlot(i).is(item)) {
                    noneFound = false;
                    break;
                }
            }
        }
        if (noneFound) {
            this.fail("No matching items " + Arrays.toString(items) + " found in handler at pos: " + pos);
        }
    }

    public void assertContentPresent(Object2LongMap<Item> content, BlockPos pos) {
        IItemHandler handler = this.itemStorageAt(pos);
        Object2LongMap<Item> map = new Object2LongArrayMap(content);
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                long amount = map.getLong(item);
                amount -= (long) stack.getCount();
                if (amount == 0L) {
                    map.removeLong(item);
                } else {
                    map.put(item, amount);
                }
            }
        }
        if (!map.isEmpty()) {
            this.fail("Storage missing content: " + map);
        }
    }

    public void assertContainersEmpty(List<BlockPos> positions) {
        for (BlockPos pos : positions) {
            this.assertContainerEmpty(pos);
        }
    }

    @Override
    public void assertContainerEmpty(@NotNull BlockPos pos) {
        IItemHandler storage = this.itemStorageAt(pos);
        for (int i = 0; i < storage.getSlots(); i++) {
            if (!storage.getStackInSlot(i).isEmpty()) {
                this.fail("Storage not empty");
            }
        }
    }

    public void assertContainerContains(BlockPos pos, ItemLike item) {
        this.assertContainerContains(pos, item.asItem());
    }

    @Override
    public void assertContainerContains(@NotNull BlockPos pos, @NotNull Item item) {
        this.assertContainerContains(pos, new ItemStack(item));
    }

    public void assertContainerContains(BlockPos pos, ItemStack item) {
        IItemHandler storage = this.itemStorageAt(pos);
        ItemStack extracted = ItemHelper.extract(storage, stack -> ItemHandlerHelper.canItemStacksStack(stack, item), item.getCount(), true);
        if (extracted.isEmpty()) {
            this.fail("item not present: " + item);
        }
    }

    public void assertSecondsPassed(int seconds) {
        if (this.m_177436_() < (long) seconds * 20L) {
            this.fail("Waiting for %s seconds to pass".formatted(seconds));
        }
    }

    public long secondsPassed() {
        return this.m_177436_() % 20L;
    }

    public void whenSecondsPassed(int seconds, Runnable run) {
        this.m_177306_((long) seconds * 20L, run);
    }

    public void assertCloseEnoughTo(double value, double expected) {
        this.assertInRange(value, expected - 1.0, expected + 1.0);
    }

    public void assertInRange(double value, double min, double max) {
        if (value < min) {
            this.fail("Value %s below expected min of %s".formatted(value, min));
        }
        if (value > max) {
            this.fail("Value %s greater than expected max of %s".formatted(value, max));
        }
    }

    @Contract("_->fail")
    @Override
    public void fail(@NotNull String exceptionMessage) {
        super.fail(exceptionMessage);
    }
}