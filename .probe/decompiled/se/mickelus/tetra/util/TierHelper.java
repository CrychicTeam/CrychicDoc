package se.mickelus.tetra.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.TierSortingRegistry;
import se.mickelus.tetra.data.DataManager;

public class TierHelper {

    public static List<Tier> tiers = Collections.emptyList();

    public static void init() {
        DataManager.instance.tierData.onReload(TierHelper::setupTiers);
    }

    private static void setupTiers() {
        List<ResourceLocation> relevant = (List<ResourceLocation>) DataManager.instance.tierData.getData().values().stream().flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        tiers = (List<Tier>) TierSortingRegistry.getSortedTiers().stream().filter(tier -> relevant.contains(TierSortingRegistry.getName(tier))).collect(Collectors.toList());
    }

    public static int getIndex(Tier tier) {
        return tiers.indexOf(tier);
    }

    @Nullable
    public static Tier getTier(int index) {
        return index > -1 ? (Tier) tiers.get(Math.min(index, tiers.size() - 1)) : null;
    }
}