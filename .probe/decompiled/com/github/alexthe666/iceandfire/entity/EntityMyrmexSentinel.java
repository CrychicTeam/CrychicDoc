package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackPlayers;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIDefendHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIFindHidingSpot;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILeaveHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILookAtTradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAITradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWander;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexSentinel extends EntityMyrmexBase {

    public static final Animation ANIMATION_GRAB = Animation.create(15);

    public static final Animation ANIMATION_NIBBLE = Animation.create(10);

    public static final Animation ANIMATION_STING = Animation.create(25);

    public static final Animation ANIMATION_SLASH = Animation.create(25);

    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_sentinel_desert");

    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_sentinel_jungle");

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_sentinel.png");

    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_sentinel.png");

    private static final ResourceLocation TEXTURE_DESERT_HIDDEN = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_sentinel_hidden.png");

    private static final ResourceLocation TEXTURE_JUNGLE_HIDDEN = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_sentinel_hidden.png");

    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(EntityMyrmexSentinel.class, EntityDataSerializers.BOOLEAN);

    public float holdingProgress;

    public float hidingProgress;

    public int visibleTicks = 0;

    public int daylightTicks = 0;

    public EntityMyrmexSentinel(EntityType t, Level worldIn) {
        super(t, worldIn);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel1Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_SENTINEL.get(1) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_SENTINEL.get(1);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel2Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_SENTINEL.get(2) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_SENTINEL.get(2);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getExperienceReward() {
        return 8;
    }

    public Entity getHeldEntity() {
        return this.m_20197_().isEmpty() ? null : (Entity) this.m_20197_().get(0);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        LivingEntity attackTarget = this.m_5448_();
        if (this.visibleTicks > 0) {
            this.visibleTicks--;
        } else {
            this.visibleTicks = 0;
        }
        if (attackTarget != null) {
            this.visibleTicks = 100;
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        boolean holding = this.getHeldEntity() != null;
        boolean hiding = this.isHiding() && !this.hasCustomer();
        if (holding || this.isOnResin() || attackTarget != null || this.visibleTicks > 0) {
            this.setHiding(false);
        }
        if (holding && this.holdingProgress < 20.0F) {
            this.holdingProgress++;
        } else if (!holding && this.holdingProgress > 0.0F) {
            this.holdingProgress--;
        }
        if (hiding) {
            this.m_146922_(this.f_19859_);
        }
        if (hiding && this.hidingProgress < 20.0F) {
            this.hidingProgress++;
        } else if (!hiding && this.hidingProgress > 0.0F) {
            this.hidingProgress--;
        }
        if (this.getHeldEntity() != null) {
            this.setAnimation(ANIMATION_NIBBLE);
            if (this.getAnimationTick() == 5) {
                this.playBiteSound();
                this.getHeldEntity().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() / 6));
            }
        }
        if (this.getAnimation() == ANIMATION_GRAB && attackTarget != null && this.getAnimationTick() == 7) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(attackTarget.m_20191_())) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() / 2));
                if (attackTarget instanceof EntityDragonBase) {
                    if (!((EntityDragonBase) attackTarget).isMobDead()) {
                        attackTarget.m_20329_(this);
                    }
                } else {
                    attackTarget.m_20329_(this);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_SLASH && attackTarget != null && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() <= 20) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(attackTarget.m_20191_())) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() / 4));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && (this.getAnimationTick() == 0 || this.getAnimationTick() == 10)) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && attackTarget != null && (this.getAnimationTick() == 6 || this.getAnimationTick() == 16)) {
            double dist = this.m_20280_(attackTarget);
            if (dist < 18.0) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                attackTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 3));
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new MyrmexAIFindHidingSpot(this));
        this.f_21345_.addGoal(0, new MyrmexAITradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.f_21345_.addGoal(1, new MyrmexAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(3, new MyrmexAILeaveHive(this, 1.0));
        this.f_21345_.addGoal(5, new MyrmexAIWander(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MyrmexAIDefendHive(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, LivingEntity.class, 4, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexSentinel.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Enemy);
            }
        }));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(HIDING, Boolean.FALSE);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 3.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 12.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 3.0);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        if (this.isHiding()) {
            return this.isJungle() ? TEXTURE_JUNGLE_HIDDEN : TEXTURE_DESERT_HIDDEN;
        } else {
            return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
        }
    }

    @Override
    public float getModelScale() {
        return 0.8F;
    }

    @Override
    public int getCasteImportance() {
        return 2;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hiding", this.isHiding());
        tag.putInt("DaylightTicks", this.daylightTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHiding(tag.getBoolean("Hiding"));
        this.daylightTicks = tag.getInt("DaylightTicks");
    }

    @Override
    public boolean shouldLeaveHive() {
        return true;
    }

    @Override
    public boolean shouldEnterHive() {
        return false;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            this.f_20883_ = this.m_146908_();
            float radius = 1.25F;
            float extraY = 0.35F;
            if (this.getAnimation() == ANIMATION_GRAB) {
                int modTick = Mth.clamp(this.getAnimationTick(), 0, 10);
                radius = 3.25F - (float) modTick * 0.2F;
                extraY = (float) modTick * 0.035F;
            }
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            if (passenger.getBbHeight() >= 1.75F) {
                extraY = passenger.getBbHeight() - 2.0F;
            }
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + (double) extraY, this.m_20189_() + extraZ);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ((double) amount >= 1.0 && !this.m_20197_().isEmpty() && this.f_19796_.nextInt(2) == 0) {
            for (Entity entity : this.m_20197_()) {
                entity.stopRiding();
            }
        }
        this.visibleTicks = 300;
        this.setHiding(false);
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        } else if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_SLASH && this.getAnimation() != ANIMATION_GRAB && this.getHeldEntity() == null) {
            if (this.m_217043_().nextInt(2) == 0 && entityIn.getBbWidth() < 2.0F) {
                this.setAnimation(ANIMATION_GRAB);
            } else {
                this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_STING : ANIMATION_SLASH);
            }
            this.visibleTicks = 300;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean needsGaurding() {
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE, ANIMATION_SLASH, ANIMATION_STING, ANIMATION_GRAB, ANIMATION_NIBBLE };
    }

    @Override
    public boolean canMove() {
        return super.canMove() && this.getHeldEntity() == null && !this.isHiding();
    }

    public boolean shouldRiderSit() {
        return false;
    }

    public boolean isHiding() {
        return this.f_19804_.get(HIDING);
    }

    public void setHiding(boolean hiding) {
        this.f_19804_.set(HIDING, hiding);
    }

    @Override
    public int getVillagerXp() {
        return 4;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public boolean isClientSide() {
        return this.m_9236_().isClientSide;
    }
}