package dev.xkmc.l2complements.content.item.misc;

import dev.xkmc.l2damagetracker.contents.curios.L2Totem;
import java.util.function.Consumer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ILCTotem extends L2Totem {

    default void trigger(LivingEntity self, ItemStack holded, Consumer<ItemStack> second) {
        super.trigger(self, holded, second);
    }

    default boolean allow(LivingEntity self, DamageSource pDamageSource) {
        return super.allow(self, pDamageSource);
    }

    default void onClientTrigger(Entity entity, ItemStack item) {
        super.onClientTrigger(entity, item);
    }
}