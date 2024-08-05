package io.github.lightman314.lightmanscurrency.common.enchantments;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.common.core.ModEnchantments;
import io.github.lightman314.lightmanscurrency.integration.curios.LCCurios;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class MoneyMendingEnchantment extends Enchantment {

    public MoneyMendingEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BREAKABLE, slots);
    }

    @Override
    public int getMinCost(int level) {
        return level * 25;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(@Nonnull Enchantment otherEnchant) {
        return otherEnchant != Enchantments.MENDING && super.checkCompatibility(otherEnchant);
    }

    public static MoneyValue getRepairCost(@Nonnull ItemStack item) {
        MoneyValue baseCost = LCConfig.SERVER.moneyMendingRepairCost.get();
        MoneyValue cost = baseCost;
        Map<Enchantment, Integer> enchantments = item.getAllEnchantments();
        if ((Integer) enchantments.getOrDefault(Enchantments.INFINITY_ARROWS, 0) > 0) {
            cost = baseCost.addValue(LCConfig.SERVER.moneyMendingInfinityCost.get());
        }
        return cost == null ? baseCost : cost;
    }

    public static void runEntityTick(@Nonnull LivingEntity entity, @Nonnull IMoneyHandler handler) {
        Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(ModEnchantments.MONEY_MENDING.get(), entity, ItemStack::m_41768_);
        ItemStack item;
        if (entry == null) {
            if (LightmansCurrency.isCuriosLoaded()) {
                item = LCCurios.getMoneyMendingItem(entity);
            } else {
                item = null;
            }
        } else {
            item = (ItemStack) entry.getValue();
        }
        if (item != null) {
            MoneyValue repairCost = getRepairCost(item);
            MoneyView availableFunds = handler.getStoredMoney();
            if (!availableFunds.containsValue(repairCost)) {
                return;
            }
            MoneyValue nextCost = repairCost;
            MoneyValue finalCost = MoneyValue.empty();
            int currentDamage = item.getDamageValue();
            int repairAmount;
            for (repairAmount = 0; availableFunds.containsValue(nextCost) && repairAmount < currentDamage; nextCost = nextCost.addValue(repairCost)) {
                repairAmount++;
                finalCost = nextCost;
            }
            if (handler.extractMoney(finalCost, true).isEmpty()) {
                handler.extractMoney(finalCost, false);
                item.setDamageValue(currentDamage - repairAmount);
            }
        }
    }
}