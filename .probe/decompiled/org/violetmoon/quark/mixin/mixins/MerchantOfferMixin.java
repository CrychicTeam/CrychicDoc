package org.violetmoon.quark.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.experimental.hax.PseudoAccessorMerchantOffer;
import org.violetmoon.quark.content.tools.module.AncientTomesModule;

@Mixin({ MerchantOffer.class })
public class MerchantOfferMixin implements PseudoAccessorMerchantOffer {

    @Unique
    private int tier;

    @Override
    public int quark$getTier() {
        return this.tier;
    }

    @Override
    public void quark$setTier(int tier) {
        this.tier = tier;
    }

    @Inject(method = { "<init>(Lnet/minecraft/nbt/CompoundTag;)V" }, at = { @At("RETURN") })
    private void setTierWhenConstructed(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("quark:tier", 99)) {
            this.tier = tag.getInt("quark:tier");
        } else {
            this.tier = -1;
        }
    }

    @Inject(method = { "<init>(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;IIIFI)V" }, at = { @At("RETURN") })
    private void setTierWhenConstructed(ItemStack baseCostA, ItemStack costB, ItemStack result, int uses, int maxUses, int xp, float priceMultiplier, int demand, CallbackInfo ci) {
        this.tier = 6;
    }

    @ModifyReturnValue(method = { "createTag" }, at = { @At("RETURN") })
    private CompoundTag addTierToTag(CompoundTag tag) {
        if (this.tier >= 0) {
            tag.putInt("quark:tier", this.tier);
        }
        return tag;
    }

    @Inject(method = { "isRequiredItem" }, at = { @At("HEAD") }, cancellable = true)
    private void isRequiredItem(ItemStack comparing, ItemStack reference, CallbackInfoReturnable<Boolean> cir) {
        MerchantOffer offer = (MerchantOffer) this;
        if (AncientTomesModule.matchWildcardEnchantedBook(offer, comparing, reference)) {
            cir.setReturnValue(true);
        }
    }
}