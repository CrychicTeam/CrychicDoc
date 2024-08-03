package fuzs.puzzleslib.api.block.v1;

import java.util.function.Consumer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface TickingEntityBlock extends EntityBlock {

    <T extends BlockEntity & TickingBlockEntity> BlockEntityType<T> getBlockEntityType();

    @Nullable
    @Override
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == this.getBlockEntityType()) {
            Consumer<TickingBlockEntity> ticker = level.isClientSide ? TickingBlockEntity::clientTick : TickingBlockEntity::serverTick;
            return ($, blockPos, blockState, blockEntity) -> ticker.accept((TickingBlockEntity) blockEntity);
        } else {
            return null;
        }
    }
}