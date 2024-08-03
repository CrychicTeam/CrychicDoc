package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PedestalBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.SackItem;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends WaterBlock implements EntityBlock, WorldlyContainerHolder {

    protected static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.1875, 0.125, 0.1875, 0.815, 0.885, 0.815), Shapes.box(0.0625, 0.8125, 0.0625, 0.9375, 1.0, 0.9375), Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375));

    protected static final VoxelShape SHAPE_UP = Shapes.or(Shapes.box(0.1875, 0.125, 0.1875, 0.815, 1.0, 0.815), Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375));

    protected static final VoxelShape SHAPE_DOWN = Shapes.or(Shapes.box(0.1875, 0.0, 0.1875, 0.815, 0.885, 0.815), Shapes.box(0.0625, 0.8125, 0.0625, 0.9375, 1.0, 0.9375));

    protected static final VoxelShape SHAPE_UP_DOWN = Shapes.box(0.1875, 0.0, 0.1875, 0.815, 1.0, 0.815);

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final EnumProperty<ModBlockProperties.DisplayStatus> ITEM_STATUS = ModBlockProperties.ITEM_STATUS;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public PedestalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(UP, false)).m_61124_(AXIS, Direction.Axis.X)).m_61124_(DOWN, false)).m_61124_(WATERLOGGED, false)).m_61124_(ITEM_STATUS, ModBlockProperties.DisplayStatus.EMPTY));
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        Integer power = (Integer) CommonConfigs.Building.CRYSTAL_ENCHANTING.get();
        return power != 0 && world.m_7702_(pos) instanceof PedestalBlockTile te && te.getDisplayType() == PedestalBlockTile.DisplayType.CRYSTAL ? (float) power.intValue() : 0.0F;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, WATERLOGGED, ITEM_STATUS, AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        boolean flag = level.getFluidState(pos).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag)).m_61124_(AXIS, context.m_8125_().getAxis())).m_61124_(ITEM_STATUS, getStatus(level, pos, false))).m_61124_(UP, canConnectTo(level.getBlockState(pos.above()), pos, level, Direction.UP, false))).m_61124_(DOWN, canConnectTo(level.getBlockState(pos.below()), pos, level, Direction.DOWN, false));
    }

    public static boolean canConnectTo(BlockState state, BlockPos pos, LevelAccessor world, Direction dir, boolean hasItem) {
        if (state.m_60734_() instanceof PedestalBlock) {
            if (dir == Direction.DOWN) {
                return !((ModBlockProperties.DisplayStatus) state.m_61143_(ITEM_STATUS)).hasTile();
            }
            if (dir == Direction.UP) {
                return !hasItem;
            }
        }
        return false;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
        if (facing == Direction.UP) {
            boolean hasItem = ((ModBlockProperties.DisplayStatus) stateIn.m_61143_(ITEM_STATUS)).hasItem();
            return (BlockState) ((BlockState) stateIn.m_61124_(ITEM_STATUS, getStatus(level, currentPos, hasItem))).m_61124_(UP, canConnectTo(facingState, currentPos, level, facing, hasItem));
        } else {
            return facing == Direction.DOWN ? (BlockState) stateIn.m_61124_(DOWN, canConnectTo(facingState, currentPos, level, facing, ((ModBlockProperties.DisplayStatus) stateIn.m_61143_(ITEM_STATUS)).hasItem())) : stateIn;
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target.getLocation().y() > (double) (pos.m_123342_() + 1) - 0.1875 && world.getBlockEntity(pos) instanceof ItemDisplayTile tile) {
            ItemStack i = tile.getDisplayedItem();
            if (!i.isEmpty()) {
                return i;
            }
        }
        return super.m_7397_(world, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        InteractionResult resultType = InteractionResult.PASS;
        if (((ModBlockProperties.DisplayStatus) state.m_61143_(ITEM_STATUS)).hasTile() && level.getBlockEntity(pos) instanceof PedestalBlockTile tile && tile.isAccessibleBy(player)) {
            ItemStack handItem = player.m_21120_(handIn);
            if (handItem.getItem() instanceof SackItem) {
                ItemStack it = handItem.copy();
                it.setCount(1);
                ItemStack removed = tile.m_8016_(0);
                tile.setDisplayedItem(it);
                if (!player.isCreative()) {
                    handItem.shrink(1);
                }
                if (!level.isClientSide()) {
                    player.m_21008_(handIn, removed);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.95F);
                    tile.m_6596_();
                } else {
                    tile.updateClientVisualsOnLoad();
                }
                resultType = InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                resultType = tile.interact(player, handIn);
            }
        }
        return resultType;
    }

    public static boolean canHaveItemAbove(LevelAccessor level, BlockPos pos) {
        BlockState above = level.m_8055_(pos.above());
        return !above.m_60713_((Block) ModRegistry.PEDESTAL.get()) && !above.m_60796_(level, pos.above());
    }

    public static ModBlockProperties.DisplayStatus getStatus(LevelAccessor level, BlockPos pos, boolean hasItem) {
        if (hasItem) {
            return ModBlockProperties.DisplayStatus.FULL;
        } else {
            return canHaveItemAbove(level, pos) ? ModBlockProperties.DisplayStatus.EMPTY : ModBlockProperties.DisplayStatus.NONE;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean up = (Boolean) state.m_61143_(UP);
        boolean down = (Boolean) state.m_61143_(DOWN);
        if (!up) {
            return !down ? SHAPE : SHAPE_DOWN;
        } else {
            return !down ? SHAPE_UP : SHAPE_UP_DOWN;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ((ModBlockProperties.DisplayStatus) pState.m_61143_(ITEM_STATUS)).hasTile() ? new PedestalBlockTile(pPos, pState) : null;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof ItemDisplayTile tile) {
                Containers.dropContents(world, pos, tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof PedestalBlockTile tile) {
            return tile.m_7983_() ? 0 : 15;
        } else {
            return 0;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.CLOCKWISE_180) {
            return state;
        } else {
            return switch((Direction.Axis) state.m_61143_(AXIS)) {
                case Z ->
                    (BlockState) state.m_61124_(AXIS, Direction.Axis.X);
                case X ->
                    (BlockState) state.m_61124_(AXIS, Direction.Axis.Z);
                default ->
                    state;
            };
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockUtil.addOptionalOwnership(placer, world, pos);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return (WorldlyContainer) (((ModBlockProperties.DisplayStatus) state.m_61143_(ITEM_STATUS)).hasTile() ? (PedestalBlockTile) level.m_7702_(pos) : new PedestalBlock.TileLessContainer(state, level, pos));
    }

    @Deprecated(forRemoval = true)
    static class TileLessContainer extends SimpleContainer implements WorldlyContainer {

        private final BlockState state;

        private final LevelAccessor level;

        private final BlockPos pos;

        private PedestalBlockTile tileReference = null;

        public TileLessContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
            super(1);
            this.state = blockState;
            this.level = levelAccessor;
            this.pos = blockPos;
        }

        @Override
        public boolean stillValid(Player player) {
            return this.tileReference == null;
        }

        @Override
        public ItemStack getItem(int slot) {
            return this.tileReference != null ? this.tileReference.m_8020_(slot) : super.getItem(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            return this.tileReference != null ? this.tileReference.m_7407_(slot, amount) : super.removeItem(slot, amount);
        }

        @Override
        public boolean isEmpty() {
            return this.tileReference != null ? this.tileReference.m_7983_() : super.isEmpty();
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return this.tileReference != null ? this.tileReference.m_8016_(slot) : super.removeItemNoUpdate(slot);
        }

        @Override
        public void clearContent() {
            if (this.tileReference != null) {
                this.tileReference.m_6211_();
            } else {
                super.clearContent();
            }
        }

        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            return this.tileReference != null ? this.tileReference.m_7013_(index, stack) : super.m_7013_(index, stack);
        }

        @Override
        public int getMaxStackSize() {
            return this.tileReference != null ? this.tileReference.m_6893_() : 1;
        }

        @Override
        public int[] getSlotsForFace(Direction side) {
            return this.tileReference != null ? this.tileReference.m_7071_(side) : new int[] { 0 };
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
            return this.tileReference != null ? this.canTakeItemThroughFace(index, itemStack, direction) : true;
        }

        @Override
        public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
            return this.tileReference != null ? this.tileReference.canTakeItemThroughFace(index, stack, direction) : !this.isEmpty();
        }

        @Override
        public void setChanged() {
            if (!this.isEmpty()) {
                ItemStack item = this.getItem(0);
                if (!item.isEmpty()) {
                    this.level.m_7731_(this.pos, (BlockState) this.state.m_61124_(PedestalBlock.ITEM_STATUS, ModBlockProperties.DisplayStatus.EMPTY), 3);
                }
                if (this.level.m_7702_(this.pos) instanceof PedestalBlockTile tile) {
                    this.tileReference = tile;
                    tile.setDisplayedItem(item);
                }
            }
        }
    }
}