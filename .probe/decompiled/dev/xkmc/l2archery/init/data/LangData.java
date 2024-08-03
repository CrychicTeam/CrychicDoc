package dev.xkmc.l2archery.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;

public enum LangData {

    STAT_DAMAGE("stat.damage", "Damage: %s", 1, ChatFormatting.BLUE),
    STAT_PUNCH("stat.punch", "Punch: %s", 1, ChatFormatting.BLUE),
    STAT_PULL_TIME("stat.pull_time", "Pull Time: %s seconds", 1, ChatFormatting.BLUE),
    STAT_SPEED("stat.speed", "Arrow Speed: %s m/s", 1, ChatFormatting.BLUE),
    STAT_FOV("stat.fov", "Magnification: %s", 1, ChatFormatting.BLUE),
    STAT_EFFECT("stat.effects", "Apply Effects on Hit:", 0, ChatFormatting.GREEN),
    STAT_EFFECT_TOO_MANY("stat.effects_too_many", "Apply %s Effects on Hit", 1, ChatFormatting.GREEN),
    FEATURE_INFINITY("feature.infinity", "This arrow support Infinity", 0, ChatFormatting.AQUA),
    FEATURE_INFINITY_ADV("feature.infinity_adv", "This arrow support only Advanced Infinity", 0, ChatFormatting.AQUA),
    FEATURE_INFINITY_ADV_BOW("feature.infinity_adv_bow", "Tipped arrows and arrows that support Advanced Infinity will not consume when shot", 0, null),
    FEATURE_BLEED("feature.bleed", "Stacking Bleed effect on enemy", 0, null),
    FEATURE_NO_FALL("feature.no_fall", "Arrow will not feel gravity, but will disappear after %s seconds.", 1, null),
    FEATURE_AIM_GLOW("feature.aim_flow", "Aimed targets within range of %s will appear glowing (only to you).", 1, null),
    FEATURE_WIND_BOW("feature.wind_bow", "Pulling bow will not slow down player.", 0, null),
    FEATURE_ENDER_SHOOT("feature.ender_shoot", "When shooting aimed target, teleport arrow directly to the front of target. Arrow will not be released otherwise.", 0, null),
    FEATURE_FIRE("feature.fire", "When hit target, set target on fire for %s seconds.", 1, null),
    FEATURE_ENDER_ARROW("feature.ender_arrow", "When hitting entity, exchange location of player and target. Otherwise, teleport player to hit block.", 0, null),
    FEATURE_EXPLOSION_ALL("feature.explosion.all", "Create an explosion of radius %s on hit.", 1, null),
    FEATURE_EXPLOSION_HURT("feature.explosion.hurt", "Create an explosion of radius %s on hit. Will not destroy block.", 1, null),
    FEATURE_EXPLOSION_NONE("feature.explosion.none", "Create an explosion of radius %s on hit. Will not destroy block or hurt mobs.", 1, null),
    FEATURE_EXPLOSION_BREAK("feature.explosion.break", "Arrow explosions will break blocks anyway.", 0, null),
    FEATURE_PIERCE_ARMOR("feature.pierce_armor", "Arrow damage will pierce armor", 0, null),
    FEATURE_PIERCE_MAGIC("feature.pierce_magic", "Arrow damage will bypass magic protection", 0, null),
    FEATURE_PIERCE_BOTH("feature.pierce_both", "Arrow damage will pierce armor and magic protection", 0, null),
    FEATURE_PIERCE_INVUL("feature.pierce_invul", "Arrow damage will cause void damage", 0, null),
    FEATURE_PULL_EFFECT("feature.pull_effect", "Apply effects when pulling bow:", 0, null),
    FEATURE_FLUX_UP("feature.flux_up", "Consume energy in place of durability loss", 0, null),
    DAMAGE_UPGRADE("tooltip.damage", "Increase the damage bonus of bows. Doesn't work on bows without damage bonus.", 0, ChatFormatting.GRAY),
    REMAIN_UPGRADE("tooltip.remain", "Remaining Upgrade Slot: %s", 1, ChatFormatting.GRAY),
    ENERGY_STORED("tooltip.energy.store", "Energy Stored: %s/%s FE", 2, ChatFormatting.WHITE),
    ENERGY_CONSUME("tooltip.energy.consume", "Energy Consumption: %s FE", 1, ChatFormatting.WHITE);

    private final String key;

    private final String def;

    private final int arg;

    private final ChatFormatting format;

    private LangData(String key, String def, int arg, @Nullable ChatFormatting format) {
        this.key = "l2archery." + key;
        this.def = def;
        this.arg = arg;
        this.format = format;
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent getTranslate(String s) {
        return Component.translatable("l2archery." + s);
    }

    public MutableComponent get(Object... args) {
        if (args.length != this.arg) {
            throw new IllegalArgumentException("for " + this.name() + ": expect " + this.arg + " parameters, got " + args.length);
        } else {
            MutableComponent ans = Component.translatable(this.key, args);
            return this.format != null ? ans.withStyle(this.format) : ans;
        }
    }

    public MutableComponent getWithColor(Object obj, ChatFormatting color) {
        return this.get(Component.literal(obj.toString()).withStyle(color));
    }

    public void getWithSign(List<Component> list, double val) {
        if (val != 0.0) {
            String sign = val > 0.0 ? "attribute.modifier.plus.0" : "attribute.modifier.take.0";
            list.add(this.get(Component.translatable(sign, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(val)), "")));
        }
    }

    public static void genLang(RegistrateLangProvider pvd) {
        for (LangData lang : values()) {
            pvd.add(lang.key, lang.def);
        }
        List<Item> list = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW);
        for (RegistryEntry<? extends Potion> ent : ArcheryEffects.POTION_LIST) {
            for (Item item : list) {
                String pref = item.getDescriptionId();
                String[] prefs = pref.split("\\.");
                String str = ((Potion) ent.get()).getName(item.getDescriptionId() + ".effect.");
                String[] ids = ((MobEffectInstance) ((Potion) ent.get()).getEffects().get(0)).getDescriptionId().split("\\.");
                String id = ids[ids.length - 1];
                String name = (String) ArcheryEffects.NAME_CACHE.getOrDefault(id, RegistrateLangProvider.toEnglishName(id));
                String pref_name = RegistrateLangProvider.toEnglishName(prefs[prefs.length - 1]);
                if (item == Items.TIPPED_ARROW) {
                    pref_name = "Arrow";
                }
                pvd.add(str, pref_name + " of " + name);
            }
        }
    }
}