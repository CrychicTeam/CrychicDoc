package org.embeddedt.embeddium.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.client.model.data.ModelData;

public class ModelDataSnapshotter {

    public static Map<BlockPos, ModelData> getModelDataForSection(ClientLevel world, SectionPos origin) {
        Map<BlockPos, ModelData> forgeMap = world.getModelDataManager().getAt(origin.chunk());
        if (forgeMap.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Object2ObjectOpenHashMap<BlockPos, ModelData> ourMap = new Object2ObjectOpenHashMap();
            BoundingBox volume = new BoundingBox(origin.minBlockX(), origin.minBlockY(), origin.minBlockZ(), origin.maxBlockX(), origin.maxBlockY(), origin.maxBlockZ());
            for (Entry<BlockPos, ModelData> dataEntry : forgeMap.entrySet()) {
                ModelData data = (ModelData) dataEntry.getValue();
                if (data != null && data != ModelData.EMPTY) {
                    BlockPos key = (BlockPos) dataEntry.getKey();
                    if (volume.isInside(key)) {
                        ourMap.put(key, data);
                    }
                }
            }
            return (Map<BlockPos, ModelData>) (ourMap.isEmpty() ? Collections.emptyMap() : ourMap);
        }
    }
}