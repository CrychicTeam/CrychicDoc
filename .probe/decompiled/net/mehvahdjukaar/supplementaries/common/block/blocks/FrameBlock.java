package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.block.MimicBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FrameBlock extends MimicBlock implements EntityBlock, IFrameBlock {

    public static final List<Block> FRAMED_BLOCKS = new ArrayList();

    public static final BooleanProperty HAS_BLOCK = ModBlockProperties.HAS_BLOCK;

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL_0_15;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape OCCLUSION_SHAPE = Block.box(0.01, 0.01, 0.01, 15.99, 15.99, 15.99);

    private final Map<Block, Block> filledBlocks = new HashMap();

    public FrameBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> (Integer) state.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(LIGHT_LEVEL, 0)).m_61124_(HAS_BLOCK, false));
        FRAMED_BLOCKS.add(this);
    }

    public void registerFilledBlock(Block inserted, Block filled) {
        this.filledBlocks.put(inserted, filled);
    }

    @Nullable
    @Override
    public Block getFilledBlock(Block inserted) {
        return (Block) this.filledBlocks.get(inserted);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (!(adjacentBlockState.m_60734_() instanceof FrameBlock)) {
            return super.m_6104_(state, adjacentBlockState, side);
        } else {
            boolean hasBlock = (Boolean) state.m_61143_(HAS_BLOCK);
            boolean neighborHasBlock = (Boolean) adjacentBlockState.m_61143_(HAS_BLOCK);
            return hasBlock == neighborHasBlock || super.m_6104_(state, adjacentBlockState, side);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FrameBlockTile(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL, HAS_BLOCK, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        return world.getBlockEntity(pos) instanceof FrameBlockTile tile ? tile.handleInteraction(world, pos, player, hand, trace, true) : InteractionResult.PASS;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.m_61143_(HAS_BLOCK) && reader.getBlockEntity(pos) instanceof FrameBlockTile tile && !tile.getHeldBlock().m_60795_() ? Shapes.block() : OCCLUSION_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HAS_BLOCK) ? Shapes.block() : super.m_5939_(state, reader, pos, context);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.m_61143_(HAS_BLOCK) ? 0.2F : 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !(Boolean) state.m_61143_(HAS_BLOCK) || super.m_7420_(state, reader, pos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof FrameBlockTile tile) {
            tile.getHeldBlock().m_60674_(world, pos);
        }
        return 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return stateIn;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        if ((Boolean) state.m_61143_(HAS_BLOCK)) {
            return false;
        } else {
            return switch(type) {
                case LAND, AIR ->
                    true;
                case WATER ->
                    worldIn.getFluidState(pos).is(FluidTags.WATER);
            };
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_61143_(HAS_BLOCK) ? level.getMaxLightLevel() : super.m_7753_(state, level, pos);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }
}