package snownee.kiwi.customization.block.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.loader.KBlockComponents;

public record DirectionalComponent(boolean oppose) implements KBlockComponent {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final DirectionalComponent NORMAL = new DirectionalComponent(false);

    private static final DirectionalComponent OPPOSE = new DirectionalComponent(true);

    public static final Codec<DirectionalComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("oppose", false).forGetter(DirectionalComponent::oppose)).apply(instance, DirectionalComponent::getInstance));

    public static DirectionalComponent getInstance(boolean oppose) {
        return oppose ? OPPOSE : NORMAL;
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.DIRECTIONAL.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(FACING, Direction.DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        if (settings.customPlacement) {
            return state;
        } else {
            for (Direction direction : context.getNearestLookingDirections()) {
                BlockState blockstate = (BlockState) state.m_61124_(FACING, this.oppose ? direction : direction.getOpposite());
                if (blockstate.m_60710_(context.m_43725_(), context.getClickedPos())) {
                    return blockstate;
                }
            }
            return null;
        }
    }

    @Nullable
    @Override
    public Direction getHorizontalFacing(BlockState blockState) {
        Direction direction = (Direction) blockState.m_61143_(FACING);
        if (direction.getAxis().isHorizontal()) {
            return this.oppose ? direction.getOpposite() : direction;
        } else {
            return null;
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(FACING, pRotation.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }
}