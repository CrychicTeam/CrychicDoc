package com.mna.mixins;

import com.mna.api.entities.DamageHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Player.class })
public class PlayerConfalgrateNoHurtArmor {

    @Inject(at = { @At("HEAD") }, method = { "hurtArmor" }, cancellable = true)
    public void mna$hurtArmor(DamageSource pDamageSource, float pDamage, CallbackInfo cir) {
        if (pDamageSource.is(DamageHelper.CONFLAGRATE)) {
            cir.cancel();
        }
    }
}