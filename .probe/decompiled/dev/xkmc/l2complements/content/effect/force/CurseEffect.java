package dev.xkmc.l2complements.content.effect.force;

import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CurseEffect extends InherentEffect implements ForceEffect, IconOverlayEffect {

    public CurseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/curse.png"));
    }
}