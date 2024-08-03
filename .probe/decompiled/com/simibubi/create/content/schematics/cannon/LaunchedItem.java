package com.simibubi.create.content.schematics.cannon;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorItem;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LaunchedItem {

    public int totalTicks;

    public int ticksRemaining;

    public BlockPos target;

    public ItemStack stack;

    private LaunchedItem(BlockPos start, BlockPos target, ItemStack stack) {
        this(target, stack, ticksForDistance(start, target), ticksForDistance(start, target));
    }

    private static int ticksForDistance(BlockPos start, BlockPos target) {
        return (int) Math.max(10.0, Math.sqrt(Math.sqrt(target.m_123331_(start))) * 4.0);
    }

    LaunchedItem() {
    }

    private LaunchedItem(BlockPos target, ItemStack stack, int ticksLeft, int total) {
        this.target = target;
        this.stack = stack;
        this.totalTicks = total;
        this.ticksRemaining = ticksLeft;
    }

    public boolean update(Level world) {
        if (this.ticksRemaining > 0) {
            this.ticksRemaining--;
            return false;
        } else if (world.isClientSide) {
            return false;
        } else {
            this.place(world);
            return true;
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag c = new CompoundTag();
        c.putInt("TotalTicks", this.totalTicks);
        c.putInt("TicksLeft", this.ticksRemaining);
        c.put("Stack", this.stack.serializeNBT());
        c.put("Target", NbtUtils.writeBlockPos(this.target));
        return c;
    }

    public static LaunchedItem fromNBT(CompoundTag c, HolderGetter<Block> holderGetter) {
        LaunchedItem launched = (LaunchedItem) (c.contains("Length") ? new LaunchedItem.ForBelt() : (c.contains("BlockState") ? new LaunchedItem.ForBlockState() : new LaunchedItem.ForEntity()));
        launched.readNBT(c, holderGetter);
        return launched;
    }

    abstract void place(Level var1);

    void readNBT(CompoundTag c, HolderGetter<Block> holderGetter) {
        this.target = NbtUtils.readBlockPos(c.getCompound("Target"));
        this.ticksRemaining = c.getInt("TicksLeft");
        this.totalTicks = c.getInt("TotalTicks");
        this.stack = ItemStack.of(c.getCompound("Stack"));
    }

    public static class ForBelt extends LaunchedItem.ForBlockState {

        public int length;

        public BeltBlockEntity.CasingType[] casings;

        public ForBelt() {
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag serializeNBT = super.serializeNBT();
            serializeNBT.putInt("Length", this.length);
            serializeNBT.putIntArray("Casing", Arrays.stream(this.casings).map(Enum::ordinal).toList());
            return serializeNBT;
        }

        @Override
        void readNBT(CompoundTag nbt, HolderGetter<Block> holderGetter) {
            this.length = nbt.getInt("Length");
            int[] intArray = nbt.getIntArray("Casing");
            this.casings = new BeltBlockEntity.CasingType[this.length];
            for (int i = 0; i < this.casings.length; i++) {
                this.casings[i] = i >= intArray.length ? BeltBlockEntity.CasingType.NONE : BeltBlockEntity.CasingType.values()[Mth.clamp(intArray[i], 0, BeltBlockEntity.CasingType.values().length - 1)];
            }
            super.readNBT(nbt, holderGetter);
        }

        public ForBelt(BlockPos start, BlockPos target, ItemStack stack, BlockState state, BeltBlockEntity.CasingType[] casings) {
            super(start, target, stack, state, null);
            this.casings = casings;
            this.length = casings.length;
        }

        @Override
        void place(Level world) {
            boolean isStart = this.state.m_61143_(BeltBlock.PART) == BeltPart.START;
            BlockPos offset = BeltBlock.nextSegmentPosition(this.state, BlockPos.ZERO, isStart);
            int i = this.length - 1;
            Direction.Axis axis = this.state.m_61143_(BeltBlock.SLOPE) == BeltSlope.SIDEWAYS ? Direction.Axis.Y : ((Direction) this.state.m_61143_(BeltBlock.HORIZONTAL_FACING)).getClockWise().getAxis();
            world.setBlockAndUpdate(this.target, (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(AbstractSimpleShaftBlock.AXIS, axis));
            BeltConnectorItem.createBelts(world, this.target, this.target.offset(offset.m_123341_() * i, offset.m_123342_() * i, offset.m_123343_() * i));
            for (int segment = 0; segment < this.length; segment++) {
                if (this.casings[segment] != BeltBlockEntity.CasingType.NONE) {
                    BlockPos casingTarget = this.target.offset(offset.m_123341_() * segment, offset.m_123342_() * segment, offset.m_123343_() * segment);
                    if (world.getBlockEntity(casingTarget) instanceof BeltBlockEntity bbe) {
                        bbe.setCasingType(this.casings[segment]);
                    }
                }
            }
        }
    }

    public static class ForBlockState extends LaunchedItem {

        public BlockState state;

        public CompoundTag data;

        ForBlockState() {
        }

        public ForBlockState(BlockPos start, BlockPos target, ItemStack stack, BlockState state, CompoundTag data) {
            super(start, target, stack);
            this.state = state;
            this.data = data;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag serializeNBT = super.serializeNBT();
            serializeNBT.put("BlockState", NbtUtils.writeBlockState(this.state));
            if (this.data != null) {
                this.data.remove("x");
                this.data.remove("y");
                this.data.remove("z");
                this.data.remove("id");
                serializeNBT.put("Data", this.data);
            }
            return serializeNBT;
        }

        @Override
        void readNBT(CompoundTag nbt, HolderGetter<Block> holderGetter) {
            super.readNBT(nbt, holderGetter);
            this.state = NbtUtils.readBlockState(holderGetter, nbt.getCompound("BlockState"));
            if (nbt.contains("Data", 10)) {
                this.data = nbt.getCompound("Data");
            }
        }

        @Override
        void place(Level world) {
            BlockHelper.placeSchematicBlock(world, this.state, this.target, this.stack, this.data);
        }
    }

    public static class ForEntity extends LaunchedItem {

        public Entity entity;

        private CompoundTag deferredTag;

        ForEntity() {
        }

        public ForEntity(BlockPos start, BlockPos target, ItemStack stack, Entity entity) {
            super(start, target, stack);
            this.entity = entity;
        }

        @Override
        public boolean update(Level world) {
            if (this.deferredTag != null && this.entity == null) {
                try {
                    Optional<Entity> loadEntityUnchecked = EntityType.create(this.deferredTag, world);
                    if (!loadEntityUnchecked.isPresent()) {
                        return true;
                    }
                    this.entity = (Entity) loadEntityUnchecked.get();
                } catch (Exception var31) {
                    return true;
                }
                this.deferredTag = null;
            }
            return super.update(world);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag serializeNBT = super.serializeNBT();
            if (this.entity != null) {
                serializeNBT.put("Entity", this.entity.serializeNBT());
            }
            return serializeNBT;
        }

        @Override
        void readNBT(CompoundTag nbt, HolderGetter<Block> holderGetter) {
            super.readNBT(nbt, holderGetter);
            if (nbt.contains("Entity")) {
                this.deferredTag = nbt.getCompound("Entity");
            }
        }

        @Override
        void place(Level world) {
            if (this.entity != null) {
                world.m_7967_(this.entity);
            }
        }
    }
}