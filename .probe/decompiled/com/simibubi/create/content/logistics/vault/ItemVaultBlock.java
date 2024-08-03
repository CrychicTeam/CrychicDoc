package com.simibubi.create.content.logistics.vault;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.ForgeSoundType;

public class ItemVaultBlock extends Block implements IWrenchable, IBE<ItemVaultBlockEntity> {

    public static final Property<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public static final BooleanProperty LARGE = BooleanProperty.create("large");

    public static final SoundType SILENCED_METAL = new ForgeSoundType(0.1F, 1.5F, () -> SoundEvents.NETHERITE_BLOCK_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP, () -> SoundEvents.NETHERITE_BLOCK_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public ItemVaultBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(LARGE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_AXIS, LARGE);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if (pContext.m_43723_() == null || !pContext.m_43723_().m_6144_()) {
            BlockState placedOn = pContext.m_43725_().getBlockState(pContext.getClickedPos().relative(pContext.m_43719_().getOpposite()));
            Direction.Axis preferredAxis = getVaultBlockAxis(placedOn);
            if (preferredAxis != null) {
                return (BlockState) this.m_49966_().m_61124_(HORIZONTAL_AXIS, preferredAxis);
            }
        }
        return (BlockState) this.m_49966_().m_61124_(HORIZONTAL_AXIS, pContext.m_8125_().getAxis());
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.m_60734_() != pState.m_60734_()) {
            if (!pIsMoving) {
                this.withBlockEntityDo(pLevel, pPos, ItemVaultBlockEntity::updateConnectivity);
            }
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace().getAxis().isVertical()) {
            if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ItemVaultBlockEntity vault) {
                ConnectivityHandler.splitMulti(vault);
                vault.removeController(true);
            }
            state = (BlockState) state.m_61124_(LARGE, false);
        }
        return IWrenchable.super.onWrenched(state, context);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if (state.m_155947_() && (state.m_60734_() != newState.m_60734_() || !newState.m_155947_())) {
            if (!(world.getBlockEntity(pos) instanceof ItemVaultBlockEntity vaultBE)) {
                return;
            }
            ItemHelper.dropContents(world, pos, vaultBE.inventory);
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(vaultBE);
        }
    }

    public static boolean isVault(BlockState state) {
        return AllBlocks.ITEM_VAULT.has(state);
    }

    @Nullable
    public static Direction.Axis getVaultBlockAxis(BlockState state) {
        return !isVault(state) ? null : (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS);
    }

    public static boolean isLarge(BlockState state) {
        return !isVault(state) ? false : (Boolean) state.m_61143_(LARGE);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS);
        return (BlockState) state.m_61124_(HORIZONTAL_AXIS, rot.rotate(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE)).getAxis());
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }

    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        SoundType soundType = super.getSoundType(state, world, pos, entity);
        return entity != null && entity.getPersistentData().contains("SilenceVaultSound") ? SILENCED_METAL : soundType;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return (Integer) this.getBlockEntityOptional(pLevel, pPos).map(vte -> vte.getCapability(ForgeCapabilities.ITEM_HANDLER)).map(lo -> (Integer) lo.map(ItemHelper::calcRedstoneFromInventory).orElse(0)).orElse(0);
    }

    @Override
    public BlockEntityType<? extends ItemVaultBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ItemVaultBlockEntity>) AllBlockEntityTypes.ITEM_VAULT.get();
    }

    @Override
    public Class<ItemVaultBlockEntity> getBlockEntityClass() {
        return ItemVaultBlockEntity.class;
    }
}