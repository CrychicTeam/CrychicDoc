package yesman.epicfight.api.animation.property;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

public abstract class AnimationProperty<T> {

    public static class ActionAnimationProperty<T> extends AnimationProperty<T> {

        public static final AnimationProperty.ActionAnimationProperty<Boolean> STOP_MOVEMENT = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> MOVE_VERTICAL = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<TimePairList> NO_GRAVITY_TIME = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<TransformSheet> COORD = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> MOVE_ON_LINK = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<TimePairList> MOVE_TIME = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<MoveCoordFunctions.MoveCoordSetter> COORD_SET_BEGIN = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<MoveCoordFunctions.MoveCoordSetter> COORD_SET_TICK = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<MoveCoordFunctions.MoveCoordGetter> COORD_GET = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> AFFECT_SPEED = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> CANCELABLE_MOVE = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> IS_DEATH_ANIMATION = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<TimePairList> COORD_UPDATE_TIME = new AnimationProperty.ActionAnimationProperty<>();

        public static final AnimationProperty.ActionAnimationProperty<Boolean> RESET_PLAYER_COMBO_COUNTER = new AnimationProperty.ActionAnimationProperty<>();
    }

    public static class AttackAnimationProperty<T> extends AnimationProperty<T> {

        public static final AnimationProperty.AttackAnimationProperty<Boolean> FIXED_MOVE_DISTANCE = new AnimationProperty.AttackAnimationProperty<>();

        public static final AnimationProperty.AttackAnimationProperty<Float> ATTACK_SPEED_FACTOR = new AnimationProperty.AttackAnimationProperty<>();

        public static final AnimationProperty.AttackAnimationProperty<Float> BASIS_ATTACK_SPEED = new AnimationProperty.AttackAnimationProperty<>();

        public static final AnimationProperty.AttackAnimationProperty<Integer> EXTRA_COLLIDERS = new AnimationProperty.AttackAnimationProperty<>();
    }

    public static class AttackPhaseProperty<T> extends AnimationProperty<T> {

        public static final AnimationProperty.AttackPhaseProperty<ValueModifier> MAX_STRIKES_MODIFIER = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<ValueModifier> DAMAGE_MODIFIER = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<Set<ExtraDamageInstance>> EXTRA_DAMAGE = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<ValueModifier> ARMOR_NEGATION_MODIFIER = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<ValueModifier> IMPACT_MODIFIER = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<StunType> STUN_TYPE = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<SoundEvent> SWING_SOUND = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<SoundEvent> HIT_SOUND = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<RegistryObject<HitParticleType>> PARTICLE = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<HitEntityList.Priority> HIT_PRIORITY = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<Set<TagKey<DamageType>>> SOURCE_TAG = new AnimationProperty.AttackPhaseProperty<>();

        public static final AnimationProperty.AttackPhaseProperty<Function<LivingEntityPatch<?>, Vec3>> SOURCE_LOCATION_PROVIDER = new AnimationProperty.AttackPhaseProperty<>();
    }

    @FunctionalInterface
    public interface PlaybackTimeModifier {

        float modify(DynamicAnimation var1, LivingEntityPatch<?> var2, float var3, float var4);
    }

    @FunctionalInterface
    public interface PoseModifier {

        void modify(DynamicAnimation var1, Pose var2, LivingEntityPatch<?> var3, float var4, float var5);
    }

    @FunctionalInterface
    public interface Registerer<T> {

        void register(Map<AnimationProperty<T>, Object> var1, AnimationProperty<T> var2, T var3);
    }

    public static class StaticAnimationProperty<T> extends AnimationProperty<T> {

        public static final AnimationProperty.StaticAnimationProperty<AnimationEvent[]> EVENTS = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationEvent.TimeStampedEvent[]> TIME_STAMPED_EVENTS = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationEvent.TimePeriodEvent[]> TIME_PERIOD_EVENTS = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationEvent[]> ON_BEGIN_EVENTS = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationEvent[]> ON_END_EVENTS = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationProperty.PlaybackTimeModifier> PLAY_SPEED_MODIFIER = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationProperty.PlaybackTimeModifier> ELAPSED_TIME_MODIFIER = new AnimationProperty.StaticAnimationProperty<>();

        public static final AnimationProperty.StaticAnimationProperty<AnimationProperty.PoseModifier> POSE_MODIFIER = new AnimationProperty.StaticAnimationProperty<>();
    }
}