package com.craisinlord.integrated_api.modinit;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class IATags {

    public static TagKey<Structure> LARGER_LOCATE_SEARCH = TagKey.create(Registries.STRUCTURE, new ResourceLocation("integrated_api", "larger_locate_search"));

    public static void initTags() {
    }
}