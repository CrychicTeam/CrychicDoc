package net.minecraft.stats;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class Stat<T> extends ObjectiveCriteria {

    private final StatFormatter formatter;

    private final T value;

    private final StatType<T> type;

    protected Stat(StatType<T> statTypeT0, T t1, StatFormatter statFormatter2) {
        super(buildName(statTypeT0, t1));
        this.type = statTypeT0;
        this.formatter = statFormatter2;
        this.value = t1;
    }

    public static <T> String buildName(StatType<T> statTypeT0, T t1) {
        return locationToKey(BuiltInRegistries.STAT_TYPE.getKey(statTypeT0)) + ":" + locationToKey(statTypeT0.getRegistry().getKey(t1));
    }

    private static <T> String locationToKey(@Nullable ResourceLocation resourceLocation0) {
        return resourceLocation0.toString().replace(':', '.');
    }

    public StatType<T> getType() {
        return this.type;
    }

    public T getValue() {
        return this.value;
    }

    public String format(int int0) {
        return this.formatter.format(int0);
    }

    public boolean equals(Object object0) {
        return this == object0 || object0 instanceof Stat && Objects.equals(this.m_83620_(), ((Stat) object0).m_83620_());
    }

    public int hashCode() {
        return this.m_83620_().hashCode();
    }

    public String toString() {
        return "Stat{name=" + this.m_83620_() + ", formatter=" + this.formatter + "}";
    }
}