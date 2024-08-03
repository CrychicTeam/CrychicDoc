package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TradeWithVillagerEvent extends PlayerEvent {

    private final MerchantOffer offer;

    private final AbstractVillager abstractVillager;

    @Internal
    public TradeWithVillagerEvent(Player player, MerchantOffer offer, AbstractVillager abstractVillager) {
        super(player);
        this.offer = offer;
        this.abstractVillager = abstractVillager;
    }

    public MerchantOffer getMerchantOffer() {
        return this.offer;
    }

    public AbstractVillager getAbstractVillager() {
        return this.abstractVillager;
    }
}