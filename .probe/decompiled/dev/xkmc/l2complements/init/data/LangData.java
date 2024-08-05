package dev.xkmc.l2complements.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;

public class LangData {

    public static void addTranslations(RegistrateLangProvider pvd) {
        for (LangData.IDS id : LangData.IDS.values()) {
            pvd.add("l2complements." + id.id, id.def);
        }
        for (LCKeys lang : LCKeys.values()) {
            pvd.add(lang.id, lang.def);
        }
        pvd.add("death.attack.soul_flame", "%s has its soul burnt out");
        pvd.add("death.attack.soul_flame.player", "%s has its soul burnt out by %s");
        pvd.add("death.attack.life_sync", "%s was drained");
        pvd.add("death.attack.void_eye", "%s was cursed by void eye");
        pvd.add("death.attack.emerald", "%s was killed by emerald splash");
        pvd.add("death.attack.emerald.player", "%s was killed by emerald splash by %s");
        pvd.add("death.attack.bleed", "%s bleed to death");
        List<Item> list = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW);
        for (RegistryEntry<? extends Potion> ent : LCEffects.POTION_LIST) {
            for (Item item : list) {
                String pref = item.getDescriptionId();
                String[] prefs = pref.split("\\.");
                String str = ((Potion) ent.get()).getName(item.getDescriptionId() + ".effect.");
                String[] ids = ((MobEffectInstance) ((Potion) ent.get()).getEffects().get(0)).getDescriptionId().split("\\.");
                String id = ids[ids.length - 1];
                String name = (String) LCEffects.NAME_CACHE.getOrDefault(id, RegistrateLangProvider.toEnglishName(id));
                String pref_name = RegistrateLangProvider.toEnglishName(prefs[prefs.length - 1]);
                if (item == Items.TIPPED_ARROW) {
                    pref_name = "Arrow";
                }
                pvd.add(str, pref_name + " of " + name);
            }
        }
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static MutableComponent translate(String key, Object... objs) {
        return Component.translatable(key, objs);
    }

    public static MutableComponent diggerRotate() {
        return LangData.IDS.DIGGER_ROTATE.get(LCKeys.DIG.map.getKey().getDisplayName());
    }

    public static enum IDS {

