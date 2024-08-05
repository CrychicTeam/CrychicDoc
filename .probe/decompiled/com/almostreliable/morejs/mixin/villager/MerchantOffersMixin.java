package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.MoreJS;
import com.almostreliable.morejs.features.villager.OfferExtension;
import javax.annotation.Nullable;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ MerchantOffers.class })
public class MerchantOffersMixin {

    @Inject(method = { "createFromStream" }, at = { @At("RETURN") })
    private static void morejs$createFromStream(FriendlyByteBuf friendlyByteBuf, CallbackInfoReturnable<MerchantOffers> cir) {
        ((MerchantOffers) cir.getReturnValue()).forEach(o -> {
            boolean disabled = friendlyByteBuf.readBoolean();
            ((OfferExtension) o).morejs$setDisabled(disabled);
        });
    }

    @Inject(method = { "writeToStream" }, at = { @At("RETURN") })
    private void morejs$writeCustomData(FriendlyByteBuf friendlyByteBuf, CallbackInfo ci) {
        for (MerchantOffer o : this.morejs$getSelf()) {
            friendlyByteBuf.writeBoolean(((OfferExtension) o).morejs$isDisabled());
        }
    }

    @Inject(method = { "<init>(Lnet/minecraft/nbt/CompoundTag;)V" }, at = { @At("RETURN") })
    private void morejs$storeDisabled(@Nullable CompoundTag tag, CallbackInfo ci) {
        if (tag != null) {
            try {
                MerchantOffers offers = this.morejs$getSelf();
                if (!tag.contains("morejs$disabled")) {
                    return;
                }
                ListTag list = tag.getList("morejs$disabled", 1);
                if (list.size() != offers.size()) {
                    return;
                }
                for (int i = 0; i < offers.size(); i++) {
                    boolean var10000;
                    label34: {
                        if (list.get(i) instanceof ByteTag bt && bt.getAsByte() != 0) {
                            var10000 = true;
                            break label34;
                        }
                        var10000 = false;
                    }
                    boolean disabled = var10000;
                    ((OfferExtension) offers.get(i)).morejs$setDisabled(disabled);
                }
            } catch (Exception var9) {
                MoreJS.LOG.warn("Failed to receive disabled offers", var9);
            }
        }
    }

    @Inject(method = { "createTag" }, at = { @At("RETURN") })
    private void appendDisabledOnTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = (CompoundTag) cir.getReturnValue();
        if (tag != null) {
            try {
                MerchantOffers offers = this.morejs$getSelf();
                ListTag list = new ListTag();
                for (MerchantOffer offer : offers) {
                    boolean disabled = ((OfferExtension) offer).morejs$isDisabled();
                    list.add(ByteTag.valueOf(disabled));
                }
                tag.put("morejs$disabled", list);
            } catch (Exception var8) {
                MoreJS.LOG.warn("Failed to store disabled offers", var8);
            }
        }
    }

    private MerchantOffers morejs$getSelf() {
        return (MerchantOffers) this;
    }
}