package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockHummingbirdFeeder extends Block {

    public static final IntegerProperty CONTENTS = IntegerProperty.create("contents", 0, 3);

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape AABB = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    private static final VoxelShape AABB_HANGING = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public BlockHummingbirdFeeder() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.LANTERN).strength(0.5F).randomTicks().noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CONTENTS, 0)).m_61124_(HANGING, false));
    }

    @Deprecated
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HANGING) ? AABB_HANGING : AABB;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockstate = (BlockState) this.m_49966_().m_61124_(HANGING, direction == Direction.UP);
                if (blockstate.m_60710_(context.m_43725_(), context.getClickedPos())) {
                    return (BlockState) blockstate.m_61124_(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    protected static Direction getBlockConnected(BlockState state) {
        return state.m_61143_(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int contents = (Integer) state.m_61143_(CONTENTS);
        ItemStack waterBottle = AMEffectRegistry.createPotion(Potions.WATER);
        ItemStack itemStack = player.m_21120_(handIn);
        int setContent = -1;
        if (contents == 0) {
            if (itemStack.getItem() == Items.SUGAR) {
                setContent = 2;
                this.useItem(player, itemStack, false);
            } else if (itemStack.getItem() == waterBottle.getItem() && ItemStack.isSameItemSameTags(waterBottle, itemStack)) {
                setContent = 1;
                this.useItem(player, itemStack, true);
            }
        } else if (contents == 1) {
            if (itemStack.getItem() == Items.SUGAR) {
                setContent = 3;
                this.useItem(player, itemStack, false);
            }
        } else if (contents == 2 && itemStack.getItem() == waterBottle.getItem() && ItemStack.isSameItemSameTags(waterBottle, itemStack)) {
            setContent = 3;
            this.useItem(player, itemStack, true);
        }
        if (setContent >= 0) {
            worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(CONTENTS, setContent));
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    public void useItem(Player playerEntity, ItemStack stack, boolean dropBottle) {
        if (!playerEntity.isCreative()) {
            if (dropBottle) {
                playerEntity.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }
            stack.shrink(1);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = getBlockConnected(state).getOpposite();
        return Block.canSupportCenter(worldIn, pos.relative(direction), direction.getOpposite());
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return getBlockConnected(stateIn).getOpposite() == facing && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTENTS, HANGING, WATERLOGGED);
    }
}