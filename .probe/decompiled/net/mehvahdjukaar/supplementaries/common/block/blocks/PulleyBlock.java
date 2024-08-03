package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PulleyBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PulleyBlock extends RotatedPillarBlock implements EntityBlock, IRotatable {

    public static final EnumProperty<ModBlockProperties.Winding> TYPE = ModBlockProperties.WINDING;

    public static final BooleanProperty FLIPPED = ModBlockProperties.FLIPPED;

    public PulleyBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_55923_, Direction.Axis.Y)).m_61124_(TYPE, ModBlockProperties.Winding.NONE)).m_61124_(FLIPPED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE, FLIPPED);
    }

    public boolean windPulley(BlockState state, BlockPos pos, LevelAccessor world, Rotation rot, @Nullable Direction dir) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(f_55923_);
        if (axis == Direction.Axis.Y) {
            return false;
        } else {
            if (dir == null) {
                dir = axis == Direction.Axis.Z ? Direction.NORTH : Direction.WEST;
            }
            return this.rotateOverAxis(state, world, pos, rot, dir, null).isPresent();
        }
    }

    @Override
    public Optional<BlockState> getRotatedState(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, Vec3 hit) {
        Direction.Axis myAxis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
        Direction.Axis targetAxis = axis.getAxis();
        if (myAxis == targetAxis) {
            return Optional.of((BlockState) state.m_61122_(FLIPPED));
        } else if (myAxis == Direction.Axis.X) {
            return Optional.of((BlockState) state.m_61124_(f_55923_, targetAxis == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y));
        } else if (myAxis == Direction.Axis.Z) {
            return Optional.of((BlockState) state.m_61124_(f_55923_, targetAxis == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y));
        } else {
            return myAxis == Direction.Axis.Y ? Optional.of((BlockState) state.m_61124_(f_55923_, targetAxis == Direction.Axis.Z ? Direction.Axis.X : Direction.Axis.Z)) : Optional.of(state);
        }
    }

    @Override
    public void onRotated(BlockState newState, BlockState oldState, LevelAccessor world, BlockPos pos, Rotation originalRot, Direction axis, @Nullable Vec3 hit) {
        if (axis.getAxis().isHorizontal() && axis.getAxis() == oldState.m_61143_(f_55923_)) {
            Rotation rot = originalRot;
            if (world.m_7702_(pos) instanceof PulleyBlockTile pulley) {
                if (axis.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                    rot = originalRot.getRotated(Rotation.CLOCKWISE_180);
                }
                pulley.rotateDirectly(rot);
            }
            BlockPos connectedPos = pos.relative(axis);
            BlockState connected = world.m_8055_(connectedPos);
            if (connected.m_60713_(this) && newState.m_61143_(f_55923_) == connected.m_61143_(f_55923_)) {
                this.windPulley(connected, connectedPos, world, originalRot, axis);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof PulleyBlockTile tile && tile.isAccessibleBy(player)) {
            if (player instanceof ServerPlayer && (!player.m_6144_() || !this.windPulley(state, pos, worldIn, Rotation.COUNTERCLOCKWISE_90, null))) {
                player.openMenu(tile);
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level blockEntity, BlockPos pos) {
        return blockEntity.getBlockEntity(pos) instanceof MenuProvider mp ? mp : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PulleyBlockTile(pPos, pState);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof Container tile) {
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
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockUtil.addOptionalOwnership(placer, worldIn, pos);
    }
}