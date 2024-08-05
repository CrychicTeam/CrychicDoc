package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

public class EffectWrithingBrambles extends SimpleHarmfulEffect implements INoCreeperLingering {

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.getPersistentData().remove("brambles_distance");
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Vec3 pos = pLivingEntity.m_20182_();
        Vec3 oldPos = null;
        if (!pLivingEntity.getPersistentData().contains("brambles_old_x")) {
            pLivingEntity.getPersistentData().putDouble("brambles_old_x", pos.x);
            pLivingEntity.getPersistentData().putDouble("brambles_old_y", pos.y);
            pLivingEntity.getPersistentData().putDouble("brambles_old_z", pos.z);
        } else {
            double oldX = pLivingEntity.getPersistentData().getDouble("brambles_old_x");
            double oldY = pLivingEntity.getPersistentData().getDouble("brambles_old_y");
            double oldZ = pLivingEntity.getPersistentData().getDouble("brambles_old_z");
            oldPos = new Vec3(oldX, oldY, oldZ);
            pLivingEntity.getPersistentData().putDouble("brambles_old_x", pos.x);
            pLivingEntity.getPersistentData().putDouble("brambles_old_y", pos.y);
            pLivingEntity.getPersistentData().putDouble("brambles_old_z", pos.z);
            oldX = oldPos.distanceTo(pos);
            if (pLivingEntity.getPersistentData().contains("brambles_distance")) {
                oldX += pLivingEntity.getPersistentData().getDouble("brambles_distance");
            }
            oldY = (double) (10 >> pAmplifier);
            if (oldX >= oldY) {
                pLivingEntity.hurt(pLivingEntity.m_269291_().cactus(), 1.0F);
                oldX = 0.0;
            }
            pLivingEntity.getPersistentData().putDouble("brambles_distance", oldX);
        }
    }
}