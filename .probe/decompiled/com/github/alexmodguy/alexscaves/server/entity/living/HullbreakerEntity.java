package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalRandomlySwimGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.HullbreakerInspectMobGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.HullbreakerMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VerticalSwimmingMoveControl;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.KaijuMob;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;

public class HullbreakerEntity extends WaterAnimal implements IAnimatedEntity, KaijuMob {

    public static final Animation ANIMATION_PUZZLE = Animation.create(60);

    public static final Animation ANIMATION_BITE = Animation.create(20);

    public static final Animation ANIMATION_BASH = Animation.create(25);

    public static final Animation ANIMATION_DIE = Animation.create(50);

    private static final EntityDataAccessor<Integer> INTEREST_LEVEL = SynchedEntityData.defineId(HullbreakerEntity.class, EntityDataSerializers.INT);

    public static final Predicate<LivingEntity> GLOWING_TARGET = mob -> mob.m_20072_() && (mob.hasEffect(MobEffects.GLOWING) || mob.m_6095_().is(ACTagRegistry.GLOWING_ENTITIES) || mob.m_20159_() && mob.m_20202_() instanceof SubmarineEntity sub && sub.areLightsOn());

    public final HullbreakerPartEntity headPart;

    public final HullbreakerPartEntity tail1Part;

    public final HullbreakerPartEntity tail2Part;

    public final HullbreakerPartEntity tail3Part;

    public final HullbreakerPartEntity tail4Part;

    private final HullbreakerPartEntity[] allParts;

    private Animation currentAnimation;

    private int animationTick;

    private float landProgress;

    private float prevLandProgress;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private float pulseAmount;

    private float prevPulseAmount;

    private float[] yawBuffer = new float[128];

    private int yawPointer = -1;

    private int blockBreakCooldown = 0;

    private boolean collectedLoot = false;

    private List<ItemStack> deathItems = new ArrayList();

    public HullbreakerEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.headPart = new HullbreakerPartEntity(this, this, 3.0F, 2.0F);
        this.tail1Part = new HullbreakerPartEntity(this, this, 2.0F, 2.0F);
        this.tail2Part = new HullbreakerPartEntity(this, this.tail1Part, 2.0F, 1.5F);
        this.tail3Part = new HullbreakerPartEntity(this, this.tail2Part, 2.5F, 1.5F);
        this.tail4Part = new HullbreakerPartEntity(this, this.tail3Part, 1.5F, 1.0F);
        this.allParts = new HullbreakerPartEntity[] { this.headPart, this.tail1Part, this.tail2Part, this.tail3Part, this.tail4Part };
        this.f_21342_ = new VerticalSwimmingMoveControl(this, 0.7F, 30.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(INTEREST_LEVEL, 0);
    }

