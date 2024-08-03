package com.mna.items.base;

import com.mna.api.items.ManaBatteryItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableObject;

public interface IManaRepairable {

    float DEFAULT_MANACOST = 0.25F;

    float DEFAULT_REPAIR = 0.01F;

    default float manaPerRepairTick() {
        return 0.25F;
    }

    default float repairPerTick() {
        return 0.01F;
    }

    default void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player) {
            tickRepair(stack, (Player) entityIn, this.manaPerRepairTick(), this.repairPerTick(), itemSlot);
        }
    }

    static void tickRepair(ItemStack stack, Player player, float manaPerRepairTick, float repairPerTick, int itemSlot) {
        if (stack.isDamaged()) {
            MutableObject<Boolean> consumedMana = new MutableObject(false);
            Optional<ItemStack> manaCrystal = player.getInventory().items.stream().filter(i -> i.getItem() instanceof ManaBatteryItem && ((ManaBatteryItem) i.getItem()).getMana(i) > 0.0F).findFirst();
            if (manaCrystal.isPresent() && ((ManaBatteryItem) ((ItemStack) manaCrystal.get()).getItem()).consumeMana((ItemStack) manaCrystal.get(), manaPerRepairTick, player)) {
                consumedMana.setValue(true);
            }
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (!(Boolean) consumedMana.getValue() && m.getCastingResource().hasEnoughAbsolute(player, manaPerRepairTick)) {
                    m.getCastingResource().consume(player, manaPerRepairTick);
                    consumedMana.setValue(true);
                }
                if ((Boolean) consumedMana.getValue()) {
                    int repairAmount = m.bankArmorRepair(itemSlot, repairPerTick);
                    if (repairAmount > 0) {
                        stack.hurtAndBreak(-repairAmount, player, p -> {
                        });
                    }
                }
            });
        }
    }
}