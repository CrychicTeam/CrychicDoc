package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GrottoceratopsEatPlantsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GrottoceratopsMeleeGoal;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolverQuadruped;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GrottoceratopsEntity extends DinosaurEntity implements IAnimatedEntity {

    private static final EntityDataAccessor<Float> TAIL_SWING_ROT = SynchedEntityData.defineId(GrottoceratopsEntity.class, EntityDataSerializers.FLOAT);

    public LegSolverQuadruped legSolver = new LegSolverQuadruped(0.0F, 1.1F, 1.15F, 1.15F, 1.0F);

    private Animation currentAnimation;

    private int animationTick;

    public static final Animation ANIMATION_SPEAK_1 = Animation.create(15);

    public static final Animation ANIMATION_SPEAK_2 = Animation.create(20);

    public static final Animation ANIMATION_CHEW_FROM_GROUND = Animation.create(60);

    public static final Animation ANIMATION_CHEW = Animation.create(40);

    public static final Animation ANIMATION_MELEE_RAM = Animation.create(20);

    public static final Animation ANIMATION_MELEE_TAIL_1 = Animation.create(20);

    public static final Animation ANIMATION_MELEE_TAIL_2 = Animation.create(20);

    private float prevTailSwingRot;

    private int resetAttackerCooldown = 0;

    public GrottoceratopsEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ARMOR, 8.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TAIL_SWING_ROT, 0.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.GROTTOCERATOPS_STEP.get(), 0.7F, 0.85F);
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new GrottoceratopsMeleeGoal(this));
        this.f_21345_.addGoal(2, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(ACBlockRegistry.TREE_STAR.get()), false));
        this.f_21345_.addGoal(5, new GrottoceratopsEatPlantsGoal(this, 16));
        this.f_21345_.addGoal(6, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, GrottoceratopsEntity.class).setAlertOthers());
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (damageSource.getDirectEntity() instanceof VallumraptorEntity) {
            f *= 0.75F;
        }
        return super.m_6469_(damageSource, f);
    }

    @Override
    public void tick() {
        super.tick();
        float tailSwing = this.getTailSwingRot();
        this.prevTailSwingRot = tailSwing;
        if (this.getAnimation() != ANIMATION_MELEE_TAIL_1 && this.getAnimation() != ANIMATION_MELEE_TAIL_2) {
            if (Math.abs(tailSwing) > 0.0F) {
                this.setTailSwingRot(Mth.approachDegrees(tailSwing, 0.0F, 20.0F));
            }
            this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.m_21529_());
        } else {
            float start = this.getAnimation() == ANIMATION_MELEE_TAIL_1 ? 30.0F : -30.0F;
            float end = this.getAnimation() == ANIMATION_MELEE_TAIL_1 ? -180.0F : 180.0F;
            if (this.getAnimationTick() <= 7) {
                this.setTailSwingRot(Mth.approachDegrees(tailSwing, start, 5.0F));
            } else {
                this.setTailSwingRot(Mth.approachDegrees(tailSwing, end, 25.0F));
            }
            this.f_267362_.setSpeed(1.0F);
        }
        if ((this.getAnimation() == ANIMATION_CHEW || this.getAnimation() == ANIMATION_CHEW_FROM_GROUND) && this.getAnimationTick() > this.getAnimation().getDuration() - 1) {
            this.m_5634_(5.0F);
        }
        if (this.getAnimation() == ANIMATION_CHEW && this.getAnimationTick() == 2 || this.getAnimation() == ANIMATION_CHEW_FROM_GROUND && this.getAnimationTick() == 10) {
            this.m_216990_(ACSoundRegistry.GROTTOCERATOPS_GRAZE.get());
        }
        if (this.f_19797_ % 100 == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(2.0F);
        }
        if (this.resetAttackerCooldown > 0) {
            this.resetAttackerCooldown--;
        } else if (!this.m_9236_().isClientSide && !this.m_6162_() && (this.m_21188_() == null || !this.m_21188_().isAlive())) {
            this.m_6710_(this.m_21188_());
            this.resetAttackerCooldown = 600;
        }
        if (this.getAnimation() == ANIMATION_SPEAK_1 && this.getAnimationTick() == 5 || this.getAnimation() == ANIMATION_SPEAK_2 && this.getAnimationTick() == 2) {
            this.actuallyPlayAmbientSound();
        }
        this.legSolver.update(this, this.f_20883_ + this.getTailSwingRot(), this.m_6134_());
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private float getTailSwingRot() {
        return this.f_19804_.get(TAIL_SWING_ROT);
    }

    public float getTailSwingRot(float f) {
        return this.prevTailSwingRot + (this.getTailSwingRot() - this.prevTailSwingRot) * f;
    }

    public void setTailSwingRot(float rot) {
        this.f_19804_.set(TAIL_SWING_ROT, rot);
    }

    @Override
    public BlockState createEggBlockState() {
        return ACBlockRegistry.GROTTOCERATOPS_EGG.get().defaultBlockState();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ACEntityRegistry.GROTTOCERATOPS.get().create(level);
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
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SPEAK_1, ANIMATION_SPEAK_2, ANIMATION_CHEW_FROM_GROUND, ANIMATION_CHEW, ANIMATION_MELEE_RAM, ANIMATION_MELEE_TAIL_1, ANIMATION_MELEE_TAIL_2 };
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_SPEAK_2 : ANIMATION_SPEAK_1);
        }
    }

    public void actuallyPlayAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        float volume = this.m_6121_();
        if (this.getAnimation() == ANIMATION_SPEAK_2) {
            soundevent = ACSoundRegistry.GROTTOCERATOPS_CALL.get();
            volume++;
        }
        if (soundevent != null) {
            this.m_5496_(soundevent, volume, this.m_6100_());
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ACBlockRegistry.TREE_STAR.get().asItem());
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, flying ? this.m_20186_() - this.f_19855_ : 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void setInLove(@javax.annotation.Nullable Player player) {
        super.m_27595_(player);
        if (this.getAnimation() == null || this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_CHEW);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.GROTTOCERATOPS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.GROTTOCERATOPS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.GROTTOCERATOPS_DEATH.get();
    }

    public float getStepHeight() {
        return 1.1F;
    }
}