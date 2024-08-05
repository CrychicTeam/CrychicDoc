package net.blay09.mods.waystones.block;

import java.util.List;
import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class WaystoneBlockBase extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final EnumProperty<WaystoneOrigin> ORIGIN = EnumProperty.create("origin", WaystoneOrigin.class);

    public WaystoneBlockBase(BlockBehaviour.Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(ORIGIN, WaystoneOrigin.UNKNOWN));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState directionState, LevelAccessor world, BlockPos pos, BlockPos directionPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        if (!this.isDoubleBlock(state)) {
            return state;
        } else {
            DoubleBlockHalf half = (DoubleBlockHalf) state.m_61143_(HALF);
            return direction.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (directionState.m_60734_() != this || directionState.m_61143_(HALF) == half) || half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.m_60710_(world, pos) ? Blocks.AIR.defaultBlockState() : state;
        }
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (this.isDoubleBlock(state)) {
            super.m_6240_(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
        } else {
            super.m_6240_(world, player, pos, state, blockEntity, stack);
        }
    }

    private boolean isDoubleBlock(BlockState state) {
        return state.m_61138_(HALF);
    }

    protected boolean canSilkTouch() {
        return false;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        boolean isDoubleBlock = this.isDoubleBlock(state);
        DoubleBlockHalf half = isDoubleBlock ? (DoubleBlockHalf) state.m_61143_(HALF) : null;
        BlockPos offset = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        BlockEntity offsetTileEntity = isDoubleBlock ? world.getBlockEntity(offset) : null;
        boolean hasSilkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player) > 0;
        if (hasSilkTouch && this.canSilkTouch()) {
            if (blockEntity instanceof WaystoneBlockEntityBase) {
                ((WaystoneBlockEntityBase) blockEntity).setSilkTouched(true);
            }
            if (isDoubleBlock && offsetTileEntity instanceof WaystoneBlockEntityBase) {
                ((WaystoneBlockEntityBase) offsetTileEntity).setSilkTouched(true);
            }
        }
        if (isDoubleBlock) {
            BlockState offsetState = world.getBlockState(offset);
            if (offsetState.m_60734_() == this && offsetState.m_61143_(HALF) != half) {
                world.m_46953_(half == DoubleBlockHalf.LOWER ? pos : offset, false, player);
                if (!world.isClientSide && !player.getAbilities().instabuild) {
                    m_49881_(state, world, pos, blockEntity, player, player.m_21205_());
                    m_49881_(offsetState, world, offset, offsetTileEntity, player, player.m_21205_());
                }
            }
        }
        super.m_5707_(world, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, ORIGIN);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        return !PlayerWaystoneManager.mayBreakWaystone(player, world, pos) ? -1.0F : super.m_5880_(state, player, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (!this.isDoubleBlock(state)) {
            return true;
        } else if (state.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
            return true;
        } else {
            BlockState below = world.m_8055_(pos.below());
            return below.m_60734_() == this && below.m_61143_(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!PlayerWaystoneManager.mayPlaceWaystone(context.m_43723_())) {
            return null;
        } else {
            Level world = context.m_43725_();
            BlockPos pos = context.getClickedPos();
            FluidState fluidState = world.getFluidState(pos);
            return pos.m_123342_() < world.m_141928_() - 1 && world.getBlockState(pos.above()).m_60629_(context) ? (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, fluidState.getType() == Fluids.WATER) : null;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    protected void notifyObserversOfAction(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            for (Direction direction : Direction.values()) {
                BlockPos offset = pos.relative(direction);
                BlockState neighbourState = world.getBlockState(offset);
                Block neighbourBlock = neighbourState.m_60734_();
                if (neighbourBlock instanceof ObserverBlock && neighbourState.m_61143_(ObserverBlock.f_52588_) == direction.getOpposite() && !world.m_183326_().m_183582_(offset, neighbourBlock)) {
                    world.m_186460_(offset, neighbourBlock, 2);
                }
            }
        }
    }

    @Nullable
    protected InteractionResult handleEditActions(Level world, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (player.m_6144_()) {
            WaystoneEditPermissions result = PlayerWaystoneManager.mayEditWaystone(player, world, waystone);
            if (result != WaystoneEditPermissions.ALLOW) {
                if (result.getLangKey() != null) {
                    MutableComponent chatComponent = Component.translatable(result.getLangKey());
                    chatComponent.withStyle(ChatFormatting.RED);
                    player.displayClientMessage(chatComponent, true);
                }
                return InteractionResult.SUCCESS;
            } else {
                if (!world.isClientSide) {
                    MenuProvider settingsContainerProvider = tileEntity.getSettingsMenuProvider();
                    if (settingsContainerProvider != null) {
                        Balm.getNetworking().openGui(player, settingsContainerProvider);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return null;
        }
    }

    @Nullable
    protected InteractionResult handleDebugActions(Level world, Player player, InteractionHand hand, WaystoneBlockEntityBase tileEntity) {
        if (player.getAbilities().instabuild) {
            ItemStack heldItem = player.m_21120_(hand);
            if (heldItem.getItem() == Items.BAMBOO) {
                if (!world.isClientSide) {
                    tileEntity.uninitializeWaystone();
                    player.displayClientMessage(Component.literal("Waystone was successfully reset - it will re-initialize once it is next loaded."), false);
                }
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() == Items.STICK) {
                if (!world.isClientSide) {
                    player.displayClientMessage(Component.literal("Server UUID: " + tileEntity.getWaystone().getWaystoneUid()), false);
                }
                if (world.isClientSide) {
                    player.displayClientMessage(Component.literal("Client UUID: " + tileEntity.getWaystone().getWaystoneUid()), false);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }

    @Nullable
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WaystoneBlockEntityBase && (!this.canSilkTouch() || !((WaystoneBlockEntityBase) blockEntity).isSilkTouched())) {
                IWaystone waystone = ((WaystoneBlockEntityBase) blockEntity).getWaystone();
                WaystoneManager.get(world.getServer()).removeWaystone(waystone);
                PlayerWaystoneManager.removeKnownWaystone(world.getServer(), waystone);
            }
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.m_5871_(stack, world, list, flag);
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("UUID", 11)) {
            WaystoneProxy waystone = new WaystoneProxy(null, NbtUtils.loadUUID((Tag) Objects.requireNonNull(tag.get("UUID"))));
            if (waystone.isValid()) {
                this.addWaystoneNameToTooltip(list, waystone);
            }
        }
    }

    protected void addWaystoneNameToTooltip(List<Component> tooltip, WaystoneProxy waystone) {
        MutableComponent component = Component.literal(waystone.getName());
        component.withStyle(ChatFormatting.AQUA);
        tooltip.add(component);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (world.getBlockEntity(pos) instanceof WaystoneBlockEntityBase waystoneTileEntity) {
            InteractionResult result = this.handleDebugActions(world, player, hand, waystoneTileEntity);
            if (result != null) {
                return result;
            }
            IWaystone waystone = waystoneTileEntity.getWaystone();
            result = this.handleEditActions(world, player, waystoneTileEntity, waystone);
            if (result != null) {
                return result;
            }
            result = this.handleActivation(world, pos, player, waystoneTileEntity, waystone);
            if (result != null) {
                return result;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        BlockPos posAbove = pos.above();
        boolean isDoubleBlock = this.isDoubleBlock(state);
        if (isDoubleBlock) {
            FluidState fluidStateAbove = world.getFluidState(posAbove);
            world.setBlockAndUpdate(posAbove, (BlockState) ((BlockState) ((BlockState) state.m_61124_(HALF, DoubleBlockHalf.UPPER)).m_61124_(WATERLOGGED, fluidStateAbove.getType() == Fluids.WATER)).m_61124_(ORIGIN, WaystoneOrigin.PLAYER));
        }
        if (blockEntity instanceof WaystoneBlockEntityBase) {
            if (!world.isClientSide) {
                CompoundTag tag = stack.getTag();
                WaystoneProxy existingWaystone = null;
                if (tag != null && tag.contains("UUID", 11)) {
                    existingWaystone = new WaystoneProxy(world.getServer(), NbtUtils.loadUUID((Tag) Objects.requireNonNull(tag.get("UUID"))));
                }
                if (existingWaystone != null && existingWaystone.isValid() && existingWaystone.getBackingWaystone() instanceof Waystone) {
                    ((WaystoneBlockEntityBase) blockEntity).initializeFromExisting((ServerLevelAccessor) world, (Waystone) existingWaystone.getBackingWaystone(), stack);
                } else {
                    ((WaystoneBlockEntityBase) blockEntity).initializeWaystone((ServerLevelAccessor) world, placer, WaystoneOrigin.PLAYER);
                }
                if (isDoubleBlock) {
                    BlockEntity waystoneEntityAbove = world.getBlockEntity(posAbove);
                    if (waystoneEntityAbove instanceof WaystoneBlockEntityBase) {
                        ((WaystoneBlockEntityBase) waystoneEntityAbove).initializeFromBase((WaystoneBlockEntityBase) blockEntity);
                    }
                }
            }
            if (placer instanceof Player) {
                IWaystone waystone = ((WaystoneBlockEntityBase) blockEntity).getWaystone();
                PlayerWaystoneManager.activateWaystone((Player) placer, waystone);
                if (!world.isClientSide) {
                    WaystoneSyncManager.sendActivatedWaystones((Player) placer);
                }
            }
            if (!world.isClientSide && placer instanceof ServerPlayer player) {
                WaystoneBlockEntityBase waystoneTileEntity = (WaystoneBlockEntityBase) blockEntity;
                WaystoneEditPermissions result = PlayerWaystoneManager.mayEditWaystone(player, world, waystoneTileEntity.getWaystone());
                if (result == WaystoneEditPermissions.ALLOW) {
                    MenuProvider settingsContainerProvider = waystoneTileEntity.getSettingsMenuProvider();
                    if (settingsContainerProvider != null) {
                        Balm.getNetworking().openGui(player, settingsContainerProvider);
                    }
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }
}