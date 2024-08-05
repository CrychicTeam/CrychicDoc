package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AbyssalAltarBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.ai.SemiAquaticPathNavigator;
import com.github.alexmodguy.alexscaves.server.entity.ai.VerticalSwimmingMoveControl;
import com.github.alexmodguy.alexscaves.server.entity.item.InkBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class DeepOneBaseEntity extends Monster implements IAnimatedEntity {

    protected boolean isLandNavigator;

    private boolean hasSwimmingSize = false;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private Player corneringPlayer;

    private int tradingLockedTime = 0;

    private Animation currentAnimation;

    private int animationTick;

    private static final EntityDataAccessor<Boolean> SWIMMING = SynchedEntityData.defineId(DeepOneBaseEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> ALTAR_POS = SynchedEntityData.defineId(DeepOneBaseEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> SUMMONED = SynchedEntityData.defineId(DeepOneBaseEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SUMMON_TIME = SynchedEntityData.defineId(DeepOneBaseEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SOUNDS_ANGRY = SynchedEntityData.defineId(DeepOneBaseEntity.class, EntityDataSerializers.BOOLEAN);

    private UUID summonerUUID = null;

    private ItemStack swappedItem = ItemStack.EMPTY;

    private float summonedProgress = 0.0F;

    private float prevSummonedProgress = 0.0F;

    private boolean spawnedLootItem = false;

    protected DeepOneBaseEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SWIMMING, false);
        this.f_19804_.define(SUMMONED, false);
        this.f_19804_.define(SUMMON_TIME, 0);
        this.f_19804_.define(SOUNDS_ANGRY, false);
        this.f_19804_.define(ALTAR_POS, Optional.empty());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new VerticalSwimmingMoveControl(this, 0.8F, 10.0F);
            this.f_21344_ = this.createNavigation(this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    public static boolean checkDeepOneSpawnRules(EntityType entityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (!level.m_6425_(blockPos.below()).is(FluidTags.WATER)) {
            return false;
        } else {
            boolean flag = level.m_46791_() != Difficulty.PEACEFUL && m_219009_(level, blockPos, randomSource) && (mobSpawnType == MobSpawnType.SPAWNER || level.m_6425_(blockPos).is(FluidTags.WATER));
            return randomSource.nextInt(110) == 0 && blockPos.m_123342_() < level.m_5736_() - 80 && flag;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new DeepOneBaseEntity.PathNavigator(worldIn);
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.m_45784_(this);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFishPitch = this.fishPitch;
        this.prevSummonedProgress = this.summonedProgress;
        boolean water = this.m_20072_();
        if (water && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!water && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        float pitchTarget;
        if (this.isDeepOneSwimming()) {
            pitchTarget = (float) this.m_20184_().y * this.getPitchScale();
            if (!this.m_9236_().isClientSide && this.m_21573_().isDone() && this.m_20096_()) {
                this.setDeepOneSwimming(false);
            }
        } else {
            pitchTarget = 0.0F;
        }
        if (this.isSummoned()) {
            if (this.getSummonTime() > 0) {
                if (this.summonedProgress < 20.0F) {
                    if (this.summonedProgress == 0.0F) {
                        if (!this.m_9236_().isClientSide) {
                            this.m_9236_().broadcastEntityEvent(this, (byte) 61);
                        }
                        this.m_216990_(ACSoundRegistry.MAGIC_CONCH_SUMMON.get());
                    }
                    this.summonedProgress++;
                }
            } else if (this.summonedProgress > 0.0F) {
                this.summonedProgress--;
            }
            if (this.getSummonTime() > 0) {
                if (!this.m_9236_().isClientSide) {
                    this.setSummonTime(this.getSummonTime() - 1);
                }
                if (this.getSummonTime() == 0) {
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 61);
                    }
                    this.m_216990_(ACSoundRegistry.MAGIC_CONCH_SUMMON.get());
                }
            } else if (!this.m_9236_().isClientSide && this.summonedProgress <= 0.0F) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        if (!this.isSummoned() && this.summonedProgress > 0.0F) {
            this.summonedProgress--;
        }
        if (this.hasSwimmingBoundingBox()) {
            if (!this.hasSwimmingSize) {
                this.hasSwimmingSize = true;
                this.m_6210_();
            }
        } else if (this.hasSwimmingSize) {
            this.hasSwimmingSize = false;
            this.m_6210_();
        }
        if (this.tradingLockedTime > 0) {
            this.tradingLockedTime--;
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == this.getTradingAnimation() && this.m_21205_().is(ACTagRegistry.DEEP_ONE_BARTERS)) {
            BlockPos altarPos = this.getLastAltarPos();
            if (altarPos != null) {
                Vec3 center = Vec3.atCenterOf(altarPos);
                if (this.getAnimationTick() > this.getTradingAnimation().getDuration() - 10) {
                    if (this.m_9236_().getBlockEntity(altarPos) instanceof AbyssalAltarBlockEntity altar && !this.spawnedLootItem) {
                        List<ItemStack> possibles = this.generateBarterLoot();
                        ItemStack stack = possibles.isEmpty() ? ItemStack.EMPTY : (ItemStack) possibles.get(0);
                        if (altar.getItem(0).isEmpty()) {
                            altar.setItem(0, stack);
                            this.m_9236_().broadcastEntityEvent(this, (byte) 68);
                            altar.onEntityInteract(this, false);
                        } else {
                            Vec3 vec3 = center.add(0.0, 0.5, 0.0);
                            ItemEntity itemEntity = new ItemEntity(this.m_9236_(), vec3.x, vec3.y, vec3.z, stack);
                            this.m_9236_().m_7967_(itemEntity);
                        }
                        double advancementRange = 20.0;
                        for (Player player : this.m_9236_().m_45955_(TargetingConditions.forNonCombat().range(advancementRange), this, this.m_20191_().inflate(advancementRange))) {
                            if ((double) player.m_20270_(this) < advancementRange) {
                                ACAdvancementTriggerRegistry.DEEP_ONE_TRADE.triggerForEntity(player);
                            }
                        }
                        this.spawnedLootItem = true;
                    }
                    this.restoreSwappedItem();
                }
                this.m_7618_(EntityAnchorArgument.Anchor.EYES, center);
            }
        }
        if (this.spawnedLootItem && this.getAnimation() != this.getTradingAnimation()) {
            this.spawnedLootItem = false;
        }
        this.fishPitch = Mth.approachDegrees(this.fishPitch, Mth.clamp(pitchTarget, -1.4F, 1.4F) * (-180.0F / (float) Math.PI), 5.0F);
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private List<ItemStack> generateBarterLoot() {
        LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(this.getBarterLootTable());
        return loottable.getRandomItems(new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER));
    }

    protected abstract ResourceLocation getBarterLootTable();

    protected boolean hasSwimmingBoundingBox() {
        return this.isDeepOneSwimming();
    }

    private float getPitchScale() {
        return 2.0F;
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    public boolean isDeepOneSwimming() {
        return this.f_19804_.get(SWIMMING);
    }

    public void setDeepOneSwimming(boolean bool) {
        this.f_19804_.set(SWIMMING, bool);
    }

    public BlockPos getLastAltarPos() {
        return (BlockPos) this.f_19804_.get(ALTAR_POS).orElse(null);
    }

    public boolean isSummoned() {
        return this.f_19804_.get(SUMMONED);
    }

    public void setSummoned(boolean bool) {
        this.f_19804_.set(SUMMONED, bool);
    }

    private int getSummonTime() {
        return this.f_19804_.get(SUMMON_TIME);
    }

    private void setSummonTime(int i) {
        this.f_19804_.set(SUMMON_TIME, i);
    }

    public void setLastAltarPos(BlockPos lastAltarPos) {
        this.f_19804_.set(ALTAR_POS, Optional.ofNullable(lastAltarPos));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        BlockPos lastAltarPos = this.getLastAltarPos();
        if (lastAltarPos != null) {
            compound.putInt("AltarX", lastAltarPos.m_123341_());
            compound.putInt("AltarY", lastAltarPos.m_123342_());
            compound.putInt("AltarZ", lastAltarPos.m_123343_());
        }
        if (!this.swappedItem.isEmpty()) {
            compound.put("SwappedItem", this.swappedItem.save(new CompoundTag()));
        }
        compound.putBoolean("ConchSummoned", this.isSummoned());
        if (this.summonerUUID != null) {
            compound.putUUID("ConchUUID", this.summonerUUID);
            compound.putInt("ConchTime", this.getSummonTime());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.contains("AltarX") && compound.contains("AltarY") && compound.contains("AltarZ")) {
            this.setLastAltarPos(new BlockPos(compound.getInt("AltarX"), compound.getInt("AltarY"), compound.getInt("AltarZ")));
        }
        if (compound.contains("SwappedWeapon")) {
            this.swappedItem = ItemStack.of(compound.getCompound("SwappedWeapon"));
        }
        this.setSummoned(compound.getBoolean("ConchSummoned"));
        if (compound.contains("ConchUUID")) {
            this.summonerUUID = compound.getUUID("ConchUUID");
            this.setSummonTime(compound.getInt("ConchTime"));
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 61) {
            this.m_9236_().addParticle(ACParticleRegistry.BIG_SPLASH.get(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), (double) (this.m_20205_() + 0.2F), 3.0, 0.0);
        } else if (b == 68) {
            BlockPos pos = this.getLastAltarPos();
            if (pos != null && this.m_9236_().getBlockEntity(pos) instanceof AbyssalAltarBlockEntity altarBlockEntity) {
                altarBlockEntity.onEntityInteract(this, false);
            }
        } else if (b == 69) {
            BlockPos pos = this.getLastAltarPos();
            if (pos != null && this.m_9236_().getBlockEntity(pos) instanceof AbyssalAltarBlockEntity altarBlockEntity) {
                altarBlockEntity.onEntityInteract(this, true);
                altarBlockEntity.setItem(0, ItemStack.EMPTY);
            }
        } else {
            super.m_7822_(b);
        }
    }

    public void setSummonedBy(LivingEntity player, int time) {
        this.setSummoned(true);
        this.summonerUUID = player.m_20148_();
        this.setSummonTime(time);
    }

    @Override
    public boolean isEffectiveAi() {
        return super.m_21515_() && (!this.isSummoned() || this.summonedProgress >= 20.0F);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            if (Double.isNaN(delta.y)) {
                delta = new Vec3(delta.x, 0.0, delta.z);
            }
            if (this.sinksWhenNotSwimming() && !this.isDeepOneSwimming()) {
                delta = delta.scale(0.8);
                if (!this.f_20899_ && !this.f_19862_) {
                    delta = delta.add(0.0, -0.05F, 0.0);
                } else {
                    delta = delta.add(0.0, 0.1F, 0.0);
                }
            }
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(0.8));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isDeepOneSwimming();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isDeepOneSwimming() ? this.getSwimmingSize() : super.m_6972_(poseIn);
    }

    public DeepOneReaction getReactionTo(Player player) {
        return this.isSummoned() && this.summonerUUID != null && this.summonerUUID.equals(player.m_20148_()) ? DeepOneReaction.HELPFUL : DeepOneReaction.fromReputation(this.getReputationOf(player.m_20148_()));
    }

    public int getReputationOf(UUID playerUUID) {
        if (!this.m_9236_().isClientSide) {
            ACWorldData worldData = ACWorldData.get(this.m_9236_());
            return worldData == null ? 0 : worldData.getDeepOneReputation(playerUUID);
        } else {
            return 0;
        }
    }

    public void setReputationOf(UUID playerUUID, int amount) {
        if (!this.m_9236_().isClientSide) {
            ACWorldData worldData = ACWorldData.get(this.m_9236_());
            if (worldData != null) {
                worldData.setDeepOneReputation(playerUUID, amount);
            }
        }
    }

    public void addReputation(UUID playerUUID, int amount) {
        int prev = this.getReputationOf(playerUUID);
        DeepOneReaction newReaction = DeepOneReaction.fromReputation(amount + prev);
        this.setReputationOf(playerUUID, amount + prev);
        if (DeepOneReaction.fromReputation(prev) != newReaction) {
            Player player = this.m_9236_().m_46003_(playerUUID);
            if (player != null) {
                if (newReaction == DeepOneReaction.NEUTRAL) {
                    ACAdvancementTriggerRegistry.DEEP_ONE_NEUTRAL.triggerForEntity(player);
                }
                if (newReaction == DeepOneReaction.HELPFUL) {
                    ACAdvancementTriggerRegistry.DEEP_ONE_HELPFUL.triggerForEntity(player);
                }
                player.displayClientMessage(Component.translatable("entity.alexscaves.deep_one.reaction_" + newReaction.toString().toLowerCase(Locale.ROOT)), true);
            }
        }
    }

    public void setTradingLockedTime(int i) {
        this.tradingLockedTime = Math.max(i, this.tradingLockedTime);
    }

    public boolean isTradingLocked() {
        return this.tradingLockedTime > 0;
    }

    public EntityDimensions getSwimmingSize() {
        return this.m_6095_().getDimensions().scale(this.m_6134_());
    }

    protected boolean sinksWhenNotSwimming() {
        return true;
    }

    public void setCorneredBy(Player player) {
        this.corneringPlayer = player;
    }

    public Player getCorneringPlayer() {
        return this.corneringPlayer;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        if (this.isDeepOneSwimming()) {
            float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
            float f2 = Math.min(f1 * 6.0F, 1.0F);
            this.f_267362_.update(f2, 0.4F);
        } else {
            super.m_267651_(flying);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageValue) {
        boolean sup = super.m_6469_(damageSource, damageValue);
        if (sup && damageSource.getEntity() instanceof Player player && !this.m_9236_().isClientSide && !damageSource.is(ACTagRegistry.DEEP_ONE_IGNORES)) {
            int decrease = -5;
            if (!this.m_6084_()) {
                decrease = -15;
            }
            this.addReputation(player.m_20148_(), decrease);
        }
        return sup;
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

    public boolean startDisappearBehavior(Player player) {
        InkBombEntity inkBombEntity = new InkBombEntity(this.m_9236_(), this);
        Vec3 vec3 = player.m_20184_();
        double d0 = player.m_20185_() + vec3.x - this.m_20185_();
        double d1 = player.m_20188_() + vec3.y - this.m_20188_();
        double d2 = player.m_20189_() + vec3.z - this.m_20189_();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        inkBombEntity.m_6686_(d0, d1, d2, 0.5F + 0.2F * (float) d3, 12.0F);
        this.m_9236_().playSound((Player) null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_THROW, this.m_5720_(), 1.0F, 0.8F + this.m_217043_().nextFloat() * 0.4F);
        this.m_9236_().m_7967_(inkBombEntity);
        this.addReputation(player.m_20148_(), -1);
        return true;
    }

    public void startAttackBehavior(LivingEntity target) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    protected void checkAndDealMeleeDamage(LivingEntity target, float multiplier) {
        this.checkAndDealMeleeDamage(target, multiplier, 0.25F);
    }

    protected void checkAndDealMeleeDamage(LivingEntity target, float multiplier, float knockback) {
        if (this.m_142582_(target) && (double) this.m_20270_(target) < (double) (this.m_20205_() + target.m_20205_()) + 5.0) {
            float f = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * multiplier;
            target.hurt(this.m_269291_().mobAttack(this), f);
            target.knockback((double) (knockback * multiplier), this.m_20185_() - target.m_20185_(), this.m_20189_() - target.m_20189_());
            Entity entity = target.m_20202_();
            if (entity != null) {
                entity.setDeltaMovement(target.m_20184_());
                entity.hurt(this.m_269291_().mobAttack(this), f);
            }
        }
    }

    public abstract Animation getTradingAnimation();

    public boolean soundsAngry() {
        return this.f_19804_.get(SOUNDS_ANGRY);
    }

    public void setSoundsAngry(boolean angrySounding) {
        this.f_19804_.set(SOUNDS_ANGRY, angrySounding);
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof DeepOneBaseEntity) {
            return true;
        } else {
            return !(entityIn instanceof Player) ? super.m_7307_(entityIn) : this.getReactionTo((Player) entityIn) == DeepOneReaction.HELPFUL || super.m_7307_(entityIn);
        }
    }

    public void swapItemsForAnimation(ItemStack item) {
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.swappedItem = this.m_21120_(InteractionHand.MAIN_HAND).copy();
        }
        this.m_21008_(InteractionHand.MAIN_HAND, item);
    }

    public void restoreSwappedItem() {
        this.m_21008_(InteractionHand.MAIN_HAND, this.swappedItem);
    }

    public float getSummonProgress(float partialTicks) {
        return (this.prevSummonedProgress + (this.summonedProgress - this.prevSummonedProgress) * partialTicks) / 20.0F;
    }

    public void copyTarget(LivingEntity player) {
        LivingEntity priorTarget = this.m_5448_();
        if (priorTarget == null || !priorTarget.isAlive()) {
            LivingEntity target = null;
            if (player.getLastHurtMob() != null) {
                target = player.getLastHurtMob();
            } else if (player.getLastHurtByMob() != null) {
                target = player.getLastHurtByMob();
            }
            if (target != null && target.isAlive() && !target.m_7307_(player) && !target.m_7306_(player) && !target.m_7307_(this) && !(target instanceof DeepOneBaseEntity)) {
                this.m_6710_(target);
            }
        }
    }

    public abstract SoundEvent getAdmireSound();

    public class HurtByHostileTargetGoal extends HurtByTargetGoal {

        public HurtByHostileTargetGoal() {
            super(DeepOneBaseEntity.this, DeepOneBaseEntity.class);
            this.m_26044_(new Class[0]);
        }

        @Override
        protected boolean canAttack(@Nullable LivingEntity target, TargetingConditions conditions) {
            if (target instanceof Player player && DeepOneBaseEntity.this.getReactionTo(player) == DeepOneReaction.HELPFUL) {
                return false;
            }
            return super.m_26150_(target, conditions);
        }

        @Override
        protected void alertOthers() {
            double d0 = this.m_7623_();
            AABB aabb = AABB.unitCubeFromLowerCorner(this.f_26135_.m_20182_()).inflate(d0, 10.0, d0);
            for (Mob mob : this.f_26135_.m_9236_().m_6443_(DeepOneBaseEntity.class, aabb, EntitySelector.NO_SPECTATORS)) {
                if (this.f_26135_ != mob && mob.getTarget() == null && (!(this.f_26135_ instanceof TamableAnimal) || ((TamableAnimal) this.f_26135_).m_269323_() == ((TamableAnimal) mob).m_269323_()) && !mob.m_7307_(this.f_26135_.m_21188_())) {
                    boolean flag = false;
                    if (DeepOneBaseEntity.class.isAssignableFrom(mob.getClass())) {
                        flag = true;
                    } else if (flag) {
                        continue;
                    }
                    this.m_5766_(mob, this.f_26135_.m_21188_());
                }
            }
        }
    }

    private class PathNavigator extends SemiAquaticPathNavigator {

        public PathNavigator(Level worldIn) {
            super(DeepOneBaseEntity.this, worldIn);
        }

        @Override
        protected Vec3 getTempMobPos() {
            return new Vec3(this.f_26494_.m_20185_(), this.f_26494_.m_20227_(0.5), this.f_26494_.m_20189_());
        }

        @Override
        protected double getGroundY(Vec3 vec3) {
            if (!DeepOneBaseEntity.this.isDeepOneSwimming() && DeepOneBaseEntity.this.m_20072_()) {
                BlockPos blockpos = BlockPos.containing(vec3);
                return this.f_26495_.getFluidState(blockpos.below()).isEmpty() ? vec3.y : WalkNodeEvaluator.getFloorLevel(this.f_26495_, blockpos);
            } else {
                return super.m_183345_(vec3);
            }
        }
    }
}