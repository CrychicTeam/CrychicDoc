package dev.xkmc.l2complements.content.effect.skill;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BleedEffect extends InherentEffect implements ForceEffect, IconOverlayEffect, SkillEffect, StackingEffect<BleedEffect> {

    private static final UUID ID_SLOW = MathHelper.getUUIDFromString("l2complements:bleed_slow");

    private static final UUID ID_ATK = MathHelper.getUUIDFromString("l2complements:bleed_atk");

    public BleedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.m_19472_(Attributes.MOVEMENT_SPEED, ID_SLOW.toString(), -0.1F, AttributeModifier.Operation.MULTIPLY_BASE);
        this.m_19472_(Attributes.ATTACK_DAMAGE, ID_ATK.toString(), -0.1F, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amp, AttributeModifier mod) {
        return Math.pow(1.0 + mod.getAmount(), (double) (amp + 1)) - 1.0;
    }

    @Override
    public void applyEffectTick(LivingEntity target, int level) {
        DamageSource source = new DamageSource(DamageTypeGen.forKey(target.m_9236_(), DamageTypeGen.BLEED));
        target.hurt(source, (float) (6 * (level + 1)));
    }

    @Override
    public boolean isDurationEffectTick(int tick, int level) {
        return tick % 60 == 0;
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/bleed_" + Mth.clamp(lv, 0, 8) + ".png"));
    }
}