package io.redspace.ironsspellbooks.config;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class ServerConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static final ServerConfigs.SpellConfigParameters DEFAULT_CONFIG = new ServerConfigs.SpellConfigParameters(null, () -> true, SchoolRegistry.EVOCATION_RESOURCE::toString, () -> 10, () -> SpellRarity.COMMON, () -> 1.0, () -> 1.0, () -> 10.0, () -> true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> SWORDS_CONSUME_MANA = BUILDER.worldRestart().define("swordsConsumeMana", true);

    public static final ForgeConfigSpec.ConfigValue<Double> SWORDS_CD_MULTIPLIER = BUILDER.worldRestart().define("swordsCooldownMultiplier", 0.5);

    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_ATTACK_OWN_SUMMONS = BUILDER.worldRestart().define("canAttackOwnSummons", false);

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_UPGRADES = BUILDER.worldRestart().define("maxUpgrades", 3);

    public static final ForgeConfigSpec.ConfigValue<Double> MANA_SPAWN_PERCENT = BUILDER.worldRestart().define("manaSpawnPercent", 0.0);

    public static final ForgeConfigSpec.ConfigValue<Boolean> RUN_WORLD_UPGRADER = BUILDER.worldRestart().define("runWorldUpgrader", true);

    public static final ForgeConfigSpec.ConfigValue<Double> SCROLL_RECYCLE_CHANCE = BUILDER.worldRestart().define("scrollRecycleChance", 0.5);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> UPGRADE_WHITELIST = BUILDER.defineList("upgradeWhitelist", ArrayList::new, string -> true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> UPGRADE_BLACKLIST = BUILDER.defineList("upgradeBlacklist", ArrayList::new, string -> true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IMBUE_WHITELIST = BUILDER.defineList("imbueWhitelist", ArrayList::new, string -> true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IMBUE_BLACKLIST = BUILDER.defineList("imbueBlacklist", ArrayList::new, string -> true);

    public static final ForgeConfigSpec.ConfigValue<Integer> PRIEST_TOWER_SPAWNRATE = BUILDER.define("priestHouseWeight", 4);

    public static final ForgeConfigSpec.ConfigValue<Boolean> ALLOW_CAULDRON_BREWING = BUILDER.worldRestart().define("allowCauldronBrewing", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> FURLED_MAPS_SKIP_CHUNKS = BUILDER.worldRestart().define("furledMapSkipsExistingChunks", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> APPLY_ALL_MULTIHAND_ATTRIBUTES = BUILDER.worldRestart().define("applyAllMultihandAttributes", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_CREEPER_THUNDERHIT = BUILDER.worldRestart().define("betterCreeperThunderHit", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> SPELL_GREIFING = BUILDER.worldRestart().define("spellGriefing", false);

    public static final ForgeConfigSpec.ConfigValue<Boolean> ADDITIONAL_WANDERING_TRADER_TRADES = BUILDER.worldRestart().define("additionalWanderingTraderTrades", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_ADVENTURE_MODE_CASTING = BUILDER.worldRestart().define("disableAdventureModeCasting", false);

    public static final ForgeConfigSpec.ConfigValue<List<? extends Double>> RARITY_CONFIG = BUILDER.worldRestart().comment(String.format("rarityConfig array values must sum to 1: [%s, %s, %s, %s, %s]. Default: [.3d, .25d, .2d, .15d, .1d]", SpellRarity.COMMON, SpellRarity.UNCOMMON, SpellRarity.RARE, SpellRarity.EPIC, SpellRarity.LEGENDARY)).defineList("rarityConfig", List.of(0.3, 0.25, 0.2, 0.15, 0.1), x -> true);

    public static final Set<Item> UPGRADE_WHITELIST_ITEMS = new HashSet();

    public static final Set<Item> UPGRADE_BLACKLIST_ITEMS = new HashSet();

    public static final Set<Item> IMBUE_WHITELIST_ITEMS = new HashSet();

    public static final Set<Item> IMBUE_BLACKLIST_ITEMS = new HashSet();

    private static final Map<String, ServerConfigs.SpellConfigParameters> SPELL_CONFIGS = new HashMap();

    public static ServerConfigs.SpellConfigParameters getSpellConfig(AbstractSpell abstractSpell) {
        return (ServerConfigs.SpellConfigParameters) SPELL_CONFIGS.getOrDefault(abstractSpell.getSpellId(), DEFAULT_CONFIG);
    }

    public static Map<String, ServerConfigs.SpellConfigParameters> getSpellConfigs() {
        return SPELL_CONFIGS;
    }

    public static void onConfigReload() {
        IronsSpellbooks.LOGGER.debug("ServerConfigs load item blacklists:");
        cacheItemList(UPGRADE_WHITELIST.get(), UPGRADE_WHITELIST_ITEMS);
        cacheItemList(UPGRADE_BLACKLIST.get(), UPGRADE_BLACKLIST_ITEMS);
        cacheItemList(IMBUE_WHITELIST.get(), IMBUE_WHITELIST_ITEMS);
        cacheItemList(IMBUE_BLACKLIST.get(), IMBUE_BLACKLIST_ITEMS);
    }

    private static void cacheItemList(List<? extends String> ids, Set<Item> output) {
        output.clear();
        for (String name : ids) {
            try {
                if (name.startsWith("#")) {
                    TagKey<Item> tag = new TagKey<>(Registries.ITEM, new ResourceLocation(name.substring(1)));
                    output.addAll(ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(tag)).toList());
                } else {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                    if (item != null) {
                        output.add(item);
                    } else {
                        IronsSpellbooks.LOGGER.warn("Unable to add item to config, no such item id: {}", name);
                    }
                }
            } catch (Exception var5) {
                IronsSpellbooks.LOGGER.warn("Unable to validate item config: {}", var5.getMessage());
            }
        }
    }

    private static void createSpellConfig(AbstractSpell spell) {
        DefaultConfig config = spell.getDefaultConfig();
        BUILDER.push(spell.getSpellId());
        SPELL_CONFIGS.put(spell.getSpellId(), new ServerConfigs.SpellConfigParameters(config, BUILDER.define("Enabled", config.enabled), BUILDER.define("School", config.schoolResource.toString()), BUILDER.define("MaxLevel", config.maxLevel), BUILDER.defineEnum("MinRarity", config.minRarity), BUILDER.define("ManaCostMultiplier", 1.0), BUILDER.define("SpellPowerMultiplier", 1.0), BUILDER.define("CooldownInSeconds", config.cooldownInSeconds), BUILDER.define("AllowCrafting", config.allowCrafting)));
        BUILDER.pop();
    }

    private static String createSpellConfigTitle(String str) {
        String[] words = str.split("[_| ]");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }
        return (String) ((Stream) Arrays.stream(words).sequential()).collect(Collectors.joining("-"));
    }

    static {
        BUILDER.comment("Other Configuration");
        BUILDER.push("Misc");
        BUILDER.comment("Whether or not imbued weapons require mana to be casted. Default: true");
        BUILDER.comment("The multiplier on the cooldown of imbued weapons. Default: 0.5 (50% of default cooldown)");
        BUILDER.comment("Whether or not players can harm their own magic summons. Default: false");
        BUILDER.comment("The maximum amount of times an applicable piece of equipment can be upgraded in the arcane anvil. Default: 3");
        BUILDER.comment("From 0-1, the percent of max mana a player respawns with. Default: 0.0");
        BUILDER.comment("If true the world will attempt to be upgraded from an older version of ISS");
        BUILDER.comment("From 0-1, the percent chance for scrolls to be successfully recycled. Default: 0.5 (50%)");
        BUILDER.comment("Whether or not potions should be allowed to be brewed in the alchemist cauldron)");
        BUILDER.comment("Whether or not Furled Map items should skip chunks when searching for structures (only find new structures). Can impact performance while searching. Default: true");
        BUILDER.comment("Whether or not casting items should apply all attributes while in the offhand, or just magic related ones. Default: true");
        BUILDER.comment("Whether or not creepers should be healed and become fire immune when struck by lightning. Default: true");
        BUILDER.comment("Whether or not spells such as Fireball or Fire Breath should destroy terrain or create fire. Default: false");
        BUILDER.comment("Whether or not the wandering trader can have magic related trades, such as ink or scrolls. Default: true");
        BUILDER.comment("Whether casting spells should be disabled in adventure mode. Default: false");
        BUILDER.pop();
        BUILDER.push("Upgrade Overrides");
        BUILDER.comment("Use these lists to change what items can interact with the Arcane Anvil's upgrade system. This can also be done via datapack.");
        BUILDER.comment("Upgrade Whitelist. Use an item's id to allow it to be upgraded, ex: \"minecraft:iron_sword\"");
        BUILDER.comment("Upgrade Blacklist. Use an item's id to prevent it from being upgraded, ex: \"minecraft:iron_sword\"");
        BUILDER.pop();
        BUILDER.push("Imbue Overrides");
        BUILDER.comment("Use these lists to change what items can interact with the Arcane Anvil's imbue system.");
        BUILDER.comment("/!\\ Unsupported item types are not guaranteed to work out of the box.");
        BUILDER.comment("Imbue Whitelist. Use an item's id to allow it to be imbued, ex: \"minecraft:iron_sword\"");
        BUILDER.comment("Imbue Blacklist. Use an item's id to prevent it from being imbued, ex: \"minecraft:iron_sword\"");
        BUILDER.pop();
        BUILDER.push("Worldgen");
        BUILDER.comment("The weight of the priest house spawning in a village. Default: 4");
        BUILDER.pop();
        BUILDER.comment("Individual Spell Configuration");
        BUILDER.push("Spells");
        ((Map) SpellDiscovery.getSpellsForConfig().stream().collect(Collectors.groupingBy(x -> x.getDefaultConfig().schoolResource))).forEach((school, spells) -> {
            BUILDER.comment(school.toString());
            spells.forEach(ServerConfigs::createSpellConfig);
        });
        BUILDER.pop();
    }

    public static class SpellConfigParameters {

        final Supplier<Boolean> ENABLED;

        final Supplier<String> SCHOOL;

        final LazyOptional<SchoolType> ACTUAL_SCHOOL;

        final Supplier<Integer> MAX_LEVEL;

        final Supplier<SpellRarity> MIN_RARITY;

        final Supplier<Double> M_MULT;

        final Supplier<Double> P_MULT;

        final Supplier<Double> CS;

        final Supplier<Boolean> ALLOW_CRAFTING;

        SpellConfigParameters(DefaultConfig defaultConfig, Supplier<Boolean> ENABLED, Supplier<String> SCHOOL, Supplier<Integer> MAX_LEVEL, Supplier<SpellRarity> MIN_RARITY, Supplier<Double> M_MULT, Supplier<Double> P_MULT, Supplier<Double> CS, Supplier<Boolean> ALLOW_CRAFTING) {
            this.ENABLED = ENABLED;
            this.SCHOOL = SCHOOL;
            this.MAX_LEVEL = MAX_LEVEL;
            this.MIN_RARITY = MIN_RARITY;
            this.M_MULT = M_MULT;
            this.P_MULT = P_MULT;
            this.CS = CS;
            this.ALLOW_CRAFTING = ALLOW_CRAFTING;
            this.ACTUAL_SCHOOL = LazyOptional.of(() -> {
                if (ResourceLocation.isValidResourceLocation((String) SCHOOL.get())) {
                    SchoolType school = SchoolRegistry.getSchool(new ResourceLocation((String) SCHOOL.get()));
                    if (school != null) {
                        return school;
                    }
                }
                IronsSpellbooks.LOGGER.warn("Bad school config entry: {}. Reverting to default ({}).", SCHOOL.get(), defaultConfig.schoolResource);
                return SchoolRegistry.getSchool(defaultConfig.schoolResource);
            });
        }

        public boolean enabled() {
            return (Boolean) this.ENABLED.get();
        }

        public int maxLevel() {
            return (Integer) this.MAX_LEVEL.get();
        }

        public SpellRarity minRarity() {
            return (SpellRarity) this.MIN_RARITY.get();
        }

        public double powerMultiplier() {
            return (Double) this.P_MULT.get();
        }

        public double manaMultiplier() {
            return (Double) this.M_MULT.get();
        }

        public int cooldownInTicks() {
            return (int) ((Double) this.CS.get() * 20.0);
        }

        public boolean allowCrafting() {
            return (Boolean) this.ALLOW_CRAFTING.get();
        }

        public SchoolType school() {
            return (SchoolType) this.ACTUAL_SCHOOL.resolve().get();
        }
    }
}