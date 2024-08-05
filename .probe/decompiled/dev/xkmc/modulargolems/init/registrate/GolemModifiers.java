package dev.xkmc.modulargolems.init.registrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionAttackModifier;
import dev.xkmc.modulargolems.content.modifier.base.SimpleFlagModifier;
import dev.xkmc.modulargolems.content.modifier.base.TargetBonusModifier;
import dev.xkmc.modulargolems.content.modifier.common.BellModifier;
import dev.xkmc.modulargolems.content.modifier.common.ThornModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.DamageCapModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.ExplosionResistanceModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.FireImmuneModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.ImmunityModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.MagicImmuneModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.MagicResistanceModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.PlayerImmuneModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.ProjectileRejectModifier;
import dev.xkmc.modulargolems.content.modifier.immunes.ThunderImmuneModifier;
import dev.xkmc.modulargolems.content.modifier.ride.RideUpgrade;
import dev.xkmc.modulargolems.content.modifier.special.PickupModifier;
import dev.xkmc.modulargolems.content.modifier.special.PotionMetaModifier;
import dev.xkmc.modulargolems.content.modifier.special.SonicModifier;
import dev.xkmc.modulargolems.content.modifier.special.TalentMetaModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

public class GolemModifiers {

    public static final RegistryEntry<FireImmuneModifier> FIRE_IMMUNE = reg("fire_immune", FireImmuneModifier::new, "Immune to fire damage. Floats in Lava.");

    public static final RegistryEntry<ThunderImmuneModifier> THUNDER_IMMUNE = reg("thunder_immune", ThunderImmuneModifier::new, "Immune to lightning bolt damage. Attract Lightning. When struck, gain fire resistance for 10 seconds, and heal %s health.");

    public static final RegistryEntry<MagicImmuneModifier> MAGIC_IMMUNE = reg("magic_immune", MagicImmuneModifier::new, "Immune to magic damage");

    public static final RegistryEntry<MagicResistanceModifier> MAGIC_RES = reg("magic_resistant", MagicResistanceModifier::new, "Magic damage taken reduced to %s%% of original");

    public static final RegistryEntry<ThornModifier> THORN = reg("thorn", ThornModifier::new, "Reflect %s%% damage");

    public static final RegistryEntry<ExplosionResistanceModifier> EXPLOSION_RES = reg("explosion_resistant", ExplosionResistanceModifier::new, "Explosion damage taken reduced to %s%% of original, and will not break blocks.");

    public static final RegistryEntry<DamageCapModifier> DAMAGE_CAP = reg("damage_cap", DamageCapModifier::new, "Damage taken are limited within %s%% of max health.");

    public static final RegistryEntry<ProjectileRejectModifier> PROJECTILE_REJECT = reg("projectile_reject", ProjectileRejectModifier::new, "Deflect projectiles. Takes no projectile damage.");

    public static final RegistryEntry<ImmunityModifier> IMMUNITY = reg("immunity", ImmunityModifier::new, "Immune to all damage, except void damage. Mobs won't attack invulnerable golem.");

    public static final RegistryEntry<PlayerImmuneModifier> PLAYER_IMMUNE = reg("player_immune", PlayerImmuneModifier::new, "Immune to friendly fire.");

    public static final RegistryEntry<SonicModifier> SONIC = reg("sonic_boom", SonicModifier::new, "Golem can use Sonic Boom Attack. If the golem can perform area attack, then Sonic Boom can hit multiple targets.");

    public static final RegistryEntry<BellModifier> BELL = reg("bell", BellModifier::new, "When the golem wants to attack, it will ring its bell, attracting all enemies and light them up.");

    public static final RegistryEntry<PickupModifier> PICKUP = reg("pickup", PickupModifier::new, "Pickup", "Golems will pickup items and experiences within %s blocks and give them to you. See Patchouli for full documentation. The golem may destroy items if it find nowhere to store them");

    public static final RegistryEntry<TargetBonusModifier> EMERALD = reg("emerald", () -> new TargetBonusModifier(e -> e.getMobType() == MobType.ILLAGER), "Deal %s%% more damage to illagers");

    public static final RegistryEntry<TalentMetaModifier> TALENTED = reg("talented", TalentMetaModifier::new, "Talented", "First of every kind of upgrades with blue arrow will no longer consume upgrade slots (up to 4)");

    public static final RegistryEntry<PotionMetaModifier> CAULDRON = reg("cauldron", PotionMetaModifier::new, "Cauldron", "Repeated potion upgrades will no longer consume upgrade slots.");

