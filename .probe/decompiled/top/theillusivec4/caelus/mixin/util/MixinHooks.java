package top.theillusivec4.caelus.mixin.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.caelus.api.CaelusApi;

public class MixinHooks {

    public static boolean canFly(LivingEntity livingEntity, boolean oldFlag, boolean newFlag) {
        CaelusApi.TriState fallFly = CaelusApi.getInstance().canFallFly(livingEntity);
        if (fallFly == CaelusApi.TriState.DENY) {
            return false;
        } else {
            return fallFly == CaelusApi.TriState.DEFAULT ? newFlag : !livingEntity.m_20096_() && !livingEntity.m_20159_() && !livingEntity.hasEffect(MobEffects.LEVITATION) && oldFlag;
        }
    }
}