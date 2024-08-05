package dev.architectury.hooks.block;

import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityHooks {

    private BlockEntityHooks() {
    }

    public static void syncData(BlockEntity entity) {
        if (Objects.requireNonNull(entity.getLevel()) instanceof ServerLevel level) {
            level.getChunkSource().blockChanged(entity.getBlockPos());
        } else {
            throw new IllegalStateException("Cannot call syncData() on the logical client! Did you check level.isClientSide first?");
        }
    }
}