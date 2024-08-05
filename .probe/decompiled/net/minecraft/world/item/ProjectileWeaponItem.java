package net.minecraft.world.item;

import java.util.function.Predicate;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public abstract class ProjectileWeaponItem extends Item {

    public static final Predicate<ItemStack> ARROW_ONLY = p_43017_ -> p_43017_.is(ItemTags.ARROWS);

    public static final Predicate<ItemStack> ARROW_OR_FIREWORK = ARROW_ONLY.or(p_43015_ -> p_43015_.is(Items.FIREWORK_ROCKET));

    public ProjectileWeaponItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }

    public abstract Predicate<ItemStack> getAllSupportedProjectiles();

    public static ItemStack getHeldProjectile(LivingEntity livingEntity0, Predicate<ItemStack> predicateItemStack1) {
        if (predicateItemStack1.test(livingEntity0.getItemInHand(InteractionHand.OFF_HAND))) {
            return livingEntity0.getItemInHand(InteractionHand.OFF_HAND);
        } else {
            return predicateItemStack1.test(livingEntity0.getItemInHand(InteractionHand.MAIN_HAND)) ? livingEntity0.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    public abstract int getDefaultProjectileRange();
}