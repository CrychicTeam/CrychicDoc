package se.mickelus.tetra.gui.stats;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.bar.GuiStatBarBlockingDuration;
import se.mickelus.tetra.gui.stats.bar.GuiStatBarIntegrity;
import se.mickelus.tetra.gui.stats.bar.GuiStatIndicator;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterAnd;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.gui.stats.getter.StatGetterDurability;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterEnchantmentLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterFocus;
import se.mickelus.tetra.gui.stats.getter.StatGetterMagicCapacity;
import se.mickelus.tetra.gui.stats.getter.StatGetterReaching;
import se.mickelus.tetra.gui.stats.getter.StatGetterSpread;
import se.mickelus.tetra.gui.stats.getter.StatGetterStability;
import se.mickelus.tetra.gui.stats.getter.StatGetterSweepingRange;
import se.mickelus.tetra.gui.stats.getter.StatGetterToolLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterArthropod;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterAttackSpeed;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterBashing;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterBlockingReflect;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterCounterweight;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterCriticalStrike;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimal;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimalSingle;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDrawStrength;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterFierySelf;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterHowling;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterMultiValue;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterMultishot;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterNone;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentage;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentageDecimal;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterReaching;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterScannerHorizontalRange;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterSweeping;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterUnbreaking;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterVelocity;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionsInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuiverInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;
import se.mickelus.tetra.properties.TetraAttributes;

@ParametersAreNonnullByDefault
public class GuiStats {

    public static final IStatGetter sharpnessGetter = new StatGetterEnchantmentLevel(Enchantments.SHARPNESS, 0.5, 0.5);

    public static final IStatGetter attackDamageGetter = StatsHelper.sum(new StatGetterAttribute(Attributes.ATTACK_DAMAGE), sharpnessGetter);

