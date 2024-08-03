package com.simibubi.create.content.contraptions.actors.harvester;

import com.simibubi.create.foundation.blockEntity.CachedRenderBBBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class HarvesterBlockEntity extends CachedRenderBBBlockEntity {

    private float manuallyAnimatedSpeed;

    public HarvesterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_);
    }

    public float getAnimatedSpeed() {
        return this.manuallyAnimatedSpeed;
    }

    public void setAnimatedSpeed(float speed) {
        this.manuallyAnimatedSpeed = speed;
    }
}