package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public interface OnHitFeature extends BowArrowFeature {

    default void onHitEntity(GenericArrowEntity genericArrow, Entity target, EntityHitResult hit) {
        if (target instanceof LivingEntity living) {
            this.onHitLivingEntity(genericArrow, living, hit);
        }
    }

    default void onHitLivingEntity(GenericArrowEntity genericArrow, LivingEntity target, EntityHitResult hit) {
    }

    default void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {
    }

    default void postHurtEntity(GenericArrowEntity genericArrow, LivingEntity target) {
    }

    default void onHurtEntity(GenericArrowEntity genericArrow, CreateSourceEvent source) {
    }

    default void onHurtModifier(GenericArrowEntity genericArrow, AttackCache cache) {
    }
}