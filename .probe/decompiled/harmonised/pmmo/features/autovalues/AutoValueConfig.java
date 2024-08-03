package harmonised.pmmo.features.autovalues;

import com.mojang.serialization.Codec;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.readers.TomlConfigHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeConfigSpec;

public class AutoValueConfig {

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_AUTO_VALUES;

    private static ForgeConfigSpec.BooleanValue[] REQS_ENABLED;

    private static ForgeConfigSpec.BooleanValue[] EVENTS_ENABLED;

    private static Map<EventType, TomlConfigHelper.ConfigObject<Map<String, Long>>> ITEM_XP_AWARDS;

    private static Map<EventType, TomlConfigHelper.ConfigObject<Map<String, Long>>> BLOCK_XP_AWARDS;

    private static Map<EventType, TomlConfigHelper.ConfigObject<Map<String, Long>>> ENTITY_XP_AWARDS;

    public static TomlConfigHelper.ConfigObject<Map<String, Long>> AXE_OVERRIDE;

    public static TomlConfigHelper.ConfigObject<Map<String, Long>> HOE_OVERRIDE;

    public static TomlConfigHelper.ConfigObject<Map<String, Long>> SHOVEL_OVERRIDE;

    public static ForgeConfigSpec.DoubleValue RARITIES_MODIFIER;

    public static TomlConfigHelper.ConfigObject<Map<String, Long>> BREWABLES_OVERRIDE;

    public static TomlConfigHelper.ConfigObject<Map<String, Long>> SMELTABLES_OVERRIDE;

    private static Map<ReqType, TomlConfigHelper.ConfigObject<Map<String, Integer>>> ITEM_REQS;

    private static Map<ReqType, TomlConfigHelper.ConfigObject<Map<String, Integer>>> BLOCK_REQS;

    public static TomlConfigHelper.ConfigObject<Map<ResourceLocation, Integer>> ITEM_PENALTIES;

    private static TomlConfigHelper.ConfigObject<Map<String, Integer>> AXE_TOOL_OVERRIDE;

    private static TomlConfigHelper.ConfigObject<Map<String, Integer>> SHOVEL_TOOL_OVERRIDE;

    private static TomlConfigHelper.ConfigObject<Map<String, Integer>> HOE_TOOL_OVERRIDE;

    private static TomlConfigHelper.ConfigObject<Map<String, Integer>> SWORD_TOOL_OVERRIDE;

    private static Map<AutoValueConfig.UtensilTypes, TomlConfigHelper.ConfigObject<Map<String, Double>>> UTENSIL_ATTRIBUTES;

    private static Map<AutoValueConfig.WearableTypes, TomlConfigHelper.ConfigObject<Map<String, Double>>> WEARABLE_ATTRIBUTES;

    public static ForgeConfigSpec.ConfigValue<Double> HARDNESS_MODIFIER;

    public static TomlConfigHelper.ConfigObject<Map<String, Double>> ENTITY_ATTRIBUTES;

    public static void setupServer(ForgeConfigSpec.Builder builder) {
        builder.comment("Auto Values estimate values based on item/block/entity properties", "and apply when no other defined requirement or xp value is present").push("Auto_Values");
        ENABLE_AUTO_VALUES = builder.comment("set this to false to disable the auto values system.").define("Auto Values Enabled", true);
        setupReqToggles(builder);
        setupXpGainToggles(builder);
        setupXpGainMaps(builder);
        setupReqMaps(builder);
        configureItemTweaks(builder);
        configureEntityTweaks(builder);
        builder.pop();
    }

    public static boolean isReqEnabled(ReqType type) {
        return REQS_ENABLED[type.ordinal()].get();
    }

    private static void setupReqToggles(ForgeConfigSpec.Builder builder) {
        builder.comment("These settings turn auto-values on/off for the specific requirement type.  These are global settings").push("Req_Toggles");
        List<ReqType> rawReqList = new ArrayList(Arrays.asList(ReqType.values()));
        REQS_ENABLED = (ForgeConfigSpec.BooleanValue[]) rawReqList.stream().map(t -> builder.define(t.toString() + " Req Values Generate", true)).toArray(ForgeConfigSpec.BooleanValue[]::new);
        builder.pop();
    }

    public static boolean isXpGainEnabled(EventType type) {
        return EVENTS_ENABLED[type.ordinal()].get();
    }

