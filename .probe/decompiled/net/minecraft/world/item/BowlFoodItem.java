package net.minecraft.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BowlFoodItem extends Item {

    public BowlFoodItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        ItemStack $$3 = super.finishUsingItem(itemStack0, level1, livingEntity2);
        return livingEntity2 instanceof Player && ((Player) livingEntity2).getAbilities().instabuild ? $$3 : new ItemStack(Items.BOWL);
    }
}