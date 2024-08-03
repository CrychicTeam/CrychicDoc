package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record VienInstance(int x0, int x1, int y0, int y1, int z0, int z1, int max, Predicate<BlockState> match) implements BlockBreakerInstance {

    @Override
    public List<BlockPos> find(Level level, BlockPos pos, Predicate<BlockPos> pred) {
        List<BlockPos> list = new ArrayList();
        if (!this.match.test(level.getBlockState(pos))) {
            return list;
        } else {
            Set<BlockPos> added = new HashSet();
            Queue<BlockPos> queue = new ArrayDeque();
            queue.add(pos);
            added.add(pos);
            while (!queue.isEmpty()) {
                BlockPos current = (BlockPos) queue.poll();
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            BlockPos i = current.offset(x, y, z);
                            if (!added.contains(i)) {
                                added.add(i);
                                if (i.m_123341_() - pos.m_123341_() >= this.x0 && i.m_123341_() - pos.m_123341_() <= this.x1 && i.m_123342_() - pos.m_123342_() >= this.y0 && i.m_123342_() - pos.m_123342_() <= this.y1 && i.m_123343_() - pos.m_123343_() >= this.z0 && i.m_123343_() - pos.m_123343_() <= this.z1 && this.match.test(level.getBlockState(i))) {
                                    list.add(i);
                                    queue.add(i);
                                    if (list.size() >= this.max) {
                                        return list;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return list;
        }
    }
}