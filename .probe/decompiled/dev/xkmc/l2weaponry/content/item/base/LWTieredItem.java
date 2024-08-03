package dev.xkmc.l2weaponry.content.item.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.materials.generic.GenericTieredItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface LWTieredItem extends GenericTieredItem {

    default float getMultiplier(AttackCache event) {
        return 1.0F;
    }

    default void modifySource(LivingEntity attacker, CreateSourceEvent event, ItemStack item, @Nullable Entity target) {
    }

    default boolean isSharp() {
        return false;
    }

    default boolean isHeavy() {
        return false;
    }
}