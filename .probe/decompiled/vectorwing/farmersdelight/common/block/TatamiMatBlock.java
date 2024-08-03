package vectorwing.farmersdelight.common.block;

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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TatamiMatBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public TatamiMatBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(PART, BedPart.FOOT));
    }

    private static Direction getDirectionToOther(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_, PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing != getDirectionToOther((BedPart) stateIn.m_61143_(PART), (Direction) stateIn.m_61143_(f_54117_))) {
            return !stateIn.m_60710_(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, level, currentPos, facingPos);
        } else {
            return stateIn.m_60710_(level, currentPos) && facingState.m_60713_(this) && facingState.m_61143_(PART) != stateIn.m_61143_(PART) ? stateIn : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !level.isEmptyBlock(pos.below());
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart part = (BedPart) state.m_61143_(PART);
            if (part == BedPart.FOOT) {
                BlockPos pairPos = pos.relative(getDirectionToOther(part, (Direction) state.m_61143_(f_54117_)));
                BlockState pairState = level.getBlockState(pairPos);
                if (pairState.m_60734_() == this && pairState.m_61143_(PART) == BedPart.HEAD) {
                    level.setBlock(pairPos, Blocks.AIR.defaultBlockState(), 35);
                    level.m_5898_(player, 2001, pairPos, Block.getId(pairState));
                }
            }
        }
        super.m_5707_(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.m_6402_(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos facingPos = pos.relative((Direction) state.m_61143_(f_54117_));
            level.setBlock(facingPos, (BlockState) state.m_61124_(PART, BedPart.HEAD), 3);
            level.m_6289_(pos, Blocks.AIR);
            state.m_60701_(level, pos, 3);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.m_43725_();
        Direction facing = context.m_8125_();
        BlockPos pairPos = context.getClickedPos().relative(facing);
        BlockState pairState = context.m_43725_().getBlockState(pairPos);
        return pairState.m_60629_(context) && this.canSurvive(pairState, level, pairPos) ? (BlockState) this.m_49966_().m_61124_(f_54117_, facing) : null;
    }
}