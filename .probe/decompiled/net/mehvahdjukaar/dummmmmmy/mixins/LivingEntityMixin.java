package net.mehvahdjukaar.dummmmmmy.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.dummmmmmy.common.ModEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ LivingEntity.class })
public class LivingEntityMixin {

    @WrapOperation(method = { "actuallyHurt" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V") })
    private void actuallyHurt_setHealth(LivingEntity entity, float healthToSet, Operation<Void> original, DamageSource damageSource, float originalDamageAmount) {
        float originalHealth = entity.getHealth();
        float mitigatedDamageAmount = originalHealth - healthToSet;
        ModEvents.onEntityDamage(entity, mitigatedDamageAmount, damageSource);
        original.call(new Object[] { entity, healthToSet });
    }

    @WrapOperation(method = { "heal" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V") })
    private void heal_setHealth(LivingEntity entity, float healthToSet, Operation<Void> original, float originalHealAmount) {
        float originalHealth = entity.getHealth();
        float actualHealAmount = healthToSet - originalHealth;
        ModEvents.onEntityHeal(entity, actualHealAmount);
        original.call(new Object[] { entity, healthToSet });
    }
}