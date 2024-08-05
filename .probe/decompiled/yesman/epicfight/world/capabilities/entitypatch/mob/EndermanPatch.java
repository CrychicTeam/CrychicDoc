package yesman.epicfight.world.capabilities.entitypatch.mob;

import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeLivingMotion;
import yesman.epicfight.network.server.SPSpawnData;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviorGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class EndermanPatch extends MobPatch<EnderMan> {

    private static final UUID SPEED_MODIFIER_RAGE_UUID = UUID.fromString("dc362d1a-8424-11ec-a8a3-0242ac120002");

    private static final AttributeModifier SPEED_MODIFIER_RAGE = new AttributeModifier(SPEED_MODIFIER_RAGE_UUID, "Rage speed bonus", 0.1, AttributeModifier.Operation.ADDITION);

    private int deathTimerExt = 0;

    private boolean onRage;

    private Goal normalAttacks;

    private Goal teleportAttacks;

    private Goal rageAttacks;

    private Goal rageTargeting;

    public EndermanPatch() {
        super(Faction.ENDERMAN);
    }

    public void onJoinWorld(EnderMan enderman, EntityJoinLevelEvent event) {
        if (enderman.m_9236_().dimension() == Level.END && enderman.m_9236_().getGameRules().getBoolean(EpicFightGamerules.NO_MOBS_IN_BOSSFIGHT) && enderman.m_20182_().horizontalDistanceSqr() < 40000.0) {
            event.setCanceled(true);
        }
        super.onJoinWorld(enderman, event);
    }

    @Override
    public void onStartTracking(ServerPlayer trackingPlayer) {
        if (this.isRaging()) {
            SPSpawnData packet = new SPSpawnData(this.original.m_19879_());
            EpicFightNetworkManager.sendToPlayer(packet, trackingPlayer);
        }
    }

    @Override
    public void processSpawnData(ByteBuf buf) {
        ClientAnimator animator = this.getClientAnimator();
        animator.addLivingAnimation(LivingMotions.IDLE, Animations.ENDERMAN_RAGE_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, Animations.ENDERMAN_RAGE_WALK);
        animator.setCurrentMotionsAsDefault();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.STUN_ARMOR.get()).setBaseValue(8.0);
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue(1.8F);
    }

    @Override
    protected void initAI() {
        super.initAI();
        this.normalAttacks = new AnimatedAttackGoal<>(this, MobCombatBehaviors.ENDERMAN.build(this));
        this.teleportAttacks = new EndermanPatch.EndermanTeleportMove(this, MobCombatBehaviors.ENDERMAN_TELEPORT.build(this));
        this.rageAttacks = new AnimatedAttackGoal<>(this, MobCombatBehaviors.ENDERMAN_RAGE.build(this));
        this.rageTargeting = new NearestAttackableTargetGoal(this.original, Player.class, true);
        this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), 0.75, false));
        if (this.isRaging()) {
            this.original.f_21346_.addGoal(3, this.rageTargeting);
            this.original.f_21345_.addGoal(1, this.rageAttacks);
        } else {
            this.original.f_21345_.addGoal(1, this.normalAttacks);
            this.original.f_21345_.addGoal(0, this.teleportAttacks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.ENDERMAN_DEATH);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.ENDERMAN_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.ENDERMAN_IDLE);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        super.commonMobUpdateMotion(considerInaction);
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if (this.isRaging() && !this.onRage && this.original.f_19797_ > 5) {
            this.toRaging();
        } else if (this.onRage && !this.isRaging()) {
            this.toNormal();
        }
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (this.original.m_21223_() <= 0.0F) {
            this.original.m_146926_(0.0F);
            if (this.original.f_20919_ > 1 && this.deathTimerExt < 20) {
                this.deathTimerExt++;
                this.original.f_20919_--;
            }
        }
        super.tick(event);
    }

    @Override
    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        if (!this.original.m_9236_().isClientSide() && damageSource.getEntity() != null && !this.isRaging()) {
            EpicFightDamageSource extDamageSource = null;
            if (damageSource instanceof EpicFightDamageSource) {
                extDamageSource = (EpicFightDamageSource) damageSource;
            }
            if (extDamageSource == null || extDamageSource.getStunType() != StunType.HOLD) {
                int percentage = this.getServerAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation ? 10 : 3;
                if (this.original.m_217043_().nextInt(percentage) == 0) {
                    for (int i = 0; i < 9; i++) {
                        if (this.original.teleport()) {
                            if (damageSource.getEntity() instanceof LivingEntity) {
                                this.original.m_6703_((LivingEntity) damageSource.getEntity());
                            }
                            if (this.state.inaction()) {
                                this.playAnimationSynchronized(Animations.ENDERMAN_TP_EMERGENCE, 0.0F);
                            }
                            return AttackResult.blocked(amount);
                        }
                    }
                }
            }
        }
        return super.tryHurt(damageSource, amount);
    }

    public boolean isRaging() {
        return this.original.m_21223_() / this.original.m_21233_() < 0.33F;
    }

    protected void toRaging() {
        this.onRage = true;
        this.playAnimationSynchronized(Animations.ENDERMAN_CONVERT_RAGE, 0.0F);
        if (!this.original.m_21525_()) {
            this.original.f_21345_.removeGoal(this.normalAttacks);
            this.original.f_21345_.removeGoal(this.teleportAttacks);
            this.original.f_21345_.addGoal(1, this.rageAttacks);
            this.original.f_21346_.addGoal(3, this.rageTargeting);
            this.original.m_20088_().set(EnderMan.DATA_CREEPY, true);
            this.original.m_7292_(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 120000));
            this.original.m_21051_(Attributes.MOVEMENT_SPEED).addTransientModifier(SPEED_MODIFIER_RAGE);
            SPChangeLivingMotion msg = new SPChangeLivingMotion(this.original.m_19879_(), true).putPair(LivingMotions.IDLE, Animations.ENDERMAN_RAGE_IDLE).putPair(LivingMotions.WALK, Animations.ENDERMAN_RAGE_WALK);
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(msg, this.original);
        }
    }

    protected void toNormal() {
        this.onRage = false;
        if (!this.original.m_21525_()) {
            this.original.f_21345_.addGoal(1, this.normalAttacks);
            this.original.f_21345_.addGoal(0, this.teleportAttacks);
            this.original.f_21345_.removeGoal(this.rageAttacks);
            this.original.f_21346_.removeGoal(this.rageTargeting);
            if (this.original.m_5448_() == null) {
                this.original.m_20088_().set(EnderMan.DATA_CREEPY, false);
            }
            this.original.m_21195_(EpicFightMobEffects.STUN_IMMUNITY.get());
            this.original.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_RAGE);
            SPChangeLivingMotion msg = new SPChangeLivingMotion(this.original.m_19879_(), true).putPair(LivingMotions.IDLE, Animations.ENDERMAN_IDLE).putPair(LivingMotions.WALK, Animations.ENDERMAN_WALK);
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(msg, this.original);
        }
    }

    @Override
    public void aboutToDeath() {
        this.original.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        if (this.isLogicalClient()) {
            for (int i = 0; i < 100; i++) {
                RandomSource rand = this.original.m_217043_();
                Vec3f vec = new Vec3f((float) rand.nextInt(), (float) rand.nextInt(), (float) rand.nextInt());
                vec.normalise().scale(0.5F);
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.particleEngine.createParticle(EpicFightParticles.ENDERMAN_DEATH_EMIT.get(), this.original.m_20185_(), this.original.m_20186_() + (double) (this.original.m_6972_(Pose.STANDING).height / 2.0F), this.original.m_20189_(), (double) vec.x, (double) vec.y, (double) vec.z);
            }
        }
        super.aboutToDeath();
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        switch(stunType) {
            case SHORT:
                return Animations.ENDERMAN_HIT_SHORT;
            case LONG:
                return Animations.ENDERMAN_HIT_LONG;
            case HOLD:
                return Animations.ENDERMAN_HIT_SHORT;
            case KNOCKDOWN:
                return Animations.ENDERMAN_NEUTRALIZED;
            case NEUTRALIZE:
                return Animations.ENDERMAN_NEUTRALIZED;
            default:
                return null;
        }
    }

    static class EndermanTeleportMove extends CombatBehaviorGoal<EndermanPatch> {

        private int waitingCounter;

        private int delayCounter;

        private CombatBehaviors.Behavior<EndermanPatch> move;

        public EndermanTeleportMove(EndermanPatch mobpatch, CombatBehaviors<EndermanPatch> mobAttacks) {
            super(mobpatch, mobAttacks);
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.combatBehaviors.tick();
            if (super.canUse()) {
                this.move = this.combatBehaviors.selectRandomBehaviorSeries();
                return this.move != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            boolean waitExpired = this.waitingCounter <= 100;
            if (!waitExpired) {
                this.waitingCounter = 500;
            }
            return this.isValidTarget(this.mob.getTarget()) && !this.mobpatch.getEntityState().hurt() && !this.mobpatch.getEntityState().inaction() && waitExpired;
        }

        @Override
        public void start() {
            this.delayCounter = 20 + this.mob.m_217043_().nextInt(5);
            this.waitingCounter = 0;
        }

        @Override
        public void stop() {
            this.move = null;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.delayCounter-- < 0 && !this.mobpatch.getEntityState().inaction()) {
                Vec3f vec = new Vec3f((float) (this.mob.m_20185_() - target.m_20185_()), 0.0F, (float) (this.mob.m_20189_() - target.m_20189_()));
                vec.normalise().scale(1.414F);
                boolean flag = this.mob.m_20984_(target.m_20185_() + (double) vec.x, target.m_20186_(), target.m_20189_() + (double) vec.z, true);
                if (flag) {
                    this.mobpatch.rotateTo(target, 360.0F, true);
                    this.move.execute(this.mobpatch);
                    this.mob.m_9236_().playSound(null, this.mob.f_19854_, this.mob.f_19855_, this.mob.f_19856_, SoundEvents.ENDERMAN_TELEPORT, this.mob.m_5720_(), 1.0F, 1.0F);
                    this.mob.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    this.waitingCounter = 0;
                } else {
                    this.waitingCounter++;
                }
            }
        }
    }
}