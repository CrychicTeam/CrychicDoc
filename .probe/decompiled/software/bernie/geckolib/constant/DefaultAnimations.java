package software.bernie.geckolib.constant;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public final class DefaultAnimations {

    public static final RawAnimation ITEM_ON_USE = RawAnimation.begin().thenPlay("item.use");

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");

    public static final RawAnimation LIVING = RawAnimation.begin().thenLoop("misc.living");

    public static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("misc.spawn");

    public static final RawAnimation DIE = RawAnimation.begin().thenPlay("misc.die");

    public static final RawAnimation INTERACT = RawAnimation.begin().thenPlay("misc.interact");

    public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlay("misc.deploy");

    public static final RawAnimation REST = RawAnimation.begin().thenPlay("misc.rest");

    public static final RawAnimation SIT = RawAnimation.begin().thenPlayAndHold("misc.sit");

    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");

    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("move.swim");

    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("move.run");

    public static final RawAnimation DRIVE = RawAnimation.begin().thenLoop("move.drive");

    public static final RawAnimation FLY = RawAnimation.begin().thenLoop("move.fly");

    public static final RawAnimation CRAWL = RawAnimation.begin().thenLoop("move.crawl");

    public static final RawAnimation JUMP = RawAnimation.begin().thenPlay("move.jump");

    public static final RawAnimation SNEAK = RawAnimation.begin().thenLoop("move.sneak");

    public static final RawAnimation ATTACK_CAST = RawAnimation.begin().thenPlay("attack.cast");

    public static final RawAnimation ATTACK_SWING = RawAnimation.begin().thenPlay("attack.swing");

    public static final RawAnimation ATTACK_THROW = RawAnimation.begin().thenPlay("attack.throw");

    public static final RawAnimation ATTACK_BITE = RawAnimation.begin().thenPlay("attack.bite");

    public static final RawAnimation ATTACK_SLAM = RawAnimation.begin().thenPlay("attack.slam");

    public static final RawAnimation ATTACK_STOMP = RawAnimation.begin().thenPlay("attack.stomp");

    public static final RawAnimation ATTACK_STRIKE = RawAnimation.begin().thenPlay("attack.strike");

    public static final RawAnimation ATTACK_FLYING_ATTACK = RawAnimation.begin().thenPlay("attack.flying_attack");

    public static final RawAnimation ATTACK_SHOOT = RawAnimation.begin().thenPlay("attack.shoot");

    public static final RawAnimation ATTACK_BLOCK = RawAnimation.begin().thenPlay("attack.block");

    public static final RawAnimation ATTACK_CHARGE = RawAnimation.begin().thenPlay("attack.charge");

    public static final RawAnimation ATTACK_CHARGE_END = RawAnimation.begin().thenPlay("attack.charge_end");

    public static final RawAnimation ATTACK_POWERUP = RawAnimation.begin().thenPlay("attack.powerup");

    public static <T extends GeoAnimatable> AnimationController<T> basicPredicateController(T animatable, RawAnimation optionA, RawAnimation optionB, BiFunction<T, AnimationState<T>, Boolean> predicate) {
        return new AnimationController<>(animatable, "Generic", 10, state -> {
            Boolean result = (Boolean) predicate.apply(animatable, state);
            return result == null ? PlayState.STOP : state.setAndContinue(result ? optionA : optionB);
        });
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericLivingController(T animatable) {
        return new AnimationController<>(animatable, "Living", 10, state -> state.setAndContinue(LIVING));
    }

    public static <T extends LivingEntity & GeoAnimatable> AnimationController<T> genericDeathController(T animatable) {
        return new AnimationController<>(animatable, "Death", 0, state -> state.getAnimatable().isDeadOrDying() ? state.setAndContinue(DIE) : PlayState.STOP);
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericIdleController(T animatable) {
        return new AnimationController<>(animatable, "Idle", 10, state -> state.setAndContinue(IDLE));
    }

    public static <T extends GeoAnimatable> AnimationController<T> getSpawnController(T animatable, Function<AnimationState<T>, Object> objectSupplier, int ticks) {
        return new AnimationController<>(animatable, "Spawn", 0, state -> animatable.getTick(objectSupplier.apply(state)) <= (double) ticks ? state.setAndContinue(SPAWN) : PlayState.STOP);
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericWalkController(T animatable) {
        return new AnimationController<>(animatable, "Walk", 0, state -> state.isMoving() ? state.setAndContinue(WALK) : PlayState.STOP);
    }

    public static <T extends LivingEntity & GeoAnimatable> AnimationController<T> genericAttackAnimation(T animatable, RawAnimation attackAnimation) {
        return new AnimationController<>(animatable, "Attack", 5, state -> {
            if (animatable.swinging) {
                return state.setAndContinue(attackAnimation);
            } else {
                state.getController().forceAnimationReset();
                return PlayState.STOP;
            }
        });
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericWalkIdleController(T animatable) {
        return new AnimationController<>(animatable, "Walk/Idle", 0, state -> state.setAndContinue(state.isMoving() ? WALK : IDLE));
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericSwimController(T entity) {
        return new AnimationController<>(entity, "Swim", 0, state -> state.isMoving() ? state.setAndContinue(SWIM) : PlayState.STOP);
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericSwimIdleController(T animatable) {
        return new AnimationController<>(animatable, "Swim/Idle", 0, state -> state.setAndContinue(state.isMoving() ? SWIM : IDLE));
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericFlyController(T animatable) {
        return new AnimationController<>(animatable, "Fly", 0, state -> state.setAndContinue(FLY));
    }

    public static <T extends GeoAnimatable> AnimationController<T> genericFlyIdleController(T animatable) {
        return new AnimationController<>(animatable, "Fly/Idle", 0, state -> state.setAndContinue(state.isMoving() ? FLY : IDLE));
    }

    public static <T extends Entity & GeoAnimatable> AnimationController<T> genericWalkRunIdleController(T entity) {
        return new AnimationController<>(entity, "Walk/Run/Idle", 0, state -> state.isMoving() ? state.setAndContinue(entity.isSprinting() ? RUN : WALK) : state.setAndContinue(IDLE));
    }
}