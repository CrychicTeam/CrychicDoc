package net.minecraft.world.item.alchemy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

public class Potion {

    @Nullable
    private final String name;

    private final ImmutableList<MobEffectInstance> effects;

    public static Potion byName(String string0) {
        return BuiltInRegistries.POTION.get(ResourceLocation.tryParse(string0));
    }

    public Potion(MobEffectInstance... mobEffectInstance0) {
        this(null, mobEffectInstance0);
    }

    public Potion(@Nullable String string0, MobEffectInstance... mobEffectInstance1) {
        this.name = string0;
        this.effects = ImmutableList.copyOf(mobEffectInstance1);
    }

    public String getName(String string0) {
        return string0 + (this.name == null ? BuiltInRegistries.POTION.getKey(this).getPath() : this.name);
    }

    public List<MobEffectInstance> getEffects() {
        return this.effects;
    }

    public boolean hasInstantEffects() {
        if (!this.effects.isEmpty()) {
            UnmodifiableIterator var1 = this.effects.iterator();
            while (var1.hasNext()) {
                MobEffectInstance $$0 = (MobEffectInstance) var1.next();
                if ($$0.getEffect().isInstantenous()) {
                    return true;
                }
            }
        }
        return false;
    }
}