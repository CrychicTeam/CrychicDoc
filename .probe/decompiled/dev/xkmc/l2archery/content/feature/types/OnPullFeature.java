package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.entity.LivingEntity;

public interface OnPullFeature extends BowArrowFeature {

    default void onPull(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
    }

    default void tickAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
    }

    default void stopAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
    }
}