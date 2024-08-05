package dev.xkmc.l2complements.init.registrate;

import com.tterrag.registrate.builders.EnchantmentBuilder;
import com.tterrag.registrate.builders.EnchantmentBuilder.EnchantmentFactory;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.content.enchantment.armors.DurableArmorEnchantment;
import dev.xkmc.l2complements.content.enchantment.armors.FlameThornEnchantment;
import dev.xkmc.l2complements.content.enchantment.armors.IceThornEnchantment;
import dev.xkmc.l2complements.content.enchantment.armors.StableBodyEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.BannableEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.DiggerAndSwordEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.ImmuneEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.SingleLevelEnchantment;
import dev.xkmc.l2complements.content.enchantment.digging.CubicBlockBreaker;
import dev.xkmc.l2complements.content.enchantment.digging.CubicChunkBreaker;
import dev.xkmc.l2complements.content.enchantment.digging.DrillBlockBreaker;
import dev.xkmc.l2complements.content.enchantment.digging.OreDigger;
import dev.xkmc.l2complements.content.enchantment.digging.PlaneBlockBreaker;
import dev.xkmc.l2complements.content.enchantment.digging.PlaneChunkBreaker;
import dev.xkmc.l2complements.content.enchantment.digging.RangeDiggingEnchantment;
import dev.xkmc.l2complements.content.enchantment.digging.TreeDigger;
import dev.xkmc.l2complements.content.enchantment.special.LegendaryEnchantment;
import dev.xkmc.l2complements.content.enchantment.special.LifeMendingEnchantment;
import dev.xkmc.l2complements.content.enchantment.special.LifeSyncEnchantment;
import dev.xkmc.l2complements.content.enchantment.special.SoulBindingEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.CurseBladeEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.IceBladeEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.SharpBladeEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.SoulFlameBladeEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.VoidTouchEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.WindSweepEnchantment;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LCEnchantments {

    public static final EnchantmentCategory ALL = EnchantmentCategory.create("ALL", e -> e.getMaxStackSize() == 1);

    public static final RegistryEntry<BannableEnchantment> ENCH_PROJECTILE = regImmune("projectile_reject", "Projectile Reject", "Deflects all projectiles. Make wearer immune to projectile damage.");

    public static final RegistryEntry<BannableEnchantment> ENCH_FIRE = regImmune("fire_reject", "Fire Immune", "Make wearer immune to fire damage.");

    public static final RegistryEntry<BannableEnchantment> ENCH_ENVIRONMENT = regImmune("environment_reject", "Environmental Damage Immune", "Make wearer immune to damage without attacker.");

    public static final RegistryEntry<BannableEnchantment> ENCH_EXPLOSION = regImmune("explosion_reject", "Explosion Immune", "Make wearer immune to explosion damage.");

    public static final RegistryEntry<BannableEnchantment> ENCH_MAGIC = regImmune("magic_reject", "Magic Immune", "Make wearer immune to magic damage.");

    public static final RegistryEntry<BannableEnchantment> ENCH_INVINCIBLE = regImmune("invincible", "Invincible (Creative)", "Player is invincible to all damage.");

    public static final RegistryEntry<ImmuneEnchantment> ENCH_MATES = reg("owner_protection", EnchantmentCategory.ARMOR, ImmuneEnchantment::new, "Negate all damages from entities owned by you.").addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).lang("Owner Protection").register();

    public static final RegistryEntry<SingleLevelEnchantment> SHULKER_ARMOR = reg("shulker_armor", ALL, SingleLevelEnchantment::new, "Armor invisible to mobs and players when wearer has invisibility effect.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).lang("Transparent").register();

    public static final RegistryEntry<SingleLevelEnchantment> ENDER_MASK = reg("ender_mask", EnchantmentCategory.ARMOR_HEAD, SingleLevelEnchantment::new, "Endermen won't be mad at you for direct eye contact").addSlots(new EquipmentSlot[] { EquipmentSlot.HEAD }).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<SingleLevelEnchantment> SHINNY = reg("shinny", EnchantmentCategory.WEARABLE, SingleLevelEnchantment::new, "Piglins loves it.").addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<SingleLevelEnchantment> SNOW_WALKER = reg("snow_walker", EnchantmentCategory.ARMOR_FEET, SingleLevelEnchantment::new, "Allow Wearer to walk on powdered snow.").addSlots(new EquipmentSlot[] { EquipmentSlot.FEET }).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<SingleLevelEnchantment> DAMPENED = reg("dampened", EnchantmentCategory.WEARABLE, SingleLevelEnchantment::new, "When wearing 4 pieces of armors with dampened effect, cancel all vibrations emitted by wearer.").addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<SingleLevelEnchantment> SAFEGUARD = reg("safeguard", EnchantmentCategory.BREAKABLE, SingleLevelEnchantment::new, "when item has more than 1 durability, it will keep at least 1 durability if damaged").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<DiggerAndSwordEnchantment> ENDER = reg("ender_reach", EnchantmentCategory.DIGGER, DiggerAndSwordEnchantment::new, "Teleport mined items and mob drops to inventory if possible.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<DiggerAndSwordEnchantment> SMELT = reg("smelt", EnchantmentCategory.DIGGER, DiggerAndSwordEnchantment::new, "Smelt mined items and mob drops if possible, including items in chests!").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<StableBodyEnchantment> STABLE_BODY = reg("stable_body", EnchantmentCategory.ARMOR_CHEST, StableBodyEnchantment::new, "Player won't be knocked back when wearing chestplate with this enchantment.").addSlots(new EquipmentSlot[] { EquipmentSlot.CHEST }).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<LifeSyncEnchantment> LIFE_SYNC = reg("life_sync", EnchantmentCategory.BREAKABLE, LifeSyncEnchantment::new, "Cost health instead of durability when possible. May kill the user").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<LifeMendingEnchantment> LIFE_MENDING = reg("life_mending", EnchantmentCategory.BREAKABLE, LifeMendingEnchantment::new, "When healing, cost heal amount to repair item first.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<LegendaryEnchantment> ETERNAL = reg("eternal", EnchantmentCategory.BREAKABLE, LegendaryEnchantment::new, "Item will ignore all durability damage.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).lang("Eternal (Creative)").register();

    public static final RegistryEntry<LegendaryEnchantment> HARDENED = reg("hardened", EnchantmentCategory.BREAKABLE, LegendaryEnchantment::new, "Durability loss will be capped to 1.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<WindSweepEnchantment> WIND_SWEEP = reg("wind_sweep", EnchantmentCategory.WEAPON, WindSweepEnchantment::new, "Increase sweeping hit box").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<SoulBindingEnchantment> SOUL_BOUND = reg("soul_bound", ALL, SoulBindingEnchantment::new, "Remain in inventory after death.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).defaultLang().register();

    public static final RegistryEntry<IceBladeEnchantment> ICE_BLADE = reg("ice_blade", EnchantmentCategory.WEAPON, IceBladeEnchantment::new, "Apply freezing effect to target. Higher levels have longer duration.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<SoulFlameBladeEnchantment> FLAME_BLADE = reg("soul_flame_blade", EnchantmentCategory.WEAPON, SoulFlameBladeEnchantment::new, "Apply flame effect to target. Higher levels have higher damage.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<CurseBladeEnchantment> CURSE_BLADE = reg("cursed_blade", EnchantmentCategory.WEAPON, CurseBladeEnchantment::new, "Apply cursed effect to target. Higher levels have longer duration.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<SharpBladeEnchantment> SHARP_BLADE = reg("sharp_blade", EnchantmentCategory.WEAPON, SharpBladeEnchantment::new, "Stack bleeding effect to target. Higher levels have higher stack cap.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<IceThornEnchantment> ICE_THORN = reg("ice_thorn", EnchantmentCategory.ARMOR, IceThornEnchantment::new, "Apply freezing effect to attacker. Higher levels have longer duration.").addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<FlameThornEnchantment> FLAME_THORN = reg("soul_flame_thorn", EnchantmentCategory.ARMOR, FlameThornEnchantment::new, "Apply flame effect to attacker. Higher levels have higher damage.").addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<DurableArmorEnchantment> DURABLE_ARMOR = reg("durable_armor", EnchantmentCategory.ARMOR, DurableArmorEnchantment::new, "Armor will have higher durability. Conflict with Unbreaking.").addSlots(EquipmentSlot.values()).rarity(Enchantment.Rarity.VERY_RARE).lang("Durable Armor").register();

    public static final RegistryEntry<VoidTouchEnchantment> VOID_TOUCH = reg("void_touch", EnchantmentCategory.WEAPON, VoidTouchEnchantment::new, "Have a small chance to deal true damage. Chance increase significantly if the damage bypasses armor or magic already.").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).register();

    public static final RegistryEntry<RangeDiggingEnchantment> CUBIC = reg("cubic_digging", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new CubicBlockBreaker(1), r, c, s), "Dig %1$sx%1$sx%1$s blocks at once").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Cubic Digging").register();

    public static final RegistryEntry<RangeDiggingEnchantment> PLANE = reg("plane_digging", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new PlaneBlockBreaker(2), r, c, s), "Dig %1$sx%1$s blocks at once").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Plane Digging").register();

    public static final RegistryEntry<RangeDiggingEnchantment> DRILL = reg("drill_digging", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new DrillBlockBreaker(7), r, c, s), "Dig %s blocks at once").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Drill Digging").register();

    public static final RegistryEntry<RangeDiggingEnchantment> VIEN = reg("vien_mining", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new OreDigger(7, 8), r, c, s), "Dig connected blocks of the same type, up to %s blocks").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Vien Mining").register();

    public static final RegistryEntry<RangeDiggingEnchantment> TREE = reg("tree_chopping", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new TreeDigger(), r, c, s), "Chop logs and adjacent leaves").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Tree Chopper").register();

    public static final RegistryEntry<RangeDiggingEnchantment> CHUNK_CUBIC = reg("cubic_eater", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new CubicChunkBreaker(2), r, c, s), "Dig %1$sx%1$sx%1$s chunk-aligned blocks at once").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Cubic Chunk Eater").register();

    public static final RegistryEntry<RangeDiggingEnchantment> CHUNK_PLANE = reg("plane_eater", EnchantmentCategory.DIGGER, (r, c, s) -> new RangeDiggingEnchantment(new PlaneChunkBreaker(1), r, c, s), "Dig 16x16 chunk-aligned blocks at once for %s layers").addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND }).rarity(Enchantment.Rarity.VERY_RARE).lang("Planar Chunk Eater").register();

    private static RegistryEntry<BannableEnchantment> regImmune(String id, String name, String desc) {
        return reg(id, EnchantmentCategory.ARMOR, BannableEnchantment::new, desc).addArmorSlots().rarity(Enchantment.Rarity.VERY_RARE).lang(name).register();
    }

    private static <T extends Enchantment> EnchantmentBuilder<T, L2Registrate> reg(String id, EnchantmentCategory category, EnchantmentFactory<T> fac, String desc) {
        return L2Complements.REGISTRATE.enchantment(id, category, fac, desc);
    }

    public static void register() {
    }
}