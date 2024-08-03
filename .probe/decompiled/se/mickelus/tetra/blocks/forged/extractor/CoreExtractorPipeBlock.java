package se.mickelus.tetra.blocks.forged.extractor;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;

@ParametersAreNonnullByDefault
public class CoreExtractorPipeBlock extends TetraBlock {

    public static final DirectionProperty facingProp = BlockStateProperties.FACING;

    public static final BooleanProperty poweredProp = BooleanProperty.create("powered");

    public static final String identifier = "extractor_pipe";

    @ObjectHolder(registryName = "block", value = "tetra:extractor_pipe")
    public static CoreExtractorPipeBlock instance;

    public CoreExtractorPipeBlock() {
        super(ForgedBlockCommon.propertiesSolid);
    }

    public static boolean isPowered(Level world, BlockPos pos) {
        BlockState pipeState = world.getBlockState(pos);
        return pipeState.m_60713_(instance) && (Boolean) pipeState.m_61143_(poweredProp);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    private boolean shouldGetPower(Level world, BlockPos pos, Direction blockFacing) {
        for (Direction facing : Direction.values()) {
            if (!facing.equals(blockFacing)) {
                BlockState adjacent = world.getBlockState(pos.relative(facing));
                if (adjacent.m_60734_().equals(this) && facing.equals(((Direction) adjacent.m_61143_(facingProp)).getOpposite()) && (Boolean) adjacent.m_61143_(poweredProp)) {
                    return true;
                }
            }
        }
        return SeepingBedrockBlock.isActive(world, pos.relative(blockFacing.getOpposite()));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
        boolean getsPowered = this.shouldGetPower(world, pos, (Direction) state.m_61143_(facingProp));
        if ((Boolean) state.m_61143_(poweredProp) != getsPowered) {
            world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(poweredProp, getsPowered));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(facingProp, poweredProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) super.m_5573_(context).m_61124_(facingProp, context.m_43719_())).m_61124_(poweredProp, this.shouldGetPower(context.m_43725_(), context.getClickedPos(), context.m_43719_()));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return (BlockState) state.m_61124_(facingProp, direction.rotate((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(facingProp)));
    }
}