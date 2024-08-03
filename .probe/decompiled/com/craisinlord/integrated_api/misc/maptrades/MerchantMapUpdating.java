package com.craisinlord.integrated_api.misc.maptrades;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.mixins.entities.MerchantOfferAccessor;
import com.craisinlord.integrated_api.mixins.items.MapItemAccessor;
import com.craisinlord.integrated_api.utils.AsyncLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MerchantMapUpdating {

    private MerchantMapUpdating() {
    }

    public static void invalidateMap(AbstractVillager merchant, ItemStack mapStack) {
        mapStack.setHoverName(Component.translatable("item.minecraft.map"));
        merchant.getOffers().stream().filter(offer -> offer.getResult() == mapStack).findFirst().ifPresentOrElse(offer -> removeOffer(merchant, offer), () -> IntegratedAPI.LOGGER.warn("Failed to find merchant offer for map"));
    }

    public static void removeOffer(AbstractVillager merchant, MerchantOffer offer) {
        ((MerchantOfferAccessor) offer).setMaxUses(0);
        offer.setToOutOfStock();
    }

    public static void handleLocationFound(ServerLevel level, AbstractVillager merchant, ItemStack mapStack, String displayName, MapDecoration.Type destinationType, BlockPos pos) {
        if (pos == null) {
            invalidateMap(merchant, mapStack);
        } else {
            updateMap(mapStack, level, pos, 2, destinationType, displayName);
        }
        if (merchant.getTradingPlayer() instanceof ServerPlayer tradingPlayer) {
            tradingPlayer.sendMerchantOffers(tradingPlayer.f_36096_.containerId, merchant.getOffers(), merchant instanceof Villager villager ? villager.getVillagerData().getLevel() : 1, merchant.getVillagerXp(), merchant.showProgressBar(), merchant.m_7862_());
        }
    }

    public static MerchantOffer updateMapAsync(Entity pTrader, int emeraldCost, String displayName, MapDecoration.Type destinationType, int maxUses, int villagerXp, TagKey<Structure> destination, int searchRadius) {
        return updateMapAsyncInternal(pTrader, emeraldCost, maxUses, villagerXp, (level, merchant, mapStack) -> AsyncLocator.locate(level, destination, merchant.m_20183_(), searchRadius, true).thenOnServerThread(pos -> handleLocationFound(level, merchant, mapStack, displayName, destinationType, pos)));
    }

    public static MerchantOffer updateMapAsync(Entity pTrader, int emeraldCost, String displayName, MapDecoration.Type destinationType, int maxUses, int villagerXp, HolderSet<Structure> structureSet, int searchRadius) {
        return updateMapAsyncInternal(pTrader, emeraldCost, maxUses, villagerXp, (level, merchant, mapStack) -> AsyncLocator.locate(level, structureSet, merchant.m_20183_(), searchRadius, true).thenOnServerThread(pair -> handleLocationFound(level, merchant, mapStack, displayName, destinationType, (BlockPos) pair.getFirst())));
    }

    private static MerchantOffer updateMapAsyncInternal(Entity trader, int emeraldCost, int maxUses, int villagerXp, MerchantMapUpdating.MapUpdateTask task) {
        if (trader instanceof AbstractVillager merchant) {
            ItemStack mapStack = createEmptyMap();
            task.apply((ServerLevel) trader.level(), merchant, mapStack);
            return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(Items.COMPASS), mapStack, maxUses, villagerXp, 0.2F);
        } else {
            return null;
        }
    }

    public static ItemStack createEmptyMap() {
        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        stack.setHoverName(Component.translatable("menu.working"));
        return stack;
    }

    public static void updateMap(ItemStack mapStack, ServerLevel level, BlockPos pos, int scale, MapDecoration.Type destinationType, String displayName) {
        MapItemAccessor.callCreateAndStoreSavedData(mapStack, level, pos.m_123341_(), pos.m_123343_(), scale, true, true, level.m_46472_());
        MapItem.renderBiomePreviewMap(level, mapStack);
        MapItemSavedData.addTargetDecoration(mapStack, pos, "+", destinationType);
        if (displayName != null) {
            mapStack.setHoverName(Component.translatable(displayName));
        }
    }

    public interface MapUpdateTask {

        void apply(ServerLevel var1, AbstractVillager var2, ItemStack var3);
    }
}