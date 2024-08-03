package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EnderMan extends Monster implements NeutralMob {

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");

    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.15F, AttributeModifier.Operation.ADDITION);

    private static final int DELAY_BETWEEN_CREEPY_STARE_SOUND = 400;

    private static final int MIN_DEAGGRESSION_TIME = 600;

    private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);

    private int lastStareSound = Integer.MIN_VALUE;

    private int targetChangeTime;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;

    @Nullable
    private UUID persistentAngerTarget;

    public EnderMan(EntityType<? extends EnderMan> entityTypeExtendsEnderMan0, Level level1) {
        super(entityTypeExtendsEnderMan0, level1);
        this.m_274367_(1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EnderMan.EndermanFreezeWhenLookedAt(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(10, new EnderMan.EndermanLeaveBlockGoal(this));
        this.f_21345_.addGoal(11, new EnderMan.EndermanTakeBlockGoal(this));
        this.f_21346_.addGoal(1, new EnderMan.EndermanLookForPlayerGoal(this, this::m_21674_));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Endermite.class, true, false));
        this.f_21346_.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity0) {
        super.m_6710_(livingEntity0);
        AttributeInstance $$1 = this.m_21051_(Attributes.MOVEMENT_SPEED);
        if (livingEntity0 == null) {
            this.targetChangeTime = 0;
            this.f_19804_.set(DATA_CREEPY, false);
            this.f_19804_.set(DATA_STARED_AT, false);
            $$1.removeModifier(SPEED_MODIFIER_ATTACKING);
        } else {
            this.targetChangeTime = this.f_19797_;
            this.f_19804_.set(DATA_CREEPY, true);
            if (!$$1.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                $$1.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_CARRY_STATE, Optional.empty());
        this.f_19804_.define(DATA_CREEPY, false);
        this.f_19804_.define(DATA_STARED_AT, false);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    public void setRemainingPersistentAngerTime(int int0) {
        this.remainingPersistentAngerTime = int0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID0) {
        this.persistentAngerTarget = uUID0;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void playStareSound() {
        if (this.f_19797_ >= this.lastStareSound + 400) {
            this.lastStareSound = this.f_19797_;
            if (!this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20188_(), this.m_20189_(), SoundEvents.ENDERMAN_STARE, this.m_5720_(), 2.5F, 1.0F, false);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_CREEPY.equals(entityDataAccessor0) && this.hasBeenStaredAt() && this.m_9236_().isClientSide) {
            this.playStareSound();
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        BlockState $$1 = this.getCarriedBlock();
        if ($$1 != null) {
            compoundTag0.put("carriedBlockState", NbtUtils.writeBlockState($$1));
        }
        this.m_21678_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        BlockState $$1 = null;
        if (compoundTag0.contains("carriedBlockState", 10)) {
            $$1 = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compoundTag0.getCompound("carriedBlockState"));
            if ($$1.m_60795_()) {
                $$1 = null;
            }
        }
        this.setCarriedBlock($$1);
        this.m_147285_(this.m_9236_(), compoundTag0);
    }

    boolean isLookingAtMe(Player player0) {
        ItemStack $$1 = player0.getInventory().armor.get(3);
        if ($$1.is(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        } else {
            Vec3 $$2 = player0.m_20252_(1.0F).normalize();
            Vec3 $$3 = new Vec3(this.m_20185_() - player0.m_20185_(), this.m_20188_() - player0.m_20188_(), this.m_20189_() - player0.m_20189_());
            double $$4 = $$3.length();
            $$3 = $$3.normalize();
            double $$5 = $$2.dot($$3);
            return $$5 > 1.0 - 0.025 / $$4 ? player0.m_142582_(this) : false;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 2.55F;
    }

    @Override
    public void aiStep() {
        if (this.m_9236_().isClientSide) {
            for (int $$0 = 0; $$0 < 2; $$0++) {
                this.m_9236_().addParticle(ParticleTypes.PORTAL, this.m_20208_(0.5), this.m_20187_() - 0.25, this.m_20262_(0.5), (this.f_19796_.nextDouble() - 0.5) * 2.0, -this.f_19796_.nextDouble(), (this.f_19796_.nextDouble() - 0.5) * 2.0);
            }
        }
        this.f_20899_ = false;
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), true);
        }
        super.aiStep();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        if (this.m_9236_().isDay() && this.f_19797_ >= this.targetChangeTime + 600) {
            float $$0 = this.m_213856_();
            if ($$0 > 0.5F && this.m_9236_().m_45527_(this.m_20183_()) && this.f_19796_.nextFloat() * 30.0F < ($$0 - 0.4F) * 2.0F) {
                this.setTarget(null);
                this.teleport();
            }
        }
        super.m_8024_();
    }

    protected boolean teleport() {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            double $$0 = this.m_20185_() + (this.f_19796_.nextDouble() - 0.5) * 64.0;
            double $$1 = this.m_20186_() + (double) (this.f_19796_.nextInt(64) - 32);
            double $$2 = this.m_20189_() + (this.f_19796_.nextDouble() - 0.5) * 64.0;
            return this.teleport($$0, $$1, $$2);
        } else {
            return false;
        }
    }

    boolean teleportTowards(Entity entity0) {
        Vec3 $$1 = new Vec3(this.m_20185_() - entity0.getX(), this.m_20227_(0.5) - entity0.getEyeY(), this.m_20189_() - entity0.getZ());
        $$1 = $$1.normalize();
        double $$2 = 16.0;
        double $$3 = this.m_20185_() + (this.f_19796_.nextDouble() - 0.5) * 8.0 - $$1.x * 16.0;
        double $$4 = this.m_20186_() + (double) (this.f_19796_.nextInt(16) - 8) - $$1.y * 16.0;
        double $$5 = this.m_20189_() + (this.f_19796_.nextDouble() - 0.5) * 8.0 - $$1.z * 16.0;
        return this.teleport($$3, $$4, $$5);
    }

    private boolean teleport(double double0, double double1, double double2) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos(double0, double1, double2);
        while ($$3.m_123342_() > this.m_9236_().m_141937_() && !this.m_9236_().getBlockState($$3).m_280555_()) {
            $$3.move(Direction.DOWN);
        }
        BlockState $$4 = this.m_9236_().getBlockState($$3);
        boolean $$5 = $$4.m_280555_();
        boolean $$6 = $$4.m_60819_().is(FluidTags.WATER);
        if ($$5 && !$$6) {
            Vec3 $$7 = this.m_20182_();
            boolean $$8 = this.m_20984_(double0, double1, double2, true);
            if ($$8) {
                this.m_9236_().m_214171_(GameEvent.TELEPORT, $$7, GameEvent.Context.of(this));
                if (!this.m_20067_()) {
                    this.m_9236_().playSound(null, this.f_19854_, this.f_19855_, this.f_19856_, SoundEvents.ENDERMAN_TELEPORT, this.m_5720_(), 1.0F, 1.0F);
                    this.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return $$8;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        BlockState $$3 = this.getCarriedBlock();
        if ($$3 != null) {
            ItemStack $$4 = new ItemStack(Items.DIAMOND_AXE);
            $$4.enchant(Enchantments.SILK_TOUCH, 1);
            LootParams.Builder $$5 = new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.ORIGIN, this.m_20182_()).withParameter(LootContextParams.TOOL, $$4).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
            for (ItemStack $$7 : $$3.m_287290_($$5)) {
                this.m_19983_($$7);
            }
        }
    }

    public void setCarriedBlock(@Nullable BlockState blockState0) {
        this.f_19804_.set(DATA_CARRY_STATE, Optional.ofNullable(blockState0));
    }

    @Nullable
    public BlockState getCarriedBlock() {
        return (BlockState) this.f_19804_.get(DATA_CARRY_STATE).orElse(null);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            boolean $$2 = damageSource0.getDirectEntity() instanceof ThrownPotion;
            if (!damageSource0.is(DamageTypeTags.IS_PROJECTILE) && !$$2) {
                boolean $$5 = super.m_6469_(damageSource0, float1);
                if (!this.m_9236_().isClientSide() && !(damageSource0.getEntity() instanceof LivingEntity) && this.f_19796_.nextInt(10) != 0) {
                    this.teleport();
                }
                return $$5;
            } else {
                boolean $$3 = $$2 && this.hurtWithCleanWater(damageSource0, (ThrownPotion) damageSource0.getDirectEntity(), float1);
                for (int $$4 = 0; $$4 < 64; $$4++) {
                    if (this.teleport()) {
                        return true;
                    }
                }
                return $$3;
            }
        }
    }

    private boolean hurtWithCleanWater(DamageSource damageSource0, ThrownPotion thrownPotion1, float float2) {
        ItemStack $$3 = thrownPotion1.m_7846_();
        Potion $$4 = PotionUtils.getPotion($$3);
        List<MobEffectInstance> $$5 = PotionUtils.getMobEffects($$3);
        boolean $$6 = $$4 == Potions.WATER && $$5.isEmpty();
        return $$6 ? super.m_6469_(damageSource0, float2) : false;
    }

    public boolean isCreepy() {
        return this.f_19804_.get(DATA_CREEPY);
    }

    public boolean hasBeenStaredAt() {
        return this.f_19804_.get(DATA_STARED_AT);
    }

    public void setBeingStaredAt() {
        this.f_19804_.set(DATA_STARED_AT, true);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.getCarriedBlock() != null;
    }

    static class EndermanFreezeWhenLookedAt extends Goal {

        private final EnderMan enderman;

        @Nullable
        private LivingEntity target;

        public EndermanFreezeWhenLookedAt(EnderMan enderMan0) {
            this.enderman = enderMan0;
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.target = this.enderman.m_5448_();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double $$0 = this.target.m_20280_(this.enderman);
                return $$0 > 256.0 ? false : this.enderman.isLookingAtMe((Player) this.target);
            }
        }

        @Override
        public void start() {
            this.enderman.m_21573_().stop();
        }

        @Override
        public void tick() {
            this.enderman.m_21563_().setLookAt(this.target.m_20185_(), this.target.m_20188_(), this.target.m_20189_());
        }
    }

    static class EndermanLeaveBlockGoal extends Goal {

        private final EnderMan enderman;

        public EndermanLeaveBlockGoal(EnderMan enderMan0) {
            this.enderman = enderMan0;
        }

        @Override
        public boolean canUse() {
            if (this.enderman.getCarriedBlock() == null) {
                return false;
            } else {
                return !this.enderman.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? false : this.enderman.m_217043_().nextInt(m_186073_(2000)) == 0;
            }
        }

        @Override
        public void tick() {
            RandomSource $$0 = this.enderman.m_217043_();
            Level $$1 = this.enderman.m_9236_();
            int $$2 = Mth.floor(this.enderman.m_20185_() - 1.0 + $$0.nextDouble() * 2.0);
            int $$3 = Mth.floor(this.enderman.m_20186_() + $$0.nextDouble() * 2.0);
            int $$4 = Mth.floor(this.enderman.m_20189_() - 1.0 + $$0.nextDouble() * 2.0);
            BlockPos $$5 = new BlockPos($$2, $$3, $$4);
            BlockState $$6 = $$1.getBlockState($$5);
            BlockPos $$7 = $$5.below();
            BlockState $$8 = $$1.getBlockState($$7);
            BlockState $$9 = this.enderman.getCarriedBlock();
            if ($$9 != null) {
                $$9 = Block.updateFromNeighbourShapes($$9, this.enderman.m_9236_(), $$5);
                if (this.canPlaceBlock($$1, $$5, $$9, $$6, $$8, $$7)) {
                    $$1.setBlock($$5, $$9, 3);
                    $$1.m_220407_(GameEvent.BLOCK_PLACE, $$5, GameEvent.Context.of(this.enderman, $$9));
                    this.enderman.setCarriedBlock(null);
                }
            }
        }

        private boolean canPlaceBlock(Level level0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3, BlockState blockState4, BlockPos blockPos5) {
            return blockState3.m_60795_() && !blockState4.m_60795_() && !blockState4.m_60713_(Blocks.BEDROCK) && blockState4.m_60838_(level0, blockPos5) && blockState2.m_60710_(level0, blockPos1) && level0.m_45933_(this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(blockPos1))).isEmpty();
        }
    }

    static class EndermanLookForPlayerGoal extends NearestAttackableTargetGoal<Player> {

        private final EnderMan enderman;

        @Nullable
        private Player pendingTarget;

        private int aggroTime;

        private int teleportTime;

        private final TargetingConditions startAggroTargetConditions;

        private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

        private final Predicate<LivingEntity> isAngerInducing;

        public EndermanLookForPlayerGoal(EnderMan enderMan0, @Nullable Predicate<LivingEntity> predicateLivingEntity1) {
            super(enderMan0, Player.class, 10, false, false, predicateLivingEntity1);
            this.enderman = enderMan0;
            this.isAngerInducing = p_269940_ -> (enderMan0.isLookingAtMe((Player) p_269940_) || enderMan0.m_21674_(p_269940_)) && !enderMan0.m_20367_(p_269940_);
            this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.m_7623_()).selector(this.isAngerInducing);
        }

        @Override
        public boolean canUse() {
            this.pendingTarget = this.enderman.m_9236_().m_45946_(this.startAggroTargetConditions, this.enderman);
            return this.pendingTarget != null;
        }

        @Override
        public void start() {
            this.aggroTime = this.m_183277_(5);
            this.teleportTime = 0;
            this.enderman.setBeingStaredAt();
        }

        @Override
        public void stop() {
            this.pendingTarget = null;
            super.m_8041_();
        }

        @Override
        public boolean canContinueToUse() {
            if (this.pendingTarget != null) {
                if (!this.isAngerInducing.test(this.pendingTarget)) {
                    return false;
                } else {
                    this.enderman.m_21391_(this.pendingTarget, 10.0F, 10.0F);
                    return true;
                }
            } else {
                if (this.f_26050_ != null) {
                    if (this.enderman.m_20367_(this.f_26050_)) {
                        return false;
                    }
                    if (this.continueAggroTargetConditions.test(this.enderman, this.f_26050_)) {
                        return true;
                    }
                }
                return super.m_8045_();
            }
        }

        @Override
        public void tick() {
            if (this.enderman.m_5448_() == null) {
                super.setTarget(null);
            }
            if (this.pendingTarget != null) {
                if (--this.aggroTime <= 0) {
                    this.f_26050_ = this.pendingTarget;
                    this.pendingTarget = null;
                    super.start();
                }
            } else {
                if (this.f_26050_ != null && !this.enderman.m_20159_()) {
                    if (this.enderman.isLookingAtMe((Player) this.f_26050_)) {
                        if (this.f_26050_.m_20280_(this.enderman) < 16.0) {
                            this.enderman.teleport();
                        }
                        this.teleportTime = 0;
                    } else if (this.f_26050_.m_20280_(this.enderman) > 256.0 && this.teleportTime++ >= this.m_183277_(30) && this.enderman.teleportTowards(this.f_26050_)) {
                        this.teleportTime = 0;
                    }
                }
                super.m_8037_();
            }
        }
    }

    static class EndermanTakeBlockGoal extends Goal {

        private final EnderMan enderman;

        public EndermanTakeBlockGoal(EnderMan enderMan0) {
            this.enderman = enderMan0;
        }

        @Override
        public boolean canUse() {
            if (this.enderman.getCarriedBlock() != null) {
                return false;
            } else {
                return !this.enderman.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? false : this.enderman.m_217043_().nextInt(m_186073_(20)) == 0;
            }
        }

        @Override
        public void tick() {
            RandomSource $$0 = this.enderman.m_217043_();
            Level $$1 = this.enderman.m_9236_();
            int $$2 = Mth.floor(this.enderman.m_20185_() - 2.0 + $$0.nextDouble() * 4.0);
            int $$3 = Mth.floor(this.enderman.m_20186_() + $$0.nextDouble() * 3.0);
            int $$4 = Mth.floor(this.enderman.m_20189_() - 2.0 + $$0.nextDouble() * 4.0);
            BlockPos $$5 = new BlockPos($$2, $$3, $$4);
            BlockState $$6 = $$1.getBlockState($$5);
            Vec3 $$7 = new Vec3((double) this.enderman.m_146903_() + 0.5, (double) $$3 + 0.5, (double) this.enderman.m_146907_() + 0.5);
            Vec3 $$8 = new Vec3((double) $$2 + 0.5, (double) $$3 + 0.5, (double) $$4 + 0.5);
            BlockHitResult $$9 = $$1.m_45547_(new ClipContext($$7, $$8, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.enderman));
            boolean $$10 = $$9.getBlockPos().equals($$5);
            if ($$6.m_204336_(BlockTags.ENDERMAN_HOLDABLE) && $$10) {
                $$1.removeBlock($$5, false);
                $$1.m_220407_(GameEvent.BLOCK_DESTROY, $$5, GameEvent.Context.of(this.enderman, $$6));
                this.enderman.setCarriedBlock($$6.m_60734_().defaultBlockState());
            }
        }
    }
}