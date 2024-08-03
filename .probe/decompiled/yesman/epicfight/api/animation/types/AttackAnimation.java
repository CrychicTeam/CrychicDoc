package yesman.epicfight.api.animation.types;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.entity.eventlistener.AttackEndEvent;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class AttackAnimation extends ActionAnimation {

    public final AttackAnimation.Phase[] phases;

    public static final TypeFlexibleHashMap.TypeKey<List<LivingEntity>> HIT_ENTITIES = new TypeFlexibleHashMap.TypeKey<List<LivingEntity>>() {

        public List<LivingEntity> defaultValue() {
            return Lists.newArrayList();
        }
    };

    public static final TypeFlexibleHashMap.TypeKey<List<LivingEntity>> HURT_ENTITIES = new TypeFlexibleHashMap.TypeKey<List<LivingEntity>>() {

        public List<LivingEntity> defaultValue() {
            return Lists.newArrayList();
        }
    };

    public static final TypeFlexibleHashMap.TypeKey<Integer> MAX_STRIKES_COUNT = new TypeFlexibleHashMap.TypeKey<Integer>() {

        public Integer defaultValue() {
            return 0;
        }
    };

    public AttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, path, armature, new AttackAnimation.Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
    }

    public AttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, path, armature, new AttackAnimation.Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, hand, colliderJoint, collider));
    }

    public AttackAnimation(float convertTime, String path, Armature armature, AttackAnimation.Phase... phases) {
        super(convertTime, path, armature);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        this.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        this.phases = phases;
        this.stateSpectrumBlueprint.clear();
        for (AttackAnimation.Phase phase : phases) {
            if (!phase.noStateBind) {
                this.bindPhaseState(phase);
            }
        }
    }

    protected void bindPhaseState(AttackAnimation.Phase phase) {
        float preDelay = phase.preDelay;
        if (preDelay == 0.0F) {
            preDelay += 0.01F;
        }
        this.stateSpectrumBlueprint.newTimePair(phase.start, preDelay).addState(EntityState.PHASE_LEVEL, 1).newTimePair(phase.start, phase.contact + 0.01F).addState(EntityState.CAN_SKILL_EXECUTION, false).newTimePair(phase.start, phase.recovery).addState(EntityState.MOVEMENT_LOCKED, true).addState(EntityState.UPDATE_LIVING_MOTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).newTimePair(phase.start, phase.end).addState(EntityState.INACTION, true).newTimePair(phase.antic, phase.end).addState(EntityState.TURNING_LOCKED, true).newTimePair(preDelay, phase.contact + 0.01F).addState(EntityState.ATTACKING, true).addState(EntityState.PHASE_LEVEL, 2).newTimePair(phase.contact + 0.01F, phase.end).addState(EntityState.PHASE_LEVEL, 3);
    }

    @Override
    public void begin(LivingEntityPatch<?> entitypatch) {
        super.begin(entitypatch);
        entitypatch.setLastAttackSuccess(false);
    }

    @Override
    public void linkTick(LivingEntityPatch<?> entitypatch, DynamicAnimation linkAnimation) {
        super.linkTick(entitypatch, linkAnimation);
        if (!entitypatch.isLogicalClient() && entitypatch instanceof MobPatch<?> mobpatch) {
            AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(this);
            float elapsedTime = player.getElapsedTime();
            EntityState state = this.getState(entitypatch, elapsedTime);
            if (state.getLevel() == 1 && !state.turningLocked()) {
                mobpatch.getOriginal().getNavigation().stop();
                entitypatch.getOriginal().attackAnim = 2.0F;
                LivingEntity target = entitypatch.getTarget();
                if (target != null) {
                    entitypatch.rotateTo(target, entitypatch.getYRotLimit(), false);
                }
            }
        }
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        super.tick(entitypatch);
        if (!entitypatch.isLogicalClient()) {
            this.attackTick(entitypatch);
        }
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        if (entitypatch instanceof ServerPlayerPatch playerpatch && isEnd) {
            playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, new AttackEndEvent(playerpatch, this));
        }
        if (entitypatch instanceof HumanoidMobPatch<?> mobpatch && entitypatch.isLogicalClient()) {
            Mob entity = mobpatch.getOriginal();
            if (entity.getTarget() != null && !entity.getTarget().isAlive()) {
                entity.setTarget(null);
            }
        }
    }

    protected void attackTick(LivingEntityPatch<?> entitypatch) {
        AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(this);
        float elapsedTime = player.getElapsedTime();
        float prevElapsedTime = player.getPrevElapsedTime();
        EntityState state = this.getState(entitypatch, elapsedTime);
        EntityState prevState = this.getState(entitypatch, prevElapsedTime);
        AttackAnimation.Phase phase = this.getPhaseByTime(elapsedTime);
        if (state.getLevel() == 1 && !state.turningLocked() && entitypatch instanceof MobPatch<?> mobpatch) {
            mobpatch.getOriginal().getNavigation().stop();
            entitypatch.getOriginal().attackAnim = 2.0F;
            LivingEntity target = entitypatch.getTarget();
            if (target != null) {
                entitypatch.rotateTo(target, entitypatch.getYRotLimit(), false);
            }
        }
        if (prevState.attacking() || state.attacking() || prevState.getLevel() < 2 && state.getLevel() > 2) {
            if (!prevState.attacking() || phase != this.getPhaseByTime(prevElapsedTime) && (state.attacking() || prevState.getLevel() < 2 && state.getLevel() > 2)) {
                entitypatch.playSound(this.getSwingSound(entitypatch, phase), 0.0F, 0.0F);
                entitypatch.removeHurtEntities();
            }
            this.hurtCollidingEntities(entitypatch, prevElapsedTime, elapsedTime, prevState, state, phase);
        }
    }

    protected void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, AttackAnimation.Phase phase) {
        LivingEntity entity = entitypatch.getOriginal();
        entitypatch.getArmature().initializeTransform();
        float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
        float poseTime = state.attacking() ? elapsedTime : phase.contact;
        List<Entity> list = this.getPhaseByTime(elapsedTime).getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch));
        if (list.size() > 0) {
            HitEntityList hitEntities = new HitEntityList(entitypatch, list, (HitEntityList.Priority) phase.getProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
            int maxStrikes = this.getMaxStrikes(entitypatch, phase);
            while (entitypatch.getCurrenltyHurtEntities().size() < maxStrikes && hitEntities.next()) {
                Entity hitten = hitEntities.getEntity();
                LivingEntity trueEntity = this.getTrueEntity(hitten);
                if (trueEntity != null && trueEntity.isAlive() && !entitypatch.getCurrenltyAttackedEntities().contains(trueEntity) && !entitypatch.isTeammate(hitten) && (hitten instanceof LivingEntity || hitten instanceof PartEntity) && entity.hasLineOfSight(hitten)) {
                    EpicFightDamageSource source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
                    int prevInvulTime = hitten.invulnerableTime;
                    hitten.invulnerableTime = 0;
                    AttackResult attackResult = entitypatch.attack(source, hitten, phase.hand);
                    hitten.invulnerableTime = prevInvulTime;
                    if (attackResult.resultType.dealtDamage()) {
                        if (entitypatch instanceof ServerPlayerPatch playerpatch) {
                            playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, new DealtDamageEvent(playerpatch, trueEntity, source, attackResult.damage));
                        }
                        hitten.level().playSound(null, hitten.getX(), hitten.getY(), hitten.getZ(), this.getHitSound(entitypatch, phase), hitten.getSoundSource(), 1.0F, 1.0F);
                        this.spawnHitParticle((ServerLevel) hitten.level(), entitypatch, hitten, phase);
                    }
                    entitypatch.getCurrenltyAttackedEntities().add(trueEntity);
                    if (attackResult.resultType.shouldCount()) {
                        entitypatch.getCurrenltyHurtEntities().add(trueEntity);
                    }
                }
            }
        }
    }

    public LivingEntity getTrueEntity(Entity entity) {
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        } else {
            if (entity instanceof PartEntity<?> partEntity) {
                Entity parentEntity = partEntity.getParent();
                if (parentEntity instanceof LivingEntity) {
                    return (LivingEntity) parentEntity;
                }
            }
            return null;
        }
    }

    protected int getMaxStrikes(LivingEntityPatch<?> entitypatch, AttackAnimation.Phase phase) {
        return ((Float) phase.getProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER).map(valueCorrector -> valueCorrector.getTotalValue((float) entitypatch.getMaxStrikes(phase.hand))).orElse((float) entitypatch.getMaxStrikes(phase.hand))).intValue();
    }

    protected SoundEvent getSwingSound(LivingEntityPatch<?> entitypatch, AttackAnimation.Phase phase) {
        return (SoundEvent) phase.getProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND).orElse(entitypatch.getSwingSound(phase.hand));
    }

    protected SoundEvent getHitSound(LivingEntityPatch<?> entitypatch, AttackAnimation.Phase phase) {
        return (SoundEvent) phase.getProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND).orElse(entitypatch.getWeaponHitSound(phase.hand));
    }

    public EpicFightDamageSource getEpicFightDamageSource(LivingEntityPatch<?> entitypatch, Entity target, AttackAnimation.Phase phase) {
        return this.getEpicFightDamageSource(entitypatch.getDamageSource(this, phase.hand), entitypatch, target, phase);
    }

    public EpicFightDamageSource getEpicFightDamageSource(DamageSource originalSource, LivingEntityPatch<?> entitypatch, Entity target, AttackAnimation.Phase phase) {
        if (phase == null) {
            phase = this.getPhaseByTime(entitypatch.<Animator>getAnimator().getPlayerFor(this).getElapsedTime());
        }
        EpicFightDamageSource extendedSource;
        if (originalSource instanceof EpicFightDamageSource epicfightDamageSource) {
            extendedSource = epicfightDamageSource;
        } else {
            extendedSource = EpicFightDamageSources.copy(originalSource).setAnimation(this);
        }
        phase.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER).ifPresent(opt -> extendedSource.setDamageModifier(opt));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER).ifPresent(opt -> extendedSource.setArmorNegation(opt.getTotalValue(extendedSource.getArmorNegation())));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER).ifPresent(opt -> extendedSource.setImpact(opt.getTotalValue(extendedSource.getImpact())));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).ifPresent(opt -> extendedSource.setStunType(opt));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG).ifPresent(opt -> opt.forEach(extendedSource::addRuntimeTag));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE).ifPresent(opt -> opt.forEach(extendedSource::addExtraDamage));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER).ifPresent(opt -> extendedSource.setInitialPosition((Vec3) opt.apply(entitypatch)));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER).ifPresentOrElse(opt -> extendedSource.setInitialPosition((Vec3) opt.apply(entitypatch)), () -> extendedSource.setInitialPosition(entitypatch.getOriginal().m_20182_()));
        return extendedSource;
    }

    protected void spawnHitParticle(ServerLevel world, LivingEntityPatch<?> attacker, Entity hit, AttackAnimation.Phase phase) {
        Optional<RegistryObject<HitParticleType>> particleOptional = phase.getProperty(AnimationProperty.AttackPhaseProperty.PARTICLE);
        HitParticleType particle = particleOptional.isPresent() ? (HitParticleType) ((RegistryObject) particleOptional.get()).get() : attacker.getWeaponHitParticle(phase.hand);
        particle.spawnParticleWithArgument(world, null, null, hit, attacker.getOriginal());
    }

    @Override
    public float getPlaySpeed(LivingEntityPatch<?> entitypatch) {
        if (entitypatch instanceof PlayerPatch<?> playerpatch) {
            AttackAnimation.Phase phase = this.getPhaseByTime(playerpatch.<Animator>getAnimator().getPlayerFor(this).getElapsedTime());
            float speedFactor = (Float) this.getProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR).orElse(1.0F);
            Optional<Float> property = this.getProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED);
            float correctedSpeed = (Float) property.map(value -> playerpatch.getAttackSpeed(phase.hand) / value).orElse(this.totalTime * playerpatch.getAttackSpeed(phase.hand));
            correctedSpeed = (float) Math.round(correctedSpeed * 1000.0F) / 1000.0F;
            return 1.0F + (correctedSpeed - 1.0F) * speedFactor;
        } else {
            return 1.0F;
        }
    }

    public <V> AttackAnimation addProperty(AnimationProperty.AttackAnimationProperty<V> propertyType, V value) {
        this.properties.put(propertyType, value);
        return this;
    }

    public <V> AttackAnimation addProperty(AnimationProperty.AttackPhaseProperty<V> propertyType, V value) {
        return this.addProperty(propertyType, value, 0);
    }

    public <V> AttackAnimation addProperty(AnimationProperty.AttackPhaseProperty<V> propertyType, V value, int index) {
        this.phases[index].addProperty(propertyType, value);
        return this;
    }

    public AttackAnimation.Phase getPhaseByTime(float elapsedTime) {
        AttackAnimation.Phase currentPhase = null;
        for (AttackAnimation.Phase phase : this.phases) {
            currentPhase = phase;
            if (phase.end > elapsedTime) {
                break;
            }
        }
        return currentPhase;
    }

    @Deprecated
    public void changeCollider(Collider newCollider, int index) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderDebugging(PoseStack poseStack, MultiBufferSource buffer, LivingEntityPatch<?> entitypatch, float playbackTime, float partialTicks) {
        AnimationPlayer animPlayer = entitypatch.<Animator>getAnimator().getPlayerFor(this);
        float prevElapsedTime = animPlayer.getPrevElapsedTime();
        float elapsedTime = animPlayer.getElapsedTime();
        AttackAnimation.Phase phase = this.getPhaseByTime(playbackTime);
        for (Pair<Joint, Collider> colliderInfo : phase.colliders) {
            Collider collider = (Collider) colliderInfo.getSecond();
            if (collider == null) {
                collider = entitypatch.getColliderMatching(phase.hand);
            }
            collider.draw(poseStack, buffer, entitypatch, this, (Joint) colliderInfo.getFirst(), prevElapsedTime, elapsedTime, partialTicks, this.getPlaySpeed(entitypatch));
        }
    }

    public static class Phase {

        private final Map<AnimationProperty.AttackPhaseProperty<?>, Object> properties = Maps.newHashMap();

        public final float start;

        public final float antic;

        public final float preDelay;

        public final float contact;

        public final float recovery;

        public final float end;

        public final InteractionHand hand;

        public List<Pair<Joint, Collider>> colliders;

        public final boolean noStateBind;

        public Phase(float start, float antic, float contact, float recovery, float end, Joint joint, Collider collider) {
            this(start, antic, contact, recovery, end, InteractionHand.MAIN_HAND, joint, collider);
        }

        public Phase(float start, float antic, float contact, float recovery, float end, InteractionHand hand, Joint joint, Collider collider) {
            this(start, antic, antic, contact, recovery, end, hand, joint, collider);
        }

        public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, Joint joint, Collider collider) {
            this(start, antic, preDelay, contact, recovery, end, InteractionHand.MAIN_HAND, joint, collider);
        }

        public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, InteractionHand hand, Joint joint, Collider collider) {
            this(start, antic, preDelay, contact, recovery, end, false, hand, joint, collider);
        }

        public Phase(InteractionHand hand, Joint joint, Collider collider) {
            this(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, true, hand, joint, collider);
        }

        public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, boolean noStateBind, InteractionHand hand, Joint joint, Collider collider) {
            this(start, antic, preDelay, contact, recovery, end, noStateBind, hand, List.of(Pair.of(joint, collider)));
        }

        public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, boolean noStateBind, InteractionHand hand, List<Pair<Joint, Collider>> colliders) {
            this.start = start;
            this.antic = antic;
            this.preDelay = preDelay;
            this.contact = contact;
            this.recovery = recovery;
            this.end = end;
            this.colliders = colliders;
            this.hand = hand;
            this.noStateBind = noStateBind;
        }

        public <V> AttackAnimation.Phase addProperty(AnimationProperty.AttackPhaseProperty<V> propertyType, V value) {
            this.properties.put(propertyType, value);
            return this;
        }

        public void addProperties(Set<Entry<AnimationProperty.AttackPhaseProperty<?>, Object>> set) {
            for (Entry<AnimationProperty.AttackPhaseProperty<?>, Object> entry : set) {
                this.properties.put((AnimationProperty.AttackPhaseProperty) entry.getKey(), entry.getValue());
            }
        }

        public <V> Optional<V> getProperty(AnimationProperty.AttackPhaseProperty<V> propertyType) {
            return Optional.ofNullable(this.properties.get(propertyType));
        }

        public List<Entity> getCollidingEntities(LivingEntityPatch<?> entitypatch, AttackAnimation animation, float prevElapsedTime, float elapsedTime, float attackSpeed) {
            List<Entity> entities = Lists.newArrayList();
            for (Pair<Joint, Collider> colliderInfo : this.colliders) {
                Collider collider = (Collider) colliderInfo.getSecond();
                if (collider == null) {
                    collider = entitypatch.getColliderMatching(this.hand);
                }
                entities.addAll(collider.updateAndSelectCollideEntity(entitypatch, animation, prevElapsedTime, elapsedTime, (Joint) colliderInfo.getFirst(), attackSpeed));
            }
            return new ArrayList(new HashSet(entities));
        }

        public List<Pair<Joint, Collider>> getColliders() {
            return this.colliders;
        }

        public InteractionHand getHand() {
            return this.hand;
        }
    }
}