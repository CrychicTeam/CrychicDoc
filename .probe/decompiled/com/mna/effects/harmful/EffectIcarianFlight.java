package com.mna.effects.harmful;

import com.mna.network.ServerMessageDispatcher;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

public class EffectIcarianFlight extends MobEffect {

    public EffectIcarianFlight() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        writeIcarianData(pLivingEntity);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        clearIcarianData(pLivingEntity);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Vec3 vec = readIcarianData(pLivingEntity);
        pLivingEntity.m_20256_(vec);
        pLivingEntity.f_19812_ = true;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public static void writeIcarianData(LivingEntity pLivingEntity) {
        Vec3 fwd = pLivingEntity.m_20156_().normalize().scale(5.0);
        pLivingEntity.getPersistentData().putDouble("icarianX", fwd.x);
        pLivingEntity.getPersistentData().putDouble("icarianY", Math.max(Math.abs(fwd.y), 1.0));
        pLivingEntity.getPersistentData().putDouble("icarianZ", fwd.z);
        if (!pLivingEntity.m_9236_().isClientSide()) {
            ServerMessageDispatcher.sendSetIcarianData(pLivingEntity, fwd);
        }
    }

    public static void writeIcarianData(LivingEntity pLivingEntity, Vec3 fwd) {
        pLivingEntity.getPersistentData().putDouble("icarianX", fwd.x);
        pLivingEntity.getPersistentData().putDouble("icarianY", Math.max(Math.abs(fwd.y), 1.0));
        pLivingEntity.getPersistentData().putDouble("icarianZ", fwd.z);
        if (!pLivingEntity.m_9236_().isClientSide()) {
            ServerMessageDispatcher.sendSetIcarianData(pLivingEntity, fwd);
        }
    }

    public static Vec3 readIcarianData(LivingEntity pLivingEntity) {
        return new Vec3(pLivingEntity.getPersistentData().getDouble("icarianX"), pLivingEntity.getPersistentData().getDouble("icarianY"), pLivingEntity.getPersistentData().getDouble("icarianZ"));
    }

    public static void clearIcarianData(LivingEntity pLivingEntity) {
        pLivingEntity.getPersistentData().remove("icarianX");
        pLivingEntity.getPersistentData().remove("icarianY");
        pLivingEntity.getPersistentData().remove("icarianZ");
        if (!pLivingEntity.m_9236_().isClientSide()) {
            ServerMessageDispatcher.sendSetIcarianData(pLivingEntity, Vec3.ZERO);
        }
    }
}