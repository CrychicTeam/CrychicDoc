package dev.xkmc.l2complements.content.effect.force;

import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class IceEffect extends InherentEffect implements ForceEffect, IconOverlayEffect {

    private static final UUID ID = MathHelper.getUUIDFromString("l2complements:ice");

    public IceEffect(MobEffectCategory type, int color) {
        super(type, color);
        this.m_19472_(Attributes.MOVEMENT_SPEED, ID.toString(), -0.6F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity target, int level) {
        target.m_146924_(true);
    }

    @Override
    public boolean isDurationEffectTick(int tick, int level) {
        return true;
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/ice.png"));
    }
}