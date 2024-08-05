package io.github.lightman314.lightmanscurrency.api.traders.blocks;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.ITallBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class TraderBlockTallRotatable extends TraderBlockRotatable implements ITallBlock {

    protected static final BooleanProperty ISBOTTOM = BlockStateProperties.BOTTOM;

    private final BiFunction<Direction, Boolean, VoxelShape> shape;

    protected TraderBlockTallRotatable(BlockBehaviour.Properties properties) {
        this(properties, LazyShapes.TALL_BOX_SHAPE);
    }

    protected TraderBlockTallRotatable(BlockBehaviour.Properties properties, VoxelShape shape) {
        this(properties, LazyShapes.lazyTallSingleShape(shape));
    }

    protected TraderBlockTallRotatable(BlockBehaviour.Properties properties, BiFunction<Direction, Boolean, VoxelShape> shape) {
        super(properties.pushReaction(PushReaction.BLOCK));
        this.shape = shape;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(ISBOTTOM, true));
    }

    @Override
    protected boolean shouldMakeTrader(BlockState state) {
        return this.getIsBottom(state);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return (VoxelShape) this.shape.apply(this.getFacing(state), this.getIsBottom(state));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ISBOTTOM);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(ISBOTTOM, true);
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity player, @Nonnull ItemStack stack) {
        if (this.isReplaceable(level, pos.above())) {
            level.setBlockAndUpdate(pos.above(), (BlockState) ((BlockState) this.m_49966_().m_61124_(ISBOTTOM, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING)));
        } else {
            if (level.getBlockEntity(pos) instanceof TraderBlockEntity<?> be) {
                be.flagAsLegitBreak();
            }
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (player instanceof Player p) {
                ItemStack giveStack = stack.copy();
                giveStack.setCount(1);
                InventoryUtil.safeGiveToPlayer(p.getInventory(), giveStack);
            }
        }
        this.setPlacedByBase(level, pos, state, player, stack);
    }

    @Deprecated
    public boolean getReplacable(Level level, BlockPos pos, BlockState ignored, LivingEntity player, ItemStack stack) {
        return level.getBlockState(pos).m_60734_() == Blocks.AIR;
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        this.playerWillDestroyBase(level, pos, state, player);
        if (this.getBlockEntity(state, level, pos) instanceof TraderBlockEntity<?> trader && !trader.canBreak(player)) {
            return;
        }
        this.setAir(level, this.getOtherHeight(pos, state), player);
    }

    @Override
    protected void onInvalidRemoval(BlockState state, Level level, BlockPos pos, TraderData trader) {
        super.onInvalidRemoval(state, level, pos, trader);
        this.setAir(level, this.getOtherHeight(pos, state), null);
    }

    protected final void setAir(Level level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        if (state.m_60734_() == this) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            if (player != null) {
                level.m_142346_(player, GameEvent.BLOCK_DESTROY, pos);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@Nonnull BlockState state, @Nonnull LevelAccessor level, @Nonnull BlockPos pos) {
        if (level == null) {
            return null;
        } else {
            return this.getIsTop(state) ? level.m_7702_(pos.below()) : level.m_7702_(pos);
        }
    }
}