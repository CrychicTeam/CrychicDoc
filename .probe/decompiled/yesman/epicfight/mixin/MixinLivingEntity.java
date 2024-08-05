package yesman.epicfight.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin({ LivingEntity.class })
public abstract class MixinLivingEntity {

    @Inject(at = { @At("TAIL") }, method = { "blockUsingShield(Lnet/minecraft/world/entity/LivingEntity;)V" }, cancellable = true)
    private void epicfight_blockUsingShield(LivingEntity livingEntity0, CallbackInfo info) {
        LivingEntity self = (LivingEntity) this;
        LivingEntityPatch<?> opponentEntitypatch = EpicFightCapabilities.getEntityPatch(livingEntity0, LivingEntityPatch.class);
        LivingEntityPatch<?> selfEntitypatch = EpicFightCapabilities.getEntityPatch(self, LivingEntityPatch.class);
        if (opponentEntitypatch != null) {
            opponentEntitypatch.setLastAttackResult(AttackResult.blocked(0.0F));
            if (selfEntitypatch != null && opponentEntitypatch.getEpicFightDamageSource() != null) {
                opponentEntitypatch.onAttackBlocked(opponentEntitypatch.getEpicFightDamageSource(), selfEntitypatch);
            }
        }
    }

    @Inject(at = { @At("RETURN") }, method = { "hurt" }, cancellable = true)
    private void epicfight_hurt(DamageSource damagesource, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity self = (LivingEntity) this;
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(damagesource.getEntity(), LivingEntityPatch.class);
        if (entitypatch != null && (Boolean) info.getReturnValue()) {
            entitypatch.setLastAttackEntity(self);
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "push(Lnet/minecraft/world/entity/Entity;)V" }, cancellable = true)
    private void epicfight_push(Entity entity0, CallbackInfo info) {
        LivingEntity self = (LivingEntity) this;
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(self, LivingEntityPatch.class);
        if (entitypatch != null && !entitypatch.canPush(entity0)) {
            info.cancel();
        }
    }
}