package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackPlayers;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIDefendHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIEscortEntity;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIFindGaurdingEntity;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILeaveHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILookAtTradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIMoveThroughHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIReEnterHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAITradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWander;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexSoldier extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_STING = Animation.create(15);

    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_soldier_desert");

    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_soldier_jungle");

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_soldier.png");

    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_soldier.png");

    public EntityMyrmexBase guardingEntity = null;

    public EntityMyrmexSoldier(EntityType<EntityMyrmexSoldier> t, Level worldIn) {
        super(t, worldIn);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel1Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_SOLDIER.get(1) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_SOLDIER.get(1);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel2Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_SOLDIER.get(2) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_SOLDIER.get(2);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getExperienceReward() {
        return 5;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.guardingEntity != null) {
            this.guardingEntity.isBeingGuarded = true;
            this.isEnteringHive = this.guardingEntity.isEnteringHive;
            if (!this.guardingEntity.m_6084_()) {
                this.guardingEntity.isBeingGuarded = false;
                this.guardingEntity = null;
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new MyrmexAITradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.f_21345_.addGoal(1, new MyrmexAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(2, new MyrmexAIEscortEntity(this, 1.0));
        this.f_21345_.addGoal(2, new MyrmexAIReEnterHive(this, 1.0));
        this.f_21345_.addGoal(4, new MyrmexAILeaveHive(this, 1.0));
        this.f_21345_.addGoal(5, new MyrmexAIMoveThroughHive(this, 1.0));
        this.f_21345_.addGoal(6, new MyrmexAIWander(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MyrmexAIDefendHive(this));
        this.f_21346_.addGoal(2, new MyrmexAIFindGaurdingEntity(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexSoldier.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Enemy);
            }
        }));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 2.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 6.0);
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
        return 0.8F;
    }

    @Override
    public int getCasteImportance() {
        return 1;
    }

    @Override
    public boolean shouldLeaveHive() {
        return false;
    }

    @Override
    public boolean shouldEnterHive() {
        return this.guardingEntity == null || !this.guardingEntity.canSeeSky() || this.guardingEntity.shouldEnterHive();
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

    @Override
    public boolean needsGaurding() {
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING };
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