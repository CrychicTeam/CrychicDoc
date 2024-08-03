package snownee.kiwi.customization.block.component;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.loader.KBlockComponents;

public class WaterLoggableComponent implements KBlockComponent {

    private static final WaterLoggableComponent INSTANCE = new WaterLoggableComponent();

    public static WaterLoggableComponent getInstance() {
        return INSTANCE;
    }

    protected WaterLoggableComponent() {
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.WATER_LOGGABLE.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        Preconditions.checkState(block instanceof SimpleWaterloggedBlock, "Block must implement CheckedWaterloggedBlock");
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, false);
    }

    @Override
    public BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        FluidState fluidState = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, fluidState.getType().isSame(Fluids.WATER));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return pState;
    }
}