package se.mickelus.tetra.blocks.forged.extractor;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;

@ParametersAreNonnullByDefault
public class CoreExtractorBaseBlock extends TetraWaterloggedBlock implements EntityBlock {

    public static final String identifier = "core_extractor";

    public static final DirectionProperty facingProp = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape capShape = m_49796_(3.0, 14.0, 3.0, 13.0, 16.0, 13.0);

    private static final VoxelShape shaftShape = m_49796_(4.0, 13.0, 4.0, 12.0, 14.0, 12.0);

    private static final VoxelShape smallCoverShapeZ = m_49796_(1.0, 0.0, 0.0, 15.0, 12.0, 16.0);

    private static final VoxelShape largeCoverShapeZ = m_49796_(0.0, 0.0, 1.0, 16.0, 13.0, 15.0);

    private static final VoxelShape smallCoverShapeX = m_49796_(0.0, 0.0, 1.0, 16.0, 12.0, 15.0);

    private static final VoxelShape largeCoverShapeX = m_49796_(1.0, 0.0, 0.0, 15.0, 13.0, 16.0);

    private static final VoxelShape combinedShapeZ = Shapes.or(Shapes.joinUnoptimized(smallCoverShapeZ, largeCoverShapeZ, BooleanOp.OR), capShape, shaftShape);

    private static final VoxelShape combinedShapeX = Shapes.or(Shapes.joinUnoptimized(smallCoverShapeX, largeCoverShapeX, BooleanOp.OR), capShape, shaftShape);

    public static RegistryObject<CoreExtractorBaseBlock> instance;

    public CoreExtractorBaseBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Direction.Axis.X.equals(((Direction) state.m_61143_(facingProp)).getAxis()) ? combinedShapeX : combinedShapeZ;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("block.multiblock_hint.1x2x1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
        if (!pos.relative((Direction) world.getBlockState(pos).m_61143_(facingProp)).equals(fromPos)) {
            TileEntityOptional.from(world, pos, CoreExtractorBaseBlockEntity.class).ifPresent(CoreExtractorBaseBlockEntity::updateTransferState);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(facingProp);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (Direction.UP.equals(facing) && !facingState.m_60713_(CoreExtractorPistonBlock.instance.get())) {
            return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockState pistonState = (BlockState) CoreExtractorPistonBlock.instance.get().m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, world.getFluidState(pos.above()).getType() == Fluids.WATER);
        world.setBlock(pos.above(), pistonState, 3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43725_().getBlockState(context.getClickedPos().above()).m_60629_(context) ? (BlockState) super.getStateForPlacement(context).m_61124_(facingProp, context.m_8125_().getOpposite()) : null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return (BlockState) state.m_61124_(facingProp, direction.rotate((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(facingProp)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoreExtractorBaseBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return getTicker(entityType, CoreExtractorBaseBlockEntity.type, (lvl, pos, blockState, tile) -> tile.tick(lvl, pos, blockState));
    }
}