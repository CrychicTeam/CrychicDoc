package org.violetmoon.quark.addons.oddities.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.block.be.MagnetBlockEntity;
import org.violetmoon.quark.addons.oddities.block.be.MagnetizedBlockBlockEntity;
import org.violetmoon.quark.addons.oddities.magnetsystem.MagnetSystem;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.zeta.api.ICollateralMover;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class MagnetBlock extends ZetaBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");

    public MagnetBlock(@Nullable ZetaModule module) {
        super("magnet", module, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).hasPostProcess(MagnetBlock::isPowered).lightLevel(state -> state.m_61143_(POWERED) ? 3 : 0));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.DOWN)).m_61124_(POWERED, false)).m_61124_(WAXED, false));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (stack.getHoverName().getString().equals("Q")) {
            tooltip.add(Component.literal("haha yes"));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, WAXED);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        super.m_6861_(state, worldIn, pos, blockIn, fromPos, isMoving);
        boolean wasPowered = (Boolean) state.m_61143_(POWERED);
        boolean isPowered = this.hasPower(worldIn, pos, (Direction) state.m_61143_(FACING));
        if (isPowered != wasPowered) {
            worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(POWERED, isPowered));
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int action, int data) {
        boolean push = action == 0;
        Direction moveDir = (Direction) state.m_61143_(FACING);
        Direction dir = push ? moveDir : moveDir.getOpposite();
        BlockPos targetPos = pos.relative(dir, data);
        BlockState targetState = world.getBlockState(targetPos);
        if (world.getBlockEntity(pos) instanceof MagnetBlockEntity be) {
            BlockPos var18 = targetPos.relative(moveDir);
            ICollateralMover.MoveResult reaction = MagnetSystem.getPushAction(be, targetPos, targetState, moveDir);
            if (reaction != ICollateralMover.MoveResult.MOVE && reaction != ICollateralMover.MoveResult.BREAK) {
                return false;
            } else {
                BlockEntity tilePresent = world.getBlockEntity(targetPos);
                CompoundTag tileData = new CompoundTag();
                if (tilePresent != null && !(tilePresent instanceof MagnetizedBlockBlockEntity)) {
                    tileData = tilePresent.saveWithFullMetadata();
                    tilePresent.setRemoved();
                }
                BlockState setState = (BlockState) MagnetsModule.magnetized_block.defaultBlockState().m_61124_(MovingMagnetizedBlock.FACING, moveDir);
                MagnetizedBlockBlockEntity movingTile = new MagnetizedBlockBlockEntity(var18, setState, targetState, tileData, moveDir);
                if (!world.isClientSide && reaction == ICollateralMover.MoveResult.BREAK) {
                    world.m_46961_(var18, true);
                }
                world.setBlock(var18, setState, 68);
                world.setBlockEntity(movingTile);
                world.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 67);
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean isPowered(BlockState state, BlockGetter pLevel, BlockPos pPos) {
        return (Boolean) state.m_61143_(POWERED);
    }

    private boolean hasPower(Level worldIn, BlockPos pos, Direction facing) {
        return worldIn.m_276867_(pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getNearestLookingDirection().getOpposite();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, facing)).m_61124_(POWERED, this.hasPower(context.m_43725_(), context.getClickedPos(), facing));
    }

    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MagnetBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, MagnetsModule.magnetType, MagnetBlockEntity::tick);
    }
}