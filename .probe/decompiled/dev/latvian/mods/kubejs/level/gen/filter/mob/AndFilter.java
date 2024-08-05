package dev.latvian.mods.kubejs.level.gen.filter.mob;

import java.util.List;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public record AndFilter(List<MobFilter> list) implements MobFilter {

    @Override
    public boolean test(MobCategory cat, MobSpawnSettings.SpawnerData data) {
        for (MobFilter p : this.list) {
            if (!p.test(cat, data)) {
                return false;
            }
        }
        return true;
    }
}