package com.almostreliable.morejs.features.villager.events;

import java.util.List;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class UpdateVillagerOffersEventJS extends UpdateAbstractVillagerOffersEventJS {

    private final Villager villager;

    public UpdateVillagerOffersEventJS(Villager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        super(villager, offers, currentUsedItemListings, addedOffers);
        this.villager = villager;
    }

    public Villager getEntity() {
        return this.villager;
    }

    @Override
    public VillagerData getVillagerData() {
        return this.villager.getVillagerData();
    }

    public boolean isProfession(VillagerProfession profession) {
        return this.villager.getVillagerData().getProfession() == profession;
    }

    public int getVillagerLevel() {
        return this.villager.getVillagerData().getLevel();
    }

    public VillagerProfession getProfession() {
        return this.villager.getVillagerData().getProfession();
    }
}