    private static void setupXpGainToggles(ForgeConfigSpec.Builder builder) {
        builder.comment("These settings turn auto-values xp awards on/off for the specific event type.  These are global settings").push("XpGain_Toggles");
        List<EventType> rawReqList = new ArrayList(Arrays.asList(EventType.values()));
        EVENTS_ENABLED = (ForgeConfigSpec.BooleanValue[]) rawReqList.stream().map(t -> builder.define(t.toString() + " Xp Award Values Generate", true)).toArray(ForgeConfigSpec.BooleanValue[]::new);
        builder.pop();
    }

    public static Map<String, Long> getItemXpAward(EventType type) {
        TomlConfigHelper.ConfigObject<Map<String, Long>> configEntry = (TomlConfigHelper.ConfigObject<Map<String, Long>>) ITEM_XP_AWARDS.get(type);
        return (Map<String, Long>) (configEntry == null ? new HashMap() : configEntry.get());
    }

    public static Map<String, Long> getBlockXpAward(EventType type) {
        TomlConfigHelper.ConfigObject<Map<String, Long>> configEntry = (TomlConfigHelper.ConfigObject<Map<String, Long>>) BLOCK_XP_AWARDS.get(type);
        return (Map<String, Long>) (configEntry == null ? new HashMap() : configEntry.get());
    }

    public static Map<String, Long> getEntityXpAward(EventType type) {
        TomlConfigHelper.ConfigObject<Map<String, Long>> configEntry = (TomlConfigHelper.ConfigObject<Map<String, Long>>) ENTITY_XP_AWARDS.get(type);
        return (Map<String, Long>) (configEntry == null ? new HashMap() : configEntry.get());
    }

    private static void setupXpGainMaps(ForgeConfigSpec.Builder builder) {
        builder.comment("what skills and xp amount should applicable objects be granted").push("Xp_Awards");
        builder.push("Items");
        ITEM_XP_AWARDS = new HashMap();
        for (EventType type : AutoItem.EVENTTYPES) {
            ITEM_XP_AWARDS.put(type, TomlConfigHelper.defineObject(builder, type.toString() + " Default Xp Award", CodecTypes.LONG_CODEC, Collections.singletonMap(type.autoValueSkill, 10L)));
        }
        BREWABLES_OVERRIDE = TomlConfigHelper.defineObject(builder, EventType.BREW.toString() + " Default Xp Award", CodecTypes.LONG_CODEC, Collections.singletonMap(EventType.BREW.autoValueSkill, 100L));
        SMELTABLES_OVERRIDE = TomlConfigHelper.defineObject(builder, EventType.SMELT.toString() + " Default Xp Award", CodecTypes.LONG_CODEC, Collections.singletonMap(EventType.SMELT.autoValueSkill, 100L));
        builder.pop();
        builder.push("Blocks");
        BLOCK_XP_AWARDS = new HashMap();
        for (EventType type : AutoBlock.EVENTTYPES) {
            BLOCK_XP_AWARDS.put(type, TomlConfigHelper.defineObject(builder, type.toString() + " Default Xp Award", CodecTypes.LONG_CODEC, Collections.singletonMap(type.autoValueSkill, 1L)));
        }
        AXE_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Special override for " + EventType.BLOCK_BREAK.toString() + " and " + EventType.BLOCK_PLACE.toString() + " events when breaking", "blocks in the minecraft:mineable/axe tag."), "Axe Breakable Block Action Override", CodecTypes.LONG_CODEC, Collections.singletonMap("woodcutting", 10L));
        HOE_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Special override for " + EventType.BLOCK_BREAK.toString() + " and " + EventType.BLOCK_PLACE.toString() + " events when breaking", "blocks in the minecraft:mineable/hoe tag."), "Hoe Breakable Block Action Override", CodecTypes.LONG_CODEC, Collections.singletonMap("farming", 10L));
        SHOVEL_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Special override for " + EventType.BLOCK_BREAK.toString() + " and " + EventType.BLOCK_PLACE.toString() + " events when breaking", "blocks in the minecraft:mineable/shovel tag."), "Shovel Breakable Block Action Override", CodecTypes.LONG_CODEC, Collections.singletonMap("excavation", 10L));
        RARITIES_MODIFIER = builder.comment("How much should xp for rare blocks like ores be multiplied by.").defineInRange("Rarities Mulitplier", 10.0, 0.0, Double.MAX_VALUE);
        builder.pop();
        builder.push("Entities");
        ENTITY_XP_AWARDS = new HashMap();
        for (EventType type : AutoEntity.EVENTTYPES) {
            ENTITY_XP_AWARDS.put(type, TomlConfigHelper.defineObject(builder, type.toString() + " Default Xp Award", CodecTypes.LONG_CODEC, Collections.singletonMap(type.autoValueSkill, 1L)));
        }
        builder.pop();
        builder.pop();
    }

