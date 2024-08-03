package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.gui.ScreenOpener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class SequencedGearshiftBlock extends HorizontalAxisKineticBlock implements IBE<SequencedGearshiftBlockEntity>, ITransformableBlock {

    public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");

    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 5);

    public SequencedGearshiftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(STATE, VERTICAL));
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
                worldIn.m_186460_(pos, this, 0);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        boolean previouslyPowered = (Integer) state.m_61143_(STATE) != 0;
        boolean isPowered = worldIn.m_276867_(pos);
        this.withBlockEntityDo(worldIn, pos, sgte -> sgte.onRedstoneUpdate(isPowered, previouslyPowered));
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.m_61143_(VERTICAL) ? face.getAxis().isVertical() : super.hasShaftTowards(world, pos, state, face);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack held = player.m_21205_();
        if (AllItems.WRENCH.isIn(held)) {
            return InteractionResult.PASS;
        } else {
            if (held.getItem() instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) held.getItem();
                if (blockItem.getBlock() instanceof KineticBlock && this.hasShaftTowards(worldIn, pos, state, hit.getDirection())) {
                    return InteractionResult.PASS;
                }
            }
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.withBlockEntityDo(worldIn, pos, be -> this.displayScreen(be, player)));
            return InteractionResult.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void displayScreen(SequencedGearshiftBlockEntity be, Player player) {
        if (player instanceof LocalPlayer) {
            ScreenOpener.open(new SequencedGearshiftScreen(be));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis preferredAxis = RotatedPillarKineticBlock.getPreferredAxis(context);
        return preferredAxis == null || context.m_43723_() != null && context.m_43723_().m_6144_() ? this.withAxis(context.getNearestLookingDirection().getAxis(), context) : this.withAxis(preferredAxis, context);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        BlockState newState = state;
        if (context.getClickedFace().getAxis() != Direction.Axis.Y && state.m_61143_(HORIZONTAL_AXIS) != context.getClickedFace().getAxis()) {
            newState = (BlockState) state.m_61122_(VERTICAL);
        }
        return super.onWrenched(newState, context);
    }

    private BlockState withAxis(Direction.Axis axis, BlockPlaceContext context) {
        BlockState state = (BlockState) this.m_49966_().m_61124_(VERTICAL, axis.isVertical());
        return axis.isVertical() ? (BlockState) state.m_61124_(HORIZONTAL_AXIS, context.m_8125_().getAxis()) : (BlockState) state.m_61124_(HORIZONTAL_AXIS, axis);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.m_61143_(VERTICAL) ? Direction.Axis.Y : super.getRotationAxis(state);
    }

    @Override
    public Class<SequencedGearshiftBlockEntity> getBlockEntityClass() {
        return SequencedGearshiftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SequencedGearshiftBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SequencedGearshiftBlockEntity>) AllBlockEntityTypes.SEQUENCED_GEARSHIFT.get();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return (Integer) state.m_61143_(STATE);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.m_6943_(state, transform.mirror);
        }
        if (transform.rotationAxis == Direction.Axis.Y) {
            return this.m_6843_(state, transform.rotation);
        } else {
            if (transform.rotation.ordinal() % 2 == 1) {
                if (transform.rotationAxis != state.m_61143_(HORIZONTAL_AXIS)) {
                    return (BlockState) state.m_61122_(VERTICAL);
                }
                if ((Boolean) state.m_61143_(VERTICAL)) {
                    return (BlockState) ((BlockState) state.m_61122_(VERTICAL)).m_61122_(HORIZONTAL_AXIS);
                }
            }
            return state;
        }
    }
}