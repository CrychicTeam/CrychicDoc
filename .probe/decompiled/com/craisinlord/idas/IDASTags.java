package com.craisinlord.idas;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class IDASTags {

    public static TagKey<Structure> APPLIES_MINING_FATIGUE = TagKey.create(Registries.STRUCTURE, new ResourceLocation("idas", "applies_mining_fatigue"));

    public static void initTags() {
    }
}