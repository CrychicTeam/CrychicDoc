package io.github.lightman314.lightmanscurrency.common.villager_merchant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.EnchantedBookForCoinsTrade;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.EnchantedItemForCoinsTrade;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.ItemsForMapTrade;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.RandomTrade;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.SimpleTrade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.jetbrains.annotations.NotNull;

public class ItemListingSerializer {

    private static final Map<Class<? extends VillagerTrades.ItemListing>, ItemListingSerializer.IItemListingSerializer> serializers = new HashMap();

    private static final Map<ResourceLocation, ItemListingSerializer.IItemListingDeserializer> deserializers = new HashMap();

    public static <L extends VillagerTrades.ItemListing, T extends ItemListingSerializer.IItemListingSerializer & ItemListingSerializer.IItemListingDeserializer> void registerItemListing(@NotNull ResourceLocation type, @NotNull Class<L> clazz, @NotNull T serializer) {
        registerItemListing(type, clazz, serializer, serializer);
    }

    public static <T extends VillagerTrades.ItemListing> void registerItemListing(@NotNull ResourceLocation type, Class<T> clazz, @NotNull ItemListingSerializer.IItemListingSerializer serializer, @NotNull ItemListingSerializer.IItemListingDeserializer deserializer) {
        if (serializers.containsKey(clazz)) {
            LightmansCurrency.LogWarning("Attempted to register a duplicate ItemListing Serializer of class '" + clazz.getName() + "'!");
        } else if (deserializers.containsKey(type)) {
            LightmansCurrency.LogWarning("Attempted to register a duplicate ItemListing Deserializer of type '" + type + "'!");
        } else {
            serializers.put(clazz, serializer);
            deserializers.put(type, deserializer);
            LightmansCurrency.LogInfo("Registered Item Listing serializer '" + type + "'");
        }
    }

    public static JsonObject serialize(Map<Integer, List<VillagerTrades.ItemListing>> trades, int count) {
        JsonObject json = new JsonObject();
        for (int i = 1; i <= count; i++) {
            json.add("TradesLevel" + i, serializeList((List<VillagerTrades.ItemListing>) trades.getOrDefault(i, new ArrayList())));
        }
        return json;
    }

    public static Map<Integer, List<VillagerTrades.ItemListing>> deserialize(JsonObject json) {
        Map<Integer, List<VillagerTrades.ItemListing>> result = new HashMap();
        for (int i = 1; i <= 5; i++) {
            if (json.has("TradesLevel" + i) && json.get("TradesLevel" + i) instanceof JsonArray jsonList) {
                result.put(i, deserializeList(jsonList));
            } else {
                result.put(i, new ArrayList());
            }
        }
        return result;
    }

    public static JsonArray serializeList(List<VillagerTrades.ItemListing> trades) {
        JsonArray list = new JsonArray();
        for (VillagerTrades.ItemListing trade : trades) {
            JsonObject tj = serializeTrade(trade);
            if (tj != null) {
                list.add(tj);
            }
        }
        return list;
    }

    public static List<VillagerTrades.ItemListing> deserializeList(JsonArray jsonList) {
        List<VillagerTrades.ItemListing> list = new ArrayList();
        for (int i = 0; i < jsonList.size(); i++) {
            try {
                VillagerTrades.ItemListing trade = deserializeTrade(jsonList.get(i).getAsJsonObject());
                list.add(trade);
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Error deserializing item listing at index " + i + "!", var4);
            }
        }
        return list;
    }

    public static <T extends VillagerTrades.ItemListing> JsonObject serializeTrade(T listing) {
        if (listing == null) {
            return null;
        } else {
            ItemListingSerializer.IItemListingSerializer serializer = (ItemListingSerializer.IItemListingSerializer) serializers.get(listing.getClass());
            return serializer == null ? null : serializer.serialize(listing);
        }
    }

    public static VillagerTrades.ItemListing deserializeTrade(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(json, "Type"));
        ItemListingSerializer.IItemListingDeserializer deserializer = (ItemListingSerializer.IItemListingDeserializer) deserializers.get(type);
        if (deserializer == null) {
            throw new JsonSyntaxException("Could not deserialize entry as no deserializer was found of type '" + type + "'!");
        } else {
            VillagerTrades.ItemListing trade = deserializer.deserialize(json);
            if (trade == null) {
                throw new JsonSyntaxException("An unknown error occurred while deserializing entry!");
            } else {
                return trade;
            }
        }
    }

    public static void registerDefaultSerializers() {
        registerItemListing(SimpleTrade.TYPE, SimpleTrade.class, SimpleTrade.SERIALIZER);
        registerItemListing(RandomTrade.TYPE, RandomTrade.class, RandomTrade.SERIALIZER);
        registerItemListing(EnchantedItemForCoinsTrade.TYPE, EnchantedItemForCoinsTrade.class, EnchantedItemForCoinsTrade.SERIALIZER);
        registerItemListing(EnchantedBookForCoinsTrade.TYPE, EnchantedBookForCoinsTrade.class, EnchantedBookForCoinsTrade.SERIALIZER);
        registerItemListing(ItemsForMapTrade.TYPE, ItemsForMapTrade.class, ItemsForMapTrade.SERIALIZER);
    }

    public interface IItemListingDeserializer {

        VillagerTrades.ItemListing deserialize(JsonObject var1) throws JsonSyntaxException, ResourceLocationException;
    }

    public interface IItemListingSerializer {

        ResourceLocation getType();

        default JsonObject serialize(VillagerTrades.ItemListing trade) {
            JsonObject json = new JsonObject();
            json.addProperty("Type", this.getType().toString());
            return this.serializeInternal(json, trade);
        }

        JsonObject serializeInternal(JsonObject var1, VillagerTrades.ItemListing var2);
    }
}