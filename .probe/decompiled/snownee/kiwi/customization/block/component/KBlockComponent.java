package snownee.kiwi.customization.block.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.CustomizationRegistries;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.behavior.BlockBehaviorRegistry;

public interface KBlockComponent {

    Codec<KBlockComponent> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> CustomizationRegistries.BLOCK_COMPONENT.byNameCodec().dispatch(KBlockComponent::type, KBlockComponent.Type::codec));

    KBlockComponent.Type<?> type();

    void injectProperties(Block var1, StateDefinition.Builder<Block, BlockState> var2);

    BlockState registerDefaultState(BlockState var1);

    @Nullable
    default BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        return state;
    }

    default BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        return pState;
    }

    default BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState;
    }

    default BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState;
    }

    default boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    default boolean hasAnalogOutputSignal() {
        return false;
    }

    default int getAnalogOutputSignal(BlockState state) {
        return 0;
    }

    default void addBehaviors(BlockBehaviorRegistry registry) {
    }

    @Nullable
    default Boolean canBeReplaced(BlockState blockState, BlockPlaceContext context) {
        return null;
    }

    @Nullable
    default Direction getHorizontalFacing(BlockState blockState) {
        return null;
    }

    public static record Type<T extends KBlockComponent>(Codec<T> codec) {

        public String toString() {
            return "KBlockComponent.Type[" + CustomizationRegistries.BLOCK_COMPONENT.getKey(this) + "]";
        }
    }
}