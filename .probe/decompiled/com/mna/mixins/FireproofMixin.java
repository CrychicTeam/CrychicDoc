package com.mna.mixins;

import com.mna.enchantments.framework.EnchantmentInit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ItemEntity.class })
public class FireproofMixin {

    @Inject(at = { @At("RETURN") }, method = { "fireImmune" }, cancellable = true)
    public void mna$fireImmune(CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity) this;
        ItemStack stack = self.getItem();
        int level = stack.getEnchantmentLevel(EnchantmentInit.FIREPROOF.get());
        if (level > 0) {
            cir.setReturnValue(true);
        }
    }
}