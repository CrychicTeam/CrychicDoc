package com.mna.blocks.runeforging;

import com.mna.api.blocks.IManaweaveNotifiable;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.PedestalTile;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PedestalBlock extends WaterloggableBlock implements ICutoutBlock, EntityBlock, IManaweaveNotifiable {

    public static final BooleanProperty SHOW_SIGN = BooleanProperty.create("show_sign");

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public static final BooleanProperty BRIMSTONE = BooleanProperty.create("brimstone");

    private static final Direction[] CONNECTION_DIRS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH };

    private static final VoxelShape SHAPE = Block.box(1.5, 0.0, 1.5, 14.5, 16.0, 14.5);

    private final boolean showSign;

    public PedestalBlock(boolean withSign) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).noOcclusion().sound(SoundType.STONE), false);
        this.showSign = withSign;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, Direction.NORTH)).m_61124_(SHOW_SIGN, this.showSign)).m_61124_(CONNECTED, false)).m_61124_(PipeBlock.NORTH, false)).m_61124_(PipeBlock.SOUTH, false)).m_61124_(PipeBlock.EAST, false)).m_61124_(PipeBlock.WEST, false)).m_61124_(BRIMSTONE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING, SHOW_SIGN, CONNECTED, PipeBlock.NORTH, PipeBlock.SOUTH, PipeBlock.EAST, PipeBlock.WEST, BRIMSTONE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalTile(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack activeStack = player.m_21120_(handIn);
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof PedestalTile te) {
                if (!te.m_7983_()) {
                    ItemStack stack = te.removeItem(0, 1);
                    if (!player.addItem(stack)) {
                        player.drop(stack, true);
                    }
                } else if (!activeStack.isEmpty() && te.m_7983_()) {
                    ItemStack single = activeStack.copy();
                    single.setCount(1);
                    te.setItem(0, single);
                    if (!player.isCreative()) {
                        activeStack.shrink(1);
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof PedestalTile pt) {
            ItemStack displayed = pt.getDisplayedItem();
            if (!displayed.isEmpty()) {
                if (MATags.isItemIn(displayed.getItem(), MATags.Items.BRIGHT_PEDESTAL_ITEMS)) {
                    return 15;
                }
                if (MATags.isItemIn(displayed.getItem(), MATags.Items.DIM_PEDESTAL_ITEMS)) {
                    return 10;
                }
            }
        }
        return 0;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    private BlockState updateStateBasedOnNeighbors(LevelReader world, BlockPos pos, BlockState state) {
        state = (BlockState) state.m_61124_(PipeBlock.EAST, false);
        state = (BlockState) state.m_61124_(PipeBlock.WEST, false);
        state = (BlockState) state.m_61124_(PipeBlock.NORTH, false);
        state = (BlockState) state.m_61124_(PipeBlock.SOUTH, false);
        state = (BlockState) state.m_61124_(CONNECTED, false);
        state = (BlockState) state.m_61124_(BRIMSTONE, false);
        for (Direction offset : CONNECTION_DIRS) {
            BlockState neighborState = world.m_8055_(pos.offset(offset.getNormal()));
            state = this.calculateStateForSingleNeighbor(state, neighborState, offset);
        }
        return state;
    }

    private BlockState calculateStateForSingleNeighbor(BlockState state, BlockState neighborState, Direction offset) {
        if (neighborState.m_60734_() == BlockInit.RUNEFORGE.get()) {
            Direction neighborDir = (Direction) neighborState.m_61143_(RuneforgeBlock.FACING);
            boolean didConnect = false;
            if (neighborDir != Direction.EAST && neighborDir != Direction.WEST) {
                if (offset == Direction.EAST) {
                    state = (BlockState) state.m_61124_(PipeBlock.EAST, true);
                    didConnect = true;
                } else if (offset == Direction.WEST) {
                    state = (BlockState) state.m_61124_(PipeBlock.WEST, true);
                    didConnect = true;
                }
            } else if (offset == Direction.NORTH) {
                state = (BlockState) state.m_61124_(PipeBlock.NORTH, true);
                didConnect = true;
            } else if (offset == Direction.SOUTH) {
                state = (BlockState) state.m_61124_(PipeBlock.SOUTH, true);
                didConnect = true;
            }
            if (didConnect) {
                state = (BlockState) state.m_61124_(CONNECTED, true);
                if ((Integer) neighborState.m_61143_(RuneforgeBlock.MATERIAL) == 1) {
                    state = (BlockState) state.m_61124_(BRIMSTONE, true);
                }
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        stateIn = (BlockState) stateIn.m_61124_(PipeBlock.EAST, false);
        stateIn = (BlockState) stateIn.m_61124_(PipeBlock.WEST, false);
        stateIn = (BlockState) stateIn.m_61124_(PipeBlock.NORTH, false);
        stateIn = (BlockState) stateIn.m_61124_(PipeBlock.SOUTH, false);
        stateIn = (BlockState) stateIn.m_61124_(CONNECTED, false);
        stateIn = (BlockState) stateIn.m_61124_(BRIMSTONE, false);
        stateIn = this.calculateStateForSingleNeighbor(stateIn, facingState, facing);
        stateIn = this.updateStateBasedOnNeighbors(worldIn, currentPos, stateIn);
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        } else {
            state = this.updateStateBasedOnNeighbors(context.m_43725_(), context.getClickedPos(), state);
            return (BlockState) state.m_61124_(HorizontalDirectionalBlock.FACING, context.m_8125_().getOpposite());
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean isBreak) {
        BlockState updateState = this.updateStateBasedOnNeighbors(world, pos, state);
        if (!world.isClientSide()) {
            world.setBlock(pos, updateState, 3);
        }
        super.m_6861_(state, world, pos, block, neighborPos, isBreak);
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        if (world.m_8055_(neighbor).m_60734_() == BlockInit.RUNEFORGE.get()) {
            state.m_60690_((Level) world, pos, world.m_8055_(neighbor).m_60734_(), neighbor, false);
        }
        super.onNeighborChange(state, world, pos, neighbor);
    }

    private void dropInventory(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof PedestalTile) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return te != null && te instanceof PedestalTile ? AbstractContainerMenu.getRedstoneSignalFromContainer((PedestalTile) te) : 0;
    }

    @Override
    public boolean notify(Level world, BlockPos pos, BlockState state, List<IManaweavePattern> patterns, LivingEntity activator) {
        if (!world.isClientSide) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te != null && te instanceof PedestalTile && ((PedestalTile) te).m_8020_(0).getItem() == Items.GLASS_BOTTLE) {
                ItemStack bottle = new ItemStack(ItemInit.MANAWEAVE_BOTTLE.get());
                ItemManaweaveBottle.setPattern((IManaweavePattern) patterns.get(0), bottle);
                ((PedestalTile) te).setItem(0, bottle);
                world.sendBlockUpdated(pos, state, state, 3);
                return true;
            }
        }
        return false;
    }
}