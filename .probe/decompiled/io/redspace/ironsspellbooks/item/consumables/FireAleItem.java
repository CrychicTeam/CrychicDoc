package io.redspace.ironsspellbooks.item.consumables;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FireAleItem extends DrinkableItem {

    boolean foilOverride;

    public FireAleItem(Item.Properties pProperties) {
        super(pProperties, (itemstack, livingentity) -> {
            livingentity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 3, false, true, true));
            livingentity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 0, false, true, true));
            livingentity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 900, 2, false, true, true));
        }, Items.GLASS_BOTTLE, false);
    }

    public int getMaxStackSize(ItemStack stack) {
        return Items.POTION.getMaxStackSize();
    }
}