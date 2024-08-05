package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchBlock extends Block {

    protected static final int AABB_STANDING_OFFSET = 2;

    protected static final VoxelShape AABB = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);

    protected final ParticleOptions flameParticle;

    protected TorchBlock(BlockBehaviour.Properties blockBehaviourProperties0, ParticleOptions particleOptions1) {
        super(blockBehaviourProperties0);
        this.flameParticle = particleOptions1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return AABB;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return m_49863_(levelReader1, blockPos2.below(), Direction.UP);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        double $$4 = (double) blockPos2.m_123341_() + 0.5;
        double $$5 = (double) blockPos2.m_123342_() + 0.7;
        double $$6 = (double) blockPos2.m_123343_() + 0.5;
        level1.addParticle(ParticleTypes.SMOKE, $$4, $$5, $$6, 0.0, 0.0, 0.0);
        level1.addParticle(this.flameParticle, $$4, $$5, $$6, 0.0, 0.0, 0.0);
    }
}