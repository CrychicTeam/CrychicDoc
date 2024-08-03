package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EffectRepair extends MobEffect implements INoCreeperLingering {

    public EffectRepair() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player && entityLivingBaseIn.m_9236_().getGameTime() % 5L == 0L) {
            for (ItemStack stack : this.getRepairables(((Player) entityLivingBaseIn).getInventory())) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private List<ItemStack> getRepairables(Container inventory) {
        ArrayList<ItemStack> stacks = new ArrayList();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.isRepairable() && stack.isDamaged()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }
}