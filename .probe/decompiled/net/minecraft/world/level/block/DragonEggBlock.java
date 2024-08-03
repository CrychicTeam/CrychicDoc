package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DragonEggBlock extends FallingBlock {

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public DragonEggBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        this.teleport(blockState0, level1, blockPos2);
        return InteractionResult.sidedSuccess(level1.isClientSide);
    }

    @Override
    public void attack(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        this.teleport(blockState0, level1, blockPos2);
    }

    private void teleport(BlockState blockState0, Level level1, BlockPos blockPos2) {
        WorldBorder $$3 = level1.getWorldBorder();
        for (int $$4 = 0; $$4 < 1000; $$4++) {
            BlockPos $$5 = blockPos2.offset(level1.random.nextInt(16) - level1.random.nextInt(16), level1.random.nextInt(8) - level1.random.nextInt(8), level1.random.nextInt(16) - level1.random.nextInt(16));
            if (level1.getBlockState($$5).m_60795_() && $$3.isWithinBounds($$5)) {
                if (level1.isClientSide) {
                    for (int $$6 = 0; $$6 < 128; $$6++) {
                        double $$7 = level1.random.nextDouble();
                        float $$8 = (level1.random.nextFloat() - 0.5F) * 0.2F;
                        float $$9 = (level1.random.nextFloat() - 0.5F) * 0.2F;
                        float $$10 = (level1.random.nextFloat() - 0.5F) * 0.2F;
                        double $$11 = Mth.lerp($$7, (double) $$5.m_123341_(), (double) blockPos2.m_123341_()) + (level1.random.nextDouble() - 0.5) + 0.5;
                        double $$12 = Mth.lerp($$7, (double) $$5.m_123342_(), (double) blockPos2.m_123342_()) + level1.random.nextDouble() - 0.5;
                        double $$13 = Mth.lerp($$7, (double) $$5.m_123343_(), (double) blockPos2.m_123343_()) + (level1.random.nextDouble() - 0.5) + 0.5;
                        level1.addParticle(ParticleTypes.PORTAL, $$11, $$12, $$13, (double) $$8, (double) $$9, (double) $$10);
                    }
                } else {
                    level1.setBlock($$5, blockState0, 2);
                    level1.removeBlock(blockPos2, false);
                }
                return;
            }
        }
    }

    @Override
    protected int getDelayAfterPlace() {
        return 5;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}