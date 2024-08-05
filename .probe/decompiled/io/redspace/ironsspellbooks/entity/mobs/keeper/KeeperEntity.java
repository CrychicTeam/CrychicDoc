package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.AttackAnimationData;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class KeeperEntity extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {

    private final AnimationController<KeeperEntity> meleeController = new AnimationController<>(this, "keeper_animations", 0, this::predicate);

    RawAnimation animationToPlay = null;

    public KeeperEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 25;
        this.f_21365_ = this.createLookControl();
        this.f_21342_ = this.createMoveControl();
    }

    public float getStepHeight() {
        return 1.0F;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height;
    }

    public KeeperEntity(Level pLevel) {
        this(EntityRegistry.KEEPER.get(), pLevel);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(4, new KeeperAnimatedWarlockAttackGoal(this, 1.0, 10, 30, 3.5F));
        this.f_21345_.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, true, entity -> !(entity instanceof KeeperEntity)));
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl(this) {

            @Override
            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5F);
            }
        };
    }

    protected MoveControl createMoveControl() {
        return new MoveControl(this) {

            @Override
            protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
                double d0 = this.f_24975_ - this.f_24974_.m_20185_();
                double d1 = this.f_24977_ - this.f_24974_.m_20189_();
                return d0 * d0 + d1 * d1 < 0.5 ? pSourceAngle : super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * 0.25F);
            }
        };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.KEEPER_IDLE.get();
    }

    @Override
    public void playAmbientSound() {
        this.m_5496_(this.getAmbientSound(), 1.0F, (float) Mth.randomBetweenInclusive(this.m_217043_(), 5, 10) * 0.1F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundRegistry.KEEPER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.KEEPER_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return pSpawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.KEEPER_FLAMBERGE.get()));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 25.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ATTACK_KNOCKBACK, 2.0).add(Attributes.MOVEMENT_SPEED, 0.19);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof Projectile projectile) {
            pAmount *= 0.75F;
        }
        return super.m_6469_(pSource, pAmount);
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.m_5496_(SoundRegistry.KEEPER_STEP.get(), 0.25F, 1.0F);
    }

    @Override
    protected float nextStep() {
        return this.f_19788_ + 0.8F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return super.m_6673_(pSource) || pSource.is(DamageTypeTags.IS_FALL);
    }

    @Override
    public void playAnimation(String animationId) {
        try {
            KeeperEntity.AttackType attackType = KeeperEntity.AttackType.valueOf(animationId);
            this.animationToPlay = RawAnimation.begin().thenPlay(attackType.data.animationId);
        } catch (Exception var3) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationId);
        }
    }

    private PlayState predicate(AnimationState<KeeperEntity> animationEvent) {
        AnimationController<KeeperEntity> controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public boolean isAnimating() {
        return this.meleeController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
    }

    @Override
    public boolean shouldAlwaysAnimateLegs() {
        return false;
    }

    public static enum AttackType {

        Double_Slash(43, "sword_double_slash", 13, 29),
        Single_Upward(26, "sword_single_upward", 13),
        Single_Horizontal(28, "sword_single_horizontal", 12),
        Single_Horizontal_Fast(24, "sword_single_horizontal_fast", 12),
        Single_Stab(21, "sword_stab", 11),
        Lunge(76, "sword_lunge", 56, 57, 58, 59, 60, 61, 62, 63, 64);

        public final AttackAnimationData data;

        private AttackType(int lengthInTicks, String animationId, int... attackTimestamps) {
            this.data = new AttackAnimationData(lengthInTicks, animationId, attackTimestamps);
        }
    }
}