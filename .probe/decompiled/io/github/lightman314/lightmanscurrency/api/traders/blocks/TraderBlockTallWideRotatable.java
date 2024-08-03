package io.github.lightman314.lightmanscurrency.api.traders.blocks;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IWideBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.TriFunction;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class TraderBlockTallWideRotatable extends TraderBlockTallRotatable implements IWideBlock {

    private final TriFunction<Direction, Boolean, Boolean, VoxelShape> shape;

    protected TraderBlockTallWideRotatable(BlockBehaviour.Properties properties) {
        this(properties, LazyShapes.TALL_WIDE_BOX_SHAPE);
    }

    protected TraderBlockTallWideRotatable(BlockBehaviour.Properties properties, VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        this(properties, LazyShapes.lazyTallWideDirectionalShape(north, east, south, west));
    }

    protected TraderBlockTallWideRotatable(BlockBehaviour.Properties properties, BiFunction<Direction, Boolean, VoxelShape> tallShape) {
        this(properties, LazyShapes.lazyTallWideDirectionalShape(tallShape));
    }

    protected TraderBlockTallWideRotatable(BlockBehaviour.Properties properties, TriFunction<Direction, Boolean, Boolean, VoxelShape> shape) {
        super(properties);
        this.shape = shape;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(ISBOTTOM, true)).m_61124_(ISLEFT, true));
    }

    @Override
    protected boolean shouldMakeTrader(BlockState state) {
        return this.getIsBottom(state) && this.getIsLeft(state);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shape.apply(this.getFacing(state), this.getIsBottom(state), this.getIsLeft(state));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ISLEFT);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(ISLEFT, true);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity player, @NotNull ItemStack stack) {
        BlockPos rightPos = IRotatableBlock.getRightPos(pos, this.getFacing(state));
        if (this.isReplaceable(level, rightPos) && this.isReplaceable(level, rightPos.above()) && this.isReplaceable(level, pos.above())) {
            level.setBlockAndUpdate(pos.above(), (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ISBOTTOM, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING))).m_61124_(ISLEFT, true));
            level.setBlockAndUpdate(rightPos, (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ISBOTTOM, true)).m_61124_(FACING, (Direction) state.m_61143_(FACING))).m_61124_(ISLEFT, false));
            level.setBlockAndUpdate(rightPos.above(), (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ISBOTTOM, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING))).m_61124_(ISLEFT, false));
        } else {
            if (level.getBlockEntity(pos) instanceof TraderBlockEntity<?> be) {
                be.flagAsLegitBreak();
            }
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            if (player instanceof Player p) {
                ItemStack giveStack = stack.copy();
                giveStack.setCount(1);
                InventoryUtil.safeGiveToPlayer(p.getInventory(), giveStack);
            }
        }
        this.setPlacedByBase(level, pos, state, player, stack);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        this.playerWillDestroyBase(level, pos, state, player);
        if (this.getBlockEntity(state, level, pos) instanceof TraderBlockEntity<?> trader && !trader.canBreak(player)) {
            return;
        }
        if (this.getIsBottom(state)) {
            this.setAir(level, pos.above(), player);
            BlockPos otherPos = this.getOtherSide(pos, state, this.getFacing(state));
            this.setAir(level, otherPos, player);
            this.setAir(level, otherPos.above(), player);
        } else {
            this.setAir(level, pos.below(), player);
            BlockPos otherPos = this.getOtherSide(pos, state, this.getFacing(state));
            this.setAir(level, otherPos, player);
            this.setAir(level, otherPos.below(), player);
        }
    }

    @Override
    protected void onInvalidRemoval(BlockState state, Level level, BlockPos pos, TraderData trader) {
        super.onInvalidRemoval(state, level, pos, trader);
        BlockPos otherPos = this.getOtherSide(pos, state, this.getFacing(state));
        this.setAir(level, otherPos, null);
        this.setAir(level, this.getOtherHeight(otherPos, state), null);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@Nonnull BlockState state, @Nonnull LevelAccessor level, @Nonnull BlockPos pos) {
        if (level == null) {
            return null;
        } else {
            BlockPos getPos = pos;
            if (this.getIsRight(state)) {
                getPos = IRotatableBlock.getLeftPos(pos, this.getFacing(state));
            }
            return this.getIsTop(state) ? level.m_7702_(getPos.below()) : level.m_7702_(getPos);
        }
    }
}