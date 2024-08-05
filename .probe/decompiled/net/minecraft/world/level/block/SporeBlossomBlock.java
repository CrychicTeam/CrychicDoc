package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SporeBlossomBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(2.0, 13.0, 2.0, 14.0, 16.0, 14.0);

    private static final int ADD_PARTICLE_ATTEMPTS = 14;

    private static final int PARTICLE_XZ_RADIUS = 10;

    private static final int PARTICLE_Y_MAX = 10;

    public SporeBlossomBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return Block.canSupportCenter(levelReader1, blockPos2.above(), Direction.DOWN) && !levelReader1.isWaterAt(blockPos2);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.UP && !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = blockPos2.m_123341_();
        int $$5 = blockPos2.m_123342_();
        int $$6 = blockPos2.m_123343_();
        double $$7 = (double) $$4 + randomSource3.nextDouble();
        double $$8 = (double) $$5 + 0.7;
        double $$9 = (double) $$6 + randomSource3.nextDouble();
        level1.addParticle(ParticleTypes.FALLING_SPORE_BLOSSOM, $$7, $$8, $$9, 0.0, 0.0, 0.0);
        BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
        for (int $$11 = 0; $$11 < 14; $$11++) {
            $$10.set($$4 + Mth.nextInt(randomSource3, -10, 10), $$5 - randomSource3.nextInt(10), $$6 + Mth.nextInt(randomSource3, -10, 10));
            BlockState $$12 = level1.getBlockState($$10);
            if (!$$12.m_60838_(level1, $$10)) {
                level1.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, (double) $$10.m_123341_() + randomSource3.nextDouble(), (double) $$10.m_123342_() + randomSource3.nextDouble(), (double) $$10.m_123343_() + randomSource3.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }
}