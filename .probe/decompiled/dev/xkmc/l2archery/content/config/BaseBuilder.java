package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.HashMap;
import net.minecraft.world.effect.MobEffect;

public class BaseBuilder<B extends BaseBuilder<B, T, I, E>, T extends I, I, E> {

    protected final BowArrowStatConfig config;

    protected final T id;

    protected final HashMap<I, HashMap<MobEffect, E>> effmap;

    protected final HashMap<MobEffect, E> effects = new HashMap();

    protected BaseBuilder(BowArrowStatConfig config, HashMap<I, HashMap<MobEffect, E>> effmap, RegistryEntry<T> bow) {
        this.config = config;
        this.effmap = effmap;
        this.id = (T) bow.get();
    }

    public BowArrowStatConfig end() {
        if (this.effects.size() > 0) {
            this.effmap.put(this.id, this.effects);
        }
        return this.config;
    }

    public final B getThis() {
        return (B) Wrappers.cast(this);
    }
}