package dev.xkmc.l2weaponry.init.materials;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.contents.materials.api.ITool;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.RawToolFactory;
import dev.xkmc.l2weaponry.content.item.types.BattleAxeItem;
import dev.xkmc.l2weaponry.content.item.types.ClawItem;
import dev.xkmc.l2weaponry.content.item.types.DaggerItem;
import dev.xkmc.l2weaponry.content.item.types.HammerItem;
import dev.xkmc.l2weaponry.content.item.types.JavelinItem;
import dev.xkmc.l2weaponry.content.item.types.MacheteItem;
import dev.xkmc.l2weaponry.content.item.types.NunchakuItem;
import dev.xkmc.l2weaponry.content.item.types.PlateShieldItem;
import dev.xkmc.l2weaponry.content.item.types.RoundShieldItem;
import dev.xkmc.l2weaponry.content.item.types.SpearItem;
import dev.xkmc.l2weaponry.content.item.types.ThrowingAxeItem;
import dev.xkmc.l2weaponry.init.data.TagGen;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;

public enum LWToolTypes implements ITool {

    CLAW(TagGen.CLAW, ClawItem::new, 0.7F, 3.0F, "claw_base", () -> new EnchantmentInstance(Enchantments.SWEEPING_EDGE, 2)),
    DAGGER(TagGen.DAGGER, DaggerItem::new, 0.7F, 4.0F),
    MACHETE(TagGen.MACHETE, MacheteItem::new, 1.2F, 1.0F, () -> new EnchantmentInstance(Enchantments.SWEEPING_EDGE, 1)),
    THROWING_AXE(TagGen.THROWING_AXE, ThrowingAxeItem::new, 1.4F, 1.0F),
    HAMMER(TagGen.HAMMER, HammerItem::new, 2.0F, 0.7F, () -> new EnchantmentInstance((Enchantment) LCEnchantments.CUBIC.get(), 1)),
    BATTLE_AXE(TagGen.BATTLE_AXE, BattleAxeItem::new, 2.0F, 0.7F, "battle_axe", () -> new EnchantmentInstance((Enchantment) LCEnchantments.TREE.get(), 1)),
    SPEAR(TagGen.SPEAR, SpearItem::new, 1.0F, 1.0F, "long_weapon", () -> new EnchantmentInstance((Enchantment) LCEnchantments.PLANE.get(), 1)),
    JAVELIN(TagGen.JAVELIN, JavelinItem::new, 1.0F, 1.2F, "long_weapon", () -> new EnchantmentInstance((Enchantment) LCEnchantments.DRILL.get(), 1)),
    ROUND_SHIELD(TagGen.ROUND_SHIELD, RoundShieldItem::new, 5.0F, 0.5F),
    PLATE_SHIELD(TagGen.PLATE_SHIELD, PlateShieldItem::new, 20.0F, 0.125F),
    NUNCHAKU(TagGen.NUNCHAKU, NunchakuItem::new, 0.5F, 4.0F);

    public final TagKey<Item> tag;

    private final RawToolFactory fac;

    private final float damage;

    private final float speed;

    private final String customModel;

    private final List<Supplier<EnchantmentInstance>> enchs;

    @SafeVarargs
    private LWToolTypes(TagKey<Item> tag, RawToolFactory fac, float damage, float speed, @Nullable String customModel, Supplier<EnchantmentInstance>... enchs) {
        this.tag = tag;
        this.fac = fac;
        this.damage = damage;
        this.speed = speed;
        this.customModel = customModel;
        this.enchs = List.of(enchs);
    }

    @SafeVarargs
    private LWToolTypes(TagKey<Item> tag, RawToolFactory fac, float damage, float speed, Supplier<EnchantmentInstance>... enchs) {
        this(tag, fac, damage, speed, null, enchs);
    }

    public int getDamage(int base_damage) {
        return Math.round((float) base_damage * this.damage);
    }

    public float getSpeed(float base_speed) {
        return (float) Math.round(base_speed * this.speed * 10.0F) * 0.1F;
    }

    public Item create(Tier tier, int i, float v, Item.Properties properties, ExtraToolConfig extraToolConfig) {
        return this.fac.get(tier, i, v, properties, extraToolConfig);
    }

    @Nullable
    public String customModel() {
        return this.customModel;
    }

    public <T extends Item> LegendaryTool<T> legendary(LegendaryToolFactory<T> fac) {
        return new LegendaryTool<>(this, fac);
    }

    public List<EnchantmentInstance> getEnchs() {
        return this.enchs.stream().map(Supplier::get).toList();
    }
}