package com.simibubi.create.content.redstone.displayLink;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DisplayLinkContext {

    private Level level;

    private DisplayLinkBlockEntity blockEntity;

    public Object flapDisplayContext;

    public DisplayLinkContext(Level level, DisplayLinkBlockEntity blockEntity) {
        this.level = level;
        this.blockEntity = blockEntity;
    }

    public Level level() {
        return this.level;
    }

    public DisplayLinkBlockEntity blockEntity() {
        return this.blockEntity;
    }

    public BlockEntity getSourceBlockEntity() {
        return this.level.getBlockEntity(this.getSourcePos());
    }

    public BlockPos getSourcePos() {
        return this.blockEntity.getSourcePosition();
    }

    public BlockEntity getTargetBlockEntity() {
        return this.level.getBlockEntity(this.getTargetPos());
    }

    public BlockPos getTargetPos() {
        return this.blockEntity.getTargetPosition();
    }

    public CompoundTag sourceConfig() {
        return this.blockEntity.getSourceConfig();
    }
}