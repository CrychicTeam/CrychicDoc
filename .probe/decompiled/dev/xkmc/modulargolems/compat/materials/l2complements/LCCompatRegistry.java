package dev.xkmc.modulargolems.compat.materials.l2complements;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.PotionAttackModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionDefenseModifier;
import dev.xkmc.modulargolems.content.modifier.base.TargetBonusModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import java.util.function.Consumer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobType;

public class LCCompatRegistry {

    public static final RegistryEntry<ConduitModifier> CONDUIT = GolemModifiers.reg("conduit", ConduitModifier::new, "When in water: Reduce damage taken to %s%%. Every %s seconds, deal %s conduit damage to target in water/rain remotely. Boost following stats:");

    public static final RegistryEntry<FreezingModifier> FREEZE = GolemModifiers.reg("freezing", FreezingModifier::new, "Potion Upgrade: Freezing", "Get Ice Blade and Ice Thorn enchantment effects. Immune to freezing damage.");

    public static final RegistryEntry<SoulFlameModifier> FLAME = GolemModifiers.reg("soul_flame", SoulFlameModifier::new, "Potion Upgrade: Soul Flame", "Get Soul Flame Blade and Soul Flame Thorn enchantment effects. Immune to soul flame damage.");

    public static final RegistryEntry<EnderTeleportModifier> TELEPORT = GolemModifiers.reg("teleport", EnderTeleportModifier::new, "Teleport randomly to avoid physical damage. Teleport toward target when attacking. Teleport has %ss cool down.");

    public static final RegistryEntry<PotionAttackModifier> CURSE = GolemModifiers.reg("curse", () -> new PotionAttackModifier(StatFilterType.MASS, 3, i -> new MobEffectInstance((MobEffect) LCEffects.CURSE.get(), 60 * i)), "Potion Upgrade: Curse", null);

    public static final RegistryEntry<PotionAttackModifier> INCARCERATE = GolemModifiers.reg("incarcerate", () -> new PotionAttackModifier(StatFilterType.MASS, 3, i -> new MobEffectInstance((MobEffect) LCEffects.STONE_CAGE.get(), 20 * i)), "Potion Upgrade: Incarceration", null);

    public static final RegistryEntry<PotionDefenseModifier> CLEANSE = GolemModifiers.reg("cleanse", () -> new PotionDefenseModifier(1, LCEffects.CLEANSE::get), "Potion Upgrade: Cleanse", null);

    public static final RegistryEntry<TargetBonusModifier> POSEIDITE = GolemModifiers.reg("poseidite", () -> new TargetBonusModifier(e -> e.isSensitiveToWater() || e.getMobType() == MobType.WATER), "Deal %s%% more damage to mobs sensitive to water or water based mobs");

    public static final RegistryEntry<TargetBonusModifier> TOTEMIC_GOLD = GolemModifiers.reg("totemic_gold", () -> new TargetBonusModifier(e -> e.getMobType() == MobType.UNDEAD), "Deal %s%% more damage to undead mobs");

    public static final ItemEntry<SimpleUpgradeItem> FORCE_FIELD = GolemItems.regModUpgrade("force_field", () -> GolemModifiers.PROJECTILE_REJECT, "l2complements").lang("Wither Armor Upgrade").register();

    public static final ItemEntry<SimpleUpgradeItem> FREEZE_UP = GolemItems.regModUpgrade("freezing", () -> FREEZE, "l2complements").lang("Potion Upgrade: Freezing").register();

    public static final ItemEntry<SimpleUpgradeItem> FLAME_UP = GolemItems.regModUpgrade("soul_flame", () -> FLAME, "l2complements").lang("Potion Upgrade: Soul Flame").register();

    public static final ItemEntry<SimpleUpgradeItem> TELEPORT_UP = GolemItems.regModUpgrade("teleport", () -> TELEPORT, "l2complements").lang("Teleport Upgrade").register();

    public static final ItemEntry<SimpleUpgradeItem> ATK_UP = GolemItems.regModUpgrade("attack_high", () -> GolemModifiers.DAMAGE, 5, true, "l2complements").lang("Attack Upgrade V").register();

    public static final ItemEntry<SimpleUpgradeItem> SPEED_UP = GolemItems.regModUpgrade("speed_high", () -> GolemModifiers.SPEED, 5, true, "l2complements").lang("Speed Upgrade V").register();

    public static final ItemEntry<SimpleUpgradeItem> UPGRADE_CURSE = GolemItems.regModUpgrade("curse", () -> CURSE, "l2complements").lang("Potion Upgrade: Curse").register();

    public static final ItemEntry<SimpleUpgradeItem> UPGRADE_INCARCERATE = GolemItems.regModUpgrade("incarcerate", () -> INCARCERATE, "l2complements").lang("Potion Upgrade: Incarceration").register();

    public static final ItemEntry<SimpleUpgradeItem> UPGRADE_CLEANSE = GolemItems.regModUpgrade("cleanse", () -> CLEANSE, "l2complements").lang("Potion Upgrade: Cleanse").register();

    public static void register() {
        MGTagGen.OPTIONAL_ITEM.add((Consumer) e -> e.addTag(MGTagGen.POTION_UPGRADES).m_176839_(FLAME_UP.getId()).addOptional(FREEZE_UP.getId()).addOptional(UPGRADE_CURSE.getId()).addOptional(UPGRADE_INCARCERATE.getId()));
        MGTagGen.OPTIONAL_ITEM.add((Consumer) e -> e.addTag(MGTagGen.BLUE_UPGRADES).m_176839_(UPGRADE_CLEANSE.getId()));
    }
}