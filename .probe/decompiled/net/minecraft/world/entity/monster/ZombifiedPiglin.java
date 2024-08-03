package net.minecraft.world.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;

public class ZombifiedPiglin extends Zombie implements NeutralMob {

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");

    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.05, AttributeModifier.Operation.ADDITION);

    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);

    private int playFirstAngerSoundIn;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;

    @Nullable
    private UUID persistentAngerTarget;

    private static final int ALERT_RANGE_Y = 10;

    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);

    private int ticksUntilNextAlert;

    private static final float ZOMBIFIED_PIGLIN_EYE_HEIGHT = 1.79F;

    private static final float ZOMBIFIED_PIGLIN_BABY_EYE_HEIGHT_ADJUSTMENT = 0.82F;

    public ZombifiedPiglin(EntityType<? extends ZombifiedPiglin> entityTypeExtendsZombifiedPiglin0, Level level1) {
        super(entityTypeExtendsZombifiedPiglin0, level1);
        this.m_21441_(BlockPathTypes.LAVA, 8.0F);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID0) {
        this.persistentAngerTarget = uUID0;
    }

    @Override
    public double getMyRidingOffset() {
        return this.m_6162_() ? -0.05 : -0.45;
    }

    @Override
    protected void addBehaviourGoals() {
        this.f_21345_.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::m_21674_));
        this.f_21346_.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? 0.96999997F : 1.79F;
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        AttributeInstance $$0 = this.m_21051_(Attributes.MOVEMENT_SPEED);
        if (this.m_21660_()) {
            if (!this.m_6162_() && !$$0.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                $$0.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
            this.maybePlayFirstAngerSound();
        } else if ($$0.hasModifier(SPEED_MODIFIER_ATTACKING)) {
            $$0.removeModifier(SPEED_MODIFIER_ATTACKING);
        }
        this.m_21666_((ServerLevel) this.m_9236_(), true);
        if (this.m_5448_() != null) {
            this.maybeAlertOthers();
        }
        if (this.m_21660_()) {
            this.f_20889_ = this.f_19797_;
        }
        super.m_8024_();
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            this.playFirstAngerSoundIn--;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }
    }

    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            this.ticksUntilNextAlert--;
        } else {
            if (this.m_21574_().hasLineOfSight(this.m_5448_())) {
                this.alertOthers();
            }
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.f_19796_);
        }
    }

    private void alertOthers() {
        double $$0 = this.m_21133_(Attributes.FOLLOW_RANGE);
        AABB $$1 = AABB.unitCubeFromLowerCorner(this.m_20182_()).inflate($$0, 10.0, $$0);
        this.m_9236_().m_6443_(ZombifiedPiglin.class, $$1, EntitySelector.NO_SPECTATORS).stream().filter(p_34463_ -> p_34463_ != this).filter(p_289465_ -> p_289465_.m_5448_() == null).filter(p_289463_ -> !p_289463_.m_7307_(this.m_5448_())).forEach(p_289464_ -> p_289464_.setTarget(this.m_5448_()));
    }

    private void playAngerSound() {
        this.m_5496_(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, this.m_6121_() * 2.0F, this.m_6100_() * 1.8F);
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity0) {
        if (this.m_5448_() == null && livingEntity0 != null) {
            this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.f_19796_);
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.f_19796_);
        }
        if (livingEntity0 instanceof Player) {
            this.m_6598_((Player) livingEntity0);
        }
        super.m_6710_(livingEntity0);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    public static boolean checkZombifiedPiglinSpawnRules(EntityType<ZombifiedPiglin> entityTypeZombifiedPiglin0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.getDifficulty() != Difficulty.PEACEFUL && !levelAccessor1.m_8055_(blockPos3.below()).m_60713_(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return levelReader0.m_45784_(this) && !levelReader0.containsAnyLiquid(this.m_20191_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.m_21678_(compoundTag0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.m_147285_(this.m_9236_(), compoundTag0);
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
    protected SoundEvent getAmbientSound() {
        return this.m_21660_() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void randomizeReinforcementsChance() {
        this.m_21051_(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public boolean isPreventingPlayerRest(Player player0) {
        return this.m_21674_(player0);
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack0) {
        return this.m_7252_(itemStack0);
    }
}