package io.redspace.ironsspellbooks.block.scroll_forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScrollForgeBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public static final VoxelShape SHAPE_TABLETOP = Block.box(0.0, 10.0, 0.0, 16.0, 14.0, 16.0);

    public static final VoxelShape SHAPE_LEG_1 = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public static final VoxelShape SHAPE_LEG_2 = Block.box(4.0, 4.0, 4.0, 12.0, 10.0, 12.0);

    public static final VoxelShape SHAPE = Shapes.or(SHAPE_LEG_1, SHAPE_LEG_2, SHAPE_TABLETOP);

    public ScrollForgeBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE).noOcclusion().sound(SoundType.NETHERITE_BLOCK));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(FACING, pRotation.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pos);
            if (!(entity instanceof ScrollForgeTile)) {
                throw new IllegalStateException("Our Container provider is missing!");
            }
            NetworkHooks.openScreen((ServerPlayer) player, (ScrollForgeTile) entity, pos);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.m_60734_() != pNewState.m_60734_()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ScrollForgeTile) {
                ((ScrollForgeTile) blockEntity).drops();
            }
        }
        super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollForgeTile(pos, state);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }
}