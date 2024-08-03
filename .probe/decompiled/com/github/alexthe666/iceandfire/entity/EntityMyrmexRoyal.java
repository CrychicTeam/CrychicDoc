package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackPlayers;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIDefendHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIFindMate;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILeaveHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILookAtTradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIMoveThroughHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIMoveToMate;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIReEnterHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAITradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWander;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWanderHiveCenter;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.google.common.base.Predicate;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexRoyal extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_STING = Animation.create(15);

    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_royal_desert");

    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_royal_jungle");

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_royal.png");

    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_royal.png");

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityMyrmexRoyal.class, EntityDataSerializers.BOOLEAN);

    public int releaseTicks = 0;

    public int daylightTicks = 0;

    public float flyProgress;

    public EntityMyrmexRoyal mate;

    private int hiveTicks = 0;

    private int breedingTicks = 0;

    private boolean isFlying;

    private boolean isLandNavigator;

    private boolean isMating = false;

    public EntityMyrmexRoyal(EntityType<EntityMyrmexRoyal> t, Level worldIn) {
        super(t, worldIn);
        this.switchNavigator(true);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel1Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_ROYAL.get(1) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_ROYAL.get(1);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel2Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_ROYAL.get(2) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_ROYAL.get(2);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, double x, double z, RandomSource rand) {
        BlockPos pos = BlockPos.containing(x, (double) entity.getBlockY(), z);
        for (int yDown = 0; yDown < 10; yDown++) {
            if (!world.m_46859_(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FLYING, Boolean.FALSE);
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.CLIMBING);
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntityMyrmexRoyal.FlyMoveHelper(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.FLYING);
            this.isLandNavigator = false;
        }
    }

    public boolean isFlying() {
        return this.m_9236_().isClientSide ? (this.isFlying = this.f_19804_.get(FLYING)) : this.isFlying;
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
        if (!this.m_9236_().isClientSide) {
            this.isFlying = flying;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HiveTicks", this.hiveTicks);
        tag.putInt("ReleaseTicks", this.releaseTicks);
        tag.putBoolean("Flying", this.isFlying());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.hiveTicks = tag.getInt("HiveTicks");
        this.releaseTicks = tag.getInt("ReleaseTicks");
        this.setFlying(tag.getBoolean("Flying"));
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        boolean flying = this.isFlying() && !this.m_20096_();
        LivingEntity attackTarget = this.m_5448_();
        if (flying && this.flyProgress < 20.0F) {
            this.flyProgress++;
        } else if (!flying && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (flying) {
            double up = this.m_20069_() ? 0.16 : 0.08;
            this.m_20256_(this.m_20184_().add(0.0, up, 0.0));
        }
        if (flying && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!flying && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        if (flying && this.canSeeSky() && this.isBreedingSeason()) {
            this.releaseTicks++;
        }
        if (!flying && this.canSeeSky() && this.daylightTicks > 300 && this.isBreedingSeason() && attackTarget == null && this.canMove() && this.m_20096_() && !this.isMating) {
            this.setFlying(true);
            this.m_20256_(this.m_20184_().add(0.0, 0.42, 0.0));
        }
        if (this.getGrowthStage() >= 2) {
            this.hiveTicks++;
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(attackTarget.m_20191_())) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && attackTarget != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(attackTarget.m_20191_())) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * 2));
                attackTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 70, 1));
            }
        }
        if (this.mate != null) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 77);
            if (this.m_20270_(this.mate) < 10.0F) {
                this.setFlying(false);
                this.mate.setFlying(false);
                this.isMating = true;
                if (this.m_20096_() && this.mate.m_20096_()) {
                    this.breedingTicks++;
                    if (this.breedingTicks > 100) {
                        if (this.m_6084_()) {
                            this.mate.m_142687_(Entity.RemovalReason.KILLED);
                            this.m_142687_(Entity.RemovalReason.KILLED);
                            EntityMyrmexQueen queen = new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(), this.m_9236_());
                            queen.m_20359_(this);
                            queen.setJungleVariant(this.isJungle());
                            queen.setMadeHome(false);
                            if (!this.m_9236_().isClientSide) {
                                this.m_9236_().m_7967_(queen);
                            }
                        }
                        this.isMating = false;
                    }
                }
            }
            this.mate.mate = this;
            if (!this.mate.m_6084_()) {
                this.mate.mate = null;
                this.mate = null;
            }
        }
    }

    protected double attackDistance() {
        return 8.0;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new MyrmexAITradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAIMoveToMate(this, 1.0));
        this.f_21345_.addGoal(1, new EntityMyrmexRoyal.AIFlyAtTarget());
        this.f_21345_.addGoal(2, new EntityMyrmexRoyal.AIFlyRandom());
        this.f_21345_.addGoal(3, new MyrmexAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(4, new MyrmexAILeaveHive(this, 1.0));
        this.f_21345_.addGoal(4, new MyrmexAIReEnterHive(this, 1.0));
        this.f_21345_.addGoal(5, new MyrmexAIMoveThroughHive(this, 1.0));
        this.f_21345_.addGoal(5, new MyrmexAIWanderHiveCenter(this, 1.0));
        this.f_21345_.addGoal(6, new MyrmexAIWander(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MyrmexAIDefendHive(this));
        this.f_21346_.addGoal(2, new MyrmexAIFindMate(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return (!(entity instanceof EntityMyrmexBase) || !EntityMyrmexRoyal.this.isBreedingSeason()) && !(entity instanceof EntityMyrmexRoyal) ? entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexRoyal.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Enemy) : false;
            }
        }));
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal != this && otherAnimal != null) {
            if (otherAnimal.getClass() != this.getClass()) {
                return false;
            } else if (!(otherAnimal instanceof EntityMyrmexBase)) {
                return false;
            } else {
                return ((EntityMyrmexBase) otherAnimal).getHive() != null && this.getHive() != null ? !this.getHive().equals(((EntityMyrmexBase) otherAnimal).getHive()) : true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 2.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 9.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 2.0);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.25F;
    }

    @Override
    public int getCasteImportance() {
        return 2;
    }

    @Override
    public boolean shouldLeaveHive() {
        return this.isBreedingSeason();
    }

    @Override
    public boolean shouldEnterHive() {
        return !this.isBreedingSeason();
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        } else if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING };
    }

    public boolean isBreedingSeason() {
        return this.getGrowthStage() >= 2 && (this.getHive() == null || this.getHive().reproduces);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 76) {
            this.playEffect(20);
        } else if (id == 77) {
            this.playEffect(7);
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void playEffect(int hearts) {
        for (int i = 0; i < hearts; i++) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + 0.5 + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d0, d1, d2);
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        Vec3 vector3d = Vec3.atCenterOf(posVec31);
        Vec3 vector3d1 = Vec3.atCenterOf(posVec32);
        return this.m_9236_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isClientSide() {
        return this.m_9236_().isClientSide;
    }

    class AIFlyAtTarget extends Goal {

        public AIFlyAtTarget() {
        }

        @Override
        public boolean canUse() {
            return EntityMyrmexRoyal.this.m_5448_() != null && !EntityMyrmexRoyal.this.m_21566_().hasWanted() && EntityMyrmexRoyal.this.f_19796_.nextInt(7) == 0 ? EntityMyrmexRoyal.this.m_20280_(EntityMyrmexRoyal.this.m_5448_()) > 4.0 : false;
        }

        @Override
        public boolean canContinueToUse() {
            return EntityMyrmexRoyal.this.m_21566_().hasWanted() && EntityMyrmexRoyal.this.m_5448_() != null && EntityMyrmexRoyal.this.m_5448_().isAlive();
        }

        @Override
        public void start() {
            LivingEntity LivingEntity = EntityMyrmexRoyal.this.m_5448_();
            Vec3 Vector3d = LivingEntity.m_20299_(1.0F);
            EntityMyrmexRoyal.this.f_21342_.setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, 1.0);
        }

        @Override
        public void stop() {
        }

        @Override
        public void tick() {
            LivingEntity LivingEntity = EntityMyrmexRoyal.this.m_5448_();
            if (LivingEntity != null) {
                if (EntityMyrmexRoyal.this.m_20191_().intersects(LivingEntity.m_20191_())) {
                    EntityMyrmexRoyal.this.doHurtTarget(LivingEntity);
                } else {
                    double d0 = EntityMyrmexRoyal.this.m_20280_(LivingEntity);
                    if (d0 < 9.0) {
                        Vec3 Vector3d = LivingEntity.m_20299_(1.0F);
                        EntityMyrmexRoyal.this.f_21342_.setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, 1.0);
                    }
                }
            }
        }
    }

    class AIFlyRandom extends Goal {

        BlockPos target;

        public AIFlyRandom() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (EntityMyrmexRoyal.this.isFlying() && EntityMyrmexRoyal.this.m_5448_() == null) {
                if (EntityMyrmexRoyal.this instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner() != null) {
                    Entity summon = ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner();
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.m_9236_(), summon.getX() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(10) - 5.0, summon.getZ() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(10) - 5.0, EntityMyrmexRoyal.this.f_19796_);
                } else {
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.m_9236_(), EntityMyrmexRoyal.this.m_20185_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(30) - 15.0, EntityMyrmexRoyal.this.m_20189_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(30) - 15.0, EntityMyrmexRoyal.this.f_19796_);
                }
                return EntityMyrmexRoyal.this.isDirectPathBetweenPoints(EntityMyrmexRoyal.this.m_20183_(), this.target) && !EntityMyrmexRoyal.this.m_21566_().hasWanted() && EntityMyrmexRoyal.this.f_19796_.nextInt(2) == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            if (!EntityMyrmexRoyal.this.isDirectPathBetweenPoints(EntityMyrmexRoyal.this.m_20183_(), this.target)) {
                if (EntityMyrmexRoyal.this instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner() != null) {
                    Entity summon = ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner();
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.m_9236_(), summon.getX() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(10) - 5.0, summon.getZ() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(10) - 5.0, EntityMyrmexRoyal.this.f_19796_);
                } else {
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.m_9236_(), EntityMyrmexRoyal.this.m_20185_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(30) - 15.0, EntityMyrmexRoyal.this.m_20189_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(30) - 15.0, EntityMyrmexRoyal.this.f_19796_);
                }
            }
            if (EntityMyrmexRoyal.this.m_9236_().m_46859_(this.target)) {
                EntityMyrmexRoyal.this.f_21342_.setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.25);
                if (EntityMyrmexRoyal.this.m_5448_() == null) {
                    EntityMyrmexRoyal.this.m_21563_().setLookAt((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 180.0F, 20.0F);
                }
            }
        }
    }

    class FlyMoveHelper extends MoveControl {

        public FlyMoveHelper(EntityMyrmexRoyal pixie) {
            super(pixie);
            this.f_24978_ = 1.75;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                if (EntityMyrmexRoyal.this.f_19862_) {
                    EntityMyrmexRoyal.this.m_146922_(EntityMyrmexRoyal.this.m_146908_() + 180.0F);
                    this.f_24978_ = 0.1F;
                    BlockPos target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.m_9236_(), EntityMyrmexRoyal.this.m_20185_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(15) - 7.0, EntityMyrmexRoyal.this.m_20189_() + (double) EntityMyrmexRoyal.this.f_19796_.nextInt(15) - 7.0, EntityMyrmexRoyal.this.f_19796_);
                    this.f_24975_ = (double) target.m_123341_();
                    this.f_24976_ = (double) target.m_123342_();
                    this.f_24977_ = (double) target.m_123343_();
                }
                double d0 = this.f_24975_ - EntityMyrmexRoyal.this.m_20185_();
                double d1 = this.f_24976_ - EntityMyrmexRoyal.this.m_20186_();
                double d2 = this.f_24977_ - EntityMyrmexRoyal.this.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = Math.sqrt(d3);
                if (d3 < EntityMyrmexRoyal.this.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    EntityMyrmexRoyal.this.m_20256_(EntityMyrmexRoyal.this.m_20184_().multiply(0.5, 0.5, 0.5));
                } else {
                    EntityMyrmexRoyal.this.m_20256_(EntityMyrmexRoyal.this.m_20184_().add(d0 / d3 * 0.1 * this.f_24978_, d1 / d3 * 0.1 * this.f_24978_, d2 / d3 * 0.1 * this.f_24978_));
                    if (EntityMyrmexRoyal.this.m_5448_() == null) {
                        EntityMyrmexRoyal.this.m_146922_(-((float) Mth.atan2(EntityMyrmexRoyal.this.m_20184_().x, EntityMyrmexRoyal.this.m_20184_().z)) * (180.0F / (float) Math.PI));
                        EntityMyrmexRoyal.this.f_20883_ = EntityMyrmexRoyal.this.m_146908_();
                    } else {
                        double d4 = EntityMyrmexRoyal.this.m_5448_().m_20185_() - EntityMyrmexRoyal.this.m_20185_();
                        double d5 = EntityMyrmexRoyal.this.m_5448_().m_20189_() - EntityMyrmexRoyal.this.m_20189_();
                        EntityMyrmexRoyal.this.m_146922_(-((float) Mth.atan2(d4, d5)) * (180.0F / (float) Math.PI));
                        EntityMyrmexRoyal.this.f_20883_ = EntityMyrmexRoyal.this.m_146908_();
                    }
                }
            }
        }
    }
}