package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIAttackPlayers;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIDefendHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAILookAtTradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIReEnterHive;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAITradePlayer;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexAIWanderHiveCenter;
import com.github.alexthe666.iceandfire.entity.ai.MyrmexQueenAIWander;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexQueen extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_STING = Animation.create(15);

    public static final Animation ANIMATION_EGG = Animation.create(20);

    public static final Animation ANIMATION_DIGNEST = Animation.create(45);

    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_queen_desert");

    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_queen_jungle");

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_queen.png");

    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_queen.png");

    private static final EntityDataAccessor<Boolean> HASMADEHOME = SynchedEntityData.defineId(EntityMyrmexQueen.class, EntityDataSerializers.BOOLEAN);

    private int eggTicks = 0;

    public EntityMyrmexQueen(EntityType<EntityMyrmexQueen> t, Level worldIn) {
        super(t, worldIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getExperienceReward() {
        return 20;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(HASMADEHOME, Boolean.TRUE);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel1Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_QUEEN.get(1) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_QUEEN.get(1);
    }

    @Override
    protected VillagerTrades.ItemListing[] getLevel2Trades() {
        return this.isJungle() ? (VillagerTrades.ItemListing[]) MyrmexTrades.JUNGLE_QUEEN.get(2) : (VillagerTrades.ItemListing[]) MyrmexTrades.DESERT_QUEEN.get(2);
    }

    @Override
    public void setCustomName(Component name) {
        if (this.getHive() != null && !this.getHive().colonyName.equals(name.getContents())) {
            this.getHive().colonyName = name.getString();
        }
        super.m_6593_(name);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("EggTicks", this.eggTicks);
        tag.putBoolean("MadeHome", this.hasMadeHome());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.eggTicks = tag.getInt("EggTicks");
        this.setMadeHome(tag.getBoolean("MadeHome"));
    }

    public boolean hasMadeHome() {
        return this.f_19804_.get(HASMADEHOME);
    }

    public void setMadeHome(boolean madeHome) {
        this.f_19804_.set(HASMADEHOME, madeHome);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.getAnimation() == ANIMATION_DIGNEST) {
            this.spawnGroundEffects(3.0F);
        }
        if (this.getHive() != null) {
            this.getHive().tick(0, this.m_9236_());
        }
        if (this.hasMadeHome() && this.getGrowthStage() >= 2 && !this.canSeeSky()) {
            this.eggTicks++;
        } else if (this.canSeeSky()) {
            this.setAnimation(ANIMATION_DIGNEST);
            if (this.getAnimationTick() == 42) {
                int down = Math.max(15, this.m_20183_().m_123342_() - 20 + this.m_217043_().nextInt(10));
                BlockPos genPos = new BlockPos(this.m_146903_(), down, this.m_146907_());
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, (double) genPos.m_123341_(), (double) genPos.m_123342_(), (double) genPos.m_123343_()))) {
                    WorldGenMyrmexHive hiveGen = new WorldGenMyrmexHive(true, this.isJungle(), NoneFeatureConfiguration.CODEC);
                    if (!this.m_9236_().isClientSide && this.m_9236_() instanceof ServerLevel) {
                        hiveGen.placeSmallGen((ServerLevel) this.m_9236_(), this.m_217043_(), genPos);
                    }
                    this.setMadeHome(true);
                    this.m_7678_((double) genPos.m_123341_(), (double) down, (double) genPos.m_123343_(), 0.0F, 0.0F);
                    this.m_7292_(new MobEffectInstance(MobEffects.INVISIBILITY, 30));
                    this.setHive(hiveGen.hive);
                    for (int i = 0; i < 3; i++) {
                        EntityMyrmexWorker worker = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(), this.m_9236_());
                        worker.m_20359_(this);
                        worker.setHive(this.getHive());
                        worker.setJungleVariant(this.isJungle());
                        if (!this.m_9236_().isClientSide) {
                            this.m_9236_().m_7967_(worker);
                        }
                    }
                    return;
                }
            }
        }
        if (!this.m_9236_().isClientSide && this.eggTicks > IafConfig.myrmexPregnantTicks && this.getHive() == null || !this.m_9236_().isClientSide && this.getHive() != null && this.getHive().repopulate() && this.eggTicks > IafConfig.myrmexPregnantTicks) {
            float radius = -5.25F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos eggPos = BlockPos.containing(this.m_20185_() + extraX, this.m_20186_() + 0.75, this.m_20189_() + extraZ);
            if (this.m_9236_().m_46859_(eggPos)) {
                this.setAnimation(ANIMATION_EGG);
                if (this.getAnimationTick() == 10) {
                    EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), this.m_9236_());
                    egg.setJungle(this.isJungle());
                    int caste = getRandomCaste(this.m_9236_(), this.m_217043_(), this.getHive() == null || this.getHive().reproduces);
                    egg.setMyrmexCaste(caste);
                    egg.m_7678_(this.m_20185_() + extraX, this.m_20186_() + 0.75, this.m_20189_() + extraZ, 0.0F, 0.0F);
                    if (this.getHive() != null) {
                        egg.hiveUUID = this.getHive().hiveUUID;
                    }
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(egg);
                    }
                    this.eggTicks = 0;
                }
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.m_5448_() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.m_5448_().m_20191_())) {
                this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.m_5448_() != null && this.getAnimationTick() == 6 && this.getAttackBounds().intersects(this.m_5448_().m_20191_())) {
            LivingEntity attackTarget = this.m_5448_();
            attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * 2));
            attackTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
            attackTarget.f_19812_ = true;
            float f = Mth.sqrt(0.5F);
            attackTarget.m_20256_(attackTarget.m_20184_().multiply(0.5, 1.0, 0.5));
            attackTarget.m_20256_(attackTarget.m_20184_().add(-0.5 / (double) f * 4.0, 1.0, -0.5 / (double) f * 4.0));
            if (attackTarget.m_20096_()) {
                attackTarget.m_20256_(attackTarget.m_20184_().add(0.0, 0.4, 0.0));
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return super.m_6673_(source) || this.getAnimation() == ANIMATION_DIGNEST;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new MyrmexAITradePlayer(this));
        this.f_21345_.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.f_21345_.addGoal(1, new MyrmexAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(3, new MyrmexAIReEnterHive(this, 1.0));
        this.f_21345_.addGoal(4, new MyrmexAIWanderHiveCenter(this, 1.0));
        this.f_21345_.addGoal(5, new MyrmexQueenAIWander(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new MyrmexAIDefendHive(this));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new MyrmexAIAttackPlayers(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexQueen.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Enemy);
            }
        }));
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean isInHive() {
        if (this.getHive() != null) {
            for (BlockPos pos : this.getHive().getAllRooms()) {
                if (this.isCloseEnoughToTarget(MyrmexHive.getGroundedPos(this.m_9236_(), pos), 300.0)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 3.5).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ARMOR, 15.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 3.5);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.75F;
    }

    @Override
    public int getCasteImportance() {
        return 3;
    }

    @Override
    public boolean shouldLeaveHive() {
        return false;
    }

    @Override
    public boolean shouldEnterHive() {
        return true;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        } else if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
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
    public boolean canMove() {
        return super.canMove() && this.hasMadeHome();
    }

    public void spawnGroundEffects(float size) {
        for (int i = 0; (float) i < size * 3.0F; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float radius = size * this.f_19796_.nextFloat();
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ * 3.14F * this.f_19796_.nextFloat();
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                BlockState BlockState = this.m_9236_().getBlockState(BlockPos.containing((double) this.m_146903_() + extraX, (double) this.m_146904_() + extraY - 1.0, (double) this.m_146907_() + extraZ));
                if (BlockState.m_60795_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockState), true, this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING, ANIMATION_EGG, ANIMATION_DIGNEST };
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
        return false;
    }
}