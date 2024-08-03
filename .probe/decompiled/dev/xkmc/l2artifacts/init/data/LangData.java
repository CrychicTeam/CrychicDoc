package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.compat.PatchouliLang;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum LangData {

    RAW_ARTIFACT("tooltip.raw_artifact", "Right Click to Reveal Stats", 0, null),
    ARTIFACT_LEVEL("tooltip.artifact_level", "Level: %s", 1, null),
    ARTIFACT_EXP("tooltip.artifact_exp", "Exp: %s/%s", 2, ChatFormatting.DARK_GRAY),
    UPGRADE("tooltip.upgrade", "Right Click to Reveal Upgrade Result", 0, ChatFormatting.GOLD),
    MAIN_STAT("tooltip.main_stat", "Main Stats", 0, ChatFormatting.GRAY),
    SUB_STAT("tooltip.sub_stat", "Sub Stats", 0, ChatFormatting.GRAY),
    EXP_CONVERSION("tooltip.exp_conversion", "Exp as fodder: %s", 1, ChatFormatting.GRAY),
    SHIFT_TEXT("tooltip.shift", "Hold Shift for set effects", 0, ChatFormatting.AQUA),
    SET("tooltip.set", "Set: %s", 1, null),
    STAT_CAPTURE_INFO("tooltip.stat_container.capture", "Can capture the main stat of an artifact", 0, null),
    STAT_USE_INFO("tooltip.stat_container.apply", "Next upgrade of sub stat will be this stat", 0, null),
    STAT_INFO("tooltip.stat_container.info", "Stat: %s", 1, null),
    BOOST_MAIN("tooltip.boost.main", "Next upgrade of main stat will be maximized", 0, null),
    BOOST_SUB("tooltip.boost.sub", "Next upgrade of sub stat will be very high", 0, null),
    UPGRADE_STAT("tooltip.enhance.stat", "Next upgraded sub stat will be %s.", 1, null),
    UPGRADE_MAIN("tooltip.enhance.main", "Next %s upgraded main stat will be maximized.", 1, null),
    UPGRADE_SUB("tooltip.enhance.sub", "Next %s upgraded sub stat will be maximized.", 1, null),
    ALL_SET_EFFECTS("set.all_set_effects", "%s: %s piece(s)", 2, null),
    TITLE_SELECT_SET("title.select_set", "Select Artifact Set", 0, null),
    TITLE_SELECT_SLOT("title.select_slot", "Select Artifact Slot", 0, null),
    TITLE_SELECT_RANK("title.select_rank", "Select Artifact Rank", 0, null),
    TAB_FILTERED("title.tab.filtered", "Artifacts", 0, null),
    TAB_FILTER("title.tab.filter", "Filter", 0, null),
    TAB_SORT("title.tab.sort", "Sort", 0, null),
    TAB_RECYCLE("title.tab.recycle", "Exp Conversion", 0, null),
    TAB_UPGRADE("title.tab.upgrade", "Upgrade", 0, null),
    TAB_DISSOLVE("title.tab.dissolve", "Dissolve", 0, null),
    TAB_AUGMENT("title.tab.augment", "Augment", 0, null),
    TAB_SHAPE("title.tab.shape", "Genesis", 0, null),
    FILTER_RANK("title.filter.rank", "Rank Filter", 0, null),
    FILTER_SLOT("title.filter.slot", "Slot Filter", 0, null),
    FILTER_SET("title.filter.set", "Set Filter", 0, null),
    FILTER_STAT("title.filter.stat", "Stat Filter", 0, null),
    TAB_INFO_TOTAL("title.info.total", "Total Count: %s", 1, null),
    TAB_INFO_MATCHED("title.info.matched", "Matched: %s", 1, null),
    TAB_INFO_EXP("title.info.experience", "Stored Exp: %s", 1, null),
    TAB_INFO_SELECTED("title.info.selected", "Selected: %s", 1, null),
    TAB_INFO_EXP_GAIN("title.info.exp_gain", "Exp to Gain: %s", 1, null),
    TAB_INFO_EXP_COST("title.info.exp_cost", "Exp Cost: %s", 1, null),
    RANK_1("tooltip.rank.1", "Rank Lv.1", 0, null),
    RANK_2("tooltip.rank.2", "Rank Lv.2", 0, null),
    RANK_3("tooltip.rank.3", "Rank Lv.3", 0, null),
    RANK_4("tooltip.rank.4", "Rank Lv.4", 0, null),
    RANK_5("tooltip.rank.5", "Rank Lv.5", 0, null),
    PLAYER_ONLY("tooltip.player_only", "Set Effects works on player only.", 0, null),
    LOOT_POOL("tooltip.loot_pool", "Possible Sets:", 0, ChatFormatting.YELLOW),
    LOOT_POOL_ALL("tooltip.loot_pool_all", "Possible Sets: All Sets", 0, ChatFormatting.YELLOW),
    TOOL_SWAP("tool.swap", "Stores 9 sets of artifacts. When holding it in hand, click up and down arrow to select, and click R to swap. Click empty slots to disable, to prevent taking down artifacts into empty slot when you don't want to.", 0, ChatFormatting.GRAY);

    private final String key;

    private final String def;

    private final int arg;

    private final ChatFormatting format;

    private LangData(String key, String def, int arg, @Nullable ChatFormatting format) {
        this.key = "l2artifacts." + key;
        this.def = def;
        this.arg = arg;
        this.format = format;
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent getTranslate(String s, Object... args) {
        return Component.translatable("l2artifacts." + s, args);
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
        for (LangData lang : values()) {
            pvd.add(lang.key, lang.def);
        }
        for (ArtifactSlotCuriosType type : ArtifactSlotCuriosType.values()) {
            pvd.add(type.getDesc(), type.getDefTranslation());
        }
        pvd.add("l2artifacts.set.1", "When Equip: ");
        pvd.add("l2artifacts.set.2", "(2/%s) Set Bonus: ");
        pvd.add("l2artifacts.set.3", "(3/%s) Set Bonus: ");
        pvd.add("l2artifacts.set.4", "(4/%s) Set Bonus: ");
        pvd.add("l2artifacts.set.5", "(5/%s) Set Bonus: ");
        pvd.add("menu.tabs.set_effects", "Activated Set Effects");
        PatchouliLang.genLang(pvd);
    }
}