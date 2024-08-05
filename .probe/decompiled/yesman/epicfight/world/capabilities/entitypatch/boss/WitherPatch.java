package yesman.epicfight.world.capabilities.entitypatch.boss;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.network.EpicFightDataSerializers;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.DroppedNetherStar;
import yesman.epicfight.world.entity.WitherGhostClone;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviorGoal;

public class WitherPatch extends MobPatch<WitherBoss> {

    private static final EntityDataAccessor<Boolean> DATA_ARMOR_ACTIVED = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_GHOST = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_TRANSPARENCY = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Vec3> DATA_LASER_DESTINATION_A = SynchedEntityData.defineId(WitherBoss.class, EpicFightDataSerializers.VEC3.get());

    private static final EntityDataAccessor<Vec3> DATA_LASER_DESTINATION_B = SynchedEntityData.defineId(WitherBoss.class, EpicFightDataSerializers.VEC3.get());

    private static final EntityDataAccessor<Vec3> DATA_LASER_DESTINATION_C = SynchedEntityData.defineId(WitherBoss.class, EpicFightDataSerializers.VEC3.get());

    private static final List<EntityDataAccessor<Vec3>> DATA_LASER_TARGET_POSITIONS = ImmutableList.of(DATA_LASER_DESTINATION_A, DATA_LASER_DESTINATION_B, DATA_LASER_DESTINATION_C);

