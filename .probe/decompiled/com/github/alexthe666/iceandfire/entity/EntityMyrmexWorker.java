package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackPlayers;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIDefendHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIForage;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIForageForItems;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILeaveHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILookAtTradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIMoveThroughHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIPickupBabies;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIReEnterHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIStoreBabies;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIStoreItems;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAITradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWander;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexWorker extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_STING = Animation.create(15);

    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_worker_desert");

    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_worker_jungle");

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_worker.png");

    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_worker.png");

    public boolean keepSearching = true;

    public EntityMyrmexWorker(EntityType<EntityMyrmexWorker> t, Level worldIn) {
        super(t, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 4.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.m_9236_().isClientSide && !this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.die(cause);
    }

    @Override
    public int getExperienceReward() {
        return 3;
    }

    @Override
    public boolean isSmallerThanBlock() {
        return true;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.m_21120_(InteractionHand.MAIN_HAND).getItem() instanceof ItemMyrmexEgg) {
            boolean isJungle = this.m_21120_(InteractionHand.MAIN_HAND).getItem() == IafItemRegistry.MYRMEX_JUNGLE_EGG.get();
            CompoundTag tag = this.m_21120_(InteractionHand.MAIN_HAND).getTag();
            int metadata = 0;
            if (tag != null) {
                metadata = tag.getInt("EggOrdinal");
            }
            EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), this.m_9236_());
            egg.m_20359_(this);
            egg.setJungle(isJungle);
            egg.setMyrmexCaste(metadata);
            if (!this.m_9236_().isClientSide) {
                this.m_9236_().m_7967_(egg);
            }
            egg.m_20329_(this);
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (!this.m_20197_().isEmpty()) {
            for (Entity entity : this.m_20197_()) {
                if (entity instanceof EntityMyrmexBase && ((EntityMyrmexBase) entity).getGrowthStage() >= 2) {
                    entity.stopRiding();
                }
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new MyrmexAITradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.f_21345_.addGoal(1, new MyrmexAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(2, new MyrmexAIStoreBabies(this, 1.0));
        this.f_21345_.addGoal(3, new MyrmexAIStoreItems(this, 1.0));
        this.f_21345_.addGoal(4, new MyrmexAIReEnterHive(this, 1.0));
        this.f_21345_.addGoal(4, new MyrmexAILeaveHive(this, 1.0));
        this.f_21345_.addGoal(6, new MyrmexAIForage(this, 2));
        this.f_21345_.addGoal(7, new MyrmexAIMoveThroughHive(this, 1.0));
        this.f_21345_.addGoal(8, new MyrmexAIWander(this, 1.0));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MyrmexAIDefendHive(this));
        this.f_21346_.addGoal(2, new MyrmexAIForageForItems(this));
        this.f_21346_.addGoal(3, new MyrmexAIPickupBabies(this));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.f_21346_.addGoal(5, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return EntityMyrmexWorker.this.m_21205_().isEmpty() && entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexWorker.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Enemy);
            }
        }));
    }

    @Override
    public boolean shouldWander() {
        return super.shouldWander() && this.canSeeSky();
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel1Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_WORKER.get(1) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_WORKER.get(1);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel2Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_WORKER.get(2) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_WORKER.get(2);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 0.6F;
    }

    @Override
    public boolean shouldLeaveHive() {
        return !this.holdingSomething();
    }

    @Override
    public boolean shouldEnterHive() {
        return this.holdingSomething() || !this.m_9236_().isDay() && !IafConfig.myrmexHiveIgnoreDaytime;
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return !this.shouldLeaveHive() && !this.holdingSomething();
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        } else if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
            this.m_21335_(entityIn);
            boolean flag = entityIn.hurt(this.m_9236_().damageSources().mobAttack(this), f);
            if (this.getAnimation() == ANIMATION_STING && flag) {
                this.playStingSound();
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
                    this.m_6710_((LivingEntity) entityIn);
                }
            } else {
                this.playBiteSound();
            }
            if (!this.m_9236_().isClientSide && this.m_217043_().nextInt(3) == 0 && this.m_21120_(InteractionHand.MAIN_HAND) != ItemStack.EMPTY) {
                this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
            if (!this.m_20197_().isEmpty()) {
                for (Entity entity : this.m_20197_()) {
                    entity.stopRiding();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean holdingSomething() {
        return this.getHeldEntity() != null || !this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() || this.m_5448_() != null;
    }

    public boolean holdingBaby() {
        return this.getHeldEntity() != null && (this.getHeldEntity() instanceof EntityMyrmexBase || this.getHeldEntity() instanceof EntityMyrmexEgg);
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING };
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            this.f_20883_ = this.m_146908_();
            float radius = 1.05F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + 0.25, this.m_20189_() + extraZ);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ((double) amount >= 1.0 && !this.m_9236_().isClientSide && this.m_217043_().nextInt(3) == 0 && this.m_21120_(InteractionHand.MAIN_HAND) != ItemStack.EMPTY) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        if ((double) amount >= 1.0 && !this.m_20197_().isEmpty()) {
            for (Entity entity : this.m_20197_()) {
                entity.stopRiding();
            }
        }
        return super.hurt(source, amount);
    }

    public Entity getHeldEntity() {
        return this.m_20197_().isEmpty() ? null : (Entity) this.m_20197_().get(0);
    }

    public void onPickupItem(ItemEntity itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (item == IafItemRegistry.MYRMEX_JUNGLE_RESIN.get() && this.isJungle() || item == IafItemRegistry.MYRMEX_DESERT_RESIN.get() && !this.isJungle()) {
            Player owner = null;
            try {
                if (itemEntity.getOwner() != null) {
                    owner = (Player) itemEntity.getOwner();
                }
            } catch (Exception var5) {
                IceAndFire.LOGGER.warn("Myrmex picked up resin that wasn't thrown!");
            }
            if (owner != null && this.getHive() != null) {
                this.getHive().modifyPlayerReputation(owner.m_20148_(), 5);
                this.m_5496_(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(new ExperienceOrb(this.m_9236_(), owner.m_20185_(), owner.m_20186_(), owner.m_20189_(), 1 + this.f_19796_.nextInt(3)));
                }
            }
        }
    }

    @Override
    public int getVillagerXp() {
        return 0;
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