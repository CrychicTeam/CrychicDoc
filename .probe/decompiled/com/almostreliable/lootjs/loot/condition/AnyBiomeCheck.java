package com.almostreliable.lootjs.loot.condition;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class AnyBiomeCheck extends BiomeCheck {

    public AnyBiomeCheck(List<ResourceKey<Biome>> biomes, List<TagKey<Biome>> tags) {
        super(biomes, tags);
    }

    @Override
    protected boolean match(Holder<Biome> biomeHolder) {
        for (ResourceKey<Biome> biome : this.biomes) {
            if (biomeHolder.is(biome)) {
                return true;
            }
        }
        for (TagKey<Biome> tag : this.tags) {
            if (biomeHolder.is(tag)) {
                return true;
            }
        }
        return false;
    }
}