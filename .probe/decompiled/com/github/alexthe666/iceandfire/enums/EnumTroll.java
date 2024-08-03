package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollLeather;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public enum EnumTroll {

    FOREST(IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL, EnumTroll.Weapon.TRUNK, EnumTroll.Weapon.COLUMN_FOREST, EnumTroll.Weapon.AXE, EnumTroll.Weapon.HAMMER), FROST(IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL, EnumTroll.Weapon.COLUMN_FROST, EnumTroll.Weapon.TRUNK_FROST, EnumTroll.Weapon.AXE, EnumTroll.Weapon.HAMMER), MOUNTAIN(IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL, EnumTroll.Weapon.COLUMN, EnumTroll.Weapon.AXE, EnumTroll.Weapon.HAMMER);

    private final EnumTroll.Weapon[] weapons;

    public ResourceLocation TEXTURE;

    public ResourceLocation TEXTURE_STONE;

    public ResourceLocation TEXTURE_EYES;

    public CustomArmorMaterial material;

    public Supplier<Item> leather;

    public Supplier<Item> helmet;

    public Supplier<Item> chestplate;

    public Supplier<Item> leggings;

    public Supplier<Item> boots;

    private EnumTroll(CustomArmorMaterial material, EnumTroll.Weapon... weapons) {
        this.weapons = weapons;
        this.material = material;
        this.TEXTURE = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + ".png");
        this.TEXTURE_STONE = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_stone.png");
        this.TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase(Locale.ROOT) + "_eyes.png");
        this.leather = () -> new ItemTrollLeather(this);
        this.helmet = () -> new ItemTrollArmor(this, material, ArmorItem.Type.HELMET);
        this.chestplate = () -> new ItemTrollArmor(this, material, ArmorItem.Type.CHESTPLATE);
        this.leggings = () -> new ItemTrollArmor(this, material, ArmorItem.Type.LEGGINGS);
        this.boots = () -> new ItemTrollArmor(this, material, ArmorItem.Type.BOOTS);
    }

    public static EnumTroll getBiomeType(Holder<Biome> biome) {
        List<EnumTroll> types = new ArrayList();
        if (BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome)) {
            types.add(FROST);
        }
        if (BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome)) {
            types.add(FOREST);
        }
        if (BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)) {
            types.add(MOUNTAIN);
        }
        return types.isEmpty() ? values()[ThreadLocalRandom.current().nextInt(values().length)] : (EnumTroll) types.get(ThreadLocalRandom.current().nextInt(types.size()));
    }

    public static EnumTroll.Weapon getWeaponForType(EnumTroll troll) {
        return troll.weapons[ThreadLocalRandom.current().nextInt(troll.weapons.length)];
    }

    public static void initArmors() {
        for (EnumTroll troll : values()) {
            troll.leather = IafItemRegistry.registerItem("troll_leather_%s".formatted(troll.name().toLowerCase(Locale.ROOT)), () -> new ItemTrollLeather(troll));
            troll.helmet = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.HEAD), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.HELMET));
            troll.chestplate = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.CHEST), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.CHESTPLATE));
            troll.leggings = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.LEGS), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.LEGGINGS));
            troll.boots = IafItemRegistry.registerItem(ItemTrollArmor.getName(troll, EquipmentSlot.FEET), () -> new ItemTrollArmor(troll, troll.material, ArmorItem.Type.BOOTS));
        }
    }

    public static enum Weapon {

        AXE,
        COLUMN,
        COLUMN_FOREST,
        COLUMN_FROST,
        HAMMER,
        TRUNK,
        TRUNK_FROST;

        public ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/troll/weapon/weapon_" + this.name().toLowerCase(Locale.ROOT) + ".png");

        public Supplier<Item> item = IafItemRegistry.registerItem("troll_weapon_" + this.name().toLowerCase(Locale.ROOT), () -> new ItemTrollWeapon(this));
    }
}