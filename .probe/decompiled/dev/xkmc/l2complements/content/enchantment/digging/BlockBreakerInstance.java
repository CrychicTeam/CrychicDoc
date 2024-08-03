package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface BlockBreakerInstance {

    List<BlockPos> find(Level var1, BlockPos var2, Predicate<BlockPos> var3);
}