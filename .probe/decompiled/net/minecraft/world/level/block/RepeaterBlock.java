package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RepeaterBlock extends DiodeBlock {

    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    public static final IntegerProperty DELAY = BlockStateProperties.DELAY;

    protected RepeaterBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(DELAY, 1)).m_61124_(LOCKED, false)).m_61124_(f_52496_, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!player3.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61122_(DELAY), 3);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    @Override
    protected int getDelay(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(DELAY) * 2;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = super.getStateForPlacement(blockPlaceContext0);
        return (BlockState) $$1.m_61124_(LOCKED, this.isLocked(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos(), $$1));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return !levelAccessor3.m_5776_() && direction1.getAxis() != ((Direction) blockState0.m_61143_(f_54117_)).getAxis() ? (BlockState) blockState0.m_61124_(LOCKED, this.isLocked(levelAccessor3, blockPos4, blockState0)) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean isLocked(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
        return this.m_276835_(levelReader0, blockPos1, blockState2) > 0;
    }

    @Override
    protected boolean sideInputDiodesOnly() {
        return true;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(f_52496_)) {
            Direction $$4 = (Direction) blockState0.m_61143_(f_54117_);
            double $$5 = (double) blockPos2.m_123341_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2;
            double $$6 = (double) blockPos2.m_123342_() + 0.4 + (randomSource3.nextDouble() - 0.5) * 0.2;
            double $$7 = (double) blockPos2.m_123343_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2;
            float $$8 = -5.0F;
            if (randomSource3.nextBoolean()) {
                $$8 = (float) ((Integer) blockState0.m_61143_(DELAY) * 2 - 1);
            }
            $$8 /= 16.0F;
            double $$9 = (double) ($$8 * (float) $$4.getStepX());
            double $$10 = (double) ($$8 * (float) $$4.getStepZ());
            level1.addParticle(DustParticleOptions.REDSTONE, $$5 + $$9, $$6, $$7 + $$10, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, DELAY, LOCKED, f_52496_);
    }
}