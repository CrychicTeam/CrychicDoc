package se.mickelus.tetra.blocks.forged.transfer;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.items.cell.ThermalCellItem;
import se.mickelus.tetra.items.forged.InsulatedPlateItem;

@ParametersAreNonnullByDefault
public class TransferUnitBlock extends TetraWaterloggedBlock implements IInteractiveBlock, EntityBlock {

    public static final String identifier = "transfer_unit";

    public static final DirectionProperty facingProp = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty plateProp = BooleanProperty.create("plate");

    public static final IntegerProperty cellProp = IntegerProperty.create("cell", 0, 2);

    public static final EnumProperty<EnumTransferConfig> configProp = EnumProperty.create("config", EnumTransferConfig.class);

    public static final EnumProperty<EnumTransferState> transferProp = EnumProperty.create("transfer", EnumTransferState.class);

    private static final ResourceLocation plateLootTable = new ResourceLocation("tetra", "forged/plate_break");

    public static final BlockInteraction[] interactions = new BlockInteraction[] { new BlockInteraction(TetraToolActions.pry, 1, Direction.SOUTH, 3.0F, 11.0F, 4.0F, 6.0F, new PropertyMatcher().where(plateProp, Predicates.equalTo(true)), TransferUnitBlock::removePlate), new BlockInteraction(TetraToolActions.hammer, 1, Direction.SOUTH, 4.0F, 10.0F, 5.0F, 9.0F, new PropertyMatcher().where(plateProp, Predicates.equalTo(false)), TransferUnitBlock::reconfigure) };

    private static final VoxelShape eastShape = m_49796_(3.0, 0.0, 1.0, 16.0, 12.0, 15.0);

    private static final VoxelShape northShape = m_49796_(1.0, 0.0, 0.0, 15.0, 12.0, 13.0);

    private static final VoxelShape westShape = m_49796_(0.0, 0.0, 1.0, 13.0, 12.0, 15.0);

    private static final VoxelShape southShape = m_49796_(1.0, 0.0, 3.0, 15.0, 12.0, 16.0);

    @ObjectHolder(registryName = "block", value = "tetra:transfer_unit")
    public static TransferUnitBlock instance;

