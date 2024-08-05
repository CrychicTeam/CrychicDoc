package io.redspace.ironsspellbooks.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {

    public static final TagKey<Item> SCHOOL_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "school_focus"));

    public static final TagKey<Item> FIRE_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "fire_focus"));

    public static final TagKey<Item> ICE_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "ice_focus"));

    public static final TagKey<Item> LIGHTNING_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "lightning_focus"));

    public static final TagKey<Item> ENDER_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "ender_focus"));

    public static final TagKey<Item> HOLY_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "holy_focus"));

    public static final TagKey<Item> BLOOD_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "blood_focus"));

    public static final TagKey<Item> EVOCATION_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "evocation_focus"));

    public static final TagKey<Item> ELDRITCH_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "eldritch_focus"));

    public static final TagKey<Item> NATURE_FOCUS = ItemTags.create(new ResourceLocation("irons_spellbooks", "nature_focus"));

    public static final TagKey<Item> INSCRIBED_RUNES = ItemTags.create(new ResourceLocation("irons_spellbooks", "inscribed_rune"));

    public static final TagKey<Block> SPECTRAL_HAMMER_MINEABLE = BlockTags.create(new ResourceLocation("irons_spellbooks", "spectral_hammer_mineable"));

    public static final TagKey<Block> GUARDED_BY_WIZARDS = BlockTags.create(new ResourceLocation("irons_spellbooks", "guarded_by_wizards"));

    public static final TagKey<Structure> WAYWARD_COMPASS_LOCATOR = TagKey.create(Registries.STRUCTURE, new ResourceLocation("irons_spellbooks", "wayward_compass_locator"));

    public static final TagKey<EntityType<?>> ALWAYS_HEAL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("irons_spellbooks", "always_heal"));

    public static final TagKey<EntityType<?>> CANT_ROOT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("irons_spellbooks", "cant_root"));

    public static final TagKey<EntityType<?>> VILLAGE_ALLIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("irons_spellbooks", "village_allies"));

    public static final TagKey<EntityType<?>> CANT_USE_PORTAL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("irons_spellbooks", "cant_use_portal"));

    public static final TagKey<Biome> NO_DEFAULT_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation("forge", "no_default_monsters"));

    private static TagKey<DamageType> create(String tag) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("irons_spellbooks", tag));
    }
}