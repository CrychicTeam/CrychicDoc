package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.be.CrafterBlockEntity;
import org.violetmoon.quark.content.automation.module.CrafterModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class CrafterBlock extends ZetaBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final EnumProperty<CrafterBlock.PowerState> POWER = EnumProperty.create("power", CrafterBlock.PowerState.class);

    public CrafterBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(POWER, CrafterBlock.PowerState.OFF));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrafterBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (world.getBlockEntity(pos) instanceof CrafterBlockEntity cbe) {
                player.openMenu(cbe);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.m_60713_(newState.m_60734_())) {
            if (world.getBlockEntity(pos) instanceof CrafterBlockEntity cbe) {
                Containers.dropContents(world, pos, cbe);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, moved);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        boolean bl = world.m_276867_(pos) || world.m_276867_(pos.above());
        world.m_7731_(pos, (BlockState) state.m_61124_(POWER, bl ? CrafterBlock.PowerState.ON : CrafterBlock.PowerState.OFF), 2);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        CrafterBlock.PowerState powerState = (CrafterBlock.PowerState) state.m_61143_(POWER);
        boolean bl = world.m_276867_(pos) || world.m_276867_(pos.above());
        boolean bl2 = powerState.powered();
        if (bl && !bl2) {
            world.m_186460_(pos, this, 6);
            ((CrafterBlockEntity) world.getBlockEntity(pos)).craft();
            world.setBlock(pos, (BlockState) state.m_61124_(POWER, CrafterBlock.PowerState.TRIGGERED), 2);
        } else if (!bl && state.m_61143_(POWER) != CrafterBlock.PowerState.OFF) {
            world.setBlock(pos, (BlockState) state.m_61124_(POWER, CrafterBlock.PowerState.OFF), 2);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return ((CrafterBlockEntity) world.getBlockEntity(pos)).getComparatorOutput();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.m_49966_().m_61124_(FACING, ctx.m_8125_().getOpposite());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CrafterModule.blockEntityType, CrafterBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER);
    }

    public static enum PowerState implements StringRepresentable {

        OFF("off"), TRIGGERED("triggered"), ON("on");

        private final String name;

        private PowerState(String name) {
            this.name = name;
        }

        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public boolean powered() {
            return this != OFF;
        }
    }
}