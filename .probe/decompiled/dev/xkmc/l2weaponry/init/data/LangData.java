package dev.xkmc.l2weaponry.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public enum LangData {

    TOOL_DAGGER("tool.dagger", "Deal high damage to enemies not targeting attacker", 0),
    TOOL_CLAW("tool.claw", "Deal damage to all surrounding mobs. Increase damage for consecutive attack. Block damage shortly after attack.", 0),
    TOOL_CLAW_EXTRA("tool.claw.extra", "Double weld for larger sweep range, higher damage stack limit, and full angle blocking.", 0),
    TOOL_HAMMER("tool.hammer", "Double hand weapon. Break through armors", 0),
    TOOL_BATTLE_AXE("tool.battle_axe", "Double Hand weapon. Sweeping attack", 0),
    TOOL_SPEAR("tool.spear", "Sweeping attack, long attack range.", 0),
    TOOL_MACHETE("tool.machete", "Deal damage to all surrounding mobs. Increase damage for consecutive attack.", 0),
    TOOL_MACHETE_EXTRA("tool.machete.extra", "Double weld for larger sweep range and higher damage stack limit.", 0),
    TOOL_ROUND_SHIELD("tool.round_shield", "Blocking would not slow down player, and allow sprinting.", 0),
    TOOL_PLATE_SHIELD("tool.plate_shield", "Main hand only. Resistant against attacks that would break regular shields. Reflect damage with right timing.", 0),
    TOOL_PLATE_SHIELD_EXTRA("tool.plate_shield.extra", " Knockback enemies on hit, and deal 3x damage on critical hit. Damage bypass armor.", 0),
    TOOL_THROWING_AXE("tool.throwing_axe", "You can throw this axe toward target.", 0),
    TOOL_JAVELIN("tool.javelin", "Pierce through multiple enemies when thrown", 0),
    TOOL_NUNCHAKU("tool.nunchaku", "Hold Right Click to perform continuous attack", 0),
    FLAME_AXE("legendary.axe_of_cursed_flame", "Inflict soul flame on targets. Protect holder from fire damage.", 0),
    BLACK_HAMMER("legendary.hammer_of_incarceration", "Immobilize enemies within %s blocks when performed a falling attack.", 1),
    FROZEN_SPEAR("legendary.spear_of_winter_storm", "Frost targets. Protect holder from powdered snow.", 0),
    STORM_JAVELIN("legendary.poseidon_madness", "Thunder strike all enemies hit, thrown or sweep, regardless of position and weather. Protect holder from fire and thunder damage.", 0),
    ENDER_DAGGER("legendary.shadow_hunter", "Right click target within %s blocks to teleport to its back, and wipe its anger. Damage penetrates armor if target does not target you.", 1),
    ENDER_JAVELIN("legendary.void_escape", "Not affected by gravity. When hit a block, teleport to that position. Holder gets levitation and slow falling in void.", 0),
    ENDER_SPEAR("legendary.haunting_demon_of_the_end", "Aim at a target within %s blocks and right click to teleport near it.", 1),
    ABYSS_DAGGER("legendary.abyss_shock", "Damage dealt to enemies not targeting user will bypass magical protections.", 0),
    ABYSS_MACHETE("legendary.abyss_resonance", "For over %s consecutive attacks, damage will bypass magical protections.", 1),
    ABYSS_HAMMER("legendary.abyss_echo", "Critical hit will bypass magical protections.", 0),
    ABYSS_AXE("legendary.abyss_terror", "Damage dealt to enemies targeting user will bypass magical protections.", 0),
    BLOOD_CLAW("legendary.vampire_desire", "Restore health based on damage dealt. Increase damage stack limit based on enemies killed.", 0),
    BLACK_AXE("legendary.barbaric_hallow", "Damage penetrates armor, damage increase target's armor value.", 0),
    ENDER_MACHETE("legendary.shadow_shredder", "Inflict levitation and slow falling for a short time to enemies.", 0),
    CHEATER_CLAW("legendary.claw_of_determination", "When damage dealt is reduced, next consecutive attack to the same target will increase double of the amount reduced.", 0),
    CHEATER_MACHETE("legendary.blade_of_illusion", "When target has less lost health than consecutive damage dealt, next consecutive attack to the same target will increase that difference.", 0),
    HOLY_AXE("legendary.dogmatic_standoff", "Gain damage absorption equal to %s%% of target health. Would not exceed %s%% of target health.", 2),
    HOLY_HAMMER("legendary.dogmatic_punishment", "On critical hit, increase damage by user's damage absorption.", 0),
    MATS_FIERY("mats.tf.fiery", "Deal %s%% more damage to mobs not immune to fire, and ignite it for %s seconds.", 2),
    MATS_KNIGHTMETAL("mats.tf.knightmetal", "Increase damage by %s%% of enemy armor.", 1),
    MATS_REFLECT("mats.tf.shield_reflect", "Shield reflect %s%% extra damage on blocking.", 1),
    MATS_STEELEAF("mats.tf.steeleaf", "Deal %s%% more damage to mobs without armor, with %s%% chance to cause bleeding.", 2),
    MATS_IRONWOOD("mats.tf.ironwood", "Regenerate durability in Twilight Forest.", 0),
    MATS_EFFECT("mats.tf.shield_effect", "On blocking damage, grant %s to user", 1),
    MATS_AH_ARSON("mats.ah.arsonist", "Ignite target for %s seconds.", 1),
    MATS_AH_ARSON_SHIELD("mats.ah.arsonist_shield", "On blocking, deal %s extra damage to mobs not immune to fire.", 1),
    MATS_AH_VOLUCITE("mats.ah.volucite", "Holder gains slow falling when about to take fall damage.", 0),
    MATS_AH_VOLUCITE_SHIELD("mats.ah.volucite_shield", "On blocking, deal %s extra damage and apply levitation to target.", 1),
    STAT_KILL("stat.kill_count", "Enemies killed: %s", 1),
    STAT_BONUS_CLAW("stat.claw_bonus", "Damage stack limit: %s", 1);

    private final String id;

    private final String def;

    private final int count;

    private LangData(String id, String def, int count) {
        this.id = id;
        this.def = def;
        this.count = count;
    }

    public MutableComponent get(Object... objs) {
        if (objs.length != this.count) {
            throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
        } else {
            MutableComponent ans = translate("l2weaponry." + this.id, objs);
            if (this.id.startsWith("tool.")) {
                ans = ans.withStyle(ChatFormatting.DARK_GREEN);
            }
            if (this.id.startsWith("mats.")) {
                ans = ans.withStyle(ChatFormatting.GRAY);
            }
            if (this.id.startsWith("legendary.")) {
                ans = ans.withStyle(ChatFormatting.GOLD);
            }
            if (this.id.startsWith("stat.")) {
                ans = ans.withStyle(ChatFormatting.AQUA);
            }
            return ans;
        }
    }

    public static Component getTooltip(MobEffectInstance eff) {
        MutableComponent ans = Component.translatable(eff.getDescriptionId());
        MobEffect mobeffect = eff.getEffect();
        if (eff.getAmplifier() > 0) {
            ans = Component.translatable("potion.withAmplifier", ans, Component.translatable("potion.potency." + eff.getAmplifier()));
        }
        if (eff.getDuration() > 20) {
            ans = Component.translatable("potion.withDuration", ans, MobEffectUtil.formatDuration(eff, 1.0F));
        }
        return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
    }

    public static void addTranslations(RegistrateLangProvider pvd) {
        for (LangData id : values()) {
            String[] strs = id.id.split("\\.");
            String str = strs[strs.length - 1];
            pvd.add("l2weaponry." + id.id, id.def);
        }
        pvd.add("attribute.name.shield_defense", "Shield Defense");
        pvd.add("attribute.name.reflect_time", "Reflect Time");
        pvd.add("subtitles.item.trident.hit", "Thrown weapon stabs");
        pvd.add("subtitles.item.trident.hit_ground", "Thrown weapon vibrates");
        pvd.add("subtitles.item.trident.return", "Thrown weapon returns");
        pvd.add("subtitles.item.trident.riptide", "Thrown weapon zooms");
        pvd.add("subtitles.item.trident.throw", "Thrown weapon clangs");
        pvd.add("subtitles.item.trident.thunder", "Thrown weapon thunder cracks");
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent translate(String key, Object... objs) {
        return Component.translatable(key, objs);
    }
}