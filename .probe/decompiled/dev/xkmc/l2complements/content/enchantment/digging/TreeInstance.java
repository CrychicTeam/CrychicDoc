package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record TreeInstance(int x0, int x1, int y0, int y1, int z0, int z1, int max, int lv, Function<BlockState, Integer> match) implements BlockBreakerInstance {

    @Override
    public List<BlockPos> find(Level level, BlockPos pos, Predicate<BlockPos> pred) {
        int rank = (Integer) this.match.apply(level.getBlockState(pos));
        Set<BlockPos> added = new HashSet();
        List<BlockPos> l2 = new ArrayList();
        List<BlockPos> l1 = new ArrayList();
        if (rank <= 0) {
            return l1;
        } else {
            Queue<BlockPos> q2 = new ArrayDeque();
            Queue<BlockPos> q1 = new ArrayDeque();
            BlockPos npos = pos;
            if (rank == 2) {
                while (this.match.apply(level.getBlockState(npos.above())) == 2 && npos.m_123342_() - pos.m_123342_() < this.y1) {
                    npos = npos.above();
                }
            }
            (rank == 2 ? q2 : q1).add(npos);
            while (!q2.isEmpty()) {
                BlockPos current = (BlockPos) q2.poll();
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            BlockPos i = current.offset(x, y, z);
                            if (!added.contains(i)) {
                                added.add(i);
                                if (i.m_123341_() - pos.m_123341_() >= this.x0 && i.m_123341_() - pos.m_123341_() <= this.x1 && i.m_123342_() - pos.m_123342_() >= this.y0 && i.m_123342_() - pos.m_123342_() <= this.y1 && i.m_123343_() - pos.m_123343_() >= this.z0 && i.m_123343_() - pos.m_123343_() <= this.z1) {
                                    int r = (Integer) this.match.apply(level.getBlockState(i));
                                    if (r > 0) {
                                        (r == 2 ? q2 : q1).add(i);
                                        if (!i.equals(pos)) {
                                            (r == 2 ? l2 : l1).add(i);
                                            if (l2.size() >= this.max) {
                                                return l2;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (rank == 2 && this.lv == 1) {
                return l2;
            } else {
                while (!q1.isEmpty()) {
                    BlockPos current = (BlockPos) q1.poll();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int zx = -1; zx <= 1; zx++) {
                                BlockPos i = current.offset(x, y, zx);
                                if (!added.contains(i)) {
                                    added.add(i);
                                    if (i.m_123341_() - pos.m_123341_() >= this.x0 && i.m_123341_() - pos.m_123341_() <= this.x1 && i.m_123342_() - pos.m_123342_() >= this.y0 && i.m_123342_() - pos.m_123342_() <= this.y1 && i.m_123343_() - pos.m_123343_() >= this.z0 && i.m_123343_() - pos.m_123343_() <= this.z1) {
                                        int r = (Integer) this.match.apply(level.getBlockState(i));
                                        if (r == 1) {
                                            q1.add(i);
                                            if (!i.equals(pos)) {
                                                l1.add(i);
                                                if (l2.size() + l1.size() >= this.max) {
                                                    l2.addAll(l1);
                                                    return l2;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                l2.addAll(l1);
                return l2;
            }
        }
    }
}