    public static final GuiStatBar attackDamage = new GuiStatBar(0, 0, 59, "tetra.stats.attack_damage", 0.0, 40.0, false, attackDamageGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.attack_damage.tooltip", attackDamageGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.sharpness", 17, sharpnessGetter, new TooltipGetterDecimalSingle("tetra.stats.sharpness.tooltip", sharpnessGetter)));

    public static final IStatGetter attackDamageNormalizedGetter = StatsHelper.sum(new StatGetterAttribute(Attributes.ATTACK_DAMAGE, true), sharpnessGetter);

    public static final GuiStatBar attackDamageNormalized = new GuiStatBar(0, 0, 59, "tetra.stats.attack_damage_normalized", 0.0, 20.0, false, attackDamageNormalizedGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.attack_damage_normalized.tooltip", attackDamageNormalizedGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.sharpness", 17, sharpnessGetter, new TooltipGetterDecimalSingle("tetra.stats.sharpness.tooltip", sharpnessGetter)));

    public static final IStatGetter counterweightGetter = new StatGetterEffectLevel(ItemEffect.counterweight, 1.0);

    public static final IStatGetter attackSpeedGetter = new StatGetterAttribute(Attributes.ATTACK_SPEED);

    public static final GuiStatBar attackSpeed = new GuiStatBar(0, 0, 59, "tetra.stats.speed", 0.0, 4.0, false, attackSpeedGetter, LabelGetterBasic.decimalLabel, new TooltipGetterAttackSpeed(attackSpeedGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.counterweight", 5, counterweightGetter, new TooltipGetterCounterweight()));

    public static final IStatGetter attackSpeedGetterNormalized = new StatGetterAttribute(Attributes.ATTACK_SPEED, true, true);

    public static final GuiStatBar attackSpeedNormalized = new GuiStatBar(0, 0, 59, "tetra.stats.speed_normalized", -3.0, 3.0, false, true, false, attackSpeedGetterNormalized, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.speed_normalized.tooltip", attackSpeedGetterNormalized));

    public static final IStatGetter powerGetter = new StatGetterEnchantmentLevel(Enchantments.POWER_ARROWS, 0.5, 0.5);

    public static final IStatGetter drawStrengthGetter = StatsHelper.sum(new StatGetterAttribute(TetraAttributes.drawStrength.get()), powerGetter);

    public static final GuiStatBar drawStrength = new GuiStatBar(0, 0, 59, "tetra.stats.draw_strength", 0.0, 40.0, false, drawStrengthGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDrawStrength(drawStrengthGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.power", 17, powerGetter, new TooltipGetterDecimalSingle("tetra.stats.power.tooltip", powerGetter)));

    public static final IStatGetter quickChargeGetter = new StatGetterEnchantmentLevel(Enchantments.QUICK_CHARGE, -0.2);

    public static final IStatGetter quickChargeGetterInverted = new StatGetterEnchantmentLevel(Enchantments.QUICK_CHARGE, 0.2);

    public static final IStatGetter drawSpeedGetter = StatsHelper.sum(new StatGetterAttribute(TetraAttributes.drawSpeed.get()), quickChargeGetter);

    public static final GuiStatBar drawSpeed = new GuiStatBar(0, 0, 59, "tetra.stats.draw_speed", 0.0, 10.0, false, false, true, drawSpeedGetter, LabelGetterBasic.decimalLabelInverted, new TooltipGetterDecimal("tetra.stats.draw_speed.tooltip", drawSpeedGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.quick_charge", 17, quickChargeGetterInverted, new TooltipGetterDecimalSingle("tetra.stats.quick_charge.tooltip", quickChargeGetterInverted)));

    public static final GuiStatBar drawSpeedNormalized = new GuiStatBar(0, 0, 59, "tetra.stats.draw_speed_normalized", -4.0, 4.0, false, true, true, drawSpeedGetter, LabelGetterBasic.decimalLabelInverted, new TooltipGetterDecimal("tetra.stats.draw_speed_normalized.tooltip", drawSpeedGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.quick_charge", 17, quickChargeGetterInverted, new TooltipGetterDecimalSingle("tetra.stats.quick_charge.tooltip", quickChargeGetterInverted)));

    public static final IStatGetter abilityDamageGetter = new StatGetterAttribute(TetraAttributes.abilityDamage.get());

    public static final GuiStatBar abilityDamage = new GuiStatBar(0, 0, 59, "tetra.stats.ability_damage", 0.0, 40.0, false, abilityDamageGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.ability_damage.tooltip", abilityDamageGetter));

    public static final IStatGetter abilityCooldownGetter = new StatGetterAttribute(TetraAttributes.abilityCooldown.get());

    public static final GuiStatBar abilityCooldown = new GuiStatBar(0, 0, 59, "tetra.stats.ability_speed", 0.0, 32.0, false, false, true, abilityCooldownGetter, LabelGetterBasic.decimalLabelInverted, new TooltipGetterDecimal("tetra.stats.ability_speed.tooltip", abilityCooldownGetter));

    public static final GuiStatBar abilityCooldownNormalized = new GuiStatBar(0, 0, 59, "tetra.stats.ability_speed_normalized", -16.0, 16.0, false, true, true, abilityCooldownGetter, LabelGetterBasic.decimalLabelInverted, new TooltipGetterDecimal("tetra.stats.ability_speed_normalized.tooltip", abilityCooldownGetter));

    public static final IStatGetter reachGetter = new StatGetterAttribute(ForgeMod.BLOCK_REACH.get(), true);

    public static final GuiStatBar reach = new GuiStatBar(0, 0, 59, "tetra.stats.reach", -10.0, 10.0, false, true, false, reachGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDecimalSingle("tetra.stats.reach.tooltip", reachGetter));

    public static final IStatGetter attackRangeGetter = new StatGetterAttribute(ForgeMod.ENTITY_REACH.get(), true);

    public static final GuiStatBar attackRange = new GuiStatBar(0, 0, 59, "tetra.stats.attack_range", -10.0, 10.0, false, true, false, attackRangeGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDecimalSingle("tetra.stats.attack_range.tooltip", attackRangeGetter));

    public static final IStatGetter durabilityGetter = new StatGetterDurability();

    public static final GuiStatBar durability = new GuiStatBar(0, 0, 59, "tetra.stats.durability", 0.0, 2400.0, false, durabilityGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.durability.tooltip", durabilityGetter));

    public static final IStatGetter armorGetter = new StatGetterAttribute(Attributes.ARMOR);

    public static final GuiStatBar armor = new GuiStatBar(0, 0, 59, "tetra.stats.armor", 0.0, 20.0, false, armorGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterInteger("tetra.stats.armor.tooltip", armorGetter));

    public static final IStatGetter toughnessGetter = new StatGetterAttribute(Attributes.ARMOR_TOUGHNESS);

    public static final GuiStatBar toughness = new GuiStatBar(0, 0, 59, "tetra.stats.toughness", 0.0, 20.0, false, toughnessGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterInteger("tetra.stats.toughness.tooltip", toughnessGetter));

    public static final IStatGetter shieldbreakerGetter = new StatGetterEffectLevel(ItemEffect.shieldbreaker, 1.0);

    public static final GuiStatBar shieldbreaker = new GuiStatBar(0, 0, 59, "tetra.stats.shieldbreaker", 0.0, 1.0, false, shieldbreakerGetter, LabelGetterBasic.noLabel, new TooltipGetterNone("tetra.stats.shieldbreaker.tooltip"));

    public static final GuiStatBar blocking = new GuiStatBarBlockingDuration(0, 0, 59);

    public static final IStatGetter blockingReflectGetter = new StatGetterEffectLevel(ItemEffect.blockingReflect, 1.0);

    public static final GuiStatBar blockingReflect = new GuiStatBar(0, 0, 59, "tetra.stats.blocking_reflect", 0.0, 100.0, false, blockingReflectGetter, LabelGetterBasic.percentageLabel, new TooltipGetterBlockingReflect());

    public static final IStatGetter bashingGetter = new StatGetterEffectLevel(ItemEffect.bashing, 1.0);

    public static final GuiStatBar bashing = new GuiStatBar(0, 0, 59, "tetra.stats.bashing", 0.0, 16.0, false, bashingGetter, LabelGetterBasic.integerLabel, new TooltipGetterBashing());

    public static final IStatGetter throwableGetter = new StatGetterEffectEfficiency(ItemEffect.throwable, 100.0);

    public static final GuiStatBar throwable = new GuiStatBar(0, 0, 59, "tetra.stats.throwable", 0.0, 300.0, false, throwableGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentageDecimal("tetra.stats.throwable.tooltip", throwableGetter));

    public static final IStatGetter ricochetGetter = new StatGetterEffectLevel(ItemEffect.ricochet, 1.0);

    public static final GuiStatBar ricochet = new GuiStatBar(0, 0, 59, "tetra.stats.ricochet", 0.0, 12.0, true, ricochetGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.ricochet.tooltip", ricochetGetter));

    public static final IStatGetter piercingGetter = new StatGetterEffectLevel(ItemEffect.piercing, 1.0);

    public static final GuiStatBar piercing = new GuiStatBar(0, 0, 59, "tetra.stats.piercing", 0.0, 12.0, true, piercingGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.piercing.tooltip", piercingGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.piercing_harvest", 6, new StatGetterEffectLevel(ItemEffect.piercingHarvest, 1.0), new TooltipGetterNone("tetra.stats.piercing_harvest.tooltip")));

    public static final IStatGetter jabGetter = new StatGetterEffectLevel(ItemEffect.jab, 1.0);

    public static final GuiStatBar jab = new GuiStatBar(0, 0, 59, "tetra.stats.jab", 0.0, 300.0, false, jabGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentageDecimal("tetra.stats.jab.tooltip", jabGetter));

    public static final IStatGetter quickslotGetter = new StatGetterEffectLevel(ItemEffect.quickSlot, 1.0);

    public static final GuiStatBar quickslot = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.quickslot", 0.0, 12.0, true, quickslotGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.toolbelt.quickslot.tooltip", quickslotGetter));

    public static final IStatGetter potionStorageGetter = new StatGetterEffectLevel(ItemEffect.potionSlot, 1.0);

    public static final GuiStatBar potionStorage = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.potion_storage", 0.0, (double) PotionsInventory.maxSize, true, potionStorageGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.toolbelt.potion_storage.tooltip", potionStorageGetter));

    public static final IStatGetter storageGetter = new StatGetterEffectLevel(ItemEffect.storageSlot, 1.0);

    public static final GuiStatBar storage = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.storage", 0.0, (double) StorageInventory.maxSize, false, storageGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.toolbelt.storage.tooltip", storageGetter));

    public static final IStatGetter quiverGetter = new StatGetterEffectLevel(ItemEffect.quiverSlot, 1.0);

    public static final GuiStatBar quiver = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.quiver", 0.0, (double) QuiverInventory.maxSize, true, quiverGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.toolbelt.quiver.tooltip", quiverGetter));

    public static final IStatGetter boosterGetter = new StatGetterEffectLevel(ItemEffect.booster, 1.0);

    public static final GuiStatBar booster = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.booster", 0.0, 3.0, true, boosterGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.toolbelt.booster.tooltip", boosterGetter));

    public static final IStatGetter suspendSelfGetter = new StatGetterEffectLevel(ItemEffect.suspendSelf, 1.0);

    public static final GuiStatBar suspendSelf = new GuiStatBar(0, 0, 59, "tetra.stats.toolbelt.suspend_self", 0.0, 1.0, false, suspendSelfGetter, LabelGetterBasic.noLabel, new TooltipGetterNone("tetra.stats.toolbelt.suspend_self.tooltip"));

    public static final IStatGetter sweepingGetter = StatsHelper.sum(new StatGetterEffectLevel(ItemEffect.sweeping, 12.5), new StatGetterEnchantmentLevel(Enchantments.SWEEPING_EDGE, 12.5));

    public static final GuiStatBar sweeping = new GuiStatBar(0, 0, 59, "tetra.stats.sweeping", 0.0, 100.0, false, sweepingGetter, LabelGetterBasic.percentageLabelDecimal, new TooltipGetterSweeping(sweepingGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.truesweep", 4, new StatGetterEffectLevel(ItemEffect.truesweep, 1.0), new TooltipGetterNone("tetra.stats.truesweep.tooltip")));

    public static final IStatGetter sweepingRangeGetter = new StatGetterSweepingRange();

    public static final GuiStatBar sweepingRange = new GuiStatBar(0, 0, 59, "tetra.stats.sweeping.efficiency", 0.0, 10.0, false, sweepingRangeGetter, LabelGetterBasic.integerLabel, new TooltipGetterDecimalSingle("tetra.stats.sweeping.efficiency.tooltip", sweepingRangeGetter));

    public static final IStatGetter bleedingGetter = new StatGetterEffectLevel(ItemEffect.bleeding, 4.0);

    public static final GuiStatBar bleeding = new GuiStatBar(0, 0, 59, "tetra.stats.bleeding", 0.0, 20.0, false, bleedingGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.bleeding.tooltip", bleedingGetter));

    public static final IStatGetter backstabGetter = new StatGetterEffectLevel(ItemEffect.backstab, 25.0, 25.0);

    public static final GuiStatBar backstab = new GuiStatBar(0, 0, 59, "tetra.stats.backstab", 0.0, 200.0, false, backstabGetter, LabelGetterBasic.percentageLabelDecimal, new TooltipGetterPercentageDecimal("tetra.stats.backstab.tooltip", backstabGetter));

    public static final IStatGetter armorPenetrationGetter = new StatGetterEffectLevel(ItemEffect.armorPenetration, 1.0);

    public static final GuiStatBar armorPenetration = new GuiStatBar(0, 0, 59, "tetra.stats.armorPenetration", 0.0, 100.0, false, armorPenetrationGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentage("tetra.stats.armorPenetration.tooltip", armorPenetrationGetter));

    public static final IStatGetter crushingGetter = new StatGetterEffectLevel(ItemEffect.crushing, 1.0);

    public static final GuiStatBar crushing = new GuiStatBar(0, 0, 59, "tetra.stats.crushing", 0.0, 10.0, false, crushingGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.crushing.tooltip", crushingGetter));

    public static final IStatGetter skeweringGetter = new StatGetterEffectLevel(ItemEffect.skewering, 1.0);

    public static final GuiStatBar skewering = new GuiStatBar(0, 0, 59, "tetra.stats.skewering", 0.0, 10.0, false, skeweringGetter, LabelGetterBasic.integerLabel, new TooltipGetterMultiValue("tetra.stats.skewering.tooltip", StatsHelper.withStats(skeweringGetter, new StatGetterEffectEfficiency(ItemEffect.skewering, 1.0)), StatsHelper.withFormat(StatFormat.noDecimal, StatFormat.noDecimal)));

    public static final IStatGetter severingGetter = new StatGetterEffectLevel(ItemEffect.severing, 1.0);

    public static final GuiStatBar severing = new GuiStatBar(0, 0, 59, "tetra.stats.severing", 0.0, 100.0, false, severingGetter, LabelGetterBasic.percentageLabel, new TooltipGetterMultiValue("tetra.stats.severing.tooltip", StatsHelper.withStats(severingGetter, new StatGetterEffectEfficiency(ItemEffect.severing, 1.0)), StatsHelper.withFormat(StatFormat.noDecimal, StatFormat.noDecimal)));

    public static final IStatGetter stunGetter = new StatGetterEffectLevel(ItemEffect.stun, 1.0);

    public static final GuiStatBar stun = new GuiStatBar(0, 0, 59, "tetra.stats.stun", 0.0, 100.0, false, stunGetter, LabelGetterBasic.percentageLabel, new TooltipGetterMultiValue("tetra.stats.stun.tooltip", StatsHelper.withStats(stunGetter, new StatGetterEffectEfficiency(ItemEffect.stun, 1.0)), StatsHelper.withFormat(StatFormat.noDecimal, StatFormat.oneDecimal)));

    public static final IStatGetter howlingGetter = new StatGetterEffectLevel(ItemEffect.howling, 1.0);

    public static final GuiStatBar howling = new GuiStatBar(0, 0, 59, "tetra.stats.howling", 0.0, 8.0, false, howlingGetter, LabelGetterBasic.integerLabel, new TooltipGetterHowling());

    public static final IStatGetter knockbackGetter = new StatGetterEnchantmentLevel(Enchantments.KNOCKBACK, 0.5);

    public static final GuiStatBar knockback = new GuiStatBar(0, 0, 59, "tetra.stats.knockback", 0.0, 10.0, false, knockbackGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.knockback.tooltip", knockbackGetter));

    public static final IStatGetter lootingGetter = new StatGetterEnchantmentLevel(Enchantments.MOB_LOOTING, 1.0);

    public static final GuiStatBar looting = new GuiStatBar(0, 0, 59, "tetra.stats.looting", 0.0, 20.0, false, lootingGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.looting.tooltip", lootingGetter));

    public static final IStatGetter fieryGetter = new StatGetterEnchantmentLevel(Enchantments.FIRE_ASPECT, 4.0);

    public static final GuiStatBar fiery = new GuiStatBar(0, 0, 59, "tetra.stats.fiery", 0.0, 32.0, false, fieryGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.fiery.tooltip", fieryGetter));

    public static final IStatGetter smiteGetter = new StatGetterEnchantmentLevel(Enchantments.SMITE, 2.5);

    public static final GuiStatBar smite = new GuiStatBar(0, 0, 59, "tetra.stats.smite", 0.0, 25.0, false, smiteGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimal("tetra.stats.smite.tooltip", smiteGetter));

    public static final IStatGetter arthropodGetter = new StatGetterEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, 2.5);

    public static final GuiStatBar arthropod = new GuiStatBar(0, 0, 59, "tetra.stats.arthropod", 0.0, 25.0, false, arthropodGetter, LabelGetterBasic.decimalLabel, new TooltipGetterArthropod());

    public static final IStatGetter unbreakingGetter = new StatGetterEnchantmentLevel(Enchantments.UNBREAKING, 1.0);

    public static final GuiStatBar unbreaking = new GuiStatBar(0, 0, 59, "tetra.stats.unbreaking", 0.0, 3.0, true, unbreakingGetter, LabelGetterBasic.integerLabel, new TooltipGetterUnbreaking(unbreakingGetter));

    public static final IStatGetter mendingGetter = new StatGetterEnchantmentLevel(Enchantments.MENDING, 2.0);

    public static final GuiStatBar mending = new GuiStatBar(0, 0, 59, "tetra.stats.mending", 0.0, 10.0, false, mendingGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.mending.tooltip", mendingGetter));

    public static final IStatGetter silkTouchGetter = new StatGetterEnchantmentLevel(Enchantments.SILK_TOUCH, 1.0);

    public static final IStatGetter replantGetter = new StatGetterAnd(silkTouchGetter, new StatGetterEffectLevel(ItemEffect.sweepingStrike, 1.0), new StatGetterToolLevel(ToolActions.HOE_DIG));

    public static final GuiStatBar silkTouch = new GuiStatBar(0, 0, 59, "tetra.stats.silkTouch", 0.0, 1.0, false, silkTouchGetter, LabelGetterBasic.noLabel, new TooltipGetterDecimal("tetra.stats.silkTouch.tooltip", silkTouchGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.replanting", 23, replantGetter, new TooltipGetterNone("tetra.stats.replanting.tooltip")));

    public static final IStatGetter fortuneGetter = new StatGetterEnchantmentLevel(Enchantments.BLOCK_FORTUNE, 1.0);

    public static final GuiStatBar fortune = new GuiStatBar(0, 0, 59, "tetra.stats.fortune", 0.0, 20.0, false, fortuneGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.fortune.tooltip", fortuneGetter));

    public static final IStatGetter infinityGetter = new StatGetterEnchantmentLevel(Enchantments.INFINITY_ARROWS, 1.0);

    public static final GuiStatBar infinity = new GuiStatBar(0, 0, 59, "tetra.stats.infinity", 0.0, 1.0, false, infinityGetter, LabelGetterBasic.noLabel, new TooltipGetterInteger("tetra.stats.infinity.tooltip", infinityGetter));

    public static final IStatGetter flameGetter = new StatGetterEnchantmentLevel(Enchantments.FLAMING_ARROWS, 4.0);

    public static final GuiStatBar flame = new GuiStatBar(0, 0, 59, "tetra.stats.flame", 0.0, 2.0, false, flameGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.flame.tooltip", flameGetter));

    public static final IStatGetter punchGetter = new StatGetterEnchantmentLevel(Enchantments.PUNCH_ARROWS, 1.0);

    public static final GuiStatBar punch = new GuiStatBar(0, 0, 59, "tetra.stats.punch", 0.0, 4.0, false, punchGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.punch.tooltip", punchGetter));

    public static final IStatGetter quickStrikeGetter = new StatGetterEffectLevel(ItemEffect.quickStrike, 5.0, 20.0);

    public static final GuiStatBar quickStrike = new GuiStatBar(0, 0, 59, "tetra.stats.quickStrike", 0.0, 100.0, false, quickStrikeGetter, LabelGetterBasic.percentageLabelDecimal, new TooltipGetterPercentageDecimal("tetra.stats.quickStrike.tooltip", quickStrikeGetter));

    public static final IStatGetter softStrikeGetter = new StatGetterEffectLevel(ItemEffect.softStrike, 1.0);

    public static final GuiStatBar softStrike = new GuiStatBar(0, 0, 59, "tetra.stats.softStrike", 0.0, 1.0, false, softStrikeGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.softStrike.tooltip", softStrikeGetter));

    public static final IStatGetter fierySelfGetter = new StatGetterEffectEfficiency(ItemEffect.fierySelf, 100.0);

    public static final GuiStatBar fierySelf = new GuiStatBar(0, 0, 59, "tetra.stats.fierySelf", 0.0, 100.0, false, false, true, fierySelfGetter, LabelGetterBasic.percentageLabelDecimalInverted, new TooltipGetterFierySelf());

    public static final IStatGetter enderReverbGetter = new StatGetterEffectEfficiency(ItemEffect.enderReverb, 100.0);

    public static final GuiStatBar enderReverb = new GuiStatBar(0, 0, 59, "tetra.stats.enderReverb", 0.0, 100.0, false, false, true, enderReverbGetter, LabelGetterBasic.percentageLabelDecimalInverted, new TooltipGetterPercentageDecimal("tetra.stats.enderReverb.tooltip", enderReverbGetter));

    public static final IStatGetter sculkTaintGetter = new StatGetterEffectEfficiency(ItemEffect.sculkTaint, 100.0);

    public static final GuiStatBar sculkTaint = new GuiStatBar(0, 0, 59, "tetra.stats.sculkTaint", 0.0, 10.0, false, false, true, sculkTaintGetter, LabelGetterBasic.percentageLabelDecimalInverted, new TooltipGetterPercentageDecimal("tetra.stats.sculkTaint.tooltip", sculkTaintGetter));

    public static final IStatGetter medialLimitGetter = new StatGetterEffectLevel(ItemEffect.extractionMedialLimit, 1.0);

    public static final GuiStatBar medialLimit = new GuiStatBar(0, 0, 59, "tetra.stats.medialLimit", 0.0, 15.0, true, medialLimitGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.medialLimit.tooltip", medialLimitGetter));

    public static final IStatGetter lateralLimitGetter = new StatGetterEffectLevel(ItemEffect.extractionLateralLimit, 1.0);

    public static final GuiStatBar lateralLimit = new GuiStatBar(0, 0, 59, "tetra.stats.lateralLimit", 0.0, 15.0, true, lateralLimitGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.lateralLimit.tooltip", lateralLimitGetter));

    public static final IStatGetter axialLimitGetter = new StatGetterEffectLevel(ItemEffect.extractionAxialLimit, 1.0);

    public static final GuiStatBar axialLimit = new GuiStatBar(0, 0, 59, "tetra.stats.axialLimit", 0.0, 15.0, true, axialLimitGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.axialLimit.tooltip", axialLimitGetter));

    public static final IStatGetter criticalGetter = new StatGetterEffectLevel(ItemEffect.criticalStrike, 1.0);

    public static final GuiStatBar criticalStrike = new GuiStatBar(0, 0, 59, "tetra.stats.criticalStrike", 0.0, 100.0, false, criticalGetter, LabelGetterBasic.percentageLabel, new TooltipGetterCriticalStrike());

    public static final IStatGetter intuitGetter = new StatGetterEffectLevel(ItemEffect.intuit, 1.0);

    public static final GuiStatBar intuit = new GuiStatBar(0, 0, 59, "tetra.stats.intuit", 0.0, 8.0, false, intuitGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.intuit.tooltip", intuitGetter));

    public static final IStatGetter earthbindGetter = new StatGetterEffectLevel(ItemEffect.earthbind, 1.0);

    public static final GuiStatBar earthbind = new GuiStatBar(0, 0, 59, "tetra.stats.earthbind", 0.0, 16.0, false, earthbindGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.earthbind.tooltip", earthbindGetter));

    public static final IStatGetter reachingGetter = new StatGetterReaching();

    public static final GuiStatBar reaching = new GuiStatBar(0, 0, 59, "tetra.stats.reaching", 0.0, 100.0, false, reachingGetter, LabelGetterBasic.percentageLabel, new TooltipGetterReaching()).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.tool.reaching_sweeping", 1, new StatGetterEffectLevel(ItemEffect.sweepingStrike, 1.0), new TooltipGetterNone("tetra.stats.tool.reaching_sweeping.tooltip")));

    public static final IStatGetter jankingGetter = new StatGetterEffectLevel(ItemEffect.janking, 1.0);

    public static final GuiStatBar janking = new GuiStatBar(0, 0, 59, "tetra.stats.janking", 0.0, 16.0, false, jankingGetter, LabelGetterBasic.integerLabel, new TooltipGetterMultiValue("tetra.stats.janking.tooltip", StatsHelper.withStats(jankingGetter, new StatGetterEffectEfficiency(ItemEffect.janking, 100.0)), StatsHelper.withFormat(StatFormat.noDecimal, StatFormat.noDecimal)));

    public static final IStatGetter releaseLatchGetter = new StatGetterEffectLevel(ItemEffect.releaseLatch, 1.0);

    public static final GuiStatBar releaseLatch = new GuiStatBar(0, 0, 59, "tetra.stats.bow.releaseLatch", 0.0, 1.0, false, releaseLatchGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.bow.releaseLatch.tooltip", releaseLatchGetter));

    public static final IStatGetter overbowedGetter = new StatGetterEffectLevel(ItemEffect.overbowed, 0.1);

    public static final GuiStatBar overbowed = new GuiStatBar(0, 0, 59, "tetra.stats.bow.overbowed", 0.0, 10.0, false, overbowedGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDecimalSingle("tetra.stats.bow.overbowed.tooltip", overbowedGetter));

    public static final IStatGetter multishotGetter = StatsHelper.sum(new StatGetterEffectLevel(ItemEffect.multishot, 1.0), new StatGetterEnchantmentLevel(Enchantments.MULTISHOT, 3.0));

    public static final GuiStatBar multishot = new GuiStatBar(0, 0, 59, "tetra.stats.multishot", 0.0, 12.0, true, multishotGetter, LabelGetterBasic.integerLabel, new TooltipGetterMultishot());

    public static final IStatGetter focusEchoGetter = new StatGetterEffectLevel(ItemEffect.focusEcho, 1.0);

    public static final IStatGetter focusGetter = new StatGetterFocus();

    public static final GuiStatBar focus = new GuiStatBar(0, 0, 59, "tetra.stats.focus", 0.0, 2.0, false, focusGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDecimalSingle("tetra.stats.focus.tooltip", focusGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.focusEcho", 19, focusEchoGetter, new TooltipGetterNone("tetra.stats.focusEcho.tooltip")));

    public static final IStatGetter spreadGetter = new StatGetterSpread(ItemEffect.spread);

    public static final GuiStatBar spread = new GuiStatBar(0, 0, 59, "tetra.stats.spread", 0.0, 20.0, false, spreadGetter, LabelGetterBasic.decimalLabel, new TooltipGetterDecimalSingle("tetra.stats.spread.tooltip", spreadGetter)).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.focus", 18, focusGetter, new TooltipGetterDecimal("tetra.stats.focus.tooltip", focusGetter)), new GuiStatIndicator(0, 0, "tetra.stats.focusEcho", 19, focusEchoGetter, new TooltipGetterNone("tetra.stats.focusEcho.tooltip")));

    public static final IStatGetter zoomGetter = new StatGetterEffectLevel(ItemEffect.zoom, 0.1);

    public static final GuiStatBar zoom = new GuiStatBar(0, 0, 59, "tetra.stats.zoom", 0.0, 10.0, false, zoomGetter, LabelGetterBasic.singleDecimalLabel, new TooltipGetterDecimalSingle("tetra.stats.zoom.tooltip", zoomGetter));

    public static final IStatGetter velocityGetter = new StatGetterEffectLevel(ItemEffect.velocity, 1.0);

    public static final IStatGetter suspendGetter = new StatGetterEffectLevel(ItemEffect.suspend, 1.0);

    public static final GuiStatBar velocity = new GuiStatBar(0, 0, 59, "tetra.stats.velocity", 0.0, 200.0, false, velocityGetter, LabelGetterBasic.percentageLabel, new TooltipGetterVelocity()).setIndicators(new GuiStatIndicator(0, 0, "tetra.stats.suspend", 3, suspendGetter, new TooltipGetterNone("tetra.stats.suspend.tooltip")));

    public static final IStatGetter magicCapacityGetter = new StatGetterMagicCapacity();

    public static final GuiStatBar magicCapacity = new GuiStatBar(0, 0, 59, "tetra.stats.magicCapacity", 0.0, 150.0, false, magicCapacityGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.magicCapacity.tooltip", magicCapacityGetter));

    public static final IStatGetter stabilityGetter = new StatGetterStability();

    public static final GuiStatBar stability = new GuiStatBar(0, 0, 59, "tetra.stats.stability", -100.0, 100.0, false, true, false, stabilityGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentageDecimal("tetra.stats.stability.tooltip", stabilityGetter));

    public static final IStatGetter workableGetter = new StatGetterEffectLevel(ItemEffect.workable, 1.0);

    public static final GuiStatBar workable = new GuiStatBar(0, 0, 59, "tetra.stats.workable", 0.0, 100.0, false, workableGetter, LabelGetterBasic.percentageLabel, new TooltipGetterPercentageDecimal("tetra.stats.workable.tooltip", workableGetter));

    public static final IStatGetter sweeperRangeGetter = new StatGetterEffectLevel(ItemEffect.sweeperRange, 1.0);

    public static final GuiStatBar sweeperRange = new GuiStatBar(0, 0, 59, "tetra.stats.holo.sweeperRange", 0.0, 64.0, false, sweeperRangeGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.holo.sweeperRange.tooltip", sweeperRangeGetter));

    public static final IStatGetter sweeperHorizontalSpreadGetter = new StatGetterEffectLevel(ItemEffect.sweeperHorizontalSpread, 4.0);

    public static final GuiStatBar sweeperHorizontalSpread = new GuiStatBar(0, 0, 59, "tetra.stats.holo.sweeperHorizontalSpread", 0.0, 128.0, false, sweeperHorizontalSpreadGetter, LabelGetterBasic.integerLabel, new TooltipGetterScannerHorizontalRange(sweeperHorizontalSpreadGetter));

    public static final IStatGetter sweeperVerticalSpreadGetter = new StatGetterEffectLevel(ItemEffect.sweeperVerticalSpread, 10.0, 40.0);

    public static final GuiStatBar sweeperVerticalSpread = new GuiStatBar(0, 0, 59, "tetra.stats.holo.sweeperVerticalSpread", 0.0, 180.0, false, sweeperVerticalSpreadGetter, LabelGetterBasic.integerLabel, new TooltipGetterInteger("tetra.stats.holo.sweeperVerticalSpread.tooltip", sweeperVerticalSpreadGetter));

    public static final GuiStatBarIntegrity integrity = new GuiStatBarIntegrity(0, 0);

    public static final IStatGetter percussionScannerGetter = new StatGetterEffectLevel(ItemEffect.percussionScanner, 1.0);

    public static final GuiStatBar percussionScanner = new GuiStatBar(0, 0, 59, "tetra.stats.holo.percussionScanner", 0.0, 1.0, false, percussionScannerGetter, LabelGetterBasic.noLabel, new TooltipGetterNone("tetra.stats.holo.percussionScanner.tooltip"));

    private static final ITooltipGetter counterweightTooltip = new TooltipGetterInteger("tetra.stats.counterweight.tooltip", counterweightGetter);

    public static final GuiStatBar counterweight = new GuiStatBar(0, 0, 59, "tetra.stats.counterweight", 0.0, 12.0, true, counterweightGetter, LabelGetterBasic.integerLabel, counterweightTooltip);
}