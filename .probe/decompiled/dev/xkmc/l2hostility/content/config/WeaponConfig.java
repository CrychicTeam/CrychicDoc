package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

@SerialClass
public class WeaponConfig extends BaseConfig {

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<WeaponConfig.ItemConfig> melee_weapons = new ArrayList();

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<WeaponConfig.ItemConfig> armors = new ArrayList();

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<WeaponConfig.ItemConfig> ranged_weapons = new ArrayList();

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<WeaponConfig.EnchConfig> weapon_enchantments = new ArrayList();

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<WeaponConfig.EnchConfig> armor_enchantments = new ArrayList();

    public static ItemStack getRandomMeleeWeapon(int level, RandomSource r) {
        WeaponConfig config = L2Hostility.WEAPON.getMerged();
        return getRandomWeapon(config.melee_weapons, level, r);
    }

    public static ItemStack getRandomArmor(EquipmentSlot slot, int level, RandomSource r) {
        WeaponConfig config = L2Hostility.WEAPON.getMerged();
        return getRandomArmors(slot, config.armors, level, r);
    }

    public static ItemStack getRandomRangedWeapon(int level, RandomSource r) {
        WeaponConfig config = L2Hostility.WEAPON.getMerged();
        return getRandomWeapon(config.ranged_weapons, level, r);
    }

    private static ItemStack getRandomWeapon(ArrayList<WeaponConfig.ItemConfig> entries, int level, RandomSource r) {
        int total = 0;
        List<WeaponConfig.ItemConfig> list = new ArrayList();
        for (WeaponConfig.ItemConfig e : entries) {
            if (e.level <= level) {
                list.add(e);
                total += e.weight();
            }
        }
        if (total == 0) {
            return ItemStack.EMPTY;
        } else {
            int val = r.nextInt(total);
            for (WeaponConfig.ItemConfig ex : list) {
                val -= ex.weight();
                if (val <= 0) {
                    return ((ItemStack) ex.stack.get(r.nextInt(ex.stack.size()))).copy();
                }
            }
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack getRandomArmors(EquipmentSlot slot, ArrayList<WeaponConfig.ItemConfig> entries, int level, RandomSource r) {
        int total = 0;
        List<WeaponConfig.ItemConfig> list = new ArrayList();
        for (WeaponConfig.ItemConfig e : entries) {
            if (e.level <= level) {
                ArrayList<ItemStack> sub = new ArrayList();
                for (ItemStack item : e.stack) {
                    if (item.isEmpty() || item.getItem() instanceof ArmorItem eq && eq.getEquipmentSlot() == slot || item.getEquipmentSlot() == slot) {
                        sub.add(item);
                    }
                }
                if (!sub.isEmpty()) {
                    list.add(new WeaponConfig.ItemConfig(sub, e.level, e.weight));
                    total += e.weight();
                }
            }
        }
        if (total == 0) {
            return ItemStack.EMPTY;
        } else {
            int val = r.nextInt(total);
            for (WeaponConfig.ItemConfig ex : list) {
                val -= ex.weight();
                if (val <= 0) {
                    return ((ItemStack) ex.stack.get(r.nextInt(ex.stack.size()))).copy();
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public WeaponConfig putMeleeWeapon(int level, int weight, Item... items) {
        ArrayList<ItemStack> list = new ArrayList();
        for (Item e : items) {
            list.add(e.getDefaultInstance());
        }
        this.melee_weapons.add(new WeaponConfig.ItemConfig(list, level, weight));
        return this;
    }

    public WeaponConfig putArmor(int level, int weight, Item... items) {
        ArrayList<ItemStack> list = new ArrayList();
        for (Item e : items) {
            list.add(e.getDefaultInstance());
        }
        this.armors.add(new WeaponConfig.ItemConfig(list, level, weight));
        return this;
    }

    public WeaponConfig putRangedWeapon(int level, int weight, Item... items) {
        ArrayList<ItemStack> list = new ArrayList();
        for (Item e : items) {
            list.add(e.getDefaultInstance());
        }
        this.ranged_weapons.add(new WeaponConfig.ItemConfig(list, level, weight));
        return this;
    }

    public WeaponConfig putWeaponEnch(int level, float chance, Enchantment... items) {
        ArrayList<Enchantment> list = new ArrayList(Arrays.asList(items));
        this.weapon_enchantments.add(new WeaponConfig.EnchConfig(list, level, chance));
        return this;
    }

    public WeaponConfig putArmorEnch(int level, float chance, Enchantment... items) {
        ArrayList<Enchantment> list = new ArrayList(Arrays.asList(items));
        this.armor_enchantments.add(new WeaponConfig.EnchConfig(list, level, chance));
        return this;
    }

    public static record EnchConfig(ArrayList<Enchantment> enchantments, int level, float chance) {
    }

    public static record ItemConfig(ArrayList<ItemStack> stack, int level, int weight) {
    }
}