    public TransferUnitBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(plateProp, false)).m_61124_(cellProp, 0)).m_61124_(configProp, EnumTransferConfig.send)).m_61124_(transferProp, EnumTransferState.none));
    }

    public static boolean removePlate(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction hitFace) {
        if (!world.isClientSide) {
            if (player != null) {
                BlockInteraction.dropLoot(plateLootTable, player, hand, (ServerLevel) world, blockState);
            } else {
                BlockInteraction.dropLoot(plateLootTable, (ServerLevel) world, pos, blockState);
            }
        }
        world.playSound(player, pos, SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 0.5F);
        world.setBlock(pos, (BlockState) blockState.m_61124_(plateProp, false), 3);
        return true;
    }

    public static boolean attachPlate(Level world, BlockPos pos, BlockState blockState, Player player) {
        world.playSound(player, pos, SoundEvents.METAL_PLACE, SoundSource.PLAYERS, 0.5F, 1.0F);
        world.setBlock(pos, (BlockState) blockState.m_61124_(plateProp, true), 3);
        return true;
    }

    public static boolean reconfigure(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace) {
        EnumTransferConfig config = EnumTransferConfig.getNextConfiguration((EnumTransferConfig) blockState.m_61143_(configProp));
        world.playSound(player, pos, SoundEvents.ANVIL_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
        world.setBlock(pos, (BlockState) blockState.m_61124_(configProp, config), 3);
        world.m_141902_(pos, TransferUnitBlockEntity.type.get()).ifPresent(TransferUnitBlockEntity::updateTransferState);
        return true;
    }

    public static EnumTransferConfig getEffectPowered(Level world, BlockPos pos, BlockState blockState) {
        EnumTransferConfig effect = (EnumTransferConfig) blockState.m_61143_(configProp);
        if (effect.equals(EnumTransferConfig.redstone)) {
            return world.m_276867_(pos) ? EnumTransferConfig.send : EnumTransferConfig.receive;
        } else {
            return effect;
        }
    }

    public static void setReceiving(Level world, BlockPos pos, BlockState blockState, boolean receiving) {
        if (receiving) {
            world.setBlock(pos, (BlockState) blockState.m_61124_(transferProp, EnumTransferState.receiving), 2);
        } else {
            world.setBlock(pos, (BlockState) blockState.m_61124_(transferProp, EnumTransferState.none), 2);
        }
    }

    public static boolean isReceiving(BlockState blockState) {
        return EnumTransferState.receiving.equals(blockState.m_61143_(transferProp));
    }

    public static void setSending(Level world, BlockPos pos, BlockState blockState, boolean sending) {
        if (sending) {
            world.setBlock(pos, (BlockState) blockState.m_61124_(transferProp, EnumTransferState.sending), 2);
        } else {
            world.setBlock(pos, (BlockState) blockState.m_61124_(transferProp, EnumTransferState.none), 2);
        }
    }

    public static boolean isSending(BlockState blockState) {
        return EnumTransferState.sending.equals(blockState.m_61143_(transferProp));
    }

    public static boolean hasPlate(BlockState blockState) {
        return (Boolean) blockState.m_61143_(plateProp);
    }

    public static void updateCellProp(Level world, BlockPos pos, boolean hasCell, int cellCharge) {
        BlockState blockState = world.getBlockState(pos);
        world.setBlock(pos, (BlockState) blockState.m_61124_(cellProp, hasCell ? (cellCharge > 0 ? 2 : 1) : 0), 3);
    }

    public static Direction getFacing(BlockState blockState) {
        return (Direction) blockState.m_61143_(facingProp);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        return (BlockInteraction[]) Arrays.stream(interactions).filter(interaction -> interaction.isPotentialInteraction(world, pos, blockState, (Direction) blockState.m_61143_(facingProp), face, tools)).toArray(BlockInteraction[]::new);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction blockFacing = (Direction) state.m_61143_(facingProp);
        TransferUnitBlockEntity tile = (TransferUnitBlockEntity) TileEntityOptional.from(world, pos, TransferUnitBlockEntity.class).orElse(null);
        ItemStack heldStack = player.m_21120_(hand);
        if (tile == null) {
            return InteractionResult.FAIL;
        } else {
            if (hit.getDirection().equals(Direction.UP)) {
                if (tile.hasCell()) {
                    ItemStack cell = tile.removeCell();
                    if (player.getInventory().add(cell)) {
                        player.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                    } else {
                        m_49840_(world, pos.above(), cell);
                    }
                    world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.6F);
                    world.sendBlockUpdated(pos, state, state, 3);
                    if (!player.m_9236_().isClientSide) {
                        BlockUseCriterion.trigger((ServerPlayer) player, state, ItemStack.EMPTY);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (heldStack.getItem() instanceof ThermalCellItem) {
                    tile.putCell(heldStack);
                    player.m_21008_(hand, ItemStack.EMPTY);
                    world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.5F);
                    world.sendBlockUpdated(pos, state, state, 3);
                    if (!player.m_9236_().isClientSide) {
                        BlockUseCriterion.trigger((ServerPlayer) player, state, ItemStack.EMPTY);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (blockFacing.equals(hit.getDirection().getOpposite()) && heldStack.getItem() instanceof InsulatedPlateItem && !(Boolean) state.m_61143_(plateProp)) {
                attachPlate(world, pos, state, player);
                heldStack.shrink(1);
                if (!player.m_9236_().isClientSide) {
                    BlockUseCriterion.trigger((ServerPlayer) player, state, ItemStack.EMPTY);
                }
                return InteractionResult.SUCCESS;
            }
            return BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!this.equals(newState.m_60734_())) {
            TileEntityOptional.from(world, pos, TransferUnitBlockEntity.class).ifPresent(tile -> {
                if (tile.hasCell()) {
                    Containers.dropItemStack(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), tile.getCell().copy());
                }
            });
            TileEntityOptional.from(world, pos, TransferUnitBlockEntity.class).ifPresent(BlockEntity::m_7651_);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
        if (!pos.relative((Direction) world.getBlockState(pos).m_61143_(facingProp)).equals(fromPos)) {
            TileEntityOptional.from(world, pos, TransferUnitBlockEntity.class).ifPresent(TransferUnitBlockEntity::updateTransferState);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = (Direction) state.m_61143_(facingProp);
        switch(facing) {
            case NORTH:
                return northShape;
            case EAST:
                return eastShape;
            case SOUTH:
                return southShape;
            case WEST:
                return westShape;
            default:
                return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(facingProp, configProp, plateProp, cellProp, transferProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(facingProp, context.m_8125_());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(facingProp, rotation.rotate((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(facingProp)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TransferUnitBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return !level.isClientSide ? getTicker(entityType, TransferUnitBlockEntity.type.get(), (lvl, pos, blockState, tile) -> tile.serverTick(lvl, pos, blockState)) : null;
    }
}