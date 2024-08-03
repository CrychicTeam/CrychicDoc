package net.minecraft.world.level.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.PositionImpl;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class DispenserBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY = Util.make(new Object2ObjectOpenHashMap(), p_52723_ -> p_52723_.defaultReturnValue(new DefaultDispenseItemBehavior()));

    private static final int TRIGGER_DURATION = 4;

    public static void registerBehavior(ItemLike itemLike0, DispenseItemBehavior dispenseItemBehavior1) {
        DISPENSER_REGISTRY.put(itemLike0.asItem(), dispenseItemBehavior1);
    }

    protected DispenserBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(TRIGGERED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity $$6 = level1.getBlockEntity(blockPos2);
            if ($$6 instanceof DispenserBlockEntity) {
                player3.openMenu((DispenserBlockEntity) $$6);
                if ($$6 instanceof DropperBlockEntity) {
                    player3.awardStat(Stats.INSPECT_DROPPER);
                } else {
                    player3.awardStat(Stats.INSPECT_DISPENSER);
                }
            }
            return InteractionResult.CONSUME;
        }
    }

    protected void dispenseFrom(ServerLevel serverLevel0, BlockPos blockPos1) {
        BlockSourceImpl $$2 = new BlockSourceImpl(serverLevel0, blockPos1);
        DispenserBlockEntity $$3 = $$2.getEntity();
        int $$4 = $$3.getRandomSlot(serverLevel0.f_46441_);
        if ($$4 < 0) {
            serverLevel0.m_46796_(1001, blockPos1, 0);
            serverLevel0.m_220407_(GameEvent.BLOCK_ACTIVATE, blockPos1, GameEvent.Context.of($$3.m_58900_()));
        } else {
            ItemStack $$5 = $$3.m_8020_($$4);
            DispenseItemBehavior $$6 = this.getDispenseMethod($$5);
            if ($$6 != DispenseItemBehavior.NOOP) {
                $$3.m_6836_($$4, $$6.dispense($$2, $$5));
            }
        }
    }

    protected DispenseItemBehavior getDispenseMethod(ItemStack itemStack0) {
        return (DispenseItemBehavior) DISPENSER_REGISTRY.get(itemStack0.getItem());
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        boolean $$6 = level1.m_276867_(blockPos2) || level1.m_276867_(blockPos2.above());
        boolean $$7 = (Boolean) blockState0.m_61143_(TRIGGERED);
        if ($$6 && !$$7) {
            level1.m_186460_(blockPos2, this, 4);
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(TRIGGERED, true), 4);
        } else if (!$$6 && $$7) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(TRIGGERED, false), 4);
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.dispenseFrom(serverLevel1, blockPos2);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new DispenserBlockEntity(blockPos0, blockState1);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (itemStack4.hasCustomHoverName()) {
            BlockEntity $$5 = level0.getBlockEntity(blockPos1);
            if ($$5 instanceof DispenserBlockEntity) {
                ((DispenserBlockEntity) $$5).m_58638_(itemStack4.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            BlockEntity $$5 = level1.getBlockEntity(blockPos2);
            if ($$5 instanceof DispenserBlockEntity) {
                Containers.dropContents(level1, blockPos2, (DispenserBlockEntity) $$5);
                level1.updateNeighbourForOutputSignal(blockPos2, this);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    public static Position getDispensePosition(BlockSource blockSource0) {
        Direction $$1 = (Direction) blockSource0.getBlockState().m_61143_(FACING);
        double $$2 = blockSource0.x() + 0.7 * (double) $$1.getStepX();
        double $$3 = blockSource0.y() + 0.7 * (double) $$1.getStepY();
        double $$4 = blockSource0.z() + 0.7 * (double) $$1.getStepZ();
        return new PositionImpl($$2, $$3, $$4);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level1.getBlockEntity(blockPos2));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, TRIGGERED);
    }
}