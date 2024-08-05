package com.nameless.indestructible.utils;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class ExtraPredicate {

    public static class Phase<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final int minLevel;

        private final int maxLevel;

        public Phase(int minLevel, int maxLevel) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        @Override
        public boolean test(T mobpatch) {
            if (!(mobpatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch)) {
                return false;
            } else {
                int phase = advancedCustomHumanoidMobPatch.getPhase();
                return this.minLevel <= phase && phase <= this.maxLevel;
            }
        }
    }

    public static class SelfStamina<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final float value;

        private final CombatBehaviors.Health.Comparator comparator;

        public SelfStamina(float value, CombatBehaviors.Health.Comparator comparator) {
            this.value = value;
            this.comparator = comparator;
        }

        @Override
        public boolean test(T mobpatch) {
            if (!(mobpatch instanceof AdvancedCustomHumanoidMobPatch)) {
                return false;
            } else {
                float stamina = ((AdvancedCustomHumanoidMobPatch) mobpatch).getStamina();
                float maxstamina = ((AdvancedCustomHumanoidMobPatch) mobpatch).getMaxStamina();
                return switch(this.comparator) {
                    case LESS_ABSOLUTE ->
                        this.value > stamina;
                    case GREATER_ABSOLUTE ->
                        this.value < stamina;
                    case LESS_RATIO ->
                        this.value > stamina / maxstamina;
                    case GREATER_RATIO ->
                        this.value < stamina / maxstamina;
                };
            }
        }
    }

    public static class TargetIsGuardBreak<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final boolean invert;

        public TargetIsGuardBreak(boolean invert) {
            this.invert = invert;
        }

        @Override
        public boolean test(T mobpatch) {
            LivingEntityPatch<?> tartgetpatch = EpicFightCapabilities.getEntityPatch(mobpatch.getTarget(), LivingEntityPatch.class);
            if (tartgetpatch == null) {
                return false;
            } else {
                boolean targetisguardbreak = tartgetpatch.<Animator>getAnimator().getPlayerFor(null).getAnimation() == Animations.BIPED_COMMON_NEUTRALIZED || tartgetpatch.<Animator>getAnimator().getPlayerFor(null).getAnimation() == Animations.GREATSWORD_GUARD_BREAK;
                return !this.invert ? targetisguardbreak : !targetisguardbreak;
            }
        }
    }

    public static class TargetIsKnockDown<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final boolean invert;

        public TargetIsKnockDown(boolean invert) {
            this.invert = invert;
        }

        @Override
        public boolean test(T mobpatch) {
            LivingEntityPatch<?> tartgetpatch = EpicFightCapabilities.getEntityPatch(mobpatch.getTarget(), LivingEntityPatch.class);
            if (tartgetpatch == null) {
                return false;
            } else {
                boolean targetisknockdown = tartgetpatch.getEntityState().knockDown();
                return !this.invert ? targetisknockdown : !targetisknockdown;
            }
        }
    }

    public static class TargetIsUsingItem<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final boolean isEdible;

        public TargetIsUsingItem(boolean isEdible) {
            this.isEdible = isEdible;
        }

        @Override
        public boolean test(T mobpatch) {
            LivingEntity target = mobpatch.getTarget();
            if (!target.isUsingItem()) {
                return false;
            } else {
                ItemStack item = target.getUseItem();
                return this.isEdible ? item.getItem() instanceof PotionItem || item.getItem().isEdible() : !(item.getItem() instanceof PotionItem) && !item.getItem().isEdible();
            }
        }
    }

    public static class TargetWithinState<T extends MobPatch<?>> extends CombatBehaviors.BehaviorPredicate<T> {

        private final int minLevel;

        private final int maxLevel;

        public TargetWithinState(int minLevel, int maxLevel) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        @Override
        public boolean test(T mobpatch) {
            LivingEntityPatch<?> tartgetpatch = EpicFightCapabilities.getEntityPatch(mobpatch.getTarget(), LivingEntityPatch.class);
            if (tartgetpatch == null) {
                return false;
            } else {
                int level = tartgetpatch.getEntityState().getLevel();
                return this.minLevel <= level && level <= this.maxLevel;
            }
        }
    }
}