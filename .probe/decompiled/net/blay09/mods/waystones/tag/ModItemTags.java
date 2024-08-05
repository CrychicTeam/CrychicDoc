package net.blay09.mods.waystones.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> BOUND_SCROLLS = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "bound_scrolls"));

    public static final TagKey<Item> RETURN_SCROLLS = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "return_scrolls"));

    public static final TagKey<Item> WARP_SCROLLS = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "warp_scrolls"));

    public static final TagKey<Item> WARP_STONES = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "warp_stones"));

    public static final TagKey<Item> WARP_SHARDS = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "warp_shards"));

    public static final TagKey<Item> SINGLE_USE_WARP_SHARDS = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "single_use_warp_shards"));

    public static final TagKey<Item> WAYSTONES = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "waystones"));

    public static final TagKey<Item> SHARESTONES = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "sharestones"));

    public static final TagKey<Item> DYED_SHARESTONES = TagKey.create(Registries.ITEM, new ResourceLocation("waystones", "dyed_sharestones"));
}