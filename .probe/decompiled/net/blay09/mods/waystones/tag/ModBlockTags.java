package net.blay09.mods.waystones.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> IS_TELEPORT_TARGET = TagKey.create(Registries.BLOCK, new ResourceLocation("waystones", "is_teleport_target"));

    public static final TagKey<Block> WAYSTONES = TagKey.create(Registries.BLOCK, new ResourceLocation("waystones", "waystones"));

    public static final TagKey<Block> SHARESTONES = TagKey.create(Registries.BLOCK, new ResourceLocation("waystones", "sharestones"));

    public static final TagKey<Block> DYED_SHARESTONES = TagKey.create(Registries.BLOCK, new ResourceLocation("waystones", "dyed_sharestones"));
}