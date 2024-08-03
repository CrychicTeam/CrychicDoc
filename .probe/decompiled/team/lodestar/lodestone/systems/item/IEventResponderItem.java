package team.lodestar.lodestone.systems.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface IEventResponderItem {

    default void takeDamageEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity attacked, ItemStack stack) {
        this.takeDamageEvent(attacker, attacked, stack);
    }

    default void takeDamageEvent(LivingEntity attacker, LivingEntity attacked, ItemStack stack) {
    }

    default void hurtEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        this.hurtEvent(attacker, target, stack);
    }

    default void hurtEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }

    default void killEvent(LivingDeathEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        this.killEvent(attacker, target, stack);
    }

    default void killEvent(LivingEntity attacker, LivingEntity target, ItemStack stack) {
    }
}