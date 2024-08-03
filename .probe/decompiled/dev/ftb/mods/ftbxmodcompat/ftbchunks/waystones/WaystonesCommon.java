package dev.ftb.mods.ftbxmodcompat.ftbchunks.waystones;

import dev.ftb.mods.ftbchunks.api.client.event.MapIconEvent;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class WaystonesCommon {

    private static final Map<ResourceKey<Level>, List<WaystoneMapIcon>> WAYSTONES = new HashMap();

    public static void init() {
        MapIconEvent.MINIMAP.register(WaystonesCommon::mapWidgets);
        MapIconEvent.LARGE_MAP.register(WaystonesCommon::mapWidgets);
        FTBXModCompat.LOGGER.info("[FTB Chunks] Enabled Waystones integration");
    }

    public static void updateWaystones(List<WaystoneData> waystoneData) {
        WAYSTONES.clear();
        waystoneData.forEach(w -> ((List) WAYSTONES.computeIfAbsent(w.dimension(), k -> new ArrayList())).add(w.icon()));
    }

    public static void mapWidgets(MapIconEvent event) {
        List<WaystoneMapIcon> list = (List<WaystoneMapIcon>) WAYSTONES.getOrDefault(event.getDimension(), Collections.emptyList());
        if (!list.isEmpty()) {
            for (WaystoneMapIcon icon : list) {
                event.add(icon);
            }
        }
    }
}