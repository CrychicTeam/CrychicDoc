package net.minecraft.world.entity.ai.memory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.VisibleForDebug;

public class ExpirableValue<T> {

    private final T value;

    private long timeToLive;

    public ExpirableValue(T t0, long long1) {
        this.value = t0;
        this.timeToLive = long1;
    }

    public void tick() {
        if (this.canExpire()) {
            this.timeToLive--;
        }
    }

    public static <T> ExpirableValue<T> of(T t0) {
        return new ExpirableValue<>(t0, Long.MAX_VALUE);
    }

    public static <T> ExpirableValue<T> of(T t0, long long1) {
        return new ExpirableValue<>(t0, long1);
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public T getValue() {
        return this.value;
    }

    public boolean hasExpired() {
        return this.timeToLive <= 0L;
    }

    public String toString() {
        return this.value + (this.canExpire() ? " (ttl: " + this.timeToLive + ")" : "");
    }

    @VisibleForDebug
    public boolean canExpire() {
        return this.timeToLive != Long.MAX_VALUE;
    }

    public static <T> Codec<ExpirableValue<T>> codec(Codec<T> codecT0) {
        return RecordCodecBuilder.create(p_26308_ -> p_26308_.group(codecT0.fieldOf("value").forGetter(p_148193_ -> p_148193_.value), Codec.LONG.optionalFieldOf("ttl").forGetter(p_148187_ -> p_148187_.canExpire() ? Optional.of(p_148187_.timeToLive) : Optional.empty())).apply(p_26308_, (p_148189_, p_148190_) -> new ExpirableValue<>(p_148189_, (Long) p_148190_.orElse(Long.MAX_VALUE))));
    }
}