package io.github.lightman314.lightmanscurrency.mixin.compat.supplementaries;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.VillagerTradeManager;
import io.github.lightman314.lightmanscurrency.integration.supplementaries.LCSupplementaries;
import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.supplementaries.common.entities.trades.ModVillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ModVillagerTrades.class })
public class ModVillagerTradesMixin {

    @Inject(at = { @At("RETURN") }, method = { "getRedMerchantTrades" }, cancellable = true, remap = false)
    private static void getRedMerchantTrades(CallbackInfoReturnable<VillagerTrades.ItemListing[]> callbackInfo) {
        if (LCSupplementaries.triggerMixin && LCConfig.COMMON.changeModdedTrades.get()) {
            List<VillagerTrades.ItemListing> list = new ArrayList(List.of((VillagerTrades.ItemListing[]) callbackInfo.getReturnValue()));
            VillagerTradeManager.replaceExistingTrades("supplementaries:red_merchant", list);
            callbackInfo.setReturnValue((VillagerTrades.ItemListing[]) list.toArray(VillagerTrades.ItemListing[]::new));
        }
    }
}