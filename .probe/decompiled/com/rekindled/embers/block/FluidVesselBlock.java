package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.FluidVesselBlockEntity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidVesselBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    protected static final VoxelShape VESSEL_AABB = Shapes.join(Shapes.block(), Block.box(4.0, 2.0, 4.0, 12.0, 16.0, 12.0), BooleanOp.ONLY_FIRST);

    public FluidVesselBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof FluidVesselBlockEntity vesselEntity) {
            ItemStack heldItem = player.m_21120_(hand);
            if (!heldItem.isEmpty()) {
                IFluidHandler cap = vesselEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, hit.getDirection()).orElse(null);
                if (cap != null) {
                    boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, cap);
                    if (didFill) {
                        return InteractionResult.SUCCESS;
                    }
                }
                if (heldItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
                    return InteractionResult.CONSUME_PARTIAL;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VESSEL_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.FLUID_VESSEL_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.FLUID_VESSEL_ENTITY.get(), FluidVesselBlockEntity::clientTick) : null;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pBuilder) {
        List<ItemStack> items = super.m_49635_(pState, pBuilder);
        BlockEntity blockentity = pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockentity instanceof FluidVesselBlockEntity) {
            CompoundTag nbt = blockentity.saveWithoutMetadata();
            if (nbt != null) {
                for (ItemStack stack : items) {
                    if (stack.getItem() == RegistryManager.FLUID_VESSEL_ITEM.get()) {
                        IFluidHandler cap = (IFluidHandler) stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
                        if (cap != null) {
                            cap.fill(FluidStack.loadFluidStackFromNBT(nbt), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
        return items;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) super.m_5573_(pContext).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}