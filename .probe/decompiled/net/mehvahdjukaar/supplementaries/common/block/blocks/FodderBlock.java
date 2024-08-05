package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Arrays;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FodderBlock extends WaterBlock {

    private static final int MAX_LAYERS = 8;

    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[8];

    public FodderBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LAYERS, 8)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockGetter, BlockPos pos, PathComputationType pathType) {
        return pathType == PathComputationType.LAND ? (Integer) state.m_61143_(LAYERS) <= 4 : false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[state.m_61143_(LAYERS) - 1];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos otherPos) {
        if (facingState.m_60713_(this)) {
            if (direction == Direction.UP) {
                int layers = (Integer) state.m_61143_(LAYERS);
                int missing = 8 - layers;
                if (missing > 0) {
                    int otherLayers = (Integer) facingState.m_61143_(LAYERS);
                    int newOtherLayers = otherLayers - missing;
                    BlockState newOtherState;
                    if (newOtherLayers <= 0) {
                        newOtherState = Blocks.AIR.defaultBlockState();
                    } else {
                        newOtherState = (BlockState) facingState.m_61124_(LAYERS, newOtherLayers);
                    }
                    BlockState newState = (BlockState) state.m_61124_(LAYERS, layers + otherLayers - Math.max(0, newOtherLayers));
                    world.m_7731_(currentPos, newState, 0);
                    world.m_7731_(otherPos, newOtherState, 0);
                    return newState;
                }
            } else if (direction == Direction.DOWN) {
                int layers = (Integer) facingState.m_61143_(LAYERS);
                int missing = 8 - layers;
                if (missing > 0) {
                    int myLayers = (Integer) state.m_61143_(LAYERS);
                    int myNewLayers = myLayers - missing;
                    BlockState myNewState;
                    if (myNewLayers <= 0) {
                        myNewState = Blocks.AIR.defaultBlockState();
                    } else {
                        myNewState = (BlockState) state.m_61124_(LAYERS, myNewLayers);
                    }
                    world.m_7731_(otherPos, (BlockState) state.m_61124_(LAYERS, layers + myLayers - Math.max(0, myNewLayers)), 0);
                    return myNewState;
                }
            }
        }
        return super.updateShape(state, direction, facingState, world, currentPos, otherPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60713_(this)) {
            int i = (Integer) blockstate.m_61143_(LAYERS);
            return (BlockState) blockstate.m_61124_(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LAYERS);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.getItem() instanceof HoeItem) {
            world.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                int layers = (Integer) state.m_61143_(LAYERS);
                if (layers > 1) {
                    world.m_46796_(2001, pos, Block.getId(state));
                    world.setBlock(pos, (BlockState) state.m_61124_(LAYERS, layers - 1), 11);
                } else {
                    world.m_46961_(pos, false);
                }
                stack.hurtAndBreak(1, player, e -> e.m_21190_(hand));
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    static {
        Arrays.setAll(SHAPE_BY_LAYER, l -> Block.box(0.0, 0.0, 0.0, 16.0, (double) l * 2.0 + 2.0, 16.0));
    }
}