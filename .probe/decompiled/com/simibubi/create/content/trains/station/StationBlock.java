package com.simibubi.create.content.trains.station;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.depot.SharedDepotBlockMethods;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.gui.ScreenOpener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class StationBlock extends Block implements IBE<StationBlockEntity>, IWrenchable, ProperWaterloggedBlock {

    public static final BooleanProperty ASSEMBLING = BooleanProperty.create("assembling");

    public StationBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(ASSEMBLING, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ASSEMBLING, WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.withWater(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return (Integer) this.getBlockEntityOptional(pLevel, pPos).map(ste -> ste.trainPresent ? 15 : 0).orElse(0);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        SharedDepotBlockMethods.onLanded(worldIn, entityIn);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer != null && !pPlayer.m_6144_()) {
            ItemStack itemInHand = pPlayer.m_21120_(pHand);
            if (AllItems.WRENCH.isIn(itemInHand)) {
                return InteractionResult.PASS;
            } else if (itemInHand.getItem() == Items.FILLED_MAP) {
                return this.onBlockEntityUse(pLevel, pPos, station -> {
                    if (pLevel.isClientSide) {
                        return InteractionResult.SUCCESS;
                    } else if (station.getStation() != null && station.getStation().getId() != null) {
                        if (MapItem.getSavedData(itemInHand, pLevel) instanceof StationMapData stationMapData) {
                            return !stationMapData.toggleStation(pLevel, pPos, station) ? InteractionResult.FAIL : InteractionResult.SUCCESS;
                        } else {
                            return InteractionResult.FAIL;
                        }
                    } else {
                        return InteractionResult.FAIL;
                    }
                });
            } else {
                InteractionResult result = this.onBlockEntityUse(pLevel, pPos, station -> {
                    ItemStack autoSchedule = station.getAutoSchedule();
                    if (autoSchedule.isEmpty()) {
                        return InteractionResult.PASS;
                    } else if (pLevel.isClientSide) {
                        return InteractionResult.SUCCESS;
                    } else {
                        pPlayer.getInventory().placeItemBackInInventory(autoSchedule.copy());
                        station.depotBehaviour.removeHeldItem();
                        station.notifyUpdate();
                        AllSoundEvents.playItemPickup(pPlayer);
                        return InteractionResult.SUCCESS;
                    }
                });
                if (result == InteractionResult.PASS) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.withBlockEntityDo(pLevel, pPos, be -> this.displayScreen(be, pPlayer)));
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void displayScreen(StationBlockEntity be, Player player) {
        if (player instanceof LocalPlayer) {
            GlobalStation station = be.getStation();
            BlockState blockState = be.m_58900_();
            if (station != null && blockState != null) {
                boolean assembling = blockState.m_60734_() == this && (Boolean) blockState.m_61143_(ASSEMBLING);
                ScreenOpener.open((Screen) (assembling ? new AssemblyScreen(be, station) : new StationScreen(be, station)));
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.STATION;
    }

    @Override
    public Class<StationBlockEntity> getBlockEntityClass() {
        return StationBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StationBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends StationBlockEntity>) AllBlockEntityTypes.TRACK_STATION.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}