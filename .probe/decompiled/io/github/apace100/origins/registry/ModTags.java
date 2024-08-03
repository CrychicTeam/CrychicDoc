package io.github.apace100.origins.registry;

import io.github.edwinmindcraft.origins.data.tag.OriginsBlockTags;
import io.github.edwinmindcraft.origins.data.tag.OriginsItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Item> MEAT = OriginsItemTags.MEAT;

    public static final TagKey<Block> UNPHASABLE = OriginsBlockTags.UNPHASABLE;

    public static final TagKey<Block> NATURAL_STONE = OriginsBlockTags.NATURAL_STONE;

    public static final TagKey<Item> RANGED_WEAPONS = OriginsItemTags.RANGED_WEAPONS;

    public static void register() {
    }
}