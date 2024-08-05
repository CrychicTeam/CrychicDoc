package dev.xkmc.l2complements.content.effect.force;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FlameEffect extends InherentEffect implements ForceEffect, IconOverlayEffect {

    public FlameEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int level) {
        DamageSource source = new DamageSource(DamageTypeGen.forKey(target.m_9236_(), DamageTypeGen.SOUL_FLAME));
        target.hurt(source, (float) (2 << level));
    }

    @Override
    public boolean isDurationEffectTick(int tick, int level) {
        return tick % 20 == 0;
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/flame.png"));
    }
}