    public static final RegistryEntry<SimpleFlagModifier> FLOAT = reg("float", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.FLOAT), "Golem floats in water and lava instead of sinking");

    public static final RegistryEntry<SimpleFlagModifier> SWIM = reg("swim", () -> new SimpleFlagModifier(StatFilterType.MOVEMENT, GolemFlags.SWIM), "Golem can swim");

    public static final RegistryEntry<SimpleFlagModifier> ENDER_SIGHT = reg("ender_sight", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.SEE_THROUGH), "Golem can see through wall and ceilings.");

    public static final RegistryEntry<SimpleFlagModifier> RECYCLE = reg("recycle", () -> new SimpleFlagModifier(StatFilterType.HEALTH, GolemFlags.RECYCLE), "Drop golem holder of 0 health when killed. Holder will return to inventory is player is present.");

    public static final RegistryEntry<SimpleFlagModifier> PICKUP_NODESTROY = reg("pickup_no_destroy", () -> new SimpleFlagModifier(StatFilterType.MASS, GolemFlags.NO_DESTROY), "Pickup Augment: No Destroy", "When a golem attempts to pickup an item and find nowhere to place it, it will not pickup the item instead. It will cause lag if the golem is in a region with lots of items.");

    public static final RegistryEntry<SimpleFlagModifier> PICKUP_MENDING = reg("pickup_mending", () -> new SimpleFlagModifier(StatFilterType.MASS, GolemFlags.MENDING), "Pickup Augment: Mending", "When a golem picks up experiences, it will try to heal itself with the experience.");

    public static final RegistryEntry<AttributeGolemModifier> ARMOR = reg("armor_up", () -> new AttributeGolemModifier(2, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 10.0))).register();

    public static final RegistryEntry<AttributeGolemModifier> TOUGH = reg("toughness_up", () -> new AttributeGolemModifier(2, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 15.0), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_TOUGH, () -> 6.0))).register();

    public static final RegistryEntry<AttributeGolemModifier> DAMAGE = reg("damage_up", () -> new AttributeGolemModifier(5, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATTACK, () -> 2.0))).register();

    public static final RegistryEntry<AttributeGolemModifier> REGEN = reg("regeneration_up", () -> new AttributeGolemModifier(5, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_REGEN, () -> 1.0))).register();

    public static final RegistryEntry<AttributeGolemModifier> SPEED = reg("speed_up", () -> new AttributeGolemModifier(5, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.2))).register();

    public static final RegistryEntry<AttributeGolemModifier> SIZE_UPGRADE = reg("size_up", () -> new AttributeGolemModifier(2, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.15), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, () -> 0.2), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SIZE, () -> 0.5), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_RANGE, () -> 0.5))).register();

    public static final RegistryEntry<PotionAttackModifier> SLOW = reg("slow", () -> new PotionAttackModifier(StatFilterType.MASS, 3, i -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, i - 1)), "Potion Upgrade: Slowness", null);

    public static final RegistryEntry<PotionAttackModifier> WEAK = reg("weak", () -> new PotionAttackModifier(StatFilterType.MASS, 3, i -> new MobEffectInstance(MobEffects.WEAKNESS, 60, i - 1)), "Potion Upgrade: Weakness", null);

    public static final RegistryEntry<PotionAttackModifier> WITHER = reg("wither", () -> new PotionAttackModifier(StatFilterType.MASS, 3, i -> new MobEffectInstance(MobEffects.WITHER, 60, i - 1)), "Potion Upgrade: Wither", null);

    public static final RegistryEntry<RideUpgrade> MOUNT_UPGRADE = reg("ridding_speed_up", () -> new RideUpgrade(1, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_SPEED, () -> 0.3), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_JUMP, () -> 0.25), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_HEALTH_P, () -> 0.2)), "Mount Upgrade", "Golem will not attack, and will not be targeted for attack.");

    public static <T extends GolemModifier> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup, String name, @Nullable String def) {
        Mutable<RegistryEntry<T>> holder = new MutableObject();
        L2Registrate.GenericBuilder<GolemModifier, T> ans = ModularGolems.REGISTRATE.<GolemModifier, T>generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
        ans.lang(NamedEntry::getDescriptionId, name);
        if (def != null) {
            ans.addMiscData(ProviderType.LANG, pvd -> pvd.add(((GolemModifier) ((RegistryEntry) holder.getValue()).get()).getDescriptionId() + ".desc", def));
        }
        RegistryEntry<T> result = ans.register();
        holder.setValue(result);
        return result;
    }

    public static <T extends GolemModifier> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup, @Nullable String def) {
        Mutable<RegistryEntry<T>> holder = new MutableObject();
        L2Registrate.GenericBuilder<GolemModifier, T> ans = ModularGolems.REGISTRATE.<GolemModifier, T>generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
        if (def != null) {
            ans.addMiscData(ProviderType.LANG, pvd -> pvd.add(((GolemModifier) ((RegistryEntry) holder.getValue()).get()).getDescriptionId() + ".desc", def));
        }
        RegistryEntry<T> result = ans.register();
        holder.setValue(result);
        return result;
    }

    public static <T extends GolemModifier> L2Registrate.GenericBuilder<GolemModifier, T> reg(String id, NonNullSupplier<T> sup) {
        return ModularGolems.REGISTRATE.<GolemModifier, T>generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
    }

    public static void register() {
    }
}