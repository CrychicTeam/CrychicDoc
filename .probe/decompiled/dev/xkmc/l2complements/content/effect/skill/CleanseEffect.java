package dev.xkmc.l2complements.content.effect.skill;

import dev.xkmc.l2complements.events.MagicEventHandler;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class CleanseEffect extends InherentEffect implements ForceEffect, IconOverlayEffect, SkillEffect {

    private static int recursive = 0;

    public static void clearOnEntity(LivingEntity entity) {
        recursive++;
        for (MobEffectInstance ins : new ArrayList(entity.getActiveEffects())) {
            if (!MagicEventHandler.isSkill(ins, entity)) {
                if (recursive <= 1) {
                    entity.removeEffect(ins.getEffect());
                }
                if (entity.hasEffect(ins.getEffect())) {
                    entity.getActiveEffectsMap().remove(ins.getEffect());
                }
            }
        }
        recursive--;
    }

    public CleanseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/cleanse.png"));
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        clearOnEntity(pLivingEntity);
    }

    @Override
    public boolean isDurationEffectTick(int tick, int amp) {
        return tick % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        clearOnEntity(pLivingEntity);
    }
}