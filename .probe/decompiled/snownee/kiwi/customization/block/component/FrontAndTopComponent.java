package snownee.kiwi.customization.block.component;

import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.loader.KBlockComponents;

public record FrontAndTopComponent() implements KBlockComponent {

    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    private static final FrontAndTopComponent INSTANCE = new FrontAndTopComponent();

    public static FrontAndTopComponent getInstance() {
        return INSTANCE;
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.FRONT_AND_TOP.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(ORIENTATION, FrontAndTop.NORTH_UP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        if (settings.customPlacement) {
            return state;
        } else {
            Direction front = context.m_43719_();
            Direction top;
            if (front.getAxis() == Direction.Axis.Y) {
                top = context.m_8125_();
            } else {
                top = Direction.UP;
            }
            return (BlockState) state.m_61124_(ORIENTATION, FrontAndTop.fromFrontAndTop(front, top));
        }
    }

    @Override
    public Direction getHorizontalFacing(BlockState blockState) {
        FrontAndTop frontAndTop = (FrontAndTop) blockState.m_61143_(ORIENTATION);
        return frontAndTop.front().getAxis().isHorizontal() ? frontAndTop.front() : frontAndTop.top();
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(ORIENTATION, pRotation.rotation().rotate((FrontAndTop) pState.m_61143_(ORIENTATION)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return (BlockState) pState.m_61124_(ORIENTATION, pMirror.rotation().rotate((FrontAndTop) pState.m_61143_(ORIENTATION)));
    }
}