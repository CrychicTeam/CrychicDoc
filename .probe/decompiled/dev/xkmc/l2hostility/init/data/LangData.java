package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public enum LangData {

    SHIFT("tooltip.shift", "Press SHIFT to show spawn condition", 0),
    LV("tooltip.lv", "Lv.%s", 1),
    TOOLTIP_DISABLE("tooltip.disable", "%s enchantments disabled for %ss. Any new enchantment will vanish.", 2),
    TOOLTIP_SELF_EFFECT("tooltip.self_effect", "Mob gains continuous effect: ", 0),
    TOOLTIP_TARGET_EFFECT("tooltip.target_effect", "Mob inflict effect on hit: ", 0),
    TOOLTIP_LEGENDARY("tooltip.legendary", "Legendary", 0),
    TOOLTIP_MIN_LEVEL("tooltip.min_level", "Minimum mob level: %s", 1),
    TOOLTIP_LEVEL_COST("tooltip.level_cost", "Mob level cost: %s", 1),
    TOOLTIP_WEIGHT("tooltip.weight", "Weight: %s", 1),
    TOOLTIP_BANNED("tooltip.banned", "This trait is disabled.", 0),
    TOOLTIP_SEAL_DATA("tooltip.sealed_item", "Item sealed within:", 0),
    TOOLTIP_SEAL_TIME("tooltip.seal_time", "Hold use to unseal. Takes %ss.", 1),
    TOOLTIP_WITCH_CHARGE("tooltip.witch_charge", "Right click to fire. When hit entity, increase duration of all harmful effects longer than %ss by %s%%. At most increase to %ss.", 3),
    TOOLTIP_WITCH_ETERNAL("tooltip.witch_eternal", "Right click to fire. When hit entity, all harmful effects longer than %ss becomes infinite duration.", 1),
    TOOLTIP_WITCH_BOTTLE("tooltip.witch_bottle", "Increase duration of all effects longer than %ss by %s%%. At most increase to %ss.", 3),
    ITEM_WAND_AI("item.wand.ai", "Right click to remove or restore mob AI.", 0),
    ITEM_WAND_EQUIPMENT("item.wand.equipment", "Right click to open mob equipment menu. Shift right click to open mob curios menu.", 0),
    ITEM_WAND_TARGET("item.wand.target", "Right click 2 mobs to make them fight each other.", 0),
    ITEM_WAND_ADDER("item.wand.adder", "Right click blocks to select trait. Right click mobs to select trait rank. Press shift to select in opposite direction.", 0),
    ITEM_GLASSES("item.equipment.glasses", "Allow you to see invisible mobs, and see mobs when you have blindness or darkness effects", 0),
    ITEM_DETECTOR("item.equipment.detector", "Shows player and regional difficulty information when held in hand / offhand", 0),
    ITEM_SECTION_RENDER("item.equipment.section_render", "Hold [%s] and Equip [%s] to see regions cleared of hostility", 2),
    ITEM_ORB_USE("item.equipment.orb_use", "Usable only when you hold [%s] in off hand and Equip [%s]", 2),
    ITEM_SPAWNER("item.spawner", "Summon strong mobs. Kill them all to make a chunk section no longer spawn mobs with levels", 0),
    ITEM_ORB("item.consumable.orb", "Make %sx%sx%s chunk sections no longer spawn mobs with levels.", 3),
    ITEM_BOTTLE_CURSE("item.consumable.bottle_of_curse", "Increase player difficulty by %s", 1),
    ITEM_BOTTLE_SANITY("item.consumable.bottle_of_sanity", "Clear all base player difficulty", 0),
    ITEM_BOOK_COPY("item.book.copy", "Merge it with enchanted book in anvil to copy.", 0),
    ITEM_BOOK_EVERYTHING_SHIFT("item.book.everything_shift", "Shift right click with it to turn it into a book with all enchantments obtainable from enchantment table.", 0),
    ITEM_BOOK_EVERYTHING_USE("item.book.everything_use", "Rename this item with ID of an enchantment obtainable from enchantment table, then right click to generate a book of that enchantment, costing player experience.", 0),
    ITEM_BOOK_EVERYTHING_INVALID("item.book.everything_invalid", "Name is not ID of an enchantment", 0),
    ITEM_BOOK_EVERYTHING_READY("item.book.everything_ready", "Enchantment %s cost %s levels", 2),
    ITEM_BOOK_EVERYTHING_FORBIDDEN("item.book.everything_forbidden", "%s is not obtainable", 1),
    ITEM_CHARM_ENVY("item.equipment.curse_of_envy", "Get trait items when you kill mobs with traits, with a chance of %s%% per trait rank", 1),
    ITEM_CHARM_GLUTTONY("item.equipment.curse_of_gluttony", "Get Bottle of Curse when you kill mobs with level, with a chance of %s%% per level", 1),
    ITEM_CHARM_GREED("item.equipment.curse_of_greed", "x%s hostility loot drop chance", 1),
    ITEM_CHARM_LUST("item.equipment.curse_of_lust", "Mobs you kill will drop all equipments", 0),
    ITEM_CHARM_PRIDE("item.equipment.curse_of_pride", "Gain %s%% health and %s%% attack damage per difficulty level", 2),
    ITEM_CHARM_SLOTH("item.equipment.curse_of_sloth", "You will not gain difficulty by killing mobs", 0),
    ITEM_CHARM_WRATH("item.equipment.curse_of_wrath", "Gain %s%% attack damage per difficulty level difference against mobs with higher level than you.", 1),
    ITEM_CHARM_ADD_LEVEL("item.equipment.curse_add_level", "Gain %s extra difficulty level", 1),
    ITEM_CHARM_NO_DROP("item.equipment.curse_no_drop", "Mobs you kill will not drop hostility loot", 0),
    ITEM_CHARM_TRAIT_CHEAP("item.equipment.curse_trait_cheap", "Mob traits will be +%s%% more frequent", 1),
    ITEM_RING_OCEAN("item.equipment.ring_of_ocean", "You will always be wet", 0),
    ITEM_RING_LIFE("item.equipment.ring_of_life", "You will not lose more than %s%% of your max health at once", 1),
    ITEM_RING_DIVINITY("item.equipment.ring_of_divinity", "Immune to magic damage. Gets permanent Cleanse effect", 0),
    ITEM_RING_REFLECTION("item.equipment.ring_of_reflection", "When a mob trait tries to apply a negative effect on you, apply it to surrounding enemies that target you instead.", 0),
    ITEM_FLAME_THORN("item.equipment.flame_thorn", "When you damage a mob, inflict Soul Flame with level equals to total number of effects that mob has, for %ss", 1),
    ITEM_IMAGINE_BREAKER("item.equipment.imagine_breaker", "All your melee damage bypass magical protection.", 0),
    ITEM_PLATINUM_STAR("item.equipment.platinum_star", "All your melee damage bypass damage cool down.", 0),
    ITEM_WITCH_WAND("item.equipment.witch_wand", "Throw random splash potions on right click. Types selected from random potion traits.", 0),
    ITEM_RING_CORROSION("item.equipment.ring_of_corrosion", "When you deal damage, damage all their equipments by %s%% of max durability.", 1),
    ITEM_RING_CORROSION_NEG("item.equipment.ring_of_corrosion_neg", "When you take damage, damage all your equipments by %s%% of max durability.", 1),
    ITEM_RING_INCARCERATION("item.equipment.ring_of_incarceration", "When sneaking, apply Incarceration effect on you and all mobs within your attack range.", 0),
    ITEM_RING_HEALING("item.equipment.ring_of_healing", "Heals %s%% of max health every second.", 1),
    ABRAHADABRA("item.equipment.abrahadabra", "When a mob trait tries to apply a trait effect on you, apply it to surrounding enemies that target you instead.", 0),
    NIDHOGGUR("item.equipment.nidhoggur", "Mobs you kill will drop +%s%% loot per mob level", 1),
    POCKET_OF_RESTORATION("item.equipment.pocket_of_restoration", "Automatically put sealed item inside, unseal it, and put it back when finished.", 0),
    LOOTING_CHARM("item.equipment.looting", "Enables some hostility trait drops. Check JEI for detail.", 0),
    ABYSSAL_THORN("item.equipment.abyssal_thorn", "Mobs get all possible traits at their level, but with Curse of Envy, they will always drop trait symbols of those when killed", 0),
    DIVINITY_CROSS("item.equipment.divinity_cross", "Cleanse effect will not clear Lv.1 beneficial effects", 0),
    DIVINITY_LIGHT("item.equipment.divinity_light", "Your adaptive level will stay at 0.", 0),
    MSG_AI("msg.ai", "Configure %s: Set NoAI to %s.", 2),
    MSG_SET_TARGET("msg.set_target", "Set %s and %s to fight", 2),
    MSG_TARGET_FAIL("msg.target_fail", "%s and %s cannot fight", 2),
    MSG_TARGET_RECORD("msg.target_record", "Recorded %s", 1),
    MSG_SET_TRAIT("msg.set_trait", "Set trait %1$s on entity %2$s to level %3$s", 3),
    MSG_SELECT_TRAIT("msg.select_trait", "Selected trait: %s", 1),
    MSG_ERR_MAX("msg.err_max", "Trait level already reached max value", 0),
    MSG_ERR_DISALLOW("msg.err_disallow", "Trait not applicable on this entity", 0),
    INFO_PLAYER_LEVEL("info.player_level", "Player difficulty level: %s", 1),
    INFO_PLAYER_EXP("info.player_exp", "Difficulty progress: %s%%", 1),
    INFO_PLAYER_CAP("info.player_cap", "Mob trait rank limit: %s", 1),
    INFO_CHUNK_LEVEL("info.chunk_level", "Chunk base difficulty: %s", 1),
    INFO_CHUNK_SCALE("info.chunk_scale", "Chunk difficulty scale: %s", 1),
    INFO_CHUNK_CLEAR("info.clear", "Chunk Section difficulty cleared", 0),
    INFO_TAB_TITLE("info.title", "Difficulty Information", 0),
    INFO_REWARD("info.reward", "Obtained %s rewards", 1),
    INFO_PLAYER_ADAPTIVE_LEVEL("info.player_detail.adaptive", "Adaptive level: %s", 1),
    INFO_PLAYER_DIM_LEVEL("info.player_detail.dim", "Visited dimension bonus: %s", 1),
    INFO_PLAYER_ITEM_LEVEL("info.player_detail.item", "Item bonus: %s", 1),
    INFO_PLAYER_EXT_LEVEL("info.player_detail.external", "External difficulty: %s", 1),
    INFO_SECTION_DIM_LEVEL("info.section_detail.dim", "Dimension level: %s", 1),
    INFO_SECTION_BIOME_LEVEL("info.section_detail.biome", "Biome level: %s", 1),
    INFO_SECTION_ADAPTIVE_LEVEL("info.section_detail.adaptive", "Adaptive level: %s", 1),
    INFO_SECTION_DISTANCE_LEVEL("info.section_detail.distance", "Distance bonus: %s", 1),
    BOSS_EVENT("boss_event", "Hostility Clearing Progress: %s/%s", 2),
    LOOT_TITLE("jei.loot_title", "Trait Loot", 0),
    LOOT_CHANCE("jei.loot_chance", "%s chance for %s rank %s", 3),
    LOOT_MIN_LEVEL("jei.min_level", "Drops on mobs with level %s or higher", 1),
    LOOT_MIN_HEALTH("jei.min_health", "Drops on mobs with %s or higher max health", 1),
    LOOT_NO_TRAIT("jei.no_trait", "%s chance to drop when conditions met", 1),
    LOOT_OTHER_TRAIT("jei.other_trait", "Requires %s at rank %s", 2),
    TOOLTIP_JEI_ENVY("jei.envy", "Drops when you equips Curse of Envy while killing mobs of this trait", 0),
    TOOLTIP_JEI_GLUTTONY("jei.gluttony", "Drops when you equips Curse of Gluttony while killing mobs with levels", 0),
    TOOLTIP_JEI_REQUIRED("jei.required", "Requires you to equip %s while killing mobs", 1),
    COMMAND_PLAYER_SUCCEED("command.player.success", "Performed actions on %s players", 1),
    COMMAND_PLAYER_FAIL("command.player.fail", "Command has no target or no effect", 0),
    COMMAND_PLAYER_GET_BASE("command.player.get_base", "%s has base difficulty level %s", 2),
    COMMAND_PLAYER_GET_TOTAL("command.player.get_total", "%s has overall difficulty level %s", 2),
    COMMAND_PLAYER_GET_DIM("command.player.get_dim", "%s has visited %s dimensions", 2),
    COMMAND_PLAYER_GET_TRAIT_CAP("command.player.trait_cap", "The max rank %s has killed is rank %s", 2),
    COMMAND_REGION_SUCCEED("command.region.success", "Action Succeed", 0),
    COMMAND_REGION_LOCAL_OFF("command.region.local_off", "Section difficulty is turned off", 0),
    COMMAND_REGION_COUNT("command.region.count", "Performed Actions for %s chunk sections", 1),
    COMMAND_REGION_GET_BASE("command.region.get_base", "Target section has base difficulty level %s", 1),
    COMMAND_REGION_GET_TOTAL("command.region.get_total", "Target section has total difficulty level %s", 1),
    COMMAND_REGION_GET_SCALE("command.region.get_scale", "Target section has difficulty scale %s", 1),
    COMMAND_REGION_CLEAR("command.region.clear", "Section Cleared", 0),
    COMMAND_REGION_NOT_CLEAR("command.region.not_clear", "Section Not Cleared", 0),
    COMMAND_MOB_SUCCEED("command.mob.success", "Performed actions on %s mobs", 1),
    COMMAND_INVALID_TRAIT("command.invalid_trait", "Invalid trait id %s", 1),
    PATCHOULI_TITLE("patchouli.title", "L2Hostility Guide", 0),
    PATCHOULI_LANDING("patchouli.landing", "Welcome to Champion-like difficulty scaling mod", 0);

    final String id;

    final String def;

    final int count;

    private LangData(String id, String def, int count) {
        this.id = id;
        this.def = def;
        this.count = count;
    }

    public MutableComponent get(Object... objs) {
        if (objs.length != this.count) {
            throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
        } else {
            return translate("l2hostility." + this.id, objs);
        }
    }

    public static void addTranslations(RegistrateLangProvider pvd) {
        for (LangData id : values()) {
            String[] strs = id.id.split("\\.");
            String str = strs[strs.length - 1];
            pvd.add("l2hostility." + id.id, id.def);
        }
        pvd.add("config.jade.plugin_l2hostility.mob", "L2Hostility");
        pvd.add("death.attack.killer_aura", "%s was killed by killer aura");
        pvd.add("death.attack.killer_aura.player", "%s was killed by %s's killer aura");
        pvd.add("curios.identifier.hostility_curse", "L2Hostility - Curse");
        pvd.add("curios.modifiers.hostility_curse", "When worn as Curse:");
        if (ModList.get().isLoaded("gateways")) {
            GatewayConfigGen.genLang(pvd);
        }
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent translate(String key, Object... objs) {
        return Component.translatable(key, objs);
    }

    public static MutableComponent item(ItemStack stack) {
        return stack.getHoverName().copy().withStyle(stack.getRarity().getStyleModifier());
    }

    public static MutableComponent sectionRender() {
        return ITEM_SECTION_RENDER.get(item(LHItems.DETECTOR.asStack()), item(LHItems.DETECTOR_GLASSES.asStack()));
    }

    public static MutableComponent orbUse() {
        return ITEM_ORB_USE.get(item(LHItems.DETECTOR.asStack()), item(LHItems.DETECTOR_GLASSES.asStack()));
    }
}