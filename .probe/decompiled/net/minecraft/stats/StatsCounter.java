package net.minecraft.stats;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.Player;

public class StatsCounter {

    protected final Object2IntMap<Stat<?>> stats = Object2IntMaps.synchronize(new Object2IntOpenHashMap());

    public StatsCounter() {
        this.stats.defaultReturnValue(0);
    }

    public void increment(Player player0, Stat<?> stat1, int int2) {
        int $$3 = (int) Math.min((long) this.getValue(stat1) + (long) int2, 2147483647L);
        this.setValue(player0, stat1, $$3);
    }

    public void setValue(Player player0, Stat<?> stat1, int int2) {
        this.stats.put(stat1, int2);
    }

    public <T> int getValue(StatType<T> statTypeT0, T t1) {
        return statTypeT0.contains(t1) ? this.getValue(statTypeT0.get(t1)) : 0;
    }

    public int getValue(Stat<?> stat0) {
        return this.stats.getInt(stat0);
    }
}