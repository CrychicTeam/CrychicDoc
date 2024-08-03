package dev.xkmc.l2backpack.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.l2backpack.compat.GolemCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;

public class TagGen {

    public static final TagKey<Item> BACKPACKS = ItemTags.create(new ResourceLocation("l2backpack", "backpacks"));

    public static final TagKey<Item> BAGS = ItemTags.create(new ResourceLocation("l2backpack", "bags"));

    public static final TagKey<Item> DRAWERS = ItemTags.create(new ResourceLocation("l2backpack", "drawers"));

    public static final TagKey<Item> SWAPS = ItemTags.create(new ResourceLocation("l2backpack", "swaps"));

    public static final TagKey<Item> ENDER_CHEST = ItemTags.create(new ResourceLocation("l2backpack", "ender_chest_access"));

    public static final TagKey<Item> DIMENSIONAL_STORAGES = ItemTags.create(new ResourceLocation("l2backpack", "dimensional_storages"));

    public static void onBlockTagGen(IntrinsicImpl<Block> pvd) {
        if (ModList.get().isLoaded("modulargolems")) {
            GolemCompat.genBlockTag(pvd);
        }
    }

    public static void onItemTagGen(IntrinsicImpl<Item> pvd) {
    }
}