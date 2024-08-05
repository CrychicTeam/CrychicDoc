package io.github.apace100.origins.content;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TemporaryCobwebBlock extends WebBlock {

    public TemporaryCobwebBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Deprecated
    @Override
    public void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!world.f_46443_) {
            world.m_46597_(pos, Blocks.AIR.defaultBlockState());
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void onPlace(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        worldIn.m_186460_(pos, this, 60);
    }
}