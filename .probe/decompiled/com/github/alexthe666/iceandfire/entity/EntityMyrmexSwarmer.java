package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.EntityAIAttackMeleeNoCooldown;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIFollowSummoner;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAISummonerHurtByTarget;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAISummonerHurtTarget;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWander;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexSwarmer extends EntityMyrmexRoyal {

    private static final EntityDataAccessor<Optional<UUID>> SUMMONER_ID = SynchedEntityData.defineId(EntityMyrmexSwarmer.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> TICKS_ALIVE = SynchedEntityData.defineId(EntityMyrmexSwarmer.class, EntityDataSerializers.INT);

    public EntityMyrmexSwarmer(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new EntityMyrmexRoyal.FlyMoveHelper(this);
        this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.FLYING);
        this.switchNavigator(false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 0.0);
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    @Override
    protected void switchNavigator(boolean onLand) {
    }

    @Override
    protected double attackDistance() {
        return 25.0;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MyrmexAIFollowSummoner(this, 1.0, 10.0F, 5.0F));
        this.f_21345_.addGoal(2, new EntityMyrmexRoyal.AIFlyAtTarget());
        this.f_21345_.addGoal(3, new EntityMyrmexRoyal.AIFlyRandom());
        this.f_21345_.addGoal(4, new EntityAIAttackMeleeNoCooldown(this, 1.0, true));
        this.f_21345_.addGoal(5, new MyrmexAIWander(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new MyrmexAISummonerHurtByTarget(this));
        this.f_21346_.addGoal(3, new MyrmexAISummonerHurtTarget(this));
    }

    @Override
    protected void doPush(Entity entityIn) {
        if (entityIn instanceof EntityMyrmexSwarmer) {
            super.m_7324_(entityIn);
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SUMMONER_ID, Optional.empty());
        this.f_19804_.define(TICKS_ALIVE, 0);
    }

    @Nullable
    public LivingEntity getSummoner() {
        try {
            UUID uuid = this.getSummonerUUID();
            return uuid == null ? null : this.m_9236_().m_46003_(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (entityIn == null) {
            return false;
        } else if (this.getSummonerUUID() != null && (!(entityIn instanceof EntityMyrmexSwarmer) || ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() != null)) {
            if (entityIn instanceof TamableAnimal) {
                UUID ownerID = ((TamableAnimal) entityIn).getOwnerUUID();
                return ownerID != null && ownerID.equals(this.getSummonerUUID());
            } else {
                return entityIn.getUUID().equals(this.getSummonerUUID()) || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() != null && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID().equals(this.getSummonerUUID());
            }
        } else {
            return false;
        }
    }

    public void setSummonerID(@Nullable UUID uuid) {
        this.f_19804_.set(SUMMONER_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getSummonerUUID() == null) {
            compound.putString("SummonerUUID", "");
        } else {
            compound.putString("SummonerUUID", this.getSummonerUUID().toString());
        }
        compound.putInt("SummonTicks", this.getTicksAlive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        String s = "";
        if (compound.hasUUID("SummonerUUID")) {
            s = compound.getString("SummonerUUID");
        }
        if (!s.isEmpty()) {
            try {
                this.setSummonerID(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
        this.setTicksAlive(compound.getInt("SummonTicks"));
    }

    public void setSummonedBy(Player player) {
        this.setSummonerID(player.m_20148_());
    }

    @Nullable
    public UUID getSummonerUUID() {
        return (UUID) this.f_19804_.get(SUMMONER_ID).orElse(null);
    }

    public int getTicksAlive() {
        return this.f_19804_.get(TICKS_ALIVE);
    }

    public void setTicksAlive(int ticks) {
        this.f_19804_.set(TICKS_ALIVE, ticks);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setFlying(true);
        boolean flying = this.isFlying() && !this.m_20096_();
        this.setTicksAlive(this.getTicksAlive() + 1);
        if (flying) {
            this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            if (this.f_21342_.getWantedY() > this.m_20186_()) {
                this.m_20256_(this.m_20184_().add(0.0, 0.08, 0.0));
            }
        }
        if (this.m_20096_()) {
            this.m_20256_(this.m_20184_().add(0.0, 0.2, 0.0));
        }
        if (this.m_5448_() != null) {
            this.f_21342_.setWantedPosition(this.m_5448_().m_20185_(), this.m_5448_().m_20191_().minY, this.m_5448_().m_20189_(), 1.0);
            if (this.getAttackBounds().intersects(this.m_5448_().m_20191_())) {
                this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_BITE : ANIMATION_STING);
            }
        }
        if (this.getTicksAlive() > 1800) {
            this.m_6074_();
        }
        if (this.getAnimation() == ANIMATION_BITE && this.m_5448_() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            double dist = this.m_20280_(this.m_5448_());
            if (dist < this.attackDistance()) {
                this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.m_5448_() != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            double dist = this.m_20280_(this.m_5448_());
            if (dist < this.attackDistance()) {
                this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * 2));
                if (this.m_5448_() != null) {
                    this.m_5448_().addEffect(new MobEffectInstance(MobEffects.POISON, 70, 1));
                }
            }
        }
    }

    @Override
    public int getGrowthStage() {
        return 2;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return null;
    }

    @Override
    public float getModelScale() {
        return 0.25F;
    }

    @Override
    public boolean shouldHaveNormalAI() {
        return false;
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    @Override
    public boolean isBreedingSeason() {
        return false;
    }

    public boolean shouldAttackEntity(LivingEntity attacker, LivingEntity LivingEntity) {
        return !this.isAlliedTo(attacker);
    }
}