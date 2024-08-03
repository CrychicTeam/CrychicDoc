package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class BlockDreadPortal extends BaseEntityBlock implements IDreadBlock {

    public BlockDreadPortal() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).pushReaction(PushReaction.BLOCK).dynamicShape().strength(-1.0F, 100000.0F).lightLevel(state -> 1).randomTicks());
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entity) {
    }

    public void updateTick(Level worldIn, BlockPos pos, BlockState state, RandomSource rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.m_46961_(pos, true);
        }
    }

    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.m_46961_(pos, true);
        }
    }

    public boolean canSurviveAt(Level world, BlockPos pos) {
        return DragonUtils.isDreadBlock(world.getBlockState(pos.above())) && DragonUtils.isDreadBlock(world.getBlockState(pos.below()));
    }

    public int quantityDropped(RandomSource random) {
        return 0;
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityDreadPortal) {
            int i = 3;
            for (int j = 0; j < i; j++) {
                double d0 = (double) ((float) pos.m_123341_() + rand.nextFloat());
                double d1 = (double) ((float) pos.m_123342_() + rand.nextFloat());
                double d2 = (double) ((float) pos.m_123343_() + rand.nextFloat());
                double d3 = ((double) rand.nextFloat() - 0.5) * 0.25;
                double d4 = (double) rand.nextFloat() * -0.25;
                double d5 = ((double) rand.nextFloat() - 0.5) * 0.25;
                int k = rand.nextInt(2) * 2 - 1;
                IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Portal, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return m_152132_(entityType, IafTileEntityRegistry.DREAD_PORTAL.get(), TileEntityDreadPortal::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDreadPortal(pos, state);
    }
}