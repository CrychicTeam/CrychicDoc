package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FarmBlock extends Block {

    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    public static final int MAX_MOISTURE = 7;

    protected FarmBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(MOISTURE, 0));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == Direction.UP && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.above());
        return !$$3.m_280296_() || $$3.m_60734_() instanceof FenceGateBlock || $$3.m_60734_() instanceof MovingPistonBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return !this.m_49966_().m_60710_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos()) ? Blocks.DIRT.defaultBlockState() : super.getStateForPlacement(blockPlaceContext0);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            turnToDirt(null, blockState0, serverLevel1, blockPos2);
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = (Integer) blockState0.m_61143_(MOISTURE);
        if (!isNearWater(serverLevel1, blockPos2) && !serverLevel1.m_46758_(blockPos2.above())) {
            if ($$4 > 0) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(MOISTURE, $$4 - 1), 2);
            } else if (!shouldMaintainFarmland(serverLevel1, blockPos2)) {
                turnToDirt(null, blockState0, serverLevel1, blockPos2);
            }
        } else if ($$4 < 7) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(MOISTURE, 7), 2);
        }
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        if (!level0.isClientSide && level0.random.nextFloat() < float4 - 0.5F && entity3 instanceof LivingEntity && (entity3 instanceof Player || level0.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && entity3.getBbWidth() * entity3.getBbWidth() * entity3.getBbHeight() > 0.512F) {
            turnToDirt(entity3, blockState1, level0, blockPos2);
        }
        super.fallOn(level0, blockState1, blockPos2, entity3, float4);
    }

    public static void turnToDirt(@Nullable Entity entity0, BlockState blockState1, Level level2, BlockPos blockPos3) {
        BlockState $$4 = m_49897_(blockState1, Blocks.DIRT.defaultBlockState(), level2, blockPos3);
        level2.setBlockAndUpdate(blockPos3, $$4);
        level2.m_220407_(GameEvent.BLOCK_CHANGE, blockPos3, GameEvent.Context.of(entity0, $$4));
    }

    private static boolean shouldMaintainFarmland(BlockGetter blockGetter0, BlockPos blockPos1) {
        return blockGetter0.getBlockState(blockPos1.above()).m_204336_(BlockTags.MAINTAINS_FARMLAND);
    }

    private static boolean isNearWater(LevelReader levelReader0, BlockPos blockPos1) {
        for (BlockPos $$2 : BlockPos.betweenClosed(blockPos1.offset(-4, 0, -4), blockPos1.offset(4, 1, 4))) {
            if (levelReader0.m_6425_($$2).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(MOISTURE);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}