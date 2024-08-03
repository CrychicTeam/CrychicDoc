package org.violetmoon.quark.mixin.mixins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.experimental.hax.PseudoAccessorMerchantOffer;

@Mixin({ MerchantOffers.class })
public class MerchantOffersMixin {

    @Inject(method = { "<init>(Lnet/minecraft/nbt/CompoundTag;)V" }, at = { @At("RETURN") })
    public void setUpTiers(CompoundTag tag, CallbackInfo ci) {
        MerchantOffers offers = (MerchantOffers) this;
        for (int i = 0; i < offers.size(); i++) {
            PseudoAccessorMerchantOffer offer = (PseudoAccessorMerchantOffer) offers.get(i);
            if (offer.quark$getTier() < 0) {
                offer.quark$setTier(i / 2);
            }
        }
    }
}