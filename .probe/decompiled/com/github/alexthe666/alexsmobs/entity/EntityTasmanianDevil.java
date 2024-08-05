package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityTasmanianDevil extends Animal implements IAnimatedEntity, ITargetsDroppedItems {

    private int animationTick;

    private Animation currentAnimation;

    public static final Animation ANIMATION_HOWL = Animation.create(40);

    public static final Animation ANIMATION_ATTACK = Animation.create(8);

    private static final EntityDataAccessor<Boolean> BASKING = SynchedEntityData.defineId(EntityTasmanianDevil.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityTasmanianDevil.class, EntityDataSerializers.BOOLEAN);

    public float prevBaskProgress;

    public float prevSitProgress;

    public float baskProgress;

    public float sitProgress;

    private int sittingTime;

    private int maxSitTime;

    private int scareMobsTime = 0;

    protected EntityTasmanianDevil(EntityType type, Level world) {
        super(type, world);
    }

    public boolean shouldMove() {
        return !this.isSitting() && !this.isBasking();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.TASMANIAN_DEVIL_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TASMANIAN_DEVIL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TASMANIAN_DEVIL_HURT.get();
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.5, true));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.1, Ingredient.of(Items.ROTTEN_FLESH), false) {

            @Override
            public void tick() {
                super.tick();
                if (EntityTasmanianDevil.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    EntityTasmanianDevil.this.setBasking(false);
                    EntityTasmanianDevil.this.setSitting(false);
                }
            }
        });
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityTasmanianDevil.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Animal.class, 120, false, false, p_213487_0_ -> p_213487_0_ instanceof Chicken || p_213487_0_ instanceof Rabbit));
        this.f_21346_.addGoal(3, new CreatureAITargetItems(this, false, 30));
    }

    public void killed(ServerLevel world, LivingEntity entity) {
        if (this.m_217043_().nextBoolean() && (entity instanceof Animal || entity.getMobType() == MobType.UNDEAD)) {
            entity.m_19983_(new ItemStack(Items.BONE));
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (!this.shouldMove()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    public void setSitting(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BASKING, false);
        this.f_19804_.define(SITTING, false);
    }

    public boolean isBasking() {
        return this.f_19804_.get(BASKING);
    }

    public void setBasking(boolean basking) {
        this.f_19804_.set(BASKING, basking);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat() && stack.getItem() != Items.ROTTEN_FLESH;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBaskProgress = this.baskProgress;
        this.prevSitProgress = this.sitProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.isBasking()) {
            if (this.baskProgress < 5.0F) {
                this.baskProgress++;
            }
        } else if (this.baskProgress > 0.0F) {
            this.baskProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 5 && this.m_142582_(this.m_5448_())) {
                float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
                this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * 0.02F), 0.0, (double) (Mth.cos(f1) * 0.02F)));
                this.m_5448_().knockback(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
            if ((this.isSitting() || this.isBasking()) && ++this.sittingTime > this.maxSitTime) {
                this.setSitting(false);
                this.setBasking(false);
                this.sittingTime = 0;
                this.maxSitTime = 75 + this.f_19796_.nextInt(50);
            }
            if (this.m_20184_().lengthSqr() < 0.03 && this.getAnimation() == NO_ANIMATION && !this.isBasking() && !this.isSitting() && this.f_19796_.nextInt(100) == 0) {
                this.sittingTime = 0;
                this.maxSitTime = 100 + this.f_19796_.nextInt(550);
                if (this.m_217043_().nextBoolean()) {
                    this.setSitting(true);
                    this.setBasking(false);
                } else {
                    this.setSitting(false);
                    this.setBasking(true);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_HOWL && this.getAnimationTick() == 1) {
            this.m_146850_(GameEvent.ENTITY_ROAR);
            this.m_5496_(AMSoundRegistry.TASMANIAN_DEVIL_ROAR.get(), this.m_6121_() * 2.0F, this.m_6100_());
        }
        if (this.getAnimation() == ANIMATION_HOWL && this.getAnimationTick() > 3) {
            this.scareMobsTime = 40;
        }
        if (this.scareMobsTime > 0) {
            for (Monster e : this.m_9236_().m_45976_(Monster.class, this.m_20191_().inflate(16.0, 8.0, 16.0))) {
                e.m_6710_(null);
                e.m_6703_(null);
                if (this.scareMobsTime % 5 == 0) {
                    Vec3 vec = LandRandomPos.getPosAway(e, 20, 7, this.m_20182_());
                    if (vec != null) {
                        e.m_21573_().moveTo(vec.x, vec.y, vec.z, 1.5);
                    }
                }
            }
            this.scareMobsTime--;
        }
        if (this.m_5448_() != null && this.m_5448_().isAlive() && (this.m_21188_() == null || !this.m_21188_().isAlive())) {
            this.m_6703_(this.m_5448_());
        }
        if ((this.isSitting() || this.isBasking()) && (this.m_5448_() != null || this.m_27593_())) {
            this.setSitting(false);
            this.setBasking(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (item == Items.ROTTEN_FLESH && this.getAnimation() != ANIMATION_HOWL) {
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.FOX_EAT, this.m_6121_(), this.m_6100_());
            this.m_19983_(item.getCraftingRemainingItem(itemstack));
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setAnimation(ANIMATION_HOWL);
            return InteractionResult.SUCCESS;
        } else {
            return type;
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
        if (animation == ANIMATION_HOWL) {
            this.setSitting(true);
            this.setBasking(false);
            this.maxSitTime = Math.max(25, this.maxSitTime);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK, ANIMATION_HOWL };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.TASMANIAN_DEVIL.get().create(serverWorld);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat() || stack.getItem() == Items.BONE;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_146850_(GameEvent.EAT);
        if (e.getItem().getItem() == Items.BONE) {
            this.dropBonemeal();
            this.m_5496_(SoundEvents.SKELETON_STEP, this.m_6121_(), this.m_6100_());
        } else {
            this.m_5496_(SoundEvents.FOX_EAT, this.m_6121_(), this.m_6100_());
            this.m_5634_(5.0F);
        }
    }

    public void dropBonemeal() {
        ItemStack stack = new ItemStack(Items.BONE_MEAL);
        for (int i = 0; i < 3 + this.f_19796_.nextInt(1); i++) {
            this.m_19983_(stack);
        }
    }
}