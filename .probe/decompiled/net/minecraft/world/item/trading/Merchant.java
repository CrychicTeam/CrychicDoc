package net.minecraft.world.item.trading;

import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;

public interface Merchant {

    void setTradingPlayer(@Nullable Player var1);

    @Nullable
    Player getTradingPlayer();

    MerchantOffers getOffers();

    void overrideOffers(MerchantOffers var1);

    void notifyTrade(MerchantOffer var1);

    void notifyTradeUpdated(ItemStack var1);

    int getVillagerXp();

    void overrideXp(int var1);

    boolean showProgressBar();

    SoundEvent getNotifyTradeSound();

    default boolean canRestock() {
        return false;
    }

    default void openTradingScreen(Player player0, Component component1, int int2) {
        OptionalInt $$3 = player0.openMenu(new SimpleMenuProvider((p_45298_, p_45299_, p_45300_) -> new MerchantMenu(p_45298_, p_45299_, this), component1));
        if ($$3.isPresent()) {
            MerchantOffers $$4 = this.getOffers();
            if (!$$4.isEmpty()) {
                player0.sendMerchantOffers($$3.getAsInt(), $$4, int2, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
            }
        }
    }

    boolean isClientSide();
}