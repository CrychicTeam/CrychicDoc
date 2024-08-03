package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class Silverfish extends Monster {

    @Nullable
    private Silverfish.SilverfishWakeUpFriendsGoal friendsGoal;

    public Silverfish(EntityType<? extends Silverfish> entityTypeExtendsSilverfish0, Level level1) {
        super(entityTypeExtendsSilverfish0, level1);
    }

    @Override
    protected void registerGoals() {
        this.friendsGoal = new Silverfish.SilverfishWakeUpFriendsGoal(this);
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.m_9236_()));
        this.f_21345_.addGoal(3, this.friendsGoal);
        this.f_21345_.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(5, new Silverfish.SilverfishMergeWithStoneGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.13F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            if ((damageSource0.getEntity() != null || damageSource0.is(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)) && this.friendsGoal != null) {
                this.friendsGoal.notifyHurt();
            }
            return super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    public void tick() {
        this.f_20883_ = this.m_146908_();
        super.m_8119_();
    }

    @Override
    public void setYBodyRot(float float0) {
        this.m_146922_(float0);
        super.m_5618_(float0);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return InfestedBlock.isCompatibleHostBlock(levelReader1.m_8055_(blockPos0.below())) ? 10.0F : super.getWalkTargetValue(blockPos0, levelReader1);
    }

    public static boolean checkSilverfishSpawnRules(EntityType<Silverfish> entityTypeSilverfish0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        if (m_219019_(entityTypeSilverfish0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4)) {
            Player $$5 = levelAccessor1.m_45924_((double) blockPos3.m_123341_() + 0.5, (double) blockPos3.m_123342_() + 0.5, (double) blockPos3.m_123343_() + 0.5, 5.0, true);
            return $$5 == null;
        } else {
            return false;
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    static class SilverfishMergeWithStoneGoal extends RandomStrollGoal {

        @Nullable
        private Direction selectedDirection;

        private boolean doMerge;

        public SilverfishMergeWithStoneGoal(Silverfish silverfish0) {
            super(silverfish0, 1.0, 10);
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.f_25725_.m_5448_() != null) {
                return false;
            } else if (!this.f_25725_.m_21573_().isDone()) {
                return false;
            } else {
                RandomSource $$0 = this.f_25725_.m_217043_();
                if (this.f_25725_.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && $$0.nextInt(m_186073_(10)) == 0) {
                    this.selectedDirection = Direction.getRandom($$0);
                    BlockPos $$1 = BlockPos.containing(this.f_25725_.m_20185_(), this.f_25725_.m_20186_() + 0.5, this.f_25725_.m_20189_()).relative(this.selectedDirection);
                    BlockState $$2 = this.f_25725_.m_9236_().getBlockState($$1);
                    if (InfestedBlock.isCompatibleHostBlock($$2)) {
                        this.doMerge = true;
                        return true;
                    }
                }
                this.doMerge = false;
                return super.canUse();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.doMerge ? false : super.canContinueToUse();
        }

        @Override
        public void start() {
            if (!this.doMerge) {
                super.start();
            } else {
                LevelAccessor $$0 = this.f_25725_.m_9236_();
                BlockPos $$1 = BlockPos.containing(this.f_25725_.m_20185_(), this.f_25725_.m_20186_() + 0.5, this.f_25725_.m_20189_()).relative(this.selectedDirection);
                BlockState $$2 = $$0.m_8055_($$1);
                if (InfestedBlock.isCompatibleHostBlock($$2)) {
                    $$0.m_7731_($$1, InfestedBlock.infestedStateByHost($$2), 3);
                    this.f_25725_.m_21373_();
                    this.f_25725_.m_146870_();
                }
            }
        }
    }

    static class SilverfishWakeUpFriendsGoal extends Goal {

        private final Silverfish silverfish;

        private int lookForFriends;

        public SilverfishWakeUpFriendsGoal(Silverfish silverfish0) {
            this.silverfish = silverfish0;
        }

        public void notifyHurt() {
            if (this.lookForFriends == 0) {
                this.lookForFriends = this.m_183277_(20);
            }
        }

        @Override
        public boolean canUse() {
            return this.lookForFriends > 0;
        }

        @Override
        public void tick() {
            this.lookForFriends--;
            if (this.lookForFriends <= 0) {
                Level $$0 = this.silverfish.m_9236_();
                RandomSource $$1 = this.silverfish.m_217043_();
                BlockPos $$2 = this.silverfish.m_20183_();
                for (int $$3 = 0; $$3 <= 5 && $$3 >= -5; $$3 = ($$3 <= 0 ? 1 : 0) - $$3) {
                    for (int $$4 = 0; $$4 <= 10 && $$4 >= -10; $$4 = ($$4 <= 0 ? 1 : 0) - $$4) {
                        for (int $$5 = 0; $$5 <= 10 && $$5 >= -10; $$5 = ($$5 <= 0 ? 1 : 0) - $$5) {
                            BlockPos $$6 = $$2.offset($$4, $$3, $$5);
                            BlockState $$7 = $$0.getBlockState($$6);
                            Block $$8 = $$7.m_60734_();
                            if ($$8 instanceof InfestedBlock) {
                                if ($$0.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                                    $$0.m_46953_($$6, true, this.silverfish);
                                } else {
                                    $$0.setBlock($$6, ((InfestedBlock) $$8).hostStateByInfested($$0.getBlockState($$6)), 3);
                                }
                                if ($$1.nextBoolean()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}