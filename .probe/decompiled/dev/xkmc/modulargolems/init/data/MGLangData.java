package dev.xkmc.modulargolems.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.compat.misc.PatchouliLang;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemPartType;
import dev.xkmc.modulargolems.content.entity.humanoid.HumaniodGolemPartType;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum MGLangData {

    HEALTH("tooltip.health", "Health: %s/%s", 2, null),
    SLOT("tooltip.slot", "Remaining Upgrade Slot: %s", 1, null),
    SHIFT("tooltip.shift", "Press SHIFT to show modifier details", 0, ChatFormatting.GRAY),
    POTION_ATTACK("tooltip.potion_attack", "Inflict %s to enemies attacking or attacked.", 1, ChatFormatting.GREEN),
    POTION_DEFENSE("tooltip.potion_defense", "Golem gain %s.", 1, ChatFormatting.GREEN),
    GOLEM_EQUIPMENT("tooltip.golem_equipment", "Only effective on %s", 1, ChatFormatting.LIGHT_PURPLE),
    UPGRADE_COUNT("tooltip.modifier", "Total %s modifiers from %s upgrades", 2, ChatFormatting.LIGHT_PURPLE),
    TARGET_UUID_ADD("tooltip.target.uuid_add", "Right click entity to add entity UUID to the list", 0, ChatFormatting.GRAY),
    TARGET_UUID_REMOVE("tooltip.target.uuid_remove", "Sneak right click entity to remove entity UUID from the list", 0, ChatFormatting.GRAY),
    TARGET_TYPE_ADD("tooltip.target.type_add", "Right click entity to add entity type to the list", 0, ChatFormatting.GRAY),
    TARGET_TYPE_REMOVE("tooltip.target.type_remove", "Sneak right click entity to remove entity type from the list", 0, ChatFormatting.GRAY),
    TARGET_NAME("tooltip.target.name", "Rename the item and right click to add the name as entity type or entity type tag to the list", 0, ChatFormatting.GRAY),
    TARGET_DEFAULT("tooltip.target.default", "Default Iron Golem hostile targets: all enemies except creeper.", 0, ChatFormatting.GRAY),
    TARGET_REMOVE("tooltip.target.remove", "Sneak right click to remove last entry from the list", 0, ChatFormatting.GRAY),
    TARGET_SHIFT("tooltip.target.shift", "Press SHIFT to show usage", 0, ChatFormatting.GRAY),
    TARGET_MSG_ADDED("msg.add_target", "Add %s to filter", 1, null),
    TARGET_MSG_REMOVED("msg.remove_target", "Remove %s from filter", 1, null),
    MODE_FOLLOW("tooltip.follow", "Follow", 0, ChatFormatting.AQUA),
    MODE_GUARD("tooltip.guard", "Wander", 0, ChatFormatting.AQUA),
    MODE_STAND("tooltip.stand", "Stand", 0, ChatFormatting.AQUA),
    MODE_WANDER("tooltip.wander", "Wander", 0, ChatFormatting.AQUA),
    MODE_SQUAD("tooltip.squad", "Squad", 0, ChatFormatting.AQUA),
    MODE_PATROL("tooltip.patrol", "Patrol", 0, ChatFormatting.AQUA),
    MODE_FOLLOWING("tooltip.following", "Golem will follow you", 0, ChatFormatting.AQUA),
    MODE_GUARDING("tooltip.guarding", "Golem will wander around (%s, %s, %s)", 3, ChatFormatting.AQUA),
    MODE_STANDING("tooltip.standing", "Golem will stay at (%s, %s, %s)", 3, ChatFormatting.AQUA),
    MODE_WANDERING("tooltip.wandering", "Golem will move freely", 0, ChatFormatting.AQUA),
    MODE_TEAMING("tooltip.team_up", "Golem will follow their captain", 0, ChatFormatting.AQUA),
    MODE_PATROLLING("tooltip.patrolling", "Golem will patrol in a designated way", 0, ChatFormatting.AQUA),
    WAND_SWITCH("wand.switch", "Scroll or use up/down arrow key to switch between modes", 0, ChatFormatting.GRAY),
    WAND_MODE("wand.mode", "Current Mode: %s", 1, ChatFormatting.GOLD),
    WAND_RIGHT("wand.right_click", "On right click:", 0, ChatFormatting.YELLOW),
    WAND_SHIFT("wand.shift_click", "On sneak right click:", 0, ChatFormatting.YELLOW),
    WAND_RETRIEVE_RIGHT("wand.retrieve_right", "Retrieve all your surrounding golems.", 0, ChatFormatting.GRAY),
    WAND_RETRIEVE_SHIFT("wand.retrieve_shift", "Target a faraway golem to retrieve it.", 0, ChatFormatting.GRAY),
    WAND_COMMAND_RIGHT("wand.command_right", "Target a golem to switch modes. Target entity to command nearby golems to attack it.", 0, ChatFormatting.GRAY),
    WAND_COMMAND_SHIFT("wand.command_shift", "Click golem to configure inventory and view stats.", 0, ChatFormatting.GRAY),
    WAND_SUMMON_RIGHT("wand.summon_right", "Summon one golem to a faraway position pointed by the wand.", 0, ChatFormatting.GRAY),
    WAND_SUMMON_SHIFT("wand.summon_shift", "Summon all golems from your inventory.", 0, ChatFormatting.GRAY),
    WAND_RIDER("wand.rider", "Target your dog golem to ride on it. If it's not your golem, you can ride when its owner is riding. When mounted, right click anything to dismount.", 0, ChatFormatting.GRAY),
    WAND_SQUAD("wand.squad", "Click a golem with config to set it as captain. Click a captain to remove its captain status.", 0, ChatFormatting.GRAY),
    CONFIG_CARD("wand.config", "Right click golem to set config. Right click again to remove. When in offhand, wands would only target golems having this config.", 0, ChatFormatting.GRAY),
    CONFIG_INIT("wand.config_init", "Right click to initialize config", 0, ChatFormatting.GRAY),
    CONFIG_OTHER("wand.config_other", "Not your card. You cannot edit it", 0, ChatFormatting.RED),
    PATH("wand.path_record", "Right click block to add to path, right click again to remove. Path forms a cycle.", 0, ChatFormatting.GRAY),
    PATH_ADD("wand.add_node", "Added node to path", 0, null),
    PATH_REMOVE("wand.remove_node", "Removed node from path", 0, null),
    PATH_COUNT("wand.node_count", "Path has %s nodes", 1, ChatFormatting.GRAY),
    DESTROY_ITEM("msg.destroy_item", "Golem %s destroyed %s items because it finds no place to store.", 2, ChatFormatting.RED),
    DESTROY_EXP("msg.destroy_exp", "Golem %s destroyed %s experience because it finds no place to store.", 2, ChatFormatting.RED),
    CALL_ATTACK("msg.call_attack", "%s Golems will attack %s", 2, null),
    LOADING("config.loading", "Loading...", 0, ChatFormatting.GRAY),
    NO_CONFIG("config.no", "No config card assigned", 0, ChatFormatting.GRAY),
    SUMMON_FAILED("msg.summon_failed", "Failed to summon %s, as target position is too far", 1, ChatFormatting.RED),
    SUMMON_FAR("msg.summon_far", "Summoned %s, at (%s,%s,%s)", 4, ChatFormatting.GOLD),
    SLOT_MAIN("slot.main", "Main Hand Slot", 0, ChatFormatting.YELLOW),
    SLOT_OFF("slot.off", "Off Hand Slot", 0, ChatFormatting.YELLOW),
    SLOT_BACKUP("slot.backup", "Secondary Weapon Slot", 0, ChatFormatting.YELLOW),
    SLOT_ARROW("slot.arrow", "Arrow Slot", 0, ChatFormatting.YELLOW),
    SLOT_MAIN_DESC("slot.main_desc", "Place weapons here. Accepts anything.", 0, ChatFormatting.GRAY),
    SLOT_BACKUP_DESC("slot.backup_desc", "Place secondary weapons here.", 0, ChatFormatting.GRAY),
    SLOT_BACKUP_INFO("slot.backup_info", "If you have bow and sword, one in main hand and one in backup slot, your golem can switch between them depending on distance to enemies.", 0, ChatFormatting.GRAY),
    SLOT_ARROW_DESC("slot.arrow_desc", "Place arrows here to free up off hand for shields", 0, ChatFormatting.GRAY),
    CONFIG_SET("config.set", "Insert to set config", 0, null),
    CONFIG_MODE("config.mode", "Default Mode", 0, null),
    CONFIG_POS("config.pos", "Summon to", 0, null),
    CONFIG_LOCK("config.lock", "Lock Interaction", 0, null),
    CONFIG_TO_POSITION("config.pos_original", "Original Position", 0, ChatFormatting.AQUA),
    CONFIG_TO_TARGET("config.pos_target", "Pointed Position", 0, ChatFormatting.AQUA),
    CONFIG_MODE_TOOLTIP("config.mode_tooltip", "The default mode golem will take when summoned. Does not affect golem in world already. Retrieved golems will take this mode instead of old mode when summoned.", 0, ChatFormatting.GRAY),
    CONFIG_TO_POSITION_TOOLTIP("config.pos_original_tooltip", "If golem is retrieved, summon this golem to the original position. It would not take effect if the golem is fresh. If the original position is too far away, summon would fail.", 0, ChatFormatting.GRAY),
    CONFIG_TO_TARGET_TOOLTIP("config.pos_target_tooltip", "Summon golem to the position player points to.", 0, ChatFormatting.GRAY),
    UI_WHITELIST("config.whitelist", "Using whitelist. Click to switch to blacklist.", 0, null),
    UI_BLACKLIST("config.blacklist", "Using blacklist. Click to switch to whitelist.", 0, null),
    UI_MATCH_ITEM("config.match_item", "Match item only. Click to match NBT.", 0, null),
    UI_MATCH_TAG("config.match_tag", "Match item and NBT. Click to match item only.", 0, null),
    UI_TARGET_HOSTILE("config.hostile", "Hostile List", 0, null),
    UI_TARGET_FRIENDLY("config.friendly", "Friendly List", 0, null),
    UI_TARGET_RESET("config.reset", "Reset", 0, null),
    TAB_TOGGLE("tab.toggle", "General Config", 0, null),
    TAB_PICKUP("tab.pickup", "Pickup Filter Config", 0, null),
    TAB_TARGET("tab.target", "Target Filter Config", 0, null),
    TAB_PATH("tab.path", "Patrol Path Config", 0, null),
    TAB_EQUIPMENT("tab.equipment", "Golem Equipments", 0, null),
    TAB_ATTRIBUTE("tab.attribute", "Golem Attributes", 0, null),
    BOT_MANA("msg.botania_mana", "Mana: %s/%s", 2, null),
    BOT_NO_RING("msg.botania_no_ring", "No Band of Mana Equipped", 0, null);

    private final String key;

    private final String def;

    private final int arg;

    private final ChatFormatting format;

    private MGLangData(String key, String def, int arg, @Nullable ChatFormatting format) {
        this.key = "modulargolems." + key;
        this.def = def;
        this.arg = arg;
        this.format = format;
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent getTranslate(String s) {
        return Component.translatable("modulargolems." + s);
    }

    public MutableComponent get(Object... args) {
        if (args.length != this.arg) {
            throw new IllegalArgumentException("for " + this.name() + ": expect " + this.arg + " parameters, got " + args.length);
        } else {
            MutableComponent ans = Component.translatable(this.key, args);
            return this.format != null ? ans.withStyle(this.format) : ans;
        }
    }

    public static void genLang(RegistrateLangProvider pvd) {
        for (MGLangData lang : values()) {
            pvd.add(lang.key, lang.def);
        }
        pvd.add("attribute.name.golem_regen", "Golem Regen");
        pvd.add("attribute.name.golem_sweep", "Sweep Range");
        pvd.add("attribute.name.golem_size", "Golem Size");
        pvd.add("attribute.name.golem_jump", "Golem Jump Strength");
        pvd.add("golem_material.modulargolems.copper", "Copper");
        pvd.add("golem_material.modulargolems.iron", "Iron");
        pvd.add("golem_material.modulargolems.gold", "Gold");
        pvd.add("golem_material.modulargolems.netherite", "Netherite");
        pvd.add("golem_material.modulargolems.sculk", "Sculk");
        pvd.add("curios.identifier.golem_skin", "Humanoid Golem Skin");
        pvd.add("curios.identifier.golem_route", "Golem Patrol Path");
        CompatManager.dispatchGenLang(pvd);
        for (MetalGolemPartType type : MetalGolemPartType.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            pvd.add("golem_part.metal_golem." + name, RegistrateLangProvider.toEnglishName(name) + ": %s");
        }
        for (HumaniodGolemPartType type : HumaniodGolemPartType.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            pvd.add("golem_part.humanoid_golem." + name, RegistrateLangProvider.toEnglishName(name) + ": %s");
        }
        for (DogGolemPartType type : DogGolemPartType.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            pvd.add("golem_part.dog_golem." + name, RegistrateLangProvider.toEnglishName(name) + ": %s");
        }
        PatchouliLang.genLang(pvd);
    }
}