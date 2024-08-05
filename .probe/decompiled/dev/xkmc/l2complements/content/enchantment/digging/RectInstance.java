package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record RectInstance(int x0, int x1, int y0, int y1, int z0, int z1) implements BlockBreakerInstance {

    @Override
    public List<BlockPos> find(Level level, BlockPos pos, Predicate<BlockPos> pred) {
        List<BlockPos> list = new ArrayList();
        for (int x = this.x0; x <= this.x1; x++) {
            for (int y = this.y0; y <= this.y1; y++) {
                for (int z = this.z0; z <= this.z1; z++) {
                    BlockPos i = pos.offset(x, y, z);
                    if (pred.test(i)) {
                        list.add(i);
                    }
                }
            }
        }
        return list;
    }
}