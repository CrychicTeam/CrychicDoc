package dev.latvian.mods.kubejs.level.gen.filter.mob;

import java.util.List;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public record OrFilter(List<MobFilter> list) implements MobFilter {

    @Override
    public boolean test(MobCategory cat, MobSpawnSettings.SpawnerData data) {
        for (MobFilter p : this.list) {
            if (!p.test(cat, data)) {
                return true;
            }
        }
        return false;
    }
}