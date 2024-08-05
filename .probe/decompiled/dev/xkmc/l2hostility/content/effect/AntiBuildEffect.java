package dev.xkmc.l2hostility.content.effect;

import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AntiBuildEffect extends InherentEffect implements ForceEffect {

    public AntiBuildEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}