        WIND_BOTTLE("tooltip.misc.wind_bottle", "Used to obtain Captured Wind or Captured Bullet.", 0),
        VOID_EYE("tooltip.misc.void_eye", "Obtained by killing an angry Enderman %s block below the void. This item kill holder in void, collect it with care.", 1),
        SUN_MEMBRANE("tooltip.misc.sun_membrane", "Obtained by killing a sun-burning Phantom %s blocks above max build height.", 1),
        SOUL_FLAME("tooltip.misc.soul_flame", "Obtained by killing a ghast with soul flame.", 0),
        CAPTURED_WIND("tooltip.misc.captured_wind", "Obtained by moving faster than %s blocks per second while having Wind Bottle in hand or inventory.", 1),
        CAPTURED_BULLET("tooltip.misc.captured_shulker_bullet", "Obtained by right clicking shulker bullet with Wind Bottle.", 0),
        EXPLOSION_SHARD("tooltip.misc.explosion_shard", "Obtained by surviving an explosion damage of at least %s.", 1),
        HARD_ICE("tooltip.misc.hard_ice", "Obtained by killing a Drowned with Powdered Snow.", 0),
        STORM_CORE("tooltip.misc.storm_core", "Obtained by killing a Phantom with explosion.", 0),
        BLACKSTONE_CORE("tooltip.misc.blackstone_core", "Obtained by killing a Piglin Brute that has Incarceration effect.", 0),
        RESONANT_FEATHER("tooltip.misc.resonant_feather", "Obtained when a chicken survives a sonic boom attack.", 0),
        SPACE_SHARD("tooltip.misc.space_shard", "Obtained by causing a projectile damage of at least %s.", 1),
        FORCE_FIELD("tooltip.misc.force_field", "Obtained by killing a wither with arrow.", 0),
        WARDEN_BONE_SHARD("tooltip.misc.warden_bone_shard", "Dropped when Warden is killed by player.", 0),
        GUARDIAN_EYE("tooltip.misc.guardian_eye", "Dropped when Elder Guardian is killed by lightning strike.", 0),
        GUARDIAN_RUNE("tooltip.misc.guardian_rune", "Right click guardian to turn it into an elder guardian.", 0),
        PIGLIN_RUNE("tooltip.misc.piglin_rune", "Right click piglin to turn it into a piglin brute.", 0),
        BURNT_TITLE("jei.burnt.title", "Burning", 0),
        BURNT_COUNT("jei.burnt.count", "One in %s chance of conversion", 1),
        DIFFUSE_TITLE("jei.diffuse.title", "Diffusion", 0),
        FLOAT("tooltip.misc.float", "This item will float in the air.", 0),
        WARP_RECORD("tooltip.misc.warp_record", "Right click to record position. After that, right click to teleport. Durability: %s", 1),
        WARP_TELEPORT("tooltip.misc.warp_teleport", "Right click in inventory or UI to teleport. Durability: %s", 1),
        WARP_POS("tooltip.misc.warp_pos", "Target: %s, (%s,%s,%s)", 4),
        WARP_GRIND("tooltip.misc.warp_grind", "Use grindstone to remove record", 0),
        TOTEM_DREAM("tooltip.misc.totem_dream", "Return players back to home when triggers, and becomes a fragile warp stone to go back. Valid against void damage. Also heal player to full health.", 0),
        TOTEM_SEA("tooltip.misc.totem_sea", "It's stackable, but can only be triggered when in water or rain.", 0),
        CHARGE_THROW("tooltip.misc.charge_throw", "Right click to throw at target", 0),
        EFFECT_CHARGE("tooltip.misc.effect_charge", "Apply on Hit: %s", 1),
        EXPLOSION_CHARGE("tooltip.misc.explosion_charge", "Create explosion of level %s on Hit", 1),
        ARMOR_IMMUNE("tooltip.tool.immune", "Immune to: ", 0),
        POSEIDITE_TOOL("tooltip.tool.poseidite_tool", "Sharper and faster when user is in rain or water. Effective against water based mobs and mobs sensitive to water.", 0),
        POSEIDITE_ARMOR("tooltip.tool.poseidite_armor", "When user is in rain or water: provides extra protection, walk/swim speed boost, and conduit/dolphin grace effect.", 0),
        SCULKIUM_TOOL("tooltip.tool.sculkium_tool", "Breaks all breakable blocks for the same speed. Be aware of the breaking level.", 0),
        SCULKIUM_ARMOR("tooltip.tool.sculkium_armor", "Dampened: When wearing 4 pieces of armors with dampened effect, cancel all vibrations emitted by wearer.", 0),
        SHULKERATE_TOOL("tooltip.tool.shulkerate_tool", "Really durable. Not easily damaged. Increase Reach and Attack distance", 0),
        SHULKERATE_ARMOR("tooltip.tool.shulkerate_armor", "Really durable. Not easily damaged.", 0),
        TOTEMIC_TOOL("tooltip.tool.totemic_tool", "Heal user when used. Effective against undead mobs.", 0),
        TOTEMIC_ARMOR("tooltip.tool.totemic_armor", "Heal user when damaged. Regenerate health over time.", 0),
        DELAY_WARNING("msg.delay_warning", "Your tool needs %s to break more than %s blocks", 2),
        SONIC_SHOOTER("tooltip.misc.sonic_shooter", "Hold use to charge. Shoot sonic boom automatically when charged to full. Damage all mobs in front of you.", 0),
        HELLFIRE_WAND("tooltip.misc.hellfire_wand", "Hold use to start. Gradually grows a ring of fire on target position. On release, damage all mobs inside the ring.", 0),
        WINTERSTORM_WAND("tooltip.misc.winterstorm_wand", "Hold use to create a ring of storm. Push mobs away and freeze them.", 0),
        DIFFUSION_WAND("tooltip.misc.diffusion_wand", "Right click a block of gem/dust to diffuse it into nearby stones to create ore. Check JEI for recipe", 0),
        BANNED("tooltip.misc.banned", "This item is disabled.", 0),
        BANNED_ENCH("tooltip.misc.banned_ench", "Disabled", 0),
        DIGGER_ACTIVATED("msg.digger_activated", "Activated: %s", 1),
        TREE_CHOP("tooltip.ench.tree", "Breaks leaves as well, and doesn't cost durability when breaking leaves", 0),
        DIGGER_ROTATE("tooltip.ench.rotate", "Press keybind [%s] to toggle", 1);

        final String id;

        final String def;

        final int count;

        private IDS(String id, String def, int count) {
            this.id = id;
            this.def = def;
            this.count = count;
        }

        public MutableComponent get(Object... objs) {
            if (objs.length != this.count) {
                throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
            } else {
                return LangData.translate("l2complements." + this.id, objs);
            }
        }
    }

    public interface LangEnum<T extends Enum<T> & LangData.LangEnum<T>> {

        int getCount();

        @Nullable
        default Class<? extends Enum<?>> mux() {
            return null;
        }

        default T getThis() {
            return (T) this;
        }
    }
}