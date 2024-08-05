package dev.latvian.mods.kubejs.level.gen.properties;

import dev.latvian.mods.kubejs.level.gen.filter.biome.BiomeFilter;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AddSpawnProperties {

    public MobCategory _category = MobCategory.CREATURE;

    public BiomeFilter biomes = BiomeFilter.ALWAYS_TRUE;

    public int weight = 10;

    public EntityType<?> _entity = null;

    public int minCount = 4;

    public int maxCount = 4;

    public void setCategory(String s) {
        this._category = UtilsJS.mobCategoryByName(s);
    }

    public void setEntity(String s) {
        this._entity = (EntityType<?>) EntityType.byString(s).orElse(null);
    }
}