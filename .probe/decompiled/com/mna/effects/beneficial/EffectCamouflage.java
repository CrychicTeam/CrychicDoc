package com.mna.effects.beneficial;

import com.mna.effects.EffectInit;
import com.mna.tools.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EffectCamouflage extends SimpleBeneficialEffect {

    public static final String NBT_CAMOFLAGE_PCT = "mna:camoflage_percent";

    public static final float CAMOUFLAGE_THRESHOLD = 0.5F;

    public static float getCamoflagePercent(LivingEntity entity) {
        if (entity.hasEffect(EffectInit.CAMOUFLAGE.get())) {
            return entity.getPersistentData().getFloat("mna:camoflage_percent");
        } else {
            entity.getPersistentData().remove("mna:camoflage_percent");
            return 0.0F;
        }
    }

    public static void setCamoflagePercent(LivingEntity entity, float percent) {
        entity.getPersistentData().putFloat("mna:camoflage_percent", percent);
        if (percent > 0.5F) {
            entity.m_6842_(true);
        } else {
            entity.m_6842_(false);
        }
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        float allowedMovement = pAmplifier == 1 && pLivingEntity.m_20163_() ? 0.075F : 0.01F;
        Vec3 movement = new Vec3(pLivingEntity.m_20184_().x, 0.0, pLivingEntity.m_20184_().z);
        if (!(movement.length() > (double) allowedMovement) && !pLivingEntity.isCurrentlyGlowing()) {
            float curCamouflage = getCamoflagePercent(pLivingEntity);
            curCamouflage = Math.min(curCamouflage + 0.0125F, 1.0F);
            setCamoflagePercent(pLivingEntity, curCamouflage);
        } else {
            setCamoflagePercent(pLivingEntity, 0.0F);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player) {
            EntityUtil.removeInvisibility(entityLivingBaseIn);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}