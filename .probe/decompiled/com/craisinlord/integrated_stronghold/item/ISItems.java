package com.craisinlord.integrated_stronghold.item;

import com.craisinlord.integrated_stronghold.sound.ISSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.DiscFragmentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ISItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "integrated_stronghold");

    public static final Rarity RARITY_ANTIQUE = Rarity.create("integrated_stronghold:antique", ChatFormatting.DARK_PURPLE);

    public static final RegistryObject<Item> DISC_FRAGMENT_SIGHT = ITEMS.register("disc_fragment_sight", () -> new DiscFragmentItem(new Item.Properties()));

    public static final RegistryObject<Item> MUSIC_DISC_SIGHT = ITEMS.register("music_disc_sight", () -> new RecordItem(14, ISSounds.SIGHT, new Item.Properties().stacksTo(1).rarity(RARITY_ANTIQUE), 3260));

    public static final RegistryObject<Item> MUSIC_DISC_FORLORN = ITEMS.register("music_disc_forlorn", () -> new RecordItem(14, ISSounds.FORLORN, new Item.Properties().stacksTo(1).rarity(RARITY_ANTIQUE), 2960));
}