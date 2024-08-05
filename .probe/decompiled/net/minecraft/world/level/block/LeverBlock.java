package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeverBlock extends FaceAttachedHorizontalDirectionalBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected static final int DEPTH = 6;

    protected static final int WIDTH = 6;

    protected static final int HEIGHT = 8;

    protected static final VoxelShape NORTH_AABB = Block.box(5.0, 4.0, 10.0, 11.0, 12.0, 16.0);

    protected static final VoxelShape SOUTH_AABB = Block.box(5.0, 4.0, 0.0, 11.0, 12.0, 6.0);

    protected static final VoxelShape WEST_AABB = Block.box(10.0, 4.0, 5.0, 16.0, 12.0, 11.0);

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 4.0, 5.0, 6.0, 12.0, 11.0);

    protected static final VoxelShape UP_AABB_Z = Block.box(5.0, 0.0, 4.0, 11.0, 6.0, 12.0);

    protected static final VoxelShape UP_AABB_X = Block.box(4.0, 0.0, 5.0, 12.0, 6.0, 11.0);

    protected static final VoxelShape DOWN_AABB_Z = Block.box(5.0, 10.0, 4.0, 11.0, 16.0, 12.0);

    protected static final VoxelShape DOWN_AABB_X = Block.box(4.0, 10.0, 5.0, 12.0, 16.0, 11.0);

    protected LeverBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(POWERED, false)).m_61124_(f_53179_, AttachFace.WALL));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((AttachFace) blockState0.m_61143_(f_53179_)) {
            case FLOOR:
                switch(((Direction) blockState0.m_61143_(f_54117_)).getAxis()) {
                    case X:
                        return UP_AABB_X;
                    case Z:
                    default:
                        return UP_AABB_Z;
                }
            case WALL:
                switch((Direction) blockState0.m_61143_(f_54117_)) {
                    case EAST:
                        return EAST_AABB;
                    case WEST:
                        return WEST_AABB;
                    case SOUTH:
                        return SOUTH_AABB;
                    case NORTH:
                    default:
                        return NORTH_AABB;
                }
            case CEILING:
            default:
                switch(((Direction) blockState0.m_61143_(f_54117_)).getAxis()) {
                    case X:
                        return DOWN_AABB_X;
                    case Z:
                    default:
                        return DOWN_AABB_Z;
                }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            BlockState $$6 = (BlockState) blockState0.m_61122_(POWERED);
            if ((Boolean) $$6.m_61143_(POWERED)) {
                makeParticle($$6, level1, blockPos2, 1.0F);
            }
            return InteractionResult.SUCCESS;
        } else {
            BlockState $$7 = this.pull(blockState0, level1, blockPos2);
            float $$8 = $$7.m_61143_(POWERED) ? 0.6F : 0.5F;
            level1.playSound(null, blockPos2, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, $$8);
            level1.m_142346_(player3, $$7.m_61143_(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, blockPos2);
            return InteractionResult.CONSUME;
        }
    }

    public BlockState pull(BlockState blockState0, Level level1, BlockPos blockPos2) {
        blockState0 = (BlockState) blockState0.m_61122_(POWERED);
        level1.setBlock(blockPos2, blockState0, 3);
        this.updateNeighbours(blockState0, level1, blockPos2);
        return blockState0;
    }

    private static void makeParticle(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, float float3) {
        Direction $$4 = ((Direction) blockState0.m_61143_(f_54117_)).getOpposite();
        Direction $$5 = m_53200_(blockState0).getOpposite();
        double $$6 = (double) blockPos2.m_123341_() + 0.5 + 0.1 * (double) $$4.getStepX() + 0.2 * (double) $$5.getStepX();
        double $$7 = (double) blockPos2.m_123342_() + 0.5 + 0.1 * (double) $$4.getStepY() + 0.2 * (double) $$5.getStepY();
        double $$8 = (double) blockPos2.m_123343_() + 0.5 + 0.1 * (double) $$4.getStepZ() + 0.2 * (double) $$5.getStepZ();
        levelAccessor1.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, float3), $$6, $$7, $$8, 0.0, 0.0, 0.0);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(POWERED) && randomSource3.nextFloat() < 0.25F) {
            makeParticle(blockState0, level1, blockPos2, 0.5F);
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Boolean) blockState0.m_61143_(POWERED)) {
                this.updateNeighbours(blockState0, level1, blockPos2);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) && m_53200_(blockState0) == direction3 ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    private void updateNeighbours(BlockState blockState0, Level level1, BlockPos blockPos2) {
        level1.updateNeighborsAt(blockPos2, this);
        level1.updateNeighborsAt(blockPos2.relative(m_53200_(blockState0).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_53179_, f_54117_, POWERED);
    }
}