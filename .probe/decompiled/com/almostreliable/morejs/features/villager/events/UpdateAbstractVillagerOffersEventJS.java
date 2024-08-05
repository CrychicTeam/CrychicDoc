package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class UpdateAbstractVillagerOffersEventJS extends LivingEntityEventJS {

    private final AbstractVillager villager;

    private final MerchantOffers offers;

    private final VillagerTrades.ItemListing[] currentUsedItemListings;

    private final List<MerchantOffer> addedOffers;

    @Nullable
    private List<VillagerTrades.ItemListing> cachedWandererTrades;

    public static void invokeEvent(AbstractVillager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        if (villager instanceof Villager v) {
            UpdateVillagerOffersEventJS e = new UpdateVillagerOffersEventJS(v, offers, currentUsedItemListings, addedOffers);
            Events.UPDATE_VILLAGER_OFFERS.post(e);
        } else {
            UpdateAbstractVillagerOffersEventJS e = new UpdateAbstractVillagerOffersEventJS(villager, offers, currentUsedItemListings, addedOffers);
            if (villager instanceof WanderingTrader) {
                Events.UPDATE_WANDERER_OFFERS.post(e);
            } else {
                Events.UPDATE_ABSTRACT_VILLAGER_OFFERS.post(e);
            }
        }
    }

    public UpdateAbstractVillagerOffersEventJS(AbstractVillager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        this.villager = villager;
        this.offers = offers;
        this.currentUsedItemListings = currentUsedItemListings;
        this.addedOffers = Collections.unmodifiableList(addedOffers);
    }

    @Override
    public LivingEntity getEntity() {
        return this.villager;
    }

    @Nullable
    public VillagerData getVillagerData() {
        return this.villager instanceof Villager v ? v.getVillagerData() : null;
    }

    public boolean isVillager() {
        return this.villager instanceof Villager;
    }

    public boolean isWanderer() {
        return this.villager instanceof WanderingTrader;
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    public List<VillagerTrades.ItemListing> getUsedTrades() {
        return Arrays.asList(this.currentUsedItemListings);
    }

    public Collection<MerchantOffer> getAddedOffers() {
        return this.addedOffers;
    }

    public void deleteAddedOffers() {
        this.offers.removeAll(this.addedOffers);
    }

    @Nullable
    public MerchantOffer addRandomOffer() {
        return this.addRandomOffer(this.getUsedTrades());
    }

    @Nullable
    public MerchantOffer addRandomOffer(List<VillagerTrades.ItemListing> possibleTrades) {
        if (possibleTrades.isEmpty()) {
            return null;
        } else {
            int i = this.getLevel().getRandom().nextInt(possibleTrades.size());
            VillagerTrades.ItemListing randomListing = (VillagerTrades.ItemListing) possibleTrades.get(i);
            MerchantOffer offer = randomListing.getOffer(this.villager, this.getLevel().getRandom());
            if (offer != null) {
                this.offers.add(offer);
                return offer;
            } else {
                return null;
            }
        }
    }

    public List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession) {
        return VillagerUtils.getVillagerTrades(profession);
    }

    public List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession, int level) {
        return VillagerUtils.getVillagerTrades(profession, level);
    }

    public List<VillagerTrades.ItemListing> getWandererTrades() {
        if (this.cachedWandererTrades == null) {
            this.cachedWandererTrades = new ArrayList();
            ObjectIterator var1 = VillagerTrades.WANDERING_TRADER_TRADES.values().iterator();
            while (var1.hasNext()) {
                VillagerTrades.ItemListing[] listings = (VillagerTrades.ItemListing[]) var1.next();
                this.cachedWandererTrades.addAll(Arrays.asList(listings));
            }
        }
        return this.cachedWandererTrades;
    }

    public List<VillagerTrades.ItemListing> getWandererTrades(int level) {
        return VillagerUtils.getWandererTrades(level);
    }
}