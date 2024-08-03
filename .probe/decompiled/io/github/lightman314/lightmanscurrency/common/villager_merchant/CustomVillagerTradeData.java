package io.github.lightman314.lightmanscurrency.common.villager_merchant;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerTrades;

public class CustomVillagerTradeData {

    private static final ResourceLocation WANDERING_TRADER_ID = new ResourceLocation("lightmanscurrency", "wandering_trader");

    private static final Map<ResourceLocation, Map<Integer, List<VillagerTrades.ItemListing>>> defaultValues = new HashMap();

    private static Map<Integer, List<VillagerTrades.ItemListing>> getEmptyMap() {
        Map<Integer, List<VillagerTrades.ItemListing>> map = new HashMap();
        for (int i = 1; i <= 5; i++) {
            map.put(i, new ArrayList());
        }
        return map;
    }

    private static Map<Integer, List<VillagerTrades.ItemListing>> getDefaultVillagerData(@Nonnull ResourceLocation villager) {
        return (Map<Integer, List<VillagerTrades.ItemListing>>) defaultValues.getOrDefault(villager, getEmptyMap());
    }

    public static void registerDefaultWanderingTrades(@Nonnull List<VillagerTrades.ItemListing> genericValues, @Nonnull List<VillagerTrades.ItemListing> rareValues) {
        Map<Integer, List<VillagerTrades.ItemListing>> valueMap = new HashMap();
        valueMap.put(1, genericValues);
        valueMap.put(2, rareValues);
        registerDefaultFile(WANDERING_TRADER_ID, valueMap);
    }

    public static void registerDefaultFile(@Nonnull ResourceLocation villager, @Nonnull Map<Integer, List<VillagerTrades.ItemListing>> value) {
        if (defaultValues.containsKey(villager)) {
            LightmansCurrency.LogWarning("Attempted to register default villager data of type '" + villager + "' twice!");
        } else {
            for (int i = 1; i <= 5; i++) {
                if (value.get(i) == null && villager != WANDERING_TRADER_ID) {
                    LightmansCurrency.LogError("Default value for '" + villager + "' does not have all five valid entries!");
                }
            }
            defaultValues.put(villager, value);
        }
    }

    public static Pair<List<VillagerTrades.ItemListing>, List<VillagerTrades.ItemListing>> getWanderingTraderData() {
        Map<Integer, List<VillagerTrades.ItemListing>> value = getVillagerData(WANDERING_TRADER_ID);
        return Pair.of((List) value.getOrDefault(1, new ArrayList()), (List) value.getOrDefault(2, new ArrayList()));
    }

    @Nonnull
    public static Map<Integer, List<VillagerTrades.ItemListing>> getVillagerData(@Nonnull ResourceLocation villager) {
        File file = getVillagerDataFile(villager);
        if (file.exists()) {
            try {
                String text = Files.readString(file.toPath());
                JsonObject json = GsonHelper.parse(text);
                return ItemListingSerializer.deserialize(json);
            } catch (Throwable var5) {
                LightmansCurrency.LogError("Error loading villager data file '" + file.getName() + "'!", var5);
            }
        } else {
            try {
                File dir = file.getParentFile();
                dir.mkdirs();
                Map<Integer, List<VillagerTrades.ItemListing>> defaultValues = getDefaultVillagerData(villager);
                FileUtil.writeStringToFile(file, FileUtil.GSON.toJson(ItemListingSerializer.serialize(defaultValues, villager.equals(WANDERING_TRADER_ID) ? 2 : 5)));
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Error creating default villager data file '" + file.getName() + "'!", var4);
            }
        }
        return getDefaultVillagerData(villager);
    }

    @Nonnull
    public static File getVillagerDataFile(@Nonnull ResourceLocation villager) {
        String filePath = "config/trades/" + villager.getNamespace() + "/custom_" + villager.getPath() + "_trades.json";
        return new File(filePath);
    }
}