package com.craisinlord.idas.item;

import com.craisinlord.idas.sound.IDASSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "idas", bus = Bus.MOD)
public class IDASItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "idas");

    public static final RegistryObject<Item> MUSIC_DISC_SLITHER = ITEMS.register("music_disc_slither", () -> new RecordItem(1, IDASSounds.SLITHER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2400));

    public static final RegistryObject<Item> MUSIC_DISC_CALIDUM = ITEMS.register("music_disc_calidum", () -> new RecordItem(2, IDASSounds.CALIDUM, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3900));

    public static final RegistryObject<Item> DISC_FRAGMENT_SLITHER = ITEMS.register("disc_fragment_slither", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}