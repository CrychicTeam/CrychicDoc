package software.bernie.example.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.example.item.GeckoArmorItem;
import software.bernie.example.item.GeckoHabitatItem;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.example.item.WolfArmorItem;

public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "geckolib");

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "geckolib");

    public static final RegistryObject<BlockItem> GECKO_HABITAT = ITEMS.register("gecko_habitat", () -> new GeckoHabitatItem(BlockRegistry.GECKO_HABITAT.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> FERTILIZER = ITEMS.register("fertilizer", () -> new BlockItem(BlockRegistry.FERTILIZER.get(), new Item.Properties()));

    public static final RegistryObject<JackInTheBoxItem> JACK_IN_THE_BOX = ITEMS.register("jack_in_the_box", () -> new JackInTheBoxItem(new Item.Properties()));

    public static final RegistryObject<WolfArmorItem> WOLF_ARMOR_HELMET = ITEMS.register("wolf_armor_helmet", () -> new WolfArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<WolfArmorItem> WOLF_ARMOR_CHESTPLATE = ITEMS.register("wolf_armor_chestplate", () -> new WolfArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<WolfArmorItem> WOLF_ARMOR_LEGGINGS = ITEMS.register("wolf_armor_leggings", () -> new WolfArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<WolfArmorItem> WOLF_ARMOR_BOOTS = ITEMS.register("wolf_armor_boots", () -> new WolfArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<GeckoArmorItem> GECKO_ARMOR_HELMET = ITEMS.register("gecko_armor_helmet", () -> new GeckoArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<GeckoArmorItem> GECKO_ARMOR_CHESTPLATE = ITEMS.register("gecko_armor_chestplate", () -> new GeckoArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<GeckoArmorItem> GECKO_ARMOR_LEGGINGS = ITEMS.register("gecko_armor_leggings", () -> new GeckoArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<GeckoArmorItem> GECKO_ARMOR_BOOTS = ITEMS.register("gecko_armor_boots", () -> new GeckoArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> BAT_SPAWN_EGG = ITEMS.register("bat_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.BAT, 2039583, 855309, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> BIKE_SPAWN_EGG = ITEMS.register("bike_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.BIKE, 13886438, 15331829, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> RACE_CAR_SPAWN_EGG = ITEMS.register("race_car_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.RACE_CAR, 10360342, 5855577, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> PARASITE_SPAWN_EGG = ITEMS.register("parasite_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.PARASITE, 3154457, 11316396, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> MUTANT_ZOMBIE_SPAWN_EGG = ITEMS.register("mutant_zombie_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.MUTANT_ZOMBIE, 3957302, 5740937, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> FAKE_GLASS_SPAWN_EGG = ITEMS.register("fake_glass_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.FAKE_GLASS, 14483456, 14221303, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> COOL_KID_SPAWN_EGG = ITEMS.register("cool_kid_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.COOL_KID, 6236721, 7288382, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> GREMLIN_SPAWN_EGG = ITEMS.register("gremlin_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.GREMLIN, 5263440, 6316128, new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> GECKOLIB_TAB = TABS.register("geckolib_examples", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.geckolib.geckolib_examples")).icon(() -> new ItemStack(JACK_IN_THE_BOX.get())).displayItems((enabledFeatures, entries) -> {
        entries.accept(JACK_IN_THE_BOX.get());
        entries.accept(GECKO_ARMOR_HELMET.get());
        entries.accept(GECKO_ARMOR_CHESTPLATE.get());
        entries.accept(GECKO_ARMOR_LEGGINGS.get());
        entries.accept(GECKO_ARMOR_BOOTS.get());
        entries.accept(WOLF_ARMOR_HELMET.get());
        entries.accept(WOLF_ARMOR_CHESTPLATE.get());
        entries.accept(WOLF_ARMOR_LEGGINGS.get());
        entries.accept(WOLF_ARMOR_BOOTS.get());
        entries.accept(GECKO_HABITAT.get());
        entries.accept(FERTILIZER.get());
        entries.accept(BAT_SPAWN_EGG.get());
        entries.accept(BIKE_SPAWN_EGG.get());
        entries.accept(RACE_CAR_SPAWN_EGG.get());
        entries.accept(PARASITE_SPAWN_EGG.get());
        entries.accept(MUTANT_ZOMBIE_SPAWN_EGG.get());
        entries.accept(GREMLIN_SPAWN_EGG.get());
        entries.accept(FAKE_GLASS_SPAWN_EGG.get());
        entries.accept(COOL_KID_SPAWN_EGG.get());
    }).build());
}