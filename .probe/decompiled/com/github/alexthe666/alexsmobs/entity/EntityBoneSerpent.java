package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentAIFindLava;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentAIJump;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentAIMeleeJump;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.LavaAndWaterAIRandomSwimming;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityBoneSerpent extends Monster {

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityBoneSerpent.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final Predicate<LivingEntity> NOT_RIDING_STRADDLEBOARD_FRIENDLY = entity -> entity.isAlive() && (entity.m_20202_() == null || !(entity.m_20202_() instanceof EntityStraddleboard) || !((EntityStraddleboard) entity.m_20202_()).shouldSerpentFriend());

    private static final Predicate<EntityStraddleboard> STRADDLEBOARD_FRIENDLY = entity -> entity.m_20160_() && entity.shouldSerpentFriend();

    public int jumpCooldown = 0;

    private boolean isLandNavigator;

    private int boardCheckCooldown = 0;

    private EntityStraddleboard boardToBoast = null;

    protected EntityBoneSerpent(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.switchNavigator(false);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.boneSeprentSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.BONE_SERPENT_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BONE_SERPENT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BONE_SERPENT_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 25.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MOVEMENT_SPEED, 1.45F);
    }

    @Override
    public int getMaxFallDistance() {
        return 256;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() == MobEffects.WITHER ? false : super.m_7301_(potioneffectIn);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        if (!worldIn.m_8055_(pos).m_60819_().is(FluidTags.WATER) && !worldIn.m_8055_(pos).m_60819_().is(FluidTags.LAVA)) {
            return this.m_20077_() ? Float.NEGATIVE_INFINITY : 0.0F;
        } else {
            return 10.0F;
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public static boolean canBoneSerpentSpawn(EntityType<EntityBoneSerpent> p_234314_0_, LevelAccessor p_234314_1_, MobSpawnType p_234314_2_, BlockPos p_234314_3_, RandomSource p_234314_4_) {
        BlockPos.MutableBlockPos blockpos$mutable = p_234314_3_.mutable();
        do {
            blockpos$mutable.move(Direction.UP);
        } while (p_234314_1_.m_6425_(blockpos$mutable).is(FluidTags.LAVA));
        return p_234314_1_.m_8055_(blockpos$mutable).m_60795_();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreathAirGoal(this));
        this.f_21345_.addGoal(0, new BoneSerpentAIFindLava(this));
        this.f_21345_.addGoal(1, new BoneSerpentAIMeleeJump(this));
        this.f_21345_.addGoal(2, new BoneSerpentAIJump(this, 10));
        this.f_21345_.addGoal(3, new LavaAndWaterAIRandomSwimming(this, 1.0, 8));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        if (!AMConfig.neutralBoneSerpents) {
            this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, 10, true, false, NOT_RIDING_STRADDLEBOARD_FRIENDLY));
            this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, AbstractVillager.class, 10, true, false, NOT_RIDING_STRADDLEBOARD_FRIENDLY));
        }
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, WitherSkeleton.class, 10, true, false, NOT_RIDING_STRADDLEBOARD_FRIENDLY));
        this.f_21346_.addGoal(5, new EntityAINearestTarget3D(this, EntitySoulVulture.class, 10, true, false, NOT_RIDING_STRADDLEBOARD_FRIENDLY));
    }

    @Override
    public void travel(Vec3 travelVector) {
        boolean liquid = this.m_20077_() || this.m_20069_();
        if (this.m_21515_() && liquid) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntityBoneSerpent.BoneSerpentMoveController(this);
            this.f_21344_ = new BoneSerpentPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        entities.stream().filter(entity -> !(entity instanceof EntityBoneSerpentPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.m_6673_(source);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CHILD_UUID, Optional.empty());
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.f_19817_ = false;
        boolean ground = !this.m_20077_() && !this.m_20069_() && this.m_20096_();
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
            float f2 = -((float) this.m_20184_().y * (180.0F / (float) Math.PI));
            this.m_146926_(f2);
        }
        if (ground) {
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        } else if (this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!this.m_9236_().isClientSide) {
            Entity child = this.getChild();
            if (child == null) {
                LivingEntity partParent = this;
                int segments = 7 + this.m_217043_().nextInt(8);
                for (int i = 0; i < segments; i++) {
                    EntityBoneSerpentPart part = new EntityBoneSerpentPart(AMEntityRegistry.BONE_SERPENT_PART.get(), partParent, 0.9F, 180.0F, 0.0F);
                    part.setParent(partParent);
                    part.setBodyIndex(i);
                    if (partParent == this) {
                        this.setChildId(part.m_20148_());
                    }
                    part.setInitialPartPos(this);
                    partParent = part;
                    if (i == segments - 1) {
                        part.setTail(true);
                    }
                    this.m_9236_().m_7967_(part);
                }
            }
            if (this.boardCheckCooldown > 0) {
                this.boardCheckCooldown--;
            } else {
                this.boardCheckCooldown = 100 + this.f_19796_.nextInt(150);
                List<EntityStraddleboard> list = this.m_9236_().m_6443_(EntityStraddleboard.class, this.m_20191_().inflate(100.0, 15.0, 100.0), STRADDLEBOARD_FRIENDLY);
                EntityStraddleboard closestBoard = null;
                for (EntityStraddleboard board : list) {
                    if (closestBoard == null || this.m_20270_(closestBoard) > this.m_20270_(board)) {
                        closestBoard = board;
                    }
                }
                this.boardToBoast = closestBoard;
            }
            if (this.boardToBoast != null) {
                if (this.m_20270_(this.boardToBoast) > 200.0F) {
                    this.boardToBoast = null;
                } else {
                    if (this.jumpCooldown == 0 && (this.m_20077_() || this.m_20069_()) && this.m_20270_(this.boardToBoast) < 15.0F) {
                        float up = 0.7F + this.m_217043_().nextFloat() * 0.8F;
                        Vec3 vector3d1 = this.m_20154_();
                        this.m_20256_(this.m_20184_().add(vector3d1.x() * 0.6, (double) up, vector3d1.y() * 0.6));
                        this.m_21573_().stop();
                        this.jumpCooldown = this.m_217043_().nextInt(300) + 100;
                    }
                    if (this.m_20270_(this.boardToBoast) > 5.0F) {
                        this.m_21573_().moveTo(this.boardToBoast, 1.5);
                    } else {
                        this.m_21573_().stop();
                    }
                }
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    static class BoneSerpentMoveController extends MoveControl {

        private final EntityBoneSerpent dolphin;

        public BoneSerpentMoveController(EntityBoneSerpent dolphinIn) {
            super(dolphinIn);
            this.dolphin = dolphinIn;
        }

        @Override
        public void tick() {
            if (this.dolphin.m_20069_() || this.dolphin.m_20077_()) {
                this.dolphin.m_20256_(this.dolphin.m_20184_().add(0.0, 0.005, 0.0));
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.dolphin.m_21573_().isDone()) {
                double d0 = this.f_24975_ - this.dolphin.m_20185_();
                double d1 = this.f_24976_ - this.dolphin.m_20186_();
                double d2 = this.f_24977_ - this.dolphin.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < 2.5000003E-7F) {
                    this.f_24974_.setZza(0.0F);
                } else {
                    float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.dolphin.m_146922_(this.m_24991_(this.dolphin.m_146908_(), f, 10.0F));
                    this.dolphin.f_20883_ = this.dolphin.m_146908_();
                    this.dolphin.f_20885_ = this.dolphin.m_146908_();
                    float f1 = (float) (this.f_24978_ * this.dolphin.m_21133_(Attributes.MOVEMENT_SPEED));
                    if (!this.dolphin.m_20069_() && !this.dolphin.m_20077_()) {
                        this.dolphin.m_7910_(f1 * 0.1F);
                    } else {
                        this.dolphin.m_7910_(f1 * 0.02F);
                        float f2 = -((float) (Mth.atan2(d1, (double) Mth.sqrt((float) (d0 * d0 + d2 * d2))) * 180.0F / (float) Math.PI));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.m_20256_(this.dolphin.m_20184_().add(0.0, (double) this.dolphin.m_6113_() * d1 * 0.6, 0.0));
                        this.dolphin.m_146926_(this.m_24991_(this.dolphin.m_146909_(), f2, 1.0F));
                        float f3 = Mth.cos(this.dolphin.m_146909_() * (float) (Math.PI / 180.0));
                        float f4 = Mth.sin(this.dolphin.m_146909_() * (float) (Math.PI / 180.0));
                        this.dolphin.f_20902_ = f3 * f1;
                        this.dolphin.f_20901_ = -f4 * f1;
                    }
                }
            } else {
                this.dolphin.m_7910_(0.0F);
                this.dolphin.m_21570_(0.0F);
                this.dolphin.m_21567_(0.0F);
                this.dolphin.m_21564_(0.0F);
            }
        }
    }
}