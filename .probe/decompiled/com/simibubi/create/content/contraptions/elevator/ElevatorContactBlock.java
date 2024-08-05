package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ElevatorContactBlock extends WrenchableDirectionalBlock implements IBE<ElevatorContactBlockEntity>, ISpecialBlockItemRequirement {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty CALLING = BooleanProperty.create("calling");

    public static final BooleanProperty POWERING = BrassDiodeBlock.POWERING;

    public ElevatorContactBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(CALLING, false)).m_61124_(POWERING, false)).m_61124_(POWERED, false)).m_61124_(f_52588_, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CALLING, POWERING, POWERED));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = super.onWrenched(state, context);
        if (onWrenched != InteractionResult.SUCCESS) {
            return onWrenched;
        } else {
            Level level = context.getLevel();
            if (level.isClientSide()) {
                return onWrenched;
            } else {
                BlockPos pos = context.getClickedPos();
                state = level.getBlockState(pos);
                Direction facing = (Direction) state.m_61143_(RedstoneContactBlock.f_52588_);
                if (facing.getAxis() != Direction.Axis.Y && ElevatorColumn.get(level, new ElevatorColumn.ColumnCoords(pos.m_123341_(), pos.m_123343_(), facing)) != null) {
                    return onWrenched;
                } else {
                    level.setBlockAndUpdate(pos, BlockHelper.copyProperties(state, AllBlocks.REDSTONE_CONTACT.getDefaultState()));
                    return onWrenched;
                }
            }
        }
    }

    @Nullable
    public static ElevatorColumn.ColumnCoords getColumnCoords(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.m_8055_(pos);
        if (!AllBlocks.ELEVATOR_CONTACT.has(blockState) && !AllBlocks.REDSTONE_CONTACT.has(blockState)) {
            return null;
        } else {
            Direction facing = (Direction) blockState.m_61143_(f_52588_);
            return new ElevatorColumn.ColumnCoords(pos.m_123341_(), pos.m_123343_(), facing);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean isPowered = (Boolean) pState.m_61143_(POWERED);
            if (isPowered != pLevel.m_276867_(pPos)) {
                pLevel.setBlock(pPos, (BlockState) pState.m_61122_(POWERED), 2);
                if (!isPowered) {
                    if (!(Boolean) pState.m_61143_(CALLING)) {
                        ElevatorColumn elevatorColumn = ElevatorColumn.getOrCreate(pLevel, getColumnCoords(pLevel, pPos));
                        this.callToContactAndUpdate(elevatorColumn, pState, pLevel, pPos, true);
                    }
                }
            }
        }
    }

    public void callToContactAndUpdate(ElevatorColumn elevatorColumn, BlockState pState, Level pLevel, BlockPos pPos, boolean powered) {
        pLevel.setBlock(pPos, (BlockState) pState.m_61122_(CALLING), 2);
        for (BlockPos otherPos : elevatorColumn.getContacts()) {
            if (!otherPos.equals(pPos)) {
                BlockState otherState = pLevel.getBlockState(otherPos);
                if (AllBlocks.ELEVATOR_CONTACT.has(otherState)) {
                    pLevel.setBlock(otherPos, (BlockState) otherState.m_61124_(CALLING, false), 18);
                    this.scheduleActivation(pLevel, otherPos);
                }
            }
        }
        if (powered) {
            pState = (BlockState) pState.m_61124_(POWERED, true);
        }
        pLevel.setBlock(pPos, (BlockState) pState.m_61124_(CALLING, true), 2);
        pLevel.updateNeighborsAt(pPos, this);
        elevatorColumn.target(pPos.m_123342_());
        elevatorColumn.markDirty();
    }

    public void scheduleActivation(LevelAccessor pLevel, BlockPos pPos) {
        if (!pLevel.getBlockTicks().m_183582_(pPos, this)) {
            pLevel.scheduleTick(pPos, this, 1);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRand) {
        boolean wasPowering = (Boolean) pState.m_61143_(POWERING);
        Optional<ElevatorContactBlockEntity> optionalBE = this.getBlockEntityOptional(pLevel, pPos);
        boolean shouldBePowering = (Boolean) optionalBE.map(be -> {
            boolean activateBlock = be.activateBlock;
            be.activateBlock = false;
            be.m_6596_();
            return activateBlock;
        }).orElse(false);
        shouldBePowering |= RedstoneContactBlock.hasValidContact(pLevel, pPos, (Direction) pState.m_61143_(f_52588_));
        if (wasPowering || shouldBePowering) {
            pLevel.m_7731_(pPos, (BlockState) pState.m_61124_(POWERING, shouldBePowering), 18);
        }
        pLevel.updateNeighborsAt(pPos, this);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != stateIn.m_61143_(f_52588_)) {
            return stateIn;
        } else {
            boolean hasValidContact = RedstoneContactBlock.hasValidContact(worldIn, currentPos, facing);
            if ((Boolean) stateIn.m_61143_(POWERING) != hasValidContact) {
                this.scheduleActivation(worldIn, currentPos);
            }
            return stateIn;
        }
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return (Boolean) state.m_61143_(POWERING);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return AllBlocks.REDSTONE_CONTACT.asStack();
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return side == null ? true : state.m_61143_(f_52588_) != side.getOpposite();
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        if (side == null) {
            return 0;
        } else {
            BlockState toState = blockAccess.getBlockState(pos.relative(side.getOpposite()));
            if (toState.m_60713_(this)) {
                return 0;
            } else {
                return state.m_61143_(POWERING) ? 15 : 0;
            }
        }
    }

    @Override
    public Class<ElevatorContactBlockEntity> getBlockEntityClass() {
        return ElevatorContactBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElevatorContactBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ElevatorContactBlockEntity>) AllBlockEntityTypes.ELEVATOR_CONTACT.get();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(AllBlocks.REDSTONE_CONTACT.getDefaultState(), be);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player != null && AllItems.WRENCH.isIn(player.m_21120_(handIn))) {
            return InteractionResult.PASS;
        } else {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.withBlockEntityDo(worldIn, pos, be -> this.displayScreen(be, player)));
            return InteractionResult.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void displayScreen(ElevatorContactBlockEntity be, Player player) {
        if (player instanceof LocalPlayer) {
            ScreenOpener.open(new ElevatorContactScreen(be.m_58899_(), be.shortName, be.longName, be.doorControls.mode));
        }
    }

    public static int getLight(BlockState state) {
        return state.m_61143_(POWERING) ? 10 : 0;
    }
}