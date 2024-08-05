package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Shadow
    SynchedEntityData entityData;

    @Shadow
    static EntityDataAccessor<Integer> DATA_TICKS_FROZEN;

    @Shadow
    public abstract boolean isFree(double var1, double var3, double var5);

    @Inject(method = { "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z" }, at = { @At("HEAD") }, cancellable = true)
    public void isAlliedTo(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) this;
        if (entity instanceof MagicSummon summon && summon.getSummoner() != null) {
            cir.setReturnValue(self.isAlliedTo(summon.getSummoner()) || self.equals(summon.getSummoner()));
        }
    }

    @Inject(method = { "setTicksFrozen" }, at = { @At("HEAD") }, cancellable = true)
    public void setTicksFrozen(int pTicksFrozen, CallbackInfo ci) {
        if (this instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffectRegistry.CHILLED.get())) {
            int currentTicks = ((Entity) this).getTicksFrozen();
            int deltaTicks = pTicksFrozen - currentTicks;
            if (deltaTicks > 0) {
                deltaTicks *= 2;
                this.entityData.set(DATA_TICKS_FROZEN, currentTicks + deltaTicks);
                ci.cancel();
            }
        }
    }

    @Inject(method = { "isInvisibleTo" }, at = { @At("HEAD") }, cancellable = true)
    public void isInvisibleTo(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ItemRegistry.INVISIBILITY_RING.get().isEquippedBy(player)) {
            cir.setReturnValue(false);
        }
    }
}