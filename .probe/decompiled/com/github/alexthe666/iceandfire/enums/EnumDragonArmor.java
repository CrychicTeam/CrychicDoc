package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.item.IafArmorMaterial;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public enum EnumDragonArmor {

    armor_red(12, EnumDragonEgg.RED),
    armor_bronze(13, EnumDragonEgg.BRONZE),
    armor_green(14, EnumDragonEgg.GREEN),
    armor_gray(15, EnumDragonEgg.GRAY),
    armor_blue(12, EnumDragonEgg.BLUE),
    armor_white(13, EnumDragonEgg.WHITE),
    armor_sapphire(14, EnumDragonEgg.SAPPHIRE),
    armor_silver(15, EnumDragonEgg.SILVER),
    armor_electric(12, EnumDragonEgg.ELECTRIC),
    armor_amythest(13, EnumDragonEgg.AMYTHEST),
    armor_copper(14, EnumDragonEgg.COPPER),
    armor_black(15, EnumDragonEgg.BLACK);

    public CustomArmorMaterial material;

    public int armorId;

    public EnumDragonEgg eggType;

    public RegistryObject<Item> helmet;

    public RegistryObject<Item> chestplate;

    public RegistryObject<Item> leggings;

    public RegistryObject<Item> boots;

    public CustomArmorMaterial armorMaterial;

    private EnumDragonArmor(int armorId, EnumDragonEgg eggType) {
        this.armorId = armorId;
        this.eggType = eggType;
    }

    public static void initArmors() {
        for (int i = 0; i < values().length; i++) {
            values()[i].armorMaterial = new IafArmorMaterial("iceandfire:armor_dragon_scales" + (i + 1), 36, new int[] { 5, 7, 9, 5 }, 15, SoundEvents.ARMOR_EQUIP_CHAIN, 2.0F);
            String sub = values()[i].name();
            int finalI = i;
            values()[finalI].helmet = IafItemRegistry.registerItem(sub + "_helmet", () -> new ItemScaleArmor(values()[finalI].eggType, values()[finalI], values()[finalI].armorMaterial, ArmorItem.Type.HELMET));
            values()[finalI].chestplate = IafItemRegistry.registerItem(sub + "_chestplate", () -> new ItemScaleArmor(values()[finalI].eggType, values()[finalI], values()[finalI].armorMaterial, ArmorItem.Type.CHESTPLATE));
            values()[finalI].leggings = IafItemRegistry.registerItem(sub + "_leggings", () -> new ItemScaleArmor(values()[finalI].eggType, values()[finalI], values()[finalI].armorMaterial, ArmorItem.Type.LEGGINGS));
            values()[finalI].boots = IafItemRegistry.registerItem(sub + "_boots", () -> new ItemScaleArmor(values()[finalI].eggType, values()[finalI], values()[finalI].armorMaterial, ArmorItem.Type.BOOTS));
        }
    }

    public static Item getScaleItem(EnumDragonArmor armor) {
        return switch(armor) {
            case armor_red ->
                (Item) IafItemRegistry.DRAGONSCALES_RED.get();
            case armor_bronze ->
                (Item) IafItemRegistry.DRAGONSCALES_BRONZE.get();
            case armor_green ->
                (Item) IafItemRegistry.DRAGONSCALES_GREEN.get();
            case armor_gray ->
                (Item) IafItemRegistry.DRAGONSCALES_GRAY.get();
            case armor_blue ->
                (Item) IafItemRegistry.DRAGONSCALES_BLUE.get();
            case armor_white ->
                (Item) IafItemRegistry.DRAGONSCALES_WHITE.get();
            case armor_sapphire ->
                (Item) IafItemRegistry.DRAGONSCALES_SAPPHIRE.get();
            case armor_silver ->
                (Item) IafItemRegistry.DRAGONSCALES_SILVER.get();
            case armor_electric ->
                (Item) IafItemRegistry.DRAGONSCALES_ELECTRIC.get();
            case armor_amythest ->
                (Item) IafItemRegistry.DRAGONSCALES_AMYTHEST.get();
            case armor_copper ->
                (Item) IafItemRegistry.DRAGONSCALES_COPPER.get();
            case armor_black ->
                (Item) IafItemRegistry.DRAGONSCALES_BLACK.get();
            default ->
                (Item) IafItemRegistry.DRAGONSCALES_RED.get();
        };
    }
}