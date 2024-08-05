package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.MooseAIJostle;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class EntityMoose extends Animal implements IAnimatedEntity {

    public static final Animation ANIMATION_EAT_GRASS = Animation.create(30);

    public static final Animation ANIMATION_ATTACK = Animation.create(15);

    private static final int DAY = 24000;

    private static final EntityDataAccessor<Boolean> ANTLERED = SynchedEntityData.defineId(EntityMoose.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> JOSTLING = SynchedEntityData.defineId(EntityMoose.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> JOSTLE_ANGLE = SynchedEntityData.defineId(EntityMoose.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> JOSTLER_UUID = SynchedEntityData.defineId(EntityMoose.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> SNOWY = SynchedEntityData.defineId(EntityMoose.class, EntityDataSerializers.BOOLEAN);

    public float prevJostleAngle;

    public float prevJostleProgress;

    public float jostleProgress;

    public boolean jostleDirection;

    public int jostleTimer = 0;

    public boolean instantlyTriggerJostleAI = false;

    public int jostleCooldown = 100 + this.f_19796_.nextInt(40);

    public int timeUntilAntlerDrop = 168000 + this.f_19796_.nextInt(3) * 24000;

    private int animationTick;

    private Animation currentAnimation;

    private int snowTimer = 0;

    private boolean permSnow = false;

    protected EntityMoose(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_274367_(1.1F);
    }

    public static boolean canMooseSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockState blockstate = worldIn.m_8055_(pos.below());
        return blockstate.m_60713_(Blocks.GRASS_BLOCK) || blockstate.m_60713_(Blocks.SNOW) || blockstate.m_60713_(Blocks.SNOW_BLOCK) && worldIn.m_45524_(pos, 0) > 8;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 55.0).add(Attributes.ATTACK_DAMAGE, 7.5).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mooseSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new MooseAIJostle(this));
        this.f_21345_.addGoal(3, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(4, new MeleeAttackGoal(this, 1.1, true));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new TemptGoal(this, 1.1, Ingredient.of(Items.DANDELION), false));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 120, 1.0, 14, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 6) {
            for (int lvt_3_1_ = 0; lvt_3_1_ < 7; lvt_3_1_++) {
                double lvt_4_1_ = this.f_19796_.nextGaussian() * 0.02;
                double lvt_6_1_ = this.f_19796_.nextGaussian() * 0.02;
                double lvt_8_1_ = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), lvt_4_1_, lvt_6_1_, lvt_8_1_);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        if (stack.getItem() == Items.DANDELION && !this.m_27593_() && this.m_146764_() == 0) {
            if (this.m_217043_().nextInt(5) == 0) {
                return true;
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        if (!this.m_6162_()) {
            super.m_6710_(entitylivingbaseIn);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ANTLERED, true);
        this.f_19804_.define(JOSTLING, false);
        this.f_19804_.define(SNOWY, false);
        this.f_19804_.define(JOSTLE_ANGLE, 0.0F);
        this.f_19804_.define(JOSTLER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSnowy(compound.getBoolean("Snowy"));
        if (compound.contains("AntlerTime")) {
            this.timeUntilAntlerDrop = compound.getInt("AntlerTime");
        }
        this.setAntlered(compound.getBoolean("Antlered"));
        this.jostleCooldown = compound.getInt("JostlingCooldown");
        this.permSnow = compound.getBoolean("SnowPerm");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Snowy", this.isSnowy());
        compound.putBoolean("SnowPerm", this.permSnow);
        compound.putInt("AntlerTime", this.timeUntilAntlerDrop);
        compound.putBoolean("Antlered", this.isAntlered());
        compound.putInt("JostlingCooldown", this.jostleCooldown);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevJostleProgress = this.jostleProgress;
        this.prevJostleAngle = this.getJostleAngle();
        if (this.isJostling()) {
            if (this.jostleProgress < 5.0F) {
                this.jostleProgress++;
            }
        } else if (this.jostleProgress > 0.0F) {
            this.jostleProgress--;
        }
        if (this.jostleCooldown > 0) {
            this.jostleCooldown--;
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && this.m_217043_().nextInt(120) == 0 && (this.m_5448_() == null || !this.m_5448_().isAlive()) && !this.isJostling() && this.getJostlingPartnerUUID() == null && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK) && this.m_217043_().nextInt(3) == 0) {
            this.setAnimation(ANIMATION_EAT_GRASS);
        }
        if (this.timeUntilAntlerDrop > 0) {
            this.timeUntilAntlerDrop--;
        }
        if (this.timeUntilAntlerDrop == 0) {
            if (this.isAntlered()) {
                this.setAntlered(false);
                this.m_19983_(new ItemStack(AMItemRegistry.MOOSE_ANTLER.get()));
                this.timeUntilAntlerDrop = 48000 + this.f_19796_.nextInt(3) * 24000;
            } else {
                this.setAntlered(true);
                this.timeUntilAntlerDrop = 168000 + this.f_19796_.nextInt(3) * 24000;
            }
        }
        if (this.m_5448_() != null && this.m_5448_().isAlive()) {
            if (this.isJostling()) {
                this.setJostling(false);
            }
            if (!this.m_9236_().isClientSide && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 8) {
                float dmg = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
                if (!this.isAntlered()) {
                    dmg = 3.0F;
                }
                if (this.m_5448_() instanceof Wolf || this.m_5448_() instanceof EntityOrca) {
                    dmg = 2.0F;
                }
                this.m_5448_().knockback(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), dmg);
            }
        }
        if (this.snowTimer > 0) {
            this.snowTimer--;
        }
        if (this.snowTimer == 0 && !this.m_9236_().isClientSide) {
            this.snowTimer = 200 + this.f_19796_.nextInt(400);
            if (this.isSnowy()) {
                if (!this.permSnow && (!this.m_9236_().isClientSide || this.m_20094_() > 0 || this.m_20072_() || !EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_().above()))) {
                    this.setSnowy(false);
                }
            } else if (!this.m_9236_().isClientSide && EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_())) {
                this.setSnowy(true);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity instanceof EntityOrca || entity instanceof Wolf) {
                amount = (amount + 1.0F) * 3.0F;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.MOOSE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MOOSE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MOOSE_HURT.get();
    }

    public boolean isAntlered() {
        return this.f_19804_.get(ANTLERED);
    }

    public void setAntlered(boolean anters) {
        this.f_19804_.set(ANTLERED, anters);
    }

    public boolean isJostling() {
        return this.f_19804_.get(JOSTLING);
    }

    public void setJostling(boolean jostle) {
        this.f_19804_.set(JOSTLING, jostle);
    }

    public float getJostleAngle() {
        return this.f_19804_.get(JOSTLE_ANGLE);
    }

    public void setJostleAngle(float scale) {
        this.f_19804_.set(JOSTLE_ANGLE, scale);
    }

    @Nullable
    public UUID getJostlingPartnerUUID() {
        return (UUID) this.f_19804_.get(JOSTLER_UUID).orElse(null);
    }

    public void setJostlingPartnerUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(JOSTLER_UUID, Optional.ofNullable(uniqueId));
    }

    public boolean isSnowy() {
        return this.f_19804_.get(SNOWY);
    }

    public void setSnowy(boolean honeyed) {
        this.f_19804_.set(SNOWY, honeyed);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (item == Items.SNOW && !this.isSnowy() && !this.m_9236_().isClientSide) {
            this.m_142075_(player, hand, itemstack);
            this.permSnow = true;
            this.setSnowy(true);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SNOW_PLACE, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else if (item instanceof ShovelItem && this.isSnowy() && !this.m_9236_().isClientSide) {
            this.permSnow = false;
            if (!player.isCreative()) {
                itemstack.hurt(1, this.m_217043_(), player instanceof ServerPlayer ? (ServerPlayer) player : null);
            }
            this.setSnowy(false);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SNOW_BREAK, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Nullable
    public Entity getJostlingPartner() {
        UUID id = this.getJostlingPartnerUUID();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setJostlingPartner(@Nullable Entity jostlingPartner) {
        if (jostlingPartner == null) {
            this.setJostlingPartnerUUID(null);
        } else {
            this.setJostlingPartnerUUID(jostlingPartner.getUUID());
        }
    }

    public void pushBackJostling(EntityMoose entityMoose, float strength) {
        this.applyKnockbackFromMoose(strength, entityMoose.m_20185_() - this.m_20185_(), entityMoose.m_20189_() - this.m_20189_());
    }

    private void applyKnockbackFromMoose(float strength, double ratioX, double ratioZ) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(this, strength, ratioX, ratioZ);
        if (!event.isCanceled()) {
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            if (!(strength <= 0.0F)) {
                this.f_19812_ = true;
                Vec3 vector3d = this.m_20184_();
                Vec3 vector3d1 = new Vec3(ratioX, 0.0, ratioZ).normalize().scale((double) strength);
                this.m_20334_(vector3d.x / 2.0 - vector3d1.x, 0.3F, vector3d.z / 2.0 - vector3d1.z);
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK, ANIMATION_EAT_GRASS };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.MOOSE.get().create(serverWorld);
    }

    public boolean canJostleWith(EntityMoose moose) {
        return !moose.isJostling() && moose.isAntlered() && moose.getAnimation() == NO_ANIMATION && !moose.m_6162_() && moose.getJostlingPartnerUUID() == null && moose.jostleCooldown == 0;
    }

    public void playJostleSound() {
        this.m_5496_(AMSoundRegistry.MOOSE_JOSTLE.get(), this.m_6100_(), this.m_6121_());
    }
}