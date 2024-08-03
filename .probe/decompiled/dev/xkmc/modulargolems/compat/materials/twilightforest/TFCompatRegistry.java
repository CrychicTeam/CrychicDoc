package dev.xkmc.modulargolems.compat.materials.twilightforest;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;

public class TFCompatRegistry {

    public static final RegistryEntry<FieryModifier> FIERY = GolemModifiers.reg("fiery", FieryModifier::new, "Deal %s%% fire damage to mobs not immune to fire");

    public static final RegistryEntry<TFDamageModifier> TF_DAMAGE = GolemModifiers.reg("tf_damage", TFDamageModifier::new, "TF Damage Bonus", "Deal %s%% extra damage in twilight forest");

    public static final RegistryEntry<TFHealingModifier> TF_HEALING = GolemModifiers.reg("tf_healing", TFHealingModifier::new, "TF Healing Bonus", "Healing becomes %s%% more in twilight forest");

    public static final RegistryEntry<CarminiteModifier> CARMINITE = GolemModifiers.reg("carminite", CarminiteModifier::new, "After being hurt, turn invisible and invinsible for %s seconds");

    public static final RegistryEntry<AttributeGolemModifier> NAGA = GolemModifiers.reg("naga", () -> new AttributeGolemModifier(2, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 10.0), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.3), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATTACK, () -> 4.0), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATKKB, () -> 1.0))).register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_CARMINITE = GolemItems.regModUpgrade("carminite", () -> CARMINITE, "twilightforest").lang("Carminite Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_STEELEAF = GolemItems.regModUpgrade("steeleaf", () -> TF_DAMAGE, "twilightforest").lang("Steeleaf Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_FIERY = GolemItems.regModUpgrade("fiery", () -> FIERY, "twilightforest").lang("Fiery Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_IRONWOOD = GolemItems.regModUpgrade("ironwood", () -> TF_HEALING, "twilightforest").lang("Ironwood Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_KNIGHTMETAL = GolemItems.regModUpgrade("knightmetal", () -> GolemModifiers.THORN, "twilightforest").lang("Knightmetal Upgrade").register();

    public static final RegistryEntry<SimpleUpgradeItem> UP_NAGA = GolemItems.regModUpgrade("naga", () -> NAGA, "twilightforest").lang("Naga Upgrade").register();

    public static void register() {
    }
}