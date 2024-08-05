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
import net.minecraftforge.common.ForgeMod;

public class StoneCageEffect extends InherentEffect implements ForceEffect, IconOverlayEffect {

    private static final UUID ID_SLOW = MathHelper.getUUIDFromString("l2complements:stone_cage_slow");

    private static final UUID ID_FLY = MathHelper.getUUIDFromString("l2complements:stone_cage_fly");

    private static final UUID ID_KB = MathHelper.getUUIDFromString("l2complements:stone_cage_kb");

    private static final UUID ID_JUMP = MathHelper.getUUIDFromString("l2complements:stone_cage_jump");

    public StoneCageEffect(MobEffectCategory type, int color) {
        super(type, color);
        this.m_19472_(Attributes.MOVEMENT_SPEED, ID_SLOW.toString(), -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.FLYING_SPEED, ID_FLY.toString(), -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, ID_KB.toString(), 1.0, AttributeModifier.Operation.ADDITION);
        this.m_19472_(ForgeMod.SWIM_SPEED.get(), ID_JUMP.toString(), -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int int0) {
        entity.m_20334_(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isDurationEffectTick(int int0, int int1) {
        return true;
    }

    @Override
    public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
        return DelayedEntityRender.icon(entity, new ResourceLocation("l2complements", "textures/effect_overlay/stone_cage.png"));
    }
}