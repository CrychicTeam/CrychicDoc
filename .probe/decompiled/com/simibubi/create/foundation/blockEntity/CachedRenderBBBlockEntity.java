package com.simibubi.create.foundation.blockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class CachedRenderBBBlockEntity extends SyncedBlockEntity {

    private AABB renderBoundingBox;

    public CachedRenderBBBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (this.renderBoundingBox == null) {
            this.renderBoundingBox = this.createRenderBoundingBox();
        }
        return this.renderBoundingBox;
    }

    protected void invalidateRenderBoundingBox() {
        this.renderBoundingBox = null;
    }

    protected AABB createRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }
}