    private static final EntityDataAccessor<Integer> DATA_LASER_TARGET_A = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_LASER_TARGET_B = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_LASER_TARGET_C = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);

    private static final List<EntityDataAccessor<Integer>> DATA_LASER_TARGETS = ImmutableList.of(DATA_LASER_TARGET_A, DATA_LASER_TARGET_B, DATA_LASER_TARGET_C);

    public static final TargetingConditions WTIHER_TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0).selector(livingentity -> livingentity.getMobType() != MobType.UNDEAD && livingentity.attackable());

    public static final TargetingConditions WTIHER_GHOST_TARGETING_CONDITIONS = WTIHER_TARGETING_CONDITIONS.copy().ignoreLineOfSight();

    private boolean blockedNow;

    private int deathTimerExt;

    private int blockingCount;

    private int blockingStartTick;

    private LivingEntityPatch<?> blockingEntity;

    public void onConstructed(WitherBoss witherBoss) {
        super.onConstructed(witherBoss);
        this.original.m_20088_().define(DATA_ARMOR_ACTIVED, false);
        this.original.m_20088_().define(DATA_GHOST, false);
        this.original.m_20088_().define(DATA_TRANSPARENCY, 0);
        this.original.m_20088_().define(DATA_LASER_DESTINATION_A, new Vec3(Double.NaN, Double.NaN, Double.NaN));
        this.original.m_20088_().define(DATA_LASER_DESTINATION_C, new Vec3(Double.NaN, Double.NaN, Double.NaN));
        this.original.m_20088_().define(DATA_LASER_DESTINATION_B, new Vec3(Double.NaN, Double.NaN, Double.NaN));
        this.original.m_20088_().define(DATA_LASER_TARGET_A, 0);
        this.original.m_20088_().define(DATA_LASER_TARGET_B, 0);
        this.original.m_20088_().define(DATA_LASER_TARGET_C, 0);
    }

    @Override
    public void initAI() {
        super.initAI();
        this.original.f_21345_.addGoal(1, new WitherPatch.WitherChasingGoal());
        this.original.f_21345_.addGoal(0, new WitherPatch.WitherGhostAttackGoal());
        this.original.f_21345_.addGoal(0, new CombatBehaviorGoal<>(this, MobCombatBehaviors.WITHER.build(this)));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue(3.0);
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.WITHER_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.WITHER_DEATH);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else {
            this.currentLivingMotion = LivingMotions.IDLE;
        }
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (this.original.m_21223_() <= 0.0F && this.original.f_20919_ > 1 && this.deathTimerExt < 17) {
            this.deathTimerExt++;
            this.original.f_20919_--;
        }
        if (!this.getEntityState().inaction()) {
            int targetId = this.original.getAlternativeTarget(0);
            Entity target = this.original.m_9236_().getEntity(targetId);
            if (target != null) {
                Vec3 vec3 = target.position().subtract(this.original.m_20182_()).normalize();
                float yrot = MathUtils.rotlerp(this.original.m_146908_(), (float) Mth.atan2(vec3.z, vec3.x) * (180.0F / (float) Math.PI) - 90.0F, 10.0F);
                this.original.m_146922_(yrot);
            }
        }
        super.tick(event);
    }

    @Override
    public void clientTick(LivingEvent.LivingTickEvent event) {
        super.clientTick(event);
        this.original.m_20334_(0.0, 0.0, 0.0);
        int transparencyCount = this.getTransparency();
        if (transparencyCount != 0) {
            this.setTransparency(transparencyCount + (transparencyCount > 0 ? -1 : 1));
        }
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if (this.original.m_21223_() <= this.original.m_21233_() * 0.5F) {
            if (!this.isArmorActivated() && !this.getEntityState().inaction() && this.original.getInvulnerableTicks() <= 0 && this.original.m_6084_()) {
                this.playAnimationSynchronized(Animations.WITHER_SPELL_ARMOR, 0.0F);
            }
        } else if (this.isArmorActivated()) {
            this.setArmorActivated(false);
        }
        if (this.animator.getPlayerFor(null).getAnimation().equals(Animations.WITHER_CHARGE) && this.getEntityState().attacking() && ForgeEventFactory.getMobGriefingEvent(this.original.m_9236_(), this.original)) {
            int x = Mth.floor(this.original.m_20185_());
            int y = Mth.floor(this.original.m_20186_());
            int z = Mth.floor(this.original.m_20189_());
            boolean flag = false;
            for (int j = -1; j <= 1; j++) {
                for (int k2 = -1; k2 <= 1; k2++) {
                    for (int k = 0; k <= 3; k++) {
                        int l2 = x + j;
                        int l = y + k;
                        int i1 = z + k2;
                        BlockPos blockpos = new BlockPos(l2, l, i1);
                        BlockState blockstate = this.original.m_9236_().getBlockState(blockpos);
                        if (blockstate.canEntityDestroy(this.original.m_9236_(), blockpos, this.original) && ForgeEventFactory.onEntityDestroyBlock(this.original, blockpos, blockstate)) {
                            flag = this.original.m_9236_().m_46953_(blockpos, true, this.original) || flag;
                        }
                    }
                }
            }
            if (flag) {
                this.original.m_9236_().m_5898_(null, 1022, this.original.m_20183_(), 0);
            }
        }
        if (this.blockedNow) {
            if (this.blockingCount < 0) {
                this.playAnimationSynchronized(Animations.WITHER_NEUTRALIZED, 0.0F);
                this.original.m_5496_(EpicFightSounds.NEUTRALIZE_BOSSES.get(), 5.0F, 1.0F);
                this.blockedNow = false;
                this.blockingEntity = null;
            } else if (this.original.f_19797_ % 4 == (this.blockingStartTick - 1) % 4) {
                if (this.original.m_20182_().distanceToSqr(this.blockingEntity.getOriginal().m_20182_()) < 9.0) {
                    EpicFightDamageSource extendedSource = this.getDamageSource(Animations.WITHER_CHARGE, InteractionHand.MAIN_HAND);
                    extendedSource.setStunType(StunType.KNOCKDOWN).setImpact(4.0F).setInitialPosition(this.lastAttackPosition);
                    AttackResult attackResult = this.tryHarm(this.blockingEntity.getOriginal(), extendedSource, (float) this.blockingCount);
                    if (attackResult.resultType == AttackResult.ResultType.SUCCESS) {
                        this.blockingEntity.getOriginal().hurt(extendedSource, 4.0F);
                        this.blockedNow = false;
                        this.blockingEntity = null;
                    }
                } else {
                    this.blockedNow = false;
                    this.blockingEntity = null;
                }
            }
        }
    }

    @Override
    public void onAttackBlocked(DamageSource damageSource, LivingEntityPatch<?> opponent) {
        if (damageSource instanceof EpicFightDamageSource extendedDamageSource && Animations.WITHER_CHARGE.equals(extendedDamageSource.getAnimation())) {
            if (!this.blockedNow) {
                this.blockedNow = true;
                this.blockingStartTick = this.original.f_19797_;
                this.blockingEntity = opponent;
                this.playAnimationSynchronized(Animations.WITHER_BLOCKED, 0.0F);
            }
            this.blockingCount--;
            Vec3 lookAngle = opponent.getOriginal().m_20154_();
            lookAngle = lookAngle.subtract(0.0, lookAngle.y, 0.0);
            lookAngle.scale(0.1);
            this.original.m_146884_(opponent.getOriginal().m_20182_().add(lookAngle));
        }
    }

    @Override
    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        DynamicAnimation animation = this.<Animator>getAnimator().getPlayerFor(null).getAnimation();
        if (animation.equals(Animations.WITHER_CHARGE) || animation.equals(Animations.WITHER_BLOCKED)) {
            Entity entity = damageSource.getDirectEntity();
            if (entity instanceof AbstractArrow) {
                return AttackResult.blocked(0.0F);
            }
        }
        return super.tryHurt(damageSource, amount);
    }

    @Override
    public void onDeath(LivingDeathEvent event) {
        super.onDeath(event);
        if (!this.isLogicalClient() && this.original.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            Vec3 startMovement = this.original.m_20154_().scale(0.4).add(0.0, 0.63, 0.0);
            ItemEntity itemEntity = new DroppedNetherStar(this.original.m_9236_(), this.original.m_20182_().add(0.0, (double) this.original.m_20206_() * 0.5, 0.0), startMovement);
            this.original.m_9236_().m_7967_(itemEntity);
        }
    }

    @Override
    public boolean onDrop(LivingDropsEvent event) {
        event.getDrops().removeIf(itemEntity -> itemEntity.getItem().is(Items.NETHER_STAR));
        return false;
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        float prevYRot;
        float yRot;
        if (this.original.m_20202_() instanceof LivingEntity ridingEntity) {
            prevYRot = ridingEntity.yBodyRotO;
            yRot = ridingEntity.yBodyRot;
        } else {
            prevYRot = this.isLogicalClient() ? this.original.f_20884_ : this.original.f_19859_;
            yRot = this.isLogicalClient() ? this.original.f_20883_ : this.original.m_146908_();
        }
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, prevYRot, yRot, partialTicks, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return null;
    }

    public void startCharging() {
        this.setLastAttackPosition();
        this.blockingCount = 3;
    }

    public void setArmorActivated(boolean set) {
        this.original.m_20088_().set(DATA_ARMOR_ACTIVED, set);
    }

    public boolean isArmorActivated() {
        return this.original.m_20088_().get(DATA_ARMOR_ACTIVED);
    }

    public void setGhost(boolean set) {
        this.original.m_20088_().set(DATA_GHOST, set);
        this.original.m_20242_(set);
        this.setTransparency(set ? 40 : -40);
        this.original.m_6842_(set);
    }

    public boolean isGhost() {
        return this.original.m_20088_().get(DATA_GHOST);
    }

    public void setTransparency(int set) {
        this.original.m_20088_().set(DATA_TRANSPARENCY, set);
    }

    public int getTransparency() {
        return this.original.m_20088_().get(DATA_TRANSPARENCY);
    }

    public void setLaserTargetPosition(int head, Vec3 pos) {
        this.original.m_20088_().set((EntityDataAccessor<Vec3>) DATA_LASER_TARGET_POSITIONS.get(head), pos);
    }

    public Vec3 getLaserTargetPosition(int head) {
        return this.original.m_20088_().get((EntityDataAccessor<Vec3>) DATA_LASER_TARGET_POSITIONS.get(head));
    }

    public void setLaserTarget(int head, Entity target) {
        this.original.m_20088_().set((EntityDataAccessor<Integer>) DATA_LASER_TARGETS.get(head), target != null ? target.getId() : -1);
    }

    public Entity getLaserTargetEntity(int head) {
        int laserTarget = this.original.m_20088_().<Integer>get((EntityDataAccessor<Integer>) DATA_LASER_TARGETS.get(head));
        return laserTarget > 0 ? this.original.m_9236_().getEntity(laserTarget) : null;
    }

    public Entity getAlternativeTargetEntity(int head) {
        int id = this.original.getAlternativeTarget(head);
        return id > 0 ? this.original.m_9236_().getEntity(id) : null;
    }

    public double getHeadX(int index) {
        if (index <= 0) {
            return this.original.m_20185_();
        } else {
            float f = (this.original.m_146908_() + (float) (180 * (index - 1))) * (float) (Math.PI / 180.0);
            float f1 = Mth.cos(f);
            return this.original.m_20185_() + (double) f1 * 1.3;
        }
    }

    public double getHeadY(int index) {
        return index <= 0 ? this.original.m_20186_() + 3.0 : this.original.m_20186_() + 2.2;
    }

    public double getHeadZ(int index) {
        if (index <= 0) {
            return this.original.m_20189_();
        } else {
            float f = (this.original.m_146908_() + (float) (180 * (index - 1))) * (float) (Math.PI / 180.0);
            float f1 = Mth.sin(f);
            return this.original.m_20189_() + (double) f1 * 1.3;
        }
    }

    public class WitherChasingGoal extends Goal {

        public WitherChasingGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return WitherPatch.this.original.getAlternativeTarget(0) > 0;
        }

        @Override
        public void tick() {
            WitherBoss witherBoss = WitherPatch.this.getOriginal();
            Vec3 vec3 = witherBoss.m_20184_().multiply(1.0, 0.6, 1.0);
            Entity entity = witherBoss.m_9236_().getEntity(WitherPatch.this.original.getAlternativeTarget(0));
            if (!WitherPatch.this.getEntityState().hurt() && !WitherPatch.this.blockedNow) {
                if (entity != null) {
                    Vec3 vec31 = new Vec3(entity.getX() - witherBoss.m_20185_(), 0.0, entity.getZ() - witherBoss.m_20189_());
                    double d0 = vec3.y;
                    if (witherBoss.m_20186_() < entity.getY() || !witherBoss.isPowered() && witherBoss.m_20186_() < entity.getY() + 5.0 && !(Boolean) WitherPatch.this.<Animator>getAnimator().getPlayerFor(null).getAnimation().getProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL).orElse(false)) {
                        d0 = Math.max(0.0, d0);
                        d0 += 0.3 - d0 * 0.6F;
                    }
                    vec3 = new Vec3(vec3.x, d0, vec3.z);
                    double followingRange = witherBoss.isPowered() ? 9.0 : 49.0;
                    if (vec31.horizontalDistanceSqr() > followingRange && !WitherPatch.this.getEntityState().inaction()) {
                        Vec3 vec32 = vec31.normalize();
                        vec3 = vec3.add(vec32.x * 0.3 - vec3.x * 0.6, 0.0, vec32.z * 0.3 - vec3.z * 0.6);
                    }
                }
                witherBoss.m_20256_(vec3);
            }
        }
    }

    public class WitherGhostAttackGoal extends Goal {

        private int ghostSummonCount;

        private int maxGhostSpawn;

        private int summonInverval;

        private int cooldown;

        public WitherGhostAttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return --this.cooldown < 0 && WitherPatch.this.isArmorActivated() && !WitherPatch.this.getEntityState().inaction() && WitherPatch.this.original.m_5448_() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.ghostSummonCount <= this.maxGhostSpawn;
        }

        @Override
        public void start() {
            WitherPatch.this.playAnimationSynchronized(Animations.WITHER_GHOST_STANDBY, 0.0F);
            WitherPatch.this.updateEntityState();
            WitherPatch.this.setGhost(true);
            List<LivingEntity> nearbyEnemies = this.getNearbyTargets();
            this.ghostSummonCount = 0;
            this.summonInverval = 25;
            this.maxGhostSpawn = Mth.clamp(nearbyEnemies.size() / 2, 2, 4);
        }

        @Override
        public void tick() {
            if (--this.summonInverval <= 0) {
                if (this.ghostSummonCount < this.maxGhostSpawn) {
                    List<LivingEntity> nearbyEnemies = this.getNearbyTargets();
                    if (nearbyEnemies.size() > 0) {
                        LivingEntity randomTarget = (LivingEntity) nearbyEnemies.get(WitherPatch.this.original.m_217043_().nextInt(nearbyEnemies.size()));
                        Vec3 summonPosition = randomTarget.m_20182_().add(new Vec3(0.0, 0.0, 6.0).yRot(WitherPatch.this.original.m_217043_().nextFloat() * 360.0F));
                        WitherGhostClone ghostclone = new WitherGhostClone((ServerLevel) WitherPatch.this.original.m_9236_(), summonPosition, randomTarget);
                        WitherPatch.this.original.m_9236_().m_7967_(ghostclone);
                    } else {
                        this.ghostSummonCount = this.maxGhostSpawn + 1;
                    }
                }
                this.ghostSummonCount++;
                this.summonInverval = this.ghostSummonCount < this.maxGhostSpawn ? 25 : 35;
                if (this.ghostSummonCount == this.maxGhostSpawn) {
                    LivingEntity target = WitherPatch.this.original.m_5448_();
                    if (target != null) {
                        Vec3 summonPosition = target.m_20182_().add(new Vec3(0.0, 0.0, 6.0).yRot(WitherPatch.this.original.m_217043_().nextFloat() * 360.0F)).add(0.0, 5.0, 0.0);
                        WitherPatch.this.original.m_146884_(summonPosition);
                        WitherPatch.this.original.m_7618_(EntityAnchorArgument.Anchor.FEET, WitherPatch.this.original.m_5448_().m_20182_());
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.cooldown = 300;
            if (WitherPatch.this.original.m_5448_() != null) {
                WitherPatch.this.playSound(SoundEvents.WITHER_AMBIENT, -0.1F, 0.1F);
                WitherPatch.this.playAnimationSynchronized(Animations.WITHER_CHARGE, 0.0F);
            } else {
                WitherPatch.this.playAnimationSynchronized(Animations.OFF_ANIMATION_HIGHEST, 0.0F);
            }
            WitherPatch.this.setGhost(false);
        }

        public List<LivingEntity> getNearbyTargets() {
            return WitherPatch.this.original.m_9236_().m_45971_(LivingEntity.class, WitherPatch.WTIHER_GHOST_TARGETING_CONDITIONS, WitherPatch.this.original, WitherPatch.this.original.m_20191_().inflate(20.0, 5.0, 20.0));
        }
    }
}