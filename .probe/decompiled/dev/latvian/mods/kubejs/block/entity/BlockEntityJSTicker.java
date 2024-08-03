package dev.latvian.mods.kubejs.block.entity;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public record BlockEntityJSTicker(BlockEntityInfo info, int frequency, int offset, BlockEntityCallback callback, boolean server) implements BlockEntityTicker<BlockEntityJS> {

    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntityJS e) {
        if (this.frequency > 1 && e.tick % this.frequency != this.offset) {
            e.postTick(false);
        } else {
            try {
                this.callback.accept(e);
            } catch (Exception var6) {
                if (this.server) {
                    ConsoleJS.SERVER.error("Error while ticking KubeJS block entity '" + this.info.blockBuilder.id + "'", var6);
                } else {
                    ConsoleJS.CLIENT.error("Error while ticking KubeJS block entity '" + this.info.blockBuilder.id + "'", var6);
                }
            }
            e.postTick(true);
        }
    }
}