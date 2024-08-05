package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record WrappedInstance(Function<BlockPos, BlockPos> modulator, RectInstance ins) implements BlockBreakerInstance {

    @Override
    public List<BlockPos> find(Level level, BlockPos pos, Predicate<BlockPos> pred) {
        return this.ins.find(level, (BlockPos) this.modulator.apply(pos), pred);
    }
}