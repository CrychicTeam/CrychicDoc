package com.simibubi.create.content.contraptions.behaviour.dispenser;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import javax.annotation.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@MethodsReturnNonnullByDefault
public class ContraptionBlockSource implements BlockSource {

    private final BlockPos pos;

    private final MovementContext context;

    private final Direction overrideFacing;

    public ContraptionBlockSource(MovementContext context, BlockPos pos) {
        this(context, pos, null);
    }

    public ContraptionBlockSource(MovementContext context, BlockPos pos, @Nullable Direction overrideFacing) {
        this.pos = pos;
        this.context = context;
        this.overrideFacing = overrideFacing;
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
        return this.context.state.m_61138_(BlockStateProperties.FACING) && this.overrideFacing != null ? (BlockState) this.context.state.m_61124_(BlockStateProperties.FACING, this.overrideFacing) : this.context.state;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> T getEntity() {
        return null;
    }

    @Nullable
    @Override
    public ServerLevel getLevel() {
        MinecraftServer server = this.context.world.getServer();
        return server != null ? server.getLevel(this.context.world.dimension()) : null;
    }
}