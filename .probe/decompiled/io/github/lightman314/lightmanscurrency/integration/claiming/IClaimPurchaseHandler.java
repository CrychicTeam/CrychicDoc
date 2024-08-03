package io.github.lightman314.lightmanscurrency.integration.claiming;

import io.github.lightman314.lightmanscurrency.LCConfig;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;

public interface IClaimPurchaseHandler {

    default boolean canBuyClaims(@Nonnull ServerPlayer player) {
        return LCConfig.SERVER.claimingAllowClaimPurchase.get();
    }

    default boolean canBuyForceload(@Nonnull ServerPlayer player) {
        return LCConfig.SERVER.claimingAllowForceloadPurchase.get();
    }

    int getCurrentBonusClaims(@Nonnull ServerPlayer var1);

    int getCurrentBonusForceloadChunks(@Nonnull ServerPlayer var1);

    void addBonusClaims(@Nonnull ServerPlayer var1, int var2);

    void addBonusForceloadChunks(@Nonnull ServerPlayer var1, int var2);
}