package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IllusionBlockTile extends BlockEntity {

    private static final int OFFSET_DIST = 8;

    private BlockState mimicState;

    private BlockPos mimicPos;

    private long lastRecheck = -1L;

    public IllusionBlockTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
    }

    public IllusionBlockTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.ILLUSION_BLOCK.get(), pos, state);
    }

    public Pair<BlockPos, BlockState> getMimicBlock() {
        if (this.mimicState == null && this.f_58857_.getGameTime() >= this.lastRecheck + 100L || this.lastRecheck + 600L < this.f_58857_.getGameTime()) {
            this.lastRecheck = this.f_58857_.getGameTime();
            this.resolveMimicBlock();
        }
        return new Pair(this.mimicPos, this.mimicState);
    }

    private void resolveMimicBlock() {
        Direction dir = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
        ClipContext rtc = new ClipContext(Vec3.atCenterOf(this.m_58899_().offset(dir.getStepX(), dir.getStepY(), dir.getStepZ())), Vec3.atCenterOf(this.m_58899_().offset(dir.getStepX() * 8, dir.getStepY() * 8, dir.getStepZ() * 8)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
        BlockHitResult brtr = this.m_58904_().m_45547_(rtc);
        if (brtr.getType() == HitResult.Type.MISS) {
            this.mimicPos = this.m_58899_();
            this.mimicState = null;
        }
        this.mimicPos = brtr.getBlockPos();
        this.mimicState = this.f_58857_.getBlockState(this.mimicPos);
        if (!this.mimicState.m_60838_(this.m_58904_(), this.mimicPos)) {
            this.mimicState = null;
        }
    }
}