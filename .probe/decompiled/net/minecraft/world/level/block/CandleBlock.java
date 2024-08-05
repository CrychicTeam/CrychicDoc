package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.function.ToIntFunction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CandleBlock extends AbstractCandleBlock implements SimpleWaterloggedBlock {

    public static final int MIN_CANDLES = 1;

    public static final int MAX_CANDLES = 4;

    public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;

    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final ToIntFunction<BlockState> LIGHT_EMISSION = p_152848_ -> p_152848_.m_61143_(LIT) ? 3 * (Integer) p_152848_.m_61143_(CANDLES) : 0;

    private static final Int2ObjectMap<List<Vec3>> PARTICLE_OFFSETS = Util.make(() -> {
        Int2ObjectMap<List<Vec3>> $$0 = new Int2ObjectOpenHashMap();
        $$0.defaultReturnValue(ImmutableList.of());
        $$0.put(1, ImmutableList.of(new Vec3(0.5, 0.5, 0.5)));
        $$0.put(2, ImmutableList.of(new Vec3(0.375, 0.44, 0.5), new Vec3(0.625, 0.5, 0.44)));
        $$0.put(3, ImmutableList.of(new Vec3(0.5, 0.313, 0.625), new Vec3(0.375, 0.44, 0.5), new Vec3(0.56, 0.5, 0.44)));
        $$0.put(4, ImmutableList.of(new Vec3(0.44, 0.313, 0.56), new Vec3(0.625, 0.44, 0.56), new Vec3(0.375, 0.44, 0.375), new Vec3(0.56, 0.5, 0.375)));
        return Int2ObjectMaps.unmodifiable($$0);
    });

    private static final VoxelShape ONE_AABB = Block.box(7.0, 0.0, 7.0, 9.0, 6.0, 9.0);

    private static final VoxelShape TWO_AABB = Block.box(5.0, 0.0, 6.0, 11.0, 6.0, 9.0);

    private static final VoxelShape THREE_AABB = Block.box(5.0, 0.0, 6.0, 10.0, 6.0, 11.0);

    private static final VoxelShape FOUR_AABB = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 10.0);

    public CandleBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CANDLES, 1)).m_61124_(LIT, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (player3.getAbilities().mayBuild && player3.m_21120_(interactionHand4).isEmpty() && (Boolean) blockState0.m_61143_(LIT)) {
            m_151899_(player3, blockState0, level1, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return !blockPlaceContext1.m_7078_() && blockPlaceContext1.m_43722_().getItem() == this.m_5456_() && blockState0.m_61143_(CANDLES) < 4 ? true : super.m_6864_(blockState0, blockPlaceContext1);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos());
        if ($$1.m_60713_(this)) {
            return (BlockState) $$1.m_61122_(CANDLES);
        } else {
            FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
            boolean $$3 = $$2.getType() == Fluids.WATER;
            return (BlockState) super.m_5573_(blockPlaceContext0).m_61124_(WATERLOGGED, $$3);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch(blockState0.m_61143_(CANDLES)) {
            case 1:
            default:
                return ONE_AABB;
            case 2:
                return TWO_AABB;
            case 3:
                return THREE_AABB;
            case 4:
                return FOUR_AABB;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(CANDLES, LIT, WATERLOGGED);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        if (!(Boolean) blockState2.m_61143_(WATERLOGGED) && fluidState3.getType() == Fluids.WATER) {
            BlockState $$4 = (BlockState) blockState2.m_61124_(WATERLOGGED, true);
            if ((Boolean) blockState2.m_61143_(LIT)) {
                m_151899_(null, $$4, levelAccessor0, blockPos1);
            } else {
                levelAccessor0.m_7731_(blockPos1, $$4, 3);
            }
            levelAccessor0.scheduleTick(blockPos1, fluidState3.getType(), fluidState3.getType().getTickDelay(levelAccessor0));
            return true;
        } else {
            return false;
        }
    }

    public static boolean canLight(BlockState blockState0) {
        return blockState0.m_204338_(BlockTags.CANDLES, p_152810_ -> p_152810_.m_61138_(LIT) && p_152810_.m_61138_(WATERLOGGED)) && !(Boolean) blockState0.m_61143_(LIT) && !(Boolean) blockState0.m_61143_(WATERLOGGED);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState blockState0) {
        return (Iterable<Vec3>) PARTICLE_OFFSETS.get((Integer) blockState0.m_61143_(CANDLES));
    }

    @Override
    protected boolean canBeLit(BlockState blockState0) {
        return !(Boolean) blockState0.m_61143_(WATERLOGGED) && super.canBeLit(blockState0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return Block.canSupportCenter(levelReader1, blockPos2.below(), Direction.UP);
    }
}