    public static boolean checkHullbreakerSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.m_6425_(pos).is(FluidTags.WATER) && pos.m_123342_() < level.m_5736_() - 25;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isMaxGroupSizeReached(int i) {
        return i >= this.getMaxSpawnClusterSize();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new HullbreakerMeleeGoal(this));
        this.f_21345_.addGoal(1, new HullbreakerInspectMobGoal(this));
        this.f_21345_.addGoal(2, new AnimalRandomlySwimGoal(this, 10, 35, 15, 1.0));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void playSwimSound(float f) {
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itementity = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), stack);
        if (itementity != null) {
            if (this.headPart != null) {
                Vec3 yOnlyViewVector = new Vec3(this.m_20252_(1.0F).x, 0.0, this.m_20252_(1.0F).z);
                Vec3 mouth = this.headPart.m_20182_().add(yOnlyViewVector.scale(-0.5)).add(0.0, 0.5, 0.0);
                itementity.m_146884_(mouth);
                itementity.m_20256_(yOnlyViewVector.add((double) (this.f_19796_.nextFloat() * 0.2F - 0.1F), (double) (this.f_19796_.nextFloat() * 0.2F - 0.1F), (double) (this.f_19796_.nextFloat() * 0.2F - 0.1F)).normalize().scale((double) (0.8F + this.m_9236_().random.nextFloat() * 0.3F)));
            }
            itementity.m_146915_(true);
            itementity.setDefaultPickUpDelay();
        }
        this.m_9236_().m_7967_(itementity);
        return itementity;
    }

    @Override
    protected void tickDeath() {
        this.f_20919_++;
        this.setAnimation(ANIMATION_DIE);
        this.m_146926_(0.0F);
        this.m_5616_(this.m_146908_());
        if (!this.m_9236_().isClientSide) {
            if (!this.collectedLoot) {
                this.populateDeathLootForHullbreaker();
            }
            if (this.getAnimation() == ANIMATION_DIE && this.getAnimationTick() > 10 && this.getAnimationTick() % 7 == 0 && this.collectedLoot && !this.deathItems.isEmpty()) {
                ItemStack randomItem = Util.getRandom(this.deathItems, this.m_217043_());
                this.spawnAtLocation(randomItem.copy());
                this.deathItems.remove(randomItem);
            }
        }
        if (this.getAnimation() == ANIMATION_DIE && this.getAnimationTick() > 45 && !this.m_9236_().isClientSide() && !this.m_213877_()) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    private void populateDeathLootForHullbreaker() {
        ResourceLocation resourcelocation = this.m_5743_();
        DamageSource damageSource = this.m_21225_();
        if (damageSource != null) {
            LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(resourcelocation);
            LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.m_20182_()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());
            if (this.f_20888_ != null) {
                lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.f_20888_).withLuck(this.f_20888_.getLuck());
            }
            LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
            loottable.getRandomItems(lootparams, this.m_287233_(), this.deathItems::add);
        }
        this.collectedLoot = true;
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean b) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.45F * dimensions.height;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 400.0).add(Attributes.ATTACK_DAMAGE, 16.0);
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        super.m_142687_(removalReason);
        if (this.allParts != null) {
            for (PartEntity part : this.allParts) {
                part.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void tick() {
        this.tickMultipart();
        super.m_8119_();
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.getHeadRotSpeed());
        this.prevLandProgress = this.landProgress;
        this.prevFishPitch = this.fishPitch;
        this.prevPulseAmount = this.pulseAmount;
        float targetFishPitch = Mth.clamp((float) this.m_20184_().y * 2.0F, -1.4F, 1.4F) * (-180.0F / (float) Math.PI);
        if (!this.m_6084_()) {
            targetFishPitch = 0.0F;
        }
        this.fishPitch = Mth.approachDegrees(this.fishPitch, targetFishPitch, 2.5F);
        boolean grounded = this.m_20096_() && !this.m_20072_();
        if (grounded && this.landProgress < 5.0F) {
            this.landProgress++;
        }
        if (!grounded && this.landProgress > 0.0F) {
            this.landProgress--;
        }
        float pulseBy = (float) this.getInterestLevel() * 0.45F;
        this.pulseAmount += pulseBy;
        if (!this.m_9236_().isClientSide) {
            double waterHeight = this.getFluidTypeHeight(ForgeMod.WATER_TYPE.get());
            if (waterHeight > 0.0 && waterHeight < (double) (this.m_20206_() - 1.0F)) {
                this.m_20256_(this.m_20184_().add(0.0, -0.05, 0.0));
            }
        }
        if (this.getAnimation() == ANIMATION_BASH && this.getAnimationTick() > 10 && this.getAnimationTick() <= 20) {
            this.breakBlock();
        }
        if (this.blockBreakCooldown > 0) {
            this.blockBreakCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    public float getLandProgress(float partialTicks) {
        return (this.prevLandProgress + (this.landProgress - this.prevLandProgress) * partialTicks) * 0.2F;
    }

    public float getPulseAmount(float partialTicks) {
        return (this.prevPulseAmount + (this.pulseAmount - this.prevPulseAmount) * partialTicks) * 0.2F;
    }

    public int getInterestLevel() {
        return this.f_19804_.get(INTEREST_LEVEL);
    }

    public void setInterestLevel(int level) {
        this.f_19804_.set(INTEREST_LEVEL, level);
    }

    @Override
    public int getHeadRotSpeed() {
        return 5;
    }

    public void breakBlock() {
        if (this.blockBreakCooldown-- <= 0) {
            boolean flag = false;
            AABB damageBox = this.headPart.m_20191_().inflate(1.2F).move(this.m_20171_(this.m_146909_(), this.m_146908_()));
            if (!this.m_9236_().isClientSide && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) && this.m_5448_() instanceof Player) {
                for (int a = (int) Math.round(damageBox.minX); a <= (int) Math.round(damageBox.maxX); a++) {
                    for (int b = (int) Math.round(damageBox.minY) - 1; b <= (int) Math.round(damageBox.maxY) + 1 && b <= 127; b++) {
                        for (int c = (int) Math.round(damageBox.minZ); c <= (int) Math.round(damageBox.maxZ); c++) {
                            BlockPos pos = new BlockPos(a, b, c);
                            BlockState state = this.m_9236_().getBlockState(pos);
                            if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && !state.m_204336_(ACTagRegistry.UNMOVEABLE) && state.m_60734_().getExplosionResistance() <= 15.0F) {
                                Block block = state.m_60734_();
                                if (block != Blocks.AIR) {
                                    this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                    flag = true;
                                    this.m_9236_().m_46961_(pos, true);
                                    if (state.m_204336_(BlockTags.ICE)) {
                                        this.m_9236_().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCooldown = 3;
            }
        }
    }

    private void tickMultipart() {
        if (this.yawPointer == -1) {
            for (int i = 0; i < this.yawBuffer.length; i++) {
                this.yawBuffer[i] = this.f_20883_;
            }
        }
        if (++this.yawPointer == this.yawBuffer.length) {
            this.yawPointer = 0;
        }
        this.yawBuffer[this.yawPointer] = this.f_20883_;
        Vec3[] avector3d = new Vec3[this.allParts.length];
        for (int j = 0; j < this.allParts.length; j++) {
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
        }
        Vec3 center = this.m_20182_().add(0.0, (double) (this.m_20206_() * 0.5F), 0.0);
        this.headPart.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, 3.5), this.fishPitch + this.m_146909_(), this.m_6080_()).add(center));
        this.tail1Part.setPosCenteredY(this.rotateOffsetVec(new Vec3(this.swimDegree(1.0F, 4.0F), 0.0, -3.5), this.fishPitch, this.getYawFromBuffer(2, 1.0F)).add(center));
        this.tail2Part.setPosCenteredY(this.rotateOffsetVec(new Vec3(this.swimDegree(1.0F, 3.0F), 0.0, -2.0), this.fishPitch, this.getYawFromBuffer(4, 1.0F)).add(this.tail1Part.centeredPosition()));
        this.tail3Part.setPosCenteredY(this.rotateOffsetVec(new Vec3(this.swimDegree(2.0F, 2.0F), 0.0, -2.65F), this.fishPitch, this.getYawFromBuffer(6, 1.0F)).add(this.tail2Part.centeredPosition()));
        this.tail4Part.setPosCenteredY(this.rotateOffsetVec(new Vec3(this.swimDegree(1.5F, 1.0F), 0.0, -3.0), this.fishPitch, this.getYawFromBuffer(8, 1.0F)).add(this.tail3Part.centeredPosition()));
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    private double swimDegree(float width, float sinOffset) {
        double move = Math.cos((double) (this.f_267362_.position() * 0.33F + sinOffset)) * (double) this.f_267362_.speed() * (double) width * 0.8F;
        double idle = Math.sin((double) (((float) this.f_19797_ + AlexsCaves.PROXY.getPartialTicks()) * 0.05F + sinOffset)) * (double) width * 0.5;
        return (move + idle * (double) (1.0F - this.f_267362_.speed())) * (double) (1.0F - this.getLandProgress(AlexsCaves.PROXY.getPartialTicks()));
    }

    private Vec3 rotateOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(-xRot * (float) (Math.PI / 180.0)).yRot(-yRot * (float) (Math.PI / 180.0));
    }

    public float getYawFromBuffer(int pointer, float partialTick) {
        int i = this.yawPointer - pointer & 127;
        int j = this.yawPointer - pointer - 1 & 127;
        float d0 = this.yawBuffer[j];
        float d1 = this.yawBuffer[i] - d0;
        return d0 + d1 * partialTick;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 3.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
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
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.65F;
        }
        return super.m_6469_(source, amount);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUZZLE, ANIMATION_BITE, ANIMATION_BASH, ANIMATION_DIE };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_20072_() ? ACSoundRegistry.HULLBREAKER_IDLE.get() : ACSoundRegistry.HULLBREAKER_LAND_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.m_20072_() ? ACSoundRegistry.HULLBREAKER_HURT.get() : ACSoundRegistry.HULLBREAKER_LAND_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.m_20072_() ? ACSoundRegistry.HULLBREAKER_DEATH.get() : ACSoundRegistry.HULLBREAKER_LAND_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return super.m_6121_() + 2.0F;
    }
}