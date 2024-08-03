package dev.latvian.mods.kubejs.level.gen.filter.mob;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.regex.Pattern;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public record RegexIDFilter(Pattern pattern) implements MobFilter {

    @Override
    public boolean test(MobCategory cat, MobSpawnSettings.SpawnerData data) {
        return this.pattern.matcher(RegistryInfo.ENTITY_TYPE.getId(data.type).toString()).find();
    }
}