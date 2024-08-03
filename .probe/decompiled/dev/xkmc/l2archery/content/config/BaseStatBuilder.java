package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import java.util.HashMap;
import net.minecraft.world.effect.MobEffect;

public class BaseStatBuilder<B extends BaseStatBuilder<B, T, I>, T extends I, I> extends BaseBuilder<B, T, I, BowArrowStatConfig.ConfigEffect> {

    private final HashMap<I, HashMap<BowArrowStatType, Double>> statmap;

    private final HashMap<BowArrowStatType, Double> stats = new HashMap();

    protected BaseStatBuilder(BowArrowStatConfig config, HashMap<I, HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>> map, HashMap<I, HashMap<BowArrowStatType, Double>> statmap, RegistryEntry<T> bow) {
        super(config, map, bow);
        this.statmap = statmap;
    }

    public final B putStat(BowArrowStatType type, double val) {
        this.stats.put(type, val);
        return this.getThis();
    }

    public final B putEffect(MobEffect type, int duration, int amplifier) {
        this.effects.put(type, new BowArrowStatConfig.ConfigEffect(duration, amplifier));
        return this.getThis();
    }

    @Override
    public BowArrowStatConfig end() {
        super.end();
        if (this.stats.size() > 0) {
            this.statmap.put(this.id, this.stats);
        }
        return this.config;
    }
}