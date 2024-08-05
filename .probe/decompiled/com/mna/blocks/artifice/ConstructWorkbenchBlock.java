package com.mna.blocks.artifice;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.blocks.tileentities.ConstructWorkbenchTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.utility.WaterloggableBlockWithOffset;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class ConstructWorkbenchBlock extends WaterloggableBlockWithOffset implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final BlockPos[] offsetsWE = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 1, 0), new BlockPos(0, 1, -1), new BlockPos(0, 1, 1), new BlockPos(0, 2, 0), new BlockPos(0, 2, -1), new BlockPos(0, 2, 1) };

    private final BlockPos[] offsetsNS = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(1, 1, 0), new BlockPos(0, 2, 0), new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0) };

    public ConstructWorkbenchBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().strength(3.0F).lightLevel(b -> 15), false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
        BlockPos pos = context.getClickedPos();
        for (BlockPos offset : this.getOffsets(state)) {
            BlockPos offsetPos = pos.offset(offset);
            if (!context.m_43725_().m_46859_(offsetPos)) {
                FluidState flState = context.m_43725_().getFluidState(offsetPos);
                if (flState == null || flState.isEmpty() || flState.getType() != Fluids.WATER && flState.getType() != Fluids.FLOWING_WATER) {
                    return null;
                }
            }
        }
        return state;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConstructWorkbenchTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.CONSTRUCT_WORKBENCH.get() ? (lvl, pos, state1, be) -> ConstructWorkbenchTile.Tick(lvl, pos, state1, (ConstructWorkbenchTile) be) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        } else {
            ItemStack activeStack = player.m_21120_(handIn);
            ConstructWorkbenchTile te = (ConstructWorkbenchTile) worldIn.getBlockEntity(pos);
            if (te == null) {
                return InteractionResult.FAIL;
            } else if (activeStack.isEmpty() && te.startCrafting(player)) {
                return InteractionResult.SUCCESS;
            } else if (!(activeStack.getItem() instanceof ItemConstructPart)) {
                ItemStack pop = te.popPart();
                if (!pop.isEmpty() && !player.addItem(pop)) {
                    player.m_19983_(pop);
                }
                return InteractionResult.SUCCESS;
            } else {
                ItemStack result = te.placePart(activeStack);
                if (result.equals(activeStack)) {
                    return InteractionResult.PASS;
                } else {
                    if (!player.isCreative()) {
                        activeStack.shrink(1);
                    }
                    if (!result.isEmpty() && !player.addItem(result)) {
                        player.m_19983_(result);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos, null);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos, @Nullable BlockEntity te) {
        if (!world.isClientSide) {
            ConstructWorkbenchTile wb = (ConstructWorkbenchTile) (te != null ? te : world.getBlockEntity(pos));
            if (wb != null) {
                for (ConstructSlot slot : ConstructSlot.values()) {
                    wb.getConstruct().getPart(slot).ifPresent(p -> {
                        ItemEntity ie = new ItemEntity(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), new ItemStack(p));
                        ie.setDefaultPickUpDelay();
                        world.m_7967_(ie);
                    });
                }
            }
        }
    }

    @Override
    protected BlockPos[] getOffsets(BlockState state) {
        Direction dir = (Direction) state.m_61143_(FACING);
        return dir != Direction.EAST && dir != Direction.WEST ? this.offsetsNS : this.offsetsWE;
    }
}