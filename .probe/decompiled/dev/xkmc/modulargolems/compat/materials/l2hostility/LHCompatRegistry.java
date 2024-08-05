package dev.xkmc.modulargolems.compat.materials.l2hostility;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionDefenseModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

public class LHCompatRegistry {

    public static final RegistryEntry<HostilityCoreModifier> LH_CORE = GolemModifiers.reg("hostility_core", () -> new HostilityCoreModifier(StatFilterType.HEALTH, 1), "Hostility Core", "All other hostility upgrades don't consume upgrade slots");

    public static final RegistryEntry<HostilityPotionModifier> LH_POTION = GolemModifiers.reg("hostility_potion", () -> new HostilityPotionModifier(StatFilterType.HEALTH, 1), "Hostility Upgrade: Potions", "First level of each kind of potion upgrades don't consume upgrade slot");

    public static final RegistryEntry<AttributeGolemModifier> LH_TANK = GolemModifiers.reg("hostility_tank", () -> new AttributeGolemModifier(5, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, LHConfig.COMMON.tankHealth::get), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, LHConfig.COMMON.tankArmor::get), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_TOUGH, LHConfig.COMMON.tankTough::get)), "Hostility Upgrade: Tanky", null);

    public static final RegistryEntry<AttributeGolemModifier> LH_SPEED = GolemModifiers.reg("hostility_speed", () -> new AttributeGolemModifier(5, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, LHConfig.COMMON.speedy::get)), "Hostility Upgrade: Speedy", null);

    public static final RegistryEntry<PotionDefenseModifier> LH_PROTECTION = GolemModifiers.reg("hostility_protection", () -> new PotionDefenseModifier(4, () -> MobEffects.DAMAGE_RESISTANCE), "Hostility Upgrade: Protection", null);

    public static final RegistryEntry<RegenModifier> LH_REGEN = GolemModifiers.reg("hostility_regen", () -> new RegenModifier(StatFilterType.HEALTH, 5), "Hostility Upgrade: Regeneration", null);

    public static final RegistryEntry<ReflectiveModifier> LH_REFLECTIVE = GolemModifiers.reg("hostility_reflect", () -> new ReflectiveModifier(StatFilterType.HEALTH, 5), "Hostility Upgrade: Reflective", null);

    public static final ItemEntry<SimpleUpgradeItem> CORE = GolemItems.regModUpgrade("hostility_core", () -> LH_CORE, "l2hostility").lang("Hostility Core").register();

    public static final ItemEntry<SimpleUpgradeItem> POTION = GolemItems.regModUpgrade("hostility_potion", () -> LH_POTION, "l2hostility").lang("Hostility Upgrade: Potion").register();

    public static final ItemEntry<SimpleUpgradeItem> TANK = GolemItems.regModUpgrade("hostility_tank", () -> LH_TANK, "l2hostility").lang("Hostility Upgrade: Tanky").register();

    public static final ItemEntry<SimpleUpgradeItem> SPEED = GolemItems.regModUpgrade("hostility_speed", () -> LH_SPEED, "l2hostility").lang("Hostility Upgrade: Speedy").register();

    public static final ItemEntry<SimpleUpgradeItem> PROTECTION = GolemItems.regModUpgrade("hostility_protection", () -> LH_PROTECTION, "l2hostility").lang("Hostility Upgrade: Protection").register();

    public static final ItemEntry<SimpleUpgradeItem> REGEN = GolemItems.regModUpgrade("hostility_regen", () -> LH_REGEN, "l2hostility").lang("Hostility Upgrade: Regeneration").register();

    public static final ItemEntry<SimpleUpgradeItem> REFLECTIVE = GolemItems.regModUpgrade("hostility_reflect", () -> LH_REFLECTIVE, "l2hostility").lang("Hostility Upgrade: Reflective").register();

    public static final TagKey<Item> HOSTILITY_UPGRADE = ItemTags.create(new ResourceLocation("modulargolems", "hostility_upgrades"));

    public static void register() {
        MGTagGen.OPTIONAL_ITEM.add((Consumer) pvd -> pvd.addTag(HOSTILITY_UPGRADE).m_176839_(POTION.getId()).addOptional(TANK.getId()).addOptional(SPEED.getId()).addOptional(PROTECTION.getId()).addOptional(REGEN.getId()).addOptional(REFLECTIVE.getId()));
    }
}