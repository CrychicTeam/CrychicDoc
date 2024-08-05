package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.SirenLightBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SirenLightBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape SHAPE_UP = ACMath.buildShape(Block.box(4.0, 2.0, 4.0, 12.0, 10.0, 12.0), Block.box(3.0, 0.0, 3.0, 13.0, 2.0, 13.0), Block.box(6.0, 2.0, 6.0, 10.0, 8.0, 10.0));

    private static final VoxelShape SHAPE_DOWN = ACMath.buildShape(Block.box(4.0, 6.0, 4.0, 12.0, 14.0, 12.0), Block.box(3.0, 14.0, 3.0, 13.0, 16.0, 13.0));

    private static final VoxelShape SHAPE_NORTH = ACMath.buildShape(Block.box(4.0, 4.0, 6.0, 12.0, 12.0, 14.0), Block.box(3.0, 3.0, 14.0, 13.0, 13.0, 16.0));

    private static final VoxelShape SHAPE_SOUTH = ACMath.buildShape(Block.box(4.0, 4.0, 2.0, 12.0, 12.0, 10.0), Block.box(3.0, 3.0, 0.0, 13.0, 13.0, 2.0));

    private static final VoxelShape SHAPE_EAST = ACMath.buildShape(Block.box(2.0, 4.0, 4.0, 10.0, 12.0, 12.0), Block.box(0.0, 3.0, 3.0, 2.0, 13.0, 13.0));

    private static final VoxelShape SHAPE_WEST = ACMath.buildShape(Block.box(6.0, 4.0, 4.0, 14.0, 12.0, 12.0), Block.box(14.0, 3.0, 3.0, 16.0, 13.0, 13.0));

    protected SirenLightBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1.0F, 12.0F).sound(SoundType.GLASS).noOcclusion().dynamicShape().lightLevel(poweredBlockEmission(10)));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.UP)).m_61124_(POWERED, false));
    }

    private static ToIntFunction<BlockState> poweredBlockEmission(int light) {
        return state -> state.m_61143_(BlockStateProperties.POWERED) ? light : 0;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_())).m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch((Direction) state.m_61143_(FACING)) {
            case DOWN:
                return SHAPE_DOWN;
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
            default:
                return SHAPE_UP;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, ACBlockEntityRegistry.SIREN_LIGHT.get(), SirenLightBlockEntity::tick);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateState(state, worldIn, pos, blockIn);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.f_46443_) {
            this.updateState(state, worldIn, pos, state.m_60734_());
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag = (Boolean) state.m_61143_(POWERED);
        boolean flag1 = worldIn.m_276867_(pos);
        if (flag1 != flag) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(POWERED, flag1), 3);
            worldIn.updateNeighborsAt(pos.below(), this);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.m_21120_(handIn);
        if (worldIn.getBlockEntity(pos) instanceof SirenLightBlockEntity sirenLightBlock && !player.m_6144_() && heldItem.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            sirenLightBlock.setColor(dyeColor.getTextColor());
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SirenLightBlockEntity(pos, state);
    }
}