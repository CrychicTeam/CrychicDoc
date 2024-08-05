package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneBarterGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneDisappearGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneReactToPlayerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneTargetHostilePlayersGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneWanderGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VerticalSwimmingMoveControl;
import com.github.alexmodguy.alexscaves.server.entity.item.WaterBoltEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.WaveEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DeepOneMageEntity extends DeepOneBaseEntity {

    public static final Animation ANIMATION_DISAPPEAR = Animation.create(55);

    public static final Animation ANIMATION_ATTACK = Animation.create(25);

    public static final Animation ANIMATION_SPIN = Animation.create(70);

    public static final Animation ANIMATION_TRADE = Animation.create(75);

    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.2F, 1.5F, false);

    private int spinCooldown = 0;

    private int rangedCooldown = 0;

    private Vec3 strafeTarget = null;

    public static final ResourceLocation BARTER_LOOT = new ResourceLocation("alexscaves", "gameplay/deep_one_mage_barter");

    private boolean isMageInWater = true;

    public DeepOneMageEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 80.0).add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new DeepOneAttackGoal(this));
        this.f_21345_.addGoal(1, new DeepOneBarterGoal(this));
        this.f_21345_.addGoal(2, new DeepOneReactToPlayerGoal(this));
        this.f_21345_.addGoal(3, new DeepOneDisappearGoal(this));
        this.f_21345_.addGoal(4, new DeepOneWanderGoal(this, 12, 1.0));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 1.0, 45) {

            @Override
            public boolean canUse() {
                return !DeepOneMageEntity.this.m_20072_() && super.canUse() && DeepOneMageEntity.this.getAnimation() != DeepOneMageEntity.ANIMATION_TRADE;
            }

            @Override
            public boolean canContinueToUse() {
                return !DeepOneMageEntity.this.m_20072_() && DeepOneMageEntity.this.getAnimation() != DeepOneMageEntity.ANIMATION_TRADE && super.canContinueToUse();
            }

            @Override
            protected Vec3 getPosition() {
                Vec3 prev = super.getPosition();
                return prev == null ? prev : prev.add(0.0, 1.0, 0.0);
            }
        });
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new DeepOneBaseEntity.HurtByHostileTargetGoal());
        this.f_21346_.addGoal(2, new DeepOneTargetHostilePlayersGoal(this));
    }

    @Override
    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.m_20256_(this.m_20184_().add(0.0, 0.1, 0.0));
            this.f_21344_ = this.createFlightNavigation(this.m_9236_());
            this.f_21342_ = new DeepOneMageEntity.FlightMoveController();
            this.isLandNavigator = true;
        } else {
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.f_21342_ = new VerticalSwimmingMoveControl(this, 0.8F, 10.0F);
            this.isLandNavigator = false;
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return this.m_9236_().getBlockState(pos).m_60795_() ? 10.0F : super.m_5610_(pos, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_20072_() && !this.m_21023_(ACEffectRegistry.BUBBLED.get())) {
            this.m_7292_(new MobEffectInstance(ACEffectRegistry.BUBBLED.get(), 200));
        }
        if (this.m_20072_() && this.m_21023_(ACEffectRegistry.BUBBLED.get())) {
            this.m_21195_(ACEffectRegistry.BUBBLED.get());
        }
        this.isMageInWater = this.m_20072_();
        if (this.getAnimation() == ANIMATION_SPIN) {
            this.m_20256_(this.m_20184_().scale(0.6F));
            LivingEntity target = this.m_5448_();
            if (target != null) {
                Vec3 vec = target.m_20182_().subtract(this.m_20182_()).normalize();
                this.m_20256_(this.m_20184_().add(vec.scale(0.1)));
            }
            if (this.getAnimationTick() % 6 == 0) {
                AABB bashBox = this.m_20191_().inflate(2.0, 0.0, 2.0);
                for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, bashBox)) {
                    if (!this.m_7307_(entity) && !(entity instanceof DeepOneBaseEntity)) {
                        this.checkAndDealMeleeDamage(entity, 0.4F, 1.0F);
                    }
                }
            }
        }
        if (this.getAnimation() == ANIMATION_ATTACK) {
            LivingEntity targetx = this.m_5448_();
            if (targetx != null && targetx.isAlive()) {
                if (this.getAnimationTick() == 16) {
                    this.useMagicAttack(targetx);
                } else if (this.getAnimationTick() < 16) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 68);
                }
                this.m_21563_().setLookAt(targetx.m_20185_(), targetx.m_20188_(), targetx.m_20189_(), 180.0F, 10.0F);
            }
        }
        if (this.spinCooldown > 0) {
            this.spinCooldown--;
        }
        if (this.rangedCooldown > 0) {
            this.rangedCooldown--;
        }
    }

    @Override
    protected ResourceLocation getBarterLootTable() {
        return BARTER_LOOT;
    }

    @Override
    public boolean isNoGravity() {
        return !this.isDeepOneSwimming() || super.m_20068_();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void useMagicAttack(LivingEntity target) {
        this.m_9236_().broadcastEntityEvent(this, (byte) 68);
        if (this.f_19796_.nextBoolean()) {
            int lifespan = (int) Math.floor((double) this.m_20270_(target)) + 10;
            Vec3 vec3 = target.m_20182_().subtract(this.m_20182_());
            for (int i = -2; i <= 2; i++) {
                WaveEntity waveEntity = new WaveEntity(this.m_9236_(), this);
                waveEntity.m_6034_(this.m_20185_(), target.m_20186_(), this.m_20189_());
                waveEntity.setLifespan(lifespan);
                waveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)) + (float) (i * 10));
                this.m_9236_().m_7967_(waveEntity);
            }
        } else {
            WaterBoltEntity waterBoltEntity = new WaterBoltEntity(this.m_9236_(), this);
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20227_(0.3333333333333333) - waterBoltEntity.m_20186_();
            double d2 = target.m_20189_() - this.m_20189_();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            waterBoltEntity.setBubbling(this.f_19796_.nextInt(2) == 0);
            waterBoltEntity.setArcingTowards(target.m_20148_());
            waterBoltEntity.m_6686_(d0, d1 + d3 * 0.67F, d2, 0.6F, 30.0F);
            this.m_9236_().m_7967_(waterBoltEntity);
        }
    }

    @Override
    public void startAttackBehavior(LivingEntity target) {
        this.f_20883_ = this.m_146908_();
        double distance = (double) this.m_20270_(target);
        float f = this.m_20205_() + target.m_20205_();
        if (distance > (double) (20.0F + f)) {
            this.m_21573_().moveTo(target, 1.2);
        } else if (distance < (double) (2.0F + f) && this.spinCooldown <= 0) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_SPIN);
                this.spinCooldown = 1000 + this.f_19796_.nextInt(60);
            }
        } else {
            if (this.strafeTarget != null && !(this.strafeTarget.distanceTo(this.m_20182_()) < 4.0)) {
                this.m_21573_().moveTo(this.strafeTarget.x, this.strafeTarget.y, this.strafeTarget.z, 1.5);
            } else {
                Vec3 possible = target.m_20182_().add((double) (this.f_19796_.nextInt(20) - 10), (double) this.f_19796_.nextInt(2), (double) (this.f_19796_.nextInt(20) - 10));
                if (!this.isTargetBlocked(possible)) {
                    this.strafeTarget = possible;
                }
            }
            if (this.rangedCooldown <= 0 && this.getAnimation() == NO_ANIMATION && this.m_142582_(target)) {
                this.setAnimation(ANIMATION_ATTACK);
                this.m_216990_(ACSoundRegistry.DEEP_ONE_MAGE_ATTACK.get());
                this.rangedCooldown = 30 + this.f_19796_.nextInt(20);
            }
            this.m_21563_().setLookAt(target.m_20185_(), target.m_20188_(), target.m_20189_(), 30.0F, 10.0F);
        }
    }

    private boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    @Override
    public EntityDimensions getSwimmingSize() {
        return SWIMMING_SIZE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_DISAPPEAR, ANIMATION_ATTACK, ANIMATION_SPIN, ANIMATION_TRADE };
    }

    @Override
    public boolean isDeepOneSwimming() {
        return this.isMageInWater && !this.m_20096_();
    }

    @Override
    public Animation getTradingAnimation() {
        return ANIMATION_TRADE;
    }

    protected PathNavigation createFlightNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.f_26495_.getBlockState(pos.below()).m_60795_();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean startDisappearBehavior(Player player) {
        this.m_21563_().setLookAt(player.m_20185_(), player.m_20188_(), player.m_20189_(), 20.0F, (float) this.m_8132_());
        this.m_21573_().stop();
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_DISAPPEAR);
        }
        if (this.getAnimation() == ANIMATION_DISAPPEAR) {
            if (this.getAnimationTick() > 50) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 67);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return true;
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 66);
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 66) {
            for (int i = 0; i < 2 + this.f_19796_.nextInt(4); i++) {
                this.m_9236_().addParticle((ParticleOptions) (this.f_19796_.nextBoolean() ? ACParticleRegistry.DEEP_ONE_MAGIC.get() : ParticleTypes.DOLPHIN), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), 0.0, -0.1F, 0.0);
            }
        } else if (b == 67) {
            for (int i = 0; i < 13 + this.f_19796_.nextInt(6); i++) {
                this.m_9236_().addParticle(ACParticleRegistry.DEEP_ONE_MAGIC.get(), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F));
                this.m_9236_().addParticle(ParticleTypes.NAUTILUS, this.m_20208_(1.0), this.m_20187_() + 1.0, this.m_20262_(1.0), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F));
            }
        } else if (b == 68) {
            Vec3 deltaPos = this.m_20182_().add(this.m_20184_());
            Vec3 rVec = new Vec3(0.65F, (double) (this.m_20206_() * 0.5F + 0.15F), 0.2F).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0)).add(deltaPos);
            Vec3 lVec = new Vec3(-0.65F, (double) (this.m_20206_() * 0.5F + 0.15F), 0.2F).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0)).add(deltaPos);
            this.m_9236_().addParticle(ACParticleRegistry.DEEP_ONE_MAGIC.get(), rVec.x + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), rVec.y + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), rVec.z + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.3F) + this.m_20184_().x, 1.0, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.3F) + this.m_20184_().z);
            this.m_9236_().addParticle(ACParticleRegistry.DEEP_ONE_MAGIC.get(), lVec.x + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), lVec.y + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), lVec.z + (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.3F) + this.m_20184_().x, 1.0, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.3F) + this.m_20184_().z);
        } else {
            super.handleEntityEvent(b);
        }
    }

    public float getStepHeight() {
        return 1.3F;
    }

    @Override
    public SoundEvent getAdmireSound() {
        return ACSoundRegistry.DEEP_ONE_MAGE_ADMIRE.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.soundsAngry() ? ACSoundRegistry.DEEP_ONE_MAGE_HOSTILE.get() : ACSoundRegistry.DEEP_ONE_MAGE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.DEEP_ONE_MAGE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.DEEP_ONE_MAGE_DEATH.get();
    }

    class FlightMoveController extends MoveControl {

        private final Mob parentEntity = DeepOneMageEntity.this;

        public FlightMoveController() {
            super(DeepOneMageEntity.this);
        }

        @Override
        public void tick() {
            this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(0.0, Math.sin((double) DeepOneMageEntity.this.f_19797_ * 0.1) * 0.005F, 0.0));
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                LivingEntity attackTarget = this.parentEntity.getTarget();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.025 / d0);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1));
                if (d0 < width * 0.3F) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (d0 >= width && attackTarget == null) {
                    if (DeepOneMageEntity.this.m_5448_() != null) {
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    } else {
                        this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    }
                }
            }
        }
    }
}