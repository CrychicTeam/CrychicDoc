package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.CaminiteValveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CaminiteValveEdgeBlock extends MechEdgeBlockBase implements EntityBlock {

    public static final VoxelShape X_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(1.0, 0.0, 3.0, 7.0, 16.0, 13.0), Block.box(9.0, 0.0, 3.0, 15.0, 16.0, 13.0), Block.box(4.0, 4.0, 1.0, 12.0, 12.0, 15.0));

    public static final VoxelShape Z_AABB = Shapes.or(Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0), Block.box(3.0, 0.0, 1.0, 13.0, 16.0, 7.0), Block.box(3.0, 0.0, 9.0, 13.0, 16.0, 15.0), Block.box(1.0, 4.0, 4.0, 15.0, 12.0, 12.0));

    public static final VoxelShape NORTHEAST_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 12.0, 16.0, 12.0), Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 16.0));

    public static final VoxelShape SOUTHEAST_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 12.0, 16.0, 12.0), Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 12.0));

    public static final VoxelShape SOUTHWEST_AABB = Shapes.or(Block.box(4.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 12.0));

    public static final VoxelShape NORTHWEST_AABB = Shapes.or(Block.box(4.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 16.0));

    public static final VoxelShape[] SHAPES = new VoxelShape[] { X_AABB, NORTHEAST_AABB, Z_AABB, SOUTHEAST_AABB, X_AABB, SOUTHWEST_AABB, Z_AABB, NORTHWEST_AABB };

    public CaminiteValveEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CaminiteValveBlockEntity valveEntity) {
            ItemStack heldItem = player.m_21120_(hand);
            if (!heldItem.isEmpty() && valveEntity.getReservoir() != null) {
                IFluidHandler cap = valveEntity.getReservoir().getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index];
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.CAMINITE_VALVE.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ((MechEdgeBlockBase.MechEdge) pState.m_61143_(EDGE)).corner ? null : RegistryManager.CAMINITE_VALVE_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return ((MechEdgeBlockBase.MechEdge) pState.m_61143_(EDGE)).corner ? null : BaseEntityBlock.createTickerHelper(pBlockEntityType, RegistryManager.CAMINITE_VALVE_ENTITY.get(), CaminiteValveBlockEntity::commonTick);
    }
}