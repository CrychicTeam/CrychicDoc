package io.redspace.ironsspellbooks.block.inscription_table;

import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InscriptionTableBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<ChestType> PART = BlockStateProperties.CHEST_TYPE;

    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 18.0, 16.0);

    public static final VoxelShape SHAPE_TABLETOP = Block.box(0.0, 10.0, 0.0, 16.0, 14.0, 16.0);

    public static final VoxelShape SHAPE_LEG_1 = Block.box(1.0, 0.0, 1.0, 4.0, 10.0, 4.0);

    public static final VoxelShape SHAPE_LEG_2 = Block.box(12.0, 0.0, 1.0, 15.0, 10.0, 4.0);

    public static final VoxelShape SHAPE_LEG_3 = Block.box(1.0, 0.0, 12.0, 4.0, 10.0, 15.0);

    public static final VoxelShape SHAPE_LEG_4 = Block.box(12.0, 0.0, 12.0, 15.0, 10.0, 15.0);

    public static final VoxelShape SHAPE_LEGS_EAST = Shapes.or(SHAPE_LEG_2, SHAPE_LEG_4, SHAPE_TABLETOP);

    public static final VoxelShape SHAPE_LEGS_WEST = Shapes.or(SHAPE_LEG_1, SHAPE_LEG_3, SHAPE_TABLETOP);

    public static final VoxelShape SHAPE_LEGS_NORTH = Shapes.or(SHAPE_LEG_3, SHAPE_LEG_4, SHAPE_TABLETOP);

    public static final VoxelShape SHAPE_LEGS_SOUTH = Shapes.or(SHAPE_LEG_1, SHAPE_LEG_2, SHAPE_TABLETOP);

    public InscriptionTableBlock() {
        super(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD).noOcclusion());
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pos1, BlockState state1, Player pPlayer) {
        if (!pLevel.isClientSide) {
            ChestType half = (ChestType) state1.m_61143_(PART);
            BlockPos pos2 = pos1.relative(getNeighbourDirection(half, (Direction) state1.m_61143_(f_54117_)));
            BlockState state2 = pLevel.getBlockState(pos2);
            if (state2.m_60713_(this) && state2.m_61143_(PART) != state1.m_61143_(PART)) {
                pLevel.setBlock(pos2, Blocks.AIR.defaultBlockState(), 35);
                pLevel.m_5898_(pPlayer, 2001, pos2, Block.getId(state2));
            }
        }
        super.m_5707_(pLevel, pos1, state1, pPlayer);
    }

    private static Direction getNeighbourDirection(ChestType pPart, Direction pDirection) {
        return pPart == ChestType.LEFT ? pDirection.getCounterClockWise() : pDirection.getClockWise();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = ((ChestType) pState.m_61143_(PART)).equals(ChestType.RIGHT) ? (Direction) pState.m_61143_(f_54117_) : ((Direction) pState.m_61143_(f_54117_)).getOpposite();
        return switch(direction) {
            case NORTH ->
                SHAPE_LEGS_WEST;
            case SOUTH ->
                SHAPE_LEGS_EAST;
            case WEST ->
                SHAPE_LEGS_NORTH;
            default ->
                SHAPE_LEGS_SOUTH;
        };
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.m_8125_();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction.getCounterClockWise());
        Level level = pContext.m_43725_();
        return level.getBlockState(blockpos1).m_60629_(pContext) && level.getWorldBorder().isWithinBounds(blockpos1) ? (BlockState) this.m_49966_().m_61124_(f_54117_, direction.getOpposite()) : null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pPos.relative(((Direction) pState.m_61143_(f_54117_)).getClockWise());
            pLevel.setBlock(blockpos, (BlockState) pState.m_61124_(PART, ChestType.LEFT), 3);
            pLevel.setBlock(pPos, (BlockState) pState.m_61124_(PART, ChestType.RIGHT), 3);
            pLevel.m_6289_(pPos, Blocks.AIR);
            pState.m_60701_(pLevel, pPos, 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_, PART);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return ((ChestType) blockState.m_61143_(PART)).equals(ChestType.RIGHT) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.m_60750_(pLevel, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((i, inventory, player) -> new InscriptionTableMenu(i, inventory, ContainerLevelAccess.create(pLevel, pPos)), Component.translatable("block.irons_spellbooks.inscription_table"));
    }
}