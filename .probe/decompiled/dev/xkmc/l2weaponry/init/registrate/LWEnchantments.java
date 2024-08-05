package dev.xkmc.l2weaponry.init.registrate;

import com.tterrag.registrate.builders.EnchantmentBuilder;
import com.tterrag.registrate.builders.EnchantmentBuilder.EnchantmentFactory;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.content.enchantment.core.SingleLevelEnchantment;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2weaponry.content.enchantments.ClawBlockEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.EnderHandEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.EnergizedWillEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.HardShieldEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.HeavyEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.HeavyShieldEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.ProjectionEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.RaisedSpiritEnchantment;
import dev.xkmc.l2weaponry.content.enchantments.StealthEnchantment;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.content.item.base.BaseThrowableWeaponItem;
import dev.xkmc.l2weaponry.content.item.base.DoubleWieldItem;
import dev.xkmc.l2weaponry.content.item.base.LWTieredItem;
import dev.xkmc.l2weaponry.content.item.types.ClawItem;
import dev.xkmc.l2weaponry.content.item.types.DaggerItem;
import dev.xkmc.l2weaponry.content.item.types.MacheteItem;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LWEnchantments {

    public static final EnchantmentCategory THROWABLE = EnchantmentCategory.create("throwable", e -> e instanceof BaseThrowableWeaponItem);

    public static final EnchantmentCategory DAGGER = EnchantmentCategory.create("dagger", e -> e instanceof DaggerItem);

    public static final EnchantmentCategory HEAVY_WEAPON = EnchantmentCategory.create("heavy_weapon", e -> e instanceof LWTieredItem t && t.isHeavy() || e instanceof AxeItem);

    public static final EnchantmentCategory SHIELDS = EnchantmentCategory.create("shields", e -> e instanceof BaseShieldItem);

    public static final EnchantmentCategory MACHETES = EnchantmentCategory.create("machetes", e -> e instanceof MacheteItem);

    public static final EnchantmentCategory DOUBLE_WIELD = EnchantmentCategory.create("double_wield", e -> e instanceof DoubleWieldItem);

    public static final EnchantmentCategory CLAW = EnchantmentCategory.create("claws", e -> e instanceof ClawItem);

    public static final RegistryEntry<EnderHandEnchantment> ENDER_HAND = reg("ender_hand", THROWABLE, EnderHandEnchantment::new, "Thrown attacks will appear as direct hit.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<ProjectionEnchantment> PROJECTION = reg("projection", THROWABLE, ProjectionEnchantment::new, "Thrown attacks will not consume the used weapon").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<SingleLevelEnchantment> INSTANT_THROWING = reg("instant_shot", THROWABLE, SingleLevelEnchantment::new, "Throw the weapon out immediately on right click when not sneaking").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<StealthEnchantment> NO_AGGRO = reg("stealth", DAGGER, StealthEnchantment::new, "Dagger damage has a chance to not aggravate enemy, but reduce damage.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<HeavyEnchantment> HEAVY = reg("heavy", HEAVY_WEAPON, HeavyEnchantment::new, "Reduce attack speed, increase critical hit and projectile damage. Works on Axe and heavy weapons.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<HardShieldEnchantment> HARD_SHIELD = reg("hard_shield", SHIELDS, HardShieldEnchantment::new, "Increase shield defense. Works for both hands.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }).defaultLang().register();

    public static final RegistryEntry<HeavyShieldEnchantment> HEAVY_SHIELD = reg("heavy_shield", SHIELDS, HeavyShieldEnchantment::new, "Reduce movement speed, increase shield defense by a lot in main hand.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<EnergizedWillEnchantment> ENERGIZED_WILL = reg("energized_will", MACHETES, EnergizedWillEnchantment::new, "Gradually increase machete attack range when stacking consecutive attacks. Conflicts with Raised Spirit.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<RaisedSpiritEnchantment> RAISED_SPIRIT = reg("raised_spirit", MACHETES, RaisedSpiritEnchantment::new, "Gradually increase machete attack speed when stacking consecutive attacks. Conflicts with Energized Will.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<SingleLevelEnchantment> GHOST_SLASH = reg("ghost_slash", DOUBLE_WIELD, SingleLevelEnchantment::new, "Empty hits will stack hit count and consume durability as well.").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).defaultLang().register();

    public static final RegistryEntry<ClawBlockEnchantment> CLAW_BLOCK = reg("claw_shielding", CLAW, ClawBlockEnchantment::new, "Increase damage blocking time for claws. Works on either hand").rarity(Enchantment.Rarity.RARE).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }).defaultLang().register();

    private static <T extends Enchantment> EnchantmentBuilder<T, L2Registrate> reg(String id, EnchantmentCategory category, EnchantmentFactory<T> fac, String desc) {
        return L2Weaponry.REGISTRATE.enchantment(id, category, fac, desc);
    }

    public static void register() {
    }
}