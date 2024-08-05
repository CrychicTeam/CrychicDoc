package dev.xkmc.modulargolems.compat.materials.create.modifier;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.UUID;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MechMobileEffect extends InherentEffect {

    public MechMobileEffect(MobEffectCategory category, int color) {
        super(category, color);
        UUID uuid = MathHelper.getUUIDFromString("modulargolems:mech_mobility");
        this.m_19472_(Attributes.MOVEMENT_SPEED, uuid.toString(), 0.2, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int lv, AttributeModifier val) {
        return MGConfig.COMMON.mechSpeed.get() * (double) (lv + 1);
    }
}