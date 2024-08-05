package com.github.alexthe666.iceandfire.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockGoldPile extends Block {

    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);

    protected static final VoxelShape[] SHAPES = new VoxelShape[] { Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) };

    public BlockGoldPile() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.3F, 1.0F).randomTicks().sound(IafBlockRegistry.SOUND_TYPE_GOLD));
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, PathComputationType type) {
        switch(type) {
            case LAND:
                return (Integer) state.m_61143_(LAYERS) < 5;
            case WATER:
                return false;
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES[state.m_61143_(LAYERS)];
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES[state.m_61143_(LAYERS) - 1];
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState blockstate = worldIn.m_8055_(pos.below());
        Block block = blockstate.m_60734_();
        if (block == Blocks.ICE || block == Blocks.PACKED_ICE || block == Blocks.BARRIER) {
            return false;
        } else {
            return block != Blocks.HONEY_BLOCK && block != Blocks.SOUL_SAND ? Block.isFaceFull(blockstate.m_60812_(worldIn, pos.below()), Direction.UP) || block instanceof BlockGoldPile && (Integer) blockstate.m_61143_(LAYERS) == 8 : true;
        }
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return false;
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60734_() == this) {
            int i = (Integer) blockstate.m_61143_(LAYERS);
            return (BlockState) blockstate.m_61124_(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player playerIn, @NotNull InteractionHand handIn, @NotNull BlockHitResult resultIn) {
        ItemStack item = playerIn.getInventory().getSelected();
        if (!item.isEmpty() && item.getItem() != null && item.getItem() == this.m_5456_() && !item.isEmpty() && (Integer) state.m_61143_(LAYERS) < 8) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(LAYERS, (Integer) state.m_61143_(LAYERS) + 1), 3);
            if (!playerIn.isCreative()) {
                item.shrink(1);
                if (item.isEmpty()) {
                    playerIn.getInventory().setItem(playerIn.getInventory().selected, ItemStack.EMPTY);
                } else {
                    playerIn.getInventory().setItem(playerIn.getInventory().selected, item);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}