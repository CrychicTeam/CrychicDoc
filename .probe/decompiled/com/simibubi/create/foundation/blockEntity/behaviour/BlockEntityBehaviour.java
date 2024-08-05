package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import java.util.ConcurrentModificationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityBehaviour {

    public SmartBlockEntity blockEntity;

    private int lazyTickRate;

    private int lazyTickCounter;

    public BlockEntityBehaviour(SmartBlockEntity be) {
        this.blockEntity = be;
        this.setLazyTickRate(10);
    }

    public abstract BehaviourType<?> getType();

    public void initialize() {
    }

    public void tick() {
        if (this.lazyTickCounter-- <= 0) {
            this.lazyTickCounter = this.lazyTickRate;
            this.lazyTick();
        }
    }

    public void read(CompoundTag nbt, boolean clientPacket) {
    }

    public void write(CompoundTag nbt, boolean clientPacket) {
    }

    public boolean isSafeNBT() {
        return false;
    }

    public ItemRequirement getRequiredItems() {
        return ItemRequirement.NONE;
    }

    public void onBlockChanged(BlockState oldState) {
    }

    public void onNeighborChanged(BlockPos neighborPos) {
    }

    public void unload() {
    }

    public void destroy() {
    }

    public void setLazyTickRate(int slowTickRate) {
        this.lazyTickRate = slowTickRate;
        this.lazyTickCounter = slowTickRate;
    }

    public void lazyTick() {
    }

    public BlockPos getPos() {
        return this.blockEntity.m_58899_();
    }

    public Level getWorld() {
        return this.blockEntity.m_58904_();
    }

    public static <T extends BlockEntityBehaviour> T get(BlockGetter reader, BlockPos pos, BehaviourType<T> type) {
        BlockEntity be;
        try {
            be = reader.getBlockEntity(pos);
        } catch (ConcurrentModificationException var5) {
            be = null;
        }
        return get(be, type);
    }

    public static <T extends BlockEntityBehaviour> T get(BlockEntity be, BehaviourType<T> type) {
        if (be == null) {
            return null;
        } else {
            return !(be instanceof SmartBlockEntity ste) ? null : ste.getBehaviour(type);
        }
    }
}