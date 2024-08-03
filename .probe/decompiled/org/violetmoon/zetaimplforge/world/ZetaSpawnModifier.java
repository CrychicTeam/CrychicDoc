package org.violetmoon.zetaimplforge.world;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.violetmoon.zeta.config.type.CostSensitiveEntitySpawnConfig;
import org.violetmoon.zeta.world.EntitySpawnHandler;

public class ZetaSpawnModifier {

    public static void modifyBiome(Holder<Biome> biome, EntitySpawnHandler handler, ModifiableBiomeInfo.BiomeInfo.Builder biomeInfoBuilder) {
        MobSpawnSettingsBuilder builder = biomeInfoBuilder.getMobSpawnSettings();
        for (EntitySpawnHandler.TrackedSpawnConfig c : handler.trackedSpawnConfigs) {
            List<MobSpawnSettings.SpawnerData> l = builder.getSpawner(c.classification);
            if (!c.secondary) {
                l.removeIf(e -> e.type.equals(c.entityType));
            }
            if (c.config.isEnabled() && c.config.biomes.canSpawn(biome)) {
                l.add(c.getEntry());
            }
            if (c.config instanceof CostSensitiveEntitySpawnConfig csc) {
                builder.m_48370_(c.entityType, csc.spawnCost, csc.maxCost);
            }
        }
    }
}