package se.mickelus.tetra.blocks.forged;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.InitializableBlock;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.properties.IToolProvider;

@ParametersAreNonnullByDefault
public class ForgedCrateBlock extends FallingBlock implements InitializableBlock, IInteractiveBlock, SimpleWaterloggedBlock {

    public static final DirectionProperty propFacing = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty propStacked = BooleanProperty.create("stacked");

    public static final IntegerProperty propIntegrity = IntegerProperty.create("integrity", 0, 3);

    public static final ResourceLocation interactionLootTable = new ResourceLocation("tetra", "forged/crate_content");

    public static final String identifier = "forged_crate";

    static final BlockInteraction[] interactions = new BlockInteraction[] { new BlockInteraction(TetraToolActions.pry, 1, Direction.EAST, 6.0F, 8.0F, 6.0F, 8.0F, BlockStatePredicate.ANY, ForgedCrateBlock::attemptBreakPry), new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 1.0F, 4.0F, 1.0F, 4.0F, BlockStatePredicate.ANY, ForgedCrateBlock::attemptBreakHammer), new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 10.0F, 13.0F, 10.0F, 13.0F, BlockStatePredicate.ANY, ForgedCrateBlock::attemptBreakHammer) };

    private static final VoxelShape shape = m_49796_(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    private static final VoxelShape[] shapesNormal = new VoxelShape[4];

    private static final VoxelShape[] shapesOffset = new VoxelShape[4];

    public ForgedCrateBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5.0F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(propFacing, Direction.EAST)).m_61124_(propStacked, false)).m_61124_(propIntegrity, 3)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    private static boolean attemptBreakHammer(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        return attemptBreak(world, pos, blockState, player, hand, player.m_21120_(hand), TetraToolActions.hammer, 2, 1);
    }

    private static boolean attemptBreakPry(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        return attemptBreak(world, pos, blockState, player, hand, player.m_21120_(hand), TetraToolActions.pry, 0, 2);
    }

    private static boolean attemptBreak(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, ItemStack itemStack, ToolAction toolAction, int min, int multiplier) {
        if (player == null) {
            return false;
        } else {
            int integrity = (Integer) blockState.m_61143_(propIntegrity);
            int progress = (Integer) CastOptional.cast(itemStack.getItem(), IToolProvider.class).map(item -> item.getToolLevel(itemStack, toolAction)).map(level -> (level - min) * multiplier).orElse(1);
            if (integrity - progress >= 0) {
                if (TetraToolActions.hammer.equals(toolAction)) {
                    world.playSound(player, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 1.0F, 0.5F);
                } else {
                    world.playSound(player, pos, SoundEvents.LADDER_STEP, SoundSource.PLAYERS, 0.7F, 2.0F);
                }
                world.setBlockAndUpdate(pos, (BlockState) blockState.m_61124_(propIntegrity, integrity - progress));
            } else {
                boolean didBreak = EffectHelper.breakBlock(world, player, itemStack, pos, blockState, false, false);
                if (didBreak && world instanceof ServerLevel) {
                    BlockInteraction.getLoot(interactionLootTable, player, hand, (ServerLevel) world, blockState).forEach(lootStack -> m_49840_(world, pos, lootStack));
                }
            }
            return true;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState state, Direction face, Collection<ToolAction> tools) {
        return interactions;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(propFacing, propStacked, propIntegrity, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) ((BlockState) super.m_5573_(context).m_61124_(propFacing, context.m_8125_())).m_61124_(propStacked, this.equals(context.m_43725_().getBlockState(context.getClickedPos().below()).m_60734_()))).m_61124_(BlockStateProperties.WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return Direction.DOWN.equals(facing) ? (BlockState) super.updateShape(state, facing, facingState, world, currentPos, facingPos).m_61124_(propStacked, this.equals(facingState.m_60734_())) : super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return !state.m_61143_(propStacked) ? shapesNormal[((Direction) state.m_61143_(propFacing)).get2DDataValue()] : shapesOffset[((Direction) state.m_61143_(propFacing)).get2DDataValue()];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(propFacing, rotation.rotate((Direction) state.m_61143_(propFacing)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(propFacing)));
    }

    static {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            shapesNormal[dir.get2DDataValue()] = shape.move((double) ((float) dir.getStepX() / 16.0F), (double) ((float) dir.getStepY() / 16.0F), (double) ((float) dir.getStepZ() / 16.0F));
            shapesOffset[dir.get2DDataValue()] = shapesNormal[dir.get2DDataValue()].move(0.0, -0.125, 0.0);
        }
    }
}