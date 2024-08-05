package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BubbleColumnBlock extends Block implements BucketPickup {

    public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;

    private static final int CHECK_PERIOD = 5;

    public BubbleColumnBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DRAG_DOWN, true));
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        BlockState $$4 = level1.getBlockState(blockPos2.above());
        if ($$4.m_60795_()) {
            entity3.onAboveBubbleCol((Boolean) blockState0.m_61143_(DRAG_DOWN));
            if (!level1.isClientSide) {
                ServerLevel $$5 = (ServerLevel) level1;
                for (int $$6 = 0; $$6 < 2; $$6++) {
                    $$5.sendParticles(ParticleTypes.SPLASH, (double) blockPos2.m_123341_() + level1.random.nextDouble(), (double) (blockPos2.m_123342_() + 1), (double) blockPos2.m_123343_() + level1.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                    $$5.sendParticles(ParticleTypes.BUBBLE, (double) blockPos2.m_123341_() + level1.random.nextDouble(), (double) (blockPos2.m_123342_() + 1), (double) blockPos2.m_123343_() + level1.random.nextDouble(), 1, 0.0, 0.01, 0.0, 0.2);
                }
            }
        } else {
            entity3.onInsideBubbleColumn((Boolean) blockState0.m_61143_(DRAG_DOWN));
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        updateColumn(serverLevel1, blockPos2, blockState0, serverLevel1.m_8055_(blockPos2.below()));
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return Fluids.WATER.getSource(false);
    }

    public static void updateColumn(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        updateColumn(levelAccessor0, blockPos1, levelAccessor0.m_8055_(blockPos1), blockState2);
    }

    public static void updateColumn(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3) {
        if (canExistIn(blockState2)) {
            BlockState $$4 = getColumnState(blockState3);
            levelAccessor0.m_7731_(blockPos1, $$4, 2);
            BlockPos.MutableBlockPos $$5 = blockPos1.mutable().move(Direction.UP);
            while (canExistIn(levelAccessor0.m_8055_($$5))) {
                if (!levelAccessor0.m_7731_($$5, $$4, 2)) {
                    return;
                }
                $$5.move(Direction.UP);
            }
        }
    }

    private static boolean canExistIn(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.BUBBLE_COLUMN) || blockState0.m_60713_(Blocks.WATER) && blockState0.m_60819_().getAmount() >= 8 && blockState0.m_60819_().isSource();
    }

    private static BlockState getColumnState(BlockState blockState0) {
        if (blockState0.m_60713_(Blocks.BUBBLE_COLUMN)) {
            return blockState0;
        } else if (blockState0.m_60713_(Blocks.SOUL_SAND)) {
            return (BlockState) Blocks.BUBBLE_COLUMN.defaultBlockState().m_61124_(DRAG_DOWN, false);
        } else {
            return blockState0.m_60713_(Blocks.MAGMA_BLOCK) ? (BlockState) Blocks.BUBBLE_COLUMN.defaultBlockState().m_61124_(DRAG_DOWN, true) : Blocks.WATER.defaultBlockState();
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        double $$4 = (double) blockPos2.m_123341_();
        double $$5 = (double) blockPos2.m_123342_();
        double $$6 = (double) blockPos2.m_123343_();
        if ((Boolean) blockState0.m_61143_(DRAG_DOWN)) {
            level1.addAlwaysVisibleParticle(ParticleTypes.CURRENT_DOWN, $$4 + 0.5, $$5 + 0.8, $$6, 0.0, 0.0, 0.0);
            if (randomSource3.nextInt(200) == 0) {
                level1.playLocalSound($$4, $$5, $$6, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2F + randomSource3.nextFloat() * 0.2F, 0.9F + randomSource3.nextFloat() * 0.15F, false);
            }
        } else {
            level1.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, $$4 + 0.5, $$5, $$6 + 0.5, 0.0, 0.04, 0.0);
            level1.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, $$4 + (double) randomSource3.nextFloat(), $$5 + (double) randomSource3.nextFloat(), $$6 + (double) randomSource3.nextFloat(), 0.0, 0.04, 0.0);
            if (randomSource3.nextInt(200) == 0) {
                level1.playLocalSound($$4, $$5, $$6, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + randomSource3.nextFloat() * 0.2F, 0.9F + randomSource3.nextFloat() * 0.15F, false);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        if (!blockState0.m_60710_(levelAccessor3, blockPos4) || direction1 == Direction.DOWN || direction1 == Direction.UP && !blockState2.m_60713_(Blocks.BUBBLE_COLUMN) && canExistIn(blockState2)) {
            levelAccessor3.scheduleTick(blockPos4, this, 5);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
        return $$3.m_60713_(Blocks.BUBBLE_COLUMN) || $$3.m_60713_(Blocks.MAGMA_BLOCK) || $$3.m_60713_(Blocks.SOUL_SAND);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(DRAG_DOWN);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        levelAccessor0.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 11);
        return new ItemStack(Items.WATER_BUCKET);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }
}