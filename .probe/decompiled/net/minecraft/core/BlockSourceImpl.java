package net.minecraft.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSourceImpl implements BlockSource {

    private final ServerLevel level;

    private final BlockPos pos;

    public BlockSourceImpl(ServerLevel serverLevel0, BlockPos blockPos1) {
        this.level = serverLevel0;
        this.pos = blockPos1;
    }

    @Override
    public ServerLevel getLevel() {
        return this.level;
    }

    @Override
    public double x() {
        return (double) this.pos.m_123341_() + 0.5;
    }

    @Override
    public double y() {
        return (double) this.pos.m_123342_() + 0.5;
    }

    @Override
    public double z() {
        return (double) this.pos.m_123343_() + 0.5;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public BlockState getBlockState() {
        return this.level.m_8055_(this.pos);
    }

    @Override
    public <T extends BlockEntity> T getEntity() {
        return (T) this.level.m_7702_(this.pos);
    }
}