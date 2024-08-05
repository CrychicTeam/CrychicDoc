package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.OfferExtension;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;

public class StartTradingEventJS extends PlayerEventJS {

    private final Player player;

    private final Merchant merchant;

    public StartTradingEventJS(Player player, Merchant merchant) {
        this.player = player;
        this.merchant = merchant;
    }

    @Override
    public Player getEntity() {
        return this.player;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public void forEachOffers(BiConsumer<OfferExtension, Integer> consumer) {
        for (int i = 0; i < this.merchant.getOffers().size(); i++) {
            MerchantOffer offer = (MerchantOffer) this.merchant.getOffers().get(i);
            consumer.accept((OfferExtension) offer, i);
        }
    }
}