    public static Map<String, Integer> getItemReq(ReqType type) {
        TomlConfigHelper.ConfigObject<Map<String, Integer>> configEntry = (TomlConfigHelper.ConfigObject<Map<String, Integer>>) ITEM_REQS.get(type);
        return (Map<String, Integer>) (configEntry == null ? new HashMap() : configEntry.get());
    }

    public static Map<String, Integer> getBlockReq(ReqType type) {
        TomlConfigHelper.ConfigObject<Map<String, Integer>> configEntry = (TomlConfigHelper.ConfigObject<Map<String, Integer>>) BLOCK_REQS.get(type);
        return (Map<String, Integer>) (configEntry == null ? new HashMap() : configEntry.get());
    }

    public static Map<String, Integer> getToolReq(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ShovelItem) {
            return SHOVEL_TOOL_OVERRIDE.get();
        } else if (item instanceof SwordItem) {
            return SWORD_TOOL_OVERRIDE.get();
        } else if (item instanceof AxeItem) {
            return AXE_TOOL_OVERRIDE.get();
        } else {
            return item instanceof HoeItem ? HOE_TOOL_OVERRIDE.get() : getItemReq(ReqType.TOOL);
        }
    }

    private static void setupReqMaps(ForgeConfigSpec.Builder builder) {
        builder.comment("what skills and level should be required to perform the specified action").push("Requirements");
        builder.push("Items");
        ITEM_REQS = new HashMap();
        for (ReqType type : AutoItem.REQTYPES) {
            ITEM_REQS.put(type, TomlConfigHelper.defineObject(builder, type.toString() + " Default Req", CodecTypes.INTEGER_CODEC, Collections.singletonMap(type.defaultSkill, 1)));
        }
        SHOVEL_TOOL_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Tool requirments specifically for shovels.)"), "Shovel TOOL Override", CodecTypes.INTEGER_CODEC, Map.of("excavation", 1));
        SWORD_TOOL_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Tool requirments specifically for swords.)"), "Sword TOOL Override", CodecTypes.INTEGER_CODEC, Map.of("farming", 1));
        AXE_TOOL_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Tool requirments specifically for axes.)"), "Axe TOOL Override", CodecTypes.INTEGER_CODEC, Map.of("woodcutting", 1));
        HOE_TOOL_OVERRIDE = TomlConfigHelper.defineObject(builder.comment("Tool requirments specifically for hoes.)"), "Hoe TOOL Override", CodecTypes.INTEGER_CODEC, Map.of("farming", 1));
        ITEM_PENALTIES = TomlConfigHelper.defineObject(builder.comment("What effects and levels should be applied if a player does not meet an item req"), "Item Penalties", Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT), Map.of(new ResourceLocation("mining_fatigue"), 1, new ResourceLocation("weakness"), 1, new ResourceLocation("slowness"), 1));
        builder.pop();
        builder.push("BLocks");
        BLOCK_REQS = new HashMap();
        for (ReqType type : AutoBlock.REQTYPES) {
            BLOCK_REQS.put(type, TomlConfigHelper.defineObject(builder, type.toString() + " Default Req", CodecTypes.INTEGER_CODEC, Collections.singletonMap(type.defaultSkill, 1)));
        }
        builder.pop();
        builder.pop();
    }

    public static double getUtensilAttribute(AutoValueConfig.UtensilTypes tool, AutoValueConfig.AttributeKey key) {
        return (Double) ((Map) ((TomlConfigHelper.ConfigObject) UTENSIL_ATTRIBUTES.get(tool)).get()).getOrDefault(key.key, 0.0);
    }

    public static double getWearableAttribute(AutoValueConfig.WearableTypes piece, AutoValueConfig.AttributeKey key) {
        return (Double) ((Map) ((TomlConfigHelper.ConfigObject) WEARABLE_ATTRIBUTES.get(piece)).get()).getOrDefault(key.key, 0.0);
    }

    private static void configureItemTweaks(ForgeConfigSpec.Builder builder) {
        builder.comment("Configuration tweaks specific to items.", "'" + AutoValueConfig.AttributeKey.DUR.key + "' determines how much item durability affects auto value calculations", "Default: 0.01 is equal to 1 per hundred durability", "'" + AutoValueConfig.AttributeKey.DMG.key + "' determines how much item damage affects auto value calculations", "'" + AutoValueConfig.AttributeKey.SPD.key + "' determines how much item attack speed affects auto value calculations", "'" + AutoValueConfig.AttributeKey.TIER.key + "' multiplies the default req by this per teir.", "'" + AutoValueConfig.AttributeKey.DIG.key + "' Determines how much item block break speed affects auto value calculations", "'" + AutoValueConfig.AttributeKey.AMR.key + "' Determines how much item armor amount affects auto value calculations", "'" + AutoValueConfig.AttributeKey.KBR.key + "' Determines how much item knockback resistance affects auto value calculations", "'" + AutoValueConfig.AttributeKey.TUF.key + "' Determines how much item armor toughness affects auto value calculations").push("Item_Tweaks");
        UTENSIL_ATTRIBUTES = new HashMap();
        for (AutoValueConfig.UtensilTypes utensil : AutoValueConfig.UtensilTypes.values()) {
            UTENSIL_ATTRIBUTES.put(utensil, TomlConfigHelper.defineObject(builder, utensil.toString() + "_Attributes", CodecTypes.DOUBLE_CODEC, AutoValueConfig.AttributeKey.DEFAULT_ITEM_MAP));
        }
        WEARABLE_ATTRIBUTES = new HashMap();
        for (AutoValueConfig.WearableTypes piece : AutoValueConfig.WearableTypes.values()) {
            WEARABLE_ATTRIBUTES.put(piece, TomlConfigHelper.defineObject(builder, piece.toString() + "_Attributes", CodecTypes.DOUBLE_CODEC, AutoValueConfig.AttributeKey.DEFAULT_ARMOR_MAP));
        }
        HARDNESS_MODIFIER = builder.comment("how much should block hardness contribute to value calculations").define("Block Hardness Modifier", 0.65);
        builder.pop();
    }

    private static void configureEntityTweaks(ForgeConfigSpec.Builder builder) {
        builder.comment("Configuration tweaks specific to entities.", "'" + AutoValueConfig.AttributeKey.HEALTH.key + "' Determines how much entity health affects auto value calculations", "'" + AutoValueConfig.AttributeKey.SPEED.key + "' Determines how much entity speed affects auto value calculations", "'" + AutoValueConfig.AttributeKey.DMG.key + "' Determines how much entity damage affects auto value calculations").push("Entity_Tweaks");
        ENTITY_ATTRIBUTES = TomlConfigHelper.defineObject(builder, "Entity_Attributes", CodecTypes.DOUBLE_CODEC, AutoValueConfig.AttributeKey.DEFAULT_ENTITY_MAP);
        builder.pop();
    }

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        setupServer(SERVER_BUILDER);
        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static enum AttributeKey {

        DUR("Durability", 0.01),
        TIER("Tier", 10.0),
        DMG("Damage", 1.5),
        SPD("Attack_Speed", 10.0),
        DIG("Dig_Speed", 10.0),
        AMR("Armor", 10.0),
        KBR("Knockback_Resistance", 10.0),
        TUF("Toughness", 10.0),
        HEALTH("Health", 0.5),
        SPEED("Move_Speed", 0.15);

        String key;

        double value;

        public static final Map<String, Double> DEFAULT_ITEM_MAP;

        public static Map<String, Double> DEFAULT_ARMOR_MAP;

        public static Map<String, Double> DEFAULT_ENTITY_MAP;

        private AttributeKey(String key, double value) {
            this.key = key;
            this.value = value;
        }

        static {
            DEFAULT_ITEM_MAP = Map.of(DUR.key, DUR.value, TIER.key, TIER.value, DMG.key, DMG.value, SPD.key, SPD.value, DIG.key, DIG.value);
            DEFAULT_ARMOR_MAP = Map.of(DUR.key, DUR.value, AMR.key, AMR.value, KBR.key, KBR.value, TUF.key, TUF.value);
            DEFAULT_ENTITY_MAP = Map.of(DMG.key, DMG.value, HEALTH.key, HEALTH.value, SPEED.key, SPEED.value);
        }
    }

    public static enum UtensilTypes {

        SWORD, PICKAXE, AXE, SHOVEL, HOE
    }

    public static enum WearableTypes {

        HEAD, CHEST, LEGS, BOOTS, WINGS;

        public static AutoValueConfig.WearableTypes fromSlot(EquipmentSlot slot, boolean isElytra) {
            switch(slot) {
                case HEAD:
                    return HEAD;
                case CHEST:
                    return isElytra ? WINGS : CHEST;
                case LEGS:
                    return LEGS;
                case FEET:
                    return BOOTS;
                default:
                    return null;
            }
        }
    }
}