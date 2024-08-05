package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.entity.Gravisand;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BlockUtils;

public class GravisandBlock extends ZetaBlock {

    public GravisandBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
        }
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        this.checkRedstone(world, pos);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        this.checkRedstone(worldIn, pos);
    }

    private void checkRedstone(Level worldIn, BlockPos pos) {
        boolean powered = worldIn.m_276867_(pos);
        if (powered) {
            worldIn.m_186460_(pos, this, 2);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level worldIn, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!worldIn.f_46443_ && this.checkFallable(state, worldIn, pos)) {
            for (Direction face : Direction.values()) {
                BlockPos offPos = pos.relative(face);
                BlockState offState = worldIn.m_8055_(offPos);
                if (offState.m_60734_() == this) {
                    worldIn.m_186460_(offPos, this, 2);
                }
            }
        }
    }

    private boolean checkFallable(BlockState state, Level worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            return this.tryFall(state, worldIn, pos, Direction.DOWN) ? true : this.tryFall(state, worldIn, pos, Direction.UP);
        } else {
            return false;
        }
    }

    private boolean tryFall(BlockState state, Level worldIn, BlockPos pos, Direction facing) {
        BlockPos target = pos.relative(facing);
        if ((worldIn.m_46859_(target) || BlockUtils.canFallThrough(worldIn.getBlockState(target))) && worldIn.isInWorldBounds(pos)) {
            Gravisand entity = new Gravisand(worldIn, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, (float) facing.getStepY());
            worldIn.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
            worldIn.m_7967_(entity);
            return true;
        } else {
            return false;
        }
    }
}