package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class RopeBlock extends IronBarsBlock {

    public static final BooleanProperty TIED_TO_BELL = BooleanProperty.create("tied_to_bell");

    protected static final VoxelShape LOWER_SUPPORT_AABB = Block.box(7.0, 0.0, 7.0, 9.0, 1.0, 9.0);

    public RopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52309_, false)).m_61124_(f_52311_, false)).m_61124_(f_52310_, false)).m_61124_(f_52312_, false)).m_61124_(TIED_TO_BELL, false)).m_61124_(f_52313_, false));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter world = context.m_43725_();
        BlockPos posAbove = context.getClickedPos().above();
        BlockState state = super.getStateForPlacement(context);
        return state != null ? (BlockState) state.m_61124_(TIED_TO_BELL, world.getBlockState(posAbove).m_60734_() == Blocks.BELL) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.m_21120_(hand).isEmpty()) {
            if (Configuration.ENABLE_ROPE_REELING.get() && player.isSecondaryUseActive()) {
                if (player.getAbilities().mayBuild && (player.getAbilities().instabuild || player.getInventory().add(new ItemStack(this.m_5456_())))) {
                    BlockPos.MutableBlockPos reelingPos = pos.mutable().move(Direction.DOWN);
                    int minBuildHeight = level.m_141937_();
                    while (reelingPos.m_123342_() >= minBuildHeight) {
                        BlockState blockStateBelow = level.getBlockState(reelingPos);
                        if (!blockStateBelow.m_60713_(this)) {
                            reelingPos.move(Direction.UP);
                            level.m_46953_(reelingPos, false, player);
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                        reelingPos.move(Direction.DOWN);
                    }
                }
            } else {
                BlockPos.MutableBlockPos bellRingingPos = pos.mutable().move(Direction.UP);
                for (int i = 0; i < 24; i++) {
                    BlockState blockStateAbove = level.getBlockState(bellRingingPos);
                    Block blockAbove = blockStateAbove.m_60734_();
                    if (blockAbove == Blocks.BELL) {
                        ((BellBlock) blockAbove).attemptToRing(level, bellRingingPos, ((Direction) blockStateAbove.m_61143_(BellBlock.FACING)).getClockWise());
                        return InteractionResult.SUCCESS;
                    }
                    if (blockAbove != ModBlocks.ROPE.get()) {
                        return InteractionResult.PASS;
                    }
                    bellRingingPos.move(Direction.UP);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return LOWER_SUPPORT_AABB;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.m_43722_().getItem() == this.m_5456_();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) state.m_61143_(f_52313_)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        boolean tiedToBell = (Boolean) state.m_61143_(TIED_TO_BELL);
        if (facing == Direction.UP) {
            tiedToBell = level.m_8055_(facingPos).m_60734_() == Blocks.BELL;
        }
        return facing.getAxis().isHorizontal() ? (BlockState) ((BlockState) state.m_61124_(TIED_TO_BELL, tiedToBell)).m_61124_((Property) f_52314_.get(facing), this.m_54217_(facingState, facingState.m_60783_(level, facingPos, facing.getOpposite()))) : super.updateShape((BlockState) state.m_61124_(TIED_TO_BELL, tiedToBell), facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52309_, f_52310_, f_52312_, f_52311_, f_52313_, TIED_TO_BELL);
    }
}