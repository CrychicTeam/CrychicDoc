package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import java.util.function.BiFunction;
import javax.annotation.Nonnull;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TallRotatableBlock extends RotatableBlock implements ITallBlock {

    private final BiFunction<Direction, Boolean, VoxelShape> shape;

    protected TallRotatableBlock(BlockBehaviour.Properties properties) {
        this(properties, LazyShapes.TALL_BOX_SHAPE);
    }

    protected TallRotatableBlock(BlockBehaviour.Properties properties, VoxelShape shape) {
        this(properties, LazyShapes.lazyTallSingleShape(shape));
    }

    protected TallRotatableBlock(BlockBehaviour.Properties properties, BiFunction<Direction, Boolean, VoxelShape> shape) {
        super(properties.pushReaction(PushReaction.BLOCK));
        this.shape = shape;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(ISBOTTOM, true));
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
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity player, @Nonnull ItemStack stack) {
        if (this.isReplaceable(level, pos.above())) {
            level.setBlockAndUpdate(pos.above(), (BlockState) ((BlockState) this.m_49966_().m_61124_(ISBOTTOM, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING)));
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            if (player instanceof Player) {
                ItemStack giveStack = stack.copy();
                giveStack.setCount(1);
                ((Player) player).getInventory().add(giveStack);
            }
        }
    }

    @Nonnull
    @Override
    public BlockState updateShape(@Nonnull BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (facing == Direction.UP && (Boolean) stateIn.m_61143_(ISBOTTOM) || facing == Direction.DOWN && !(Boolean) stateIn.m_61143_(ISBOTTOM)) {
            return facingState.m_60713_(this) ? stateIn : Blocks.AIR.defaultBlockState();
        } else {
            return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }
}