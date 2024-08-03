package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractFurnaceBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected AbstractFurnaceBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(LIT, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level1, blockPos2, player3);
            return InteractionResult.CONSUME;
        }
    }

    protected abstract void openContainer(Level var1, BlockPos var2, Player var3);

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (itemStack4.hasCustomHoverName()) {
            BlockEntity $$5 = level0.getBlockEntity(blockPos1);
            if ($$5 instanceof AbstractFurnaceBlockEntity) {
                ((AbstractFurnaceBlockEntity) $$5).m_58638_(itemStack4.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            BlockEntity $$5 = level1.getBlockEntity(blockPos2);
            if ($$5 instanceof AbstractFurnaceBlockEntity) {
                if (level1 instanceof ServerLevel) {
                    Containers.dropContents(level1, blockPos2, (AbstractFurnaceBlockEntity) $$5);
                    ((AbstractFurnaceBlockEntity) $$5).getRecipesToAwardAndPopExperience((ServerLevel) level1, Vec3.atCenterOf(blockPos2));
                }
                level1.updateNeighbourForOutputSignal(blockPos2, this);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level1.getBlockEntity(blockPos2));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, LIT);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level0, BlockEntityType<T> blockEntityTypeT1, BlockEntityType<? extends AbstractFurnaceBlockEntity> blockEntityTypeExtendsAbstractFurnaceBlockEntity2) {
        return level0.isClientSide ? null : m_152132_(blockEntityTypeT1, blockEntityTypeExtendsAbstractFurnaceBlockEntity2, AbstractFurnaceBlockEntity::m_155013_);
    }
}